$java.lang
$java.io

readAll(filename) #
	fileinput = FileInputStream(filename)
	bytes = array.create("byte", fileinput.available())
	fileinput.read(bytes)
	~ String(bytes)
#