package yw.socket;


import yw.model.Greeting;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * @author lnagaraj
 */
public class SocketClient {

    /**
     * Host name of the Shared Hardware Server
     */
    private String hostName;

    /**
     * Port number of the Shared Hardware Server
     */
    private int portNumber;

    /**
     * time out of the response from the Shared Hardware Server
     */
    private final int readTimeout = 10 * 60 * 1000;

    /**
     * @param hostName
     *            The IP/Hostname of the machine on which the Shared Hardware server is running
     * @param portNumber
     *            The port number of the machine on which the Shared Hardware server is listening.
     * @throws UnknownHostException
     * @throws IOException
     */
    public SocketClient(String hostName, int portNumber) {
        super();
        this.setHostName(hostName);
        this.setPortNumber(portNumber);
    }

    /**
     * @return name of the Shared Hardware Server
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * @param hostName
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * @return port number of the Shared Hardware Server
     */
    public int getPortNumber() {
        return portNumber;
    }

    /**
     * @param portNumber
     */
    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public Greeting greeting(String name) {
        HashMap<String, String> params = new HashMap();
        Greeting serResponse = null;
        params.put("name", name);
        OutputStream outputStream;
        InputStream inputStream;
        Socket clientSocket = null;
        try {
            clientSocket = new Socket(this.hostName, portNumber);
            clientSocket.setSoTimeout(readTimeout);
            // Get output and input stream handles
            outputStream = clientSocket.getOutputStream();
            inputStream = clientSocket.getInputStream();

            // Get Output stream object handle and write the SharedHardware request to
            // this output object stream
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(params);
            objectOutputStream.flush();

            // Wait for response from the Shared Hardware Server
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            serResponse = (Greeting) objectInputStream.readObject();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (clientSocket != null) {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return serResponse;
    }
}
