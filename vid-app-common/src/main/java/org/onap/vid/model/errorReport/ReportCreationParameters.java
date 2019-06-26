package org.onap.vid.model.errorReport;

public class ReportCreationParameters {
	private String requestId;
	private String serviceUuid;

	public ReportCreationParameters() {}

	public ReportCreationParameters(String requestId, String serviceUuid) {
		this.requestId = requestId;
		this.serviceUuid = serviceUuid;
	}

	public String getRequestId() {
		return requestId;
	}

	public String getServiceUuid() {
		return serviceUuid;
	}
}

