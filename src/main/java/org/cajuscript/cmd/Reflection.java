/*
 * Reflection.java
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

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.regex.Pattern;
import org.cajuscript.CajuScript;
import org.cajuscript.CajuScriptException;
import org.cajuscript.Context;
import org.cajuscript.Syntax;
import org.cajuscript.SyntaxPosition;
import org.cajuscript.Value;

/**
 * Java invoke with reflection.
 * @author eduveks
 */
public class Reflection {
    /**
     * Invoke native classes, methods and attributes.
     * @param cajuScript CajuScript instance
     * @param context Context
     * @param syntax Syntax
     * @param value Object value to be invoked
     * @param script Script command
     * @param scriptCommand ScriptCommand instance to save the procedure in cache
     * @return Object returned by invokation
     * @throws org.cajuscript.CajuScriptException Invocation exceptions
     */
    public static Object invokeNative(CajuScript cajuScript, Context context, Syntax syntax, Object value, String script, ScriptCommand scriptCommand) throws CajuScriptException {
        try {
            Class<?> c = null;
            String cName = "";
            if (scriptCommand.getClassReference() == null || (scriptCommand.getMethod() == null && scriptCommand.getConstructor() == null && scriptCommand.getParamName().length() == 0)) {
                if (script == null || script.length() == 0) {
                    return value;
                }
                String realClassName = "";
                SyntaxPosition syntaxPathSeparator = syntax.matcherPosition(script, syntax.getFunctionCallPathSeparator());
                if (syntaxPathSeparator.getStart() == 0) {
                    script = script.substring(syntaxPathSeparator.getEnd());
                }
                script = script.trim();
                String path = "";
                String realPath = "";
                int p = -1;
                String scriptRest = script;
                String scriptPart = "";
                while (true) {
                    p++;
                    syntaxPathSeparator = syntax.matcherPosition(scriptRest, syntax.getFunctionCallPathSeparator());
                    if (syntaxPathSeparator.getStart() > -1) {
                        scriptPart = scriptRest.substring(0, syntaxPathSeparator.getStart());
                        scriptRest = scriptRest.substring(syntaxPathSeparator.getEnd());
                    } else {
                        scriptPart = scriptRest;
                        scriptRest = "";
                    }
                    if (p > 0) {
                        path = path.concat(".");
                        realPath = realPath.concat(".");
                    }
                    if (realPath.endsWith("..")) {
                        realClassName = path.substring(0, path.lastIndexOf('.') - 1);
                        c = Class.forName(realClassName);
                    }
                    SyntaxPosition syntaxParameterBegin = syntax.matcherPosition(scriptPart, syntax.getFunctionCallParametersBegin());
                    if (syntaxParameterBegin.getStart() > -1 && value == null) {
                        cName = scriptPart.substring(0, syntaxParameterBegin.getStart());
                        realPath = realPath.concat(cName);
                        cName = cName.trim();
                        path = path.concat(cName);
                    } else if (syntaxParameterBegin.getStart() > -1 && value != null) {
                        cName = scriptPart.substring(0, syntaxParameterBegin.getStart());
                        realPath = realPath.concat(cName);
                        cName = cName.trim();
                        path = path.concat(cName);
                    } else {
                        path = path.concat(scriptPart.trim());
                        realPath = realPath.concat(scriptPart);
                    }
                    if (value == null) {
                        c = cajuScript.getContext().findClass(path);
                        if (c == null) {
                            boolean isRootContext = false;
                            Value _value = context.getVar(path);
                            if (_value == null) {
                                isRootContext = true;
                                _value = cajuScript.getVar(path);
                            }
                            if (_value != null) {
                                scriptCommand.setVar(path);
                                scriptCommand.setValue(_value);
                                if (isRootContext) {
                                    scriptCommand.setType(ScriptCommand.Type.NATIVE_OBJECT_ROOT);
                                } else {
                                    scriptCommand.setType(ScriptCommand.Type.NATIVE_OBJECT);
                                }
                                value = _value.getValue();
                                c = value.getClass();
                            } else {
                                continue;
                            }
                        }
                        script = script.substring(realPath.length());
                    } else {
                        c = value.getClass();
                    }
                    scriptCommand.setClassPath(c.getName());
                    scriptCommand.setClassReference(c);
                    break;
                }
                if (cName.length() != 0 && value == null) {
                    Object[] values = invokeFunctionValues(cajuScript, context, syntax, scriptPart.substring(cName.length()), scriptCommand);
                    return invokeConstructor(cajuScript, c, values, script, scriptCommand);
                } else if (cName.length() != 0 && value != null) {
                    Object[] values = invokeFunctionValues(cajuScript, context, syntax, script, scriptCommand);
                    return invokeMethod(cajuScript, c, value, cName, values, script, scriptCommand);
                } else {
                    syntaxPathSeparator = syntax.matcherPosition(script, syntax.getFunctionCallPathSeparator());
                    if (syntaxPathSeparator.getStart() == 0) {
                        script = script.substring(syntaxPathSeparator.getEnd());
                    }
                    syntaxPathSeparator = syntax.matcherPosition(script, syntax.getFunctionCallPathSeparator());
                    SyntaxPosition syntaxParameterBegin = syntax.matcherPosition(script, syntax.getFunctionCallParametersBegin());
                    boolean isMethod = false;
                    if (syntaxParameterBegin.getStart() > -1 && (syntaxParameterBegin.getStart() < syntaxPathSeparator.getStart() || syntaxPathSeparator.getStart() == -1)) {
                        scriptPart = script.substring(0, syntaxParameterBegin.getStart());
                        isMethod = true;
                        for (Class declaredClass : c.getDeclaredClasses()) {
                            if (declaredClass.getSimpleName().equals(scriptPart)) {
                                isMethod = false;
                            }
                        }
                    } else {
                        scriptPart = script;
                    }
                    syntaxPathSeparator = syntax.matcherPosition(script, syntax.getFunctionCallPathSeparator());
                    if (!isMethod || syntaxPathSeparator.getStart() > -1) {
                        String paramName = scriptPart;
                        if (syntaxPathSeparator.getStart() > -1) {
                            paramName = scriptPart.substring(0, syntaxPathSeparator.getStart());
                        }
                        script = script.substring(paramName.length());
                        if (value != null) {
                            scriptCommand.setParamName(paramName);
                            ScriptCommand sc = new ScriptCommand(script, ScriptCommand.Type.NATIVE_OBJECT);
                            sc.setVar(scriptCommand.getVar());
                            Object oParam = c.getField(paramName).get(value);
                            if (oParam != null) {
                                sc.setClassReference(oParam.getClass());
                                sc.setClassPath(oParam.getClass().getName());
                            }
                            sc.setValue(cajuScript.toValue(oParam));
                            Object o = invokeNative(cajuScript, context, syntax, oParam, script, sc);
                            scriptCommand.setNextScriptCommand(sc);
                            return o;
                        }
                        if (paramName.equals("class")) {
                            return invokeNative(cajuScript, context, syntax, c, script, scriptCommand);
                        } else {
                            try {
                                String subclassName = c.getName().concat("$").concat(paramName);
                                Class.forName(subclassName, false, c.getClassLoader());
                                Object o = invokeNative(cajuScript, context, syntax, null, subclassName.concat(script), scriptCommand);
                                return o;
                            } catch (Exception e) {
                                scriptCommand.setParamName(paramName);
                                ScriptCommand sc = new ScriptCommand(script, ScriptCommand.Type.NATIVE_OBJECT);
                                Object oParam = c.getField(paramName).get(c);
                                if (oParam != null) {
                                    sc.setClassReference(oParam.getClass());
                                    sc.setClassPath(oParam.getClass().getName());
                                }
                                Object o = invokeNative(cajuScript, context, syntax, oParam, script, sc);
                                scriptCommand.setNextScriptCommand(sc);
                                return o;
                            }
                        }
                    } else {
                        Object[] values = invokeFunctionValues(cajuScript, context, syntax, script.substring(syntaxParameterBegin.getStart()), scriptCommand);
                        String propName = script.substring(0, syntaxParameterBegin.getStart());
                        return invokeMethod(cajuScript, c, value, propName, values, script, scriptCommand);
                    }
                }
            } else {
                c = scriptCommand.getClassReference();
                if (scriptCommand.getMethod() != null) {
                    Object[] values = invokeFunctionValues(cajuScript, context, syntax, null, scriptCommand);
                    return invokeMethod(cajuScript, c, value, null, values, null, scriptCommand);
                } else if (scriptCommand.getConstructor() != null) {
                    Object[] values = invokeFunctionValues(cajuScript, context, syntax, null, scriptCommand);
                    return invokeConstructor(cajuScript, c, values, script, scriptCommand);
                } else if (scriptCommand.getParamName().length() != 0) {
                    String nextScript = scriptCommand.getNextScriptCommand() != null ? scriptCommand.getNextScriptCommand().getScript() : "";
                    if (scriptCommand.getValue() != null) {
                        return invokeNative(cajuScript, context, syntax, c.getField(scriptCommand.getParamName()).get(scriptCommand.getValue().getValue()), nextScript, scriptCommand.getNextScriptCommand());
                        //return invokeNative(cajuScript, context, syntax, c.getField(scriptCommand.getParamName()).get(scriptCommand.getValue().getValue()), scriptCommand.getScript().substring(scriptCommand.getScript().indexOf(".".concat(script)) + script.length() + 1), scriptCommand.getNextScriptCommand());
                    } else {
                        return invokeNative(cajuScript, context, syntax, c.getField(scriptCommand.getParamName()).get(c), nextScript, scriptCommand.getNextScriptCommand());
                        //return invokeNative(cajuScript, context, syntax, c.getField(scriptCommand.getParamName()).get(c), scriptCommand.getScript().substring(scriptCommand.getScript().indexOf(".".concat(script)) + script.length() + 1), scriptCommand.getNextScriptCommand());
                    }
                }
                throw new Exception("Cannot invoke ".concat(scriptCommand.getScript()));
            }
        } catch (CajuScriptException e) {
            throw e;
        } catch (Exception e) {
            throw CajuScriptException.create(cajuScript, context, e.getMessage(), e);
        }
    }

    private static int findBestCallIndex(CajuScript cajuScript, Object[] originalValues, Class<?>[][] originalCallsParams, ScriptCommand scriptCommand) throws Exception {
        int countFits = 0;
        int maxFits = 3;
        int[] originalCallsIndexes = new int[originalCallsParams.length];
        Class<?>[][] callsParams = new Class<?>[originalCallsParams.length][];
        Object[][] callsValues = new Object[originalCallsParams.length][];
        int callsParamsIndex = 0;
        for (int c = 0; c < originalCallsParams.length; c++) {
            Class<?>[] cx = originalCallsParams[c];
            Object[] values = fitValues(cajuScript, originalValues, cx);
            if (cx.length == values.length) {
                originalCallsIndexes[callsParamsIndex] = c;
                callsParams[callsParamsIndex] = originalCallsParams[c];
                callsValues[callsParamsIndex] = values;
                callsParamsIndex++;
            }
        }
        callsParams = Arrays.copyOf(callsParams, callsParamsIndex);
        callsValues = Arrays.copyOf(callsValues, callsParamsIndex);
        for (int c = 0; c < callsParams.length; c++) {
            Class<?>[] cx = callsParams[c];
            Object[] values = callsValues[c];
            int count = 0;
            for (int i = 0; i < values.length; i++) {
                if (values[i] == null && !CajuScript.isPrimitiveType(cx[i].getName())) {
                    count++;
                } else if (values[i] != null) {
                    if ((countFits >= 1
                            && CajuScript.isPrimitiveType(values[i].getClass().getName())
                            && CajuScript.isPrimitiveType(cx[i].getName())
                            && CajuScript.isAlmostSameType(
                                    values[i].getClass().getName(),
                                    cx[i].getName()))
                        || (countFits >= 2
                            && CajuScript.isPrimitiveType(values[i].getClass().getName())
                            && CajuScript.isPrimitiveType(cx[i].getName())
                            && CajuScript.hasTypePriorityOverAnother(
                                    values[i].getClass().getName(),
                                    cx[i].getName()))
                        || (countFits >= 2 && cajuScript.isInstance(values[i], cx[i]))
                        || (countFits >= 3
                            && CajuScript.isPrimitiveType(values[i].getClass().getName())
                            && CajuScript.isPrimitiveType(cx[i].getName()))
                        || (countFits >= 3 && cx[i].getName().equals("java.lang.Object"))
                        || values[i].getClass().getName().equals(cx[i].getName())
                        || CajuScript.isSameType(cx[i].getName(), values[i].getClass().getName())
                        || (cx[i].isArray() && values[i].getClass().isArray() && CajuScript.isSameType(cx[i].getComponentType().getName(), values[i].getClass().getComponentType().getName()))) {
                        count++;
                    } else {
                        count = 0;
                    }
                }
            }
            if (count == cx.length && values.length == count) {
                return originalCallsIndexes[c];
            }
            if (c + 1 == callsParams.length && countFits <= maxFits) {
                countFits++;
                c = -1;
            } else {
                countFits = 0;
            }
        }
        return -1;
    }

    private static boolean foundMethod(CajuScript cajuScript, Object[] values, Class<?>[] cx, boolean allowAutoPrimitiveCast, ScriptCommand scriptCommand) {
        int count = 0;
        for (int x = 0; x < values.length; x++) {
            if (values[x] == null && !CajuScript.isPrimitiveType(cx[x].getName())) {
                count++;
            } else if (values[x] != null) {
                if (cx[x].getName().equals("java.lang.Object")
                    || (CajuScript.isPrimitiveType(values[x].getClass().getName()) && CajuScript.isPrimitiveType(cx[x].getName()) && allowAutoPrimitiveCast)
                    || values[x].getClass().getName().equals(cx[x].getName())
                    || CajuScript.isSameType(cx[x].getName(), values[x].getClass().getName())
                    || (cx[x].isArray() && values[x].getClass().isArray() && CajuScript.isSameType(cx[x].getComponentType().getName(), values[x].getClass().getComponentType().getName()))) {
                    count++;
                } else {
                    if (cajuScript.isInstance(values[x], cx[x])) {
                        count++;
                    } else {
                        count = 0;
                    }
                }
            } else {
                count = 0;
            }
        }
        if (count == cx.length && values.length == count) {
            return true;
        } else {
            return false;
        }
    }

    private static Object invokeConstructor(CajuScript cajuScript, Class<?> cls, Object[] values, String script, ScriptCommand scriptCommand) throws Exception {
        if (scriptCommand.getConstructor() != null) {
            try {
                return scriptCommand.getConstructor().newInstance(getParams(cajuScript, values, scriptCommand.getConstructor().getParameterTypes(), scriptCommand));
            } catch (ClassCastException e) { }
        }
        Constructor<?>[] declaredConstructors = cls.getDeclaredConstructors();
        Constructor<?>[] constructors = new Constructor<?>[declaredConstructors.length];
        int constructorsIndex = 0;
        for (Constructor<?> c : declaredConstructors) {
            if (c.getParameterCount() == values.length) {
                constructors[constructorsIndex++] = c;
            }
        }
        constructors = Arrays.copyOfRange(constructors, 0, constructorsIndex);
        Class<?>[][] constructorsParams = new Class<?>[constructors.length][];
        for (int i = 0; i < constructors.length; i++) {
            constructorsParams[i] = constructors[i].getParameterTypes();
        }
        int index = findBestCallIndex(cajuScript, values, constructorsParams, scriptCommand);
        if (index >= 0) {
            scriptCommand.setConstructor(constructors[index]);
            scriptCommand.setType(ScriptCommand.Type.NATIVE_OBJECT);
            return constructors[index].newInstance(getParams(cajuScript, values, constructorsParams[index], scriptCommand));
        }
        try {
            return cls.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new Exception("Constructor \"".concat(cls.getName()).concat("\" cannot be invoked"));
        }
    }

    private static Object invokeMethod(CajuScript cajuScript, Class<?> c, Object o, String name, Object[] values, String script, ScriptCommand scriptCommand) throws Exception {
        if (scriptCommand.getMethod() != null) {
            try {
                Object r = scriptCommand.getMethod().invoke(o, getParams(cajuScript, values, scriptCommand.getMethod().getParameterTypes(), scriptCommand));
                return r;
            } catch (ClassCastException e) { }
        }
        if (name == null) {
            name = scriptCommand.getMethod().getName();
        }
        Class<?>[] classes = null;
        Class<?>[] interfaces = c.getInterfaces();
        if (c.isMemberClass() || interfaces.length > 0) {
            classes = new Class[interfaces.length + 2];
            classes[0] = c.getSuperclass();
            System.arraycopy(interfaces, 0, classes, 1, interfaces.length);
            classes[classes.length - 1] = c;
        } else {
            classes = new Class[] {c};
        }
        for (Class<?> cls : classes) {
            final String methodName = name;
            Method[] declaredMethods = cls.getMethods();
            Method[] methods = new Method[declaredMethods.length];
            int methodsIndex = 0;
            for (Method m : declaredMethods) {
                if (m.getName().equals(methodName)) {
                    methods[methodsIndex++] = m;
                }
            }
            methods = Arrays.copyOfRange(methods, 0, methodsIndex);
            Class<?>[][] methodsParams = new Class<?>[methods.length][];
            for (int i = 0; i < methods.length; i++) {
                methodsParams[i] = methods[i].getParameterTypes();
            }
            int index = findBestCallIndex(cajuScript, values, methodsParams, scriptCommand);
            if (index >= 0) {
                scriptCommand.setMethod(methods[index]);
                return methods[index].invoke(o, getParams(cajuScript, values, methodsParams[index], scriptCommand));
            }
        }
        throw new Exception("Method \"".concat(name).concat("\" cannot be invoked"));
    }

    /**
     * Catch values from function syntax to be used into invocations.
     * @param cajuScript CajuScript instance.
     * @param context Context
     * @param syntax Syntax
     * @param script Parameters script
     * @param scriptCommand ScriptCommand instance to save the procedure in cache
     * @return Objects values from the parameters.
     * @throws org.cajuscript.CajuScriptException Catching values exception
     */
    public static Object[] invokeFunctionValues(CajuScript cajuScript, Context context, Syntax syntax, String script, ScriptCommand scriptCommand) throws CajuScriptException {
        return invokeValues(cajuScript, context, syntax, script, scriptCommand, syntax.getFunctionCallParametersBegin(), syntax.getFunctionCallParametersSeparator(), syntax.getFunctionCallParametersEnd());
    }

    /**
     * Catch values from array syntax to be used into invocations.
     * @param cajuScript CajuScript instance.
     * @param context Context
     * @param syntax Syntax
     * @param script Parameters script
     * @param scriptCommand ScriptCommand instance to save the procedure in cache
     * @return Objects values from the parameters.
     * @throws org.cajuscript.CajuScriptException Catching values exception
     */
    public static Object[] invokeArrayValues(CajuScript cajuScript, Context context, Syntax syntax, String script, ScriptCommand scriptCommand) throws CajuScriptException {
        return invokeValues(cajuScript, context, syntax, script, scriptCommand, syntax.getArrayCallParametersBegin(), syntax.getArrayCallParametersSeparator(), syntax.getArrayCallParametersEnd());
    }

    /**
     * Catch values to be used into invocations.
     * @param cajuScript CajuScript instance.
     * @param context Context
     * @param syntax Syntax
     * @param script Parameters script
     * @param scriptCommand ScriptCommand instance to save the procedure in cache
     * @param parametersBegin Parameters begin syntax
     * @param parametersSeparator Parameters separator syntax
     * @param parametersEnd Parameters end syntax
     * @return Objects values from the parameters.
     * @throws org.cajuscript.CajuScriptException Catching values exception
     */
    public static Object[] invokeValues(CajuScript cajuScript, Context context, Syntax syntax, String script, ScriptCommand scriptCommand, Pattern parametersBegin, Pattern parametersSeparator, Pattern parametersEnd) throws CajuScriptException {
        if (scriptCommand.getParams() != null) {
            String[] paramsVal = scriptCommand.getParams();
            Object[] values = new Object[paramsVal.length];
            for (int x = 0; x < paramsVal.length; x++) {
                values[x] = context.getVar(paramsVal[x]).getValue();
            }
            return values;
        }
        int lenBegin = (syntax.matcherPosition(script, parametersBegin)).getEnd();
        int lenEnd = (syntax.matcherPosition(script, parametersEnd)).getStart();
        String params = script.substring(lenBegin, lenEnd);
        if (params.trim().length() == 0) {
            scriptCommand.setParams(new String[0]);
            return new Object[0];
        }
        String[] paramsKeys = parametersSeparator.split(params);
        Value[] paramsVal = new Value[paramsKeys.length];
        Object[] values = new Object[paramsKeys.length];
        for (int x = 0; x < paramsKeys.length; x++) {
            SyntaxPosition syntaxRootContext = syntax.matcherPosition(paramsKeys[x], syntax.getRootContext());
            if (syntaxRootContext.getStart() == 0) {
                paramsKeys[x] = paramsKeys[x].substring(syntaxRootContext.getEnd());
                values[x] = cajuScript.getVar(paramsKeys[x]);
            } else {
                values[x] = context.getVar(paramsKeys[x]);
                if (values[x] == null) {
                    values[x] = cajuScript.getVar(paramsKeys[x]);
                }
            }
            paramsVal[x] = (Value)values[x];
            values[x] = paramsVal[x].getValue();
        }
        scriptCommand.setParams(paramsKeys);
        return values;
    }

    private static Object[] fitValues(CajuScript cajuScript, Object[] values, Class<?>[] cx) throws Exception {
        if (values.length < cx.length) {
            return values;
        }
        Object[] finalValues = new Object[values.length];
        int finalLength = 0;
        for (int i = 0; i < values.length; i++) {
            if (cx.length > i && cx[i].isArray() && !values[i].getClass().isArray()) {
                String className = cx[i].getName();
                if (className.startsWith("[L") && className.endsWith(";")) {
                    className = className.substring(2, className.length() - 1);
                }
                Class<?> c = Class.forName(className);
                Object paramArray = Array.newInstance(c, values.length - i);
                for (int j = i; j < values.length; j++) {
                    if (CajuScript.isPrimitiveType(c.getName())) {
                        Array.set(paramArray, j - i, cajuScript.cast(values[j], c.getName()));
                    } else {
                        try {
                            Array.set(paramArray, j - i, c.cast(values[j]));
                        } catch (ClassCastException e) {
                            return values;
                        }
                    }
                }
                finalValues[finalLength] = paramArray;
                i = values.length;
            } else {
                finalValues[i] = values[i];
            }
            finalLength++;
        }
        if (finalLength < finalValues.length) {
            return Arrays.copyOf(finalValues, finalLength);
        }
        return finalValues;
    }

    private static Object[] getParams(CajuScript cajuScript, Object[] values, Class<?>[] cx, ScriptCommand scriptCommand) throws Exception {
        values = fitValues(cajuScript, values, cx);
        if (Arrays.equals(values, scriptCommand.getParamsValues())) {
            return scriptCommand.getParamsFinal();
        }
        Object[] params = new Object[cx.length];
        for (int x = 0; x < cx.length; x++) {
            if (scriptCommand.getParamsValues() != null && scriptCommand.getParamsValues()[x].equals(values[x])) {
                params[x] = scriptCommand.getParamsValues()[x];
            }
            if (CajuScript.isPrimitiveType(cx[x].getName())) {
                params[x] = cajuScript.cast(values[x], cx[x].getName());
            } else {
                params[x] = cx[x].cast(values[x]);
            }
        }
        scriptCommand.setParamsValues(values);
        scriptCommand.setParamsFinal(params);
        return params;
    }
}
