/*
 * Compiler.java
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
package org.cajuscript.compiler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.bcel.Const;
import org.apache.bcel.generic.AASTORE;
import org.apache.bcel.generic.ACONST_NULL;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ANEWARRAY;
import org.apache.bcel.generic.ARETURN;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ATHROW;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.DUP;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.ICONST;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.IFNE;
import org.apache.bcel.generic.IF_ICMPGE;
import org.apache.bcel.generic.IINC;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.InstructionConst;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.Type;
import org.cajuscript.CajuScript;
import org.cajuscript.CajuScriptException;
import org.cajuscript.Context;
import org.cajuscript.Syntax;
import org.cajuscript.Value;
import org.cajuscript.parser.Base;
import org.cajuscript.parser.Break;
import org.cajuscript.parser.Command;
import org.cajuscript.parser.Continue;
import org.cajuscript.parser.Element;
import org.cajuscript.parser.Function;
import org.cajuscript.parser.If;
import org.cajuscript.parser.IfGroup;
import org.cajuscript.parser.Import;
import org.cajuscript.parser.LineDetail;
import org.cajuscript.parser.Loop;
import org.cajuscript.parser.Operation;
import org.cajuscript.parser.Return;
import org.cajuscript.parser.TryCatch;
import org.cajuscript.parser.Variable;

/**
 * CajuScript Compiler
 * @author eduveks
 */
public class Compiler {

    private File baseDir = null;
    private static Map<String, Class> classes = new HashMap<String, Class>();
    private String packagePath = null;
    private String className = null;
    private File packageDir = null;
    private File scriptFile = null;
    private File classFile = null;
    private CajuScript caju = null;
    private long varCount = 1;
    private Map<String, Integer> valuesIndexes = new HashMap<String, Integer>();
    private LineDetail lastLiteDetail = null;

    /**
     * Compiler an script.
     * @param cajuScript CajuScript instance
     * @param path Class path to be created new class from the script.
     */
    public Compiler(CajuScript cajuScript, String path) {
        this.caju = cajuScript;
        baseDir = new File(caju.getCompileBaseDirectory());
        if (path.lastIndexOf(".") > -1) {
            packagePath = path.substring(0, path.lastIndexOf("."));
            className = path.substring(path.lastIndexOf(".") + 1);
            packageDir = new File(baseDir.getAbsolutePath().concat(Character.toString(File.separatorChar)).concat(packagePath.replace('.', File.separatorChar)));
        } else {
            packagePath = "";
            className = path;
            packageDir = baseDir;
        }
        scriptFile = new File(packageDir.getAbsolutePath().concat(Character.toString(File.separatorChar)).concat(className).concat(".cj"));
        classFile = new File(packageDir.getAbsolutePath().concat(Character.toString(File.separatorChar)).concat(className).concat(".class"));
    }

    /**
     * Execute an script compiled.
     * @param context Context
     * @param syntax Syntax
     * @return Value returned by the script
     * @throws org.cajuscript.CajuScriptException Script executing exceptions
     */
    public Value execute(Context context, Syntax syntax) throws CajuScriptException {
        try {
            String path = packagePath.length() > 0 ? packagePath.concat(".").concat(className) : className;
            if (classes.get(path) == null) {
                loadClass(context);
            }
            return ((org.cajuscript.compiler.Executable) classes.get(path).newInstance()).execute(caju, context, syntax);
        } catch (Exception e) {
            throw CajuScriptException.create(caju, context, e.getMessage(), e);
        }
    }

    private void loadClass(Context context) throws CajuScriptException {
        try {
            String path = packagePath.length() > 0 ? packagePath.concat(".").concat(className) : className;
            URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{baseDir.toURI().toURL()}, CajuScript.class.getClassLoader());
            Executable parserExecute = (Executable) urlClassLoader.loadClass(path).newInstance();
            classes.put(path, parserExecute.getClass());
        } catch (Exception e) {
            throw CajuScriptException.create(caju, context, e.getMessage(), e);
        }
    }

    /**
     * The class compiled is the latest version
     * @param script Script
     * @return Is latest version
     * @throws org.cajuscript.CajuScriptException Looking if is latest version exceptions.
     */
    public boolean isLatest(String script) throws CajuScriptException {
        if (!scriptFile.exists() || !classFile.exists()) {
            return false;
        }
        String scriptClass = "";
        java.io.InputStream is = null;
        try {
            is = new java.io.FileInputStream(scriptFile);
            byte[] b = new byte[is.available()];
            is.read(b);
            scriptClass = new String(b);
        } catch (Exception e) {
            throw new CajuScriptException(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                }
            }
        }
        return scriptClass.equals(script);
    }

    /**
     * Compile an script.
     * @param staticContext Static context
     * @param script Script to be compiled
     * @param base Parser base
     * @throws org.cajuscript.CajuScriptException Compiling exceptions
     */
    public void compile(Context staticContext, String script, Element base) throws CajuScriptException {
        packageDir.mkdirs();
        try {
            ClassGen cg = new ClassGen(packagePath.length() > 0 ? packagePath.concat(".").concat(className) : className, "java.lang.Object",
                    "<generated>", Const.ACC_PUBLIC | Const.ACC_SUPER,
                    new String[]{"org.cajuscript.compiler.Executable"});
            ConstantPoolGen cp = cg.getConstantPool();
            InstructionList il = new InstructionList();
            MethodGen mg = new MethodGen(Const.ACC_PUBLIC,
                    new ObjectType("org.cajuscript.Value"),
                    new Type[]{
                        new ObjectType("org.cajuscript.CajuScript"),
                        new ObjectType("org.cajuscript.Context"),
                        new ObjectType("org.cajuscript.Syntax")
                    },
                    new String[]{"caju", "context", "syntax"},
                    "execute", "className",
                    il, cp);
            mg.addException("org.cajuscript.CajuScriptException");
            InstructionFactory factory = new InstructionFactory(cg);

            for (String key : staticContext.getStaticStrings().keySet()) {
                il.append(new ALOAD(1));
                il.append(new PUSH(cp, key));
                il.append(new PUSH(cp, staticContext.getStaticStrings().get(key)));
                il.append(factory.createInvoke("org.cajuscript.CajuScript", "set", Type.VOID,
                    new Type[]{Type.STRING, Type.OBJECT},
                        Const.INVOKEVIRTUAL));
            }
            for (String key : staticContext.getAllKeys(true)) {
                il.append(new ALOAD(1));
                il.append(new PUSH(cp, key));
                il.append(new PUSH(cp, staticContext.getVar(key).toString()));
                il.append(factory.createInvoke("org.cajuscript.CajuScript", "set", Type.VOID,
                    new Type[]{Type.STRING, Type.OBJECT},
                        Const.INVOKEVIRTUAL));
            }

            int iFunc = 0;
            for (String key : caju.getContext().getFuncs().keySet()) {
                Function function = (Function)caju.getContext().getFuncs().get(key);
                String funcName = "f".concat(Integer.toString(iFunc)).concat("_").concat(function.getName());
                InstructionList ilFunc = new InstructionList();
                MethodGen mgFunc = new MethodGen(Const.ACC_PUBLIC,
                        new ObjectType("org.cajuscript.Value"),
                        new Type[]{
                            new ObjectType("org.cajuscript.CajuScript"),
                            new ObjectType("org.cajuscript.Context"),
                            new ObjectType("org.cajuscript.Syntax")
                        },
                        new String[]{"caju", "context", "syntax"},
                        funcName, "className",
                        ilFunc, cp);
                mgFunc.addException("org.cajuscript.CajuScriptException");
                InstructionFactory factoryFunc = new InstructionFactory(cg);
                LocalVariableGen lgI = mgFunc.addLocalVariable("i", Type.INT, null, null);
                valuesIndexes.put("i", lgI.getIndex());
                List<String> funcValueKeys = new ArrayList<String>();
                launchCompileElements(cg, cp, ilFunc, mgFunc, factoryFunc, funcValueKeys, function, 0, true);
                for (String valueKey : funcValueKeys) {
                    ilFunc.append(factoryFunc.createNew(new ObjectType("org.cajuscript.Value")));
                    ilFunc.append(new DUP());
                    LocalVariableGen lg = mgFunc.addLocalVariable(valueKey, new ObjectType("org.cajuscript.Value"), null, null);
                    int i = lg.getIndex();
                    ilFunc.append(new ALOAD(1));
                    ilFunc.append(new ALOAD(2));
                    ilFunc.append(new ALOAD(3));
                    ilFunc.append(factoryFunc.createInvoke("org.cajuscript.Value", "<init>",
                        Type.VOID, new Type[] {
                            new ObjectType("org.cajuscript.CajuScript"),
                            new ObjectType("org.cajuscript.Context"),
                            new ObjectType("org.cajuscript.Syntax")
                        },
                            Const.INVOKESPECIAL));
                    ilFunc.append(new ASTORE(i));
                    valuesIndexes.put(valueKey, i);
                }
                String returnFunc = launchCompileElements(cg, cp, ilFunc, mgFunc, factoryFunc, funcValueKeys, function, 0, false);
                if (!returnFunc.equals("__return")) {
                    ilFunc.append(new ACONST_NULL());
                    ilFunc.append(new ARETURN());
                }
                mgFunc.setMaxStack();
                cg.addMethod(mgFunc.getMethod());
                ilFunc.dispose();

                il.append(new ALOAD(2));
                il.append(new PUSH(cp, function.getName()));
                il.append(factoryFunc.createNew(new ObjectType("org.cajuscript.parser.Function")));
                il.append(new DUP());
                il.append(new ALOAD(0));
                il.append(new PUSH(cp, funcName));
                il.append(new ICONST(function.getParameters().length));
                il.append(new ANEWARRAY(cp.addClass(Type.STRING)));
                for (int i = 0; i < function.getParameters().length; i++) {
                    il.append(new DUP());
                    il.append(new ICONST(i));
                    il.append(new PUSH(cp, function.getParameters()[i]));
                    il.append(new AASTORE());
                }
                il.append(factory.createInvoke("org.cajuscript.parser.Function", "<init>",
                        Type.VOID, new Type[] {
                            new ObjectType("org.cajuscript.compiler.Executable"),
                            Type.STRING,
                            new ArrayType(Type.STRING, 1)
                        },
                        Const.INVOKESPECIAL));
                il.append(factory.createInvoke("org.cajuscript.Context", "setFunc",
                    Type.VOID, new Type[] { Type.STRING, new ObjectType("org.cajuscript.parser.Function") },
                        Const.INVOKEVIRTUAL));
                valuesIndexes.clear();
                iFunc++;
            }

            List<String> valuesKeys = new ArrayList<String>();

            launchCompileElements(cg, cp, il, mg, factory, valuesKeys, base, 0, true);

            for (String key : valuesKeys) {
                il.append(factory.createNew(new ObjectType("org.cajuscript.Value")));
                il.append(new DUP());
                LocalVariableGen lg = mg.addLocalVariable(key, new ObjectType("org.cajuscript.Value"), null, null);
                int i = lg.getIndex();
                il.append(new ALOAD(1));
                il.append(new ALOAD(2));
                il.append(new ALOAD(3));
                il.append(factory.createInvoke("org.cajuscript.Value", "<init>",
                    Type.VOID, new Type[] {
                        new ObjectType("org.cajuscript.CajuScript"),
                        new ObjectType("org.cajuscript.Context"),
                        new ObjectType("org.cajuscript.Syntax")
                    },
                        Const.INVOKESPECIAL));
                il.append(new ASTORE(i));
                valuesIndexes.put(key, i);
            }

            LocalVariableGen lgI = mg.addLocalVariable("i", Type.INT, null, null);
            valuesIndexes.put("i", lgI.getIndex());

            valuesKeys = new ArrayList<String>();

            launchCompileElements(cg, cp, il, mg, factory, valuesKeys, base, 0, false);

            il.append(new ACONST_NULL());
            il.append(new ARETURN());
            
            mg.setMaxStack();
            cg.addMethod(mg.getMethod());
            il.dispose();
            cg.addEmptyConstructor(Const.ACC_PUBLIC);
            cg.getJavaClass().dump(new File(classFile.getAbsolutePath().replace(".class", ".class")));
        } catch (IOException ex) {
            throw new CajuScriptException(ex);
        }
        Writer fwScript = null;
        try {
            fwScript = new FileWriter(scriptFile);
            fwScript.append(script);
        } catch (IOException ex) {
            throw new CajuScriptException(ex);
        } finally {
            if (fwScript != null) {
                try {
                    fwScript.close();
                } catch (IOException ex) {
                    throw new CajuScriptException(ex);
                }
            }
        }
        loadClass(staticContext);
    }

    private String compileElement(ClassGen cg, ConstantPoolGen cp, InstructionList il, MethodGen mg, InstructionFactory factory, List<String> valueKeys, Element element, int level, boolean onlyValues, Map<String, GOTO> gotosContinue, Map<String, GOTO> gotosBreak) {
        String key = "";
        if (element == null) {
            return null;
        }
        boolean addKey = true;
        boolean isReturn = false;
        boolean isBreak = false;
        boolean isContinue = false;
        int nextLevel = level + 1;
        if (element instanceof Command) {
            lineDetail(cp, il, mg, factory, element.getLineDetail(), onlyValues);
            compileElements(cg, cp, il, mg, factory, valueKeys, element, nextLevel, onlyValues, gotosContinue, gotosBreak);
            key = "c".concat(Integer.toString(level)).concat("_").concat(Long.toString(varCount++));
            if (!onlyValues) {
                Command command = (Command) element;
                il.append(new ALOAD(valuesIndexes.get(key)));
                il.append(new PUSH(cp, command.getCommand()));
                il.append(factory.createInvoke("org.cajuscript.Value", "setScript",
                        Type.VOID, new Type[] { Type.STRING },
                        Const.INVOKEVIRTUAL));
            }
        } else if (element instanceof Variable) {
            lineDetail(cp, il, mg, factory, element.getLineDetail(), onlyValues);
            compileElements(cg, cp, il, mg, factory, valueKeys, element, nextLevel, onlyValues, gotosContinue, gotosBreak);
            Variable variable = (Variable) element;
            String keyValue = compileElement(cg, cp, il, mg, factory, valueKeys, variable.getValue(), nextLevel, onlyValues, gotosContinue, gotosBreak);
            if (variable.getKey().equals("")) {
                addKey = false;
                key = keyValue;
            } else if (!onlyValues) {
                if (variable.isKeyRootContext(caju.getSyntax())) {
                    il.append(new ALOAD(1));
                    il.append(new PUSH(cp, variable.getKeyRootContext(caju.getSyntax())));
                } else {
                    il.append(new ALOAD(2));
                    il.append(new PUSH(cp, variable.getKey()));
                }
                if (keyValue == null || keyValue.equals("")) {
                    il.append(InstructionConst.ACONST_NULL);
                }  else {
                    il.append(new ALOAD(valuesIndexes.get(keyValue)));
                    il.append(factory.createInvoke("org.cajuscript.Value", "clone",
                            new ObjectType("org.cajuscript.Value"), new Type[] { },
                            Const.INVOKEVIRTUAL));
                }
                il.append(factory.createInvoke(
                        variable.isKeyRootContext(caju.getSyntax()) ? "org.cajuscript.CajuScript" : "org.cajuscript.Context",
                        "setVar", Type.VOID, new Type[] { Type.STRING, new ObjectType("org.cajuscript.Value") },
                        Const.INVOKEVIRTUAL));
            }
        } else if (element instanceof Operation) {
            lineDetail(cp, il, mg, factory, element.getLineDetail(), onlyValues);
            compileElements(cg, cp, il, mg, factory, valueKeys, element, nextLevel, onlyValues, gotosContinue, gotosBreak);
            key = "o".concat(Integer.toString(level)).concat("_").concat(Long.toString(varCount++));
            Operation operation = (Operation) element;
            String firstCommand = compileElement(cg, cp, il, mg, factory, valueKeys, operation.getFirstCommand(), nextLevel, onlyValues, gotosContinue, gotosBreak);
            String secondCommand = compileElement(cg, cp, il, mg, factory, valueKeys, operation.getSecondCommand(), nextLevel, onlyValues, gotosContinue, gotosBreak);
            if (!onlyValues) {
                il.append(factory.createFieldAccess("org.cajuscript.parser.Operation$Operator",
                        operation.getOperator().name(), new ObjectType("org.cajuscript.parser.Operation$Operator"),
                        Const.GETSTATIC));
                il.append(new ALOAD(valuesIndexes.get(key)));
                il.append(new ALOAD(valuesIndexes.get(firstCommand)));
                il.append(new ALOAD(valuesIndexes.get(secondCommand)));
                il.append(factory.createInvoke("org.cajuscript.parser.Operation$Operator", "compare",
                        Type.VOID, new Type[] { new ObjectType("org.cajuscript.Value")
                            , new ObjectType("org.cajuscript.Value")
                            , new ObjectType("org.cajuscript.Value")},
                        Const.INVOKEVIRTUAL));
            }
        } else if (element instanceof Return) {
            compileElements(cg, cp, il, mg, factory, valueKeys, element, nextLevel, onlyValues, gotosContinue, gotosBreak);
            Return _return = (Return) element;
            String valueKey = compileElement(cg, cp, il, mg, factory, valueKeys, _return.getValue(), nextLevel, onlyValues, gotosContinue, gotosBreak);
            lineDetail(cp, il, mg, factory, element.getLineDetail(), onlyValues);
            if (!onlyValues) {
                if (valueKey == null) {
                    il.append(new ACONST_NULL());
                } else {
                    il.append(new ALOAD(valuesIndexes.get(valueKey)));
                }
                il.append(new ARETURN());
            }
            isReturn = true;
        } else if (element instanceof Break) {
            compileElements(cg, cp, il, mg, factory, valueKeys, element, nextLevel, onlyValues, gotosContinue, gotosBreak);
            lineDetail(cp, il, mg, factory, element.getLineDetail(), onlyValues);
            if (!onlyValues) {
                Break _break = (Break) element;
                GOTO gt = new GOTO(null);
                il.append(gt);
                gotosBreak.put("loop_".concat(_break.getLabel()), gt);
            }
            isBreak = true;
        } else if (element instanceof Continue) {
            compileElements(cg, cp, il, mg, factory, valueKeys, element, nextLevel, onlyValues, gotosContinue, gotosBreak);
            lineDetail(cp, il, mg, factory, element.getLineDetail(), onlyValues);
            if (!onlyValues) {
                Continue _continue = (Continue) element;
                GOTO gt = new GOTO(null);
                il.append(gt);
                gotosContinue.put("loop_".concat(_continue.getLabel()), gt);
            }
            isContinue = true;
        } else if (element instanceof Import) {
            compileElements(cg, cp, il, mg, factory, valueKeys, element, nextLevel, onlyValues, gotosContinue, gotosBreak);
            lineDetail(cp, il, mg, factory, element.getLineDetail(), onlyValues);
            if (!onlyValues) {
                Import _import = (Import) element;
                if (_import.getPath().startsWith(CajuScript.CAJU_VARS)) {
                    il.append(new ALOAD(1));
                    il.append(new ALOAD(2));
                    il.append(new PUSH(cp, _import.getPath()));
                    il.append(factory.createInvoke("org.cajuscript.Context", "getVar",
                        new ObjectType("org.cajuscript.Value"), new Type[] { Type.STRING },
                            Const.INVOKEVIRTUAL));
                    il.append(factory.createInvoke("org.cajuscript.Value", "toString",
                        Type.STRING, new Type[] { },
                            Const.INVOKEVIRTUAL));
                    il.append(factory.createInvoke("org.cajuscript.CajuScript", "evalFile",
                        new ObjectType("org.cajuscript.Value"), new Type[] { Type.STRING },
                            Const.INVOKEVIRTUAL));
                } else {
                    il.append(new ALOAD(2));
                    il.append(new PUSH(cp, _import.getPath()));
                    il.append(factory.createInvoke("org.cajuscript.Context", "addImport",
                        Type.VOID, new Type[] { Type.STRING },
                            Const.INVOKEVIRTUAL));
                }
            }
        } else if (element instanceof IfGroup) {
            lineDetail(cp, il, mg, factory, element.getLineDetail(), onlyValues);
            BranchHandle ifBlock = null;
            InstructionHandle ifStart = null;
            int i = 0;
            if (!onlyValues) {
                String keyCode = Integer.toString(element.hashCode());
                LocalVariableGen lg = mg.addLocalVariable("i".concat(keyCode), Type.INT, null, null);
                i = lg.getIndex();
                il.append(new ICONST(0));
                il.append(new ISTORE(i));
                ifStart = il.append(new ILOAD(i));
                il.append(new ICONST(1));
                ifBlock = il.append(new IF_ICMPGE(null));
            }
            compileElements(cg, cp, il, mg, factory, valueKeys, element, nextLevel, onlyValues, gotosContinue, gotosBreak);
            if (!onlyValues) {
                il.append(new IINC(i, 1));
                il.append(new GOTO(ifStart));
                InstructionHandle ifGroupEnd = il.append(new PUSH(cp, 1));
                il.append(new ISTORE(valuesIndexes.get("i")));
                ifBlock.setTarget(ifGroupEnd);
                for (Element e : element.getElements()) {
                    String k = Integer.toString(e.hashCode());
                    if (gotosBreak.containsKey(k)) {
                        gotosBreak.get(k).setTarget(ifGroupEnd);
                        gotosBreak.remove(k);
                    }
                }
            }
        } else if (element instanceof If) {
            If _if = (If) element;
            String conditionKey = compileElement(cg, cp, il, mg, factory, valueKeys, _if.getCondition(), nextLevel, onlyValues, gotosContinue, gotosBreak);
            lineDetail(cp, il, mg, factory, element.getLineDetail(), onlyValues);
            BranchHandle ifBlock = null;
            if (!onlyValues) {
                il.append(new ALOAD(valuesIndexes.get(conditionKey)));
                il.append(factory.createInvoke("org.cajuscript.Value", "getBooleanValue",
                        Type.BOOLEAN, new Type[] { },
                        Const.INVOKEVIRTUAL));
                ifBlock = il.append(new IFEQ(null));
            }
            String __key = compileElements(cg, cp, il, mg, factory, valueKeys, element, nextLevel, onlyValues, gotosContinue, gotosBreak);
            if (!onlyValues) {
                if (!__key.equals("__return")
                        && !__key.equals("__break")
                        && !__key.equals("__continue")) {
                    GOTO gt = new GOTO(null);
                    il.append(gt);
                    gotosBreak.put(Integer.toString(_if.hashCode()), gt);
                }
                InstructionHandle ifEnd = il.append(InstructionConst.NOP);
                ifBlock.setTarget(ifEnd);
            }
        } else if (element instanceof Loop) {
            Loop loop = (Loop) element;
            InstructionHandle loopStart = null;
            if (!onlyValues) {
                loopStart = il.append(new PUSH(cp, 1));
                il.append(new ISTORE(valuesIndexes.get("i")));
            }
            lineDetail(cp, il, mg, factory, element.getLineDetail(), onlyValues);
            GOTO gotoLoopEnd = null;
            String conditionKey = compileElement(cg, cp, il, mg, factory, valueKeys, loop.getCondition(), nextLevel, onlyValues, gotosContinue, gotosBreak);
            if (!onlyValues) {
                il.append(new ALOAD(valuesIndexes.get(conditionKey)));
                il.append(factory.createInvoke("org.cajuscript.Value", "getBooleanValue",
                    Type.BOOLEAN, new Type[] { },
                        Const.INVOKEVIRTUAL));
                BranchHandle ifBlock = il.append(new IFNE(null));
                gotoLoopEnd = new GOTO(null);
                il.append(gotoLoopEnd);
                InstructionHandle ifEnd = il.append(InstructionConst.NOP);
                ifBlock.setTarget(ifEnd);
            }
            compileElements(cg, cp, il, mg, factory, valueKeys, element, nextLevel, onlyValues, gotosContinue, gotosBreak);
            if (!onlyValues) {
                GOTO gotoLoopStart = new GOTO(null);
                gotoLoopStart.setTarget(loopStart);
                il.append(gotoLoopStart);
                InstructionHandle loopEnd = il.append(new PUSH(cp, 1));
                il.append(new ISTORE(valuesIndexes.get("i")));
                gotoLoopEnd.setTarget(loopEnd);
                for (String keyContinue : gotosContinue.keySet()) {
                    if (keyContinue.equals("loop_") || keyContinue.equals("loop_".concat(loop.getLabel()))) {
                        gotosContinue.get(keyContinue).setTarget(loopStart);
                        gotosContinue.remove(keyContinue);
                    }
                }
                for (String keyBreak : gotosBreak.keySet()) {
                    if (keyBreak.equals("loop_") || keyBreak.equals("loop_".concat(loop.getLabel()))) {
                        gotosBreak.get(keyBreak).setTarget(loopEnd);
                        gotosBreak.remove(keyBreak);
                    }
                }
            }
        } else if (element instanceof TryCatch) {
            lineDetail(cp, il, mg, factory, element.getLineDetail(), onlyValues);
            TryCatch tryCatch = (TryCatch) element;
            key = "t".concat(Integer.toString(level)).concat("_").concat(Long.toString(varCount++));
            compileElement(cg, cp, il, mg, factory, valueKeys, tryCatch.getError(), nextLevel, onlyValues, gotosContinue, gotosBreak);
            int throwIndex = 0;
            if (!onlyValues) {
                LocalVariableGen lg = mg.addLocalVariable("throw_".concat(key),
                    new ObjectType("java.lang.Throwable"), null, null);
                throwIndex = lg.getIndex();
            }
            InstructionHandle tryStart = null;
            InstructionHandle tryEnd = null;
            InstructionHandle tryHandlerStart = null;
            InstructionHandle tryHandlerEnd = null;
            InstructionHandle tryFinallyStart = null;
            InstructionHandle tryFinallyStartPoint = null;
            InstructionHandle tryFinallyEnd = null;
            GOTO gotoTry = new GOTO(null);
            GOTO gotoTryHandler = new GOTO(null);
            if (!onlyValues) {
                tryStart = il.append(new PUSH(cp, 1));
                il.append(new ISTORE(valuesIndexes.get("i")));
            }
            compileElement(cg, cp, il, mg, factory, valueKeys, tryCatch.getTry(), nextLevel, onlyValues, gotosContinue, gotosBreak);
            if (!onlyValues) {
                tryEnd = il.append(new PUSH(cp, 1));
                il.append(new ISTORE(valuesIndexes.get("i")));
                compileElement(cg, cp, il, mg, factory, valueKeys, tryCatch.getFinally(), nextLevel, onlyValues, gotosContinue, gotosBreak);
                il.append(gotoTry);
            } else {
                compileElement(cg, cp, il, mg, factory, valueKeys, tryCatch.getFinally(), nextLevel, onlyValues, gotosContinue, gotosBreak);
            }
            if (!onlyValues) {
                tryHandlerStart = il.append(new ASTORE(throwIndex));
            }
            if (!onlyValues) {
                il.append(new ALOAD(valuesIndexes.get(key)));
                il.append(new ALOAD(throwIndex));
                il.append(factory.createInvoke("org.cajuscript.Value", "setValue",
                    Type.VOID, new Type[] { Type.OBJECT },
                        Const.INVOKEVIRTUAL));
                il.append(new ALOAD(2));
                il.append(new PUSH(cp, tryCatch.getError().getKey()));
                il.append(new ALOAD(valuesIndexes.get(key)));
                il.append(factory.createInvoke("org.cajuscript.Context", "setVar",
                    Type.VOID, new Type[] { Type.STRING, new ObjectType("org.cajuscript.Value") },
                        Const.INVOKEVIRTUAL));
            }
            compileElement(cg, cp, il, mg, factory, valueKeys, tryCatch.getCatch(), nextLevel, onlyValues, gotosContinue, gotosBreak);
            if (!onlyValues) {
                tryHandlerEnd = il.append(new PUSH(cp, 1));
                il.append(new ISTORE(valuesIndexes.get("i")));
                compileElement(cg, cp, il, mg, factory, valueKeys, tryCatch.getFinally(), nextLevel, onlyValues, gotosContinue, gotosBreak);
                il.append(gotoTryHandler);
            } else {
                compileElement(cg, cp, il, mg, factory, valueKeys, tryCatch.getFinally(), nextLevel, onlyValues, gotosContinue, gotosBreak);
            }
            if (!onlyValues) {
                LocalVariableGen lg = mg.addLocalVariable("finally_".concat(key), Type.OBJECT, null, null);
                int finallyIndex = lg.getIndex();
                tryFinallyStart = il.append(new ASTORE(finallyIndex));
                tryFinallyStartPoint = il.append(new PUSH(cp, 1));
                il.append(new ISTORE(valuesIndexes.get("i")));
                compileElement(cg, cp, il, mg, factory, valueKeys, tryCatch.getFinally(), nextLevel, onlyValues, gotosContinue, gotosBreak);
                il.append(new ALOAD(finallyIndex));
                il.append(new ATHROW());
                tryFinallyEnd = il.append(new PUSH(cp, 1));
                il.append(new ISTORE(valuesIndexes.get("i")));
                gotoTry.setTarget(tryFinallyEnd);
                gotoTryHandler.setTarget(tryFinallyEnd);
                mg.addExceptionHandler(tryStart, tryEnd, tryHandlerStart, new ObjectType("java.lang.Throwable"));
                mg.addExceptionHandler(tryStart, tryEnd, tryFinallyStart, null);
                mg.addExceptionHandler(tryHandlerStart, tryHandlerEnd, tryFinallyStart, null);
                mg.addExceptionHandler(tryFinallyStart, tryFinallyStartPoint, tryFinallyStart, null);
            } else {
                compileElement(cg, cp, il, mg, factory, valueKeys, tryCatch.getFinally(), nextLevel, onlyValues, gotosContinue, gotosBreak);
            }
        } else if (element instanceof Base) {
            compileElements(cg, cp, il, mg, factory, valueKeys, element, nextLevel, onlyValues, gotosContinue, gotosBreak);
        }
        if (!key.equals("") && addKey && !valueKeys.contains(key)) {
            valueKeys.add(key);
        }
        if (isReturn) {
            return "__return";
        }
        if (isBreak) {
            return "__break";
        }
        if (isContinue) {
            return "__continue";
        }
        return key;
    }

    private String compileElements(ClassGen cg, ConstantPoolGen cp, InstructionList il, MethodGen mg, InstructionFactory factory, List<String> valueKeys, Element elements, int level, boolean onlyValues, Map<String, GOTO> gotosContinue, Map<String, GOTO> gotosBreak) {
        String key = "";
        int nextLevel = level + 1;
        for (Element element : elements.getElements()) {
            key = compileElement(cg, cp, il, mg, factory, valueKeys, element, nextLevel, onlyValues, gotosContinue, gotosBreak);
            if (key.equals("__return")
                    || key.equals("__break")
                    || key.equals("__continue")) {
                break;
            }
        }
        return key;
    }

    private String launchCompileElements(ClassGen cg, ConstantPoolGen cp, InstructionList il, MethodGen mg, InstructionFactory factory, List<String> valueKeys, Element elements, int level, boolean onlyValues) {
        varCount = 1;
        return compileElements(cg, cp, il, mg, factory, valueKeys, elements, level, onlyValues, new HashMap<String, GOTO>(), new HashMap<String, GOTO>());
    }

    private void lineDetail(ConstantPoolGen cp, InstructionList il, MethodGen mg, InstructionFactory factory, LineDetail lineDetail, boolean onlyValues) {
        if (lastLiteDetail != null
                && lastLiteDetail.getNumber() == lineDetail.getNumber()
                && lastLiteDetail.getContent().equals(lineDetail.getContent())) {
            return;
        }
        lastLiteDetail = lineDetail;
        varCount = 1;
        if (!onlyValues) {
            il.append(new ALOAD(1));
            il.append(factory.createInvoke("org.cajuscript.CajuScript", "getRunningLine",
                    new ObjectType("org.cajuscript.parser.LineDetail"), new Type[] {},
                    Const.INVOKEVIRTUAL));
            il.append(new PUSH(cp, lineDetail.getNumber()));
            il.append(new PUSH(cp, lineDetail.getContent()));
            il.append(factory.createInvoke("org.cajuscript.parser.LineDetail", "set",
                    Type.VOID, new Type[]{ Type.INT, Type.STRING },
                    Const.INVOKEVIRTUAL));
        }
    }
}
