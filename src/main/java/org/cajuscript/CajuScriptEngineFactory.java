/*
 * CajuScriptEngineFactory.java
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

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Details about Caju Script Engine.<br/>
 * And loader for CajuScript.<br/>
 * <br/>
 * <code>
 * javax.script.EngineFactory cajuEngineFactory = new org.cajuscript.CajuScriptEngineFactory();<br/>
 * javax.script.ScriptEngine cajuEngine = cajuEngineFactory.getScriptEngine();<br/>
 * </code>
 * @author eduveks
 */
public class CajuScriptEngineFactory implements ScriptEngineFactory {  
    private static CajuScriptEngineFactory INSTANCE = new CajuScriptEngineFactory();
    
    /**
     * Create a newly instance.<br/>
     */
    public CajuScriptEngineFactory() {
        
    }
    
    /**
     * Get the engine name.
     * @return Engine name.
     */
    public String getEngineName() {
        return "CajuScript";
    }
    
    /**
     * Get the engine version.
     * @return Engine version.
     */
    public String getEngineVersion() {
        return CajuScript.VERSION;
    }
    
    /**
     * Get all extensions to CajuScript files.
     * @return List of extensions to CajuScript files.
     */
    public List<String> getExtensions() {
        List<String> v = new ArrayList<String>();
        v.add("cj");
        v.add("caju");
        v.add("cjs");
        return v;
    }
    
    /**
     * Get a list of mime types?
     * @return All mime types to CajuScript.
     */
    public List<String> getMimeTypes() {
        List<String> v = new ArrayList<String>();
        v.add("text/cajuscript");
        v.add("text/plain");
        return v;
    }
    
    /**
     * Get a list of names.
     * @return All names to CajuScript.
     */
    public List<String> getNames() {
        List<String> v = new ArrayList<String>();
        v.add("Caju");
        v.add("caju");
        v.add("CajuScript");
        v.add("cajuscript");
        return v;
    }
    
    /**
     * Get the language name.
     * @return Language name.
     */
    public String getLanguageName() {
        return CajuScript.NAME;
    }
    
    /**
     * Get the language version.
     * @return Language version.
     */
    public String getLanguageVersion() {
        return CajuScript.LANGUAGE_VERSION;
    }
    
    /**
     * Parameters of the engine.<br/>
     * Keys:<br/>
     * - ScriptEngine.ENGINE = <i>getEngineName()</i><br/>
     * - ScriptEngine.ENGINE_VERSION = <i>getEngineVersion()</i><br/>
     * - ScriptEngine.NAME = <i>getEngineName()</i><br/>
     * - ScriptEngine.LANGUAGE = <i>getLanguageName()</i><br/>
     * - ScriptEngine.LANGUAGE_VERSION = <i>getLanguageVersion()</i><br/>
     * @param key Key of the parameter.
     * @return Value of the parameter.
     */
    public Object getParameter(String key) {
        if (key.equalsIgnoreCase("ScriptEngine.ENGINE")) {
            return getEngineName();
        } else if (key.equalsIgnoreCase("ScriptEngine.ENGINE_VERSION")) {
            return getEngineVersion();
        } else if (key.equalsIgnoreCase("ScriptEngine.NAME")) {
            return getEngineName();
        } else if (key.equalsIgnoreCase("ScriptEngine.LANGUAGE")) {
            return getLanguageName();
        } else if (key.equalsIgnoreCase("ScriptEngine.LANGUAGE_VERSION")) {
            return getLanguageVersion();
        }
        return "";
    }
    
    /**
     * Sintax in CajuScript to call a method.<br/>
     * <br/>
     * <code>
     *    javax.script.ScriptEngineFactory cajuEngineFactory = new org.cajuscript.CajuScriptEngineFactory();<br/>
     *    String cmd = cajuEngineFactory.getMethodCallSyntax("obj", "method", "arg1", "arg2", "arg3");<br/>
     *    // cmd = obj.method("arg1", "arg2", "arg3")<br/>
     * </code>
     * @param obj Name of the object.
     * @param m Method to call.
     * @param args Arguments of the method.
     * @return Script to invoke the method.
     */
    public String getMethodCallSyntax(String obj, String m, String... args) {
        String cmd = "";
        if (obj != null && obj.length() != 0) {
            cmd = cmd.concat(obj).concat(".");
        }
        cmd = cmd.concat(m).concat("(");
        for (int i = 0; i < args.length; i++) {
            cmd = cmd.concat(args[i]);
            if (i < args.length - 1) {
                cmd = cmd.concat(",");
            }
        }
        cmd = cmd.concat(")");
        return cmd;
    }
    
    /**
     * Show a string in output.<br/>
     * <br/>
     * <code>
     *    javax.script.ScriptEngineFactory cajuEngineFactory = new org.cajuscript.CajuScriptEngineFactory();<br/>
     *    String cmd = cajuEngineFactory.getOutputStatement("Test");<br/>
     *    // cmd = java.lang.System.out.println("Test");
     * </code>
     * @param toDisplay String for pushing to output.
     * @return Command for printing the string in output.
     */
    public String getOutputStatement(String toDisplay) {
        return "java.lang.System.out.println(\"".concat(toDisplay).concat("\");");
    }
    
    /**
     * Not has implemented.
     * @param statements Not has implemented.
     * @return Not has implemented.
     */
    public String getProgram(String... statements) {
        return "";
    }
    
    /**
     * Get a newly instance of the Caju Script Engine.
     * @return New instance of the Caju Script Engine.
     */
    public ScriptEngine getScriptEngine() {
        try {
            return new CajuScriptEngine();
        } catch (Exception e) {
            throw new Error(e);
        }
    }
    
    /**
     * Load script engine manager with CajuScript.
     * @param mgr Script engine manager to be loaded.
     * @return Script engine manager is loaded with CajuScript.
     */
    public static ScriptEngineManager loadScriptEngineManager(ScriptEngineManager mgr) {
        for (String i : INSTANCE.getNames()) {
            mgr.registerEngineName(i, INSTANCE);
        }
        for (String i : INSTANCE.getExtensions()) {
            mgr.registerEngineExtension(i, INSTANCE);
        }
        for (String i : INSTANCE.getMimeTypes()) {
            mgr.registerEngineMimeType(i, INSTANCE);
        }
        return mgr;
    }
}

