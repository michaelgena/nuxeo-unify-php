package org.nuxeo.unify.computed.group;

import java.util.List;

import org.nuxeo.ecm.platform.computedgroups.AbstractGroupComputer;
import org.nuxeo.ecm.platform.usermanager.NuxeoPrincipalImpl;
import org.nuxeo.unify.entitlement.EntitlementHelper;

public class PartnerCategoriesGroupComputer extends AbstractGroupComputer{

	@Override
	public List<String> getAllGroupIds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getGroupMembers(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getGroupsForUser(NuxeoPrincipalImpl nuxeoPrincipal) {
		String username = nuxeoPrincipal.getName();
		EntitlementHelper entitlement = new EntitlementHelper();
		List<String> result = entitlement.getCategoriesForUser(username);
        return result;
	}

	@Override
	public List<String> getParentsGroupNames(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getSubGroupsNames(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
