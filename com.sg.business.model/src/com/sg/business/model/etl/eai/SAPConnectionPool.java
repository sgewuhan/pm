package com.sg.business.model.etl.eai;

import com.mobnut.admin.dataset.Setting;
import com.mobnut.commons.util.Utils;
import com.sap.mw.jco.JCO;
import com.sap.mw.jco.JCO.Client;
import com.sap.mw.jco.JCO.Pool;
import com.sg.business.model.IModelConstants;

public class SAPConnectionPool {

	public static String POOL_NAME = "SAPJCO"; //$NON-NLS-1$

	public Client connSAP() {

		Object value = Setting
				.getSystemSetting(IModelConstants.S_EAI_SAP_MAXCONN);
		int maxConn = 200;
		if (Utils.isNullOrEmptyString(value)) {
			try {
				value = Integer.parseInt((String) value);
			} catch (Exception e) {
			}
		}

		value = Setting.getSystemSetting(IModelConstants.S_EAI_SAP_CLIENT);
		String client = Utils.isNullOrEmptyString(value) ? "700" //$NON-NLS-1$
				: (String) value;

		value = Setting.getSystemSetting(IModelConstants.S_EAI_SAP_USERID);
		String userid = Utils.isNullOrEmptyString(value) ? "ITFSAP" //$NON-NLS-1$
				: (String) value;

		value = Setting.getSystemSetting(IModelConstants.S_EAI_SAP_PASSWORD);
		String password = Utils.isNullOrEmptyString(value) ? "12392008" //$NON-NLS-1$
				: (String) value;

		value = Setting.getSystemSetting(IModelConstants.S_EAI_SAP_LANGUAGE);
		String lang = Utils.isNullOrEmptyString(value) ? "ZH" : (String) value; //$NON-NLS-1$

		value = Setting.getSystemSetting(IModelConstants.S_EAI_SAP_HOST);
		String host = Utils.isNullOrEmptyString(value) ? "172.16.9.74" //$NON-NLS-1$
				: (String) value;

		value = Setting
				.getSystemSetting(IModelConstants.S_EAI_SAP_INSTANCENUMBER);
		String instance = Utils.isNullOrEmptyString(value) ? "00" //$NON-NLS-1$
				: (String) value;

		Client mConnection = null;
		try {
			Pool pool = JCO.getClientPoolManager().getPool(POOL_NAME);
			if (pool == null) {
				JCO.addClientPool(POOL_NAME, // Alias for this pool
						maxConn, // Max. number of connections
						client, // SAP client
						userid, // userid
						password, // password
						lang, // language
						// "172.16.9.90", // host name
						// "01" );
						host, // host name
						instance);
			}
			mConnection = JCO.getClient(POOL_NAME);
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
		return mConnection;
	}
}