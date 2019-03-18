package org.onap.sdc.ci.tests.datatypes;

public class LocalLoginFieldsIds {

	private String userIdFieldName;
	private String passwordFieldName;
	
	
	public LocalLoginFieldsIds(String userIdFieldName, String passwordFieldName) {
		super();
		this.userIdFieldName = userIdFieldName;
		this.passwordFieldName = passwordFieldName;
	}
	
	
	public String getUserIdFieldName() {
		return userIdFieldName;
	}
	public void setUserIdFieldName(String userIdFieldName) {
		this.userIdFieldName = userIdFieldName;
	}
	public String getPasswordFieldName() {
		return passwordFieldName;
	}
	public void setPasswordFieldName(String passwordFieldName) {
		this.passwordFieldName = passwordFieldName;
	}
	
	
}
