package org.cajuscript;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class TestMethods {

    public class Demo extends ArrayList {
        public String toString() {
            return "xpto";
        }
    }

    public TestMethods() {

    }

    public int toJSON() {
        return 1;
    }

    public int toJSON(int x) {
        return x;
    }

    public int get(boolean x) {
        return -1;
    }

    public int get(int x) {
        return x - 1;
    }

    public int get(Integer x) {
        return x - 1;
    }

    public String get(String demo) {
        return "demoX";
    }

    public String get(Object demo) {
        return "object";
    }

    public String get(Demo demo) {
        return "demo";
    }

    public String get(Collection demo) {
        return "demoCol";
    }

    public String get(List demo) {
        return "demoList";
    }

    public int[] getIds() {
        return new int[] {1, 2, 3};
    }

    @org.junit.Test
    public void test() throws CajuScriptException {
        CajuScript cajuScript = new CajuScript();
        cajuScript.set("test", this);
        cajuScript.set("demo", new Demo());
        cajuScript.eval(
                "my = [1, 2, 3]; ids = test.getIds(); i= 0; i < array.size(ids) @ System.out.println(''+ i); i += 1; @"
                //"System.out.println(org.cajuscript.TestMethods.get(\"\"+test.toJSON(20)));"+
                //"a = 1; x = 1 +\n1;"
                        /*+" a = b ? \n" +
                        "a = a.c(12)\n.c(13)\n?\n" +
                        "System.out\n" +
                        ".println(test.get(demo));" +
                        "System.out.println(test.get(test.toJSON(20)));"+
                "System.out.println(test.get(\"a\"));"*/
        );
        assertEquals(2, cajuScript.get("x"));
    }
}
