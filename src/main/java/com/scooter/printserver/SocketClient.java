package com.scooter.printserver;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

public class SocketClient {

    private static Logger log = Logger.getLogger(SocketClient.class.getName());

    private Socket clientSocket;
    private DataOutputStream out;
    private DataInputStream in;

    public SocketClient() {
        log.info("socketclient init");
    }

    public void startConnection(String ip, int port) {

        try {
            clientSocket = new Socket(ip, port);
            clientSocket.setSoTimeout(500);
            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new DataInputStream(clientSocket.getInputStream());

        } catch (UnknownHostException ue) {
            log.severe(ue.getMessage());
        } catch (IOException ioe) {
            log.severe(ioe.getMessage());
        }
    }

 public String sendFile(File fileToSend) {

String response = "no";

        try {
            byte[] fileByteArray = new byte[(int) fileToSend.length()];
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileToSend));
            bis.read(fileByteArray, 0, fileByteArray.length);
            bis.close();

            out.writeUTF(Long.toString(fileToSend.length()));
            out.write(fileByteArray, 0, fileByteArray.length);
            out.flush();

            response = in.readUTF();


        } catch (IOException ioe) {
            log.warning(ioe.getMessage());
        }

        return response;

 }

    public void stopConnection() {
        try {
            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException ioe) {
            log.severe(ioe.getMessage());
        }
    }

}
