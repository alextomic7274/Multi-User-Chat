import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClient {
    private int portNumber = 0;
    private String hostname = null;
    private Scanner scanner = null;

    public static void main(String[] args) {
        try {
            ChatClient chatClient = new ChatClient();
            chatClient.startClient();
        } catch (Exception e) {
            System.out.println("Chat Client Failed");
            e.printStackTrace();
        }
    }

    public void startClient() throws UnknownHostException {
        scanner = new Scanner(System.in);
        System.out.println("Enter port: ");
        portNumber = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter hostname");
        hostname = scanner.nextLine();
        System.out.println("local"+ InetAddress.getLocalHost());

        try (Socket socket = new Socket(InetAddress.getByName(hostname), portNumber)) {

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}