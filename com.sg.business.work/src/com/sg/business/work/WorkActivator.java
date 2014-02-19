package com.sg.business.work;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

import com.mobnut.admin.dataset.Setting;
import com.mobnut.portal.Portal;
import com.sg.business.model.IModelConstants;

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
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		context.addBundleListener(new BundleListener() {

			@Override
			public void bundleChanged(BundleEvent event) {
				if (Portal.PLUGIN_ID
						.equals(event.getBundle().getSymbolicName())) {
					// ��ʼ��
					startWorkSync();
				}
			}
		});
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

		stopWorkSync();
	}

	private void stopWorkSync() {
		sync.stop();
	}

	/**
	 * ͬ�������ĸ���
	 */
	public void startWorkSync() {
		if (sync == null) {
			sync = new WorkflowSynchronizer(false);
			Object data = Setting
					.getSystemSetting(IModelConstants.S_S_WORK_RESERVED_REFRESH_INTERVAL);
			int delay;
			try {
				delay = Integer.parseInt((String) data);
			} catch (Exception e) {
				delay = 10;
			}

			sync.start(delay * 60 * 1000, false);
		}
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
