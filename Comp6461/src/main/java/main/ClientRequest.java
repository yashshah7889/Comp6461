package main;

public class ClientRequest {
	private String httpRequest;
	
	ClientRequest(){
		System.out.println("hello");
	}
	
	public void setHttpRequest(String cURL) {
		cURL=httpRequest;
		
	}
	public String getHttpRequest() {
		return httpRequest;
	}
	
}
