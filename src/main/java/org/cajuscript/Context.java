/*
 * Context.java
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

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import org.cajuscript.parser.Function;

/**
 * Context contains all variables, functions and imports, exists global context 
 * and local context, functions have a local context.
 * @author eduveks
 */
public class Context {
    private List<String> imports = new ArrayList<String>();
    private Map<String, Value> vars = new HashMap<String, Value>();
    private Map<String, Function> funcs = new HashMap<String, Function>();
    private Map<String, Class> classCache = new HashMap<String, Class>();
    private Value staticString = null;
    private Map<String, String> staticStrings = new HashMap<String, String>();

    /**
     * New context instance.
     */
    public Context() {
        staticString = new Value(null, null, null);
        imports.add("java.lang");
    }
    
    /**
     * Get list of all imports used by script in execution.
     * @return List of imports defined.
     */
    public List<String> getImports() {
        return imports;
    }
    
    /**
     * Get all varriables.
     * @return All variables.
     */
    public Map<String, Value> getVars() {
        return vars;
    }
    
    /**
     * Get all functions.
     * @return All functions.
     */
    public Map<String, Function> getFuncs() {
        return funcs;
    }
    
    /**
     * Get function.
     * @param key Function name.
     * @return Function object.
     */
    public Function getFunc(String key) {
        return funcs.get(key);
    }
    
    /**
     * Define a function.
     * @param key Funcion name.
     * @param func Object of the function.
     */
    public void setFunc(String key, Function func) {
        funcs.put(key, func);
    }
    
    /**
     * Get variable.
     * @param key Variable name.
     * @return Variable object.
     */
    public Value getVar(String key) throws CajuScriptException {
        if (key.startsWith(CajuScript.CAJU_VARS_STATIC_STRING)) {
            return getStaticStringValue(key);
        } else {
            return vars.get(key);
        }
    }
    
    /**
     * Get all name of variables without variables created automaticaly by
     * CajuScript.
     * @return List of all variables names.
     */
    public Set<String> getAllKeys() {
        return getAllKeys(false);
    }
    
    /**
     * Get all name of variables including variables created automaticaly by
     * CajuScript if parameter are true.
     * @param withCajuVars Including variables created automaticaly by CajuScript or not.
     * @return List of all variables names.
     */
    public Set<String> getAllKeys(boolean withCajuVars) {
        Set<String> keys = new HashSet<String>();
        for (String key : vars.keySet()) {
            if (!withCajuVars && key.startsWith(CajuScript.CAJU_VARS)) {
                continue;
            }
            keys.add(key);
        }
        return keys;
    }
    
    /**
     * Setting new variable.
     * @param key Variable name.
     * @param value Variable value.
     */
    public void setVar(String key, Value value) {
        if (key.startsWith(CajuScript.CAJU_VARS_STATIC_STRING)) {
            setStaticString(key, value.toString());
        } else {
            vars.put(key.trim(), value);
        }
    }
    
    /**
     * Define a new import to be used. Only Java package.
     * @param i The content of importing is only Java package.
     */
    public void addImport(String i) {
        imports.add(i);
    }
    
    /**
     * Remove import.
     * @param s Import content to be removed.
     */
    public void removeImport(String s) {
        imports.remove(s);
    }

    public void setClassCache(String path, Class c) {
        classCache.put(path, c);
    }
    public Class getClassCache(String path) {
        return classCache.get(path);
    }

    public Class findClass(String path) {
        Class c = null;
        if (getClassCache(path) != null) {
            c = getClassCache(path);
        } else {
            try {
                c = Class.forName(path);
                setClassCache(path, c);
            } catch (Throwable e) {
                for(String i : getImports()) {
                    if (i.endsWith(path)) {
                        try {
                            c = Class.forName(i);
                            setClassCache(path, c);
                        } catch (Throwable ex) { }
                    } else {
                        try {
                            c = Class.forName(i.concat(".").concat(path));
                            setClassCache(path, c);
                        } catch (Throwable ex) { }
                    }
                    if (c != null) {
                        break;
                    }
                }
            }
        }
        return c;
    }

    public void setStaticString(String key, String value) {
        staticStrings.put(key, value);
    }
    public String getStaticString(String key) {
        return staticStrings.get(key);
    }

    public Map<String, String> getStaticStrings() {
        return staticStrings;
    }

    public Value getStaticStringValue(String key) throws CajuScriptException {
        if (!staticStrings.containsKey(key)) {
            return null;
        }
        staticString.setValue(staticStrings.get(key));
        return staticString;
    }
}
