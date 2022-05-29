# Loop

See the for each.

Is like "While" and "For" in others languages. In CajuScript only "While". But an variable can be used to do like "For".

### While

```
fileinput = java.io.FileInputStream("file.txt")
content = ""
loop = 1
loop = 1 @
    byte = fileinput.read()
    byte = -1 ?
        loop = 2
    ??
        content += caju.cast(byte, "c")
    ?
@
java.lang.System.out.println(content)
```

### For

```
x = 0
x < 10 @
    System.out.println(x)
    x += 1
@
```

### Or

| used for the sign "or".

```
x = 1
x = 1 | x = 2 | x = 3 @
    System.out.println("X is " + x + "!")
    x += 1
@
```

### And

& used for the sign "and".

```
x = 1
x > 0 & x < 10 @
    System.out.println("X is " + x + "!")
    x += 1
@
```

### Groups
Parenthesis to determine groups of conditions.

```
x = 1 (x > 0 & x < 10) | (x >= 10 & x < 20) @
    System.out.println("X is " + x + "!")
    x += 1
@
```

See about Comparison operators

### Continue and Break

The sign of continue is `..` and the sign of break is `!!`:

```
x = 0 x < 100 & x >= 0 @
    System.out.println(x) x += 1 x = 10 ?
        !!
    ??
        ..
    ?
@
```