package ass2;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Scanner;

import main.HttpClient;

public class MainStart {

	public static void main(String args[]) throws URISyntaxException, UnknownHostException, IOException {
	
		//main method to run the program
		ClientHttp client = new ClientHttp();
		System.out.println("Please enter the command");
		Scanner sc=new Scanner(System.in);
		String cURL=sc.nextLine();
		client.processRequest(cURL);
		
		sc.close();
	}
}
