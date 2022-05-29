package org.cajuscript;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.File;

public class SimpleTest {
    @Test
    public void testSimpleJavaConstructor() throws CajuScriptException {
        CajuScript caju = new CajuScript();
        caju.eval("s = StringBuilder(); s.append('ok');");
        assertEquals("ok", caju.get("s").toString());
    }

    @Test
    public void testForWith10Times() throws CajuScriptException {
        CajuScript cajuScript = new CajuScript();
        cajuScript.eval(
                "x = 0; x < 10 @ x = x + 1; @"
        );
        CajuScript caju = new CajuScript();
        caju.eval("myVar = \"Value from CajuScript\"");
        System.out.println(caju.get("myVar"));
        assertEquals(10, cajuScript.get("x"));
    }

    @Test
    public void testIf() throws CajuScriptException {
        CajuScript cajuScript = new CajuScript();
        cajuScript.eval(
                "x = 0; x < 1 ? x = 1; ?"
        );
        assertEquals(1, cajuScript.get("x"));
        cajuScript.eval(
                "x > 0 ? x = 2; ?"
        );
        assertEquals(2, cajuScript.get("x"));
        cajuScript.eval(
                "x = 2 ? x = 3; ?"
        );
        assertEquals(3, cajuScript.get("x"));
        cajuScript.eval(
                "x <= 3 ? x = 4; ?"
        );
        assertEquals(4, cajuScript.get("x"));
        cajuScript.eval(
                "x >= 4 ? x = 5; ?"
        );
        assertEquals(5, cajuScript.get("x"));
        cajuScript.eval(
                "x ! 6 ? x = 6; ?"
        );
        assertEquals(6, cajuScript.get("x"));
    }

    @Test
    public void testCompilation() throws CajuScriptException {
        CajuScript cajuScript = new CajuScript();
        System.out.println(new File(".").getAbsolutePath());
        cajuScript.eval("caju.compile: my.package.MyClass\n\n" +
                "System.out.println(\"Compile ok!\");");
    }
}
