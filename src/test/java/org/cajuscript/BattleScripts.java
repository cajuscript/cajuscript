package org.cajuscript;

import bsh.EvalError;
import org.codehaus.groovy.jsr223.GroovyScriptEngineFactory;
import org.graalvm.polyglot.Context;
import org.jruby.embed.jsr223.JRubyEngineFactory;
import org.junit.Assert;
import org.junit.Test;
import org.python.jsr223.PyScriptEngineFactory;

import javax.script.*;
import java.util.*;

public class BattleScripts {
    ScriptEngineManager manager = new ScriptEngineManager();
    Context.Builder graalVMContextBuilder = null;

    private static int callCounter = 0;

    public void call() {
        callCounter++;
    }
    public static void callX() {
        callCounter++;
    }

    @Test
    public void test() throws EvalError, ScriptException {
        graalVMContextBuilder = Context.newBuilder(new String[] { "js", "ruby", "python" });
        GroovyScriptEngineFactory groovyEngineFactory = new GroovyScriptEngineFactory();
        manager.registerEngineName("groovy", groovyEngineFactory);
        JRubyEngineFactory jrubyEngineFactory = new JRubyEngineFactory();
        manager.registerEngineName("jruby", jrubyEngineFactory);
        PyScriptEngineFactory pyScriptEngineFactory = new PyScriptEngineFactory();
        manager.registerEngineName("jython", pyScriptEngineFactory);
        CajuScriptEngineFactory cajuScriptEngineFactory = new CajuScriptEngineFactory();
        manager.registerEngineName("caju", cajuScriptEngineFactory);

        int times = 1;
        System.out.println("======================");
        System.out.println("Scripts Tester - "+ times +" times");
        System.out.println("======================");
        Map<String, List<Long>> finalResults = new TreeMap<>();
        List<Map<String, Long>> results = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            results.add(runTest(100000, "1"));
        }
        for (Map<String, Long> result : results) {
            for (String key : result.keySet()) {
                if (!finalResults.keySet().contains(key)) {
                    finalResults.put(key, new ArrayList<>());
                }
                finalResults.get(key).add(result.get(key));
            }
        }
        for (String key : finalResults.keySet()) {
            long sum = 0;
            for (Long time : finalResults.get(key)) {
                sum += time;
            }
            System.out.println(key +": "+ (sum / finalResults.size()) + "ms");
        }
    }
    private Map<String, Long> runTest(int times, String value) throws EvalError, ScriptException {
        Map<String, Long> result = new TreeMap<>();

        long timer = 0;

        // BeanShell
        timer = System.currentTimeMillis();
        callCounter = 0;
        bsh.Interpreter beanShell = new bsh.Interpreter();
        beanShell.set("test", this);
        beanShell.eval("x = 0; while (x < "+ times +") { test.call(); x = x + 1; }");
        Assert.assertEquals(times, callCounter);
        Assert.assertEquals(times, beanShell.get("x"));
        result.put("BeanShell", System.currentTimeMillis() - timer);

        // Groovy
        timer = System.currentTimeMillis();
        callCounter = 0;
        ScriptEngine groovy = manager.getEngineByName("groovy");
        groovy.put("test", this);
        groovy.put("x", 0);
        groovy.eval("while (x < "+ times +") {test.call(); x = x + "+ value +";}");
        Assert.assertEquals(times, callCounter);
        Assert.assertEquals(times, groovy.get("x"));
        result.put("Groovy", System.currentTimeMillis() - timer);

        // Jython
        timer = System.currentTimeMillis();
        callCounter = 0;
        ScriptEngine jython = manager.getEngineByName("jython");
        jython.put("test", this);
        jython.put("x", 0);
        jython.eval("for i in range("+ times +" + 1):\n  test.call()\n  x = i");
        Assert.assertEquals(times + 1, callCounter);
        Assert.assertEquals(times, jython.get("x"));
        result.put("Jython", System.currentTimeMillis() - timer);

        // JRuby
        timer = System.currentTimeMillis();
        callCounter = 0;
        ScriptEngine jruby = manager.getEngineByName("jruby");
        jruby.put("$test", this);
        jruby.put("$x", 0);
        jruby.eval("while $x < "+ times +"\r\n$test.call()\r\n$x = $x + "+ value +"\r\nend");
        Assert.assertEquals(times, callCounter);
        Assert.assertEquals(times, ((Long)jruby.get("$x")).intValue());
        result.put("JRuby", System.currentTimeMillis() - timer);

        // JavaScript/Nashorn
        timer = System.currentTimeMillis();
        callCounter = 0;
        ScriptEngine nashorn = manager.getEngineByName("JavaScript");
        //nashorn.put("test", this);
        nashorn.put("x", 0);
        //nashorn.eval("while (x < "+ times +") { test.call(); x = x + 1; }");
        nashorn.eval("while (x < "+ times +") { x = x + 1; }");
        //Assert.assertEquals(times, callCounter);
        Assert.assertEquals(times, nashorn.get("x"));
        result.put("JavaScript/Nashorn", System.currentTimeMillis() - timer);

        // GraalVM
        Context graalVMContext = graalVMContextBuilder.build();

        // GraalVM - JavaScript
        timer = System.currentTimeMillis();
        callCounter = 0;
        graalVMContext.getBindings("js").putMember("test", this);
        //graalVMContext.eval("js", "var x = 0; while (x < "+ times +") { test.call(); x = x + 1; }");
        graalVMContext.eval("js", "var x = 0; while (x < "+ times +") { x = x + 1; }");
        //Assert.assertEquals(times, callCounter);
        Assert.assertEquals(times, graalVMContext.getBindings("js").getMember("x").asInt());
        result.put("GraalVM - JavaScript", System.currentTimeMillis() - timer);

        // GraalVM - Ruby
        timer = System.currentTimeMillis();
        callCounter = 0;
        graalVMContext.getBindings("ruby").putMember("$test", this);
        //graalVMContext.eval("ruby", "$x = 0\r\nwhile $x < "+ times +"\r\n$test.call()\r\n$x = $x + "+ value +"\r\nend");
        graalVMContext.eval("ruby", "$x = 0\r\nwhile $x < "+ times +"\r\n$x = $x + "+ value +"\r\nend");
        //Assert.assertEquals(times, callCounter);
        Assert.assertEquals(times, graalVMContext.getBindings("ruby").getMember("$x").asInt());
        result.put("GraalVM - Ruby", System.currentTimeMillis() - timer);

        // GraalVM - Python
        timer = System.currentTimeMillis();
        callCounter = 0;
        graalVMContext.getBindings("python").putMember("test", this);
        //graalVMContext.eval("python", "x = 0\nfor i in range("+ times +" + 1):\n  test.call()\n  x = i");
        graalVMContext.eval("python", "x = 0\nfor i in range("+ times +" + 1):\n  x = i");
        //Assert.assertEquals(times, callCounter);
        Assert.assertEquals(times, graalVMContext.getBindings("python").getMember("x").asInt());
        result.put("GraalVM - Python", System.currentTimeMillis() - timer);

        // GraalVM Close
        graalVMContext.close(true);

        // Caju
        timer = System.currentTimeMillis();
        callCounter = 0;
        ScriptEngine caju = manager.getEngineByName("caju");
        caju.put("test", this);
        caju.put("x", Integer.valueOf(0));
        caju.eval("x = 0; x < "+ times +" @ test.call(); x = x + "+ value +"; @");
        Assert.assertEquals(times, callCounter);
        Assert.assertEquals(times, caju.get("x"));
        result.put("CajuScript", System.currentTimeMillis() - timer);
        long cajuCacheId = System.currentTimeMillis();
        for (int i = 1; i <= 2; i++) {
            timer = System.currentTimeMillis();
            callCounter = 0;
            caju.put("test", this);
            caju.put("x", Integer.valueOf(0));
            caju.eval("caju.cache: BattleScript"+ cajuCacheId +"; x = 0; x < " + times + " @ test.call(); x = x + " + value + "; @");
            Assert.assertEquals(times, callCounter);
            Assert.assertEquals(times, caju.get("x"));
            result.put("CajuScript Cache "+ i, System.currentTimeMillis() - timer);
        }
        long cajuCompileId = System.currentTimeMillis();
        for (int i = 1; i <= 2; i++) {
            timer = System.currentTimeMillis();
            callCounter = 0;
            caju.put("test", this);
            caju.put("x", Integer.valueOf(0));
            caju.eval("caju.compile: BattleScript"+ cajuCompileId +"; x = 0; x < " + times + " @ test.call(); x = x + " + value + "; @");
            Assert.assertEquals(times, callCounter);
            Assert.assertEquals(times, caju.get("x"));
            result.put("CajuScript Compiled "+ i, System.currentTimeMillis() - timer);
        }
        return result;
    }
}
