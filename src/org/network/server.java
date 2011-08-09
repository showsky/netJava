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

import javax.xml.crypto.Data;

/**
 * 聊天室
 * 
 * [目前現有功能]
 * 1. uptime 執行時間
 * 2. online 線上人數
 * 
 * [預計增加功能]
 * 1.goole 翻譯
 * 2. 資料庫 log 處理
 * @author Cheng-Ting
 * @version 0.1
 */

class cleanTask extends TimerTask
{
    private HashSet<Socket> room = null;
    
    public cleanTask(HashSet<Socket> room)
    {
        this.room = room;
    }
    
    public void run() 
    {
        
    }
}

class handle implements Runnable
{
    private Socket accept;
    private HashSet<Socket> room;
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
        return "[" +  this.id + "]Say:  " + content + "     [From]" + this.accept.getInetAddress();
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
            out.println("****************************");
            out.println("***Welcome ~!!!***");   
            out.println("****************************");
            out.println(Runtime.getRuntime().maxMemory() / 1024 / 104 + "mb");   
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        /*
         * 接收訊息
         */
        while(flag)
        {
            try {
                //in = new Scanner(this.accept.getInputStream());
                in.useDelimiter("\n");
                //in.useLocale(Locale.getDefault());
                if(in.hasNextLine())
                {
                     content = in.nextLine();
                     
                    if("exit".equals(content))   //離開指令
                    {
                        out.println("****************************");
                        out.println("***bye bye ~!!!***");   
                        out.println("****************************");   
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
                    }else 
                    {
                        this.broadcast(content);   //處理廣播出去訊息
                    }
                    
                    //Thread.sleep(1000);
                    
                }
            } catch (Exception e) {
               System.out.println(e.getStackTrace());
               System.out.println(e.getMessage());
            }
            
        }
    }
}

public class server {
    public static void main(String[] args) throws Exception {
        
        HashSet<Socket> room = new HashSet<Socket>();
        final int PORT = 9999;
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("Server Start  " + new Date() );
        while(true)
        {
            Socket accept = server.accept();
            accept.setKeepAlive(true);
            System.out.println(accept.getInetAddress());
            room.add(accept);
            new Thread(new handle(accept, room)).start();
            
        }
    }
}
