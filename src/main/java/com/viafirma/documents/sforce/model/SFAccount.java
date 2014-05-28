package com.viafirma.documents.sforce.model;

import com.sforce.soap.partner.sobject.SObject;
import com.viafirma.documents.sforce.client.SFModel;

public class SFAccount implements SFModel {
	
	private static final String TYPE = "Account";
	
	String id;
	String name;
	String personEmail;
	String phone;

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPersonEmail() {
		return personEmail;
	}
	
	public void setPersonEmail(String personEmail) {
		this.personEmail = personEmail;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getType() {
		return TYPE;
	}
	
	public void populate(SObject sObject) {
		setId((String)sObject.getField("Id"));
		setName((String)sObject.getField("Name"));
		setPersonEmail((String)sObject.getField("PersonEmail"));
		setPhone((String)sObject.getField("Phone"));
	}

	public SObject getSObject() {
		SObject sObject = new SObject();
		sObject.setType(getType());
		sObject.setField("Id", getId());
		sObject.setField("Name", getName());
//		sObject.setField("PersonEmail", getPersonEmail());
		sObject.setField("Phone", getPhone());
		return sObject;
	}
}
