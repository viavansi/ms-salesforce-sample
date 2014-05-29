package com.viafirma.documents.sample;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.viafirma.documents.sforce.model.SFAccount;
import com.viafirma.mobile.services.sdk.java.api.V1Api;
import com.viafirma.mobile.services.sdk.java.model.Device;
import com.viafirma.mobile.services.sdk.java.model.Document;
import com.viafirma.mobile.services.sdk.java.model.Item;
import com.viafirma.mobile.services.sdk.java.model.Message;
import com.viafirma.mobile.services.sdk.java.model.Notification;
import com.viafirma.mobile.services.sdk.java.model.Param;
import com.viafirma.mobile.services.sdk.java.model.Policy;
import com.viafirma.mobile.services.sdk.java.model.Workflow;

/**
 * Sample Helper
 */
public class SampleHelper {

	private static final Logger log = Logger.getLogger(SampleHelper.class.getName());
	
	public static String sendExampleMessage(SFAccount account, Device device) throws Exception {

		// Document template
		Document document = new Document();
		document.setTemplateCode(SampleConfig.TEMPLATE_CODE);

		DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);

		// Fill document items from account fields
		ArrayList<Item> items = new ArrayList<Item>();
		Item item1 = new Item();
		Item item2 = new Item();
		Item item3 = new Item();
		Item item4 = new Item();
		Item item5 = new Item();
		item1.setKey("KEY_01");
		item1.setValue(account.getName());
		items.add(item1);
		item2.setKey("KEY_02");
		item2.setValue(account.getPersonEmail());
		items.add(item2);
		item3.setKey("KEY_03");
		item3.setValue(account.getPhone());
		items.add(item3);
		item4.setKey("KEY_04");
		item4.setValue(df.format(new Date()));
		items.add(item4);
		item5.setKey("authorId");
		item5.setValue(account.getId());
		items.add(item5);
		document.setItems(items);

		// Notification
		Notification notification = new Notification();
		notification.setText("Salesforce Sample Document");
		notification.setDetail("");

		// Device
		List<Device> devices = new ArrayList<Device>();
		devices.add(device);
		notification.setDevices(devices);

		// Workflow
		Workflow workflow = new Workflow();
		workflow.setCode("DEMO");

		// Policy
		Policy policy = new Policy();
		policy.setTypeFormatSign("DIGITALIZED_SIGN");
		policy.setTypeSign("ATTACHED");
		
		List<Param> paramList = new ArrayList<Param>();
		String params[][] = {
				{"signPositionEnable", "true"},
				{"biometricAlias", SampleConfig.BIOMETRIC_ALIAS},
				{"biometricPass", SampleConfig.BIOMETRIC_PASS},
				{"alias", SampleConfig.ALIAS},
				{"pass", SampleConfig.PASS},
				{"op", "pen"},
				{"PDF_ANNOTATION_RECTANGLE", "{\"x\":478,\"y\":583,\"width\":73,\"height\":93}"},
				{"PDF_ANNOTATION_PAGE", "1"}
		};
		for (String[] paramArray : params) {
			Param param = new Param(); 
			param.setKey(paramArray[0]);
			param.setValue(paramArray[1]);
			paramList.add(param);
		}
		policy.setParamList(paramList);

		// Clients
		V1Api clientIOS = new V1Api();
		clientIOS.setBasePath(SampleConfig.MS_API_SERVER_URL);
		clientIOS.setConsumerKey(SampleConfig.APPCODE_IOS);
		clientIOS.setConsumerSecret(SampleConfig.TOKEN_IOS);
		
		V1Api clientAndroid = new V1Api();
		clientAndroid.setBasePath(SampleConfig.MS_API_SERVER_URL);
		clientAndroid.setConsumerKey(SampleConfig.APPCODE_ANDROID);
		clientAndroid.setConsumerSecret(SampleConfig.TOKEN_ANDROID);
		
		Message message = new Message();
		message.setDocument(document);
		message.setNotification(notification);
		message.setWorkflow(workflow);
		message.setPolicies(Arrays.asList(policy));
		message.setCallbackURL(SampleConfig.CALLBACK_URL);

		log.info(message.toString());
		if (device.getType().equals("IOS")) {
			return clientIOS.sendMessage(message);
		} else {
			return clientAndroid.sendMessage(message);
		}
	}

	public static List<Device> getDevices(String userCode) throws Exception {
		
		// Clients
		V1Api clientIOS = new V1Api();
		clientIOS.setBasePath(SampleConfig.MS_API_SERVER_URL);
		clientIOS.setConsumerKey(SampleConfig.APPCODE_IOS);
		clientIOS.setConsumerSecret(SampleConfig.TOKEN_IOS);
		
		V1Api clientAndroid = new V1Api();
		clientAndroid.setBasePath(SampleConfig.MS_API_SERVER_URL);
		clientAndroid.setConsumerKey(SampleConfig.APPCODE_ANDROID);
		clientAndroid.setConsumerSecret(SampleConfig.TOKEN_ANDROID);

		List<Device> devices = null;
		log.info("Get IOS Devices");
		devices = clientIOS.findDeviceByUser(userCode);

		log.info("Get Android Devices");
		devices.addAll(clientAndroid.findDeviceByUser(userCode));

		return devices;
	}
}
