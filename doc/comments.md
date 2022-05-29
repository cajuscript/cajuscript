## Comments

All of these signs of comments are allowed:

```
    -- Comment...

    // Comment...

    \ Comment...
```

Comments supported only as an entire line:

```
x = 1 ?
    // Comments...
    x += 1
?
```

This comment is *not* supported:

```
x = 1 ? // This is unsupported...
    x += 1
?
```