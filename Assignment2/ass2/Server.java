package ass2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/***
 *This class is a server class for http server and also ftp client Implementation 
 */
public class Server {
	static final String SERVER = "Server: httpfs/1.0.0";
	static final String DATE = "Date: ";
	
	//server socket declaration
	private static ServerSocket serverSocket;
	//port for server socket
	private static int port=8080;
	
	static ObjectOutputStream output = null;
	static ObjectInputStream input = null;
	
	private static ResponseClient serverResponse;

	private static RequestClient clientRequest;
		
	public static void main(String args[]) throws IOException, ClassNotFoundException {
		serverSocket=new ServerSocket(port);
		System.out.println("listening for connection on port 8080");
		
		while(true) {
			final Socket socket=serverSocket.accept();
			//initialize input and output stream
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
			
			//1.Read HTTP request from client socket
			clientRequest = (RequestClient)input.readObject();
			
			String clientType= clientRequest.getClientType();
			String method= clientRequest.getRequestMethod();
			String responseHeaders= getResponseHeaders("HTTP/1.1 200 OK");
			
			//2.Prepare an HTTP response
			
			//3.sent HTTP response to the client
			
			
			//4.close the socket and other resources
			input.close();
			output.close();
			serverSocket.close();
		}
	}
	
	/***
	 * this method is used to get response header in proper required format.
	 * 
	 * @param string
	 * @return
	 */
	static String getResponseHeaders(String string) {
		DateFormat format =new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date= new Date();
		String dateTime = format.format(date);
		String responseHeaders = string + "\n" + "Connection: keep-alive" + "\n" + Server.SERVER + "\n" + Server.DATE + dateTime
				+ "\n" + "Access-Control-Allow-Origin: *" + "\n" +  "Access-Control-Allow-Credentials: true" + "\n" + "Via : 1.1 vegur" + "\n";
		return responseHeaders;
	}
	
}
