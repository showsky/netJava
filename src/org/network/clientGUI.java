package org.network;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.CardLayout;
import javax.swing.JPanel;
import java.awt.BorderLayout;

import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Scanner;

import javax.swing.JTextPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * 處理背後的讀取訊息的 Thread
 * @author Cheng-Ting
 *
 */
class view implements Runnable
{
    private Scanner b_in;
    private JTextArea textPane;
    private boolean flag = true;
    
    /**
     * 建構
     * @param b_in
     * @param textPane
     */
    public view(Scanner b_in, JTextArea textPane)
    {
        this.b_in = b_in;
        this.textPane = textPane;
    }
    
    public void close()
    {
        this.flag = false;
    }
    
    public void run()
    {
        while(true)
        {
            try {
                if(b_in.hasNextLine())
                {
                    textPane.append(b_in.nextLine() + "\n");
                    textPane.setCaretPosition(textPane.getDocument().getLength());
                }
                
                //Thread.sleep(1000);
                
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }
}


public class clientGUI {

    private JFrame frmV;
    private JTextField textField;
    private JTextArea textArea;
    private InetSocketAddress addr;
    private JButton buttonSend;
    
    private Scanner b_in;
    private PrintStream b_out;
    private Socket server;
    private JTextField textStatus;
    private JTextField textServer;
    private JTextField textPort;
    private JTextField textName;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    clientGUI window = new clientGUI();
                    window.frmV.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    /**
     * 連線
     */
    public void conn()
    {
        try {
            server = new Socket();
            addr = new InetSocketAddress(this.textServer.getText(), Integer.parseInt(this.textPort.getText()));
            this.textArea.append("資料連線中~!!!\n");
            server.connect(addr, 3000);
            
            this.textStatus.setText("OnLine");
            InputStreamReader in = new InputStreamReader(server.getInputStream());
            b_in = new Scanner(in);
            b_out = new PrintStream(server.getOutputStream());
            b_out.println(this.textName.getText());
            
            new Thread(new view(b_in, textArea)).start();
            
        } catch (SocketTimeoutException e) {
            this.textArea.append("目前沒有辦法連線~!!\n");
            this.textStatus.setText("OffLine");
        }catch (IOException e) {
            this.textArea.append("Socket IO 錯誤~!!\n");
        }
    }
    
    /**
     * 檢測是否連線送出訊息
     * @param content
     */
    public void testSpeak(String content)
    {
        if(clientGUI.this.server.isConnected())
        {
            clientGUI.this.b_out.println(content);
        }else 
        {
            clientGUI.this.textArea.append("目前為離線狀態請連線~!!!\n");
        }
    }

    /**
     * Create the application.
     */
    public clientGUI() {
        initialize();
        
        /*
         * 進行初始連線 
         */
        this.server = new Socket();
        this.textStatus.setText("OffLine");
        
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frmV = new JFrame();
        frmV.setAlwaysOnTop(true);
        frmV.setResizable(false);
        frmV.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                try {
                    if(clientGUI.this.server.isConnected())
                    {
                        clientGUI.this.b_out.print("exit");
                        clientGUI.this.b_out.close();
                        clientGUI.this.b_in.close();
                        clientGUI.this.server.close();
                    }
                } catch (IOException e) {
                    
                }
            }
        });
        frmV.setTitle("聊天室 v0.1");
        frmV.setBounds(100, 100, 381, 354);
        frmV.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmV.getContentPane().setLayout(new CardLayout(0, 0));
        
        JPanel panel_login = new JPanel();
        frmV.getContentPane().add(panel_login, "name_546949993280710");
        panel_login.setLayout(null);
        
        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(21, 37, 318, 204);
        panel_login.add(scrollPane_1);
        
        JTextArea textVersion = new JTextArea();
        textVersion.setText("[作者]\r\nChing Ting\r\n[信箱]\r\nshowsky@gmail.com\r\n[功能]\r\n1.聊天室ID定義\r\n2.伺服器 IP 和 PROT 設定\r\n3.線上人數功能\r\n4.伺服器時間功能");
        textVersion.setEditable(false);
        scrollPane_1.setViewportView(textVersion);
        
        JLabel labelVersion = new JLabel("版本說明");
        labelVersion.setBounds(10, 10, 63, 15);
        panel_login.add(labelVersion);
        
        JLabel labelId = new JLabel("設定ID");
        labelId.setBounds(10, 249, 46, 15);
        panel_login.add(labelId);
        
        textName = new JTextField();
        textName.setBounds(20, 274, 96, 21);
        panel_login.add(textName);
        textName.setColumns(10);
        
        JButton buttonReg = new JButton("註冊");
        buttonReg.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if(textName.getText().equals(""))
                {
                    JOptionPane.showMessageDialog(frmV, "請設定ID名稱");
                }else
                {
                    CardLayout card = (CardLayout) frmV.getContentPane().getLayout();
                    card.next(frmV.getContentPane());
                }
            }
        });
        buttonReg.setVerticalAlignment(SwingConstants.BOTTOM);
        buttonReg.setBounds(128, 273, 87, 23);
        panel_login.add(buttonReg);
        
        JPanel panel_main = new JPanel();
        frmV.getContentPane().add(panel_main, "name_546953600864635");
        panel_main.setLayout(null);
        
        textField = new JTextField();
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent arg0) {
                if(KeyEvent.getKeyText(arg0.getKeyCode()).equals("Enter"))
                {
                    clientGUI.this.testSpeak(clientGUI.this.textField.getText());
                    clientGUI.this.textField.setText(null);
                }
            }
        });
        textField.setBounds(26, 233, 227, 21);
        panel_main.add(textField);
        textField.setColumns(10);
        
        buttonSend = new JButton("送出");
        buttonSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                clientGUI.this.testSpeak(clientGUI.this.textField.getText());
                clientGUI.this.textField.setText(null);
            }
        });
        buttonSend.setBounds(261, 232, 87, 23);
        panel_main.add(buttonSend);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 10, 355, 188);
        panel_main.add(scrollPane);
        
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        scrollPane.setViewportView(textArea);
        textArea.setAutoscrolls(true);
        textArea.setEditable(false);
        
        JLabel labelFunc = new JLabel("功能:");
        labelFunc.setBounds(10, 264, 46, 15);
        panel_main.add(labelFunc);
        
        JButton buttonOnline = new JButton("線上人數");
        buttonOnline.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                clientGUI.this.testSpeak("online");
            }
        });
        buttonOnline.setBounds(20, 289, 87, 23);
        panel_main.add(buttonOnline);
        
        JButton buttonUptime = new JButton("運行時間");
        buttonUptime.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                clientGUI.this.testSpeak("uptime");              
            }
        });
        buttonUptime.setBounds(117, 289, 87, 23);
        panel_main.add(buttonUptime);
        
        JLabel labelStatus = new JLabel("目前狀態:");
        labelStatus.setBounds(10, 208, 56, 15);
        panel_main.add(labelStatus);
        
        JButton buttonConn = new JButton("連線");
        buttonConn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if(clientGUI.this.textServer.getText().equals("") || clientGUI.this.textPort.getText().equals(""))
                {
                    JOptionPane.showMessageDialog(frmV, "請輸入完整SERVER和PORT\n");
                }else {
                    if(clientGUI.this.server.isConnected())
                    {
                        clientGUI.this.textArea.append("目前為連線狀態~!\n");
                    }else 
                    {
                        clientGUI.this.conn();
                    }
                }
            }
        });
        buttonConn.setBounds(215, 289, 63, 23);
        panel_main.add(buttonConn);
        
        textStatus = new JTextField();
        textStatus.setEditable(false);
        textStatus.setBounds(65, 205, 45, 21);
        panel_main.add(textStatus);
        textStatus.setColumns(10);
        
        JLabel labelServer = new JLabel("伺服器:");
        labelServer.setBounds(117, 208, 46, 15);
        panel_main.add(labelServer);
        
        textServer = new JTextField();
        textServer.setText("192.168.1.106");
        textServer.setBounds(165, 205, 96, 21);
        panel_main.add(textServer);
        textServer.setColumns(10);
        
        JLabel labelPort = new JLabel("PORT:");
        labelPort.setBounds(267, 208, 37, 15);
        panel_main.add(labelPort);
        
        textPort = new JTextField();
        textPort.setText("9999");
        textPort.setBounds(308, 205, 46, 21);
        panel_main.add(textPort);
        textPort.setColumns(10);
        
        JButton buttonClose = new JButton("離線");
        buttonClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if(clientGUI.this.server.isConnected())
                {
                    try {
                        clientGUI.this.b_out.println("quit");
                        clientGUI.this.server.close();
                        clientGUI.this.textArea.append("關閉連線中~!!\n");
                    } catch (IOException e) {
                        clientGUI.this.textArea.append("關閉錯誤 ~!!\n");
                    }
                    clientGUI.this.server = new Socket();
                    clientGUI.this.textStatus.setText("OffLine");
                }else 
                {
                    clientGUI.this.textArea.append("目前沒有連線~!!\n");
                }
            }
        });
        buttonClose.setBounds(285, 289, 63, 23);
        panel_main.add(buttonClose);
    }
}
