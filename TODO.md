# Tasks

All tasks to further development:

## Bug with methods

Has the same behaviour, the parameter is not passed:

```
a = map.toJSON()
a = map.toJSON(20)
```

## New Arrays and Maps

```
array = {'a', 1, true}
map = {'key1' = 'a', 'key2' = 1, 'key3' = true}
```

## Safe-null-operators

```
s = person?.getAddress()?:defaultAddress.getPostalCode()?.getValue();
```

## Class Implementation
```
ClassName : InterfaceClass, ExtendClass ##
    param = ''
    \ Useful methods:
    classInit #
      param = 'x'
    #
    methodMissing(method, args) #
    #
    paramMissing(param, value) #
    #
##
```