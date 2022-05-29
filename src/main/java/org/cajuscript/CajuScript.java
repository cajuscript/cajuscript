/*
 * CajuScript.java
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

import java.io.Reader;
import java.util.Collection;
import java.util.Enumeration;
import org.cajuscript.parser.LineDetail;

import java.util.HashMap;
import java.util.Set;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.cajuscript.parser.Function;
import org.cajuscript.parser.Base;
import org.cajuscript.compiler.Compiler;

/**
 * The core of the <code>CajuScript</code> language.
 * <p>
 * Sample:
 * </p>
 * <p>
 * <blockquote>
 * 
 * <pre>
 * try {
 * 	CajuScript caju = new CajuScript();
 * 	String javaHello = &quot;Java: Hello!&quot;;
 * 	caju.set(&quot;javaHello&quot;, javaHello);
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
 * 	CajuScript caju = new CajuScript();
 * 	caju.evalFile(&quot;file.cj&quot;);
 * } catch (Exception e) {
 * 	e.printStackTrace();
 * }
 * </pre>
 * 
 * </blockquote>
 * </p>
 * 
 * @author eduveks
 */
public class CajuScript {

    /**
     * Core name.
     */
    public static final String NAME = "CajuScript";
    /**
     * Core version.
     */
    public static final String VERSION = "0.5";
    /**
     * Language version.
     */
    public static final String LANGUAGE_VERSION = "0.3";
    /**
     * Line Limiter.
     */
    public static final String LINE_LIMITER = "\n";
    /**
     * SubLine Limiter.
     */
    public static final String SUBLINE_LIMITER = ";";
    /**
     * Prefix of the variables created automaticaly.
     */
    public static final String CAJU_VARS = "__caju";
    /**
     * Strings along the code are replaced by statics variables with this name.
     */
    public static final String CAJU_VARS_STATIC_STRING = CAJU_VARS.concat("_static_string_");
    /**
     * Commands embraced by parenthesis are executed and the value is saved on
     * variables with this name. All parenthesis are replaced by variables with
     * the final value when the line is interpreted.
     */
    public static final String CAJU_VARS_GROUP = CAJU_VARS.concat("_group_");
    /**
     * Commands embraced by parenthesis are executed and the value is saved on
     * variables with this name. All parenthesis are replaced by variables with
     * the final value when the line is interpreted.
     */
    public static final String CAJU_VARS_MATH = CAJU_VARS.concat("_math_");
    /**
     * Variable name to configure the base directory to store classes compiled.
     */
    public static final String CAJU_VAR_COMPILE_BASEDIRECTORY = "caju.compile.baseDirectory";
    /**
     * Variable name to configure the class path to be used for compile scripts
     * in runtime.
     */
    public static final String CAJU_VAR_COMPILE_CLASSPATH = "caju.compile.classPath";
    /**
     * Functions parameters are going to variables setting with this name.
     */
    public static final String CAJU_VARS_PARAMETER = CAJU_VARS.concat("_param_");
    public static final String LINE_DETAIL_START = "#caju_line$";
    public static final String LINE_DETAIL_END = ":";
    private static final Map<String, Syntax> globalSyntaxs = new HashMap<String, Syntax>();
    private Context context = new Context();
    private LineDetail runningLine = new LineDetail(0, "");
    private Syntax syntax = new Syntax();
    private org.cajuscript.parser.Base parserBase = null;
    private Map<String, Syntax> syntaxs = new HashMap<String, Syntax>();
    private static Map<String, String> cacheScripts = new HashMap<String, String>();
    private static Map<String, Base> cacheParsers = new HashMap<String, Base>();
    private static Map<String, Context> cacheStaticContexts = new HashMap<String, Context>();
    private static long staticVarsStringCounter = 1;
    private String compileBaseDirectory = "cajuscript-classes";
    private String compileClassPath = "";
    private int varsCounter = 0;

    /**
     * Create a newly instance of Caju Script. The variables caju and array are
     * initialized.
     *
     * @throws org.cajuscript.CajuScriptException
     *             Problems on starting.
     */
    public CajuScript() throws CajuScriptException {
        context.setVar("caju", toValue(this));
        context.setVar("array", toValue(new Array()));
    }

    /**
     * Counter to variables names created on runtime.
     *
     * @return Next counter to be used how variable name.
     */
    public String nextVarsCounter() {
        varsCounter = varsCounter + 1;
        return "_".concat(Long.toString(varsCounter)).concat("_");
    }

    /**
     * Add custom syntax for all instances of CajuScript.
     *
     * @param name
     *            Syntax name.
     * @param syntax
     *            Syntax instance.
     */
    public static void addGlobalSyntax(String name, Syntax syntax) {
        globalSyntaxs.put(name, syntax);
    }

    /**
     * Get global custom syntax by name.
     *
     * @param name
     *            Syntax name.
     * @return Syntax instance.
     */
    public static Syntax getGlobalSyntax(String name) {
        return globalSyntaxs.get(name);
    }

    /**
     * Get default syntax.
     *
     * @return Syntax.
     */
    public Syntax getSyntax() {
        return syntax;
    }

    /**
     * Set default syntax.
     *
     * @param s
     *            Syntax.
     */
    public void setSyntax(Syntax s) {
        this.syntax = s;
    }

    /**
     * Add custom syntax.
     *
     * @param name
     *            Syntax name.
     * @param syntax
     *            Syntax instance.
     */
    public void addSyntax(String name, Syntax syntax) {
        syntaxs.put(name, syntax);
    }

    /**
     * Get custom syntax by name.
     *
     * @param name
     *            Syntax name.
     * @return Syntax instance.
     */
    public Syntax getSyntax(String name) {
        return syntaxs.get(name);
    }

    /**
     * Get root context.
     *
     * @return Context.
     */
    public Context getContext() {
        return context;
    }

    /**
     * Set root context.
     *
     * @param c
     *            Context.
     */
    public void setContext(Context c) {
        this.context = c;
    }

    /**
     * Parser base.
     *
     * @return Parser base.
     */
    public Base getParserBase() {
        return parserBase;
    }

    /**
     * Get compile base directory.
     *
     * @return Directory to put classes compiled
     */
    public String getCompileBaseDirectory() {
        return compileBaseDirectory;
    }

    /**
     * Set compile base directory.
     *
     * @param baseDirectory
     *            Directory to put classes compiled
     */
    public void setCompileBaseDirectory(String baseDirectory) {
        compileBaseDirectory = baseDirectory;
    }

    /**
     * Get compile class path.
     *
     * @return Class path with dependencies to be used by the compiler can be
     *         used.
     */
    public String getCompileClassPath() {
        return compileClassPath;
    }

    /**
     * Set compile class path.
     *
     * @param classPath
     *            Class path with dependencies to be used by the compiler can be
     *            used.
     */
    public void setCompileClassPath(String classPath) {
        compileClassPath = classPath;
    }

    /**
     * Get line detail in execution.
     *
     * @return Line detail.
     */
    public LineDetail getRunningLine() {
        return runningLine;
    }

    /**
     * Set line detail in execution.
     *
     * @param l
     *            Line detail.
     */
    public void setRunningLine(LineDetail l) {
        runningLine = l;
    }

    /**
     * Script execute.
     *
     * @param script
     *            Script to be executed.
     * @return Value returned by script.
     * @throws org.cajuscript.CajuScriptException
     *             Errors ocurred on script execution.
     */
    public Value eval(String script) throws CajuScriptException {
        return eval(script, getGlobalSyntax("Caju"), true);
    }

    /**
     * Script execute.
     *
     * @param script
     *            Script to be executed.
     * @param execute
     *            If can be executed.
     * @return Value returned by script.
     * @throws org.cajuscript.CajuScriptException
     *             Errors ocurred on script execution.
     */
    public Value eval(String script, boolean execute)
            throws CajuScriptException {
        return eval(script, getGlobalSyntax("Caju"), execute);
    }

    /**
     * Script execute with specific syntax.
     *
     * @param script
     *            Script to be executed.
     * @param syntax
     *            Syntax of the script.
     * @return Value returned by script.
     * @throws org.cajuscript.CajuScriptException
     *             Errors ocurred on script execution.
     */
    public Value eval(String script, Syntax syntax) throws CajuScriptException {
        return eval(script, syntax, true);
    }

    /**
     * Script execute with specific syntax.
     *
     * @param script
     *            Script to be executed.
     * @param syntax
     *            Syntax of the script.
     * @param execute
     *            If can be executed.
     * @return Value returned by script.
     * @throws org.cajuscript.CajuScriptException
     *             Errors ocurred on script execution.
     */
    public Value eval(String script, Syntax syntax, boolean execute)
            throws CajuScriptException {
        Syntax syntaxBackup = getSyntax();
        try {
            String originalScript = script;
            if (script.length() == 0) {
                return null;
            }
            script = script.concat(LINE_LIMITER);
            script = script.replace((CharSequence) "\r\n", LINE_LIMITER);
            script = script.replace((CharSequence) "\n\r", LINE_LIMITER);
            script = script.replace((CharSequence) "\n", LINE_LIMITER);
            script = script.replace((CharSequence) "\r", LINE_LIMITER);
            String[] lines = script.split(LINE_LIMITER);
            script = "";
            StringBuilder scriptFinal = new StringBuilder();
            String staticStringKey = "";
            String staticStringValue = "";
            String previousLine = "";
            boolean isString1 = false;
            boolean isString2 = false;
            int lineNumber = 0;
            String cacheId = "";
            boolean config = true;
            Context staticContexts = null;
            Base cacheParser = null;
            String cacheScript = null;
            String compilePath = null;
            lines:
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i].trim();
                lineNumber++;
                if (config) {
                    while (true) {
                        int lineLimiter = line.indexOf(SUBLINE_LIMITER);
                        String configLine = line;
                        if (lineLimiter > -1) {
                            configLine = configLine.substring(0, lineLimiter);
                        }
                        configLine = configLine.replace('\t', ' ').trim();
                        if (configLine.startsWith("caju.syntax")) {
                            String syntaxName = configLine.substring(configLine.lastIndexOf(' ') + 1);
                            Syntax _syntax = getSyntax(syntaxName);
                            Syntax __syntax = getGlobalSyntax(syntaxName);
                            if (_syntax != null) {
                                syntax = _syntax;
                            } else if (__syntax != null) {
                                syntax = __syntax;
                            } else {
                                throw CajuScriptException.create(this, context,
                                        "Syntax \"".concat(syntaxName).concat(
                                        "\" not found."));
                            }
                            setSyntax(syntax);
                        } else if (configLine.startsWith("caju.cache")) {
                            cacheId = configLine.substring(configLine.lastIndexOf(' ') + 1);
                            cacheParser = cacheParsers.get(cacheId);
                            cacheScript = cacheScripts.get(cacheId);
                            if (cacheParser != null && originalScript.equals(cacheScript)) {
                                staticContexts = cacheStaticContexts.get(cacheId);
                            } else if (!(cacheId.length() == 0)) {
                                staticContexts = new Context();
                            }
                        } else if (configLine.startsWith("caju.compile.baseDirectory")) {
                            set(CAJU_VAR_COMPILE_BASEDIRECTORY, configLine.substring(configLine.lastIndexOf(' ') + 1).trim());
                        } else if (configLine.startsWith("caju.compile.classPath")) {
                            set(CAJU_VAR_COMPILE_CLASSPATH, configLine.substring(configLine.lastIndexOf(' ') + 1).trim());
                        } else if (configLine.startsWith("caju.compile")) {
                            if (exists(CAJU_VAR_COMPILE_BASEDIRECTORY)) {
                                setCompileBaseDirectory((String) get(CAJU_VAR_COMPILE_BASEDIRECTORY));
                            }
                            if (exists(CAJU_VAR_COMPILE_CLASSPATH)) {
                                setCompileClassPath((String) get(CAJU_VAR_COMPILE_CLASSPATH));
                            }
                            compilePath = configLine.substring(
                                    configLine.lastIndexOf(' ') + 1).trim();
                            staticContexts = new Context();
                        } else {
                            if (!(configLine.length() == 0)) {
                                config = false;
                                break;
                            }
                        }
                        if (lineLimiter > -1) {
                            line = line.substring(lineLimiter + 1);
                        } else {
                            continue lines;
                        }
                    }
                }
                if (!config && cacheParser != null && originalScript.equals(cacheScript)) {
                    Set<String> keys = staticContexts.getAllKeys(true);
                    for (String key : keys) {
                        context.setVar(key, staticContexts.getVar(key));
                    }
                    keys = staticContexts.getStaticStrings().keySet();
                    for (String key : keys) {
                        context.setVar(key, staticContexts.getVar(key));
                    }
                    Map<String, Function> funcs = staticContexts.getFuncs();
                    keys = funcs.keySet();
                    for (String key : keys) {
                        context.setFunc(key, staticContexts.getFunc(key));
                    }
                    parserBase = cacheParser;
                    Value finalValue = parserBase.execute(this, context, syntax);
                    parserBase.clear();
                    return finalValue;
                }
                if (!config && compilePath != null) {
                    Compiler compiler = new Compiler(this, compilePath);
                    if (compiler.isLatest(originalScript)) {
                        return compiler.execute(context, syntax);
                    }
                }
                if (isString1 || isString2) {
                    setRunningLine(new LineDetail(lineNumber, previousLine));
                    throw CajuScriptException.create(this, context,
                            "String not closed");
                }
                String lineGarbage = line.trim();
                if (lineGarbage.length() == 0) {
                    continue;
                }
                for (Pattern comment : syntax.getComments()) {
                    Matcher m = comment.matcher(lineGarbage);
                    if (m.find() && m.start() == 0) {
                        continue lines;
                    }
                }
                char[] chars = line.toCharArray();
                previousLine = line;
                isString1 = false;
                isString2 = false;
                char cO = (char) -1;
                for (char c : chars) {
                    switch (c) {
                        case '\'':
                            if (cO != '\\' && !isString2) {
                                if (isString1) {
                                    isString1 = false;
                                    Value valueString = new Value(null, null, null);
                                    valueString.setScript("'".concat(staticStringValue).concat("'"));
                                    context.setVar(staticStringKey, valueString);
                                    if (staticContexts != null) {
                                        Value valueStringStatic = new Value(null, null, null);
                                        valueStringStatic.setScript("'".concat(staticStringValue).concat("'"));
                                        staticContexts.setVar(staticStringKey, valueStringStatic);
                                    }
                                    line = line.replace(
                                            (CharSequence) ("'".concat(staticStringValue).concat("'")), staticStringKey);
                                    staticStringKey = "";
                                    staticStringValue = "";
                                } else {
                                    isString1 = true;
                                    if (staticVarsStringCounter == Long.MAX_VALUE) {
                                        staticVarsStringCounter = 0;
                                    }
                                    staticStringKey = CAJU_VARS_STATIC_STRING.concat(nextVarsCounter()).concat(
                                            Long.toString(staticVarsStringCounter));
                                    staticVarsStringCounter++;
                                }
                            } else if (isString2 || cO == '\\') {
                                staticStringValue = staticStringValue.concat(Character.toString(c));
                            }
                            break;
                        case '"':
                            if (cO != '\\' && !isString1) {
                                if (isString2) {
                                    isString2 = false;
                                    Value valueString = new Value(null, null, null);
                                    valueString.setScript("\"".concat(staticStringValue).concat("\""));
                                    context.setVar(staticStringKey, valueString);
                                    if (staticContexts != null) {
                                        Value valueStringStatic = new Value(null, null, null);
                                        valueStringStatic.setScript("\"".concat(staticStringValue).concat("\""));
                                        staticContexts.setVar(staticStringKey, valueStringStatic);
                                    }
                                    line = line.replace((CharSequence) ("\"".concat(staticStringValue).concat("\"")), staticStringKey);
                                    staticStringKey = "";
                                    staticStringValue = "";
                                } else {
                                    isString2 = true;
                                    if (staticVarsStringCounter == Long.MAX_VALUE) {
                                        staticVarsStringCounter = 0;
                                    }
                                    staticStringKey = CAJU_VARS_STATIC_STRING.concat(nextVarsCounter()).concat(
                                            Long.toString(staticVarsStringCounter));
                                    staticVarsStringCounter++;
                                }
                            } else if (isString1 || cO == '\\') {
                                staticStringValue = staticStringValue.concat(Character.toString(c));
                            }
                            break;
                        default:
                            if (isString1 || isString2) {
                                staticStringValue = staticStringValue.concat(Character.toString(c));
                            }
                            break;
                    }
                    cO = c;
                }
                scriptFinal.append(LINE_DETAIL_START);
                scriptFinal.append(Integer.toString(lineNumber));
                scriptFinal.append(LINE_DETAIL_END);
                scriptFinal.append(line);
                scriptFinal.append(SUBLINE_LIMITER);
            }
            lines = scriptFinal.toString().split(SUBLINE_LIMITER);
            scriptFinal = new StringBuilder();
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                String lineN = "";
                if (line.startsWith(LINE_DETAIL_START)) {
                    lineN = line.substring(0, line.indexOf(LINE_DETAIL_END) + 1);
                    line = line.substring(lineN.length()).trim();
                }
                while (true) {
                    line = line.trim();
                    int p = endLineIndex(line, syntax);
                    if (p > -1) {
                        scriptFinal.append(lineN);
                        scriptFinal.append(line.substring(0, p));
                        scriptFinal.append(SUBLINE_LIMITER);
                        line = line.substring(p);
                    } else {
                        scriptFinal.append(lineN);
                        scriptFinal.append(line);
                        scriptFinal.append(SUBLINE_LIMITER);
                        break;
                    }
                }
            }
            script = scriptFinal.toString();
            parserBase = new org.cajuscript.parser.Base(new LineDetail(-1, ""));
            parserBase.parse(this, script, syntax);
            if (!(cacheId.length() == 0)) {
                cacheScripts.put(cacheId, originalScript);
                cacheParsers.put(cacheId, (Base) parserBase.cloneSerialization());
                cacheStaticContexts.put(cacheId, staticContexts);
            }
            if (compilePath != null) {
                Compiler compiler = new Compiler(this, compilePath);
                compiler.compile(staticContexts, originalScript, parserBase);
            }
            if (execute) {
                Value finalValue = parserBase.execute(this, context, syntax);
                if (!(cacheId.length() == 0)) {
                    Map<String, Function> funcs = context.getFuncs();
                    Set<String> keys = funcs.keySet();
                    Context cacheContext = cacheStaticContexts.get(cacheId);
                    for (String key : keys) {
                        cacheContext.setFunc(key, funcs.get(key));
                    }
                }
                return finalValue;
            } else {
                return null;
            }
        } catch (CajuScriptException e) {
            throw e;
        } catch (Throwable t) {
            throw CajuScriptException.create(this, context, t.getMessage(), t);
        } finally {
            setSyntax(syntaxBackup);
        }
    }

    private int endLineIndex(String line, Syntax syntax) {
        if (line.length() == 0) {
            return -1;
        }
        SyntaxPosition syntaxPosition = null;
        int l = -1;
        if ((l = syntax.matcherPosition(line, syntax.getLabel()).getEnd()) > -1) {
            line = line.substring(l);
        }
        int[] starts = new int[12];
        int[] ends = new int[starts.length];
        for (int i = 0; i < starts.length; i++) {
            starts[i] = Integer.MAX_VALUE;
            ends[i] = Integer.MAX_VALUE;
        }
        if ((syntaxPosition = syntax.matcherPosition(line, syntax.getIf())).getEnd() > -1) {
            starts[0] = syntaxPosition.getStart();
            ends[0] = syntaxPosition.getEnd();
        }
        if ((syntaxPosition = syntax.matcherPosition(line, syntax.getElseIf())).getEnd() > -1) {
            starts[1] = syntaxPosition.getStart();
            ends[1] = syntaxPosition.getEnd();
        }
        if ((syntaxPosition = syntax.matcherPosition(line, syntax.getElse())).getEnd() > -1) {
            starts[2] = syntaxPosition.getStart();
            ends[2] = syntaxPosition.getEnd();
        }
        if ((syntaxPosition = syntax.matcherPosition(line, syntax.getLoop())).getEnd() > -1) {
            starts[3] = syntaxPosition.getStart();
            ends[3] = syntaxPosition.getEnd();
        }
        if ((syntaxPosition = syntax.matcherPosition(line, syntax.getFunction())).getEnd() > -1) {
            starts[4] = syntaxPosition.getStart();
            ends[4] = syntaxPosition.getEnd();
        }
        if ((syntaxPosition = syntax.matcherPosition(line, syntax.getTry())).getEnd() > -1) {
            starts[5] = syntaxPosition.getStart();
            ends[5] = syntaxPosition.getEnd();
        }
        if ((syntaxPosition = syntax.matcherPosition(line, syntax.getTryCatch())).getEnd() > -1) {
            starts[6] = syntaxPosition.getStart();
            ends[6] = syntaxPosition.getEnd();
        }
        if ((syntaxPosition = syntax.matcherPosition(line, syntax.getTryFinally())).getEnd() > -1) {
            starts[7] = syntaxPosition.getStart();
            ends[7] = syntaxPosition.getEnd();
        }
        if ((syntaxPosition = syntax.matcherPosition(line, syntax.getIfEnd())).getEnd() > -1) {
            starts[8] = syntaxPosition.getStart();
            ends[8] = syntaxPosition.getEnd();
        }
        if ((syntaxPosition = syntax.matcherPosition(line, syntax.getLoopEnd())).getEnd() > -1) {
            starts[9] = syntaxPosition.getStart();
            ends[9] = syntaxPosition.getEnd();
        }
        if ((syntaxPosition = syntax.matcherPosition(line, syntax.getFunctionEnd())).getEnd() > -1) {
            starts[10] = syntaxPosition.getStart();
            ends[10] = syntaxPosition.getEnd();
        }
        if ((syntaxPosition = syntax.matcherPosition(line, syntax.getTryEnd())).getEnd() > -1) {
            starts[11] = syntaxPosition.getStart();
            ends[11] = syntaxPosition.getEnd();
        }
        int id = -1;
        for (int i = 0; i < starts.length; i++) {
            if ((id == -1 && ends[i] < Integer.MAX_VALUE) || (ends[i] < Integer.MAX_VALUE && id > -1 && (ends[i] <= starts[id] || (starts[i] < starts[id] && ends[id] == ends[i])))) {
                id = i;
            }
        }
        if (id > -1) {
            if (id == 3 && l > -1) {
                ends[id] += l;
            }
            return ends[id];
        }
        return -1;
    }

    /**
     * File exucute.
     *
     * @param path
     *            File to be executed.
     * @return Value returned by script.
     * @throws org.cajuscript.CajuScriptException
     *             File cannot be executed or error ocurred on execution.
     */
    public Value evalFile(String path) throws CajuScriptException {
        return evalFile(path, getGlobalSyntax("Caju"));
    }

    /**
     * File exucute.
     *
     * @param path
     *            File to be executed.
     * @param execute
     *            If can be executed.
     * @return Value returned by script.
     * @throws org.cajuscript.CajuScriptException
     *             File cannot be executed or error ocurred on execution.
     */
    public Value evalFile(String path, boolean execute)
            throws CajuScriptException {
        return evalFile(path, getGlobalSyntax("Caju"), execute);
    }

    /**
     * File execute with specific syntax.
     *
     * @param path
     *            File to be executed.
     * @param syntax
     *            Syntax of the script in file.
     * @return Value returned by script.
     * @throws org.cajuscript.CajuScriptException
     *             File cannot be executed or error ocurred on execution.
     */
    public Value evalFile(String path, Syntax syntax)
            throws CajuScriptException {
        return evalFile(path, syntax, true);
    }

    /**
     * File execute with specific syntax.
     *
     * @param path
     *            File to be executed.
     * @param syntax
     *            Syntax of the script in file.
     * @param execute
     *            If can be executed.
     * @return Value returned by script.
     * @throws org.cajuscript.CajuScriptException
     *             File cannot be executed or error ocurred on execution.
     */
    public Value evalFile(String path, Syntax syntax, boolean execute)
            throws CajuScriptException {
        java.io.InputStream is = null;
        try {
            is = new java.io.FileInputStream(path);
            byte[] b = new byte[is.available()];
            is.read(b);
            return eval(new String(b), syntax);
        } catch (CajuScriptException e) {
            throw e;
        } catch (Exception e) {
            throw CajuScriptException.create(this, context,
                    "Cannot read file \"".concat(e.getMessage()).concat("\""),
                    e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * Get function.
     *
     * @param key
     *            Function name.
     * @return Function object.
     */
    public Function getFunc(String key) {
        return context.getFunc(key);
    }

    /**
     * Define a function.
     *
     * @param key
     *            Function name.
     * @param func
     *            Object of the function.
     */
    public void setFunc(String key, Function func) {
        context.setFunc(key, func);
    }

    /**
     * Get variable value.
     *
     * @param key
     *            Variable name.
     * @return Variable object.
     */
    public Value getVar(String key) throws CajuScriptException {
        if (key.startsWith(CAJU_VARS_STATIC_STRING)) {
            return context.getStaticStringValue(key);
        } else {
            return context.getVar(key);
        }
    }

    /**
     * Get all name of variables without variables created automaticaly by
     * CajuScript.
     *
     * @return List of all variables names.
     */
    public Set<String> getAllKeys() {
        return getAllKeys(false);
    }

    /**
     * Get all name of variables including variables created automaticaly by
     * CajuScript if parameter are true.
     *
     * @param withCajuVars
     *            Including variables created automaticaly by CajuScript or not.
     * @return List of all variables names.
     */
    public Set<String> getAllKeys(boolean withCajuVars) {
        return context.getAllKeys(withCajuVars);
    }

    /**
     * Setting new variable.
     *
     * @param key
     *            Variable name.
     * @param value
     *            Variable value.
     */
    public void setVar(String key, Value value) {
        context.setVar(key.trim(), value);
    }

    /**
     * Convert Java objects to CajuScript object to be used like value of
     * variables.
     *
     * @param obj
     *            Object to be converted in Value.
     * @return New value generated from object.
     * @throws org.cajuscript.CajuScriptException
     *             Errors.
     */
    public final Value toValue(Object obj) throws CajuScriptException {
        return toValue(obj, getContext(), getSyntax());
    }

    /**
     * Convert Java objects to CajuScript object to be used like value of
     * variables.
     *
     * @param obj
     *            Object to be converted in Value.
     * @param context
     *            Context for this value.
     * @param syntax
     *            Syntax for this value.
     * @return New value generated from object.
     * @throws org.cajuscript.CajuScriptException
     *             Errors.
     */
    public Value toValue(Object obj, Context context, Syntax syntax)
            throws CajuScriptException {
        Value v = new Value(this, context, syntax);
        v.setValue(obj);
        return v;
    }

    /**
     * Defining new variable and setting value from a Java object.
     *
     * @param key
     *            Variable name.
     * @param value
     *            Variable value.
     * @throws org.cajuscript.CajuScriptException
     *             Errors.
     */
    public void set(String key, Object value) throws CajuScriptException {
        if (key.startsWith(CAJU_VARS_STATIC_STRING)) {
            context.setStaticString(key, value.toString());
        } else {
            setVar(key.trim(), toValue(value));
        }
    }

    /**
     * Getting value from variables to Java object.
     *
     * @param key
     *            Variable name.
     * @return Variable value in Java.
     * @throws org.cajuscript.CajuScriptException
     *             Errors.
     */
    public Object get(String key) throws CajuScriptException {
        if (key.startsWith(CAJU_VARS_STATIC_STRING)) {
            return context.getStaticStringValue(key).getValue();
        } else {
            return getVar(key).getValue();
        }
    }

    /**
     * Variable exists?
     *
     * @param key
     *            Variable name.
     * @throws org.cajuscript.CajuScriptException
     *             Errors.
     */
    public boolean exists(String key) throws CajuScriptException {
        if (getVar(key) != null) {
            return true;
        }
        return false;
    }

    /**
     * Get list of all imports used by script in execution.
     *
     * @return List of imports defined.
     */
    public List<String> getImports() {
        return context.getImports();
    }

    /**
     * Define a new import to be used. Only Java package.
     *
     * @param i
     *            The content of importing is only Java package.
     */
    public void addImport(String i) {
        context.addImport(i);
    }

    /**
     * Remove import.
     *
     * @param s
     *            Import content to be removed.
     */
    public void removeImport(String s) {
        context.removeImport(s);
    }

    /**
     * Convert value to type specified.
     *
     * @param value
     *            Value to be converted.
     * @param type
     *            Types: "short" or "sh", "int" or "i", "long" or "l",
     *            "double" or "d", "float" or "f", "char" or "c",
     *            "boolean" or "b", "byte" or "bt",
     *            "string" or "s", "java.ANY_CLASS".
     * @return Object converted.
     * @throws org.cajuscript.CajuScriptException
     *             Errors ocurred on converting.
     */
    public Object cast(Object value, String type) throws Exception {
        if (type.equalsIgnoreCase("short") || type.equalsIgnoreCase("java.lang.Short") || type.equalsIgnoreCase("sh")) {
            return Short.valueOf((short) Double.valueOf(value.toString()).doubleValue());
        }
        if (type.equalsIgnoreCase("int") || type.equalsIgnoreCase("java.lang.Integer") || type.equalsIgnoreCase("i")) {
            return Integer.valueOf((int) Double.valueOf(value.toString()).doubleValue());
        }
        if (type.equalsIgnoreCase("long") || type.equalsIgnoreCase("java.lang.Long") || type.equalsIgnoreCase("l")) {
            return Long.valueOf((long) Double.valueOf(value.toString()).doubleValue());
        }
        if (type.equalsIgnoreCase("double") || type.equalsIgnoreCase("java.lang.Double") || type.equalsIgnoreCase("d")) {
            return Double.valueOf(value.toString());
        }
        if (type.equalsIgnoreCase("float") || type.equalsIgnoreCase("java.lang.Float") || type.equalsIgnoreCase("f")) {
            return Float.valueOf((float) Double.valueOf(value.toString()).doubleValue());
        }
        if (type.equalsIgnoreCase("char") || type.equalsIgnoreCase("java.lang.Character") || type.equalsIgnoreCase("c")) {
            try {
                return (char) Double.valueOf(value.toString()).doubleValue();
            } catch (Exception e) {
                return value.toString().toCharArray()[0];
            }
        }
        if (type.equalsIgnoreCase("boolean") || type.equalsIgnoreCase("java.lang.Boolean") || type.equalsIgnoreCase("b") || type.equalsIgnoreCase("bool")) {
            try {
                if (Double.valueOf(value.toString()).doubleValue() >= 1) {
                    return Boolean.valueOf(true);
                } else {
                    return Boolean.valueOf(false);
                }
            } catch (Exception e) {
                return Boolean.valueOf(value.toString());
            }
        }
        if (type.equalsIgnoreCase("byte") || type.equalsIgnoreCase("java.lang.Byte") || type.equalsIgnoreCase("bt")) {
            try {
                return (byte) (int) Double.valueOf(value.toString()).doubleValue();
            } catch (Exception e) {
                return value.toString().getBytes()[0];
            }
        }
        if (type.equalsIgnoreCase("string") || type.equalsIgnoreCase("java.lang.String") || type.equalsIgnoreCase("s") || type.equalsIgnoreCase("str")) {
            return value.toString();
        }
        return Class.forName(type).cast(value);
    }

    /**
     * Generate an exception.
     *
     * @throws org.cajuscript.CajuScriptException
     *             Exception generated.
     */
    public static void error() throws CajuScriptException {
        throw new CajuScriptException();
    }

    /**
     * Generate an exception with custom message.
     *
     * @param msg
     *            Message of the exception.
     * @throws org.cajuscript.CajuScriptException
     *             Exception generated.
     */
    public static void error(String msg) throws CajuScriptException {
        throw new CajuScriptException(msg);
    }

    /**
     * Generate an specific throwable.
     *
     * @param t
     *            Throwable instance.
     * @throws Throwable
     *             Throwable generated.
     */
    public static void error(Throwable t) throws Throwable {
        throw t;
    }

    /**
     * If two Class are from same type.
     *
     * @param c1
     *            First type, Class.
     * @param c2
     *            Second type, Class.
     * @return Is from same type or not.
     */
    public static boolean isSameType(Class<?> c1, Class<?> c2) {
        return isSameType(c1.getName(), c2.getName());
    }

    /**
     * If two Object are from same type.
     *
     * @param obj1
     *            First type, Object.
     * @param obj2
     *            Second type, Object.
     * @return Is from same type or not.
     */
    public static boolean isSameType(Object obj1, Object obj2) {
        return isSameType(obj1.getClass().getName(), obj2.getClass().getName());
    }

    /**
     * If two Object.getClass().getName() are from same type.
     *
     * @param type1
     *            First type, Object.getClass().getName().
     * @param type2
     *            Second type, Object.getClass().getName().
     * @return Is from same type or not.
     */
    public static boolean isSameType(String type1, String type2) {
        int p = type1.indexOf("$");
        if (p > -1) {
            type1 = type1.substring(0, p);
        }
        p = type2.indexOf("$");
        if (p > -1) {
            type2 = type2.substring(0, p);
        }
        return type1.toLowerCase().endsWith(".".concat(type2.toLowerCase())) || type2.toLowerCase().endsWith(".".concat(type1.toLowerCase())) || type1.equals(type2);
    }

    public static boolean isAlmostSameType(String type1, String type2) {
        int p = type1.indexOf("$");
        if (p > -1) {
            type1 = type1.substring(0, p);
        }
        p = type2.indexOf("$");
        if (p > -1) {
            type2 = type2.substring(0, p);
        }
        if (type1.equals("short") || type1.equals("java.lang.Short")
                && type2.equals("short") || type2.equals("java.lang.Short")) {
            return true;
        }
        if (type1.equals("int") || type1.equals("java.lang.Integer")
                && type2.equals("int") || type2.equals("java.lang.Integer")) {
            return true;
        }
        if (type1.equals("long") || type1.equals("java.lang.Long")
                && type2.equals("long") || type2.equals("java.lang.Long")) {
            return true;
        }
        if (type1.equals("float") || type1.equals("java.lang.Float")
                && type2.equals("float") || type2.equals("java.lang.Float")) {
            return true;
        }
        if (type1.equals("double") || type1.equals("java.lang.Double")
                && type2.equals("double") || type2.equals("java.lang.Double")) {
            return true;
        }
        if (type1.equals("char") || type1.equals("java.lang.Character")
                && type2.equals("char") || type2.equals("java.lang.Character")) {
            return true;
        }
        if (type1.equals("byte") || type1.equals("java.lang.Byte")
                && type2.equals("byte") || type2.equals("java.lang.Byte")) {
            return true;
        }
        if (type1.equals("boolean") || type1.equals("java.lang.Boolean")
                && type2.equals("boolean") || type2.equals("java.lang.Boolean")) {
            return true;
        }
        return false;
    }

    public static boolean hasTypePriorityOverAnother(String type1, String type2) {
        int p = type1.indexOf("$");
        if (p > -1) {
            type1 = type1.substring(0, p);
        }
        p = type2.indexOf("$");
        if (p > -1) {
            type2 = type2.substring(0, p);
        }
        if (type1.equals("byte") || type1.equals("java.lang.Byte")
                && type2.equals("byte") || type2.equals("java.lang.Byte")
                && type2.equals("short") || type2.equals("java.lang.Short")
                && type2.equals("int") || type2.equals("java.lang.Integer")
                && type2.equals("long") || type2.equals("java.lang.Long")
                && type2.equals("float") || type2.equals("java.lang.Float")
                && type2.equals("double") || type2.equals("java.lang.Double")) {
            return true;
        }
        if (type1.equals("short") || type1.equals("java.lang.Short")
                && type2.equals("short") || type2.equals("java.lang.Short")
                && type2.equals("int") || type2.equals("java.lang.Integer")
                && type2.equals("long") || type2.equals("java.lang.Long")
                && type2.equals("float") || type2.equals("java.lang.Float")
                && type2.equals("double") || type2.equals("java.lang.Double")) {
            return true;
        }
        if (type1.equals("int") || type1.equals("java.lang.Integer")
                && type2.equals("int") || type2.equals("java.lang.Integer")
                && type2.equals("long") || type2.equals("java.lang.Long")
                && type2.equals("float") || type2.equals("java.lang.Float")
                && type2.equals("double") || type2.equals("java.lang.Double")) {
            return true;
        }
        if (type1.equals("long") || type1.equals("java.lang.Long")
                && type2.equals("long") || type2.equals("java.lang.Long")
                && type2.equals("double") || type2.equals("java.lang.Double")) {
            return true;
        }
        if (type1.equals("float") || type1.equals("java.lang.Float")
                && type2.equals("float") || type2.equals("java.lang.Float")
                && type2.equals("double") || type2.equals("java.lang.Double")) {
            return true;
        }
        if (type1.equals("double") || type1.equals("java.lang.Double")
                && type2.equals("double") || type2.equals("java.lang.Double")) {
            return true;
        }
        if (type1.equals("char") || type1.equals("java.lang.Character")
                && type2.equals("char") || type2.equals("java.lang.Character")) {
            return true;
        }
        if (type1.equals("boolean") || type1.equals("java.lang.Boolean")
                && type2.equals("boolean") || type2.equals("java.lang.Boolean")) {
            return true;
        }
        return false;
    }

    /**
     * If Class is from primitive type.
     *
     * @param c
     *            Class
     * @return Is from primitive type or not.
     */
    public static boolean isPrimitiveType(Class<?> c) {
        return isPrimitiveType(c.getName());
    }

    /**
     * If Object is from primitive type.
     *
     * @param obj
     *            Object
     * @return Is from primitive type or not.
     */
    public static boolean isPrimitiveType(Object obj) {
        return isPrimitiveType(obj.getClass().getName());
    }

    /**
     * If Object.getClass().getName() is from primitive type.
     *
     * @param type
     *            Object type, Object.getClass().getName().
     * @return Is from primitive type or not.
     */
    public static boolean isPrimitiveType(String type) {
        if (type.equals("short") || type.equals("java.lang.Short")
                || type.equals("int") || type.equals("java.lang.Integer")
                || type.equals("long") || type.equals("java.lang.Long")
                || type.equals("float") || type.equals("java.lang.Float")
                || type.equals("double") || type.equals("java.lang.Double")
                || type.equals("char") || type.equals("java.lang.Character")
                || type.equals("byte") || type.equals("java.lang.Byte")
                || type.equals("boolean") || type.equals("java.lang.Boolean")) {
            return true;
        }
        return false;
    }

    /**
     * If the object is instance of specified class.
     *
     * @param o
     *            Object.
     * @param c
     *            Class.
     * @return If the object is instance of class or not.
     */
    public boolean isInstance(Object o, Class<?> c) {
        if (c.isInstance(o)) {
            return true;
        } else {
            for (int y = 0; y < o.getClass().getClasses().length; y++) {
                if (isSameType(o.getClass().getClasses()[y].getName(), c.getName())) {
                    return true;
                }
            }
        }
        return false;
    }
    private Map<String, Value> eachValues = new HashMap<String, Value>();
    private Map<String, Integer> eachIndexs = new HashMap<String, Integer>();
    private Map<String, Iterator<Object>> eachIterators = new HashMap<String, Iterator<Object>>();
    private Map<String, Enumeration<Object>> eachEnumerations = new HashMap<String, Enumeration<Object>>();

    /**
     * To do loops like "for each":
     * <p><blockquote><pre>
     * caju.each('myValue', myArray) @
     *     System.out.println('Value = '+ myValue);
     *     System.out.println('Index = '+ caju.index('myValue'));
     *     System.out.println('-------------------');
     * &#64;;
     * </pre></blockquote></p>
     * <p><blockquote><pre>
     * caju.each('myValue', myMap) @
     *     System.out.println('Key = '+ caju.key('myValue'));
     *     System.out.println('Value = '+ myValue); System.out.println('Index = '+ caju.index('myValue'));
     *     System.out.println('-------------------' ) ;
     * &#64;;
     * </pre></blockquote></p>
     * <p>
     * Suports only Array, Collection,
     * Enumeration and Map.
     * </p>
     * @param var
     *            Variable to be created for saved the data from the position of
     *            array.
     * @param array
     *            Array to be interacted.
     * @return Return true to continue the loop or false to stoped.
     * @throws org.cajuscript.CajuScriptException
     *             Exception generated.
     */
    public boolean each(String var, Object array) throws CajuScriptException {
        boolean isToContinue = false;
        int index = -1;
        if (array instanceof Collection) {
            Integer iIndex = eachIndexs.get(var);
            Iterator<Object> iterator = eachIterators.get(var);
            Value value = eachValues.get(var);
            if (iIndex == null) {
                iterator = ((Collection<Object>) array).iterator();
                iIndex = Integer.valueOf(index);
                value = toValue(null);
                eachValues.put(var, value);
                eachIterators.put(var, iterator);
            }
            if (iterator.hasNext()) {
                index = iIndex.intValue() + 1;
                value = eachValues.get(var);
                value.setValue(iterator.next());
                setVar(var, value);
                isToContinue = true;
            } else {
                if (iIndex != null) {
                    index = iIndex.intValue();
                }
            }
            eachIndexs.put(var, Integer.valueOf(index));
            return isToContinue;
        } else if (array instanceof Enumeration) {
            Integer iIndex = eachIndexs.get(var);
            Enumeration enumeration = eachEnumerations.get(var);
            Value value = eachValues.get(var);
            if (iIndex == null) {
                enumeration = (Enumeration) array;
                iIndex = Integer.valueOf(index);
                value = toValue(null);
                eachValues.put(var, value);
                eachEnumerations.put(var, enumeration);
            }
            if (enumeration.hasMoreElements()) {
                index = iIndex.intValue() + 1;
                value = eachValues.get(var);
                value.setValue(enumeration.nextElement());
                setVar(var, value);
                isToContinue = true;
            } else {
                if (iIndex != null) {
                    index = iIndex.intValue();
                }
            }
            eachIndexs.put(var, Integer.valueOf(index));
            return isToContinue;
        } else if (array instanceof Map) {
            String iteratorKey = CajuScript.CAJU_VARS.concat("_").concat(var).concat("_iterator");
            Value valueIterator = getVar(iteratorKey);
            if (valueIterator == null) {
                set(iteratorKey, ((Map) array).keySet().iterator());
            }
            Iterator iterator = (Iterator) get(iteratorKey);
            Integer iIndex = eachIndexs.get(var);
            if (iterator.hasNext()) {
                if (iIndex == null) {
                    iIndex = Integer.valueOf(index);
                    eachIndexs.put(var, iIndex);
                }
                index = iIndex.intValue();
                index++;
                Object key = iterator.next();
                set(
                        CajuScript.CAJU_VARS.concat("_").concat(var).concat(
                        "_key"), key);
                set(var, ((Map) array).get(key));
                isToContinue = true;
            } else {
                if (iIndex != null) {
                    index = iIndex.intValue();
                }
            }
            eachIndexs.put(var, Integer.valueOf(index));
            return isToContinue;
        } else {
            int len = Array.size(array);
            if (len > 0) {
                Integer iIndex = eachIndexs.get(var);
                if (iIndex == null) {
                    iIndex = Integer.valueOf(index);
                    eachIndexs.put(var, iIndex);
                }
                index = iIndex.intValue();
                if (index < len - 1) {
                    index++;
                    set(var, Array.get(array, index));
                    isToContinue = true;
                }
            }
            eachIndexs.put(var, Integer.valueOf(index));
            return isToContinue;
        }
    }

    /**
     * Get the index from a interaction at an "for each".
     *
     * @param var
     *            Name of variable to be catch the index.
     * @return Index from the current interaction.
     */
    public int index(String var) {
        return eachIndexs.get(var);
    }

    /**
     * Get the key from a java.util.Map interaction at an "for each".
     *
     * @param var
     *            Name of variable to be found the correspondent key.
     * @return Key referent the current interaction.
     * @throws org.cajuscript.CajuScriptException
     *             Variable with the key not been found.
     */
    public Object key(String var) throws CajuScriptException {
        return get(CajuScript.CAJU_VARS.concat("_").concat(var).concat("_key"));
    }

    /**
     * Clean an variable used in "for each".
     * @param var
     *            Name of variable to be clean.
     */
    public void clear(String var) throws CajuScriptException {
        set(CajuScript.CAJU_VARS.concat("_").concat(var).concat("_key"), null);
        eachIndexs.put(var, null);
        eachValues.put(var, null);
        eachIterators.put(var, null);
        eachEnumerations.put(var, null);
    }

    /**
     * Entry point to running.
     * @param args Arguments.
     */
    public static void main(String[] args) throws CajuScriptException {
        if (args.length > 0) {
            CajuScript caju = new CajuScript();
            caju.set("args", args);
            caju.evalFile(args[0]);
        }
    }


    static {
        globalSyntaxs.put("Caju", new Syntax());
        Syntax syntaxJ = new Syntax();
        syntaxJ.setIf(Pattern.compile("if\\s*([\\s+|[\\s*\\(]][^\\{]+)\\{"));
        syntaxJ.setElseIf(Pattern.compile("\\}\\s*else\\s+if\\s*([\\s+|[\\s*\\(]][^\\{]+)\\{"));
        syntaxJ.setElse(Pattern.compile("\\}\\s*else\\s*\\{"));
        syntaxJ.setIfEnd(Pattern.compile("\\}"));
        syntaxJ.setLoop(Pattern.compile("while\\s*([\\s+|[\\s*\\(]][^\\{]+)\\{"));
        syntaxJ.setLoopEnd(Pattern.compile("\\}"));
        syntaxJ.setTry(Pattern.compile("try\\s*([\\s+|[\\s*\\(]][^\\{]+)\\{"));
        syntaxJ.setTryCatch(Pattern.compile("\\}\\s*catch\\s*\\{"));
        syntaxJ.setTryFinally(Pattern.compile("\\}\\s*finally\\s*\\{"));
        syntaxJ.setTryEnd(Pattern.compile("\\}"));
        syntaxJ.setFunction(Pattern.compile("function\\s*([\\s+|[\\s*\\(]][^\\{]+)\\{"));
        syntaxJ.setFunctionEnd(Pattern.compile("\\}"));
        syntaxJ.setNull(Pattern.compile("null"));
        syntaxJ.setReturn(Pattern.compile("return"));
        syntaxJ.setImport(Pattern.compile("import\\s+"));
        syntaxJ.setRootContext(Pattern.compile("root\\."));
        syntaxJ.setContinue(Pattern.compile("continue"));
        syntaxJ.setBreak(Pattern.compile("break"));
        globalSyntaxs.put("CajuJava", syntaxJ);
        Syntax syntaxB = new Syntax();
        syntaxB.setIf(Pattern.compile("^[\\s+i|i]f\\s*([\\s+|[\\s*\\(]].+)\\s*"));
        syntaxB.setElseIf(Pattern.compile("^[\\s+e|e]lseif\\s*([\\s+|[\\s*\\(]].+)\\s*"));
        syntaxB.setElse(Pattern.compile("^[\\s+e|e]ls[e\\s+|e]$"));
        syntaxB.setIfEnd(Pattern.compile("^[\\s+e|e]n[d\\s+|d]$"));
        syntaxB.setLoop(Pattern.compile("^[\\s+w|w]hile\\s*([\\s+|[\\s*\\(]].+)\\s*"));
        syntaxB.setLoopEnd(Pattern.compile("^[\\s+e|e]n[d\\s+|d]$"));
        syntaxB.setTry(Pattern.compile("^[\\s+t|t]ry\\s*([\\s+|[\\s*\\(]].+)\\s*"));
        syntaxB.setTryCatch(Pattern.compile("^[\\s+c|c]atc[h\\s+|h]$"));
        syntaxB.setTryFinally(Pattern.compile("^[\\s+f|f]inall[y\\s+|y]$"));
        syntaxB.setTryEnd(Pattern.compile("^[\\s+e|e]n[d\\s+|d]$"));
        syntaxB.setFunction(Pattern.compile("^[\\s+f|f]unction\\s*([\\s+|[\\s*\\(]].+)\\s*"));
        syntaxB.setFunctionEnd(Pattern.compile("^[\\s+e|e]n[d\\s+|d]$"));
        syntaxB.setNull(Pattern.compile("null"));
        syntaxB.setReturn(Pattern.compile("return"));
        syntaxB.setImport(Pattern.compile("import\\s+"));
        syntaxB.setRootContext(Pattern.compile("root\\."));
        syntaxB.setContinue(Pattern.compile("continue"));
        syntaxB.setBreak(Pattern.compile("break"));
        globalSyntaxs.put("CajuBasic", syntaxB);
    }

    public <T> T asObject(Reader script, Class<T> superClasz) throws Exception {
        CajuScriptEngine engine = new CajuScriptEngine();
        engine.eval(script);
        return engine.getInterface(superClasz);
    }

    public <T> T asObject(String script, Class<T> superClasz) throws Exception {
        CajuScriptEngine engine = new CajuScriptEngine();
        engine.eval(script);
        return engine.getInterface(superClasz);
    }
}
