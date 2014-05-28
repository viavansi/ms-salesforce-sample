package com.viafirma.documents.sforce.model;

import com.sforce.soap.partner.sobject.SObject;
import com.viafirma.documents.sforce.client.SFModel;

public class SFDocument implements SFModel {
	private static final String TYPE = "Document";
	
	String id;
	String name;
	String authorId;
	String description;
	String folderId;
	String url;
	
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

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return TYPE;
	}
	
	public void populate(SObject sObject) {
		setId((String)sObject.getField("Id"));
		setName((String)sObject.getField("Name"));
		setAuthorId((String)sObject.getField("AuthorId"));
		setDescription((String)sObject.getField("Description"));
		setFolderId((String)sObject.getField("FolderId"));
		setUrl((String)sObject.getField("Url"));
	}

	public SObject getSObject() {
		SObject sObject = new SObject();
		sObject.setType(getType());
		sObject.setField("Id", getId());
		sObject.setField("Name", getName());
		sObject.setField("Description", getDescription());
		sObject.setField("FolderId", getFolderId());
		sObject.setField("Url", getUrl());
		return sObject;
	}
}
