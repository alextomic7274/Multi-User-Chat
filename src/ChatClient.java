import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    private int portNumber = 0;
    private String hostname = null;
    private String username = null;
    private BufferedWriter serverWriter = null;
    private BufferedReader incomingMessageReader = null;
    private  BufferedReader userInputReader = null;
    private Socket socket = null;

    public static void main(String[] args) {
        try {
            ChatClient chatClient = new ChatClient();
            chatClient.setUpClient();
            chatClient.messageReceiver();
            chatClient.sendMessage();
        } catch (Exception e) {
            System.out.println("Chat Client Failed");
            e.printStackTrace();
        }
    }

    public void setUpClient() throws IOException {
        try {
            getInfo();
            socket = new Socket(InetAddress.getByName(hostname), portNumber);
            serverWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            incomingMessageReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            userInputReader = new BufferedReader(new InputStreamReader(System.in));
            serverWriter.write(username);
            serverWriter.newLine();
            serverWriter.flush();
        }   catch(Exception e) {
            System.out.println("Connection Error - Try Again");
            closeAll();
            setUpClient();
        }
    }

    public void sendMessage() {
        try {
            while (socket.isConnected()) {
                if (userInputReader.ready()) {
                    String message = userInputReader.readLine();
                    serverWriter.write(username + ": " + message);
                    serverWriter.newLine();
                    serverWriter.flush();
                }
            }
        } catch (IOException e) {
            closeAll();
        }
    }

    public void messageReceiver() {
        new Thread(() -> {
            try {
                while (socket.isConnected()) {
                    String message = incomingMessageReader.readLine();
                    if (message != null) System.out.println(message);
                }
            } catch (IOException e) {
                System.out.println("ERROR - Disconnected from server");
                closeAll();
            }
        }).start();
    }

    public void closeAll() {
        try {
            if (serverWriter != null && incomingMessageReader != null && userInputReader != null) {
                serverWriter.close();
                incomingMessageReader.close();
                userInputReader.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getInfo() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter port: ");
        portNumber = Integer.parseInt(scanner.next());
        System.out.println("Enter hostname");
        hostname = scanner.next();
        System.out.println("Enter username: ");
        username = scanner.next().trim();
    }


}