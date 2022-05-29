/*
 * CajuScriptTest.java
 * 
 * This file is part of CajuScript.
 * 
 * CajuScript is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3, or (at your option) 
 * any later version.
 * 
 * CajuScript is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with CajuScript.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.cajuscript;

import java.util.Set;
import org.cajuscript.parser.Function;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author eduveks
 */
public class FullTest {

    public FullTest() throws CajuScriptException {
        
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
        /*
        while (true) {
            try {
                syntaxImport();
                syntaxIf();
                syntaxLoop();
                syntaxFunction();
                syntaxTryCatch();
                syntaxJavaImport();
                syntaxJavaIf();
                syntaxJavaLoop();
                syntaxJavaFunction();
                syntaxJavaTryCatch();
                syntaxBasicImport();
                syntaxBasicIf();
                syntaxBasicLoop();
                syntaxBasicFunction();
                syntaxBasicTryCatch();
            } catch (Exception e) {
                throw new Error(e);
            }
        }
        */
    }

    /**
     * Test of addGlobalSyntax and getGlobalSyntax, of class CajuScript.
     */
    @Test
    public void globalSyntax() {
        System.out.println("globalSyntax");
        Syntax syntax = new Syntax();
        String name = "CajuTest";
        CajuScript.addGlobalSyntax(name, syntax);
        Syntax syntaxX = CajuScript.getGlobalSyntax(name);
        assertEquals(syntax, syntaxX);
    }

    private void syntaxReload(CajuScript caju) throws CajuScriptException {
        String scriptReload = "";
        scriptReload += "x = 0;";
        scriptReload += "countIf = 0;";
        scriptReload += "countElseIf1 = 0;";
        scriptReload += "countElseIf2 = 0;";
        scriptReload += "countElse = 0;";
        scriptReload += "countLoop = 0;";
        scriptReload += "countBreak = 0;";
        scriptReload += "countContinue = 0;";
        scriptReload += "countTry = 0;";
        scriptReload += "countCatch = 0;";
        scriptReload += "countFinally = 0;";
        scriptReload += "countFunc = 0;";
        scriptReload += "xIf = 0;";
        scriptReload += "xElseIf1 = 0;";
        scriptReload += "xElseIf2 = 0;";
        scriptReload += "xElse = 0;";
        scriptReload += "xLoop = 0;";
        scriptReload += "xBreak = 0;";
        scriptReload += "xContinue = 0;";
        scriptReload += "xTry = 0;";
        scriptReload += "xCatch = 0;";
        scriptReload += "xFinally = 0;";
        scriptReload += "xFunc = 0;";
        caju.eval(scriptReload);
    }
    
    @Test
    public void syntaxImport() throws CajuScriptException {
        System.out.println("syntaxImport");
        CajuScript caju = new CajuScript();
        syntaxReload(caju);
        String script = "";
        java.util.Properties p = new java.util.Properties();
        p.setProperty("test", "test");
        script += "$java.util;";
        script += "p1 = Properties();";
        script += "p1.setProperty('test1', 'test');";
        script += "$java.util.Properties;";
        script += "p2 = Properties();";
        script += "p2.setProperty('test2', 'test');";
        script += "p3 = $;";
        script += "test #";
        script += "    \\p3 = Properties();";
        script += "    \\p3.setProperty('test3', 'test');";
        script += "#";
        script += "test();";
        script += "$'samples/mainReturn/main_return.cj';";
        caju.eval(script);
        syntaxCheckImport(caju);
        syntaxCheckImportCache(caju, script);
        syntaxCheckImportCompile(caju, script);
    }
    
    @Test
    public void syntaxIf() throws CajuScriptException {
        System.out.println("syntaxIf");
        CajuScript caju = new CajuScript();
        syntaxReload(caju);
        String scriptIf = "";
        scriptIf += "x = 1 | x = 0 ?";
        scriptIf += "    countIf += 1;";
        scriptIf += "    xIf += x;";
        scriptIf += "    x += 1;";
        scriptIf += "    x <= 1 | x > 5 @";
        scriptIf += "	   countLoop += 1;";
        scriptIf += "	   xLoop += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    @;";
        scriptIf += "    ex ^";
        scriptIf += "        countTry += 1;";
        scriptIf += "        xTry += x;";
        scriptIf += "        x += 1;";
        scriptIf += "        caju.error();";
        scriptIf += "    ^^";
        scriptIf += "        countCatch += 1;";
        scriptIf += "        xCatch += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    ^~^";
        scriptIf += "        countFinally += 1;";
        scriptIf += "        xFinally += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    ^;";
        scriptIf += "    x % 2 = 0 ?";
        scriptIf += "        countIf += 1;";
        scriptIf += "        xIf += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    ? x % 2 = 0 ?";
        scriptIf += "        countElseIf1 += 1;";
        scriptIf += "        xElseIf1 += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    ?  x % 2 = 0 ?";
        scriptIf += "        countElseIf2 += 1;";
        scriptIf += "        xElseIf2 += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    ??";
        scriptIf += "        countElse += 1;";
        scriptIf += "        xElse += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    ?;";
        scriptIf += "?;";
        scriptIf += "x = 1 ?";
        scriptIf += "    countIf += 1;";
        scriptIf += "    xIf += x;";
        scriptIf += "? x % 3 = 0 ?";
        scriptIf += "    countElseIf1 += 1;";
        scriptIf += "    xElseIf1 += x;";
        scriptIf += "    x += 1;";
        scriptIf += "    (x < 8 & x > 6) @";
        scriptIf += "	   countLoop += 1;";
        scriptIf += "	   xLoop += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    @;";
        scriptIf += "    ex ^";
        scriptIf += "        countTry += 1;";
        scriptIf += "        xTry += x;";
        scriptIf += "        x += 1;";
        scriptIf += "        caju.error();";
        scriptIf += "    ^^";
        scriptIf += "        countCatch += 1;";
        scriptIf += "        xCatch += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    ^~^";
        scriptIf += "        countFinally += 1;";
        scriptIf += "        xFinally += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    ^;";
        scriptIf += "    (x % 5 = 0) ?";
        scriptIf += "        countIf += 1;";
        scriptIf += "        xIf += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    ? (x % 5 = 1) ? ";
        scriptIf += "        countElseIf1 += 1;";
        scriptIf += "        xElseIf1 += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    ? (x % 5 = 0) ?";
        scriptIf += "        countElseIf2 += 1;";
        scriptIf += "        xElseIf2 += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    ??";
        scriptIf += "        countElse += 1;";
        scriptIf += "        xElse += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    ?;";
        scriptIf += "??";
        scriptIf += "    countElse += 1;";
        scriptIf += "    xElse += x;";
        scriptIf += "?;";
        scriptIf += "x = 10 ?";
        scriptIf += "    countIf += 1;";
        scriptIf += "    xIf += x;";
        scriptIf += "? x = 11 ?";
        scriptIf += "    countElseIf1 += 1;";
        scriptIf += "    xElseIf1 += x;";
        scriptIf += "??";
        scriptIf += "    countElse += 1;";
        scriptIf += "    xElse += x;";
        scriptIf += "    x += 1;";
        scriptIf += "    (x >= 13 & x <= 13) @";
        scriptIf += "	     countLoop += 1;";
        scriptIf += "	     xLoop += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    @;";
        scriptIf += "    ex ^";
        scriptIf += "        countTry += 1;";
        scriptIf += "        xTry += x;";
        scriptIf += "        x += 1;";
        scriptIf += "        caju.error();";
        scriptIf += "    ^^";
        scriptIf += "        countCatch += 1;";
        scriptIf += "        xCatch += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    ^~^";
        scriptIf += "        countFinally += 1;";
        scriptIf += "        xFinally += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    ^;";
        scriptIf += "    (x % 5 = 0) ?";
        scriptIf += "        countIf += 1;";
        scriptIf += "        xIf += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    ? (x % 5 = 0) ?";
        scriptIf += "        countElseIf1 += 1;";
        scriptIf += "        xElseIf1 += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    ? (x = 17) ?";
        scriptIf += "        countElseIf2 += 1;";
        scriptIf += "        xElseIf2 += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    ??";
        scriptIf += "        countElse += 1;";
        scriptIf += "        xElse += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    ?;";
        scriptIf += "?;";
        caju.eval(scriptIf);
        syntaxCheckIf(caju);
        syntaxCheckIfCache(caju, scriptIf);
        syntaxCheckIfCompile(caju, scriptIf);
    }
    
    @Test
    public void syntaxLoop() throws CajuScriptException {
        System.out.println("syntaxLoop");
        CajuScript caju = new CajuScript();
        syntaxReload(caju);
        String scriptLoop = "";
        scriptLoop += "loop: (x >= 0 & x <= 10) @";
        scriptLoop += "    countLoop += 1;";
        scriptLoop += "    xLoop += x;";
        scriptLoop += "    x += 1;";
        scriptLoop += "    (x = 7) @";
        scriptLoop += "	       countLoop += 1;";
        scriptLoop += "	       xLoop += x;";
        scriptLoop += "        !!;";
        scriptLoop += "    @;";
        scriptLoop += "    (x = 5) @";
        scriptLoop += "	       countLoop = 10 ?";
        scriptLoop += "            countBreak += 1;";
        scriptLoop += "            xBreak += x;";
        scriptLoop += "	           ! !;";
        scriptLoop += "	       ?;";
        scriptLoop += "	       countLoop += 1;";
        scriptLoop += "	       xLoop += x;";
        scriptLoop += "        countContinue += 1;";
        scriptLoop += "        xContinue += x;";
        scriptLoop += "        x += 1;";
        scriptLoop += "        . .;";
        scriptLoop += "    @;";
        scriptLoop += "    ex ^";
        scriptLoop += "        countTry += 1;";
        scriptLoop += "        xTry += x;";
        scriptLoop += "        caju.error();";
        scriptLoop += "    ^^";
        scriptLoop += "        countCatch += 1;";
        scriptLoop += "        xCatch += x;";
        scriptLoop += "    ^~^";
        scriptLoop += "        countFinally += 1;";
        scriptLoop += "        xFinally += x;";
        scriptLoop += "    ^;";
        scriptLoop += "    (x % 3 = 0) ?";
        scriptLoop += "        countIf += 1;";
        scriptLoop += "        xIf += x;";
        scriptLoop += "    ? (x % 5 = 0) ?";
        scriptLoop += "        countElseIf1 += 1;";
        scriptLoop += "        xElseIf1 += x;";
        scriptLoop += "    ? (x % 4 = 0) ?";
        scriptLoop += "        countElseIf2 += 1;";
        scriptLoop += "        xElseIf2 += x;";
        scriptLoop += "    ??";
        scriptLoop += "        countElse += 1;";
        scriptLoop += "        xElse += x;";
        scriptLoop += "    ?;";
        scriptLoop += "    (x = 10) ?";
        scriptLoop += "        countIf += 1;";
        scriptLoop += "        xIf += x;";
        scriptLoop += "        (true) @";
        scriptLoop += "            countLoop += 1;";
        scriptLoop += "            xLoop += x;";
        scriptLoop += "            countBreak += 1;";
        scriptLoop += "            xBreak += x;";
        scriptLoop += "            !! loop;";
        scriptLoop += "        @;";
        scriptLoop += "    ??";
        scriptLoop += "        countIf += 1;";
        scriptLoop += "        xIf += x;";
        scriptLoop += "        (true) @";
        scriptLoop += "            countLoop += 1;";
        scriptLoop += "            xLoop += x;";
        scriptLoop += "            countContinue += 1;";
        scriptLoop += "            xContinue += x;";
        scriptLoop += "            .. loop;";
        scriptLoop += "        @;";
        scriptLoop += "    ?;";
        scriptLoop += "@;";
        caju.eval(scriptLoop);
        syntaxCheckLoop(caju);
        syntaxCheckLoopCache(caju, scriptLoop);
        syntaxCheckLoopCompile(caju, scriptLoop);
    }
    
    @Test
    public void syntaxFunction() throws CajuScriptException {
        System.out.println("syntaxFunction");
        CajuScript caju = new CajuScript();
        syntaxReload(caju);
        String scriptFunc = "";
        scriptFunc += "func x #";
        scriptFunc += "    x += 1;";
        scriptFunc += "    .x += 1;";
        scriptFunc += "    .countFunc += 1;";
        scriptFunc += "    (x >= 1 & x <= 1) @";
        scriptFunc += "	       .countLoop += 1;";
        scriptFunc += "	       .xLoop += x;";
        scriptFunc += "        x += 1;";
        scriptFunc += "    @;";
        scriptFunc += "    ex ^";
        scriptFunc += "        .countTry += 1;";
        scriptFunc += "        .xTry += x;";
        scriptFunc += "        caju.error();";
        scriptFunc += "    ^^";
        scriptFunc += "        .countCatch += 1;";
        scriptFunc += "        .xCatch += x;";
        scriptFunc += "    ^~^";
        scriptFunc += "        .countFinally += 1;";
        scriptFunc += "        .xFinally += x;";
        scriptFunc += "    ^;";
        scriptFunc += "    x = 2 ?";
        scriptFunc += "        .x += 1;";
        scriptFunc += "        .countIf += 1;";
        scriptFunc += "        .xIf += x;";
        scriptFunc += "    ? (x % 4 = 0) ?";
        scriptFunc += "        .x += 1;";
        scriptFunc += "        .countElseIf1 += 1;";
        scriptFunc += "        .xElseIf1 += x;";
        scriptFunc += "    ? (x % 2 = 0) ?";
        scriptFunc += "        .x += 1;";
        scriptFunc += "        .countElseIf2 += 1;";
        scriptFunc += "        .xElseIf2 += x;";
        scriptFunc += "    ??";
        scriptFunc += "        .x += 1;";
        scriptFunc += "        .countElse += 1;";
        scriptFunc += "        .xElse += x;";
        scriptFunc += "    ?;";
        scriptFunc += "    x < 10 ?";
        scriptFunc += "        ~ func(x);";
        scriptFunc += "    ?;";
        scriptFunc += "    .x += 1;";
        scriptFunc += "    ~ x;";
        scriptFunc += "#";
        scriptFunc += "xFunc += func(x);";
        scriptFunc += "func1 x, y #";
        scriptFunc += "    .x += 1;";
        scriptFunc += "    .countFunc += 1;";
        scriptFunc += "    ~ x + y;";
        scriptFunc += "#;";
        scriptFunc += "xFunc += func1(x, 5);";
        scriptFunc += "func2 (x, y) #";
        scriptFunc += "    .x += 1;";
        scriptFunc += "    .countFunc += 1;";
        scriptFunc += "    ~ x + y;";
        scriptFunc += "#";
        scriptFunc += "xFunc += func2(x, 5);";
        scriptFunc += "func3 #";
        scriptFunc += "    .x += 1;";
        scriptFunc += "    .countFunc += 1;";
        scriptFunc += "    ~ x + 1;";
        scriptFunc += "#";
        scriptFunc += "xFunc += func3();";
        caju.eval(scriptFunc);
        syntaxCheckFunction(caju);
        syntaxCheckFunctionCache(caju, scriptFunc);
        syntaxCheckFunctionCompile(caju, scriptFunc);
    }
    
    @Test
    public void syntaxTryCatch() throws CajuScriptException {
        System.out.println("syntaxTryCatch");
        CajuScript caju = new CajuScript();
        syntaxReload(caju);
        String scriptTry = "";
        scriptTry += "e ^";
        scriptTry += "    x += 1;";
        scriptTry += "    countTry += 1;";
        scriptTry += "    xTry += x;";
        scriptTry += "    (x = 0 | x = 1) @";
        scriptTry += "        x += 1;";
        scriptTry += "	      countLoop += 1;";
        scriptTry += "	      xLoop += x;";
        scriptTry += "    @;";
        scriptTry += "    ex ^";
        scriptTry += "        x += 1;";
        scriptTry += "        countTry += 1;";
        scriptTry += "        xTry += x;";
        scriptTry += "        caju.error();";
        scriptTry += "    ^^";
        scriptTry += "        x += 1;";
        scriptTry += "        countCatch += 1;";
        scriptTry += "        xCatch += x;";
        scriptTry += "    ^~^";
        scriptTry += "        x += 1;";
        scriptTry += "        countFinally += 1;";
        scriptTry += "        xFinally += x;";
        scriptTry += "    ^;";
        scriptTry += "    x = 0 | x = 5 ?";
        scriptTry += "        x += 1;";
        scriptTry += "        countIf += 1;";
        scriptTry += "        xIf += x;";
        scriptTry += "    ? (x % 4 = 0) ?";
        scriptTry += "        x += 1;";
        scriptTry += "        countElseIf1 += 1;";
        scriptTry += "        xElseIf1 += x;";
        scriptTry += "    ? (x % 2 = 0) ?";
        scriptTry += "        x += 1;";
        scriptTry += "        countElseIf2 += 1;";
        scriptTry += "        xElseIf2 += x;";
        scriptTry += "    ??";
        scriptTry += "        x += 1;";
        scriptTry += "        countElse += 1;";
        scriptTry += "        xElse += x;";
        scriptTry += "    ?;";
        scriptTry += "    caju.error();";
        scriptTry += "^^";
        scriptTry += "    x += 1;";
        scriptTry += "    countCatch += 1;";
        scriptTry += "    xCatch += x;";
        scriptTry += "    (x = 7) @";
        scriptTry += "        x += 1;";
        scriptTry += "	      countLoop += 1;";
        scriptTry += "	      xLoop += x;";
        scriptTry += "    @;";
        scriptTry += "    ex ^";
        scriptTry += "        x += 1;";
        scriptTry += "        countTry += 1;";
        scriptTry += "        xTry += x;";
        scriptTry += "        caju.error();";
        scriptTry += "    ^^";
        scriptTry += "        x += 1;";
        scriptTry += "        countCatch += 1;";
        scriptTry += "        xCatch += x;";
        scriptTry += "    ^~^";
        scriptTry += "        x += 1;";
        scriptTry += "        countFinally += 1;";
        scriptTry += "        xFinally += x;";
        scriptTry += "    ^;";
        scriptTry += "    x = 0 ?";
        scriptTry += "        x += 1;";
        scriptTry += "        countIf += 1;";
        scriptTry += "        xIf += x;";
        scriptTry += "    ? (x = 11) ?";
        scriptTry += "        x += 1;";
        scriptTry += "        countElseIf1 += 1;";
        scriptTry += "        xElseIf1 += x;";
        scriptTry += "    ? (x = 0) ?";
        scriptTry += "        x += 1;";
        scriptTry += "        countElseIf2 += 1;";
        scriptTry += "        xElseIf2 += x;";
        scriptTry += "    ??";
        scriptTry += "        x += 1;";
        scriptTry += "        countElse += 1;";
        scriptTry += "        xElse += x;";
        scriptTry += "    ?;";
        scriptTry += "^~^";
        scriptTry += "    x += 1;";
        scriptTry += "    countFinally += 1;";
        scriptTry += "    xFinally += x;";
        scriptTry += "    (x = 13) @";
        scriptTry += "        x += 1;";
        scriptTry += "	      countLoop += 1;";
        scriptTry += "	      xLoop += x;";
        scriptTry += "    @;";
        scriptTry += "    ex ^";
        scriptTry += "        x += 1;";
        scriptTry += "        countTry += 1;";
        scriptTry += "        xTry += x;";
        scriptTry += "        caju.error();";
        scriptTry += "    ^^";
        scriptTry += "        x += 1;";
        scriptTry += "        countCatch += 1;";
        scriptTry += "        xCatch += x;";
        scriptTry += "    ^~^";
        scriptTry += "        x += 1;";
        scriptTry += "        countFinally += 1;";
        scriptTry += "        xFinally += x;";
        scriptTry += "    ^;";
        scriptTry += "    x = 0 ?";
        scriptTry += "        x += 1;";
        scriptTry += "        countIf += 1;";
        scriptTry += "        xIf += x;";
        scriptTry += "    ? x = 0 ?";
        scriptTry += "        x += 1;";
        scriptTry += "        countElseIf1 += 1;";
        scriptTry += "        xElseIf1 += x;";
        scriptTry += "    ? x = 0 ?";
        scriptTry += "        x += 1;";
        scriptTry += "        countElseIf2 += 1;";
        scriptTry += "        xElseIf2 += x;";
        scriptTry += "    ??";
        scriptTry += "        x += 1;";
        scriptTry += "        countElse += 1;";
        scriptTry += "        xElse += x;";
        scriptTry += "    ?;";
        scriptTry += "^;";
        scriptTry += "e ^";
        scriptTry += "    x += 1;";
        scriptTry += "    countTry += 1;";
        scriptTry += "    xTry = x;";
        scriptTry += "^;";
        scriptTry += "e ^";
        scriptTry += "    x += 1;";
        scriptTry += "    countTry += 1;";
        scriptTry += "    xTry += x;";
        scriptTry += "    caju.error();";
        scriptTry += "^^";
        scriptTry += "    x += 1;";
        scriptTry += "    countCatch += 1;";
        scriptTry += "    xCatch += x;";
        scriptTry += "^;";
        scriptTry += "e ^";
        scriptTry += "    x += 1;";
        scriptTry += "    countTry += 1;";
        scriptTry += "    xTry += x;";
        scriptTry += "    caju.error();";
        scriptTry += "^~^";
        scriptTry += "    x += 1;";
        scriptTry += "    countFinally += 1;";
        scriptTry += "    xFinally += x;";
        scriptTry += "^;";
        caju.eval(scriptTry);
        syntaxCheckTryCatch(caju);
        syntaxCheckTryCatchCache(caju, scriptTry);
        syntaxCheckTryCatchCompile(caju, scriptTry);
    }
    
    @Test
    public void syntaxJavaImport() throws CajuScriptException {
        System.out.println("syntaxJavaImport");
        CajuScript caju = new CajuScript();
        syntaxReload(caju);
        String script = "";
        java.util.Properties p = new java.util.Properties();
        p.setProperty("test", "test");
        script += "caju.syntax: CajuJava;";
        script += "import java.util;";
        script += "p1 = Properties();";
        script += "p1.setProperty('test1', 'test');";
        script += "import java.util.Properties;";
        script += "p2 = Properties();";
        script += "p2.setProperty('test2', 'test');";
        script += "p3 = null;";
        script += "function test {";
        script += "    root.p3 = Properties();";
        script += "    root.p3.setProperty('test3', 'test');";
        script += "}";
        script += "test();";
        script += "import 'samples/mainReturn/main_return.cj';";
        caju.eval(script);
        syntaxCheckImport(caju);
        syntaxCheckImportCache(caju, script);
        syntaxCheckImportCompile(caju, script);
    }
    
    @Test
    public void syntaxJavaIf() throws CajuScriptException {
        System.out.println("syntaxJavaIf");
        CajuScript caju = new CajuScript();
        syntaxReload(caju);
        String scriptIf = "caju.syntax: CajuJava;";
        scriptIf += "if x = 1 | x = 0 {";
        scriptIf += "    countIf += 1;";
        scriptIf += "    xIf += x;";
        scriptIf += "    x += 1;";
        scriptIf += "    while x <= 1 | x > 5 {";
        scriptIf += "	   countLoop += 1;";
        scriptIf += "	   xLoop += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    }";
        scriptIf += "    try ex {";
        scriptIf += "        countTry += 1;";
        scriptIf += "        xTry += x;";
        scriptIf += "        x += 1;";
        scriptIf += "        caju.error();";
        scriptIf += "    } catch {";
        scriptIf += "        countCatch += 1;";
        scriptIf += "        xCatch += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    } finally {";
        scriptIf += "        countFinally += 1;";
        scriptIf += "        xFinally += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    }";
        scriptIf += "    if x % 2 = 0 {";
        scriptIf += "        countIf += 1;";
        scriptIf += "        xIf += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    } else if x % 2 = 0 {";
        scriptIf += "        countElseIf1 += 1;";
        scriptIf += "        xElseIf1 += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    } else if x % 2 = 0 {";
        scriptIf += "        countElseIf2 += 1;";
        scriptIf += "        xElseIf2 += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    } else {";
        scriptIf += "        countElse += 1;";
        scriptIf += "        xElse += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    }";
        scriptIf += "}";
        scriptIf += "if x = 1 {";
        scriptIf += "    countIf += 1;";
        scriptIf += "    xIf += x;";
        scriptIf += "} else if x % 3 = 0 {";
        scriptIf += "    countElseIf1 += 1;";
        scriptIf += "    xElseIf1 += x;";
        scriptIf += "    x += 1;";
        scriptIf += "    while (x < 8 & x > 6) {";
        scriptIf += "	   countLoop += 1;";
        scriptIf += "	   xLoop += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    }";
        scriptIf += "    try ex {";
        scriptIf += "        countTry += 1;";
        scriptIf += "        xTry += x;";
        scriptIf += "        x += 1;";
        scriptIf += "        caju.error();";
        scriptIf += "    } catch {";
        scriptIf += "        countCatch += 1;";
        scriptIf += "        xCatch += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    } finally {";
        scriptIf += "        countFinally += 1;";
        scriptIf += "        xFinally += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    }";
        scriptIf += "    if (x % 5 = 0) {";
        scriptIf += "        countIf += 1;";
        scriptIf += "        xIf += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    } else if (x % 5 = 1) {";
        scriptIf += "        countElseIf1 += 1;";
        scriptIf += "        xElseIf1 += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    } else if (x % 5 = 0) {";
        scriptIf += "        countElseIf2 += 1;";
        scriptIf += "        xElseIf2 += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    } else {";
        scriptIf += "        countElse += 1;";
        scriptIf += "        xElse += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    }";
        scriptIf += "} else {";
        scriptIf += "    countElse += 1;";
        scriptIf += "    xElse += x;";
        scriptIf += "}";
        scriptIf += "if x = 10 {";
        scriptIf += "    countIf += 1;";
        scriptIf += "    xIf += x;";
        scriptIf += "} else if x = 11 {";
        scriptIf += "    countElseIf1 += 1;";
        scriptIf += "    xElseIf1 += x;";
        scriptIf += "} else {";
        scriptIf += "    countElse += 1;";
        scriptIf += "    xElse += x;";
        scriptIf += "    x += 1;";
        scriptIf += "    while (x >= 13 & x <= 13) {";
        scriptIf += "	     countLoop += 1;";
        scriptIf += "	     xLoop += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    }";
        scriptIf += "    try ex {";
        scriptIf += "        countTry += 1;";
        scriptIf += "        xTry += x;";
        scriptIf += "        x += 1;";
        scriptIf += "        caju.error();";
        scriptIf += "    } catch {";
        scriptIf += "        countCatch += 1;";
        scriptIf += "        xCatch += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    } finally {";
        scriptIf += "        countFinally += 1;";
        scriptIf += "        xFinally += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    }";
        scriptIf += "    if (x % 5 = 0) {";
        scriptIf += "        countIf += 1;";
        scriptIf += "        xIf += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    } else if (x % 5 = 0) {";
        scriptIf += "        countElseIf1 += 1;";
        scriptIf += "        xElseIf1 += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    } else if (x = 17) {";
        scriptIf += "        countElseIf2 += 1;";
        scriptIf += "        xElseIf2 += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    } else {";
        scriptIf += "        countElse += 1;";
        scriptIf += "        xElse += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    }";
        scriptIf += "}";
        caju.eval(scriptIf);
        syntaxCheckIf(caju);
        syntaxCheckIfCache(caju, scriptIf);
        syntaxCheckIfCompile(caju, scriptIf);
    }
    
    @Test
    public void syntaxJavaLoop() throws CajuScriptException {
        System.out.println("syntaxJavaLoop");
        CajuScript caju = new CajuScript();
        syntaxReload(caju);
        String scriptLoop = "caju.syntax: CajuJava;";
        scriptLoop += "loop: while (x >= 0 & x <= 10) {";
        scriptLoop += "    countLoop += 1;";
        scriptLoop += "    xLoop += x;";
        scriptLoop += "    x += 1;";
        scriptLoop += "    while (x = 7) {";
        scriptLoop += "	       countLoop += 1;";
        scriptLoop += "	       xLoop += x;";
        scriptLoop += "        break;";
        scriptLoop += "    }";
        scriptLoop += "    while (x = 5) {";
        scriptLoop += "	       if countLoop = 10 {";
        scriptLoop += "            countBreak += 1;";
        scriptLoop += "            xBreak += x;";
        scriptLoop += "	           break;";
        scriptLoop += "	       }";
        scriptLoop += "	       countLoop += 1;";
        scriptLoop += "	       xLoop += x;";
        scriptLoop += "        countContinue += 1;";
        scriptLoop += "        xContinue += x;";
        scriptLoop += "        x += 1;";
        scriptLoop += "        continue;";
        scriptLoop += "    }";
        scriptLoop += "    try ex {";
        scriptLoop += "        countTry += 1;";
        scriptLoop += "        xTry += x;";
        scriptLoop += "        caju.error();";
        scriptLoop += "    } catch {";
        scriptLoop += "        countCatch += 1;";
        scriptLoop += "        xCatch += x;";
        scriptLoop += "    } finally {";
        scriptLoop += "        countFinally += 1;";
        scriptLoop += "        xFinally += x;";
        scriptLoop += "    }";
        scriptLoop += "    if (x % 3 = 0) {";
        scriptLoop += "        countIf += 1;";
        scriptLoop += "        xIf += x;";
        scriptLoop += "    } else if (x % 5 = 0) {";
        scriptLoop += "        countElseIf1 += 1;";
        scriptLoop += "        xElseIf1 += x;";
        scriptLoop += "    } else if (x % 4 = 0) {";
        scriptLoop += "        countElseIf2 += 1;";
        scriptLoop += "        xElseIf2 += x;";
        scriptLoop += "    } else {";
        scriptLoop += "        countElse += 1;";
        scriptLoop += "        xElse += x;";
        scriptLoop += "    }";
        scriptLoop += "    if (x = 10) {";
        scriptLoop += "        countIf += 1;";
        scriptLoop += "        xIf += x;";
        scriptLoop += "        while (true) {";
        scriptLoop += "            countLoop += 1;";
        scriptLoop += "            xLoop += x;";
        scriptLoop += "            countBreak += 1;";
        scriptLoop += "            xBreak += x;";
        scriptLoop += "            break loop;";
        scriptLoop += "        }";
        scriptLoop += "    } else {";
        scriptLoop += "        countIf += 1;";
        scriptLoop += "        xIf += x;";
        scriptLoop += "        while (true) {";
        scriptLoop += "            countLoop += 1;";
        scriptLoop += "            xLoop += x;";
        scriptLoop += "            countContinue += 1;";
        scriptLoop += "            xContinue += x;";
        scriptLoop += "            continue loop;";
        scriptLoop += "        }";
        scriptLoop += "    }";
        scriptLoop += "}";
        caju.eval(scriptLoop);
        syntaxCheckLoop(caju);
        syntaxCheckLoopCache(caju, scriptLoop);
        syntaxCheckLoopCompile(caju, scriptLoop);
    }
    
    @Test
    public void syntaxJavaFunction() throws CajuScriptException {
        System.out.println("syntaxJavaFunction");
        CajuScript caju = new CajuScript();
        syntaxReload(caju);
        String scriptFunc = "caju.syntax: CajuJava;";
        scriptFunc += "function func x {";
        scriptFunc += "    x += 1;";
        scriptFunc += "    root.x += 1;";
        scriptFunc += "    root.countFunc += 1;";
        scriptFunc += "    while (x >= 1 & x <= 1) {";
        scriptFunc += "	       root.countLoop += 1;";
        scriptFunc += "	       root.xLoop += x;";
        scriptFunc += "        x += 1;";
        scriptFunc += "    }";
        scriptFunc += "    try ex {";
        scriptFunc += "        root.countTry += 1;";
        scriptFunc += "        root.xTry += x;";
        scriptFunc += "        caju.error();";
        scriptFunc += "    } catch {";
        scriptFunc += "        root.countCatch += 1;";
        scriptFunc += "        root.xCatch += x;";
        scriptFunc += "    } finally {";
        scriptFunc += "        root.countFinally += 1;";
        scriptFunc += "        root.xFinally += x;";
        scriptFunc += "    }";
        scriptFunc += "    if x = 2 {";
        scriptFunc += "        root.x += 1;";
        scriptFunc += "        root.countIf += 1;";
        scriptFunc += "        root.xIf += x;";
        scriptFunc += "    } else if (x % 4 = 0) {";
        scriptFunc += "        root.x += 1;";
        scriptFunc += "        root.countElseIf1 += 1;";
        scriptFunc += "        root.xElseIf1 += x;";
        scriptFunc += "    } else if (x % 2 = 0) {";
        scriptFunc += "        root.x += 1;";
        scriptFunc += "        root.countElseIf2 += 1;";
        scriptFunc += "        root.xElseIf2 += x;";
        scriptFunc += "    } else {";
        scriptFunc += "        root.x += 1;";
        scriptFunc += "        root.countElse += 1;";
        scriptFunc += "        root.xElse += x;";
        scriptFunc += "    }";
        scriptFunc += "    if x < 10 {";
        scriptFunc += "        return func(x);";
        scriptFunc += "    }";
        scriptFunc += "    root.x += 1;";
        scriptFunc += "    return x;";
        scriptFunc += "}";
        scriptFunc += "xFunc += func(x);";
        scriptFunc += "function func1 x, y {";
        scriptFunc += "    root.x += 1;";
        scriptFunc += "    root.countFunc += 1;";
        scriptFunc += "    return x + y;";
        scriptFunc += "}";
        scriptFunc += "xFunc += func1(x, 5);";
        scriptFunc += "function func2 (x, y) {";
        scriptFunc += "    root.x += 1;";
        scriptFunc += "    root.countFunc += 1;";
        scriptFunc += "    return x + y;";
        scriptFunc += "}";
        scriptFunc += "xFunc += func2(x, 5);";
        scriptFunc += "function func3 {";
        scriptFunc += "    root.x += 1;";
        scriptFunc += "    root.countFunc += 1;";
        scriptFunc += "    return x + 1;";
        scriptFunc += "}";
        scriptFunc += "xFunc += func3();";
        caju.eval(scriptFunc);
        syntaxCheckFunction(caju);
        syntaxCheckFunctionCache(caju, scriptFunc);
        syntaxCheckFunctionCompile(caju, scriptFunc);
    }
    
    @Test
    public void syntaxJavaTryCatch() throws CajuScriptException {
        System.out.println("syntaxJavaTryCatch");
        CajuScript caju = new CajuScript();
        syntaxReload(caju);
        String scriptTry = "caju.syntax: CajuJava;";
        scriptTry += "try e {";
        scriptTry += "    x += 1;";
        scriptTry += "    countTry += 1;";
        scriptTry += "    xTry += x;";
        scriptTry += "    while (x = 0 | x = 1) {";
        scriptTry += "        x += 1;";
        scriptTry += "	      countLoop += 1;";
        scriptTry += "	      xLoop += x;";
        scriptTry += "    }";
        scriptTry += "    try ex {";
        scriptTry += "        x += 1;";
        scriptTry += "        countTry += 1;";
        scriptTry += "        xTry += x;";
        scriptTry += "        caju.error();";
        scriptTry += "    } catch {";
        scriptTry += "        x += 1;";
        scriptTry += "        countCatch += 1;";
        scriptTry += "        xCatch += x;";
        scriptTry += "    } finally {";
        scriptTry += "        x += 1;";
        scriptTry += "        countFinally += 1;";
        scriptTry += "        xFinally += x;";
        scriptTry += "    }";
        scriptTry += "    if x = 0 | x = 5 {";
        scriptTry += "        x += 1;";
        scriptTry += "        countIf += 1;";
        scriptTry += "        xIf += x;";
        scriptTry += "    } else if (x % 4 = 0) {";
        scriptTry += "        x += 1;";
        scriptTry += "        countElseIf1 += 1;";
        scriptTry += "        xElseIf1 += x;";
        scriptTry += "    } else if (x % 2 = 0) {";
        scriptTry += "        x += 1;";
        scriptTry += "        countElseIf2 += 1;";
        scriptTry += "        xElseIf2 += x;";
        scriptTry += "    } else {";
        scriptTry += "        x += 1;";
        scriptTry += "        countElse += 1;";
        scriptTry += "        xElse += x;";
        scriptTry += "    }";
        scriptTry += "    caju.error();";
        scriptTry += "} catch {";
        scriptTry += "    x += 1;";
        scriptTry += "    countCatch += 1;";
        scriptTry += "    xCatch += x;";
        scriptTry += "    while (x = 7) {";
        scriptTry += "        x += 1;";
        scriptTry += "	      countLoop += 1;";
        scriptTry += "	      xLoop += x;";
        scriptTry += "    }";
        scriptTry += "    try ex {";
        scriptTry += "        x += 1;";
        scriptTry += "        countTry += 1;";
        scriptTry += "        xTry += x;";
        scriptTry += "        caju.error();";
        scriptTry += "    } catch {";
        scriptTry += "        x += 1;";
        scriptTry += "        countCatch += 1;";
        scriptTry += "        xCatch += x;";
        scriptTry += "    } finally {";
        scriptTry += "        x += 1;";
        scriptTry += "        countFinally += 1;";
        scriptTry += "        xFinally += x;";
        scriptTry += "    }";
        scriptTry += "    if x = 0 {";
        scriptTry += "        x += 1;";
        scriptTry += "        countIf += 1;";
        scriptTry += "        xIf += x;";
        scriptTry += "    } else if (x = 11) {";
        scriptTry += "        x += 1;";
        scriptTry += "        countElseIf1 += 1;";
        scriptTry += "        xElseIf1 += x;";
        scriptTry += "    } else if (x = 0) {";
        scriptTry += "        x += 1;";
        scriptTry += "        countElseIf2 += 1;";
        scriptTry += "        xElseIf2 += x;";
        scriptTry += "    } else {";
        scriptTry += "        x += 1;";
        scriptTry += "        countElse += 1;";
        scriptTry += "        xElse += x;";
        scriptTry += "    }";
        scriptTry += "} finally {";
        scriptTry += "    x += 1;";
        scriptTry += "    countFinally += 1;";
        scriptTry += "    xFinally += x;";
        scriptTry += "    while (x = 13) {";
        scriptTry += "        x += 1;";
        scriptTry += "	      countLoop += 1;";
        scriptTry += "	      xLoop += x;";
        scriptTry += "    }";
        scriptTry += "    try ex {";
        scriptTry += "        x += 1;";
        scriptTry += "        countTry += 1;";
        scriptTry += "        xTry += x;";
        scriptTry += "        caju.error();";
        scriptTry += "    } catch {";
        scriptTry += "        x += 1;";
        scriptTry += "        countCatch += 1;";
        scriptTry += "        xCatch += x;";
        scriptTry += "    } finally {";
        scriptTry += "        x += 1;";
        scriptTry += "        countFinally += 1;";
        scriptTry += "        xFinally += x;";
        scriptTry += "    }";
        scriptTry += "    if x = 0 {";
        scriptTry += "        x += 1;";
        scriptTry += "        countIf += 1;";
        scriptTry += "        xIf += x;";
        scriptTry += "    } else if x = 0 {";
        scriptTry += "        x += 1;";
        scriptTry += "        countElseIf1 += 1;";
        scriptTry += "        xElseIf1 += x;";
        scriptTry += "    } else if x = 0 {";
        scriptTry += "        x += 1;";
        scriptTry += "        countElseIf2 += 1;";
        scriptTry += "        xElseIf2 += x;";
        scriptTry += "    } else {";
        scriptTry += "        x += 1;";
        scriptTry += "        countElse += 1;";
        scriptTry += "        xElse += x;";
        scriptTry += "    }";
        scriptTry += "}";
        scriptTry += "try e {";
        scriptTry += "    x += 1;";
        scriptTry += "    countTry += 1;";
        scriptTry += "    xTry = x;";
        scriptTry += "}";
        scriptTry += "try e {";
        scriptTry += "    x += 1;";
        scriptTry += "    countTry += 1;";
        scriptTry += "    xTry += x;";
        scriptTry += "    caju.error();";
        scriptTry += "} catch {";
        scriptTry += "    x += 1;";
        scriptTry += "    countCatch += 1;";
        scriptTry += "    xCatch += x;";
        scriptTry += "}";
        scriptTry += "try e {";
        scriptTry += "    x += 1;";
        scriptTry += "    countTry += 1;";
        scriptTry += "    xTry += x;";
        scriptTry += "    caju.error();";
        scriptTry += "} finally {";
        scriptTry += "    x += 1;";
        scriptTry += "    countFinally += 1;";
        scriptTry += "    xFinally += x;";
        scriptTry += "}";
        caju.eval(scriptTry);
        syntaxCheckTryCatch(caju);
        syntaxCheckTryCatchCache(caju, scriptTry);
        syntaxCheckTryCatchCompile(caju, scriptTry);
    }
    
    @Test
    public void syntaxBasicImport() throws CajuScriptException {
        System.out.println("syntaxBasicImport");
        CajuScript caju = new CajuScript();
        syntaxReload(caju);
        String script = "";
        java.util.Properties p = new java.util.Properties();
        p.setProperty("test", "test");
        script += "caju.syntax: CajuBasic;";
        script += "import java.util;";
        script += "p1 = Properties();";
        script += "p1.setProperty('test1', 'test');";
        script += "import java.util.Properties;";
        script += "p2 = Properties();";
        script += "p2.setProperty('test2', 'test');";
        script += "p3 = null;";
        script += "function test ;";
        script += "    root.p3 = Properties();";
        script += "    root.p3.setProperty('test3', 'test');";
        script += "end;";
        script += "test();";
        script += "import 'samples/mainReturn/main_return.cj';";
        caju.eval(script);
        syntaxCheckImport(caju);
        syntaxCheckImportCache(caju, script);
        syntaxCheckImportCompile(caju, script);
    }
    
    @Test
    public void syntaxBasicIf() throws CajuScriptException {
        System.out.println("syntaxBasicIf");
        CajuScript caju = new CajuScript();
        syntaxReload(caju);
        String scriptIf = "caju.syntax: CajuBasic;";
        scriptIf += "if x = 1 | x = 0 ;";
        scriptIf += "    countIf += 1;";
        scriptIf += "    xIf += x;";
        scriptIf += "    x += 1;";
        scriptIf += "    while x <= 1 | x > 5 ;";
        scriptIf += "	   countLoop += 1;";
        scriptIf += "	   xLoop += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    end;";
        scriptIf += "    try ex ;";
        scriptIf += "        countTry += 1;";
        scriptIf += "        xTry += x;";
        scriptIf += "        x += 1;";
        scriptIf += "        caju.error();";
        scriptIf += "    catch;";
        scriptIf += "        countCatch += 1;";
        scriptIf += "        xCatch += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    finally;";
        scriptIf += "        countFinally += 1;";
        scriptIf += "        xFinally += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    end;";
        scriptIf += "    if x % 2 = 0 ;";
        scriptIf += "        countIf += 1;";
        scriptIf += "        xIf += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    elseif x % 2 = 0 ;";
        scriptIf += "        countElseIf1 += 1;";
        scriptIf += "        xElseIf1 += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    elseif x % 2 = 0 ;";
        scriptIf += "        countElseIf2 += 1;";
        scriptIf += "        xElseIf2 += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    else ;";
        scriptIf += "        countElse += 1;";
        scriptIf += "        xElse += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    end;";
        scriptIf += "end;";
        scriptIf += "if x = 1 ;";
        scriptIf += "    countIf += 1;";
        scriptIf += "    xIf += x;";
        scriptIf += "elseif x % 3 = 0 ;";
        scriptIf += "    countElseIf1 += 1;";
        scriptIf += "    xElseIf1 += x;";
        scriptIf += "    x += 1;";
        scriptIf += "    while (x < 8 & x > 6) ;";
        scriptIf += "	   countLoop += 1;";
        scriptIf += "	   xLoop += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    end;";
        scriptIf += "    try ex ;";
        scriptIf += "        countTry += 1;";
        scriptIf += "        xTry += x;";
        scriptIf += "        x += 1;";
        scriptIf += "        caju.error();";
        scriptIf += "    catch;";
        scriptIf += "        countCatch += 1;";
        scriptIf += "        xCatch += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    finally;";
        scriptIf += "        countFinally += 1;";
        scriptIf += "        xFinally += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    end;";
        scriptIf += "    if (x % 5 = 0) ;";
        scriptIf += "        countIf += 1;";
        scriptIf += "        xIf += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    elseif (x % 5 = 1);";
        scriptIf += "        countElseIf1 += 1;";
        scriptIf += "        xElseIf1 += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    elseif (x % 5 = 0);";
        scriptIf += "        countElseIf2 += 1;";
        scriptIf += "        xElseIf2 += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    else;";
        scriptIf += "        countElse += 1;";
        scriptIf += "        xElse += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    end;";
        scriptIf += "else;";
        scriptIf += "    countElse += 1;";
        scriptIf += "    xElse += x;";
        scriptIf += "end;";
        scriptIf += "if x = 10;";
        scriptIf += "    countIf += 1;";
        scriptIf += "    xIf += x;";
        scriptIf += "elseif x = 11 ;";
        scriptIf += "    countElseIf1 += 1;";
        scriptIf += "    xElseIf1 += x;";
        scriptIf += "else;";
        scriptIf += "    countElse += 1;";
        scriptIf += "    xElse += x;";
        scriptIf += "    x += 1;";
        scriptIf += "    while (x >= 13 & x <= 13);";
        scriptIf += "	     countLoop += 1;";
        scriptIf += "	     xLoop += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    end;";
        scriptIf += "    try ex;";
        scriptIf += "        countTry += 1;";
        scriptIf += "        xTry += x;";
        scriptIf += "        x += 1;";
        scriptIf += "        caju.error();";
        scriptIf += "    catch;";
        scriptIf += "        countCatch += 1;";
        scriptIf += "        xCatch += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    finally;";
        scriptIf += "        countFinally += 1;";
        scriptIf += "        xFinally += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    end;";
        scriptIf += "    if (x % 5 = 0);";
        scriptIf += "        countIf += 1;";
        scriptIf += "        xIf += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    elseif (x % 5 = 0);";
        scriptIf += "        countElseIf1 += 1;";
        scriptIf += "        xElseIf1 += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    elseif (x = 17);";
        scriptIf += "        countElseIf2 += 1;";
        scriptIf += "        xElseIf2 += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    else;";
        scriptIf += "        countElse += 1;";
        scriptIf += "        xElse += x;";
        scriptIf += "        x += 1;";
        scriptIf += "    end;";
        scriptIf += "end;";
        caju.eval(scriptIf);
        syntaxCheckIf(caju);
        syntaxCheckIfCache(caju, scriptIf);
        syntaxCheckIfCompile(caju, scriptIf);
    }
    
    @Test
    public void syntaxBasicLoop() throws CajuScriptException {
        System.out.println("syntaxBasicLoop");
        CajuScript caju = new CajuScript();
        syntaxReload(caju);
        String scriptLoop = "caju.syntax: CajuBasic;";
        scriptLoop += "loop: while (x >= 0 & x <= 10) ;";
        scriptLoop += "    countLoop += 1;";
        scriptLoop += "    xLoop += x;";
        scriptLoop += "    x += 1;";
        scriptLoop += "    while (x = 7) ;";
        scriptLoop += "	       countLoop += 1;";
        scriptLoop += "	       xLoop += x;";
        scriptLoop += "        break;";
        scriptLoop += "    end;";
        scriptLoop += "    while (x = 5) ;";
        scriptLoop += "	       if countLoop = 10 ;";
        scriptLoop += "            countBreak += 1;";
        scriptLoop += "            xBreak += x;";
        scriptLoop += "	           break;";
        scriptLoop += "	       end;";
        scriptLoop += "	       countLoop += 1;";
        scriptLoop += "	       xLoop += x;";
        scriptLoop += "        countContinue += 1;";
        scriptLoop += "        xContinue += x;";
        scriptLoop += "        x += 1;";
        scriptLoop += "        continue;";
        scriptLoop += "    end;";
        scriptLoop += "    try ex ;";
        scriptLoop += "        countTry += 1;";
        scriptLoop += "        xTry += x;";
        scriptLoop += "        caju.error();";
        scriptLoop += "    catch ;";
        scriptLoop += "        countCatch += 1;";
        scriptLoop += "        xCatch += x;";
        scriptLoop += "    finally ;";
        scriptLoop += "        countFinally += 1;";
        scriptLoop += "        xFinally += x;";
        scriptLoop += "    end;";
        scriptLoop += "    if (x % 3 = 0) ;";
        scriptLoop += "        countIf += 1;";
        scriptLoop += "        xIf += x;";
        scriptLoop += "    elseif (x % 5 = 0) ;";
        scriptLoop += "        countElseIf1 += 1;";
        scriptLoop += "        xElseIf1 += x;";
        scriptLoop += "    elseif (x % 4 = 0) ;";
        scriptLoop += "        countElseIf2 += 1;";
        scriptLoop += "        xElseIf2 += x;";
        scriptLoop += "    else;";
        scriptLoop += "        countElse += 1;";
        scriptLoop += "        xElse += x;";
        scriptLoop += "    end;";
        scriptLoop += "    if (x = 10) ;";
        scriptLoop += "        countIf += 1;";
        scriptLoop += "        xIf += x;";
        scriptLoop += "        while (true) ;";
        scriptLoop += "            countLoop += 1;";
        scriptLoop += "            xLoop += x;";
        scriptLoop += "            countBreak += 1;";
        scriptLoop += "            xBreak += x;";
        scriptLoop += "            break loop;";
        scriptLoop += "        end;";
        scriptLoop += "    else ;";
        scriptLoop += "        countIf += 1;";
        scriptLoop += "        xIf += x;";
        scriptLoop += "        while (true) ;";
        scriptLoop += "            countLoop += 1;";
        scriptLoop += "            xLoop += x;";
        scriptLoop += "            countContinue += 1;";
        scriptLoop += "            xContinue += x;";
        scriptLoop += "            continue loop;";
        scriptLoop += "        end;";
        scriptLoop += "    end;";
        scriptLoop += "end;";
        caju.eval(scriptLoop);
        syntaxCheckLoop(caju);
        syntaxCheckLoopCache(caju, scriptLoop);
        syntaxCheckLoopCompile(caju, scriptLoop);
    }
    
    @Test
    public void syntaxBasicFunction() throws CajuScriptException {
        System.out.println("syntaxBasicFunction");
        CajuScript caju = new CajuScript();
        syntaxReload(caju);
        String scriptFunc = "caju.syntax: CajuBasic;";
        scriptFunc += "function func x ;";
        scriptFunc += "    x += 1;";
        scriptFunc += "    root.x += 1;";
        scriptFunc += "    root.countFunc += 1;";
        scriptFunc += "    while (x >= 1 & x <= 1) ;";
        scriptFunc += "	       root.countLoop += 1;";
        scriptFunc += "	       root.xLoop += x;";
        scriptFunc += "        x += 1;";
        scriptFunc += "    end;";
        scriptFunc += "    try ex ;";
        scriptFunc += "        root.countTry += 1;";
        scriptFunc += "        root.xTry += x;";
        scriptFunc += "        caju.error();";
        scriptFunc += "    catch ;";
        scriptFunc += "        root.countCatch += 1;";
        scriptFunc += "        root.xCatch += x;";
        scriptFunc += "    finally ;";
        scriptFunc += "        root.countFinally += 1;";
        scriptFunc += "        root.xFinally += x;";
        scriptFunc += "    end;";
        scriptFunc += "    if x = 2 ;";
        scriptFunc += "        root.x += 1;";
        scriptFunc += "        root.countIf += 1;";
        scriptFunc += "        root.xIf += x;";
        scriptFunc += "    elseif (x % 4 = 0) ;";
        scriptFunc += "        root.x += 1;";
        scriptFunc += "        root.countElseIf1 += 1;";
        scriptFunc += "        root.xElseIf1 += x;";
        scriptFunc += "    elseif (x % 2 = 0) ;";
        scriptFunc += "        root.x += 1;";
        scriptFunc += "        root.countElseIf2 += 1;";
        scriptFunc += "        root.xElseIf2 += x;";
        scriptFunc += "    else ;";
        scriptFunc += "        root.x += 1;";
        scriptFunc += "        root.countElse += 1;";
        scriptFunc += "        root.xElse += x;";
        scriptFunc += "    end;";
        scriptFunc += "    if x < 10 ;";
        scriptFunc += "        return func(x);";
        scriptFunc += "    end;";
        scriptFunc += "    root.x += 1;";
        scriptFunc += "    return x;";
        scriptFunc += "end;";
        scriptFunc += "xFunc += func(x);";
        scriptFunc += "function func1 x, y ;";
        scriptFunc += "    root.x += 1;";
        scriptFunc += "    root.countFunc += 1;";
        scriptFunc += "    return x + y;";
        scriptFunc += "end;";
        scriptFunc += "xFunc += func1(x, 5);";
        scriptFunc += "function func2 (x, y) ;";
        scriptFunc += "    root.x += 1;";
        scriptFunc += "    root.countFunc += 1;";
        scriptFunc += "    return x + y;";
        scriptFunc += "end;";
        scriptFunc += "xFunc += func2(x, 5);";
        scriptFunc += "function func3 ;";
        scriptFunc += "    root.x += 1;";
        scriptFunc += "    root.countFunc += 1;";
        scriptFunc += "    return x + 1;";
        scriptFunc += "end;";
        scriptFunc += "xFunc += func3();";
        caju.eval(scriptFunc);
        syntaxCheckFunction(caju);
        syntaxCheckFunctionCache(caju, scriptFunc);
        syntaxCheckFunctionCompile(caju, scriptFunc);
    }
    
    @Test
    public void syntaxBasicTryCatch() throws CajuScriptException {
        System.out.println("syntaxBasicTryCatch");
        CajuScript caju = new CajuScript();
        syntaxReload(caju);
        String scriptTry = "caju.syntax: CajuBasic;";
        scriptTry += "try e ;";
        scriptTry += "    x += 1;";
        scriptTry += "    countTry += 1;";
        scriptTry += "    xTry += x;";
        scriptTry += "    while (x = 0 | x = 1) ;";
        scriptTry += "        x += 1;";
        scriptTry += "	      countLoop += 1;";
        scriptTry += "	      xLoop += x;";
        scriptTry += "    end;";
        scriptTry += "    try ex ;";
        scriptTry += "        x += 1;";
        scriptTry += "        countTry += 1;";
        scriptTry += "        xTry += x;";
        scriptTry += "        caju.error();";
        scriptTry += "    catch ;";
        scriptTry += "        x += 1;";
        scriptTry += "        countCatch += 1;";
        scriptTry += "        xCatch += x;";
        scriptTry += "    finally ;";
        scriptTry += "        x += 1;";
        scriptTry += "        countFinally += 1;";
        scriptTry += "        xFinally += x;";
        scriptTry += "    end;";
        scriptTry += "    if x = 0 | x = 5 ;";
        scriptTry += "        x += 1;";
        scriptTry += "        countIf += 1;";
        scriptTry += "        xIf += x;";
        scriptTry += "    elseif (x % 4 = 0) ;";
        scriptTry += "        x += 1;";
        scriptTry += "        countElseIf1 += 1;";
        scriptTry += "        xElseIf1 += x;";
        scriptTry += "    elseif (x % 2 = 0) ;";
        scriptTry += "        x += 1;";
        scriptTry += "        countElseIf2 += 1;";
        scriptTry += "        xElseIf2 += x;";
        scriptTry += "    else ;";
        scriptTry += "        x += 1;";
        scriptTry += "        countElse += 1;";
        scriptTry += "        xElse += x;";
        scriptTry += "    end;";
        scriptTry += "    caju.error();";
        scriptTry += "catch ;";
        scriptTry += "    x += 1;";
        scriptTry += "    countCatch += 1;";
        scriptTry += "    xCatch += x;";
        scriptTry += "    while (x = 7) ;";
        scriptTry += "        x += 1;";
        scriptTry += "	      countLoop += 1;";
        scriptTry += "	      xLoop += x;";
        scriptTry += "    end;";
        scriptTry += "    try ex ;";
        scriptTry += "        x += 1;";
        scriptTry += "        countTry += 1;";
        scriptTry += "        xTry += x;";
        scriptTry += "        caju.error();";
        scriptTry += "    catch ;";
        scriptTry += "        x += 1;";
        scriptTry += "        countCatch += 1;";
        scriptTry += "        xCatch += x;";
        scriptTry += "    finally ;";
        scriptTry += "        x += 1;";
        scriptTry += "        countFinally += 1;";
        scriptTry += "        xFinally += x;";
        scriptTry += "    end;";
        scriptTry += "    if x = 0 ;";
        scriptTry += "        x += 1;";
        scriptTry += "        countIf += 1;";
        scriptTry += "        xIf += x;";
        scriptTry += "    elseif (x = 11);";
        scriptTry += "        x += 1;";
        scriptTry += "        countElseIf1 += 1;";
        scriptTry += "        xElseIf1 += x;";
        scriptTry += "    elseif (x = 0) ;";
        scriptTry += "        x += 1;";
        scriptTry += "        countElseIf2 += 1;";
        scriptTry += "        xElseIf2 += x;";
        scriptTry += "    else ;";
        scriptTry += "        x += 1;";
        scriptTry += "        countElse += 1;";
        scriptTry += "        xElse += x;";
        scriptTry += "    end;";
        scriptTry += "finally;";
        scriptTry += "    x += 1;";
        scriptTry += "    countFinally += 1;";
        scriptTry += "    xFinally += x;";
        scriptTry += "    while (x = 13) ;";
        scriptTry += "        x += 1;";
        scriptTry += "	      countLoop += 1;";
        scriptTry += "	      xLoop += x;";
        scriptTry += "    end;";
        scriptTry += "    try ex ;";
        scriptTry += "        x += 1;";
        scriptTry += "        countTry += 1;";
        scriptTry += "        xTry += x;";
        scriptTry += "        caju.error();";
        scriptTry += "    catch ;";
        scriptTry += "        x += 1;";
        scriptTry += "        countCatch += 1;";
        scriptTry += "        xCatch += x;";
        scriptTry += "    finally ;";
        scriptTry += "        x += 1;";
        scriptTry += "        countFinally += 1;";
        scriptTry += "        xFinally += x;";
        scriptTry += "    end;";
        scriptTry += "    if x = 0 ;";
        scriptTry += "        x += 1;";
        scriptTry += "        countIf += 1;";
        scriptTry += "        xIf += x;";
        scriptTry += "    elseif x = 0 ;";
        scriptTry += "        x += 1;";
        scriptTry += "        countElseIf1 += 1;";
        scriptTry += "        xElseIf1 += x;";
        scriptTry += "    elseif x = 0 ;";
        scriptTry += "        x += 1;";
        scriptTry += "        countElseIf2 += 1;";
        scriptTry += "        xElseIf2 += x;";
        scriptTry += "    else ;";
        scriptTry += "        x += 1;";
        scriptTry += "        countElse += 1;";
        scriptTry += "        xElse += x;";
        scriptTry += "    end;";
        scriptTry += "end;";
        scriptTry += "try e ;";
        scriptTry += "    x += 1;";
        scriptTry += "    countTry += 1;";
        scriptTry += "    xTry = x;";
        scriptTry += "end;";
        scriptTry += "try e ;";
        scriptTry += "    x += 1;";
        scriptTry += "    countTry += 1;";
        scriptTry += "    xTry += x;";
        scriptTry += "    caju.error();";
        scriptTry += "catch ;";
        scriptTry += "    x += 1;";
        scriptTry += "    countCatch += 1;";
        scriptTry += "    xCatch += x;";
        scriptTry += "end;";
        scriptTry += "try e ;";
        scriptTry += "    x += 1;";
        scriptTry += "    countTry += 1;";
        scriptTry += "    xTry += x;";
        scriptTry += "    caju.error();";
        scriptTry += " finally ;";
        scriptTry += "    x += 1;";
        scriptTry += "    countFinally += 1;";
        scriptTry += "    xFinally += x;";
        scriptTry += "end;";
        caju.eval(scriptTry);
        syntaxCheckTryCatch(caju);
        syntaxCheckTryCatchCache(caju, scriptTry);
        syntaxCheckTryCatchCompile(caju, scriptTry);
    }

    private void syntaxCheckImportCache(CajuScript caju, String script) throws CajuScriptException {
        syntaxReload(caju);
        caju.eval("caju.cache: test;"+ script);
        syntaxCheckImport(caju);
        syntaxReload(caju);
        caju.eval("caju.cache: test;"+ script);
        syntaxCheckImport(caju);
        caju = new CajuScript();
        syntaxReload(caju);
        caju.eval("caju.cache: test;"+ script);
        syntaxCheckImport(caju);
    }

    private void syntaxCheckImportCompile(CajuScript caju, String script) throws CajuScriptException {
        syntaxReload(caju);
        caju.eval("caju.compile.classPath: dist/cajuscript.jar;caju.compile: test.TestImport;"+ script);
        syntaxCheckImport(caju);
        syntaxReload(caju);
        caju.eval("caju.compile.classPath: dist/cajuscript.jar;caju.compile: test.TestImport;"+ script);
        syntaxCheckImport(caju);
        caju = new CajuScript();
        syntaxReload(caju);
        caju.eval("caju.compile.classPath: dist/cajuscript.jar;caju.compile: test.TestImport;"+ script);
        syntaxCheckImport(caju);
    }
    
    private void syntaxCheckImport(CajuScript caju) throws CajuScriptException {
        if (((Integer)caju.get("x")).intValue() != 1) {
            fail("x is "+ caju.get("x") +". Need be 1!");
        }
        if (!((java.util.Properties)caju.get("p1")).getProperty("test1").equals("test")) {
            fail("p1 failed!");
        }
        if (!((java.util.Properties)caju.get("p2")).getProperty("test2").equals("test")) {
            fail("p2 failed!");
        }
        if (!((java.util.Properties)caju.get("p3")).getProperty("test3").equals("test")) {
            fail("p3 failed!");
        }
    }
    
    private void syntaxCheckIfCache(CajuScript caju, String script) throws CajuScriptException {
        syntaxReload(caju);
        caju.eval("caju.cache: test;"+ script);
        syntaxCheckIf(caju);
        syntaxReload(caju);
        caju.eval("caju.cache: test;"+ script);
        syntaxCheckIf(caju);
        caju = new CajuScript();
        syntaxReload(caju);
        caju.eval("caju.cache: test;"+ script);
        syntaxCheckIf(caju);
    }

    private void syntaxCheckIfCompile(CajuScript caju, String script) throws CajuScriptException {
        syntaxReload(caju);
        caju.eval("caju.compile.classPath: dist/cajuscript.jar;caju.compile: test.TestIf;"+ script);
        syntaxCheckIf(caju);
        syntaxReload(caju);
        caju.eval("caju.compile.classPath: dist/cajuscript.jar;caju.compile: test.TestIf;"+ script);
        syntaxCheckIf(caju);
        caju = new CajuScript();
        syntaxReload(caju);
        caju.eval("caju.compile.classPath: dist/cajuscript.jar;caju.compile: test.TestIf;"+ script);
        syntaxCheckIf(caju);
    }
    
    private void syntaxCheckIf(CajuScript caju) throws CajuScriptException {
        syntaxCheck(caju, 18, 1, 2, 1, 2, 3, 0, 0, 3, 3, 3, 0, 0, 17, 17, 17, 21, 0, 0, 24, 27, 30, 0);
    }

    private void syntaxCheckLoopCache(CajuScript caju, String script) throws CajuScriptException {
        syntaxReload(caju);
        caju.eval("caju.cache: test;"+ script);
        syntaxCheckLoop(caju);
        syntaxReload(caju);
        caju.eval("caju.cache: test;"+ script);
        syntaxCheckLoop(caju);
        caju = new CajuScript();
        syntaxReload(caju);
        caju.eval("caju.cache: test;"+ script);
        syntaxCheckLoop(caju);
    }

    private void syntaxCheckLoopCompile(CajuScript caju, String script) throws CajuScriptException {
        syntaxReload(caju);
        caju.eval("caju.compile.classPath: dist/cajuscript.jar;caju.compile: test.TestLoop;"+ script);
        syntaxCheckLoop(caju);
        syntaxReload(caju);
        caju.eval("caju.compile.classPath: dist/cajuscript.jar;caju.compile: test.TestLoop;"+ script);
        syntaxCheckLoop(caju);
        caju = new CajuScript();
        syntaxReload(caju);
        caju.eval("caju.compile.classPath: dist/cajuscript.jar;caju.compile: test.TestLoop;"+ script);
        syntaxCheckLoop(caju);
    }

    private void syntaxCheckLoop(CajuScript caju) throws CajuScriptException {
        syntaxCheck(caju, 10, 12, 1, 2, 3, 20, 1, 9, 9, 9, 9, 0, 68, 10, 12, 10, 102, 10, 45, 50, 50, 50, 0);
    }

    private void syntaxCheckFunctionCache(CajuScript caju, String script) throws CajuScriptException {
        syntaxReload(caju);
        caju.eval("caju.cache: test;"+ script);
        syntaxCheckFunction(caju);
        syntaxReload(caju);
        caju.eval("caju.cache: test;"+ script);
        syntaxCheckFunction(caju);
        caju = new CajuScript();
        syntaxReload(caju);
        caju.eval("caju.cache: test;"+ script);
        syntaxCheckFunction(caju);
    }

    private void syntaxCheckFunctionCompile(CajuScript caju, String script) throws CajuScriptException {
        syntaxReload(caju);
        caju.eval("caju.compile.classPath: dist/cajuscript.jar;caju.compile: test.TestFunction;"+ script);
        syntaxCheckFunction(caju);
        syntaxReload(caju);
        caju.eval("caju.compile.classPath: dist/cajuscript.jar;caju.compile: test.TestFunction;"+ script);
        syntaxCheckFunction(caju);
        caju = new CajuScript();
        syntaxReload(caju);
        caju.eval("caju.compile.classPath: dist/cajuscript.jar;caju.compile: test.TestFunction;"+ script);
        syntaxCheckFunction(caju);
    }
    
    private void syntaxCheckFunction(CajuScript caju) throws CajuScriptException {
        syntaxCheck(caju, 22, 1, 2, 2, 4, 1, 0, 0, 9, 9, 9, 12, 2, 12, 16, 24, 1, 0, 0, 54, 54, 54, 82);
    }
    
    private void syntaxCheckTryCatchCache(CajuScript caju, String script) throws CajuScriptException {
        syntaxReload(caju);
        caju.eval("caju.cache: test;"+ script);
        syntaxCheckTryCatch(caju);
        syntaxReload(caju);
        caju.eval("caju.cache: test;"+ script);
        syntaxCheckTryCatch(caju);
        caju = new CajuScript();
        syntaxReload(caju);
        caju.eval("caju.cache: test;"+ script);
        syntaxCheckTryCatch(caju);
    }

    private void syntaxCheckTryCatchCompile(CajuScript caju, String script) throws CajuScriptException {
        syntaxReload(caju);
        caju.eval("caju.compile.classPath: dist/cajuscript.jar;caju.compile: test.TestTryCatch;"+ script);
        syntaxCheckTryCatch(caju);
        syntaxReload(caju);
        caju.eval("caju.compile.classPath: dist/cajuscript.jar;caju.compile: test.TestTryCatch;"+ script);
        syntaxCheckTryCatch(caju);
        caju = new CajuScript();
        syntaxReload(caju);
        caju.eval("caju.compile.classPath: dist/cajuscript.jar;caju.compile: test.TestTryCatch;"+ script);
        syntaxCheckTryCatch(caju);
    }
    
    private void syntaxCheckTryCatch(CajuScript caju) throws CajuScriptException {
        syntaxCheck(caju, 23, 1, 1, 0, 1, 3, 0, 0, 7, 5, 5, 0, 6, 12, 0, 18, 24, 0, 0, 61, 58, 69, 0);
    }
    
    private void syntaxCheck(CajuScript caju, 
    int x, int countIf, int countElseIf1, int countElseIf2, int countElse, 
    int countLoop, int countBreak, int countContinue, int countTry, int countCatch,
    int countFinally, int countFunc, int xIf, int xElseIf1, int xElseIf2, int xElse,
    int xLoop, int xBreak, int xContinue, int xTry, int xCatch, int xFinally, int xFunc
    ) throws CajuScriptException {
        if (((Integer)caju.get("x")).intValue() != x) {
            syntaxPrintAllVars(caju);
            fail("x is "+ caju.get("x") +". Need be "+ x +"!");
        }
        if (((Integer)caju.get("countIf")).intValue() != countIf) {
            syntaxPrintAllVars(caju);
            fail("countIf is "+ caju.get("countIf") +". Need be "+ countIf +"!");
        }
        if (((Integer)caju.get("countElseIf1")).intValue() != countElseIf1) {
            syntaxPrintAllVars(caju);
            fail("countElseIf1 is "+ caju.get("countElseIf1") +". Need be "+ countElseIf1 +"!");
        }
        if (((Integer)caju.get("countElseIf2")).intValue() != countElseIf2) {
            syntaxPrintAllVars(caju);
            fail("countElseIf2 is "+ caju.get("countElseIf2") +". Need be "+ countElseIf2 +"!");
        }
        if (((Integer)caju.get("countElse")).intValue() != countElse) {
            syntaxPrintAllVars(caju);
            fail("countElse is "+ caju.get("countElse") +". Need be "+ countElse +"!");
        }
        if (((Integer)caju.get("countLoop")).intValue() != countLoop) {
            syntaxPrintAllVars(caju);
            fail("countLoop is "+ caju.get("countLoop") +". Need be "+ countLoop +"!");
        }
        if (((Integer)caju.get("countBreak")).intValue() != countBreak) {
            syntaxPrintAllVars(caju);
            fail("countBreak is "+ caju.get("countBreak") +". Need be "+ countBreak +"!");
        }
        if (((Integer)caju.get("countContinue")).intValue() != countContinue) {
            syntaxPrintAllVars(caju);
            fail("countContinue is "+ caju.get("countContinue") +". Need be "+ countContinue +"!");
        }
        if (((Integer)caju.get("countFunc")).intValue() != countFunc) {
            syntaxPrintAllVars(caju);
            fail("countFunc is "+ caju.get("countFunc") +". Need be "+ countFunc +"!");
        }
        if (((Integer)caju.get("countTry")).intValue() != countTry) {
            syntaxPrintAllVars(caju);
            fail("countTry is "+ caju.get("countTry") +". Need be "+ countTry +"!");
        }
        if (((Integer)caju.get("countCatch")).intValue() != countCatch) {
            syntaxPrintAllVars(caju);
            fail("countCatch is "+ caju.get("countCatch") +". Need be "+ countCatch +"!");
        }
        if (((Integer)caju.get("countFinally")).intValue() != countFinally) {
            syntaxPrintAllVars(caju);
            fail("countFinally is "+ caju.get("countFinally") +". Need be "+ countFinally +"!");
        }
        if (((Integer)caju.get("xIf")).intValue() != xIf) {
            syntaxPrintAllVars(caju);
            fail("xIf is "+ caju.get("xIf") +". Need be "+ xIf +"!");
        }
        if (((Integer)caju.get("xElseIf1")).intValue() != xElseIf1) {
            syntaxPrintAllVars(caju);
            fail("xElseIf1 is "+ caju.get("xElseIf1") +". Need be "+ xElseIf1 +"!");
        }
        if (((Integer)caju.get("xElseIf2")).intValue() != xElseIf2) {
            syntaxPrintAllVars(caju);
            fail("xElseIf2 is "+ caju.get("xElseIf2") +". Need be "+ xElseIf2 +"!");
        }
        if (((Integer)caju.get("xElse")).intValue() != xElse) {
            syntaxPrintAllVars(caju);
            fail("xElse is "+ caju.get("xElse") +". Need be "+ xElse +"!");
        }
        if (((Integer)caju.get("xLoop")).intValue() != xLoop) {
            syntaxPrintAllVars(caju);
            fail("xLoop is "+ caju.get("xLoop") +". Need be "+ xLoop +"!");
        }
        if (((Integer)caju.get("xBreak")).intValue() != xBreak) {
            syntaxPrintAllVars(caju);
            fail("xBreak is "+ caju.get("xBreak") +". Need be "+ xBreak +" Break!");
        }
        if (((Integer)caju.get("xContinue")).intValue() != xContinue) {
            syntaxPrintAllVars(caju);
            fail("xContinue is "+ caju.get("xContinue") +". Need be "+ xContinue +"!");
        }
        if (((Integer)caju.get("xFunc")).intValue() != xFunc) {
            syntaxPrintAllVars(caju);
            fail("xFunc is "+ caju.get("xFunc") +". Need be "+ xFunc +"!");
        }
        if (((Integer)caju.get("xTry")).intValue() != xTry) {
            syntaxPrintAllVars(caju);
            fail("xTry is "+ caju.get("xTry") +". Need be "+ xTry +"!");
        }
        if (((Integer)caju.get("xCatch")).intValue() != xCatch) {
            syntaxPrintAllVars(caju);
            fail("xCatch is "+ caju.get("xCatch") +". Need be "+ xCatch +"!");
        }
        if (((Integer)caju.get("xFinally")).intValue() != xFinally) {
            syntaxPrintAllVars(caju);
            fail("xFinally is "+ caju.get("xFinally") +". Need be "+ xFinally +"!");
        }
    }

    private void syntaxPrintAllVars(CajuScript caju) throws CajuScriptException {
        System.out.println("x is "+ caju.get("x") +".");
        System.out.println("countIf is "+ caju.get("countIf") +".");
        System.out.println("countElseIf1 is "+ caju.get("countElseIf1") +".");
        System.out.println("countElseIf2 is "+ caju.get("countElseIf2") +".");
        System.out.println("countElse is "+ caju.get("countElse") +".");
        System.out.println("countLoop is "+ caju.get("countLoop") +".");
        System.out.println("countBreak is "+ caju.get("countBreak") +".");
        System.out.println("countContinue is "+ caju.get("countContinue") +".");
        System.out.println("countFunc is "+ caju.get("countFunc") +".");
        System.out.println("countTry is "+ caju.get("countTry") +".");
        System.out.println("countCatch is "+ caju.get("countCatch") +".");
        System.out.println("countFinally is "+ caju.get("countFinally") +".");
        System.out.println("xIf is "+ caju.get("xIf") +".");
        System.out.println("xElseIf1 is "+ caju.get("xElseIf1") +".");
        System.out.println("xElseIf2 is "+ caju.get("xElseIf2") +".");
        System.out.println("xElse is "+ caju.get("xElse") +".");
        System.out.println("xLoop is "+ caju.get("xLoop") +".");
        System.out.println("xBreak is "+ caju.get("xBreak") +".");
        System.out.println("xContinue is "+ caju.get("xContinue") +".");
        System.out.println("xFunc is "+ caju.get("xFunc") +".");
        System.out.println("xTry is "+ caju.get("xTry") +".");
        System.out.println("xCatch is "+ caju.get("xCatch") +".");
        System.out.println("xFinally is "+ caju.get("xFinally") +".");
    }

    /**
     * Test of getSyntax method, of class CajuScript.
     */
    @Test
    public void syntax() throws CajuScriptException {
        System.out.println("syntax");
        CajuScript caju = new CajuScript();
        caju.setSyntax(CajuScript.getGlobalSyntax("CajuBasic"));
        assertSame(CajuScript.getGlobalSyntax("CajuBasic"), caju.getSyntax());
        Syntax syntax = new Syntax();
        caju.addSyntax("CajuTest", syntax);
        assertSame(syntax, caju.getSyntax("CajuTest"));
    }

    /**
     * Test of context, of class CajuScript.
     */
    @Test
    public void context() throws CajuScriptException {
        System.out.println("context");
        CajuScript caju = new CajuScript();
        Context context = new Context();
        caju.setContext(context);
        assertSame(context, caju.getContext());
    }

    /**
     * Test of eval method, of class CajuScript.
     */
    @Test
    public void evalMath() throws Exception {
        System.out.println("evalMath");
        String script = "x = 22 * (((78.3 + 87.6) % 1.006) + (3.8 * 3 / 6.4 - (65.8 - 34 % 4.8765)));";
        CajuScript caju = new CajuScript();
        caju.eval(script);
        if (((Float)caju.get("x")).floatValue() != -1283.959f) {
            fail("x is "+ caju.get("x") +". Need be -1283.959!");
        }
        caju.eval("caju.cache: test;"+ script);
        if (((Float)caju.get("x")).floatValue() != -1283.959f) {
            fail("x is "+ caju.get("x") +". Need be -1283.959!");
        }
        caju = new CajuScript();
        caju.eval("caju.cache: test;"+ script);
        if (((Float)caju.get("x")).floatValue() != -1283.959f) {
            fail("x is "+ caju.get("x") +". Need be -1283.959!");
        }
    }
    
    
    /**
     * Test of eval method, of class CajuScript.
     */
    @Test
    public void evalNative() throws Exception {
        System.out.println("evalNative");
        String script = "";
        script += "alist = $;";
        script += "alist = java.util.ArrayList();";
        script += "alist.add('test');";
        script += "test1 = alist.iterator().next().substring(0, 2);";
        script += "test2 = array.get(alist.toArray(), 0);";
        CajuScript caju = new CajuScript();
        caju.eval(script);
        if (!((String)caju.get("test1")).equals("te")) {
            fail("test1 is "+ caju.get("test1") +". Need be 'te'!");
        }
        if (!((String)caju.get("test2")).equals("test")) {
            fail("test2 is "+ caju.get("test2") +". Need be 'test'!");
        }
        caju.eval("caju.cache: test;"+ script);
        if (!((String)caju.get("test1")).equals("te")) {
            fail("test1 is "+ caju.get("test1") +". Need be 'te'!");
        }
        if (!((String)caju.get("test2")).equals("test")) {
            fail("test2 is "+ caju.get("test2") +". Need be 'test'!");
        }
        caju = new CajuScript();
        caju.eval("caju.cache: test;"+ script);
        if (!((String)caju.get("test1")).equals("te")) {
            fail("test1 is "+ caju.get("test1") +". Need be 'te'!");
        }
        if (!((String)caju.get("test2")).equals("test")) {
            fail("test2 is "+ caju.get("test2") +". Need be 'test'!");
        }
    }

    /**
     * Test of evalFile method, of class CajuScript.
     */
    @Test
    public void evalFile() throws Exception {
        System.out.println("evalFile");
        CajuScript instance = new CajuScript();
        Value result = instance.evalFile("samples/mainReturn/main_return.cj");
        if (result.getNumberIntegerValue() != 1) {
            fail("Eval file was failed.");
        }
    }

    /**
     * Test of func method, of class CajuScript.
     */
    @Test
    public void func() throws CajuScriptException {
        System.out.println("func");
        String key = "func";
        CajuScript caju = new CajuScript();
        Function func = new Function(null);
        caju.setFunc(key, func);
        assertSame(caju.getFunc(key), func);
    }

    /**
     * Test of var method, of class CajuScript.
     */
    @Test
    public void var() throws CajuScriptException {
        System.out.println("var");
        String key = "var";
        CajuScript caju = new CajuScript();
        Value var = new Value(caju, caju.getContext(), caju.getSyntax());
        caju.setVar(key, var);
        assertSame(caju.getVar(key), var);
    }

    /**
     * Test of getAllKeys method, of class CajuScript.
     */
    @Test
    public void getAllKeys() throws CajuScriptException {
        System.out.println("getAllKeys");
        CajuScript instance = new CajuScript();
        instance.set("x", "test");
        instance.set("y", "test");
        Set<String> result = instance.getAllKeys();
        int count = 0;
        for (String key : result) {
            if (key.equals("x")) {
                assertEquals(instance.get("x"), "test");
                count++;
            } else if (key.equals("y")) {
                assertEquals(instance.get("y"), "test");
                count++;
            } else if (key.equals("caju")) {
                assertSame(instance.get("caju"), instance);
                count++;
            } else if (key.equals("array")) {
                assertSame(instance.get("array"), instance.getVar("array").getValue());
                count++;
            } else {
                fail("Invalid key: ".concat(key));
            }
        }
        if (count != 4) {
            fail("Some key failed.");
        }
    }
    
    /**
     * Test of toValue method, of class CajuScript.
     */
    @Test
    public void toValue() throws Exception {
        System.out.println("toValue");
        Object obj = "test";
        CajuScript instance = new CajuScript();
        Value result = instance.toValue(obj);
        instance.setVar("x", result);
        assertSame(obj, instance.get("x"));
    }

    /**
     * Test of set and value method, of class CajuScript.
     */
    @Test
    public void objectSetAndGet() throws Exception {
        System.out.println("set");
        String key = "var";
        Object value = "test";
        CajuScript instance = new CajuScript();
        instance.set(key, value);
        assertSame(instance.get(key), value);
    }

    /**
     * Test of imports method, of class CajuScript.
     */
    @Test
    public void imports() throws CajuScriptException {
        System.out.println("imports");
        CajuScript instance = new CajuScript();
        instance.addImport("java.util");
        int count = 0;
        for (String i : instance.getImports()) {
            if (i.equals("java.util")) {
                count++;
            } else if (i.equals("java.lang")) {
                count++;
            } else {
                fail("Invalid import: ".concat(i));
            }
        }
        if (count != 2) {
            fail("Some import failed.");
        }
        instance.removeImport("java.util");
        count = 0;
        for (String i : instance.getImports()) {
            if (i.equals("java.lang")) {
                count++;
            } else {
                fail("Invalid import: ".concat(i));
            }
        }
        if (count != 1) {
            fail("Some import failed.");
        }
    }

    /**
     * Test of cast method, of class CajuScript.
     */
    @Test
    public void cast() throws Exception {
        System.out.println("cast");
        CajuScript instance = new CajuScript();
        assertEquals(new Integer(1), instance.cast(new Double(1), "i"));
        assertEquals(new Integer(1), instance.cast(new Long(1), "java.lang.Integer"));
        assertEquals(new Integer(1), instance.cast(new Float(1), "int"));
        assertEquals(new Long(1), instance.cast(new Double(1), "l"));
        assertEquals(new Long(1), instance.cast(new Integer(1), "java.lang.Long"));
        assertEquals(new Long(1), instance.cast(new Float(1), "long"));
        assertEquals(new Float(1), instance.cast(new Double(1), "f"));
        assertEquals(new Float(1), instance.cast(new Integer(1), "java.lang.Float"));
        assertEquals(new Float(1), instance.cast(new Long(1), "float"));
        assertEquals(new Double(1), instance.cast(new Float(1), "d"));
        assertEquals(new Double(1), instance.cast(new Integer(1), "java.lang.Double"));
        assertEquals(new Double(1), instance.cast(new Long(1), "double"));
        assertEquals(new Character('A'), instance.cast(new Integer((int)'A'), "c"));
        assertEquals(new Character('A'), instance.cast(new Double((int)'A'), "java.lang.Character"));
        assertEquals(new Character('A'), instance.cast(new Long((int)'A'), "char"));
        assertEquals(new Character('A'), instance.cast(new Float((int)'A'), "c"));
        assertEquals(true, instance.cast(new Integer(1), "b"));
        assertEquals(true, instance.cast(new Double(1.1), "java.lang.Boolean"));
        assertEquals(true, instance.cast(new Long(1), "boolean"));
        assertEquals(true, instance.cast(new Float(1.3), "bool"));
        assertEquals(new Byte((byte)1), instance.cast(new Double(1), "bt"));
        assertEquals(new Byte((byte)1), instance.cast(new Integer(1), "java.lang.Byte"));
        assertEquals(new Byte((byte)1), instance.cast(new Long(1), "byte"));
        assertEquals(new Byte((byte)1), instance.cast(new Float(1), "bt"));
        assertEquals("1.0", instance.cast(new Double(1), "s"));
        assertEquals("1", instance.cast(new Integer(1), "java.lang.String"));
        assertEquals("1", instance.cast(new Long(1), "str"));
        assertEquals("1.0", instance.cast(new Float(1), "string"));
        assertEquals((CharSequence)"test", instance.cast("test", "java.lang.CharSequence"));
    }

    /**
     * Test of error method, of class CajuScript.
     */
    @Test
    public void error() throws Exception {
        System.out.println("error");
        int count = 0;
        try {
            CajuScript.error();
            fail("Exception wasn't generated.");
        } catch (Exception e) {
            count++;
        }
        String message = "test";
        try {
            CajuScript.error(message);
            fail("Exception wasn't generated.");
        } catch (Exception e) {
            if (!e.getMessage().equals(message)) {
                fail("Exception message isn't valid.");
            }
            count++;
        }
        if (count != 2) {
            fail("Some exception failed.");
        }
    }

    /**
     * Test of isSameType method, of class CajuScript.
     */
    @Test
    public void isSameType() {
        System.out.println("isSameType");
        if (!CajuScript.isSameType(Byte.class, Byte.class)) {
            fail("Class failed.");
        }
        if (!CajuScript.isSameType(new String(), (CharSequence)new String())) {
            fail("Object failed.");
        }
        if (CajuScript.isSameType(new String(), new Exception())) {
            fail("Not equal failed.");
        }
    }

    /**
     * Test of isPrimitiveType method, of class CajuScript.
     */
    @Test
    public void isPrimitiveType() {
        System.out.println("isPrimitiveType");
        if (!CajuScript.isPrimitiveType(Integer.class)) {
            fail("Integer failed.");
        }
        if (!CajuScript.isPrimitiveType(Long.class)) {
            fail("Long failed.");
        }
        if (!CajuScript.isPrimitiveType(Double.class)) {
            fail("Double failed.");
        }
        if (!CajuScript.isPrimitiveType(Float.class)) {
            fail("Float failed.");
        }
        if (!CajuScript.isPrimitiveType(Character.class)) {
            fail("Character failed.");
        }
        if (!CajuScript.isPrimitiveType(Byte.class)) {
            fail("Byte failed.");
        }
        if (!CajuScript.isPrimitiveType(Boolean.class)) {
            fail("Boolean failed.");
        }
        if (CajuScript.isPrimitiveType(Object.class)) {
            fail("Object failed.");
        }
    }

    /**
     * Test of array.
     */
    @Test
    public void each() throws CajuScriptException {
        System.out.println("array");
        CajuScript caju = new CajuScript();
        String script = "";
        script += "a = array.create('i', 5);";
        script += "array.set(a, 0, 10);";
        script += "array.set(a, 1, 20);";
        script += "array.set(a, 2, 30);";
        script += "array.set(a, 3, 40);";
        script += "array.set(a, 4, 50);";
        script += "array_indexes = 0;";
        script += "array_values = 0;";
        script += "caju.each('_array', a) @";
        script += "  array_indexes += caju.index('_array');";
        script += "  array_values += _array;";
        script += "@;";
        script += "array_indexes += caju.index('_array');";
        script += "list = java.util.ArrayList();";
        script += "list.add('10');";
        script += "list.add('20');";
        script += "list.add('30');";
        script += "list.add('40');";
        script += "list.add('50');";
        script += "collection_indexes = 0;";
        script += "collection_values = 0;";
        script += "caju.each('_collection', list) @";
        script += "  collection_indexes += caju.index('_collection');";
        script += "  collection_values += caju.cast(_collection, 'i');";
        script += "@;";
        script += "collection_indexes += caju.index('_collection');";
        script += "map = java.util.HashMap();";
        script += "map.put('100', '10');";
        script += "map.put('200', '20');";
        script += "map.put('300', '30');";
        script += "map.put('400', '40');";
        script += "map.put('500', '50');";
        script += "map_indexes = 0;";
        script += "map_keys = 0;";
        script += "map_values = 0;";
        script += "caju.each('_map', map) @";
        script += "  map_indexes += caju.index('_map');";
        script += "  map_keys += caju.cast(caju.key('_map'), 'i');";
        script += "  map_values += caju.cast(_map, 'i');";
        script += "@;";
        script += "map_indexes += caju.index('_map');";
        script += "prop = java.util.Properties();";
        script += "prop.put('1', '10');";
        script += "prop.put('2', '20');";
        script += "prop.put('3', '30');";
        script += "prop.put('4', '40');";
        script += "prop.put('5', '50');";
        script += "enumeration_indexes = 0;";
        script += "enumeration_values = 0;";
        script += "caju.each('_enumeration', prop.elements()) @";
        script += "  enumeration_indexes += caju.index('_enumeration');";
        script += "  enumeration_values += caju.cast(_enumeration, 'i');";
        script += "@;";
        script += "enumeration_indexes += caju.index('_enumeration');";
        caju.eval(script);
        if (((Integer)caju.get("array_indexes")).intValue() != 14) {
            fail("array_indexes is "+ caju.get("array_indexes") +". Need be 14!");
        }
        if (((Integer)caju.get("array_values")).intValue() != 150) {
            fail("array_values is "+ caju.get("array_values") +". Need be 150!");
        }
        if (((Integer)caju.get("collection_indexes")).intValue() != 14) {
            fail("collection_indexes is "+ caju.get("collection_indexes") +". Need be 14!");
        }
        if (((Integer)caju.get("collection_values")).intValue() != 150) {
            fail("collection_values is "+ caju.get("collection_values") +". Need be 150!");
        }
        if (((Integer)caju.get("map_indexes")).intValue() != 14) {
            fail("map_indexes is "+ caju.get("map_indexes") +". Need be 14!");
        }
        if (((Integer)caju.get("map_keys")).intValue() != 1500) {
            fail("map_keys is "+ caju.get("map_keys") +". Need be 1500!");
        }
        if (((Integer)caju.get("map_values")).intValue() != 150) {
            fail("map_values is "+ caju.get("map_values") +". Need be 150!");
        }
        if (((Integer)caju.get("enumeration_indexes")).intValue() != 14) {
            fail("enumeration_indexes is "+ caju.get("enumeration_indexes") +". Need be 14!");
        }
        if (((Integer)caju.get("enumeration_values")).intValue() != 150) {
            fail("enumeration_values is "+ caju.get("enumeration_values") +". Need be 150!");
        }
    }

    /**
     * Test of isInstance method, of class CajuScript.
     */
    @Test
    public void isInstance() throws CajuScriptException {
        System.out.println("isInstance");
        CajuScript instance = new CajuScript();
        if (!instance.isInstance("test", String.class)) {
            fail("Returned false when should return true.");
        }
        if (instance.isInstance("test", Integer.class)) {
            fail("Returned true when should return false.");
        }
    }

    public enum Enum {
        TEST1,
        TEST2,
    }

    public Enum testEnum(Enum e) {
        if (e == Enum.TEST1) {
            return Enum.TEST1;
        } else if (e == Enum.TEST2) {
            return Enum.TEST2;
        }
        fail("Invalid enumeration.");
        throw new Error("Invalid enumeration.");
    }

    /**
     * Test of enumerations.
     */
    @Test
    public void enums() throws CajuScriptException {
        System.out.println("enums");
        CajuScript caju = new CajuScript();
        caju.set("tester", this);
        caju.set("enum1", Enum.TEST1);
        caju.set("enum2", Enum.TEST2);
        String script = "";
        script += "enum1s = 0;";
        script += "enum2s = 0;";
        script += "enum1 = org.cajuscript.FullTest.Enum.TEST1 ?;";
        script += "  enum1s += 2;";
        script += "?;";
        script += "enum2 = org.cajuscript.FullTest.Enum.TEST2 ?;";
        script += "  enum2s += 3;";
        script += "?;";
        script += "tester.testEnum(org.cajuscript.FullTest.Enum.TEST1) ! org.cajuscript.FullTest.Enum.TEST2 ?;";
        script += "  enum1s += 2;";
        script += "?;";
        script += "tester.testEnum(org.cajuscript.FullTest.Enum.TEST2) ! org.cajuscript.FullTest.Enum.TEST1 ?;";
        script += "  enum2s += 3;";
        script += "?;";
        script += "i = 0;";
        script += "i < 10 @;";
        script += "  enum = $;";
        script += "  i % 2 = 0 ?;";
        script += "    enum1s += 1;";
        script += "  ??;";
        script += "    enum2s += 1;";
        script += "  ?;";
        script += "  i += 1;";
        script += "@;";
        caju.eval(script);
        enumsCheck(caju);
        caju = new CajuScript();
        caju.set("tester", this);
        caju.set("enum1", Enum.TEST1);
        caju.set("enum2", Enum.TEST2);
        caju.eval("caju.cache: Test;" + script);
        enumsCheck(caju);
        caju = new CajuScript();
        caju.set("tester", this);
        caju.set("enum1", Enum.TEST1);
        caju.set("enum2", Enum.TEST2);
        caju.eval("caju.compile: Test;" + script);
        enumsCheck(caju);
    }

    private void enumsCheck(CajuScript caju) throws CajuScriptException {
        if (((Integer)caju.get("enum1s")).intValue() != 9) {
            fail("enum1s is "+ caju.get("enum1s") +". Need be 9!");
        }
        if (((Integer)caju.get("enum2s")).intValue() != 11) {
            fail("enum2s is "+ caju.get("enum2s") +". Need be 11!");
        }
    }
}