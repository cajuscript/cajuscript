public class CajuScript035ExecutionModes {
    public static void main(String[] args) {
        try {
            System.out.println("================================");
            System.out.println("CajuScript 0.3.5 Execution Modes");
            System.out.println("================================");
            runTester(100000, "1");
        } catch (Exception e) {
            throw new Error(e);
        }
    }
    public static void runTester(long times, String value) throws Exception {
        long time = 0;
        // Load classes
        org.cajuscript.CajuScript caju = new org.cajuscript.CajuScript();
        caju.eval("x = 0; x < "+ times +" @ x = x + "+ value +"; @");
        // Caju
        time = System.currentTimeMillis();
        caju = new org.cajuscript.CajuScript();
        caju.eval("x = 0; x < "+ times +" @ x = x + "+ value +"; @");
        System.out.println("Caju: "+ (System.currentTimeMillis() - time) + "ms - "+ caju.get("x"));
        // Caju - Cache 1
        time = System.currentTimeMillis();
        caju = new org.cajuscript.CajuScript();
        caju.eval("caju.cache: test; x = 0; x < "+ times +" @ x = x + "+ value +"; @");
        System.out.println("Caju - Cache First: "+ (System.currentTimeMillis() - time) + "ms - "+ caju.get("x"));
        // Caju - Cache 2
        time = System.currentTimeMillis();
        caju = new org.cajuscript.CajuScript();
        caju.eval("caju.cache: test; x = 0; x < "+ times +" @ x = x + "+ value +"; @");
        System.out.println("Caju - Cache Next: "+ (System.currentTimeMillis() - time) + "ms - "+ caju.get("x"));
        // Caju - Compile 1
        time = System.currentTimeMillis();
        caju = new org.cajuscript.CajuScript();
        caju.eval("caju.compile: Test; x = 0; x < "+ times +" @ x = x + "+ value +"; @");
        System.out.println("Caju - Compile First: "+ (System.currentTimeMillis() - time) + "ms - "+ caju.get("x"));
        // Caju - Compile 2
        time = System.currentTimeMillis();
        caju = new org.cajuscript.CajuScript();
        caju.eval("caju.compile: Test; x = 0; x < "+ times +" @ x = x + "+ value +"; @");
        System.out.println("Caju - Compile Next: "+ (System.currentTimeMillis() - time) + "ms - "+ caju.get("x"));
    }
}
