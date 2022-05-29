package org.cajuscript;

import org.junit.Test;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.fail;

public class BuilderTest {

    public enum EnumTest {
        ENUM1,
        ENUM2
    }

    public static class BeanTest {
        private String name = "";
        private String country = "";
        private double total = 0;

        public List<BeanTest> list = new ArrayList();

        public BeanTest() {

        }

        public String getName() {
            return name;
        }

        public BeanTest setName(String name) {
            this.name = name;
            System.out.println("setName"+ name);
            return this;
        }

        public String getCountry() {
            return country;
        }

        public BeanTest setCountry(String country) {
            this.country = country;
            System.out.println("setCountry"+ country);
            return this;
        }

        public double getTotal() {
            return total;
        }

        public BeanTest setParams(int x) {
            System.out.println("setParams.x"+ x);
            throw new Error("BAD setParams");
        }

        public BeanTest setParams(int x, String... name) {
            this.name = name[0];
            System.out.println("setName"+ name[0]);
            return this;
        }

        /*public BeanTest setTotal(int total) {
            this.total = total;
            return this;
        }

        public BeanTest setTotal(float total) {
            this.total = total;
            return this;
        }

        public BeanTest setTotal(double total) {
            this.total = total;
            return this;
        }

        public BeanTest setTotal(String total) {
            this.total = Double.parseDouble(total.toString());
            return this;
        }*/

        public BeanTest setTotal(Object total) {
            this.total = Double.parseDouble(total.toString());
            return this;
        }

        public BeanTest setEnum(EnumTest enumTest) {
            System.out.println("setEnum"+ enumTest.toString());
            return this;
        }

        public BeanTest setCollection(Collection collection) {
            System.out.println("setCollection"+ collection.toString() + ": "+ collection.size());
            return this;
        }

        public BeanTest print() {
            System.out.println("{name: "+ getName() + ", country: "+ getCountry() + ", total: "+ getTotal() +"}");
            return this;
        }

        public BeanTest add(BeanTest beanTest) {
            beanTest.print();
            list.add(beanTest);
            return this;
        }

        public BeanTest init() {
            System.out.println("INIT"+ this.toString());
            BeanTest newInstance = new BeanTest();
            System.out.println("newInstance"+ newInstance.toString());
            return newInstance;
        }
    }

    @Test
    public void test() throws CajuScriptException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        new BeanTest();
        BeanTest.class.getDeclaredConstructor().newInstance();
        CajuScript cajuScript = new CajuScript();
        System.out.println(new File(".").getAbsolutePath());
        cajuScript.eval("$java.util;x = 1;p3 = $;" +
                "a #" +
                "    \\x = 2;" +
                "    \\p3 = Properties();" +
                "    \\p3.setProperty('test3', 'test');"+
                "#" +
                "a();");
        if (((Integer)cajuScript.get("x")).intValue() != 2) {
            fail("x is "+ cajuScript.get("x") +" and not 2");
        }
        cajuScript.eval(
                //"caju.compile: my.package.MyClass\n\n" +
                //"org.cajuscript.BuilderTest.EnumTest.ENUM1.toString();\n"+
                        "a = org.cajuscript.BuilderTest.BeanTest()\n"+
                        ".setName('aaa')\n"+
                        ".setCountry('bbb')\n"+
                        ".setEnum(org.cajuscript.BuilderTest.EnumTest.ENUM1)\n"+
                        ".setEnum(org.cajuscript.BuilderTest.EnumTest.ENUM2)\n"+
                        "a.setParams(1.4, 'mosca' \n+ 'b' \n+\n 'a','zzz')\n"+
                        "\n"+
                        "a.add(\n"+
                        "  a.init()\n"+
                                "  .setName('capivara')\n"+
                                "  .setCountry('123')\n"+
                                "  .setTotal(89.6)\n"+
                        ").add(\n"+
                        "  a.init()\n"+
                        "  .setName('sucuri')\n"+
                                "  .setCountry('4332')\n"+
                                "  .setTotal(29.6)\n"+
                        ")\n"+
                        "\n"+
                        "java.lang.System.out.println('Loop')\n"+
                        "\n"+
                        "caju.each('myItem', a.list) @\n"+
                        "  myItem.print()\n"+
                        "@\n"
        );
    }
}
