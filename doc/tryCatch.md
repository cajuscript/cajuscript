#Try/Catch

Catching all exceptions.

## Full

The full implementation is like:

```
e ^
    "".substring(0, -1)
^^
    System.out.println("Error: "+ e.getMessage())
^~^
    System.out.println("Finally...")
^
```


## Catch

Simple way to catch an exception:

```
e ^
    "".substring(0, -1)
^^
    System.out.println("Error: "+ e.getMessage())
^
```

## Finally

Is possible use the variable of exception over Finally:

```
e ^
    "".substring(0, -1)
^~^
    System.out.println("Finally... error: "+ e.getMessage())
^
```

## Throw Error

Creating a new exception and throw it:

```
    e ^
        caju.error()
    ^^
        System.out.println(e.getMessage())
    ^
```

Creating a new exception with a message:

```
    e ^
        caju.error("Error!")
    ^^
        System.out.println(e.getMessage())
    ^
```