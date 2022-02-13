package newtime.wow.praetor.net;

import java.io.*;
import java.net.Socket;
import java.nio.Buffer;

public class Connection implements Runnable{

    public static final int CHUNK_SIZE = 4096;

    public Socket socket;
    public BufferedInputStream in;
    public OutputStream out;

    public Thread listenThread;

    public boolean running = true;

    protected ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedInputStream(socket.getInputStream());
        this.out = socket.getOutputStream();

        this.listenThread = new Thread(this);
        this.listenThread.start();
    }

    public void run(){
        while(running){
            try {
                listen();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            this.listenThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void listen() throws Exception {
        byte[] chunk = new byte[CHUNK_SIZE];

        int amount = 0;
        do {
            amount = in.read(chunk);
            this.buffer.write(chunk);
        }while(amount > -1 && !(amount < CHUNK_SIZE));

        byte[] data = this.buffer.toByteArray();
        this.buffer.reset();

        this.onData(data);
    }

    protected void onData(byte[] data) {
        String raw = new String(data);
        System.out.println(raw);
    }

}
