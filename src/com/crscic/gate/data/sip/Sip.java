package com.crscic.gate.data.sip;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Properties;

import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.ObjectInUseException;
import javax.sip.PeerUnavailableException;
import javax.sip.SipFactory;
import javax.sip.SipProvider;
import javax.sip.TransportNotSupportedException;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.Header;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;

import com.crscic.gate.config.IConfig;
import com.crscic.gate.connector.IConnector;
import com.crscic.gate.connector.SipConnectorImpl;
import com.crscic.gate.exception.DataGenerateException;

/**
 * 
 * @author zhaokai 2018年12月19日 下午6:41:40
 */
public class Sip
{
	private static final String ERR_MSG = "生成Sip协议报文头信息-";
	
	private SipConnectorImpl connector;
	private SipProvider sipProvider;
	private AddressFactory addressFactory;
	private MessageFactory messageFactory;
	private HeaderFactory headerFactory;
	private ContactHeader contactHeader;
	private ListeningPoint listeningPoint;
	private long invco = 1;

	public Sip(SipFactory sipFactory, IConnector connector) throws DataGenerateException
	{
		try
		{
			headerFactory = sipFactory.createHeaderFactory();
			addressFactory = sipFactory.createAddressFactory();
			messageFactory = sipFactory.createMessageFactory();

			this.connector = (SipConnectorImpl) connector;
			listeningPoint = this.connector.getListeningPoint();
			sipProvider = this.connector.getSipProvider();
		}
		catch (PeerUnavailableException e)
		{
			e.printStackTrace();
		}

	}

	public Request createRegister(IConfig config, String callId) throws DataGenerateException
	{
		return createRegister(config.getProperty("sender.ip"), config.getProperty("sender.port"),
				config.getProperty("sender.id"), config.getProperty("sender.ip"), config.getProperty("sender.ip"),
				config.getProperty("receiver.ip"), config.getProperty("receiver.port"),
				config.getProperty("receiver.id"), config.getProperty("receiver.ip"), config.getProperty("receiver.ip"),
				"", config.getProperty("sender.type"), config.getProperty("me.ip"), config.getProperty("me.sip.port"));
	}

	public Request createRegister(String toIp, String toPort, String toUser, String toSipAddress, String toDisplayName,
			String fromIp, String fromPort, String fromName, String fromSipAddress, String fromDisplayName,
			String callId, String transportType, String viaHostIp, String viaHostPort) throws DataGenerateException

	{
		Request request = null;
		String peerHostPort = toIp + ":" + toPort;
		String transport = transportType;

		try
		{
			// create >From Header
			SipURI fromAddress = addressFactory.createSipURI(fromName, fromSipAddress);

			Address fromNameAddress = addressFactory.createAddress(fromAddress);
			fromNameAddress.setDisplayName(fromDisplayName);
			FromHeader fromHeader = headerFactory.createFromHeader(fromNameAddress, "12345");

			// create To Header
			SipURI toAddress = addressFactory.createSipURI(toUser, toSipAddress);
			Address toNameAddress = addressFactory.createAddress(toAddress);
			toNameAddress.setDisplayName(toDisplayName);
			ToHeader toHeader = headerFactory.createToHeader(toNameAddress, null);

			// create Request URI
			SipURI requestURI = addressFactory.createSipURI(toUser, peerHostPort);

			// Create ViaHeaders
			ArrayList<ViaHeader> viaHeaders = new ArrayList<ViaHeader>();
			ViaHeader viaHeader = headerFactory.createViaHeader(viaHostIp,
					sipProvider.getListeningPoint(transport).getPort(), transport, null);
			// add via headers
			viaHeaders.add(viaHeader);

			// Create a new CallId header
			CallIdHeader callIdHeader;
			callIdHeader = sipProvider.getNewCallId();
			if (callId.trim().length() > 0)
				callIdHeader.setCallId(callId);

			// Create a new Cseq header
			CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(invco, Request.REGISTER);

			// Create a new MaxForwardsHeader
			MaxForwardsHeader maxForwards = headerFactory.createMaxForwardsHeader(70);

			// Create the request.
			request = messageFactory.createRequest(requestURI, Request.REGISTER, callIdHeader, cSeqHeader, fromHeader,
					toHeader, viaHeaders, maxForwards);
			// Create contact headers
			String host = fromIp + ":" + fromPort;

			SipURI contactUrl = addressFactory.createSipURI(fromName, host);
			contactUrl.setPort(listeningPoint.getPort());

			// Create the contact name address.
			SipURI contactURI = addressFactory.createSipURI(fromName, host);
			contactURI.setPort(sipProvider.getListeningPoint(transport).getPort());

			Address contactAddress = addressFactory.createAddress(contactURI);

			// Add the contact address.
			contactAddress.setDisplayName(fromName);

			contactHeader = headerFactory.createContactHeader(contactAddress);
			request.addHeader(contactHeader);

		}
		catch (DataGenerateException e)
		{
			throw new DataGenerateException(ERR_MSG + "创建地址信息失败", e);
		}
		return request;
	}

	public Request createInvite(String toIp, String toPort, String toUser, String toSipAddress, String toDisplayName,
			String fromIp, String fromPort, String fromName, String fromSipAddress, String fromDisplayName,
			String callId, String transportType, String viaHostIp, String viaHostPort)
			throws ParseException, InvalidArgumentException
	{
		// String fromName = "34020100002000000001";
		// String fromSipAddress = "192.168.175.9";
		// String fromDisplayName = "The Master Blaster";
		// String toSipAddress = "192.168.175.10";
		// String toUser = "34020100002000000001";
		// String toDisplayName = "The Little Blister";

		String peerHostPort = toIp + ":" + toPort;
		String transport = transportType;

		// create >From Header
		SipURI fromAddress = addressFactory.createSipURI(fromName, fromSipAddress);

		Address fromNameAddress = addressFactory.createAddress(fromAddress);
		fromNameAddress.setDisplayName(fromDisplayName);
		FromHeader fromHeader = headerFactory.createFromHeader(fromNameAddress, "12345");

		// create To Header
		SipURI toAddress = addressFactory.createSipURI(toUser, toSipAddress);
		Address toNameAddress = addressFactory.createAddress(toAddress);
		toNameAddress.setDisplayName(toDisplayName);
		ToHeader toHeader = headerFactory.createToHeader(toNameAddress, null);

		// create Request URI
		SipURI requestURI = addressFactory.createSipURI(toUser, peerHostPort);

		// Create ViaHeaders
		ArrayList<ViaHeader> viaHeaders = new ArrayList<ViaHeader>();
		ViaHeader viaHeader = headerFactory.createViaHeader("127.0.0.1",
				sipProvider.getListeningPoint(transport).getPort(), transport, null);
		// add via headers
		viaHeaders.add(viaHeader);

		// Create ContentTypeHeader
		ContentTypeHeader contentTypeHeader = headerFactory.createContentTypeHeader("application", "sdp");

		// Create a new CallId header
		CallIdHeader callIdHeader;
		callIdHeader = sipProvider.getNewCallId();
		if (callId.trim().length() > 0)
			callIdHeader.setCallId(callId);

		// Create a new Cseq header
		CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(invco, Request.INVITE);

		// Create a new MaxForwardsHeader
		MaxForwardsHeader maxForwards = headerFactory.createMaxForwardsHeader(70);

		// Create the request.
		Request request = messageFactory.createRequest(requestURI, Request.INVITE, callIdHeader, cSeqHeader, fromHeader,
				toHeader, viaHeaders, maxForwards);
		// Create contact headers
		String host = "127.0.0.1";
		// String host = "192.168.175.9";

		SipURI contactUrl = addressFactory.createSipURI(fromName, host);
		contactUrl.setPort(listeningPoint.getPort());

		// Create the contact name address.
		SipURI contactURI = addressFactory.createSipURI(fromName, host);
		contactURI.setPort(sipProvider.getListeningPoint(transport).getPort());

		Address contactAddress = addressFactory.createAddress(contactURI);

		// Add the contact address.
		contactAddress.setDisplayName(fromName);

		contactHeader = headerFactory.createContactHeader(contactAddress);
		request.addHeader(contactHeader);

		String sdpData = "v=0\r\n" + "o=4855 13760799956958020 13760799956958020" + " IN IP4  129.6.55.78\r\n"
				+ "s=mysession session\r\n" + "p=+46 8 52018010\r\n" + "c=IN IP4  129.6.55.78\r\n" + "t=0 0\r\n"
				+ "m=audio 6022 RTP/AVP 0 4 18\r\n" + "a=rtpmap:0 PCMU/8000\r\n" + "a=rtpmap:4 G723/8000\r\n"
				+ "a=rtpmap:18 G729A/8000\r\n" + "a=ptime:20\r\n";
		byte[] contents = sdpData.getBytes();

		request.setContent(contents, contentTypeHeader);

		Header callInfoHeader = headerFactory.createHeader("Call-Info", "<http://www.antd.nist.gov>");
		request.addHeader(callInfoHeader);

		return request;
	}
}
