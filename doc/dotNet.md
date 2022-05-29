# CajuScript for .Net

How to use CajuScript in .NET.

This tutorial will try to explain how to use CajuScript in .NET

First you have to download IKVM.NET from here

http://sourceforge.net/project/showfiles.php?group_id=69637 ( ikvmbin-0.36.0.11.zip )

After downloading to install simply extract the files from the archive.

The next step is to compile the cajuscript jar file with IKVM.NET to do this open a command or shell window, cd to ikvm\bin, and type:

```
ikvmc path_to_cajuscript_jar -target:library
```

A DLL file will be created.
So open Visual Studio, create a console application, and add as referecences the following files:

* cajuscript dll file created
* IKVM.OpenJDK.ClassLibrary.dll ( ikvm\bin )
* IKVM.Runtime.dll ( ikvm\bin )

In the Program.cs file you can now use cajuscript, if you type org.cajuscript. you can see all the classes.

Here is a lite C# example how to do it:

```
String value = "1";
long times = 100000;
long time = System.DateTime.Now.Ticks;

// Loop test           
org.cajuscript.CajuScript caju = new org.cajuscript.CajuScript();
caju.eval("x = 0; x < " + times + " @ x = x + " + value + "; @");
System.Console.WriteLine("CajuScript: " + ((System.DateTime.Now.Ticks - time) / TimeSpan.TicksPerMillisecond) + "ms - " + caju.get("x"));
// String test
org.cajuscript.CajuScriptEngine cajue = new org.cajuscript.CajuScriptEngine();
String javaHello = "Java: Hello!";
cajue.put("javaHello", javaHello);
String script = "$java.lang;";
script += "System.out.println(javaHello);";
script += "cajuHello = 'Caju: Hi!';";
cajue.eval(script);
System.Console.WriteLine(cajue.get("cajuHello"));

System.Console.ReadLine();
```

For more details in compiling with ikvmc just go to http://www.ikvm.net/userguide/ikvmc.html