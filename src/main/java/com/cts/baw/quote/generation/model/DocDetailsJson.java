package com.cts.baw.quote.generation.model;

import io.swagger.annotations.ApiModelProperty;

public class DocDetailsJson {
	public String getDocID() {
		return docID;
	}
	public void setDocID(String docID) {
		this.docID = docID;
	}
	@ApiModelProperty(notes = "Name of the docID",name="docID",required=true,value="test docID")
     private  String docID;
	@ApiModelProperty(notes = "Name of the docPath",name="docPath",required=true,value="test docPath")	
    private String docPath;
@Override
public String toString() {
	return "DocDetailsJson [docID=" + docID + ", docPath=" + docPath + "]";
}
public String getDocPath() {
	return docPath;
}
public void setDocPath(String docPath) {
	this.docPath = docPath;
}
}
