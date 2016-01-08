/**
 * 
 */

package org.nuxeo.unify.php.operations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.Blob;

/**
 * @author mgena
 */
@Operation(id=QueryAndFetchAkeneo.ID, category=Constants.CAT_DOCUMENT, label="QueryAndFetchAkeneo", description="Make a REST call to Akeneo.")
public class QueryAndFetchAkeneo {
	
	private Log logger = LogFactory.getLog(QueryAndFetchAkeneo.class);

    public static final String ID = "AKENEO.QueryAndFetch";

    @Param(name = "pid")
    protected String pid;
    
    @Param(name = "type")
    protected String type;
    
    @Param(name = "object_type")
    protected String object_type;
    
    @Param(name = "code")
    protected String code;
    
    @Param(name = "category_code")
    protected String category_code;
    
    @Param(name = "version_to_product")
    protected String version_to_product;
    
    @OperationMethod
    public Blob run() {

    	//Set proxy
    	String proxyHost = "172.28.12.5";
    	String proxyPort = "80";
    	System.setProperty("http.proxyHost", proxyHost);
    	System.setProperty("http.proxyPort", proxyPort);
    	
    	String url = "http://mchp206a.global-intra.net/api/products.json?attributes=1";
    	
    	if(type != null && !"".equals(type)){
    		url = "http://mchp206a.global-intra.net/api/"+type+".json?attributes=1";
    	}
    	
    	if(pid != null && !"".equals(pid)){
    		url += "&pid="+pid;
    	}
    	
    	if(object_type != null && !"".equals(object_type)){
    		url += "&object_type="+object_type;
    	}
    	
    	if(code != null  && !"".equals(code)){
    		url += "&code="+code;
    	}
    	
    	if(category_code != null  && !"".equals(category_code)){
    		url += "&category_code="+category_code;
    	}
    	
    	if(version_to_product != null  && !"".equals(version_to_product)){
    		url += "&version_to_product="+version_to_product;
    	}
    	StringBuffer productListBuffer = new StringBuffer();
    	
    	
    	try {
			
			// create HTTP Client
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();

			HttpGet getRequest = new HttpGet(url);
 
			// Add additional header to getRequest which accepts application/xml data
			//getRequest.addHeader("accept", "application/xml");
 
			// Execute your request and catch response
			HttpResponse response = httpClient.execute(getRequest);
 
			// Check for HTTP response code: 200 = success
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}
 
			// Get-Capture Complete application/xml body response
			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			String output;
			
			// Simply iterate through XML response and show on console.
			while ((output = br.readLine()) != null) {
				productListBuffer.append(output);
			}
 
		} catch (ClientProtocolException e) {
			logger.error(e);
 
		} catch (IOException e) {
			logger.error(e);
		}
    	return new org.nuxeo.ecm.core.api.impl.blob.StringBlob(productListBuffer.toString()); 
    }    

}
