package com.crscic.gate.event.listener;

import java.util.EventListener;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.DialogState;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipListener;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.header.CSeqHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import com.crscic.gate.connector.IConnector;
import com.crscic.gate.connector.SipConnectorImpl;
import com.crscic.gate.data.sip.Sip;
import com.crscic.gate.event.Event;
import com.crscic.gate.service.IService;
import com.crscic.gate.test.AccountManagerImpl;

import gov.nist.javax.sip.SipStackExt;
import gov.nist.javax.sip.clientauthutils.AuthenticationHelper;

/**
 * 
 * @author zhaokai 2018年12月18日 下午6:11:42
 */
public class SendListener implements EventListener, SipListener
{
	private SipConnectorImpl connector;

	public SendListener(IConnector connector)
	{
		this.connector = (SipConnectorImpl) connector;
	}

	public void send(Event e)
	{
		IService srcService = e.getSourceService();
		// service.send(srcService.getStream());
	}

	public void processBye(Request request, ServerTransaction serverTransactionId)
	{
		try
		{
			System.out.println("shootist:  got a bye .");
			if (serverTransactionId == null)
			{
				System.out.println("shootist:  null TID.");
				return;
			}
			Dialog dialog = serverTransactionId.getDialog();
			System.out.println("Dialog State = " + dialog.getState());
			Response response = Sip.getMessageFactory().createResponse(200, request);
			serverTransactionId.sendResponse(response);
			System.out.println("shootist:  Sending OK.");
			System.out.println("Dialog State = " + dialog.getState());

		}
		catch (Exception ex)
		{
			ex.printStackTrace();

		}
	}

	@Override
	public void processDialogTerminated(DialogTerminatedEvent arg0)
	{
		System.out.println("dialogTerminatedEvent");
	}

	@Override
	public void processIOException(IOExceptionEvent exceptionEvent)
	{
		System.out.println(
				"IOException happened for " + exceptionEvent.getHost() + " port = " + exceptionEvent.getPort());
	}

	@Override
	public void processRequest(RequestEvent requestReceivedEvent)
	{
		Request request = requestReceivedEvent.getRequest();
		ServerTransaction serverTransactionId = requestReceivedEvent.getServerTransaction();

		System.out.println("\n\nRequest " + request.getMethod() + " received at "
				+ connector.getSipStack().getStackName() + " with server transaction id " + serverTransactionId);

		// We are the UAC so the only request we get is the BYE.
		if (request.getMethod().equals(Request.BYE))
			processBye(request, serverTransactionId);

	}

	@Override
	public void processResponse(ResponseEvent responseReceivedEvent)
	{
		System.out.println("Got a response");
		Response response = (Response) responseReceivedEvent.getResponse();
		ClientTransaction tid = responseReceivedEvent.getClientTransaction();
		CSeqHeader cseq = (CSeqHeader) response.getHeader(CSeqHeader.NAME);

		System.out.println("Response received : Status Code = " + response.getStatusCode() + " " + cseq);
		if (tid == null)
		{
			System.out.println("Stray response -- dropping ");
			return;
		}
		System.out.println("transaction state is " + tid.getState());
		System.out.println("Dialog = " + tid.getDialog());
		System.out.println("Dialog State is " + tid.getDialog().getState());
		System.out.println("1");
		try
		{
			Dialog dialog = tid.getDialog();
			if (response.getStatusCode() == Response.OK)
			{
				if (cseq.getMethod().equals(Request.INVITE))
				{
					Request ackRequest = dialog.createAck(cseq.getSeqNumber());
					System.out.println("Sending ACK");
					dialog.sendAck(ackRequest);
				}
				else if (cseq.getMethod().equals(Request.CANCEL))
				{
					if (dialog.getState() == DialogState.CONFIRMED)
					{
						// oops cancel went in too late. Need to hang up the
						// dialog.
						System.out.println("Sending BYE -- cancel went in too late !!");
						Request byeRequest = dialog.createRequest(Request.BYE);
						ClientTransaction ct = connector.getSipProvider().getNewClientTransaction(byeRequest);
						dialog.sendRequest(ct);
					}
				}
			}
			else if (response.getStatusCode() == Response.PROXY_AUTHENTICATION_REQUIRED
					|| response.getStatusCode() == Response.UNAUTHORIZED)
			{
				AuthenticationHelper authenticationHelper = ((SipStackExt) connector.getSipStack())
						.getAuthenticationHelper(new AccountManagerImpl(), Sip.getHeaderFactory());

				tid = authenticationHelper.handleChallenge(response, tid, connector.getSipProvider(), 5);
				System.out.println("new Request:" + tid.getRequest());
				tid.sendRequest();
//TODO:
//				 invco++;
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}

	@Override
	public void processTimeout(TimeoutEvent arg0)
	{
		System.out.println("Transaction Time out");
	}

	@Override
	public void processTransactionTerminated(TransactionTerminatedEvent arg0)
	{
		System.out.println("Transaction terminated event recieved");
	}
}
