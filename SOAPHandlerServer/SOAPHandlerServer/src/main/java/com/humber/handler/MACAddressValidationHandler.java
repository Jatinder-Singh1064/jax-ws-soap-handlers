package com.humber.handler;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.Node;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;

public class MACAddressValidationHandler implements SOAPHandler<SOAPMessageContext> {
	String arr[] = { "E0-DB-55-A4-10-Z4" };
	List<String> validMacAddresses = Arrays.asList(arr);

	public boolean handleMessage(SOAPMessageContext context) {
		System.out.println("Server : handleMessage() Begin");
		Boolean outBoundProperty = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		// If its an incoming message from client, then outBoundProperty will be false
		if (!outBoundProperty) {
			try {
				SOAPMessage soapMsg = context.getMessage();
				SOAPEnvelope soapEnv = soapMsg.getSOAPPart().getEnvelope();
				SOAPHeader soapHeader = soapEnv.getHeader();

				// if no header, throw SOAPFaultException
				if (soapHeader == null) {
					generateErrorMessageAndThrowException(soapMsg, "Missing SOAP header.");
				}

				Iterator itr = soapHeader.extractHeaderElements(SOAPConstants.URI_SOAP_ACTOR_NEXT);

				// if no header block for next actor found,then throw SOAPFaultException
				if (itr == null || !itr.hasNext()) {
					generateErrorMessageAndThrowException(soapMsg, "Missing header block for next actor.");
				}

				// if no MAC address found then throw SOAPFaultException
				Node macNode = (Node) itr.next();
				String macValue = (macNode == null) ? null : macNode.getValue();

				if (macValue == null) {
					generateErrorMessageAndThrowException(soapMsg, "Missing mac address in header block.");
				}

				// if MAC address is not within the trusted MAC list, then throw
				// SOAPFaultException
				if (!validMacAddresses.contains(macValue)) {
					generateErrorMessageAndThrowException(soapMsg, "Invalid mac address, Access is denied.");
				}

				// Output the message to console
				soapMsg.writeTo(System.out);

			} catch (SOAPException e) {
				System.err.println(e);
			} catch (IOException e) {
				System.err.println(e);
			}

		}

		// Returning true makes other handler chain to continue the execution
		return true;
	}

	public boolean handleFault(SOAPMessageContext context) {
		System.out.println("Server : handleFault() Begin");
		return true;
	}

	public void close(MessageContext context) {
		System.out.println("Server : close() Begin");

	}

	public Set<QName> getHeaders() {
		System.out.println("Server : getHeaders() Begin");
		return null;
	}

	private void generateErrorMessageAndThrowException(SOAPMessage msg, String reason) {
		try {
			SOAPBody soapBody = msg.getSOAPPart().getEnvelope().getBody();
			SOAPFault soapFault = soapBody.addFault();
			soapFault.setFaultString(reason);
			throw new SOAPFaultException(soapFault);
		} catch (SOAPException e) {
			System.err.println(e);
		}
	}

}
