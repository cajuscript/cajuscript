# Syntax

Customize the syntax is allowed, you can define your style.

You can define your style of syntax using the class `org.cajuscript.Syntax`.

Register the new instance of Syntax in the CajuScript:

```
org.cajuscript.CajuScript.addGlobalSyntax("SYNTAX_NAME", syntaxInstance);
```

To use your syntax, just in first line of your script put this:

```
caju.syntax: SYNTAX_NAME
```

Right now you can execute your script:

```
javax.script.ScriptEngine caju = new org.cajuscript.CajuScriptEngine();
caju.eval(YOUR_SCRIPT);
```

See about samples of scripts using the custom syntax:

- [Basic](basic.md)
- [Java](java.md)
- [Portuguese](portuguese/README.md).

## Java

Define your style:

```
org.cajuscript.Syntax syntaxJ = new org.cajuscript.Syntax();
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
```

Register the instance of Syntax in the CajuScript:

```
org.cajuscript.CajuScript.addGlobalSyntax("MyJava", syntaxJ);
```

To use your syntax, just in first line of your script put this:

```
caju.syntax: MyJava
```

## Basic

Define your style:
```
org.cajuscript.Syntax syntaxB = new org.cajuscript.Syntax();
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
```

Register the instance of Syntax in the CajuScript:
```
org.cajuscript.CajuScript.addGlobalSyntax("MyBasic", syntaxB);
```

To use your syntax, just in first line of your script put this:

```
caju.syntax: MyBasic
```
