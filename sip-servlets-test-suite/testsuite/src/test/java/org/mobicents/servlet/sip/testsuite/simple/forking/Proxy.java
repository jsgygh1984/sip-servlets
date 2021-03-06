/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.servlet.sip.testsuite.simple.forking;

import java.util.Hashtable;
import java.util.Iterator;

import javax.sip.ClientTransaction;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.ListeningPoint;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipListener;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.RecordRouteHeader;
import javax.sip.header.RouteHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

/**
 * A very simple forking proxy server.
 * 
 * @author M. Ranganathan
 * 
 */
public class Proxy implements SipListener {

    // private ServerTransaction st;

    private SipProvider inviteServerTxProvider;

    private Hashtable clientTxTable = new Hashtable();

    private static String host = "" + System.getProperty("org.mobicents.testsuite.testhostaddr") + "";

    private int port = 5070;

    private SipProvider sipProvider;

    private static String unexpectedException = "Unexpected exception";

    private static Logger logger = Logger.getLogger(Proxy.class);

    private static AddressFactory addressFactory;

    private static MessageFactory messageFactory;

    private static HeaderFactory headerFactory;

    private static String transport = "udp";

    private SipStack sipStack;

    private int ntargets;
    
    
    private void sendTo(ServerTransaction st, Request request, int targetPort) throws Exception {
        Request newRequest = (Request) request.clone();
        
        SipURI sipUri = addressFactory.createSipURI("UA1", "" + System.getProperty("org.mobicents.testsuite.testhostaddr") + "");
        sipUri.setPort(targetPort);
        sipUri.setLrParam();
        Address address = addressFactory.createAddress("client1", sipUri);
        RouteHeader rheader = headerFactory.createRouteHeader(address);

        newRequest.addFirst(rheader);
        ViaHeader viaHeader = headerFactory.createViaHeader(host, this.port, transport, null);
        newRequest.addFirst(viaHeader);
        ClientTransaction ct1 = sipProvider.getNewClientTransaction(newRequest);
        sipUri = addressFactory.createSipURI("proxy", "" + System.getProperty("org.mobicents.testsuite.testhostaddr") + "");
        address = addressFactory.createAddress("proxy", sipUri);
        sipUri.setPort(5070);
        sipUri.setLrParam();
        RecordRouteHeader recordRoute = headerFactory.createRecordRouteHeader(address);
        newRequest.addHeader(recordRoute);
        ct1.setApplicationData(st);
        this.clientTxTable.put(new Integer(targetPort), ct1);
        ct1.sendRequest();
    }

    public void processRequest(RequestEvent requestEvent) {
        try {
            Request request = requestEvent.getRequest();
            SipProvider sipProvider = (SipProvider) requestEvent.getSource();
            this.inviteServerTxProvider = sipProvider;
            if (request.getMethod().equals(Request.INVITE)) {

                ListeningPoint lp = sipProvider.getListeningPoint(transport);
                String host = lp.getIPAddress();
                int port = lp.getPort();

                ServerTransaction st = null;
                if (requestEvent.getServerTransaction() == null) {
                    st = sipProvider.getNewServerTransaction(request);

                }
                
                for ( int i = 0; i < ntargets; i++ ) {
                    this.sendTo(st,request,5080 + i);
                }

             
               

            } else if (request.getMethod().equals(Request.CANCEL)) {
            	ClientTransaction clientTransaction = ((ClientTransaction)clientTxTable.get(new Integer(5081)));
            	Request cancelRequest = clientTransaction.createCancel();
            	sipProvider.sendRequest(cancelRequest);
            	
            } else {
                // Remove the topmost route header
                // The route header will make sure it gets to the right place.
                logger.info("proxy: Got a request " + request.getMethod());
                Request newRequest = (Request) request.clone();
                newRequest.removeFirst(RouteHeader.NAME);
                sipProvider.sendRequest(newRequest);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }

    }

    public void processResponse(ResponseEvent responseEvent) {
        try {
            Response response = responseEvent.getResponse();
            CSeqHeader cseq = (CSeqHeader) response.getHeader(CSeqHeader.NAME);
            logger.info("ClientTxID = " + responseEvent.getClientTransaction() + " client tx id "
                    + ((ViaHeader) response.getHeader(ViaHeader.NAME)).getBranch()
                    + " CSeq header = " + response.getHeader(CSeqHeader.NAME) + " status code = "
                    + response.getStatusCode());

            // JvB: stateful proxy MUST NOT forward 100 Trying
            if (response.getStatusCode() == 100)
                return;

            if (cseq.getMethod().equals(Request.INVITE)) {
                ClientTransaction ct = responseEvent.getClientTransaction();
                if (ct != null) {
                    ServerTransaction st = (ServerTransaction) ct.getApplicationData();

                    // Strip the topmost via header
                    Response newResponse = (Response) response.clone();
                    newResponse.removeFirst(ViaHeader.NAME);
                    // The server tx goes to the terminated state.

                    st.sendResponse(newResponse);
                } else {
                    // Client tx has already terminated but the UA is
                    // retransmitting
                    // just forward the response statelessly.
                    // Strip the topmost via header

                    Response newResponse = (Response) response.clone();
                    newResponse.removeFirst(ViaHeader.NAME);
                    // Send the retransmission statelessly
                    this.inviteServerTxProvider.sendResponse(newResponse);
                }
            } else {
                // this is the OK for the cancel.
                logger.info("Got a non-invite response " + response);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            TestCase.fail("unexpected exception");
        }
    }

    public void processTimeout(TimeoutEvent timeoutEvent) {
        logger.error("Timeout occured");
        TestCase.fail("unexpected event");
    }

    public void processIOException(IOExceptionEvent exceptionEvent) {
        logger.info("IOException occured");
        TestCase.fail("unexpected exception io exception");
    }

    public SipProvider createSipProvider() {
        try {
            ListeningPoint listeningPoint = sipStack.createListeningPoint(host, port, transport);

            sipProvider = sipStack.createSipProvider(listeningPoint);
            return sipProvider;
        } catch (Exception ex) {
            logger.error(unexpectedException, ex);
            TestCase.fail(unexpectedException);
            return null;
        }

    }

    public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {
        logger.info("Transaction terminated event occured -- cleaning up");
        if (!transactionTerminatedEvent.isServerTransaction()) {
            ClientTransaction ct = transactionTerminatedEvent.getClientTransaction();
            for (Iterator it = this.clientTxTable.values().iterator(); it.hasNext();) {
                if (it.next().equals(ct)) {
                    it.remove();
                }
            }
        } else {
            logger.info("Server tx terminated! ");
        }
    }

    public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {
        TestCase.fail("unexpected event");
    }

    public Proxy(int myPort, int ntargets) {
        this.port = myPort;
        this.ntargets = ntargets;
        SipObjects sipObjects = new SipObjects(myPort, "proxy","off", true);
        addressFactory = sipObjects.addressFactory;
        messageFactory = sipObjects.messageFactory;
        headerFactory = sipObjects.headerFactory;
        this.sipStack = sipObjects.sipStack;
  
    }

    public void stop() {
       this.sipStack.stop();
    }

}
