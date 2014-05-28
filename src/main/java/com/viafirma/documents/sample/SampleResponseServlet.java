package com.viafirma.documents.sample;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viafirma.documents.sforce.SFClient;
import com.viafirma.documents.sforce.SFClientException;
import com.viafirma.documents.sforce.model.SFDocument;
import com.viafirma.mobile.services.sdk.java.model.Item;
import com.viafirma.mobile.services.sdk.java.model.Message;
import com.viafirma.mobile.services.sdk.java.model.Policy;

public class SampleResponseServlet extends HttpServlet {

	private static final long serialVersionUID = -6172262676810575377L;

	SFClient client;

	private Logger log = Logger.getLogger(SampleResponseServlet.class.getName());

	@Override
	public void init() throws ServletException {
		super.init();
		try {
			client = SFClient.getInstance();
		} catch (SFClientException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
		doService(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doPost(req, resp);
		doService(req, resp);
	}

	private void doService(HttpServletRequest req, HttpServletResponse resp) {
		if (req.getParameter("message") != null) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				Message message = mapper.readValue(req.getParameter("message"), Message.class);
				messageReceived(message);
			} catch (Exception e) {
				log.log(Level.SEVERE, "Error parsing message JSON.", e);
			}
		} else {
			log.log(Level.WARNING, "Empty request received.");
		}
	}

	public void messageReceived(Message message) {
		System.out.println("Message workflow ended \nID:" + message.getCode());

		// Get Account Name
		String accountName = "";
		List<Item> items = message.getDocument().getItems();
		for (Item item : items) {
			if (item.getKey().equals("KEY_01")) {
				accountName = item.getValue();
			}
		}

		String idSign = null;
		for (Policy policy : message.getPolicies()) {
			// Get last idSign
			if (policy.getIdSign() != null) {
				idSign = policy.getIdSign();
			}
		}

		// Create new sforce Document
		SFDocument document = new SFDocument();
		document.setName("Sample Document for " + accountName);
		document.setFolderId(SampleConfig.SF_FOLDER_ID);
		document.setDescription("Sample Documento for " + accountName);
		document.setUrl(SampleConfig.VIAFIRMA_URL + "/v/" + idSign + "?d=true");

		try {
			SFClient.getInstance().create(document);
		} catch (SFClientException e) {
			e.printStackTrace();
		}
	}

}
