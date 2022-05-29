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
