package com.sg.business.pm2;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

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
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
//		context.addBundleListener(new BundleListener() {
//
//			@Override
//			public void bundleChanged(BundleEvent event) {
//				if (Portal.PLUGIN_ID
//						.equals(event.getBundle().getSymbolicName())) {
//					// 初始化
//					initialPMSystem();
//				}
//			}
//		});
	}

//	protected void initialPMSystem() {
//		// 正式发行需删除本程序
//		mntRemoveBeforeRelease();
//	}
//
//	private void mntRemoveBeforeRelease() {
//		DB db = DBActivator.getDB(IModelConstants.DB);
//		DBCollection col = db.getCollection(IModelConstants.C_WORKS_ALLOCATE);
//		col.update(
//				new BasicDBObject(),
//				new BasicDBObject().append("$rename",
//						new BasicDBObject().append("projectid", "project_id")));
//		col = db.getCollection(IModelConstants.C_WORKS_PERFORMENCE);
//		col.update(
//				new BasicDBObject(),
//				new BasicDBObject().append("$rename",
//						new BasicDBObject().append("projectid", "project_id")));
//
//	}


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
	public static PM2Activator getDefault() {
		return plugin;
	}

}
