<%@page import="com.viafirma.mobile.services.sdk.java.model.Device"%>
<%@page import="com.viafirma.documents.sample.SampleHelper"%>
<%@page import="com.viafirma.documents.sforce.model.SFAccount"%>
<%@page import="java.util.List"%>
<%@page import="com.viafirma.documents.sforce.SFClient"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<%
	// Init client
	SFClient client = SFClient.getInstance();
    
	// Accounts
    List<SFAccount> accounts = client.queryAll("SELECT id, name, phone, personEmail from Account");
	SFAccount selectedAccount = null;
	for (SFAccount account : accounts) {
		if (account.getId().equals(request.getParameter("account"))) {
			selectedAccount = account;
			break;
		}
	}
	pageContext.setAttribute("accounts", accounts);
	
	// Devices
	List<Device> devices = null;
	Device selectedDevice = null;
	String userCode = request.getParameter("userCode");
	if (userCode != null && !userCode.equals("")) {
			devices = SampleHelper.getDevices(userCode);
			if (devices != null) {
				for (Device device : devices) {
					if (device.getUniqueIdentifier().equals(request.getParameter("device"))) {
						selectedDevice = device;
						break;
					}
				}
			}
	}
	pageContext.setAttribute("devices", devices);
%>
<!DOCTYPE html>
<html>
<head>
<title>Viafirma Documents - Salesforce Integration Test</title>

<link rel="stylesheet" type="text/css" href="./sdk/css/canvas.css" />

<script>
	function listDevices() {
		document.getElementById("listDevicesButton").style="disable:true";
		document.getElementById("action").value="listDevices";
		document.getElementById("documentSettings").submit();
	}
	
	function send() {
		document.getElementById("sendButton").style="disable:true";
		document.getElementById("action").value="send";
		document.getElementById("documentSettings").submit();
	}
</script>
</head>
<body>
	<div id="page">
		<form id="documentSettings" method="post" action="index.jsp">
		<div id="content">
			<div id="canvas-content">	
				<div id="canvas-request">			
					<table border="0" width="100%">
						<tr>
							<td colspan="2">
								<h3>Document Settings</h3>
							</td>
						</tr>					
						<tr>
							<td><b>Account: </b></td>
							<td>
							<select name="account">
								<c:forEach var="account" items="${accounts}">
									<option value="${account.id}"<c:if test="${account.id == param.account}"> selected</c:if>>${account.name}</option>
								</c:forEach>
							</select>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<h3>Mobile Device Settings</h3>
							</td>
						</tr>
						<tr>
							<td><b>User Code: </b></td>
							<td><input type="text" name="userCode" value="${param.userCode}" /><input type="button" id="listDevicesButton" value="List Devices" onclick="listDevices()"></td>
						</tr>
						<c:if test="${devices != null}">				
							<tr>
								<td><b>Mobile Device: </b></td>
								<td>
								<select name="device">
									<c:forEach var="device" items="${devices}">
										<option value="${device.uniqueIdentifier}"<c:if test="${device.uniqueIdentifier == param.device}"> selected</c:if>>${device.code}</option>->
									</c:forEach>
								</select>
								</td>
							</tr>
						</c:if>
						<c:if test="${param.action == 'listDevices' && devices == null}">
							<tr>
								<td></td><td><b>NO DEVICES FOUND</b></td>
							</tr>
						</c:if> 
					</table>
				</div>
				
				<c:if test="${devices != null}">
					<div id="canvas-chatter">
				        <table border="0" width="100%">
				            <tr>
				                <td><button id="sendButton" type="button" onclick="send()">Send</button></td>
				            </tr>
				        </table>
			        </div>
		        </c:if>
		        
									<%
		        						String messageID = "";
		        									if("send".equals(request.getParameter("action"))) {
		        										try {
		        											messageID = SampleHelper.sendExampleMessage(selectedAccount, selectedDevice);
		        					%>
								  <div class="alert alert-success">
									Send message OK: Message ID=<%=messageID%>
								  </div>
								<%
							} catch (Exception e) {
								%>
								  <div class="alert alert-error">
									Send message FAIL: <br /><%=e.getMessage()%>
					  			  </div>
								<%	
							}
						}
					%>
			</div>
		</div>
	
		<input id="action" name="action" type="hidden" value="">
	    </form>
		<div id="footercont">
			<div id="footerleft">
				<p>
					Powered By: <a title="Viafirma" href="#"
						onclick="window.top.location.href='http://www.viafirma.com'"><strong>Viafirma</strong></a>
				</p>
			</div>
		</div>
	</div>
</body>
</html>
