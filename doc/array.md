# Array

All about arrays.

## Creating

```
myArray = array.create("TYPE", LENGTH)
```

## Types

> The string with type name is case-insensitive.

Integer type:

```
array.create("int", len)
array.create("i", len)
```

Long type:

```
array.create("long", len)
array.create("l", len)
```

Float type:
```
array.create("float", len)
array.create("f", len)
```

Double type:
```
array.create("double", len)
array.create("d", len)
```

Char type:
```
array.create("char", len)
array.create("c", len)
```

Boolean type:
```
array.create("boolean", len)
array.create("bool", len)
array.create("b", len)
```

Byte type:
```
array.create("byte", len)
array.create("bt", len)
```

String type:

```
array.create("string", len)
array.create("s", len)
```

For any kind of class type:

```
array.create("java.lang.Object", len)
```

## Set and get values from an array

```
STRING_ARRAY = array.create("s", 1)
// SET
array.set(STRING_ARRAY, 0, "Zero")
// GET
strZero = array.get(STRING_ARRAY, 0)
```

## Length or Size

```
length = array.size(myArray)
```

## Interaction

```
// Create
strArray = array.create("java.lang.String", 2)

// Setting
array.set(strArray, 0, "String 0")
array.set(strArray, 1, "String 1")

// Interaction
x = 0
x < array.size("s", 2) @
   // Getting
   System.out.println(array.get(strArray, x))
   x = x + 1
@
```