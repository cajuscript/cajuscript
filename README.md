![Logo](https://raw.githubusercontent.com/cajuscript/cajuscript/main/assets/logo.svg)


# CajuScript

Fast scripting language for the JVM, compiles to binary class, and full customizable.

## Documentation

- [Here is the documentation.](https://github.com/cajuscript/cajuscript/blob/main/doc/README.md)

## Compilation

```
mvn compile
```
### Build

```
mvn package
```

### Run the Console

```
java -cp target/cajuscript-0.5.jar org.cajuscript.irc.CajuConsole
```

Then execute this sample code:

```
sayHello = true

sayHello ?
    System.out.println('Hello world!')
?

count = 0

count < 5 @
    System.out.println('Loop ')
    count += 1
@
```

### Run File

Sample code file as `source.cj`:

```
sayHello = true

sayHello ?
    System.out.println('Hello world!')
?

count = 0

count < 5 @
    System.out.println('Loop ')
    count += 1
@
```

Then execute the file:

```
java -jar target/cajuscript-0.5.jar source.cj
```

![image](https://raw.githubusercontent.com/cajuscript/cajuscript/main/assets/social.jpg)

## Old Repository

With legacy versions here:

- https://code.google.com/archive/p/cajuscript/
