package org.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.crypto.Data;

/**
 * Java 實作多人聊天室
 * @author Cheng-Ting
 * @version 0.1
 */

class handle implements Runnable
{
    private Socket accept;
    private HashSet<Socket> room;
    private String name;
    private int id;
    
    public handle(Socket client, HashSet<Socket> room)
    {
        this.accept = client;
        this.room = room;
        this.id = new Random().nextInt();
    }
    
    /*
     * 處理所有 hashSet 中 所有的 socket 訊息
     */
    public void broadcast(String content)
    {
        for(Socket pc:this.room)
        {
            try {

                if(pc.getOutputStream() != null)
                {
                    new PrintStream(pc.getOutputStream()).println(this.speak(content));
                }else 
                {
                    System.out.println("close");
                }
                
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }  
    }
    
    
    /*
     * 處理訊息的格式
     */
    public String speak(String content)
    {
        return "[" +  this.name + "]Say:  " + content + "     [From]" + this.accept.getInetAddress();
    }
    
    public void run()
    {
        Scanner in = null;
        PrintStream out = null;
        String content = null;
        boolean flag = true;
        
        /*
         * 進入畫面
         */
        try {  
            out = new PrintStream(this.accept.getOutputStream());
            in = new Scanner(this.accept.getInputStream());
            
            if(in.hasNextLine())
            {
                this.name = in.nextLine();
            }
            
            out.println("****************************");
            out.println("***Welcome ~!!!***");   
            out.println("****************************");
            out.println("記憶體: " + Runtime.getRuntime().totalMemory() / 1024 / 104 + "mb");   
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        /*
         * 接收訊息
         */
        while(flag)
        {
            try {
                in.useDelimiter("\n");
                if(in.hasNextLine())
                {
                     content = in.nextLine();
                     
                    if("exit".equals(content))   //離開指令
                    {
                        System.out.println(accept.getInetAddress() + "離開");
                        in.close();
                        out.close();
                        this.accept.close();
                        this.room.remove(this.accept);
                        flag = false;   //跳出接收 Loop
                    }else if("online".equals(content))   //線上人數
                    {
                        out.println("Now:    " + this.room.size());
                    
                    }else if("uptime".equals(content)) 
                    {
                        out.println("uptime:  " + System.currentTimeMillis() / 1000 + "s");
                    
                    }else   //斷線
                    {
                        this.broadcast(content);   //處理廣播出去訊息
                    }
                    
                }else 
                {
                    System.out.println(accept.getInetAddress() + "斷線");
                    in.close();
                    out.close();
                    this.accept.close();
                    this.room.remove(this.accept);
                    flag = false;   //跳出接收 Loop
                }
            } catch (Exception e) {
               System.out.println(e.getStackTrace());
               System.out.println(e.getMessage());
            }
            
        }
    }
}

/*
 * Server 端
 */
public class server {
    
    private int port;
    private HashSet<Socket> room = new HashSet<Socket>();
    private ServerSocket server;
    private ExecutorService service = Executors.newCachedThreadPool();
    
    public server(int port)
    {
        this.port = port;
        try {
            this.server = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void listen()
    {
        System.out.println("Server Start  " + new Date() );
        try {
            while(true)
            {
                Socket accept = server.accept();
                accept.setKeepAlive(true);
                System.out.println(accept.getInetAddress());
                this.room.add(accept);
                service.submit(new handle(accept, this.room));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void closeService()
    {
        this.service.shutdownNow();
    }
    
    public static void main(String[] args) {
        server local = new server(5566);
        local.listen();
    }
}
