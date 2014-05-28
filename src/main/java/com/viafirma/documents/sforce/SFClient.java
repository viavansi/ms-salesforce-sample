package com.viafirma.documents.sforce;

import java.util.ArrayList;
import java.util.List;

import com.sforce.soap.partner.Error;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import com.viafirma.documents.sample.SampleConfig;
import com.viafirma.documents.sforce.client.SFModel;

/**
 * Salesforce Sample Client
 */
public class SFClient {

	PartnerConnection partnerConnection = null;
	static SFClient instance;
	static long lastSessionTime = 0;
	
	public static final int TIMEOUT = 300000;
	
	private boolean login() {
		boolean success = false;

		try {
			ConnectorConfig config = new ConnectorConfig();
			config.setUsername(SampleConfig.SF_USERNAME);
			config.setPassword(SampleConfig.SF_PASSWORD);
			config.setAuthEndpoint(SampleConfig.SF_AUTHENDPOINT);

			partnerConnection = new PartnerConnection(config);

			success = true;
		} catch (ConnectionException ce) {
			ce.printStackTrace();
		}

		return success;
	}

	public static SFClient getInstance() throws SFClientException {
		if (instance == null) {
			instance = new SFClient();
		}
		
		if (System.currentTimeMillis() - lastSessionTime > TIMEOUT) {
			instance.login();
			lastSessionTime = System.currentTimeMillis();
		}
		return instance;
	}

	public <T extends SFModel> List<T> queryAll(String queryString) throws SFClientException {
		try {
			List<T> resultList = new ArrayList<T>();
			partnerConnection.setQueryOptions(250);
			QueryResult qr = partnerConnection.query(queryString);

			boolean done = false;
			while (!done) {
				SObject[] records = qr.getRecords();

				for (SObject record : records) {
					@SuppressWarnings("unchecked")
					T object = (T) Class.forName("com.viafirma.documents.sforce.model.SF" + record.getType()).newInstance();
					object.populate(record);
					resultList.add(object);
				}
				if (qr.isDone()) {
					done = true;
				} else {
					qr = partnerConnection.queryMore(qr.getQueryLocator());
				}
			}
			return resultList;
		} catch (Exception e) {
			throw new SFClientException(e);
		}
	}

	public <T extends SFModel> T queryOne(String queryString) throws SFClientException {
		try {

			partnerConnection.setQueryOptions(1);
			QueryResult qr = partnerConnection.query(queryString);
			SObject[] records = qr.getRecords();

			if (records.length != 0) {
				return null;
			}

			SObject record = records[0];
			@SuppressWarnings("unchecked")
			T object = (T) Class.forName("com.viafirma.documents.sforce.model.SF" + record.getType()).newInstance();
			object.populate(record);

			return object;
		} catch (Exception e) {
			throw new SFClientException(e);
		}
	}

	public boolean create(SFModel model) throws SFClientException {
		try {

			SObject[] objects = new SObject[] { model.getSObject() };
			SaveResult[] results = partnerConnection.create(objects);

			String result;
			for (int j = 0; j < results.length; j++) {
				if (results[j].isSuccess()) {
					result = results[j].getId();
					System.out.println("\nA Object was created with an ID of: " + result);
				} else {
					for (int i = 0; i < results[j].getErrors().length; i++) {
						Error err = results[j].getErrors()[i];
						System.out.println("Errors were found on item " + j);
						System.out.println("Error code: " + err.getStatusCode().toString());
						System.out.println("Error message: " + err.getMessage());
					}
				}
			}

			return results[0].isSuccess();
		} catch (Exception e) {
			throw new SFClientException(e);
		}
	}
}
