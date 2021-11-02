package com.cts.baw.quote.generation.controller;

import org.apache.commons.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cts.baw.quote.generation.model.DocDetailsJson;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "Swagger2DemoRestController")
@RestController
public class Swagger2DemoRestController {

	@Autowired
	QuoteGenerator ec;

	@ApiOperation(value = "PDF Genaration ", tags = "pdfGeneration")
	@RequestMapping(value = "/pdfGeneration/{fID}/{fname}/{lname}/{ssn}/{email}/{beneName}/{beneType}/{beneAmount}/{brokerName}", method = RequestMethod.POST, produces = {
			"application/json" })
	@ResponseBody
	public DocDetailsJson pdfGeneration(@PathVariable(value = "fID") String fID,
			@PathVariable(value = "fname") String fname, @PathVariable(value = "lname") String lname,
			@PathVariable(value = "ssn") String ssn, @PathVariable(value = "email") String email,
			@PathVariable(value = "beneName") String beneName, @PathVariable(value = "beneType") String beneType,
			@PathVariable(value = "beneAmount") float beneAmount, @PathVariable(value = "brokerName") String brokerName)
			throws Exception {

		DocDetailsJson DocDetails = new DocDetailsJson();
		JSONObject jsonOutput = new JSONObject();

		jsonOutput = ec.retrieveDocument(fID, fname, lname, ssn, email, beneName, beneType, beneAmount, brokerName);
		String documentPath = jsonOutput.get("documentPath").toString();
		String docID = jsonOutput.get("documentId").toString();

		DocDetails.setDocPath(documentPath);
		DocDetails.setDocID(docID);

		System.out.println("Swagger2DemoRestController.DemoMethod()" + jsonOutput.get("documentPath") + "\t"
				+ jsonOutput.get("documentId"));
		return DocDetails;

	}

}
