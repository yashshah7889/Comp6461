package ass2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RequestClient {
	private String httpRequest;
	private boolean hasVerbose;
	private boolean hasHeader;
	private String url;
	private List<String> listOfHeaders=new ArrayList<String>();
	private boolean hasInlineData;
	private String inlineData;
	private String filePath;
	private boolean transferSucc;
	private String fileTransferPath;
	private String requestMethod;
	private boolean isFileWrite;
	private String fileWritePath;
	private boolean isRedirect;
	private String redirectLocation;
	private String clientType;
	private String fileSendData;
	
	
	//class contains all the getter and setter methods required in HttpClient
	public void setRequestUrl(String url) {
		this.url= url;
	}
	public String getRequestUrl() {
		return url;
	}
	
	public void setInlineData(String inlineData) {
		this.inlineData= inlineData;
	}
	public String getInlineData() {
		return this.inlineData;
	}
	
	public String getRedirectLocation() {
		return redirectLocation;
	}
	public void setRedirectLocation(String redirectLocation) {
		this.redirectLocation = redirectLocation;
	}
	
	public void setRequestMethod(String requestMethod) {
		this.requestMethod=requestMethod;
	}
	public String getRequestMethod() {
		return this.requestMethod;
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
	
	public void setHasHeader(boolean hasHeader) {
		this.hasHeader= hasHeader;
	}
	public boolean isHttpHeader() {
		return this.hasHeader;
	}
	
	public void setHasInlineData(boolean hasInlineData) {
		this.hasInlineData= hasInlineData;
	}
	public boolean getHasInlineData() {
		return hasInlineData;
	}
	
	public void setTransferSuc(boolean transferSucc) {
		this.transferSucc= transferSucc;
	}
	public boolean getTransferSuc() {
		return transferSucc;
	}

	public boolean isFileWrite() {
		return isFileWrite;
	}
	public void setFileWrite(boolean isFileWrite) {
		this.isFileWrite = isFileWrite;
	}
	
	public void setFileTransferPath(String fileTransferPath) {
		this.fileTransferPath=fileTransferPath;
	}
	public String getFileTransferPath() {
		return this.fileTransferPath;
	}
	
	public void setFileWritePath(String fileWritePath) {
		this.fileWritePath=fileWritePath;
	}
	public String getFileWritePath() {
		return this.fileWritePath;
	}
	
	public boolean isRedirect() {
		return isRedirect;
	}
	public void setRedirect(boolean isRedirect) {
		this.isRedirect = isRedirect;
	}
	
	public void setHeaders(List<String> listOfHeaders) {
		this.listOfHeaders= listOfHeaders;
	}
	public List<String> getHeaderLst() {
		return listOfHeaders;
	}
	
	public String getClientType() {
		return this.clientType;
	}
	public void setClientType(String clientType) {
		this.clientType= clientType;
	}
	
	public String getFileSendData() {
		return fileSendData;
	}
	public void setFileSendData(String fileSendData) {
		this.fileSendData = fileSendData;
	}

}
