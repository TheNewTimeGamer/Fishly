package newtime.wow.praetor.net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable {

    ServerSocket server;

    ArrayList<Connection> connections = new ArrayList<Connection>();

    Thread connectionThread;

    public Server(int port) throws IOException {
        this.server = new ServerSocket(port);
        this.connectionThread = new Thread(this);
    }

    boolean running = true;

    public void run(){
        while(running){
            try {
                Connection connection = listen();
                if(connection != null){
                    this.connections.add(connection);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            this.connectionThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Connection listen() throws IOException {
        Socket socket = this.server.accept();
        Connection connection = null;
        if(socket != null){
            try {
                connection = new Connection(socket);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return connection;
    }

}
