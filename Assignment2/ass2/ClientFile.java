package ass2;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ClientFile {
	static RequestClient req= new RequestClient();
	private static List<String> listOfHeaders= null;
	private static Socket socket=null;
	static ResponseClient resp;
	static ObjectOutputStream os= null;
	static ObjectInputStream is=null;
	
	public static void main(String[] args) {
		//user working directory
		String directory = System.getProperty("user.dir");
		File file = new File("report");
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
					String a = methodCollection[3]+"/"+methodCollection[4];
					req.setRequestMethod(a);
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
