# Function

Declaring functions and using it.

Is allowed four aways to defining functions:

First way:

```
funcName(param1, param2) #
    ...
#
```

Second way:

```
funcName(param1 param2) #
    ...
#
```

Third way:

```
funcName param1, param2 #
    ...
#
```

Forth way:

```
funcName param1 param2 #
    ...
#
```

Case not have parameters:
```
funcName() #
    ...
#

// OR
funcName #
    ...
#
```

## Return

The sign of return is "~".

```
funcName #
    ~ 1
#
```

## Calls

Always with parenthesis and commas, use commas to separate parameters:
```
// Without parameters
funcName()

// With parameters
funcName1(1)
funcName2(1, 2)
funcName2(x, y)
```

## Sample
```
x = 10
addWithX(v1, v2) #
    ~ x + v1 + v2
#
x = addWithX(1, 2)
```

## Context

If have a variable global with same name of a variable internal of function, must use the sign of the context global to differentiate the variables. The sign of global context is `\` (look the `\x` is the global variable "x"):

```
x = 5
addWithX(x) #
    ~ \x + x
#
```