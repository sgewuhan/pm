package com.sg.business.organization.command;

import java.util.HashSet;
import java.util.Set;

public class SyscHR {

	public static void doSyscHR() {
		OrgExchange pmOrg = new OrgExchange(null, true);
		OrgExchange hrOrg = new OrgExchange(null, false);
		Set<OrgExchange> insertSet = new HashSet<OrgExchange>();
		Set<OrgExchange> removeSet = new HashSet<OrgExchange>();
		Set<OrgExchange> renameSet = new HashSet<OrgExchange>();
		if (!hrOrg.equals(pmOrg)) {
			insertSet.add(hrOrg);
			removeSet.add(pmOrg);
		} else {
			getPMDifferentHR(pmOrg, hrOrg, insertSet, removeSet, renameSet);
		}
		for (OrgExchange orgExchange : insertSet) {
			orgExchange.doAddAllHR();
		}
		for (OrgExchange orgExchange : renameSet) {
			orgExchange.doRenameHR();
		}
		pmOrg.sendMessage(removeSet);
	}

	private static void getPMDifferentHR(OrgExchange pmOrg, OrgExchange hrOrg,
			Set<OrgExchange> insertSet, Set<OrgExchange> removeSet,
			Set<OrgExchange> renameSet) {
		insertSet.addAll(hrOrg.getDifferentChildren(pmOrg));
		removeSet.addAll(pmOrg.getDifferentChildren(hrOrg));
		if (!hrOrg.getDifferentName(pmOrg)) {
			hrOrg.setPmId(pmOrg.getPmId());
			renameSet.add(hrOrg);
		}
		Set<OrgExchange> hrSameChildren = hrOrg.getSameChildren(pmOrg);
		for (OrgExchange orgExchange : hrSameChildren) {
			OrgExchange pmOrgChildren = new OrgExchange(orgExchange.getOrgId(),
					true);
			OrgExchange hrOrgChildren = new OrgExchange(orgExchange.getOrgId(),
					false);
			getPMDifferentHR(pmOrgChildren, hrOrgChildren, insertSet,
					removeSet, renameSet);
		}
	}

}
