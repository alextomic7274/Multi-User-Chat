import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static ChatServer chatServer = null;

    public static void main(String[] args) throws IOException {
        chatServer = new ChatServer();
        chatServer.startServer();
    }

    public void startServer() {
        /*
        System.out.println("Specify Port:");
        int portNumber = Integer.parseInt(scanner.next());
        */
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Waiting for clients on port: "+1234);

            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("Chat client connected from "+socket.getInetAddress()+" on port "+socket.getPort());
                    Thread clientThread = new Thread(new ClientThreadHandler(socket));
                    clientThread.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }   catch (IOException ex) {
            System.out.println("Unable to start chat server");
            ex.printStackTrace();
        }

    }

    private class ClientThreadHandler implements Runnable {
        private static ArrayList<ClientThreadHandler> clientThreadHandlers = new ArrayList<>();
        private Socket socket;
        private BufferedReader reader;
        public BufferedWriter writer;
        public String username = null;


        ClientThreadHandler(Socket socket) throws IOException {
            this.socket = socket;
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            clientThreadHandlers.add(this);
            username = reader.readLine();
        }

        public void run() {
            try {
                System.out.println(username+" has entered the chat.");
                while (socket.isConnected()) {
                        String message = reader.readLine();
                        if (message != null) {
                            broadcastMessage(message);
                        }
                }
            } catch (IOException e) {
                broadcastMessage(username+" has disconnected");
                closeAll();
            }
        }

        public void broadcastMessage(String message) {
            try {
                for (ClientThreadHandler handler : clientThreadHandlers) {
                    if (handler != this) {
                        handler.writer.write(message);
                        handler.writer.newLine();
                        handler.writer.flush();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void closeAll() {
            try {
                if (writer != null && reader != null) {
                    writer.close();
                    reader.close();
                    socket.close();
                    clientDisconnected();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void clientDisconnected() {
            clientThreadHandlers.remove(this);
            System.out.println(username+" has left the chat.");
        }
    }




}




