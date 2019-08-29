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
        private PrintWriter out;
        private DataInputStream in;

        public SocketClientHandler(Socket socket) {
            clientSocket = socket;
        }

        public void run() {

            try {

                in = new DataInputStream(clientSocket.getInputStream());
                out = new PrintWriter(clientSocket.getOutputStream());
                File tempFile = File.createTempFile("tempReceipt", null);
                log.warning(tempFile.getAbsolutePath());

                FileOutputStream fos = new FileOutputStream(tempFile);

                int count = 0;
                byte[] fileBuffer = new byte[4096];
                while ((count = in.read(fileBuffer)) > 0 ) {
                    fos.write(fileBuffer, 0, count);
                }

                log.warning(tempFile.getAbsolutePath());
                PrintReceipt.printFile(tempFile);
                tempFile.deleteOnExit();

                out.println("200");

                in.close();
                out.close();
                clientSocket.close();

            } catch (IOException ioe) {
                log.severe(ioe.getMessage());
            }
        }

    }
}
