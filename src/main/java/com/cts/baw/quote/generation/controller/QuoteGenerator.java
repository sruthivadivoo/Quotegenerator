package com.cts.baw.quote.generation.controller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import javax.security.auth.Subject;

import org.apache.commons.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cts.baw.quote.generation.model.PathJson;
import com.filenet.api.collection.ContentElementList;
import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Connection;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.filenet.api.property.Properties;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.util.Id;
import com.filenet.api.util.UserContext;
import com.ibm.casemgmt.api.Case;
import com.ibm.casemgmt.api.CaseType;
import com.ibm.casemgmt.api.constants.ModificationIntent;
import com.ibm.casemgmt.api.exception.CaseMgmtException;
import com.ibm.casemgmt.api.objectref.ObjectStoreReference;
import com.ibm.casemgmt.api.properties.CaseMgmtProperties;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
@Component
public class QuoteGenerator {
	
	@Value("${ce.url}")
	private String ceUrl;
	@Value("${ce.username}")
	private String userName;
	@Value("${ce.password}")
	private String password;
	
	private static Connection connection;
	private static Domain domain;
	private static ObjectStore objectStore;
	private static UserContext context;
	public ObjectStore getCeConnection() {
		System.out.println("ExecutableClass.getCeConnection()");
		System.out.println("CE URL:"+ceUrl+"USERNAME"+userName+"PASSWORD"+password);
		String ceUri = ceUrl;
		String username = userName;
		String pwd = password;
		connection = Factory.Connection.getConnection(ceUri);
		Subject subject = UserContext.createSubject(connection, username, pwd, null);
		context = UserContext.get();
		context.pushSubject(subject);
		domain = Factory.Domain.getInstance(connection, null);
		objectStore = Factory.ObjectStore.fetchInstance(domain, "tos", null);
		System.out.println(objectStore.get_SymbolicName());
		System.out.println(objectStore.get_DisplayName() + "\t" + objectStore.get_DatabaseSchemaName());
		return objectStore;
	}

	public JSONObject retrieveDocument(String folderId,String firstName,String lastName,String ssn,String email,String beneName,String beneType,float beneAmount,String brokerName) throws Exception {
		JSONObject jsonObject = new JSONObject();
		String docID=null;
		//String lastName="lname";
		//JSONObject caseData
		String documentClass="RequestQuotation";
		String METHOD_NAME = "retrieveDocument";
		System.out.println(METHOD_NAME);
		
		ObjectStore targetOS = getCeConnection();
		ObjectStoreReference targetOsRef = new ObjectStoreReference(targetOS);
		String passingQuery = "SELECT * from " + documentClass + " where DocumentTitle='RequestQuotation.pdf'";
		System.out.println("Template retrieval Query : " + passingQuery);
		SearchScope searchScope = new SearchScope(targetOS);
		SearchSQL searchSQL = new SearchSQL(passingQuery);
		IndependentObjectSet independentObjectSet = searchScope.fetchObjects(searchSQL, new Integer(10), null, new Boolean(true));
		if (!(independentObjectSet.isEmpty())) {
			Iterator<Document> iterator = independentObjectSet.iterator();
			while (iterator.hasNext()) {
				Document document = iterator.next();
				Properties properties = document.getProperties();
				ContentElementList contentElementList = document.get_ContentElements();
				Iterator<ContentTransfer> itr = contentElementList.iterator();
				while (itr.hasNext()) {
					ContentTransfer ct = itr.next();
					InputStream stream = ct.accessContentStream();
					ByteArrayOutputStream bout = new ByteArrayOutputStream();
					byte[] buffer = new byte[4096000];
					int bytesRead = 0;
					while ((bytesRead = stream.read(buffer)) != -1) {
						bout.write(buffer, 0, bytesRead);
					}
					// writing to the pdf
					
					jsonObject=manipulatePdf(targetOS, bout.toByteArray(), targetOsRef,documentClass,folderId,firstName,lastName,ssn,email,beneName,beneType,beneAmount,brokerName);
					stream.close();
				}
			}
		} else {
			System.out.println("Document search is not happening, please check the document class and Title of the document in the query.");
		}
		//System.out.println("ExecutableClass.retrieveDocument()"+docDetails[0]+"\t"+docDetails[1]);
		return jsonObject;
	}

	private JSONObject manipulatePdf(ObjectStore objectStore, byte[] byteArr, ObjectStoreReference objectStoreReference,String documentClass,String folderId,String firstName,String lastName,String ssn,String email,String beneName,String beneType,float beneAmount,String brokerName ) {
		JSONObject jsonObject = new JSONObject();
		String docID = null;
		
		String METHOD_NAME = "manipulatePdf";
		System.out.println(METHOD_NAME);

		String Amount=Float.toString(beneAmount);
				// Initialize PDF document
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		PdfDocument pdf;
		try {
			pdf = new PdfDocument(new PdfReader(new ByteArrayInputStream(byteArr)), new PdfWriter(bout));
			PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, true);
		    
		    form.getFormFields().get("firstName").setValue(firstName).setFontSize(12);
			form.getFormFields().get("lastName").setValue(lastName).setFontSize(12);
			form.getFormFields().get("ssn").setValue(ssn).setFontSize(12);
			form.getFormFields().get("email").setValue(email).setFontSize(12);
			form.getFormFields().get("beneName").setValue(beneName).setFontSize(12);
			form.getFormFields().get("beneAmount").setValue(Amount).setFontSize(12);
			form.getFormFields().get("beneType").setValue(beneType).setFontSize(12);
			form.getFormFields().get("brokerName").setValue(brokerName).setFontSize(12);
			pdf.close();
			//
			
			/* jsonObject = fileToCaseFolder(objectStore, bout,folderId , documentClass); */
			jsonObject=fileToCaseFolder(objectStore, bout,folderId , documentClass);
			//return docId;
		} catch (IOException e) {
			System.out.println("Exception Occured!");
			System.err.println(e.getMessage());
			System.out.println(e.getMessage());
		}
		//System.out.println("ExecutableClass.manipulatePdf()"+docDetails[0]+"\t"+docDetails[1]);;
		return jsonObject;
		
	}

	private JSONObject fileToCaseFolder(ObjectStore objectStore, ByteArrayOutputStream bout, String folderID, String documentClass) {
		String METHOD_NAME = "fileToCaseFolder";
		System.out.println(METHOD_NAME);
		try {
			Document document = Factory.Document.createInstance(objectStore, documentClass);
			ContentElementList contentList = Factory.ContentElement.createList();
			ContentTransfer content = Factory.ContentTransfer.createInstance();
			InputStream is = new ByteArrayInputStream(bout.toByteArray());
			content.setCaptureSource(is);
			contentList.add(content);
			document.set_ContentElements(contentList);
			document.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
			Properties properties = document.getProperties();
			properties.putValue("DocumentTitle", "Quotation");
			document.set_MimeType("application/pdf");
			document.save(RefreshMode.REFRESH);
			System.out.println("Filing PDF file to : " + folderID);
			
			Folder folder = Factory.Folder.fetchInstance(objectStore, new Id(folderID), null);
			//Folder folder = Factory.Folder.fetchInstance(objectStore, folderPath, null);
			ReferentialContainmentRelationship ref = folder.file(document, AutoUniqueName.AUTO_UNIQUE, "Testing", null);
			ref.save(RefreshMode.REFRESH);
			System.out.println("PDF filed in CaseFolder.");
			JSONObject jsonObject = new JSONObject();
			
			System.out.println("Path Extension: "+folder.get_Name()+"\t"+folder.get_DateCreated());
			String pathWithDate = folder.get_FolderName();
			System.out.println(pathWithDate);
			String localPdfPath = fileToLocalFolder(folder.get_FolderName().toString(),bout);
			
			String docID=document.get_Id().toString();

			jsonObject.put("documentId", docID);
			jsonObject.put("documentPath",localPdfPath);
			System.out.println("ExecutableClass.fileToCaseFolder()"+jsonObject.get("documentId")+"\t"+jsonObject.get("documentPath"));
			return jsonObject;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("ExecutableClass.fileToCaseFolder().Exception in case fodler");
			return null;
		}
	}
	private String fileToLocalFolder(String folderName,ByteArrayOutputStream bout) {
		System.out.println("ExecutableClass.fileToLocalFolder()");
	    String path=null;  	
		File dir = new File("C:/Quote/"+folderName);
	        // attempt to create the directory here
	        boolean successful = dir.mkdirs();
	        if(successful){
	          // creating the directory succeeded
	          System.out.println("directory was created successfully");
	        }
	        try(OutputStream outputStream = new FileOutputStream(dir+"/Quotation.pdf")) {
	            bout.writeTo(outputStream);
	            path=dir+"\\Quotation.pdf";
	        } catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	        System.out.println("Local Pdf Path:"+path);
	        return path;
	}

}