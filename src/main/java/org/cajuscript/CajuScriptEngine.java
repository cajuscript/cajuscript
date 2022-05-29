/*
 * CajuScriptEngine.java
 * 
 * This file is part of CajuScript.
 * 
 * CajuScript is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3, or (at your option) 
 * any later version.
 * 
 * CajuScript is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with CajuScript.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.cajuscript;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptContext;
import javax.script.ScriptException;
import javax.script.ScriptEngineFactory;
import javax.script.SimpleScriptContext;
import javax.script.ScriptEngineManager;
import java.io.Reader;
import java.util.List;
import java.util.Set;
import javax.script.Invocable;
import javax.script.SimpleBindings;

/**
 * <code>CajuScriptEngine</code> is the standard for execute scripts and
 * transports of objects between Java and CajuScript.
 * <p>
 * <blockquote>
 * 
 * <pre>
 * javax.script.ScriptEngine cajuEngine = new org.cajuscript.CajuScriptEngine();
 * </pre>
 * 
 * </blockquote>
 * </p>
 * <p>
 * Sample:
 * </p>
 * <p>
 * <blockquote>
 * 
 * <pre>
 * try {
 * 	javax.script.ScriptEngine caju = new org.cajuscript.CajuScriptEngine();
 * 	String javaHello = &quot;Java: Hello!&quot;;
 * 	caju.put(&quot;javaHello&quot;, javaHello);
 * 	String script = &quot;$java.lang;&quot;;
 * 	script += &quot;System.out.println(javaHello);&quot;;
 * 	script += &quot;cajuHello = \&quot;Caju: Hi!\&quot;;&quot;;
 * 	caju.eval(script);
 * 	System.out.println(caju.get(&quot;cajuHello&quot;));
 * } catch (Exception e) {
 * 	e.printStackTrace();
 * }
 * </pre>
 * 
 * </blockquote>
 * </p>
 * <p>
 * To execute a file:
 * </p>
 * <p>
 * <blockquote>
 * 
 * <pre>
 * try {
 *    javax.script.ScriptEngine cajuEngine = new org.cajuscript.CajuScriptEngine();
 *    java.io.FileInputStream fis = new java.io.FileInputStream(&lt;b&gt;&quot;script.cj&quot;&lt;/b&gt;);
 *    java.io.Reader reader = new java.io.InputStreamReader(fis);
 *    cajuEngine.eval(reader);
 *    reader.close();
 *    fis.close();
 * } catch (Exception e) {
 *     e.printStackTrace();
 * }
 * </pre>
 * 
 * </blockquote>
 * </p>
 * 
 * @author eduveks
 */
public class CajuScriptEngine implements ScriptEngine, Invocable {

    /**
     * Prefix of the variables created automaticaly to catch the values returned
     * from functions.
     */
    public static final String CAJU_VARS_FUNC_RETURN = CajuScript.CAJU_VARS.concat("_func_return_");
    /**
     * Prefix of the variables created automaticaly to defined the values for
     * parameters of functions.
     */
    public static final String CAJU_VARS_FUNC_PARAM = CajuScript.CAJU_VARS.concat("_func_param_");
    /**
     * Equals the ScriptContext.ENGINE_SCOPE
     */
    public static final int ENGINE_SCOPE = ScriptContext.ENGINE_SCOPE;
    /**
     * Equals the ScriptContext.GLOBAL_SCOPE
     */
    public static final int GLOBAL_SCOPE = ScriptContext.GLOBAL_SCOPE;
    private ScriptContext context;
    private InterfaceImplementor implementor;
    private CajuScript caju;

    /**
     * Create a new CajuScriptEngine.<br/> <br/> <code>
     * javax.script.ScriptEngine cajuEngine = new org.cajuscript.CajuScriptEngine();
     * </code>
     *
     * @throws javax.script.ScriptException
     *             Exception on creating a new instance.
     */
    public CajuScriptEngine() throws ScriptException {
        try {
            caju = new CajuScript();
        } catch (Exception e) {
            throw new ScriptException(e);
        }
        context = new SimpleScriptContext();
        implementor = new InterfaceImplementor(this);
    }

    /**
     * Script on CajuScript to be executed.<br/> <br/> <code>
     *     javax.script.ScriptEngine cajuEngine = new org.cajuscript.CajuScriptEngine();<br/>
     *     cajuEngine.eval("java.lang.System.out.println('Hello CajuScript!');");<br/>
     * </code>
     *
     * @param script
     *            Script on CajuScript to be executed.
     * @param context
     *            Define the context.
     * @return Script executed can return an object.
     * @throws javax.script.ScriptException
     *             Exception on execution.
     */
    public Object eval(String script, ScriptContext context)
            throws ScriptException {
        return runScript(script, context);
    }

    /**
     * Execute script in a Reader, can be files.<br/> <br/> <code>
     *    javax.script.ScriptEngine cajuEngine = new org.cajuscript.CajuScriptEngine();<br/>
     *    java.io.FileInputStream fis = new java.io.FileInputStream(<b>"script.cj"</b>);<br/>
     *    java.io.Reader reader = new java.io.InputStreamReader(fis);<br/>
     *    cajuEngine.eval(reader);<br/>
     *    reader.close();<br/>
     *    fis.close();<br/>
     * </code>
     *
     * @param reader
     *            Input of the file with the script to be executed.
     * @param context
     *            Define the context.
     * @return Script executed can return an object.
     * @throws javax.script.ScriptException
     *             Exception on execution.
     */
    public Object eval(Reader reader, ScriptContext context)
            throws ScriptException {
        return runScript(readAll(reader), context);
    }

    /**
     * Execute script in an string.<br/> <br/> <code>
     *     javax.script.ScriptEngine cajuEngine = new org.cajuscript.CajuScriptEngine();<br/>
     *     cajuEngine.eval("java.lang.System.out.println('Hello CajuScript!');");
     * </code>
     *
     * @param script
     *            Script to be executed.
     * @return Script executed can return an object.
     * @throws javax.script.ScriptException
     *             Exception on execution.
     */
    public Object eval(String script) throws ScriptException {
        return eval(script, context);
    }

    /**
     * Execute script in an file.<br/> <br/> <code>
     *    javax.script.ScriptEngine cajuEngine = new org.cajuscript.CajuScriptEngine();<br/>
     *    java.io.FileInputStream fis = new java.io.FileInputStream(<b>"script.cj"</b>);<br/>
     *    java.io.Reader reader = new java.io.InputStreamReader(fis);<br/>
     *    cajuEngine.eval(reader);<br/>
     *    reader.close();<br/>
     *    fis.close();<br/>
     * </code>
     *
     * @param reader
     *            Input of the file with script to be executed.
     * @return Script can returned an object.
     * @throws javax.script.ScriptException
     *             Exception on execution.
     */
    public Object eval(Reader reader) throws ScriptException {
        return eval(reader, context);
    }

    /**
     * Script on CajuScript to be executed.<br/> <br/> <code>
     *     javax.script.ScriptEngine cajuEngine = new org.cajuscript.CajuScriptEngine();<br/>
     *     cajuEngine.eval("java.lang.System.out.println('Hello CajuScript!');");<br/>
     * </code>
     *
     * @param script
     *            Script to be executed.
     * @param bindings
     *            Define the bindings.
     * @return Script can returned an object.
     * @throws javax.script.ScriptException
     *             Exception on execution.
     */
    public Object eval(String script, Bindings bindings) throws ScriptException {
        return runScript(script, bindings);
    }

    /**
     * Execute script in an file.<br/> <br/> <code>
     *    javax.script.ScriptEngine cajuEngine = new org.cajuscript.CajuScriptEngine();<br/>
     *    java.io.FileInputStream fis = new java.io.FileInputStream(<b>"script.cj"</b>);<br/>
     *    java.io.Reader reader = new java.io.InputStreamReader(fis);<br/>
     *    cajuEngine.eval(reader);<br/>
     *    reader.close();<br/>
     *    fis.close();<br/>
     * </code>
     *
     * @param reader
     *            Input of the file with script to be executed.
     * @param bindings
     *            Define the bindings.
     * @return Script executed can return an object.
     * @throws javax.script.ScriptException
     *             Exception on execution.
     */
    public Object eval(Reader reader, Bindings bindings) throws ScriptException {
        return runScript(readAll(reader), bindings);
    }

    /**
     * Send object of Java to CajuScript.<br/> <br/> <code>
     *    javax.script.ScriptEngine cajuEngine = new new org.cajuscript.CajuScriptEngine();<br/>
     *    cajuEngine.put("test_put", new StringBuffer("Java object!"));<br/>
     *    cajuEngine.eval("test_put.append('\\nTest CajuScript append!')");<br/>
     *    cajuEngine.eval("java.lang.System.out.println(test_put.toString())");<br/>
     * </code>
     *
     * @param key
     *            Name of the variable on CajuScript.
     * @param value
     *            Object to set the variable.
     */
    public void put(String key, Object value) {
        context.setAttribute(key, value, SimpleScriptContext.ENGINE_SCOPE);
    }

    /**
     * Catch objects of CajuScript to Java.<br/> <br/> <code>
     *    javax.script.ScriptEngine cajuEngine = new org.cajuscript.CajuScriptEngine();<br/>
     *    cajuEngine.eval("test_get = 'CajuScript object!'");<br/>
     *    String str = (String)cajuEngine.get("test_get");<br/>
     *    System.out.println(str);
     * </code>
     *
     * @param key
     *            Name of the variable in CajuScript.
     * @return Object CajuScript in Java.
     */
    public Object get(String key) {
        Object result = context.getAttribute(key,
                SimpleScriptContext.ENGINE_SCOPE);
        if (result == null) {
            try {
                return caju.get(key);
            } catch (Exception e) {
                return null;
            }
        }

        return result;
    }

    /**
     * Get Bindings.
     *
     * @param scope
     *            Scope.
     * @return Binding.
     */
    public Bindings getBindings(int scope) {
        return context.getBindings(scope);
    }

    /**
     * Set Binding.
     *
     * @param bindings
     *            Binding.
     * @param scope
     *            Scope.
     */
    public void setBindings(Bindings bindings, int scope) {
        context.setBindings(bindings, scope);
    }

    /**
     * Load a new Binding.
     *
     * @return New Binding.
     */
    public Bindings createBindings() {
        return new SimpleBindings();
    }

    /**
     * The context.
     *
     * @return Current context.
     */
    public ScriptContext getContext() {
        return context;
    }

    /**
     * Define the context.
     *
     * @param context
     *            Context.
     */
    public void setContext(ScriptContext context) {
        this.context = context;
    }

    /**
     * Newly instance of the CajuScript Engine Factory.
     *
     * @return Intanse of the CajuScript Engine Factory.
     */
    public ScriptEngineFactory getFactory() {
        return new CajuScriptEngineFactory();
    }

    /**
     * Invoke Java method on CajuScript.
     *
     * @param thiz
     *            Object with the method to invoke.
     * @param name
     *            Name of the method to invoke.
     * @param args
     *            Parameters of the method.
     * @throws javax.script.ScriptException
     *             Exception on invoke.
     * @throws java.lang.NoSuchMethodException
     *             Exception if method was not found.
     * @return Object who was returned from the method.
     */
    public Object invokeMethod(Object thiz, String name, Object... args)
            throws ScriptException, NoSuchMethodException {
        String funcObjectName = CajuScript.CAJU_VARS.concat("_").concat(
                caju.nextVarsCounter());
        try {
            caju.set(funcObjectName, thiz);
        } catch (Exception e) {
            throw new ScriptException(e);
        }
        Object returnObject = invokeFunction(funcObjectName.concat(".").concat(
                name), args);
        return returnObject;
    }

    /**
     * Invoke a CajuScript function.
     *
     * @param name
     *            Function name.
     * @param args
     *            Parameter of the function.
     * @throws javax.script.ScriptException
     *             Exception on invoke.
     * @throws java.lang.NoSuchMethodException
     *             Exception if function was not found.
     * @return Object who was returned from the function.
     */
    public Object invokeFunction(String name, Object... args)
            throws ScriptException, NoSuchMethodException {
        String cmd = "";
        String funcReturnName = CAJU_VARS_FUNC_RETURN.concat(caju.nextVarsCounter());
        cmd = cmd.concat(funcReturnName).concat(" = ");
        cmd = cmd.concat(name).concat("(");
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                String funcParamName = CAJU_VARS_FUNC_PARAM.concat(
                        caju.nextVarsCounter()).concat(Integer.toString(i));
                cmd = cmd.concat(i > 0 ? "," : "").concat(funcParamName);
                try {
                    caju.set(funcParamName, args[i]);
                } catch (Exception e) {
                    throw new ScriptException(e);
                }
            }
        }
        cmd = cmd.concat(");");
        eval(cmd);
        try {
            return caju.get(funcReturnName);
        } catch (Exception e) {
            throw new ScriptException(e);
        }
    }

    /**
     * Get interface.
     *
     * @param clasz
     *            Class.
     * @return Interface.
     */
    @SuppressWarnings("unchecked")
    public <T> T getInterface(Class<T> clasz) {
        try {
            return (T) implementor.getInterface(null, clasz);
        } catch (ScriptException e) {
            return null;
        }
    }

    /**
     * Get Interface.
     *
     * @param thiz
     *            Object.
     * @param clasz
     *            Class.
     * @return Interface.
     */
    @SuppressWarnings("unchecked")
    public <T> T getInterface(Object thiz, Class<T> clasz) {
        if (thiz == null) {
            throw new IllegalArgumentException("script object can not be null");
        }
        try {
            return (T) implementor.getInterface(thiz, clasz);
        } catch (ScriptException e) {
            return null;
        }
    }

    /**
     * Get the CajuScript instance.
     *
     * @return CajuScript.
     */
    public CajuScript getCajuScript() {
        return caju;
    }

    /**
     * Set the CajuScript instance.
     *
     * @param caju
     *            CajuScript.
     */
    public void setCajuScript(CajuScript caju) {
        this.caju = caju;
    }

    /**
     * Load script engine manager with CajuScript.
     *
     * @param mgr
     *            Script engine manager to be loaded.
     * @return Script engine manager is loaded with CajuScript.
     */
    public static ScriptEngineManager loadScriptEngineManager(
            ScriptEngineManager mgr) {
        return CajuScriptEngineFactory.loadScriptEngineManager(mgr);
    }

    private void loadBindings(ScriptContext context) throws ScriptException {
        List<Integer> scopes = context.getScopes();
        for (Integer i : scopes) {
            loadBindings(context.getBindings(i));
        }
    }

    private void loadBindings(Bindings bindings) throws ScriptException {
        if (bindings == null) {
            return;
        }
        Set<String> keys = bindings.keySet();
        for (String key : keys) {
            try {
                caju.set(key, bindings.get(key));
            } catch (Exception e) {
                throw new ScriptException(e);
            }
        }
    }

    private void recoveryBindings(ScriptContext context) throws ScriptException {
        List<Integer> scopes = context.getScopes();
        for (Integer i : scopes) {
            recoveryBindings(context.getBindings(i));
        }
    }

    private void recoveryBindings(Bindings bindings) throws ScriptException {
        if (bindings == null) {
            return;
        }
        Set<String> keys = bindings.keySet();
        for (String key : keys) {
            try {
                bindings.put(key, caju.get(key));
            } catch (Exception e) {
                throw new ScriptException(e);
            }
        }
    }

    public Object runScript(String script) throws ScriptException {
        try {
            Value v = caju.eval(script);
            if (v != null) {
                return v.getValue();
            }
            return null;
        } catch (Exception e) {
            throw new ScriptException(e);
        }
    }

    private String readAll(Reader reader) throws ScriptException {
        int b;
        StringBuilder sb = new StringBuilder();
        try {
            while ((b = reader.read()) > -1) {
                sb.append((char) b);
            }
        } catch (Exception e) {
            throw new ScriptException(e);
        }
        return sb.toString();
    }

    private Object runScript(String script, ScriptContext context)
            throws ScriptException {
        loadBindings(context);
        Object obj = runScript(script);
        recoveryBindings(context);
        return obj;
    }

    private Object runScript(String script, Bindings bindings)
            throws ScriptException {
        loadBindings(bindings);
        Object obj = runScript(script);
        recoveryBindings(bindings);
        return obj;
    }
}

class InterfaceImplementor {

    public class InterfaceImplementorInvocationHandler implements
            java.lang.reflect.InvocationHandler {

        public InterfaceImplementorInvocationHandler(Invocable engine,
                Object thiz) {
            this$0 = InterfaceImplementor.this;
            this.engine = engine;
            this.thiz = thiz;
        }

        public Object invoke(Object proxy, java.lang.reflect.Method method,
                Object args[]) throws Throwable {
            args = convertArguments(method, args);
            Object result;
            if (thiz == null) {
                result = engine.invokeFunction(method.getName(), args);
            } else {
                result = engine.invokeMethod(thiz, method.getName(), args);
            }
            return convertResult(method, result);
        }
        private Invocable engine;
        private Object thiz;
        final InterfaceImplementor this$0;
    }

    public InterfaceImplementor(Invocable engine) {
        this.engine = engine;
    }

    public Object getInterface(Object thiz, Class<?> iface)
            throws ScriptException {
        if (iface == null || !iface.isInterface()) {
            throw new IllegalArgumentException("interface Class expected");
        } else {
            return iface.cast(java.lang.reflect.Proxy.newProxyInstance(iface.getClassLoader(), new Class[]{iface},
                    new InterfaceImplementorInvocationHandler(engine, thiz)));
        }
    }

    protected Object convertResult(java.lang.reflect.Method method, Object res)
            throws ScriptException {
        return res;
    }

    protected Object[] convertArguments(java.lang.reflect.Method method,
            Object args[]) throws ScriptException {
        return args;
    }
    private Invocable engine;
}
