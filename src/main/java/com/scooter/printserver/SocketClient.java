package com.scooter.printserver;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

public class SocketClient {

    private static Logger log = Logger.getLogger(SocketClient.class.getName());

    private Socket clientSocket;
    private OutputStream out;
    private BufferedReader in;

    public SocketClient() {
        log.info("socketclient init");
    }

    public void startConnection(String ip, int port) {

        try {
            clientSocket = new Socket(ip, port);
            out = clientSocket.getOutputStream();
            in = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));

        } catch (UnknownHostException ue) {
            log.severe(ue.getMessage());
        } catch (IOException ioe) {
            log.severe(ioe.getMessage());
        }
    }

 public void sendFile(File fileToSend) {

        String termination = "no";

        try {
            byte[] fileByteArray = new byte[(int) fileToSend.length()];
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileToSend));
            bis.read(fileByteArray, 0, fileByteArray.length);

            out.write(fileByteArray, 0, fileByteArray.length);
            out.flush();

        } catch (IOException ioe) {
            log.warning(ioe.getMessage());
        }



 }

    public void stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException ioe) {
            log.severe(ioe.getMessage());
        }
    }

}
