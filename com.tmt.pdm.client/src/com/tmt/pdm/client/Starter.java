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

	private static final String WKS_SERVICE_ID = "DF30WKS1";

	private static final String ACL_SERVICE_ID = "DF30ACL1";

	private static final String WFM_SERVICE_ID = "DF30WFM1";

	private static final String MSR_SERVICE_ID = "DF30MSR1";

	private static final String DSS_SERVICE_ID = "DF30DSS1";

	private static final String AUS_SERVICE_ID = "DF30AUS1";

	private static final String DTM_SERVICE_ID = "DF30DTM1";

	private static final String NDS_SERVICE_ID = "DF30NDS1";

	private static final String DOS_SERVICE_ID = "DF30DOS1";

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

	private static Starter starter;

	
	
	@Override
	public void start(BundleContext context) throws Exception {
		Starter.starter = this;
		lazyLoadService();
	}

	public void lazyLoadService() {
		Thread t = new Thread(new Runnable(){

			@Override
			public void run() {
				destoryClient();
				createClient();				
			}

			
		});
		t.start();
	}

	private void destoryClient() {
		if(dfw!=null){
			dfw.close();
		}
	}

	public void lookupService(String serviceId) throws Exception{
		if(dfw==null){
			throw new Exception("ERROR DCPDM Client not start!");
		}
	}
	
	
	private void createClient() {
		try {
			dfw = new Client();
			dos = (DOS) dfw.getServiceInstance(DOS_SERVICE_ID);
			nds = (NDS) dfw.getServiceInstance(NDS_SERVICE_ID);
			dtm = (DTM) dfw.getServiceInstance(DTM_SERVICE_ID);
			aus = (AUS) dfw.getServiceInstance(AUS_SERVICE_ID);
			dss = (DSS) dfw.getServiceInstance(DSS_SERVICE_ID);
			msr = (MSR) dfw.getServiceInstance(MSR_SERVICE_ID);
			wfm = (WFM) dfw.getServiceInstance(WFM_SERVICE_ID);
			acl = (ACL) dfw.getServiceInstance(ACL_SERVICE_ID);
			wks = (WKS) dfw.getServiceInstance(WKS_SERVICE_ID);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		dfw.close();
	}

	public static Starter getStarter() {
		return starter;
	}


}
