package com.sg.business.pm2;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

import com.mobnut.db.DBActivator;
import com.mobnut.portal.Portal;
import com.mongodb.DB;
import com.sg.business.model.IModelConstants;

/**
 * The activator class controls the plug-in life cycle
 */
public class PM2Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.sg.business.pm2"; //$NON-NLS-1$

	// The shared instance
	private static PM2Activator plugin;
	
	/**
	 * The constructor
	 */
	public PM2Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		context.addBundleListener(new BundleListener() {
			
			@Override
			public void bundleChanged(BundleEvent event) {
				if(Portal.PLUGIN_ID.equals(event.getBundle().getSymbolicName())){
					//初始化
					initialPMSystem();
				}
			}
		});
	}


	protected void initialPMSystem() {
		ensureIndex();
	}

	private void ensureIndex() {
		DB db = DBActivator.getDB(IModelConstants.DB);
		//此处添加程序用于创建唯一索引
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
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
	public static PM2Activator getDefault() {
		return plugin;
	}
	

}
