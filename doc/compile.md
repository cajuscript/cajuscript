# Compile

Is possible compile a script to Java Class.

Actually is a code generator, building the code Java from the Parser. Final result is a Java Class that can load the Parser faster to be executed.

To work with compiled scripts is very easy. Just start the script with *caju.compile* like that sample:

```
caju.compile: mypackage.MyClass
System.out.println("Hello world!");
```

For compile is necessary the **bcel.jar** you can find here [http://jakarta.apache.org/bcel/ jakarta.apache.org/bcel].

Wherefore for compile and run do like that:

```
java -cp "___YOUR_JDK_HOME___\lib\tools.jar:cajuscript-0.4.jar" org.cajuscript.CajuScript YOUR_SCRIPT_FILE.cj
```

Will be created a folder **./cajuscript-classes/my/package/** with these files:

- MyClass.cj
- MyClass.class

The MyClass*.cj* is for comparing with the script in executing for know if is need rebuild or not. The MyClass*.class* is the result from Java compiler.

The MyClass*.java* is the source Java generated and compiled. _(Version 0.3.5 only!)_

## Binary Output Directory

To define another folder have two ways (the folders are created automatically):

- Defining the folder path into of CajuScript instance:

```
org.cajuscript.CajuScript caju = new org.cajuscript.CajuScript();
caju.setCompileBaseDirectory("MY/PATH/TO/FILES/COMPILED");
```

- Or defining the folder path into of Script header:

```
caju.compile.baseDirectory: MY/PATH/TO/FILES/COMPILED
caju.compile: mypackage.MyClass
System.out.println("Compile ok!"); 
```
