package com.sg.business.work;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class WorkActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.sg.business.work"; //$NON-NLS-1$

	// The shared instance
	private static WorkActivator plugin;

	private WorkflowSynchronizer sync;

	/**
	 * The constructor
	 */
	public WorkActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		startWorkSync();
	}
	


	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
		stopWorkSync();
	}

	private void stopWorkSync() {
		sync.stop();
	}
	


	/**
	 * 同步工作的更新
	 */
	private void startWorkSync() {
		sync = new WorkflowSynchronizer();
		sync.start(5*60*1000);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static WorkActivator getDefault() {
		return plugin;
	}

}
