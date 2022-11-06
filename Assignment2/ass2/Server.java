package ass2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/***
 *This class is a server class for http server and also ftp client Implementation 
 */
public class Server {
	//server socket declaration
	private static ServerSocket serverSocket;
	//port for server socket
	private static int port=8080;
	public static void main(String args[]) throws IOException {
		serverSocket=new ServerSocket(port);
		System.out.println("listening for connection on port 8080");
		
		while(true) {
			final Socket socket=serverSocket.accept();
			//1.Read HTTP request from client socket
			//2.Prepare an HTTP response
			//3.sent HTTP response to the client
			//4.close the socket
		}
	}
	
}
