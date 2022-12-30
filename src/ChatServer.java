import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    public void startServer() throws Exception {
        ServerSocket s = new ServerSocket(1234);

        while(true){
            s.accept();
        }

    }


}
