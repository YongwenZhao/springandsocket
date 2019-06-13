package yw.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import yw.model.Greeting;

public class SocketWorker extends Thread {
    private static final String template = "Hello, %s!";
    private static final AtomicLong counter = new AtomicLong();
    private static Lock lock = new ReentrantLock();

    private Socket socket;
    /**
     *
     */
    private ObjectInputStream objectInputStream = null;
    /**
     *
     */
    private ObjectOutputStream objectOutputStream = null;

    /**
     * @param socket
     */
    public SocketWorker(Socket socket) {
        super();
        this.socket = socket;
    }

    /* (non-Javadoc)
     * @see java.lang.Thread#run()
     */
    public void run() {
        while (!this.socket.isClosed()) {
            HashMap<String, String> netPacket;
            Greeting serverPacket;
            try {
                InputStream inputStream = socket.getInputStream();
                OutputStream outputstream = socket.getOutputStream();
                this.objectInputStream = new ObjectInputStream(inputStream);
                this.objectOutputStream = new ObjectOutputStream(outputstream);
                netPacket = (HashMap<String, String>) this.objectInputStream.readObject();
                serverPacket = processRequest(netPacket);
            } catch (ClassNotFoundException e) {
                break;
            } catch (IOException e) {
                break;
            }
            try {
                this.objectOutputStream.writeObject(serverPacket);
                this.objectOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        cleanUpObjectStream();
    }

    private Greeting processRequest(Map netPacket) {
        try {
            lock.lock();
            for(int i = 0; i < 100; i++) {
                long sum = 0;
                for(int j = 0; j < 10000; j++) {
                    sum += j;
                }
                System.out.println(i + " sum: " + sum);
            }
            return new Greeting(counter.incrementAndGet(), String.format(template, netPacket.get("name")));
        } finally {
            lock.unlock();
        }
    }

    /**
     * Cleans up object steam handles
     */
    private void cleanUpObjectStream() {
        try {
            if (this.objectInputStream != null) {
                this.objectInputStream.close();
            }
            if (this.objectOutputStream != null) {
                this.objectOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
