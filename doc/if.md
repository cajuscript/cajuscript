# If

The condition block.

Use *?* to embrace the code refering at "if".

```
x < 10 ?
    java.lang.System.out.println("X is less than 10!")
?
```

## Else

`??` is the "else".

```
x < 10 ?
    java.lang.System.out.println("X is less than 10!")
??
    java.lang.System.out.println("X is greater than 10!")
?
```

## Else If

*?* _CONDITION_ *?* for "else if".

```
x < 10 & x > 0 ?
    System.out.println("X is less than 10!")
? x > 10 ?
    System.out.println("X is greater than 10!")
? x = 10 ?
    System.out.println("X is 10!")
??
    System.out.println("X is less than 1!")
?
```

## Or

*|* used for the sign "or".

```
x = 10 | x = 0 ?
    System.out.println("X is 10 or 0!")
?
```

## And

*&* used for the sign "and".

```
x > 0 & x < 10 ?
    System.out.println("X is greater than 0 and less than 10!")
?
```

## Groups

Parenthesis to determine groups of conditions.

```
(x > 0 & x < 10) | (x > 10 & x < 20) ?
    System.out.println("X is an value between 1 and 19 but not 10!")
?
```

See about *[http://code.google.com/p/cajuscript/wiki/tutorialComparison Comparison operators]*