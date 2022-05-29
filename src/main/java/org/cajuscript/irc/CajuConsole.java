package org.cajuscript.irc;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.cajuscript.CajuScript;
import org.cajuscript.Value;

public class CajuConsole extends JFrame implements ActionListener {
    private JTextArea output, script;
    private JScrollPane outputField, scriptField;
    private JButton run, clean, cleanOutput;

    public CajuConsole() {
        super("CajuConsole");
        System.setOut(outputPrintStream);
        setResizable(false);
        setLayout(new BorderLayout(10, 10));
        outputField = new JScrollPane();
        scriptField = new JScrollPane();
        scriptField.setViewportView(script = new JTextArea(20, 20));
        outputField.setViewportView(output = new JTextArea(15, 80));
        output.setLineWrap(true);
        output.setEditable(false);
        JPanel outputArea = new JPanel(new BorderLayout());
        outputArea.add(new JLabel("Output:"), BorderLayout.NORTH);
        outputArea.add(outputField, BorderLayout.SOUTH);
        add(outputArea, BorderLayout.SOUTH);
        JPanel scriptArea = new JPanel(new BorderLayout());
        scriptArea.add(new JLabel("Script:"), BorderLayout.NORTH);
        scriptArea.add(scriptField, BorderLayout.SOUTH);
        add(outputArea, BorderLayout.SOUTH);
        JPanel buttons = new JPanel(new GridLayout(2, 1, 10, 10));
        buttons.add(clean = new JButton("Clean Script"));
        buttons.add(cleanOutput = new JButton("Clean Output"));
        JPanel buttonsMain = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonsMain.add(run = new JButton("Run"));
        buttonsMain.add(buttons);
        JPanel header = new JPanel(new BorderLayout(10, 10));
        header.add(scriptArea, BorderLayout.CENTER);
        header.add(buttonsMain, BorderLayout.SOUTH);
        add(header, BorderLayout.CENTER);
        run.addActionListener(this);
        clean.addActionListener(this);
        cleanOutput.addActionListener(this);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new CajuConsole();
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource() == run) {
            long ini = System.currentTimeMillis();
            try {
                CajuScript caju = new CajuScript();
                Value v = caju.eval(script.getText());
                if (v != null) {
                    if (v.getValue() != null) {
                        System.out.println(v.getValue().toString());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace(outputPrintStream);
            }
            long fin = System.currentTimeMillis();
            System.out.println("---");
            System.out.println("Execution time: " + (fin - ini) + " ms");
            System.out.println("");
        }
        if (arg0.getSource() == clean) {
            script.setText("");
        }
        if (arg0.getSource() == cleanOutput) {
            output.setText("");
        }
    }
    
    private PrintStream outputPrintStream = new PrintStream(new OutputStream() {
        @Override
        public void write(int b) throws IOException {
            output.append(String.valueOf((char) b));
        }
    });
}
