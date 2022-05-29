public class ScriptsTester {
    public static void main(String[] args) {
        try {
            System.out.println("======================");
            System.out.println("Scripts Tester");
            System.out.println("======================");
            runTester(100000, "1");
        } catch (Exception e) {
            throw new Error(e);
        }
    }
    public static void runTester(long times, String value) throws Exception {
        long time = 0;
        // Rhino
        time = System.currentTimeMillis();
        javax.script.ScriptEngineManager sem = new javax.script.ScriptEngineManager();
        javax.script.ScriptEngine e = sem.getEngineByName("rhino");
        e.eval("var x = 0; while (x < "+ times +") {x = x + "+ value +";}");
        System.out.println("Rhino: "+ (System.currentTimeMillis() - time) + "ms - " + e.get("x"));
        // BeanShell
        time = System.currentTimeMillis();
        bsh.Interpreter beanShell = new bsh.Interpreter();
        beanShell.eval("x = 0; while (x < "+ times +") { x = x + 1; }");
        System.out.println("BeanShell: "+ (System.currentTimeMillis() - time) + "ms - " + beanShell.get("x"));
        // Groovy
        time = System.currentTimeMillis();
        groovy.lang.GroovyShell groovyShell = new groovy.lang.GroovyShell();
        groovyShell.setVariable("x", new Integer(0));
        groovyShell.evaluate("while (x < "+ times +") {x = x + "+ value +";}");
        System.out.println("Groovy: "+ (System.currentTimeMillis() - time) + "ms - " + groovyShell.getVariable("x"));
        // Judo
        time = System.currentTimeMillis();
        com.judoscript.JudoEngine judo = new com.judoscript.JudoEngine();
        judo.runCode("var x = 0; while x < "+ times +" { x = x + "+ value +"; }", null, null);
        System.out.println("JudoScript: "+ (System.currentTimeMillis() - time) + "ms - " + judo.getBean("x"));
        // Jython
        time = System.currentTimeMillis();
        org.python.util.PythonInterpreter jython = new org.python.util.PythonInterpreter();
        jython.exec("x = 0\r\nwhile x < "+ times +":\r\n\tx = x + "+ value +"");
        System.out.println("Jython: "+ (System.currentTimeMillis() - time) + "ms - " + jython.get("x"));
        // JRuby
        time = System.currentTimeMillis();
        org.jruby.Ruby ruby = org.jruby.Ruby.getDefaultInstance();
        ruby.getGlobalVariables().set("x", null);
        ruby.evalScript("$x = 0\r\nwhile $x < "+ times +"\r\n$x = $x + "+ value +"\r\nend");
        System.out.println("JRuby: "+ (System.currentTimeMillis() - time) + "ms - "+ ruby.getGlobalVariables().get("$x"));
        // Caju
        time = System.currentTimeMillis();
        org.cajuscript.CajuScript caju = new org.cajuscript.CajuScript();
        caju.eval("x = 0; x < "+ times +" @ x = x + "+ value +"; @");
        System.out.println("CajuScript: "+ (System.currentTimeMillis() - time) + "ms - "+ caju.get("x"));
        // LuaJava
        time = System.currentTimeMillis();
        org.keplerproject.luajava.LuaState L = org.keplerproject.luajava.LuaStateFactory.newLuaState();
        L.openLibs();
        L.LdoString("x = 0; while x < "+ times +" do x = x + "+ value +" end");
        System.out.println("LuaJava: "+ (System.currentTimeMillis() - time) + "ms - "+ L.getLuaObject("x").getNumber());
    }
}
