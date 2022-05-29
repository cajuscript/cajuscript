# For Each

CajuScript haves the `caju.each("MY_CUSTOM_VAR_NAME", MY_ARRAY)` method to do *"for each"*, that does the interaction and load into an custom variable with the value of the current position.

Is possible know the current index and with _MAPs_ the current _KEY_, using `caju.index("MY_CUSTOM_VAR_NAME")` for know the current index and `caju.key("MY_CUSTOM_VAR_NAME")` for know the current _KEY_ from an _MAP_.

## Array

```
myArray = array.create('i', 3);
array.set(myArray, 0, 10);
array.set(myArray, 1, 20);
array.set(myArray, 2, 30);
System.out.println("---");
caju.each("_array_item", myArray) @
    System.out.println("index: "+ caju.index("_array_item"));
    System.out.println("value: "+ _array_item);
    System.out.println("---");
@
```

## List & Collection

```
System.out.println("===");
System.out.println("Collection");
System.out.println("===");
myCollection = java.util.ArrayList();
myCollection.add('10');
myCollection.add('20');
myCollection.add('30');
System.out.println("---");
caju.each('_collection_item', myCollection) @
    System.out.println("index: "+ caju.index("_collection_item"));
    System.out.println("value: "+ _collection_item);
    System.out.println("---");
@;
```

## Map

```
System.out.println("===");
System.out.println("Map");
System.out.println("===");
myMap = java.util.HashMap();
myMap.put('key1', '10');
myMap.put('key2', '20');
myMap.put('key3', '30');
System.out.println("---");
caju.each('_map_item', myMap) @
    System.out.println("index: "+ caju.index("_map_item"));
    System.out.println("key: "+ caju.key('_map_item'));
    System.out.println("value: "+ _map_item);
    System.out.println("---");
@
```