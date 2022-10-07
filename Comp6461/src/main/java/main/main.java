package main;

import java.util.Scanner;

public class main {
	private static final String init = "httpc";
	private static HttpClient req=new HttpClient(); 
	
public static void main(String args[]) {
	while(true) {
		System.out.println("Enetr the command you want to execute.");
		Scanner sc=new Scanner(System.in);
		String cURL=sc.nextLine();
		req.setHttpRequest(cURL);
	
		if(req.getHttpRequest()==null || req.getHttpRequest().isEmpty()) {
			System.out.println("URL not valid please try again");
			continue;
		}
	}
}
}
