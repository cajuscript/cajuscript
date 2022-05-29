# CajuScript Documentation

The CajuScript is a JVM scripting language, then programming integrated with Java.

Is an easy-to-use script language for Java.

It has the features to create new instances of any class and invoke methods.

There are many ways to simplify programming in Java and turn it dynamic.

Supports all basics resources of programing like:

- Variables like `i = 1` or `s = StringBuilder()`
- [Array](array.md)
- [If](if.md)
- [Loop](loop.md)
- [Function](function.md)
- [Import/Include](importAndInclude.md)
- [Cast](cast.md)
- [Try, Catch and Finally](tryCatch.md)
- [Single line programming](inLine.md)

You can see the syntax overview in the [handbook](handbook.md).

About [custom syntax](samples/syntax.md) and these cool proposals:

- [Basic](samples/syntaxBasic.md)
- [Java](samples/syntaxJava.md)
- [Portuguese](samples/syntaxPortuguese.md)

Is very easy to use Java, like this sample:

```
$java.lang
str = "Hello world!"
len = 0
len < str.length() @
    System.out.println(str.substring(len, len + 1))
    len += 1
@
System.out.println(str.replaceAll("Hello", "Hi"))
str = StringBuilder(str)
str.append(" Bye bye bye...")
System.out.println(str)
```

## Interface to Run Code

Desktop window interface to play quickly:

```
java –cp dist/cajuscript-DATE.jar org.cajuscript.irc.CajuConsole
```

## Execute Script File

To execute a scripting file from the command line:

```
java -jar cajuscript.jar fileread.cj
```

## Use It

Using in your Java projects:

```
CajuScript caju = new CajuScript();
caju.eval("myVar = 'Value from CajuScript'");
System.out.println(caju.get("myVar"));
```

Or with Java Scripting API (JSR 223):

```
// Register
ScriptEngineManager manager = new ScriptEngineManager();
CajuScriptEngineFactory cajuFactory = new CajuScriptEngineFactory();
manager.registerEngineName("caju", cajuFactory);
// Execution
ScriptEngine caju = manager.getEngineByName("caju");
caju.eval("myVar = 'Value from CajuScript'");
System.out.println(caju.get("myVar"));
```
