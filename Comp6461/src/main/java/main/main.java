package main;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class main {
	private static final String init = "httpc";
	private static HttpClient req=new HttpClient(); 
	
public static void main(String args[]) throws URISyntaxException, UnknownHostException, IOException {
	
		HttpClient client = new HttpClient();
		System.out.println("Enter the command you want to execute.");
		Scanner sc=new Scanner(System.in);
		String cURL=sc.nextLine();
		client.processRequest(cURL);
		
		sc.close();
	}
}

//httpc post -h Content-Type:application/json --d '{"Assignment": 1}' http://httpbin.org/post