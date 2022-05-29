# Syntax Handbook

Here you have the handbook with an overview of language syntax.

> See more too about [sampleSyntax custom syntax] and these alternatives:
> -[sampleSyntaxBasic Basic]
> -[sampleSyntaxJava Java]
> -[sampleSyntaxPortuguese Portuguese]

## Imports & Include

See more about [importAndInclude](arithmetic.md).

```
// Import
$java.lang
// Include file
$"script.cj"
```

## Variables:

```
// Defining a new variable
x = 0
x = "Programing 'Java' with CajuScript!"
x = 'Programing "Java" with CajuScript!'
x = "Programing \"Java\" with CajuScript!"
x = 'Programing \'Java\' with CajuScript!'
x = StringBuffer(x)
x.append(" "+ x)
System.out.println(x.toString())
```

## If

See more about [if](if.md).

```
// IF
x = 10
x < 10 & x > 0 ?
System.out.println("X is less than 10!")
? x > 10 & x ! 10 ?
System.out.println("X is greater than 10!")
? x = 10 ?
System.out.println("X is 10!")
??
System.out.println("X is less than 1!")
?
```

## Loop

See more about [loop](loop.md).

```
// LOOP
x = 0
x < 10 & x >= 0 @
System.out.println(x)
x += 1
@
```

## Function

See more about [functions](arithmetic.md).

```
// FUNCTION
// Allowed:
// addWithX v1, v2 # ... #
// addWithX(v1 v2) # ... #
x = 5
addWithX(v1, v2) #
    // "~" is the return
    ~ x + v1 + v2
#
x = addWithX(10, 20)
System.out.println("X = "+ x)
```

## Null value

```
// $ is the null value
x = $
```

## Arithmetics

See more about [arithmetics](arithmetic.md).

```
// + Addition
x = 0 + 1
x += 1

     // - Subtraction
     x = 0 - 1
     x -= 1
     
     // * Multiplication
     x = 0 * 1
     x *= 1
     
     // / Division
     x = 0 / 1
     x /= 1
     
     // % Modulus
     x = 0 % 1
     x %= 1
```

## Comparisons

See more about [comparisons](comparisons.md).

```
// = Equal to
(x = y)

// ! Not equal to
(x ! y)

// < Less Than
(x < y)

// > Greater Than
(x > y)

// < Less Than or Equal To
(x <= y)

// > Greater Than or Equal To
(x >= y)
```

## Comments

See more about [comments](comments.md).

```
-- Comment...

// Comment...

\ Comment...
```

# Try / Catch

See more about [try and catch](tryCatch.md).

```
// Try/Catch
e ^
"".substring(0, -1)
^^
System.out.println("Error: "+ e.getMessage())
^~^
System.out.println("Finally...")
^
```