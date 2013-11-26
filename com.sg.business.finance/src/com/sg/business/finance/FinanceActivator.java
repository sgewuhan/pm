package com.sg.business.finance;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.sap.mw.jco.JCO;
import com.sap.mw.jco.JCO.Client;

/**
 * The activator class controls the plug-in life cycle
 */
public class FinanceActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.sg.business.finance"; //$NON-NLS-1$

	// The shared instance
	private static FinanceActivator plugin;

	private static Client sapClient;

	private static SAPConnectionPool connPool;
	
	
	/**
	 * The constructor
	 */
	public FinanceActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		connPool = new SAPConnectionPool();
	}
	
	public static Client getSAPClient(){
		if(sapClient==null||!sapClient.isValid()){
			sapClient = connPool.connSAP();
		}
		return sapClient;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		if(sapClient!=null&&sapClient.isValid()){
			sapClient.disconnect();
			JCO.releaseClient(sapClient);
		}
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static FinanceActivator getDefault() {
		return plugin;
	}

}
