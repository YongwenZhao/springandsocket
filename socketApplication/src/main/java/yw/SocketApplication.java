package yw;

import yw.socket.SocketWorker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketApplication {
    public static void main(String[] args) {
        int port = 2343;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                System.out.println("listening......");
                Socket clientSocket = serverSocket.accept();
                System.out.println("accepted");
                SocketWorker servWorkerThread = new SocketWorker(clientSocket);
                servWorkerThread.start();
            }
        } catch (IOException e) {

        }
    }
}
