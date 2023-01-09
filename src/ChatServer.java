import java.io.IOException;
import java.net.ServerSocket;
import java.net.*;

public class ChatServer {

    public static void main(String[] args) throws IOException {
        int portNumber = Integer.parseInt(args[0]);
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Waiting for clients on port "+portNumber);

            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("Chat client connected from "+socket.getInetAddress()+" on port "+socket.getPort());
                    Thread task = new ClientThread(socket);
                    task.start();
                } catch (IOException e) {
                }
            }
        }   catch (IOException ex) {
            System.out.println("Unable start chat server");
        }

    }

    private static class ClientThread extends Thread {
        private Socket socket;

        ClientThread(Socket socket) {
            this.socket = socket;
        }

        public void m() {
            super.run();
        }
    }


}




