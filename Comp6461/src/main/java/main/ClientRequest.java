package main;

import java.util.List;

public class ClientRequest {
	private String httpRequest;
	private boolean hasVerbose;
	private boolean hasHeader;
	private String url;
	private List<String> listOfHeaders;
	private boolean hasInlineData;
	private String inlineData;
	private String filePath;
	private boolean transferSucc;
	
	ClientRequest(){
		System.out.println("hello");
	}
	
	public void setHttpRequest(String cURL) {
		httpRequest= cURL;
		
	}
	public String getHttpRequest() {
		return this.httpRequest;
	}

	public void setHasVerbose(boolean hasVerbose) {
		this.hasVerbose=hasVerbose;
		
	}
	
	public boolean hasVerbose() {
		return this.hasVerbose;
	}
	
	public void setRequestUrl(String url) {
		this.url= url;
	}

	public String getRequestUrl() {
		return url;
	}
	
	public void setHasHeader(boolean hasHeader) {
		this.hasHeader= hasHeader;
	}

	public void setHeaders(List<String> listOfHeaders) {
		this.listOfHeaders= listOfHeaders;
	}

	public void setHasInlineData(boolean hasInlineData) {
		this.hasInlineData= hasInlineData;
	}
	
	public boolean getHasInlineData() {
		return hasInlineData;
	}

	public void setInlineData(String inlineData) {
		this.inlineData= inlineData;
	}

	public String getInlineData() {
		return this.inlineData;
	}
	
	public void setTransferSuc(boolean transferSucc) {
		this.transferSucc= transferSucc;
	}

	public boolean getTransferSuc() {
		return transferSucc;
	}
	
	public void setFileTransferPath(String string) {
		
	}

	public String getFileTransferPath() {
		return null;
	}
	
	public void setRequestMethod(String string) {
		
	}


	public String getRequestMethod() {
		return null;
	}

	

	public boolean isHttpHeader() {
		return false;
	}


	public String getFilePath() {
		return this.filePath;
	}



}
