package com.sg.business.visualization;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.sg.business.model.etl.OrganizationETL;

/**
 * The activator class controls the plug-in life cycle
 */
public class VisualizationActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.sg.business.visualization"; //$NON-NLS-1$

	// The shared instance
	private static VisualizationActivator plugin;

	/**
	 * The constructor
	 */
	public VisualizationActivator() {
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
		
		final Job job= new Job("读取绩效数据"){
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					new OrganizationETL().run();
				} catch (Exception e) {
				}
				return Status.OK_STATUS;
			}
		};
		job.schedule(10000);
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
	public static VisualizationActivator getDefault() {
		return plugin;
	}

}
