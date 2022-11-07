package ass2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

public class ClientFile {
	static RequestClient req= new RequestClient();
	private static List<String> listOfHeaders= null;
	private static Socket socket=null;
	static ResponseClient resp;
	static ObjectOutputStream os= null;
	static ObjectInputStream is=null;
	
	public static void main(String[] args) throws URISyntaxException, UnknownHostException, IOException, ClassNotFoundException {
		//user working directory
		String directory = System.getProperty("user.dir");
		File file = new File("attachment");
		file.mkdir();
		while(true) {
			listOfHeaders = new ArrayList();
			String fileReq="";
			System.out.println("Enter File transfer Command:");
			Scanner sc = new Scanner(System.in);
			fileReq = sc.nextLine();
			if (fileReq.length() == 0 || fileReq.isEmpty() || (fileReq.contains("post") && !fileReq.contains("-d"))) {
				System.out.println("Invalid Command or Please enter POST url with inline data");
				continue;
			}
			
			String reqSplit[] = fileReq.split(" ");
			reqSplit[0] = "httpfs";
			List<String> listOfReqData = Arrays.asList(reqSplit);
			String reqUrl = "";
			if (fileReq.contains("post")) {
				reqUrl = listOfReqData.get(2);

			} else {
				reqUrl = listOfReqData.get(listOfReqData.size() - 1);
			}
			
			if (reqUrl.contains("\'")) {
				reqUrl = reqUrl.replace("\'", "");
			}
			
			req.setHttpRequest(fileReq);
			parseFileRequest(listOfReqData);
			
			URI uri = new URI(req.getRequestUrl());
			String host = uri.getHost();
			// establish socket connection to server
			socket = new Socket(host, uri.getPort());
			// write to socket using ObjectOutputStream
			os = new ObjectOutputStream(socket.getOutputStream());
			is = new ObjectInputStream(socket.getInputStream());
			System.out.println("Sending request to Socket Server");
			os.writeObject(req);
			
			os.writeObject("Test");

			String reqMethod = req.getRequestMethod();

			// read the server response message
			resp = (ResponseClient) is.readObject();
			if (reqMethod.equalsIgnoreCase("get/")) {

				System.out.println(resp.getResponseHeader());
				System.out.println(resp.getResponseBody());

			}else if (!reqMethod.endsWith("/") && reqMethod.contains("post/")) {

				System.out.println(resp.getResponseHeader());
				System.out.println(resp.getResponseBody());
				
			} else if (!reqMethod.endsWith("/") && reqMethod.contains("get/")) {
				if (fileReq.contains("Content-Disposition:attachment")) {

					String status = resp.getResponseCode();

					if (!status.equals("404")) {

						String fileData = resp.getResponseBody();
						String fileName = resp.getRequestFileName();

						file = new File(directory + "/attachment/" + fileName);
						file.createNewFile();

						BufferedWriter bw = new BufferedWriter(new FileWriter(file));
						PrintWriter pw = new PrintWriter(bw);

						pw.print(fileData);
						pw.flush();
						pw.close();
					}

					System.out.println(resp.getResponseHeader());
					System.out.println(resp.getResponseBody());

					if (!status.equals("404"))
						System.out.println("File downloaded in " + directory + "/attachment");
					} else {
						System.out.println(resp.getResponseHeader());
						System.out.println(resp.getResponseBody());
					}
			}
			

			os.flush();
			os.close();
			
		}
	}

	private static void parseFileRequest(List<String> reqData) {
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
		
		String fileClientReq = req.getHttpRequest();
		
		String[] fileReqSplit = fileClientReq.split("\\s+");
		int j=0;
		while(j<fileReqSplit.length){
			if(fileReqSplit[i].startsWith("http://"))
			{
				String[] methodCollection = fileReqSplit[i].split("/");
				if(methodCollection.length==4)
				{
					req.setClientType(reqData.get(0));
					req.setRequestMethod(methodCollection[3]+"/");
				}
				else if(methodCollection.length==5)
				{
					req.setClientType(reqData.get(0));
					String str = methodCollection[3]+"/"+methodCollection[4];
					req.setRequestMethod(str);
				}
			}
			i++;
		}
		if (req.getHasInlineData()) {
			if (req.getInlineData().contains("\'")) {

				req.setInlineData(req.getInlineData().replace("\'", ""));

			}

		}

	}
}
