$"read_all.cj"
$"read_bytes.cj"

System.out.println()
System.out.println("===================")
System.out.println("READ ALL - file.txt")
System.out.println("===================")
time = System.currentTimeMillis()
System.out.println(readAll("sample.txt"))
System.out.println(">>> Time reading: "+ (System.currentTimeMillis() - time) + " ms")

System.out.println("====================")
System.out.println("READ BYTE - file.txt")
System.out.println("====================")
time = System.currentTimeMillis()
System.out.println(readBytes("sample.txt"))
System.out.println(">>> Time reading: "+ (System.currentTimeMillis() - time) + " ms")
