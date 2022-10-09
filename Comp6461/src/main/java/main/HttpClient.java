package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class HttpClient {
	ClientRequest req= new ClientRequest();
	int track=0;
	
	List<String> listOfHeaders= new ArrayList<String>();
	
	private static Socket socket=null;
	
	private static StringBuilder fd = null;
	
	HttpClient(){
		System.out.println("hii");
	}
	public void processRequest(String command) throws URISyntaxException, UnknownHostException, IOException{
		fd = new StringBuilder();
		
		// to enter the command till the right command not entered.
		while(true){
			if(track==0) {
				req.setHttpRequest(command);
				if(req.getHttpRequest()==null || req.getHttpRequest().isEmpty()) {
					System.out.println("URL not valid please try again");
					track++;
					continue;
				}	
			}else {
				Scanner sc= new Scanner(System.in);
				String query= sc.nextLine();
				req.setHttpRequest(query);
				if(req.getHttpRequest()==null || req.getHttpRequest().isEmpty()) {
					System.out.println("URL not valid please try again");
					continue;
				}
			}
			
			// String can be converted to lowercase here in case there is a mixture  of upper and lower case then it should be splitted
			String reqSplit[]= req.getHttpRequest().split(" ");
			List<String> listOfReqData= Arrays.asList(reqSplit);
			
			if(listOfReqData.contains("help")) {
				if(listOfReqData.contains("get")) {
					System.out.println("usage: httpc post [-v] [-h key:value] [-d inline-data] [-f file] URL\r\n"
							+ "Post executes a HTTP POST request for a given URL with inline data or from file.\r\n"
							+ "-v Prints the detail of the response such as protocol, status, and headers.\r\n"
							+ "-h key:value Associates headers to HTTP Request with the format 'key:value'.\r\n"
							+ "-d string Associates an inline data to the body HTTP POST request.\r\n"
							+ "-f file Associates the content of a file to the body HTTP POST request.\r\n"
							+ "Either [-d] or [-f] can be used but not both.");
				}else if(listOfReqData.contains("post")) {
					System.out.println("usage: httpc post [-v] [-h key:value] [-d inline-data] [-f file] URL\r\n"
							+ "Post executes a HTTP POST request for a given URL with inline data or from file.\r\n"
							+ "-v Prints the detail of the response such as protocol, status, and headers.\r\n"
							+ "-h key:value Associates headers to HTTP Request with the format 'key:value'.\r\n"
							+ "-d string Associates an inline data to the body HTTP POST request.\r\n"
							+ "-f file Associates the content of a file to the body HTTP POST request.\r\n"
							+ "Either [-d] or [-f] can be used but not both.");
				}else {
					System.out.println("httpc is a curl-like application but supports HTTP protocol only.\r\n"
							+ "Usage:\r\n"
							+ "httpc command [arguments]\r\n"
							+ "The commands are:\r\n"
							+ "get executes a HTTP GET request and prints the response.\r\n"
							+ "post executes a HTTP POST request and prints the response.\r\n"
							+ "help prints this screen.\r\n"
							+ "Use \"httpc help [command]\" for more information about a command.");
				}
			}
			
			
			if(listOfReqData.get(0).contains("httpc") && (listOfReqData.get(1).contains("get") || listOfReqData.get(0).contains("post"))) {
				if(listOfReqData.get(1).contains("get") && (listOfReqData.contains("-d") || listOfReqData.contains("-f") || listOfReqData.contains("--d"))) {
					System.out.println("get option should not be used with the options -d or -f");
					continue;
				}
				
				if(listOfReqData.get(1).contains("post") && listOfReqData.contains("-f") &&  (listOfReqData.contains("-d") || listOfReqData.contains("--d"))) {
					System.out.println("post should have either -d or -f but not both");
					continue;
				}
				
				parseRequestQuery(listOfReqData);
				
			}
		}
	}

public void displayResponse(BufferedReader reader, String statusCode) throws IOException {
		
		if(req.hasVerbose()) {
			System.out.println(statusCode);
			while(reader.readLine()!=null) {
				System.out.println(reader.readLine());
				if(reader.readLine().equals("}")) {
					break;
				}
			}
		}else {
			boolean jsonPresent= false;
			while(reader.readLine()!=null) {
				if(reader.readLine().trim().equals("{")) {
					jsonPresent= true;
				}
				if(jsonPresent) {
					System.out.println(reader.readLine());
					if(reader.readLine().equals("}")) {
						break;
					}
				}
				
			}
		}
	}
	
	public void printInFile(BufferedReader reader, String statusCode) throws IOException {
		
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(req.getFilePath(), true));
		
		if(req.hasVerbose()) {
			System.out.println(statusCode);
			while(reader.readLine()!=null) {
				bufferedWriter.write(reader.readLine());
				if(reader.readLine().equals("}")) {
					break;
				}
			}
		}else {
			boolean jsonPresent= false;
			while(reader.readLine()!=null) {
				if(reader.readLine().trim().equals("{")) {
					jsonPresent= true;
				}
				if(jsonPresent) {
					bufferedWriter.write(reader.readLine());
					if(reader.readLine().equals("}")) {
						break;
					}
				}
				
			}
		}
		
		System.out.println("Response has been stored successfully in ( "+ req.getFilePath()+ " ) File path");
		bufferedWriter.close();
		bufferedWriter.flush();
	}
	
	public void parseRequestQuery(List<String> reqData) throws URISyntaxException, UnknownHostException, IOException {
		//reqSplit(string[])  listOfReqData(list)
		int i=0;
		while(i<reqData.size()) {
			if(reqData.get(i).equalsIgnoreCase("-v")) {
				req.setHasVerbose(true);
			}else if(reqData.get(i).substring(0, 7).equals("http://")||reqData.get(i).substring(0, 8).equals("https://")) {
				//splitiing of requesting in two parts domain and path after http://
				req.setRequestUrl(reqData.get(i));
			}else if(reqData.get(i).equals("-h")) {
				listOfHeaders.add(reqData.get(i+1));
				req.setHasHeader(true);
				req.setHeaders(listOfHeaders);
			}else if(reqData.get(i).equals("-d") || reqData.get(i).equals("--d")) {
				req.setHasInlineData(true);
				req.setInlineData(reqData.get(i+1));
			}else if(reqData.get(i).equals("-f")) {
				req.setTransferSuc(true);
				req.setFileTransferPath(reqData.get(i+1));
			}
			//-o is remaining
		i++;	
		}
		i=0;
		req.setRequestMethod(reqData.get(1));
		
		URI uri =new URI(req.getRequestUrl());
		String host = uri.getHost();
		socket = new Socket(host,80);
		//OutputStream opt= socket.getOutputStream();
		
		String pathWithoutProtocol = uri.getPath();
		String query= uri.getQuery();
		
		if(query !=null && pathWithoutProtocol != null) {
			
				if(query.length()>0 || pathWithoutProtocol.length()>0) {
					pathWithoutProtocol = pathWithoutProtocol + "?" +query;
				}
		}
		
		PrintWriter writer = new PrintWriter(socket.getOutputStream());
		//if condition changed
		if(pathWithoutProtocol.length() != 0) {
			writer.println(req.getRequestMethod().toUpperCase()+ " " + pathWithoutProtocol + " / HTTP/1.0");
		}else {
			writer.println(req.getRequestMethod().toUpperCase()+" / HTTP/1.0");
		}
		
		writer.print("Host: " + host+"\r\n");
		
		// for -d inline data
		if(req.getHasInlineData()) {
			if(req.getInlineData().contains("\'")) {
				req.setInlineData(req.getInlineData().replace("\'", ""));
			}
			writer.print("Content-Length: " + req.getInlineData().length() + "\r\n");
		
		//for -f fpr sending file data
		}else if(req.getTransferSuc()) {
			
			File fsend = new File(req.getFileTransferPath());
			BufferedReader bf = new BufferedReader(new FileReader(fsend));
			String str;
			while((str =bf.readLine())!= null) {
				fd.append(str);
			}
			writer.println("Content-Length: " + fd.length() +"\r\n");

			bf.close();
		}
		
		//-h for http header
		if (req.isHttpHeader()) {
			if (!listOfHeaders.isEmpty()) {

				for (int j = 0; j < listOfHeaders.size(); j++) {
					String headerdetails[] = listOfHeaders.get(j).split(":");
					writer.write(headerdetails[0] + ":" + headerdetails[1] + "\r\n");
				}
			}
		}
		
		if(req.getHasInlineData()) {
			writer.print("\r\n");
			writer.print(req.getInlineData());
			writer.print("\r\n");
		}else if (req.getTransferSuc()) {
			writer.print("\r\n");
			writer.print(fd.toString());
			writer.print("\r\n");
		} else {
			writer.print("\r\n");
		}
		writer.flush();
	}
	
}
