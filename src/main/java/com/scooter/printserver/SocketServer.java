package com.scooter.printserver;


import java.util.logging.Logger;
import java.net.*;
import java.io.*;

public class SocketServer {

    private static Logger log = Logger.getLogger(SocketServer.class.getName());

    private ServerSocket serverSocket;

    public SocketServer() {
        log.info("socket server init");
    }

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                new SocketClientHandler(serverSocket.accept()).start();
            }

        } catch (IOException ioe) {
            log.severe(ioe.getMessage());
        }
    }

    public void stop() {

        try {
            serverSocket.close();
        } catch (IOException ioe) {
            log.severe(ioe.getMessage());
        }
    }

    private static class SocketClientHandler extends Thread {

        private Socket clientSocket;
        private DataOutputStream out;
        private DataInputStream in;

        public SocketClientHandler(Socket socket) {
            clientSocket = socket;
        }

        public void run() {

            try {

                in = new DataInputStream(clientSocket.getInputStream());
                out = new DataOutputStream(clientSocket.getOutputStream());
                File tempFile = File.createTempFile("tempReceipt", null);

                FileOutputStream fos = new FileOutputStream(tempFile, true);


                int count = -1;
                byte[] fileBuffer = new byte[Integer.parseInt(in.readUTF())];
                while ((count = in.read(fileBuffer)) > 0 ) {
                    fos.write(fileBuffer,0, count);

                }
                fos.flush();
                fos.close();

                log.warning(tempFile.getAbsolutePath());
                PrintReceipt pr = new PrintReceipt();
                pr.printFile(tempFile);
                tempFile.deleteOnExit();
                

                in.close();
                out.close();
                clientSocket.close();

            } catch (IOException ioe) {
                log.severe(ioe.getMessage());
            }
        }

    }
}
