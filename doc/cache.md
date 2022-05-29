# Cache

From version 0.3 CajuScript Parser uses regular expression. This method is very flexible but have a high consumed of cpu.

For increase the performance is possible do cached of parsers, very good for using with scripts executed many times.

Use cache like that:

```
caju.cache: YOUR_CACHE_ID; YOUR_SCRIPT_HERE
```

Sample of a script using cache:

```
caju.cache: SwingTester

$java.lang
$javax.swing
$java.awt

frame = JFrame()
frame.setTitle("CajuScript")
frame.setVisible(true)
frame.setSize(100, 100)
frame.getContentPane().setLayout(BorderLayout())

textArea = JTextArea()
textArea.setText("Hello World!")
frame.add(textArea, BorderLayout.CENTER)

button = JButton("Ok!")
frame.add(button, BorderLayout.SOUTH)

frame.pack()

frame.setSize(300, 300)
```

The parser is saved in a static array and is shared by all instance of CajuScript.

If the script is changed the parser saved in cache is reloaded.