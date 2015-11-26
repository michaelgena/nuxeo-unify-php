package org.nuxeo.unify.php.operations;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.query.sql.NXQL;
 
@Operation(id = QueryAndFetch.ID, category = Constants.CAT_FETCH, label = "QueryAndFetch", description = "Make a SOAP request to Unify Entitlement Service.")
public class QueryAndFetch {
	
	private Log logger = LogFactory.getLog(QueryAndFetch.class);
 
    public static final String ID = "GPA.QueryAndFetch";
 
    @Context
    protected CoreSession session;
 
    @Param(name = "query")
    protected String query;
 
    protected String lang = NXQL.NXQL;
 
    @OperationMethod
    public Blob run() throws Exception {
    	//Set proxy
    	String proxyHost = "172.28.12.5";
    	String proxyPort = "80";
    	System.setProperty("http.proxyHost", proxyHost);
    	System.setProperty("http.proxyPort", proxyPort);

        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();

        // Send SOAP Message to SOAP Server
        String url = "http://mchp013a.intranet.unify.com/axis/services//EntitlementService";
        
        // Get User Country
        SOAPMessage soapResponse = soapConnection.call(createSOAPRequestForUserCountry(query), url);
        User user = getUserCountry(soapResponse);
        logger.info("{\"id\":\""+user.id+"\", \"email\":\""+user.email+"\", \"country\":\""+user.country+"\"}");
        
        // Get Partner Categories
        soapResponse = soapConnection.call(createSOAPRequestForPartnerCategories(query), url);
        ArrayList<Category> categories = getPartnerCategories(soapResponse);
        soapConnection.close();
        //String jsonCategories = "";
        for(Category category : categories){
        	
        	logger.info("{\"id\":\""+category.id+"\", \"name\":\""+category.name+"\"}");
        }
       
        return new org.nuxeo.ecm.core.api.impl.blob.StringBlob("{\"id\":\""+user.id+"\", \"email\":\""+user.email+"\", \"country\":\""+user.country+"\"}");
    }
        
    private SOAPMessage createSOAPRequestForUserCountry(String login) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String serverURI = "http://service.entitlement.siemens.com";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("ser", serverURI);

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("getAccountByLogin", "ser");
        soapBodyElem.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding");
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("applicationId");
        soapBodyElem1.addTextNode("bolayer");
        soapBodyElem1.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding");
        SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("login");
        soapBodyElem2.addTextNode(login);
        soapBodyElem2.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding");

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", serverURI  + "getAccountByLogin");

        soapMessage.saveChanges();

        return soapMessage;
    }
    
    private User getUserCountry(SOAPMessage soapResponse) throws SOAPException, IOException, XMLStreamException, JAXBException{
    	 ByteArrayOutputStream os = new ByteArrayOutputStream();
         soapResponse.writeTo(os);
         
         InputStream in = new ByteArrayInputStream(os.toByteArray()); 
      
         XMLInputFactory xif = XMLInputFactory.newFactory();
         StreamSource xml = new StreamSource(in);
         XMLStreamReader xsr = xif.createXMLStreamReader(xml);
         xsr.nextTag();
              
         while(!xsr.getLocalName().equals("multiRef")) {
             xsr.nextTag();
         } 
         JAXBContext jc = JAXBContext.newInstance(User.class);
         Unmarshaller unmarshaller = jc.createUnmarshaller();
         JAXBElement<User> jb = unmarshaller.unmarshal(xsr, User.class);
         xsr.close(); 
         User user = jb.getValue();
         return user;
    }
    
    private SOAPMessage createSOAPRequestForPartnerCategories(String userId) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String serverURI = "http://service.entitlement.siemens.com";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("ser", serverURI);

        // SOAP Body
        //<ser:getCompanyByUserId soapenv:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
        //<request xsi:type="urn:CompanyByUserIdRequest" xmlns:urn="urn:model.service.entitlement.siemens.com">
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("getCompanyByUserId", "ser");
        soapBodyElem.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding");
        
        SOAPElement soapBodyElem0 = soapBodyElem.addChildElement("request");
        soapBodyElem0.setAttribute("xsi:type", "urn:CompanyByUserIdRequest");
        soapBodyElem0.setAttribute("xmlns:urn", "urn:model.service.entitlement.siemens.com");
        
        SOAPElement soapBodyElem1 = soapBodyElem0.addChildElement("applicationId");
        soapBodyElem1.addTextNode("bolayer");
        soapBodyElem1.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding");
        SOAPElement soapBodyElem2 = soapBodyElem0.addChildElement("userId");
        soapBodyElem2.addTextNode(userId);
        soapBodyElem2.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding");

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", serverURI  + "getCompanyByUserId");

        soapMessage.saveChanges();

        return soapMessage;
    }
    
    private static ArrayList<Category> getPartnerCategories(SOAPMessage soapResponse) throws SOAPException, IOException, XMLStreamException, JAXBException{
    	ArrayList<Category> categories = new ArrayList<Category>();
    	
    	ByteArrayOutputStream os = new ByteArrayOutputStream();
    	
        soapResponse.writeTo(os);
       
        System.out.println(os.toString("UTF-8"));
        InputStream in = new ByteArrayInputStream(os.toString("UTF-8").getBytes()); 

        XMLInputFactory xif = XMLInputFactory.newFactory();
        StreamSource xml = new StreamSource(in);
 
        XMLStreamReader xsr = xif.createXMLStreamReader(xml);

        while(xsr.hasNext()) {
        	int next = xsr.next();     	
        	if(next == XMLStreamReader.START_ELEMENT && xsr.getLocalName().equals("multiRef")){
        		JAXBContext jc = JAXBContext.newInstance(Category.class);
                Unmarshaller unmarshaller = jc.createUnmarshaller();
                JAXBElement<Category> jb = unmarshaller.unmarshal(xsr, Category.class);
                Category category = jb.getValue();
                if(category != null && category.id != null && category.name != null)
                categories.add(category);
        	}
        }        
        xsr.close();        
        return categories;
    }
    
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class User {    	 
        String id;           
        String email;  
        String country;
    }
    
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Category {    	 
        String id;           
        String name;
    }
}