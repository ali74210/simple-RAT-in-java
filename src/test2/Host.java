package test2;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Host {

    static int port = 7845;
    static Socket socket;
//    static ServerSocket server;

//    static {
//        try {
//            server = new ServerSocket(port);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private static void sendCommand(String command) throws IOException{

        socket = new Socket("localhost", port);
        OutputStream os = socket.getOutputStream();
        OutputStreamWriter osr = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(osr);
        osr.write(command);
        osr.flush();
        System.out.println("sent command from the remote hacker: " + command);
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        while (true){
            String command = getCommand();
            if (command.equals("EXIT")) break;
            sendCommand(command);
        }
    }

    private static String getCommand() {
//        System.out.println("command:");
        Scanner scanner = new Scanner(System.in);
        String command = scanner.next();
        scanner.close();
        return command;
    }

}
