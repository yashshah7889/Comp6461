package main;

public class HttpClient {
	private String httpRequest;
	
	HttpClient(){
		System.out.println("hii");
	}
	public void setHttpRequest(String cURL) {
		cURL=httpRequest;
		
	}
	public String getHttpRequest() {
		return httpRequest;
	}
}
