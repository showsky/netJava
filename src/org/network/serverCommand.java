package org.network;

public class serverCommand {
    public static void main(String[] args) {
        Server local = new Server(9999);
        local.listen();
    }
}
