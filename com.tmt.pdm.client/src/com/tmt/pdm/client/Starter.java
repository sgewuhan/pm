package com.tmt.pdm.client;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import dyna.framework.Client;
import dyna.framework.service.ACL;
import dyna.framework.service.AUS;
import dyna.framework.service.DOS;
import dyna.framework.service.DSS;
import dyna.framework.service.DTM;
import dyna.framework.service.MSR;
import dyna.framework.service.NDS;
import dyna.framework.service.WFM;
import dyna.framework.service.WKS;

public class Starter implements BundleActivator {

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

	@Override
	public void start(BundleContext context) throws Exception {
		lazyLoadService();
	}

	private void lazyLoadService() {
		Thread t = new Thread(new Runnable(){

			@Override
			public void run() {
				try {
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
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}
			
		});
		t.start();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		dfw.close();
	}

}
