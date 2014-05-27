package com.sg.business.management;

import java.util.Iterator;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.sg.widgets.Widgets;
import com.sg.widgets.registry.DataEditorRegistry;
import com.sg.widgets.registry.config.Configurator;

/**
 * The activator class controls the plug-in life cycle
 */
public class ManagementActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.sg.business.management"; //$NON-NLS-1$

	// The shared instance
	private static ManagementActivator plugin;
	
	
	/**
	 * The constructor
	 */
	public ManagementActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		/*DataEditorRegistry registry = Widgets.getEditorRegistry();
		Iterator<Configurator> iterator = registry.getConfigurators().iterator();
		while(iterator.hasNext()){
			System.out.println(iterator.next().getId());
		}*/
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
	public static ManagementActivator getDefault() {
		return plugin;
	}

}
