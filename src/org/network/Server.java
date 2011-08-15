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

/*
 * Server 端
 */
public class Server {
    
    private int port;
    private HashSet<Socket> room = new HashSet<Socket>();
    private ServerSocket server;
    private ExecutorService service = Executors.newCachedThreadPool();
    
    public Server(int port)
    {
        this.setPort(port);
        try {
            this.server = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void setPort(int port)
    {
        this.port = port;
    }
    
    public int getPort(int port)
    {
        return this.port;
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
                service.submit(new Handle(accept, this.room));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void closeService()
    {
        this.service.shutdownNow();
    }
}