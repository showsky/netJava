package org.network;

import java.awt.EventQueue;
import java.awt.TextArea;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.print.attribute.standard.MediaSize.Other;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class serverGUI {

    private JFrame frame;
    private JTextArea textArea;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    serverGUI window = new serverGUI();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public serverGUI() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setTitle("伺服器");
        frame.setBounds(100, 100, 248, 298);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        
        JButton ButtonStart = new JButton("運行");
        ButtonStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                
            }
        });
        ButtonStart.setBounds(10, 213, 87, 23);
        frame.getContentPane().add(ButtonStart);
        
        JButton ButtonClose = new JButton("停止");
        ButtonClose.setBounds(135, 213, 87, 23);
        frame.getContentPane().add(ButtonClose);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 10, 212, 193);
        frame.getContentPane().add(scrollPane);
        
        textArea = new JTextArea();
        scrollPane.setViewportView(textArea);
    }
    public JTextArea getTextArea() {
        return textArea;
    }
}
