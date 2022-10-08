package main;

import java.util.Scanner;

public class main {
	private static final String init = "httpc";
	private static HttpClient req=new HttpClient(); 
	
public static void main(String args[]) {
	
		HttpClient client = new HttpClient();
		System.out.println("Enetr the command you want to execute.");
		Scanner sc=new Scanner(System.in);
		String cURL=sc.nextLine();
		client.processRequest(cURL);
	}
}