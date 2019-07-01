package socket_programming;
/**
*
* @author sozen
*/
import java.io.* ;
import java.net.* ;
import java.util.* ;
public final class WebServer {
    public static void main(String argv[]) throws Exception {
  // Get the port number from the command line.
  int port = 6789;
        ServerSocket listener = new ServerSocket(port);
  
  // Process HTTP service requests in an infinite loop.
  while (true) {
      // Listen for a TCP connection request.
            Socket socket = listener.accept();
      // Construct an object to process the HTTP request message.
      HttpRequest request = new HttpRequest(socket);
      
      // Create a new thread to process the request.
      Thread thread = new Thread(request);
      
      // Start the thread.
      thread.start();
  }
    }
}