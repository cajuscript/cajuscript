# Operable

Here we are doing arithmetic calculations with a customizable Java class.

Using the Java class `org.cajuscript.math.Operable`, you can make calculations using your own Java class to manipulate each arithmetic operation.

## Your Custom Class

You need implement a Java class, like:

```
package my.package;

public class Vector2D implements org.cajuscript.math.Operable<Vector2D> {
    private int x, y = 0;
    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public Vector2D plus(Vector2D other) {
        return new Vector2D(x + other.x, y + other.y);
    }
    public Vector2D subtract(Vector2D other) {
        return new Vector2D(x - other.x, y - other.y);
    }
    public Vector2D multiply(Vector2D other) {
        return new Vector2D(x * other.x, y * other.y);
    }
    public Vector2D divide(Vector2D other) {
        return new Vector2D(x / other.x, y / other.y);
    }
    public Vector2D module(Vector2D other) {
        return new Vector2D(x % other.x, y % other.y);
    }
}
```

## Use It

Now can use the customizable class for calculus like that:

```
printVector2D action, vector #
    System.out.println(action + ' x = ' + vector.getX())
    System.out.println(action + ' y = ' + vector.getY())
#

a = my.package.Vector2D(1, 2)
b = my.package.Vector2D(2, 3)

c = a + b
printVector2D('plus', c)

c = a - b
printVector2D('subtract', c)

c = a * b
printVector2D('multiply', c)

c = a / b
printVector2D('divide', c)

c = a % b
printVector2D('module', c)
```