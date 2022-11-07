package ass2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/***
 *This class is a server class for http server and also ftp client Implementation 
 */
public class Server {
	static final String SERVER = "Server: httpfs/1.0.0";
	static final String DATE = "Date: ";
	
	static boolean temp=false;
	
	//server socket declaration
	private static ServerSocket serverSocket;
	//port for server socket
	private static int port=8080;
	
	static ObjectOutputStream output = null;
	static ObjectInputStream input = null;
	
	private static ResponseClient serverResponse;

	private static RequestClient clientRequest;
		
	public static void main(String args[]) throws IOException, ClassNotFoundException, URISyntaxException {
		String request;
		List<String> requestList = new ArrayList<>();

		String dir = System.getProperty("user.dir");
		
		System.out.println("Directory ==>>>>> " + dir);

		System.out.print("==>");
		Scanner sc = new Scanner(System.in);
		request = sc.nextLine();
		if (request.isEmpty() || request.length() == 0) {
			System.out.println("Invalid Command Please try again!!");
		}
		String[] requestArray = request.split(" ");
		requestList = new ArrayList<>();
		for (int i = 0; i < requestArray.length; i++) {
			requestList.add(requestArray[i]);
		}

		if (requestList.contains("-v")) {
			temp = true;
		}

		if (requestList.contains("-p")) {
			String portStr = requestList.get(requestList.indexOf("-p") + 1).trim();
			port = Integer.valueOf(portStr);
		}

		if (requestList.contains("-d")) {
			dir = requestList.get(requestList.indexOf("-d") + 1).trim();
			System.out.println("Directory ==>>>>> " + dir);
		}
		
		serverSocket=new ServerSocket(port);
		if (temp) {
				System.out.println("listening for connection on port: " + port);
		}
	//	System.out.println("listening for connection on port 8080");
		
		File currentFolder = new File(dir);


		
		while(true) {
			serverResponse = new ResponseClient();
			Socket socket=serverSocket.accept();
			if (temp)
				System.out.println("Server is Connected to client ======>>>");
			//initialize input and output stream
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
			
			//1.Read HTTP request from client socket
			clientRequest = (RequestClient)input.readObject();
			System.out.println("Message Received: " + clientRequest);
			
			String clientType= clientRequest.getClientType();
			String method= clientRequest.getRequestMethod();
			String responseHeaders= getResponseHeaders("HTTP/1.1 200 OK");
			
			if(clientType.equalsIgnoreCase("httpc")) {
				String url = clientRequest.getRequestUrl();
				URI uri = new URI(clientRequest.getRequestUrl());
				String host = uri.getHost();

				String query = uri.getQuery();

				System.out.println("clientType ==> " + clientType);

				if (temp)
					System.out.println(" Server is Processing the httpc request");

				String[] paramArr = {};
				if (query != null && !query.isEmpty()) {

					paramArr = query.split("&");
				}
				String inlineData = "";
				String fileData = "";

				if (clientRequest.hasVerbose()) {
					serverResponse.setResponseHeaders(responseHeaders);
				}
				String body = "{\n";
				body = body + "\t\"args\":";
				body = body + "{";
				// for query parameters from client
				if (paramArr.length > 0) {
					for (int i = 0; i < paramArr.length; i++) {
						body = body + "\n\t    \"" + paramArr[i].substring(0, paramArr[i].indexOf("=")) + "\": \""
								+ paramArr[i].substring(paramArr[i].indexOf("=") + 1) + "\"";
						if (i != paramArr.length - 1) {
							body = body + ",";
						} else {
							body = body + "\n";
							body = body + "\t},\n";
						}
					}
				} else {
					body = body + "},\n";
				}

				// if method type is POST then
				if (method.equalsIgnoreCase("POST")) {
					body = body + "\t\"data\": ";
					if (clientRequest.getHasInlineData()) {
						inlineData = clientRequest.getInlineData();
						body = body + "\"" + inlineData + "\",\n";
					} else if (clientRequest.getTransferSuc()) {
						fileData = clientRequest.getFileSendData();
						body = body + "\"" + fileData + "\",\n";
					} else {
						body = body + "\"\",\n";
					}
					body = body + "\t\"files\": {},\n";
					body = body + "\t\"form\": {},\n";
				}

				body = body + "\t\"headers\": {";

				// for headers only
				if (clientRequest.isHttpHeader()) {

					for (String header : clientRequest.getHeaderLst()) {
						String[] headerArr = header.split(":");
						if (headerArr[0].equalsIgnoreCase("connection"))
							continue;
						body = body + "\n\t\t\"" + headerArr[0] + "\": \"" + headerArr[1].trim() + "\",";

					}
				}
				if (clientRequest.getHasInlineData()) {
					body = body + "\n\t\t\"Content-Length\": \"" + clientRequest.getInlineData().length() + "\",";
				} else if (clientRequest.getTransferSuc()) {
					body = body + "\n\t\t\"Content-Length\": \"" + clientRequest.getFileSendData().length() + "\",";
				}
				body = body + "\n\t\t\"Connection\": \"close\",\n";
				body = body + "\t\t\"Host\": \"" + host + "\"\n";
				body = body + "\t},\n";

				if (method.equalsIgnoreCase("POST")) {
					body = body + "\t\"json\": ";
					if (clientRequest.getHasInlineData()) {
						body = body + "{\n\t\t " + inlineData.substring(1, inlineData.length() - 1) + "\n\t},\n";
					} else {
						body = body + "{\n\t\t " + fileData + "\n\t},\n";
					}
				}
				body = body + "\t\"origin\": \"" + InetAddress.getLocalHost().getHostAddress() + "\",\n";
				body = body + "\t\"url\": \"" + url + "\"\n";
				body = body + "}";

				serverResponse.setResponseBody(body);
				
				if (temp)
					System.out.println("Sending the response to Client ======>");

				// write object to Socket
				output.writeObject(serverResponse);
				// close resources
				input.close();
				output.close();
				serverSocket.close();

			}
			
			else if(clientType.equalsIgnoreCase("https")) {
				URI uri =new URI(clientRequest.getRequestUrl());
				String host =uri.getHost();
				
				String url=clientRequest.getHttpRequest();
				
				if(temp) {
					System.out.println("Processing httpfs request");
				}
				String body = "{\n";
				body = body + "\t\"args\":";
				body = body + "{},\n";
				body = body + "\t\"headers\": {";
				body = body + "\n\t\t\"Connection\": \"close\",\n";
				body = body + "\t\t\"Host\": \"" + host + "\"\n";
				body = body + "\t},\n";
				
				if(!method.endsWith("/") && method.contains("get/") && url.contains("Content-Disposition:attachment")) {
					body = body + "\n\t\t\"Content-Disposition\": \"attachment\",";
				} else if (!method.endsWith("/") && method.contains("get/")
						&& url.contains("Content-Disposition:inline")) {
					body = body + "\n\t\t\"Content-Disposition\": \"inline\",";
				}
				body = body + "\n\t\t\"Connection\": \"close\",\n";
				body = body + "\t\t\"Host\": \"" + host + "\"\n";
				body = body + "\t},\n";
				
				if (method.equalsIgnoreCase("get/")) {

					body = body + "\t\"files\": { ";
					List<String> files = getFilesFromDir(currentFolder);
					List<String> fileFilterList = new ArrayList<String>();
					fileFilterList.addAll(files);
					if (url.contains("Content-Type")) {
						String fileType = clientRequest.getHeaderLst().get(0).split(":")[1];
						fileFilterList = new ArrayList<String>();
						for (String file : files) {
							if (file.endsWith(fileType)) {
								fileFilterList.add(file);
							}
						}
					}
					for (int i = 0; i < fileFilterList.size(); i++) {

						if (i != fileFilterList.size() - 1) {
							body = body + fileFilterList.get(i) + " , ";
						} else {
							body = body + fileFilterList.get(i) + " },\n";
						}

					}

				}

				// if the request is get/fileName
				else if (!method.endsWith("/") && method.contains("get/")) {

					String response = "";
					String requestedFileName = method.split("/")[1];
					List<String> files = getFilesFromDir(currentFolder);

					if (!files.contains(requestedFileName)) {
						responseHeaders = getResponseHeaders("HTTP/1.1 404 FILE NOT FOUND");

					} else {

						File file = new File(dir + "/" + requestedFileName);
						BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
						String st;
						while ((st = bufferedReader.readLine()) != null) {
							response = response + st;
						}
						if (url.contains("Content-Disposition:attachment")) {
							serverResponse.setResponseCode("203");
							serverResponse.setResponseBody(response);
							serverResponse.setRequestFileName(requestedFileName);

						} else {

							serverResponse.setResponseCode("203");
							body = body + "\t\"data\": \"" + response + "\",\n";
						}

					}

				}

				else if (!method.endsWith("/") && method.contains("post/")) {

					String fileName = method.split("/")[1];
					File file = new File(fileName);
					List<String> files = getFilesFromDir(currentFolder);
					if (files.contains(fileName)) {

						if (url.contains("overwrite")) {
							String overwrite = clientRequest.getHeaderLst().get(0).split(":")[1];
							if (overwrite.equalsIgnoreCase("true")) {
								synchronized (file) {
									file.delete();
									file = new File(dir + "/" + fileName);
									file.createNewFile();
									FileWriter fw = new FileWriter(file);
									fw.write(clientRequest.getInlineData());
									fw.close();
								}
								responseHeaders = getResponseHeaders( "HTTP/1.1 201 FILE OVER-WRITTEN");
							} else {
								responseHeaders = getResponseHeaders("HTTP/1.1 201 FILE NOT OVER-WRITTEN");
							}
						} else {
							synchronized (file) {
								file.delete();
								file = new File(dir + "/" + fileName);
								file.createNewFile();
								FileWriter fw = new FileWriter(file);
								fw.write(clientRequest.getInlineData());
								fw.close();
							}
							responseHeaders = getResponseHeaders("HTTP/1.1 201 FILE OVER-WRITTEN");
						}

					} else {

						file = new File(dir + "/" + fileName);
						synchronized (file) {
							file.createNewFile();
							FileWriter fw = new FileWriter(file);
							BufferedWriter bw = new BufferedWriter(fw);
							PrintWriter pw = new PrintWriter(bw);

							pw.write(clientRequest.getInlineData());
							pw.flush();
							pw.close();
						}
						responseHeaders = getResponseHeaders("HTTP/1.1 202 NEW FILE CREATED");

					}

				}
				body = body + "\t\"origin\": \"" + InetAddress.getLocalHost().getHostAddress() + "\",\n";
				body = body + "\t\"url\": \"" + url + "\"\n";
				body = body + "}";

				if (temp)
					System.out.println("Sending the response to Client ======>");

				// write object to Socket
				serverResponse.setResponseHeaders(responseHeaders);
				serverResponse.setResponseBody(body);
				output.writeObject(serverResponse);
				
			}
//			else if(clientType.equalsIgnoreCase("httpc")) {
//				String url = clientRequest.getRequestUrl();
//				URI uri = new URI(clientRequest.getRequestUrl());
//				String host = uri.getHost();
//
//				String query = uri.getQuery();
//
//				System.out.println("clientType ==> " + clientType);
//
//				if (temp)
//					System.out.println(" Server is Processing the httpc request");
//
//				String[] paramArr = {};
//				if (query != null && !query.isEmpty()) {
//
//					paramArr = query.split("&");
//				}
//				String inlineData = "";
//				String fileData = "";
//
//				if (clientRequest.hasVerbose()) {
//					serverResponse.setResponseHeaders(responseHeaders);
//				}
//				String body = "{\n";
//				body = body + "\t\"args\":";
//				body = body + "{";
//				// for query parameters from client
//				if (paramArr.length > 0) {
//					for (int i = 0; i < paramArr.length; i++) {
//						body = body + "\n\t    \"" + paramArr[i].substring(0, paramArr[i].indexOf("=")) + "\": \""
//								+ paramArr[i].substring(paramArr[i].indexOf("=") + 1) + "\"";
//						if (i != paramArr.length - 1) {
//							body = body + ",";
//						} else {
//							body = body + "\n";
//							body = body + "\t},\n";
//						}
//					}
//				} else {
//					body = body + "},\n";
//				}
//
//				// if method type is POST then
//				if (method.equalsIgnoreCase("POST")) {
//					body = body + "\t\"data\": ";
//					if (clientRequest.getHasInlineData()) {
//						inlineData = clientRequest.getInlineData();
//						body = body + "\"" + inlineData + "\",\n";
//					} else if (clientRequest.getTransferSuc()) {
//						fileData = clientRequest.getFileSendData();
//						body = body + "\"" + fileData + "\",\n";
//					} else {
//						body = body + "\"\",\n";
//					}
//					body = body + "\t\"files\": {},\n";
//					body = body + "\t\"form\": {},\n";
//				}
//
//				body = body + "\t\"headers\": {";
//
//				// for headers only
//				if (clientRequest.isHttpHeader()) {
//
//					for (String header : clientRequest.getHeaderLst()) {
//						String[] headerArr = header.split(":");
//						if (headerArr[0].equalsIgnoreCase("connection"))
//							continue;
//						body = body + "\n\t\t\"" + headerArr[0] + "\": \"" + headerArr[1].trim() + "\",";
//
//					}
//				}
//				if (clientRequest.getHasInlineData()) {
//					body = body + "\n\t\t\"Content-Length\": \"" + clientRequest.getInlineData().length() + "\",";
//				} else if (clientRequest.getTransferSuc()) {
//					body = body + "\n\t\t\"Content-Length\": \"" + clientRequest.getFileSendData().length() + "\",";
//				}
//				body = body + "\n\t\t\"Connection\": \"close\",\n";
//				body = body + "\t\t\"Host\": \"" + host + "\"\n";
//				body = body + "\t},\n";
//
//				if (method.equalsIgnoreCase("POST")) {
//					body = body + "\t\"json\": ";
//					if (clientRequest.getHasInlineData()) {
//						body = body + "{\n\t\t " + inlineData.substring(1, inlineData.length() - 1) + "\n\t},\n";
//					} else {
//						body = body + "{\n\t\t " + fileData + "\n\t},\n";
//					}
//				}
//				body = body + "\t\"origin\": \"" + InetAddress.getLocalHost().getHostAddress() + "\",\n";
//				body = body + "\t\"url\": \"" + url + "\"\n";
//				body = body + "}";
//
//				serverResponse.setResponseBody(body);
//				
//				if (temp)
//					System.out.println("Sending the response to Client ======>");
//
//				// write object to Socket
//				output.writeObject(serverResponse);
//				// close resources
//				input.close();
//				output.close();
//				serverSocket.close();
//
//			}
			
		}
	}
	
	private static List<String> getFilesFromDir(File currentFolder) {
		List<String> filelist = new ArrayList<>();
		for (File file : currentFolder.listFiles()) {
			if (!file.isDirectory()) {
				filelist.add(file.getName());
			}
		}
		return filelist;
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
