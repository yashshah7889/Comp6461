package main;

import java.util.List;

public class ClientRequest {
	private String httpRequest;
	private boolean hasVerbose;
	private boolean hasHeader;
	private String url;
	private List<String> listOfHeaders;
	
	ClientRequest(){
		System.out.println("hello");
	}
	
	public void setHttpRequest(String cURL) {
		cURL=httpRequest;
		
	}
	public String getHttpRequest() {
		return this.httpRequest;
	}

	public void setHasVerbose(boolean hasVerbose) {
		this.hasVerbose=hasVerbose;
		
	}
	public boolean isVerbosePreset() {
		return this.hasVerbose;
	}

	public void setRequestUrl(String url) {
		this.url= url;
	}

	public void setHasHeader(boolean hasHeader) {
		this.hasHeader= hasHeader;
	}

	public void setHeaders(List<String> listOfHeaders) {
		this.listOfHeaders= listOfHeaders;
	}

	public void setHasInlineData(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public void setInlineData(String string) {
		// TODO Auto-generated method stub
		
	}

	public void setTransferSuc(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public void setFileTransferPath(String string) {
		// TODO Auto-generated method stub
		
	}
	
}
