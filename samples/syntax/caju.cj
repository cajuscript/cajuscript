$java.lang

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

// LOOP
x = 0
x < 20 & x >= 0 @
    System.out.println(x)
    x += 1
    x = 10 ?
        !!
    ??
        ..
    ?
@

// FUNCTION
x = 5
addWithX(v1, v2) #
    ~ .x + v1 + v2
#
x = addWithX(10, 20)
System.out.println("X = "+ x)

// TRY/CATCH
e ^
    "".substring(0, -1)
^^
    System.out.println("Error: "+ e.getMessage())
^~^
    System.out.println("Finally...")
^