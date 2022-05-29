/*
 * Value.java
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.cajuscript.cmd.Reflection;
import org.cajuscript.cmd.ScriptCommand;
import org.cajuscript.parser.Function;

/**
 * All values of variables of the CajuScript are instance this class.
 * <p>CajuScript:</p>
 * <p><blockquote><pre>
 *     x = 1
 *     s = "text..."
 * </pre></blockquote></p>
 * <p>Java:</p>
 * <p><blockquote><pre>
 *     org.cajuscript.CajuScript caju = new org.cajuscript.CajuScript();
 *     org.cajuscript.Value cajuValue = new org.cajuscript.Value(caju);
 *     cajuValue.setValue(new String("Text..."));
 *     caju.addVar("text", cajuValue);
 *     System.out.println(caju.get("text"));
 * </pre></blockquote></p>
 * @author eduveks
 */
public class Value implements Cloneable {
    /**
     * Types of values.
     */
    public static enum Type {
        NULL, NUMBER, BOOLEAN, STRING, OBJECT, ARRAY
    }
    /**
     * Types of numbers.
     */
    public static enum TypeNumber {
        INTEGER, LONG, DOUBLE, FLOAT
    }
    private Object value = null;
    private int valueNumberInteger = 0;
    private long valueNumberLong = 0;
    private float valueNumberFloat = 0;
    private double valueNumberDouble = 0;
    private String valueString = "";
    private boolean valueBoolean = false;
    private TypeNumber typeNumber = null;
    private Type type = Type.NULL;
    private Class classType = null;
    private boolean _isCommand = false;
    private String command = "";
    private CajuScript cajuScript = null;
    private Context context = null;
    private Syntax syntax = null;
    private String flag = "";
    private ScriptCommand scriptCommand = null;
    private String script = null;
    private static Pattern unicodePattern = Pattern.compile("\\\\u[a-fA-F0-9][a-fA-F0-9][a-fA-F0-9][a-fA-F0-9]");

    /**
     * Create a new value.
     */
    public Value() { }

    /**
     * Create a new value.
     * @param caju CajuScript.
     * @param context Context for this value.
     * @param syntax Syntax for this value.
     */
    public Value(CajuScript caju, Context context, Syntax syntax) {
        cajuScript = caju;
        this.context = context;
        this.syntax = syntax;
    }

    /**
     * Get script.
     * @return Script.
     */
    public String getScript() {
        return script;
    }

    /**
     * Set script.
     * @param s Script.
     * @throws org.cajuscript.CajuScriptException Errors loading the script.
     */
    public final void setScript(String s) throws CajuScriptException {
        try {
            if (script != null && script.equals(s)) {
                if (isCommand()) {
                    setCommand(script);
                }
                return;
            }
            _isCommand = false;
            script = s;
            script = script.trim();
            if (script.length() == 0) {
                return;
            } else if ((script.startsWith("'") && script.endsWith("'"))
                        || (script.startsWith("\"") && script.endsWith("\""))) {
                script = script.replace((CharSequence)"\\t", (CharSequence)"\t");
                script = script.replace((CharSequence)"\\r", (CharSequence)"\r");
                script = script.replace((CharSequence)"\\n", (CharSequence)"\n");
                script = script.replace((CharSequence)"\\\"", (CharSequence)"\"");
                script = script.replace((CharSequence)"\\\\", (CharSequence)"\\");
                script = script.replace((CharSequence)"\\'", (CharSequence)"'");
                Matcher m = unicodePattern.matcher(script);
                while (m.find()) {
                    script = script.replace((CharSequence)m.group(), (CharSequence)Character.toString((char)Integer.valueOf(m.group().substring(2), 16).intValue()));
                }
                type = Type.STRING;
                valueString = script.substring(1, script.length() - 1);
                value = valueString;
            } else if (syntax.matcherEquals(script, syntax.getNull())) {
                value = null;
            } else {
                try {
                    loadNumberValue(script, true);
                } catch (Exception e) {
                    if (script.toLowerCase().equals("true")) {
                        value = true;
                        valueBoolean = true;
                        valueString = "true";
                        type = Type.BOOLEAN;
                    } else if (script.toLowerCase().equals("false")) {
                        value = false;
                        valueBoolean = false;
                        valueString = "false";
                        type = Type.BOOLEAN;
                    } else {
                        setCommand(script);
                    }
                }
            }
        } catch (CajuScriptException e) {
            throw e;
        } catch (Exception e) {
            throw CajuScriptException.create(cajuScript, context, "Invalid value: ".concat(script), e);
        }
    }
    
    /**
     * Get context.
     * @return Context.
     */
    public Context getContext() {
        return context;
    }
    
    /**
     * Set context.
     * @param context Context.
     */
    public void setContext(Context context) {
        this.context = context;
    }
    
    /**
     * Get syntax.
     * @return Syntax.
     */
    public Syntax getSyntax() {
        return syntax;
    }
    
    /**
     * Set syntax.
     * @param syntax Syntax.
     */
    public void setSyntax(Syntax syntax) {
        this.syntax = syntax;
    }
    
    /**
     * If value is command type.
     * @return Is command or not.
     */
    public boolean isCommand() {
        return _isCommand;
    }
    
    /**
     * Get command.
     * @return Command.
     */
    public String getCommand() {
        return command;
    }
    
    /**
     * Set command.
     * @param script Command script.
     * @throws org.cajuscript.CajuScriptException Errors loading command.
     */
    public void setCommand(String script) throws CajuScriptException {
        _isCommand = true;
        Value v = null;
        boolean varMode = false;
        if (scriptCommand == null || !scriptCommand.getScript().equals(script)) {
            script = script.trim();
            script = script.replace((CharSequence)" ", (CharSequence)"");
            script = script.replace((CharSequence)"\t", (CharSequence)"");
            command = script;
            if (scriptCommand != null && !script.equals(scriptCommand.getScript())) {
                scriptCommand = null;
            }
            SyntaxPosition syntaxRootContext = syntax.matcherPosition(script, syntax.getRootContext());
            SyntaxPosition syntaxPathSeparator = syntax.matcherPosition(script, syntax.getFunctionCallPathSeparator());
            SyntaxPosition syntaxParamBegin = syntax.matcherPosition(script, syntax.getFunctionCallParametersBegin());
            SyntaxPosition syntaxParamEnd = syntax.matcherPosition(script, syntax.getFunctionCallParametersEnd());
            SyntaxPosition syntaxArrayParamBegin = syntax.matcherPosition(script, syntax.getArrayCallParametersBegin());
            SyntaxPosition syntaxArrayParamEnd = syntax.matcherPosition(script, syntax.getArrayCallParametersEnd());
            SyntaxPosition syntaxArrayParamSeparator = syntax.matcherPosition(script, syntax.getArrayCallParametersSeparator());
            if ((syntaxParamBegin.getStart() > -1 && syntaxParamBegin.getStart() < syntaxParamEnd.getStart()) || (syntaxPathSeparator.getStart() > -1 && syntaxArrayParamBegin.getStart() == -1 && syntaxArrayParamEnd.getStart() == -1)) {
                String path = syntaxParamBegin.getStart() > -1 ? script.substring(0, syntaxParamBegin.getStart()) : script;
                if (syntaxRootContext.getStart() == 0) {
                    path = path.substring(syntaxRootContext.getEnd());
                    syntaxPathSeparator = syntax.matcherPosition(path, syntax.getFunctionCallPathSeparator());
                    script = script.substring(syntaxRootContext.getEnd());
                }
                path = path.trim();
                String name = syntaxPathSeparator.getStart() > -1 && syntaxPathSeparator.getEnd() < syntaxParamBegin.getStart() ? path.substring(0, syntaxPathSeparator.getStart()) : path;
                name = name.trim();
                Function func = cajuScript.getFunc(path);
                Value val = null;
                boolean isRootContext = false;
                if (syntaxRootContext.getStart() != 0) {
                    val = context.getVar(name);
                    if (val == null) {
                        val = cajuScript.getVar(name);
                    }
                } else {
                    isRootContext = true;
                    val = cajuScript.getVar(name);
                    script = script.substring(name.length());
                }
                if (func != null) {
                    scriptCommand = new ScriptCommand(script, ScriptCommand.Type.FUNCTION);
                    //script = script.substring(path.length());
                    Reflection.invokeFunctionValues(cajuScript, context, syntax, script, scriptCommand);
                    scriptCommand.setClassPath(path);
                } else if (val != null) {
                    String _script = script;
                    if (syntaxPathSeparator.getStart() == -1) {
                        _script = "";
                    }
                    scriptCommand = new ScriptCommand(_script, isRootContext ? ScriptCommand.Type.NATIVE_OBJECT_ROOT : ScriptCommand.Type.NATIVE_OBJECT);
                    scriptCommand.setVar(name);
                    scriptCommand.setValue(val);
                } else {
                    scriptCommand = new ScriptCommand(script, ScriptCommand.Type.NATIVE_CLASS);
                }
            } else {
                if ((syntaxArrayParamBegin.getStart() > -1 && syntaxArrayParamBegin.getStart() < syntaxArrayParamEnd.getStart()) || syntaxArrayParamSeparator.getStart() > -1) {
                    String path = syntaxArrayParamBegin.getStart() > -1 ? script.substring(0, syntaxArrayParamBegin.getStart()) : script;
                    Value val = null;
                    if (syntaxRootContext.getStart() != 0) {
                        val = context.getVar(path);
                        if (val == null) {
                            val = cajuScript.getVar(path);
                        }
                    } else {
                        val = cajuScript.getVar(path);
                        script = script.substring(path.length());
                    }
                    path = path.trim();
                    scriptCommand = new ScriptCommand(script, ScriptCommand.Type.ARRAY);
                    Reflection.invokeArrayValues(cajuScript, context, syntax, script, scriptCommand);
                    scriptCommand.setValue(val);
                } else {
                    varMode = true;
                    if (syntaxRootContext.getStart() == 0) {
                        scriptCommand = new ScriptCommand(script.substring(syntaxRootContext.getEnd()), ScriptCommand.Type.VARIABLE_ROOT);
                    } else {
                        scriptCommand = new ScriptCommand(script, ScriptCommand.Type.VARIABLE);
                    }
                }
            }
        }
        if (scriptCommand != null && scriptCommand.getScript().equals(script)) {
            switch (scriptCommand.getType()) {
                case VARIABLE_ROOT:
                    varMode = true;
                    v = cajuScript.getVar(scriptCommand.getScript());
                    break;
                case VARIABLE:
                    varMode = true;
                    v = context.getVar(scriptCommand.getScript());
                    if (v == null) {
                        v = cajuScript.getVar(scriptCommand.getScript());
                    }
                    break;
                case NATIVE_OBJECT_ROOT:
                    scriptCommand.setValue(cajuScript.getVar(scriptCommand.getVar()));
                    value = Reflection.invokeNative(cajuScript, context, syntax, scriptCommand.getValue().getValue(), scriptCommand.getScript(), scriptCommand);
                    break;
                case NATIVE_OBJECT:
                    scriptCommand.setValue(context.getVar(scriptCommand.getVar()));
                    String _script = scriptCommand.getScript();
                    if (scriptCommand.getFinalScript().length() == 0) {
                        if (scriptCommand.getValue() != null) {
                            SyntaxPosition syntaxPathSeparator = syntax.matcherPosition(_script, syntax.getFunctionCallPathSeparator());
                            _script = _script.substring(syntaxPathSeparator.getEnd());
                            scriptCommand.setFinalScript(_script);
                        }
                    } else {
                        _script = scriptCommand.getFinalScript();
                    }
                    value = Reflection.invokeNative(cajuScript, context, syntax, scriptCommand.getValue() == null ? null : scriptCommand.getValue().getValue(), _script, scriptCommand);
                    break;
                case NATIVE_CLASS:
                    value = Reflection.invokeNative(cajuScript, context, syntax, null, scriptCommand.getScript(), scriptCommand);
                    break;
                case FUNCTION:
                    Context funcContext = new Context();
                    Function func = cajuScript.getFunc(scriptCommand.getClassPath());
                    Reflection.invokeValues(cajuScript, context, syntax, script, scriptCommand, null, null, null);
                    value = func.invoke(cajuScript, funcContext, syntax, Reflection.invokeValues(cajuScript, context, syntax, null, scriptCommand, null, null, null)).getValue();
                    break;
                case ARRAY:
                    type = Type.ARRAY;
                    throw new Error("Array constructor not implemented yet.");
                    //value = new Array(scriptCommand.getParams(), scriptCommand.getParamsValues());
                    //break;
                default:
                    break;
            }
        }
        if (varMode) {
            if (v == null) {
                SyntaxPosition syntaxParamBegin = syntax.matcherPosition(script, syntax.getFunctionCallParametersBegin());
                SyntaxPosition syntaxParamEnd = syntax.matcherPosition(script, syntax.getFunctionCallParametersEnd());
                if (script.indexOf(' ') > -1 || syntaxParamBegin.getStart() > -1 || syntaxParamEnd.getStart() > -1) {
                    throw CajuScriptException.create(cajuScript, context, "Syntax error");
                }
                throw CajuScriptException.create(cajuScript, context, script.concat(" is not defined"));
            }
            value = v.getValue();
        }
        setValue(value);
    }
    
    /**
     * Get value in string.
     * @return String value.
     */
    public String getStringValue() {
        return valueString;
    }
    
    /**
     * Get value in boolean.
     * @return Integer value.
     */
    public boolean getBooleanValue() {
        return valueBoolean;
    }
    
    /**
     * Get value in number.
     * @return Number value.
     */
    public double getNumberValue() {
        switch (typeNumber) {
            case INTEGER:
                return (double)valueNumberInteger;
            case FLOAT:
                return (double)valueNumberFloat;
            case LONG:
                return (double)valueNumberLong;
            case DOUBLE:
                return valueNumberDouble;
            default:
                return 0d;
        }
    }
    
    /**
     * Get value in integer.
     * @return Integer value.
     */
    public int getNumberIntegerValue() {
        return valueNumberInteger;
    }
    
    /**
     * Get value in long.
     * @return Long value.
     */
    public long getNumberLongValue() {
        return valueNumberLong;
    }
    
    /**
     * Get value in float.
     * @return Float value.
     */
    public float getNumberFloatValue() {
        return valueNumberFloat;
    }
    
    /**
     * Get value in double.
     * @return Double value.
     */
    public double getNumberDoubleValue() {
        return valueNumberDouble;
    }

    /**
     * Get the Script Command.
     * @return Script Command.
     */
    public ScriptCommand getScriptCommand() {
        return scriptCommand;
    }

    /**
     * Get the object of value.
     * @return Value.
     */
    public Object getValue() {
        return value;
    }
    
    /**
     * Define the value.
     * @param value Objet to be the value.
     */
    public void setValue(Object value) throws CajuScriptException {
        valueNumberInteger = 0;
        valueNumberLong = 0;
        valueNumberFloat = 0;
        valueNumberDouble = 0;
        valueString = "";
        type = Type.NULL;
        typeNumber = null;
        this.value = value;
        if (value == null) {
            return;
        } else if (value instanceof Boolean) {
            type = Type.BOOLEAN;
            if (((Boolean)value).booleanValue()) {
                valueBoolean = true;
                valueString = "true";
            } else {
                valueBoolean = false;
                valueString = "false";
            }
            classType = Boolean.class;
            return;
        } else if (value instanceof Integer) {
            valueNumberInteger = ((Integer)value).intValue();
            valueNumberLong = (long)valueNumberInteger;
            valueNumberFloat = (float)valueNumberInteger;
            valueNumberDouble = (double)valueNumberInteger;
            valueString = Integer.toString(valueNumberInteger);
            type = Type.NUMBER;
            typeNumber = TypeNumber.INTEGER;
            classType = Integer.class;
            return;
        } else if (value instanceof Float) {
            valueNumberFloat = ((Float)value).floatValue();
            valueNumberDouble = (double)valueNumberFloat;
            valueString = Float.toString(valueNumberFloat);
            type = Type.NUMBER;
            typeNumber = TypeNumber.FLOAT;
            classType = Float.class;
            return;
        } else if (value instanceof Long) {
            valueNumberLong = ((Long)value).longValue();
            valueNumberDouble = (double)valueNumberLong;
            valueString = Long.toString(valueNumberLong);
            type = Type.NUMBER;
            typeNumber = TypeNumber.LONG;
            classType = Long.class;
            return;
        } else if (value instanceof Double) {
            valueNumberDouble = (float)((Double)value).doubleValue();
            valueString = Double.toString(valueNumberDouble);
            type = Type.NUMBER;
            typeNumber = TypeNumber.DOUBLE;
            classType = Double.class;
            return;
        }
        if (value instanceof CharSequence || value instanceof Character) {
            type = Type.STRING;
            valueString = value.toString();
            classType = String.class;
        } else {
            type = Type.OBJECT;
            classType = value.getClass();
        }
        valueNumberInteger = 0;
        valueNumberLong = 0;
        valueNumberFloat = 0;
        valueNumberDouble = 0;
    }
    
    /**
     * Get the type internal of value.
     * Types: Type.NULL = 0, Type.NUMBER = 1, Type.STRING = 2, Type.OBJECT = 3.
     * @return Number of type.
     */
    public Type getType() {
        return type;
    }
    
    /**
     * Get the type internal of number value.
     * Types: TypeNumber.INTEGER = 1, TypeNumber.LONG = 2, TypeNumber.FLOAT = 3, TypeNumber.DOUBLE = 4.
     * @return Number of type.
     */
    public TypeNumber getTypeNumber() {
        return typeNumber;
    }

    /**
     * Get the class type.
     * @return Class type.
     */
    public Class getClassType() {
        return classType;
    }

    /**
     * Set the class type
     * @param classType Class type.
     */
    public void setClassType(Class classType) {
        this.classType = classType;
    }

    /**
     * Set the class type from a class path.
     * @param path Class path
     * @throws CajuScriptException Exception if the class not found.
     */
    public void setClassType(String path) throws CajuScriptException {
        if (path.length() != 0) {
            Class c = cajuScript.getContext().findClass(path);
            if (c == null) {
                throw CajuScriptException.create(cajuScript, context, "Class \"".concat(path).concat("\" not found "));
            }
            setClassType(c);
        }
    }

    /**
     * Get flag definition.
     * @return Information.
     */
    public String getFlag() {
        return flag;
    }
    
    /**
     * Set flag definition.
     * @param flag Information.
     */
    public void setFlag(String flag) {
        this.flag = flag;
    }
    
    /**
     * Get value in string.
     * @return Value in string.
     */
    @Override
    public String toString() {
        if (value == null) {
            return "";
        }
        return value.toString();
    }
    
    private void loadNumberValue(Object o, boolean loadValue) throws Exception {
        if (o instanceof Integer) {
            valueNumberInteger = ((Integer)o).intValue();
            valueNumberLong = valueNumberInteger;
            valueNumberFloat = valueNumberInteger;
            valueNumberDouble = valueNumberInteger;
            valueString = Integer.toString(valueNumberInteger);
            type = Type.NUMBER;
            typeNumber = TypeNumber.INTEGER;
            return;
        } else if (o instanceof Float) {
            valueNumberFloat = ((Float)o).floatValue();
            valueNumberDouble = Double.valueOf(Float.toString(valueNumberFloat));
            valueString = Float.toString(valueNumberFloat);
            type = Type.NUMBER;
            typeNumber = TypeNumber.FLOAT;
            return;
        } else if (o instanceof Long) {
            valueNumberLong = ((Long)o).longValue();
            valueNumberDouble = valueNumberLong;
            valueString = Long.toString(valueNumberLong);
            type = Type.NUMBER;
            typeNumber = TypeNumber.LONG;
            return;
        } else if (o instanceof Double) {
            valueNumberDouble = (float)((Double)o).doubleValue();
            valueString = Double.toString(valueNumberDouble);
            type = Type.NUMBER;
            typeNumber = TypeNumber.DOUBLE;
            return;
        }
        Double v = (Double)cajuScript.cast(o, "d");
        double d = v.doubleValue();
        if ((long)d == d) {
            if (d <= Integer.MAX_VALUE && d >= Integer.MIN_VALUE) {
                if (loadValue) {
                   value = Integer.valueOf((int)d);
                }
                loadNumberValue(Integer.valueOf((int)d), false);
            } else {
                if (loadValue) {
                   value = Long.valueOf((long)d);
                }
                loadNumberValue(Long.valueOf((long)d), false);
            }
        } else {
            if (Double.doubleToLongBits(d) >= Double.doubleToLongBits(Float.MIN_VALUE)
                && Double.doubleToLongBits(d) <= Double.doubleToLongBits(Float.MAX_VALUE)) {
                if (loadValue) {
                   value = Float.valueOf((float)d);
                }
                loadNumberValue(Float.valueOf((float)d), false);
            } else {
                if (loadValue) {
                   value = Double.valueOf(v);
                }
                loadNumberValue(v, false);
            }
        }
    }
    
    /**
     * Clone.
     * @return Object cloned.
     */
    @Override
    public Value clone() {
        Value v = new Value();
        v.value = this.value;
        v.valueNumberInteger = this.valueNumberInteger;
        v.valueNumberLong = this.valueNumberLong;
        v.valueNumberFloat = this.valueNumberFloat;
        v.valueNumberDouble = this.valueNumberDouble;
        v.valueString = this.valueString;
        v.valueBoolean = this.valueBoolean;
        v.typeNumber = this.typeNumber;
        v.type = this.type;
        v._isCommand = this._isCommand;
        v.command = this.command;
        v.cajuScript = this.cajuScript;
        v.context = this.context;
        v.syntax = this.syntax;
        v.flag = this.flag;
        v.scriptCommand = this.scriptCommand;
        v.script = this.script;
        return v;
    }
}
