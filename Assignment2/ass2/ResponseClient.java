package ass2;

public class ResponseClient {
	
	private String responseHeaders;
	private String responseBody;
	private String requestFileName;
	private String responseMessage;
	private String responseCode;



	public void setResponseHeaders(String responseHeaders) {
		this.responseHeaders = responseHeaders;
	}

	public String getResponseHeader() {
		return this.responseHeaders;
	}
	
	public void setResponseBody(String body) {
		this.responseBody = body;
	}

	public String getResponseBody() {
		return this.responseBody;
	}
	
	public String getResponseCode() {
		return this.responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return this.responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getRequestFileName() {
		return this.requestFileName;
	}

	public void setRequestFileName(String requestFileName) {
		this.requestFileName = requestFileName;
	}
	
	@Override
	public String toString() {
		return "HttpClientResponse [responseHeaders=" + responseHeaders + ", responsebody=" + responseBody + ", responseCode="
				+ responseCode + ", responseMessage=" + responseMessage + ", requestFileName=" + requestFileName + "]";
	}

	
}
