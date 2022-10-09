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
	private String fileTransferPath;
	private String requestMethod;
	
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
	public boolean isHttpHeader() {
		return this.hasHeader;
	}
	
	public void setHeaders(List<String> listOfHeaders) {
		this.listOfHeaders= listOfHeaders;
	}
	public List<String> getHeaderLst() {
		return listOfHeaders;
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
	
	public void setFileTransferPath(String fileTransferPath) {
		this.fileTransferPath=fileTransferPath;
	}
	public String getFileTransferPath() {
		return this.fileTransferPath;
	}
	
	public void setRequestMethod(String requestMethod) {
		this.requestMethod=requestMethod;
	}
	public String getRequestMethod() {
		return this.requestMethod;
	}

	public String getFilePath() {
		return this.filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}



}
