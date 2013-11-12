package com.sg.business.project;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class ProjectActivator implements BundleActivator {

	public static final String PLUGIN_ID = "com.sg.business.project";
	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		ProjectActivator.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		ProjectActivator.context = null;
	}

}
