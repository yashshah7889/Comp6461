package ass2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

import main.ClientRequest;

public class ClientHttp {
	int track=0;
	static RequestClient req= new RequestClient();
	private static List<String> listOfHeaders= null;
	private  StringBuilder fd = null;
	boolean flag=true;
	
	private static Socket soc=null;
	static ResponseClient resp;
	static ObjectOutputStream output= null;
	static ObjectInputStream input=null;
	
	/**
	 * reading of the string and initial setup and checks.
	 * @param command
	 * @throws URISyntaxException
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	public void processRequest(String command) throws URISyntaxException, UnknownHostException, IOException, ClassNotFoundException{
		int redirectCount=0;
		int count=0;
		String query=null;
		// to enter the command till the right command not entered.
		while(flag){
			
			listOfHeaders =new ArrayList<String>();
			if(req.isRedirect() && redirectCount<=3) {
				redirectCount++;
				req.setHttpRequest("httpc" + " "+ req.getRequestMethod()+ " -v "+ req.getRedirectLocation());
				req.setRedirect(false);
			}else {
					if(count==0) {
						query=command;
						if(query.equalsIgnoreCase("stop")) {
							System.exit(0);							
						}
						count++;
					}else {
						System.out.println("Please enter the command");
						Scanner sc= new Scanner(System.in);
						query= sc.nextLine();
						if(query.equalsIgnoreCase("stop")) {
							System.exit(0);							
						}
					}

					req.setHttpRequest(query);
					if(req.getHttpRequest()==null || req.getHttpRequest().isEmpty() || req.getHttpRequest().equalsIgnoreCase("httpc")) {
						System.out.println("URL not valid please try again");
						continue;
					}
				}
			
			
			String reqSplit[]= req.getHttpRequest().split(" ");
			List<String> listOfReqData= Arrays.asList(reqSplit);
			
			//will check here for the help command
			if(listOfReqData.contains("help")) {
				if(listOfReqData.contains("get")) {
					System.out.println("usage: httpc get [-v] [-h key:value] URL\r\n"
							+ "Get executes a HTTP GET request for a given URL.\r\n"
							+ "-v Prints the detail of the response such as protocol, status,\r\n"
							+ "and headers.\r\n"
							+ "-h key:value Associates headers to HTTP Request with the format\r\n"
							+ "'key:value'.");
				}else if(listOfReqData.contains("post")) {
					System.out.println("usage: httpc post [-v] [-h key:value] [-d inline-data] [-f file] URL\r\n"
							+ "Post executes a HTTP POST request for a given URL with inline data or from file.\r\n"
							+ "-v Prints the detail of the response such as protocol, status, and headers.\r\n"
							+ "-h key:value Associates headers to HTTP Request with the format 'key:value'.\r\n"
							+ "-d string Associates an inline data to the body HTTP POST request.\r\n"
							+ "-f file Associates the content of a file to the body HTTP POST request.\r\n"
							+ "Either [-d] or [-f] can be used but not both.");
				}else {
					System.out.println("httpc input a curl-like application but supports HTTP protocol only.\r\n"
							+ "Usage:\r\n"
							+ "httpc command [arguments]\r\n"
							+ "The commands are:\r\n"
							+ "get executes a HTTP GET request and prints the response.\r\n"
							+ "post executes a HTTP POST request and prints the response.\r\n"
							+ "help prints this screen.\r\n"
							+ "Use \"httpc help [command]\" for more information about a command.");
				}
			}
			
			//validation of conditions for get and post
			else if(listOfReqData.get(0).contains("httpc") && (listOfReqData.get(1).contains("get") || listOfReqData.get(1).contains("post"))) {
				if(listOfReqData.get(1).contains("get") && (listOfReqData.contains("-d") || listOfReqData.contains("-f") || listOfReqData.contains("--d"))) {
					System.out.println("get option should not be used with the options -d or -f");
					continue;
				}
				
				if(listOfReqData.get(1).contains("post") &&( listOfReqData.contains("-f") &&  (listOfReqData.contains("-d") || listOfReqData.contains("--d")))){
					System.out.println("post should have either -d or -f but not both");
					continue;
				}
				
				parseRequestQuery(listOfReqData);
				
				URI uri = new URI(req.getRequestUrl());
				String host = uri.getHost();

				// establish soc connection to server
				soc = new Socket(host, uri.getPort());
				// write to soc using ObjectOutputStream
				output = new ObjectOutputStream(soc.getOutputStream());
				input = new ObjectInputStream(soc.getInputStream());
				System.out.println("Sending request to Socket Server");
				output.writeObject(req);

				// read the server response message
				resp = (ResponseClient) input.readObject();

				if (req.isFileWrite()) {

					// Method call for write response in file
					printInFile(resp);

				} else {

					// Method call for printing response in console
					displayResponse(resp);

				}
				
				output.close();
				input.close();
				
			}else {
				System.out.println("Invalid URL please. Provide valid httpc get or httpc post URL");
			}
			//flag=false;
		}
	}


/**
 * method for displaying response in console
 * @param reader buffer reader
 * @param status status of thr command
 * @throws IOException
 */
public void displayResponse(ResponseClient resp) throws IOException {
	
		if(req.hasVerbose()) {
			System.out.println(resp.getResponseHeader());
			System.out.println(resp.getResponseBody());
		}else {
			System.out.println(resp.getResponseBody());
		}
	}
	
/**
 * method the print the output in file
 * @param reader
 * @param status
 * @throws IOException
 */
	public void printInFile(ResponseClient resp) throws IOException {
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(req.getFileWritePath(), true));
		PrintWriter pw = new PrintWriter(bw);
		String line;
		if(req.hasVerbose()) {
			pw.println(resp.getResponseHeader());
			pw.println(resp.getResponseBody());
		}else {
			pw.println(resp.getResponseBody());
		}
		
		System.out.println("Response has been stored successfully in ( "+ req.getFileWritePath()+ " ) File path");
		pw.flush();
		pw.close();
		
	}
	
	/**
	 * method to parse the request query.
	 * @param reqData
	 * @throws URISyntaxException
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public void parseRequestQuery(List<String> reqData) throws URISyntaxException, UnknownHostException, IOException {
		req= new RequestClient();
		//checking the conditions for different parameters of query
		int i=0;
		while(i<reqData.size()) {
			if(reqData.get(i).equalsIgnoreCase("-v")) {
				req.setHasVerbose(true);
			}else if(reqData.get(i).startsWith("\'http://") || reqData.get(i).startsWith("\'https://")) {
				req.setRequestUrl(reqData.get(i).replaceAll("^\'|\'$", ""));
			}else if(reqData.get(i).startsWith("http://") || reqData.get(i).startsWith("https://")) {		
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
			}else if (reqData.get(i).equals("-o")) {

				req.setFileWrite(true);
				req.setFileWritePath(reqData.get(i + 1));

			}
		i++;	
		}

		req.setRequestMethod(reqData.get(1));

		// -d : inline data
		if(req.getHasInlineData()) {
			if(req.getInlineData().contains("\'")) {
				req.setInlineData(req.getInlineData().replace("\'", ""));
			}

		
		//-f : sending file data
		}else if(req.getTransferSuc()) {
			
			File fsend = new File(req.getFileTransferPath());
			BufferedReader bf = new BufferedReader(new FileReader(fsend));
			fd=new StringBuilder();
			String str;
			while((str =bf.readLine())!= null) {
				fd.append(str);
			}


			bf.close();
			req.setFileSendData(fd.toString());
		}
		
	}
}