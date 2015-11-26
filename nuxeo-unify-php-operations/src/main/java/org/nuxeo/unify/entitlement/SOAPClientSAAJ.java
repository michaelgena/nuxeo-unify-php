package org.nuxeo.unify.entitlement;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

public class SOAPClientSAAJ {

	public static final int MAX_SOAP_MESSAGE_SIZE = 2 * 1024 * 1024;
    public static void main(String args[]) throws Exception {
        // Create SOAP Connection
    	String proxyHost = "172.28.12.5";
    	String proxyPort = "80";
    	System.setProperty("http.proxyHost", proxyHost);
    	System.setProperty("http.proxyPort", proxyPort);
    	//System.setProperty("http.nonProxyHosts", "localhost|127.0.0.1");
    	
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();

        // Send SOAP Message to SOAP Server
        String url = "http://mchp013a.intranet.unify.com/axis/services//EntitlementService";
                
        SOAPMessage soapResponse = soapConnection.call(createSOAPRequestForUserCountry("Z001KYJF"), url);
        User user = getUserCountry(soapResponse);
        System.out.println("{\"id\":\""+user.id+"\", \"email\":\""+user.email+"\", \"country\":\""+user.country+"\"}");
                
        /*SOAPMessage soapResponse = soapConnection.call(createSOAPRequest("Z001KYJF"), url);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        soapResponse.writeTo(os);
        soapConnection.close();
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

        System.out.println("{\"id\":\""+user.id+"\", \"email\":\""+user.email+"\", \"country\":\""+user.country+"\"}");        
        */
        
        // Get Partner Categories
        //soapConnectionFactory = SOAPConnectionFactory.newInstance();
        //soapConnection = soapConnectionFactory.createConnection();
        SOAPMessage soapResponse2 = soapConnection.call(createSOAPRequestForPartnerCategories("Z001KYJF"), url);
        ArrayList<Category> categories = getPartnerCategories(soapResponse2);
        soapConnection.close();
        for(Category category : categories){
        	System.out.println("{\"id\":\""+category.id+"\", \"name\":\""+category.name+"\"}");
        }
  
    }
    
    
    private static SOAPMessage createSOAPRequestForUserCountry(String login) throws Exception {
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
    
    private static User getUserCountry(SOAPMessage soapResponse) throws SOAPException, IOException, XMLStreamException, JAXBException{
    	 ByteArrayOutputStream os = new ByteArrayOutputStream();
         soapResponse.writeTo(os);
         System.out.println(os.toByteArray());
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
    
    

    private static SOAPMessage createSOAPRequest(String login) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String serverURI = "http://service.entitlement.siemens.com";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("ser", serverURI);

        /*        
        <soapenv:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 	xmlns:ser="http://service.entitlement.siemens.com">		
		   <soapenv:Header/>		
		   <soapenv:Body>		
		      <ser:getAccountByLogin soapenv:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
		         <applicationId xsi:type="soapenc:string" xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/">bolayer</applicationId>
		         <login xsi:type="soapenc:string" xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/">Z001KYJF</login>
		      </ser:getAccountByLogin>
   			</soapenv:Body>
		</soapenv:Envelope>       
        */

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("getAccountByLogin", "ser");
        soapBodyElem.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding");
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("applicationId");
        soapBodyElem1.addTextNode("bolayer");
        soapBodyElem1.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding");
        SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("login");
        soapBodyElem2.addTextNode("Z001KYJF");
        soapBodyElem2.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding");

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", serverURI  + "getAccountByLogin");

        soapMessage.saveChanges();

        return soapMessage;
    }

    private static SOAPMessage createSOAPRequestForPartnerCategories(String userId) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String serverURI = "http://service.entitlement.siemens.com";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("ser", serverURI);
        envelope.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");

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
        headers.addHeader("SOAPAction", serverURI  + "CompanyByUserIdRequest");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        //soapMessage.writeTo(os);
      
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
        		System.out.println(xsr.getLocalName());
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
