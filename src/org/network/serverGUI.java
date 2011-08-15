package org.network;

import java.awt.EventQueue;
import java.awt.TextArea;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;

import javax.print.attribute.standard.MediaSize.Other;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;

/*
 * 控制整個 System.out.print 輸出到 JTextArea
 */
class Console extends OutputStream
{
    private JTextArea textArea;
    
    public Console(final JTextArea textArea)
    {
        this.textArea = textArea;
    }
    
    @Override
    public void write(int b) throws IOException
    {
        this.textArea.append(String.valueOf(b));
    }
    
    @Override
    public void write(byte[] b) throws IOException
    {
        this.textArea.append(new String(b));
    }
    
    @Override
    public void write(byte[] b, int off, int len) throws IOException
    {
        this.textArea.append(new String(b, off, len));
    }
}

/*
 * 
 */
class ServerThread implements Runnable
{
    private Server server;
    
    public ServerThread(Server server)
    {
        this.server = server;
    }
    
    public ServerThread() {}
    
    public void setServer(Server server)
    {
        this.server = server;
    }
    
    public void close()
    {
        this.server.closeService();
    }
    
    public void run()
    {
        this.server.listen();
    }
}

public class serverGUI {

    private JFrame frame;
    private final JTextArea textArea = new JTextArea();
    private ServerThread server = new ServerThread();
    private JTextField textField;
    private boolean flag = false;

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
        System.setOut(new PrintStream(new Console(this.getTextArea())));
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setTitle("伺服器");
        frame.setBounds(100, 100, 363, 344);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        
        JButton ButtonStart = new JButton("運行");
        ButtonStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if(serverGUI.this.flag)
                {
                    System.out.println("服務已經啟動");
                }else
                {
                    serverGUI.this.flag = true;
                    serverGUI.this.server.setServer(new Server(Integer.parseInt(textField.getText())));
                    new Thread(serverGUI.this.server).start();
                }
            }
        });
        ButtonStart.setBounds(53, 255, 87, 23);
        frame.getContentPane().add(ButtonStart);
        
        JButton ButtonClose = new JButton("停止");
        ButtonClose.setEnabled(false);
        ButtonClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if(serverGUI.this.flag) 
                {
                    System.out.println("關閉服務");
                    serverGUI.this.flag = false;
                    serverGUI.this.server.close();
                }else 
                {
                    System.out.println("服務已經關閉");
                }
            }
        });
        ButtonClose.setBounds(217, 255, 87, 23);
        frame.getContentPane().add(ButtonClose);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 10, 327, 193);
        frame.getContentPane().add(scrollPane);
        textArea.setEditable(false);
        
        scrollPane.setViewportView(textArea);
        
        JLabel lblPort = new JLabel("PORT:");
        lblPort.setBounds(10, 213, 46, 15);
        frame.getContentPane().add(lblPort);
        
        textField = new JTextField();
        textField.setText("9999");
        textField.setBounds(53, 210, 96, 21);
        frame.getContentPane().add(textField);
        textField.setColumns(10);
        
    }
    
    public JTextArea getTextArea() {
        return textArea;
    }
}
