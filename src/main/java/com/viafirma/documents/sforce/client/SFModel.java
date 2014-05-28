package com.viafirma.documents.sforce.client;

import com.sforce.soap.partner.sobject.SObject;

public interface SFModel {
	
	public void populate(SObject sObject);
	public SObject getSObject();
	public String getType();

}
