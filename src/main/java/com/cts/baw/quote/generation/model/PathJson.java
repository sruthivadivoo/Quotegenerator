package com.cts.baw.quote.generation.model;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class PathJson {
	@ApiModelProperty(notes = "Name of the fPath",name="folderPath",required=true,value="test fPath")
	private String folderPath;
	@ApiModelProperty(notes = "Name of the ssn",name="ssn",required=true,value="test ssn")
	private String ssn;
	@ApiModelProperty(notes = "Name of the Fname",name="firstName",required=true,value="test Fname")
	private String firstName;
	@ApiModelProperty(notes = "Name of the Sname",name="lastName",required=true,value="test Sname")
	private String lastName;
	@ApiModelProperty(notes = "Name of the DOB",name="email",required=true,value="test DOB")
	private String email;
	@ApiModelProperty(notes = "Name of the Amount",name="beneName",required=true,value="test Amount")
	private String beneName;
	@ApiModelProperty(notes = "Name of the Sname",name="beneType",required=true,value="test Sname")
	private String beneType;
	@ApiModelProperty(notes = "Name of the DOB",name="brokerName",required=true,value="test DOB")
	private String brokerName;
	@ApiModelProperty(notes = "Name of the Amount",name="beneAmount",required=true,value="test Amount")
	private float beneAmount;	
	/*PathJson(String fPath){
		super();
	this.fPath=fPath;	
		
	}*/
	public String getFolderPath() {
		return folderPath;
	}
	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getBeneName() {
		return beneName;
	}
	public void setBeneName(String beneName) {
		this.beneName = beneName;
	}
	public String getBeneType() {
		return beneType;
	}
	public void setBeneType(String beneType) {
		this.beneType = beneType;
	}
	public String getBrokerName() {
		return brokerName;
	}
	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}
	public float getBeneAmount() {
		return beneAmount;
	}
	public void setBeneAmount(float beneAmount) {
		this.beneAmount = beneAmount;
	}
	@Override
	public String toString() {
		return "PathJson [folderPath=" + folderPath + ", ssn=" + ssn + ", firstName=" + firstName + ", lastName="
				+ lastName + ", email=" + email + ", beneName=" + beneName + ", beneType=" + beneType + ", brokerName="
				+ brokerName + ", beneAmount=" + beneAmount + "]";
	}

	
}
