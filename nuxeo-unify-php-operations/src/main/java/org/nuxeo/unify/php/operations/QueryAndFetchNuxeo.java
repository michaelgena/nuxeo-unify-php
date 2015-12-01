/**
 * 
 */

package org.nuxeo.unify.php.operations;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.IterableQueryResult;
import org.nuxeo.ecm.core.query.sql.NXQL;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author mgena
 */
@Operation(id=QueryAndFetchNuxeo.ID, category=Constants.CAT_DOCUMENT, label="QueryAndFetchNuxeo", description="Make an NXQL query to Nuxeo and retrieve data in json format.")
public class QueryAndFetchNuxeo {

    public static final String ID = "QueryAndFetchNuxeo";

    @Context
    protected CoreSession session;
 
    @Param(name = "query")
    protected String query;
 
    protected String lang = NXQL.NXQL;

    @OperationMethod
    public Blob run() {
    	IterableQueryResult result = session.queryAndFetch(query, lang);
        Iterator<Map<String, Serializable>> it = result.iterator();
 
        JSONArray array = new JSONArray();
        while (it.hasNext()) {
            Map<String, Serializable> item = it.next();
            JSONObject object = new JSONObject();
            object.accumulateAll(item);
            array.add(object);
        }
        return new org.nuxeo.ecm.core.api.impl.blob.StringBlob(array.toString());
    }    

}
