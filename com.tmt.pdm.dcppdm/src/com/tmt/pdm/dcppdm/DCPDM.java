package com.tmt.pdm.dcppdm;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import dyna.framework.Client;
import dyna.framework.iip.ServiceNotFoundException;
import dyna.framework.service.ACL;
import dyna.framework.service.AUS;
import dyna.framework.service.DOS;
import dyna.framework.service.DSS;
import dyna.framework.service.DTM;
import dyna.framework.service.MSR;
import dyna.framework.service.NDS;
import dyna.framework.service.WFM;
import dyna.framework.service.WKS;

/**
 * The activator class controls the plug-in life cycle
 */
public class DCPDM extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.tmt.pdm.dcppdm"; //$NON-NLS-1$

	// The shared instance
	private static DCPDM plugin;

	public static Client dfw;

	public static DOS dos;

	public static NDS nds;

	public static DTM dtm;

	public static AUS aus;

	public static DSS dss;

	public static MSR msr;

	public static WFM wfm;

	public static ACL acl;

	public static WKS wks;

	/**
	 * The constructor
	 */
	public DCPDM() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		startPDMClient("admin", "superman123");
	}

	private void startPDMClient(String name, String password) throws ServiceNotFoundException {
		dfw = new Client();
		dos = (DOS) dfw.getServiceInstance("DF30DOS1");
		nds = (NDS) dfw.getServiceInstance("DF30NDS1");
		dtm = (DTM) dfw.getServiceInstance("DF30DTM1");
		aus = (AUS) dfw.getServiceInstance("DF30AUS1");
		dss = (DSS) dfw.getServiceInstance("DF30DSS1");
		msr = (MSR) dfw.getServiceInstance("DF30MSR1");
		wfm = (WFM) dfw.getServiceInstance("DF30WFM1");
		acl = (ACL) dfw.getServiceInstance("DF30ACL1");
		wks = (WKS) dfw.getServiceInstance("DF30WKS1");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static DCPDM getDefault() {
		return plugin;
	}
	
}
