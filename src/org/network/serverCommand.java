package org.network;

public class serverCommand {
    public static void main(String[] args) {
        Server local = new Server(5566);
        local.listen();
    }
}
