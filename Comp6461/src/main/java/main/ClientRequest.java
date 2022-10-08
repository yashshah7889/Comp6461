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
	
	public boolean getHasInlineData() {
		// TODO Auto-generated method stub
		return false;
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

	public void setRequestMethod(String string) {
		// TODO Auto-generated method stub
		
	}

	public String getRequestUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getRequestMethod() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getInlineData() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean getTransferSuc() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getFileTransferPath() {
		// TODO Auto-generated method stub
		return null;
	}

	

	public boolean isHttpHeader() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean hasVerbose() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getFilePath() {
		// TODO Auto-generated method stub
		return null;
	}



}
