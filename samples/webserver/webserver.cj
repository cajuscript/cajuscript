$java.io
$java.net
$java.util
$java.lang

serverPort = caju.cast(array.get(args, 1), "i")

serverSocket = ServerSocket(serverPort)

System.out.println()
System.out.println("Webserver Started: localhost:"+ serverSocket.getLocalPort())

true @
	socket = serverSocket.accept()
	System.out.println()
	System.out.println()
	in = DataInputStream(socket.getInputStream())
	out = PrintStream(socket.getOutputStream())
	connected = 1
	connected = 1 @
		line = in.readLine().trim()
		line = "" ?
			connected = 0
		??
			System.out.println(line)
		?
	@
	out.println("HTTP/1.0 200 OK")
	out.println("Content-type: text/plain\n\n")
	out.println("WebServer Power By CajuScript")
	out.close()
	in.close()
	socket.close()
@