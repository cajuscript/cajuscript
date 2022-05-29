/*
 * ScriptCommand.java
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

package org.cajuscript.cmd;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.cajuscript.Value;

/**
 * To cache data from commands executed.
 * @author eduveks
 */
public class ScriptCommand {
    /**
     * Type of script commands.
     */
    public static enum Type {
        VARIABLE, VARIABLE_ROOT, FUNCTION, NATIVE_CLASS, NATIVE_OBJECT, NATIVE_OBJECT_ROOT, ARRAY, ARRAY_TYPE
    }
    private String script = "";
    private String finalScript = "";
    private Type type = null;
    private String[] params = null;
    private Object[] paramsValues = null;
    private Object[] paramsFinal = null;
    private String function = "";
    private String classPath = "";
    private String var = "";
    private String paramName = "";
    private Class<?> classReference = null;
    private Value value = null;
    private Constructor<?> constructor = null;
    private Method method = null;
    private ScriptCommand nextScriptCommand = null;

    /**
     * Create new script command with an script and type.
     * @param script Script.
     * @param type Type.
     */
    public ScriptCommand(String script, Type type) {
        this.script = script;
        this.type = type;
    }

    /**
     * Get the script.
     * @return Script.
     */
    public String getScript() {
        return script;
    }

    /**
     * Set the script.
     * @param script Script.
     */
    public void setScript(String script) {
        this.script = script;
    }

    /**
     * Get the final script, used like backup when script is changed.
     * @return Script.
     */
    public String getFinalScript() {
        return finalScript;
    }

    /**
     * Set the final script, used like backup when script is changed.
     * @param finalScript Script.
     */
    public void setFinalScript(String finalScript) {
        this.finalScript = finalScript;
    }

    /**
     * Get the type of command.
     * @return Type.
     */
    public Type getType() {
        return type;
    }

    /**
     * Set the type of command.
     * @param type Type.
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Set function configuration.
     * @param function Function name.
     * @param params Parameters names.
     */
    public void setFunction(String function, String[] params) {
        this.function = function;
        this.params = params;
    }

    /**
     * Set static method configuration.
     * @param classPath Class path.
     * @param method Method.
     * @param params Parameters.
     */
    public void setStatic(String classPath, Method method, String[] params) {
        this.classPath = classPath;
        this.method = method;
        this.params = params;
    }

    /**
     * Set method configuration.
     * @param method Method.
     * @param params Parameters.
     */
    public void setMethod(Method method, String[] params) {
        this.method = method;
        this.params = params;
    }

    /**
     * Set newly instance configuration.
     * @param classPath Class path.
     * @param constructor Constructor.
     * @param params Parameters.
     */
    public void setNewInstance(String classPath, Constructor<?> constructor, String[] params) {
        this.classPath = classPath;
        this.constructor = constructor;
        this.params = params;
    }

    /**
     * Get class reference.
     * @return Class reference.
     */
    public Class<?> getClassReference() {
        return classReference;
    }

    /**
     * Set class reference.
     * @param classReference Class reference.
     */
    public void setClassReference(Class<?> classReference) {
        this.classReference = classReference;
    }

    /**
     * Get constructor.
     * @return Constructor.
     */
    public Constructor<?> getConstructor() {
        return constructor;
    }

    /**
     * Set constructor.
     * @param constructor Constructor.
     */
    public void setConstructor(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    /**
     * Get function.
     * @return Function.
     */
    public String getFunction() {
        return function;
    }

    /**
     * Set function.
     * @param function Function.
     */
    public void setFunction(String function) {
        this.function = function;
    }

    /**
     * Get method.
     * @return Method.
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Set method.
     * @param method Method.
     */
    public void setMethod(Method method) {
        this.method = method;
    }

    /**
     * Get parameters.
     * @return Parameters.
     */
    public String[] getParams() {
        return params;
    }

    /**
     * Set parameters.
     * @param params Parameters.
     */
    public void setParams(String[] params) {
        this.params = params;
    }

    /**
     * Get final values from parameters.
     * @return Values from parameters
     */
    public Object[] getParamsFinal() {
        return paramsFinal;
    }

    /**
     * Set final values from parameters.
     * @param paramsFinal Values from parameters
     */
    public void setParamsFinal(Object[] paramsFinal) {
        this.paramsFinal = paramsFinal;
    }

    /**
     * Get values from parameters.
     * @return Values from parameters
     */
    public Object[] getParamsValues() {
        return paramsValues;
    }

    /**
     * Set values from parameters.
     * @param paramsValues Values from parameters
     */
    public void setParamsValues(Object[] paramsValues) {
        this.paramsValues = paramsValues;
    }

    /**
     * Get class path.
     * @return Class path.
     */
    public String getClassPath() {
        return classPath;
    }

    /**
     * Set class path.
     * @param classPath Class path.
     */
    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    /**
     * Get value.
     * @return Value.
     */
    public Value getValue() {
        return value;
    }

    /**
     * Set value.
     * @param value Value.
     */
    public void setValue(Value value) {
        this.value = value;
    }

    /**
     * Get next script command. Used if have others script commands in sequence.
     * @return Script command.
     */
    public ScriptCommand getNextScriptCommand() {
        return nextScriptCommand;
    }

    /**
     * Set next script command. Used if have others script commands in sequence.
     * @param nextScriptCommand Script command.
     */
    public void setNextScriptCommand(ScriptCommand nextScriptCommand) {
        this.nextScriptCommand = nextScriptCommand;
    }

    /**
     * Get variable name.
     * @return Variable name.
     */
    public String getVar() {
        return var;
    }

    /**
     * Set variable name.
     * @param var Variable name.
     */
    public void setVar(String var) {
        this.var = var;
    }

    /**
     * Get parameter name.
     * @return Script Parameter name.
     */
    public String getParamName() {
        return paramName;
    }

    /**
     * Set parameter name.
     * @param paramName Parameter name.
     */
    public void setParamName(String paramName) {
        this.paramName = paramName;
    }
}