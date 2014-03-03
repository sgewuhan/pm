package com.sg.sales;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Sales extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.sg.sales"; //$NON-NLS-1$

	public static final String IMG_A = "a_16.png";

	public static final String IMG_B = "b_16.png";

	public static final String IMG_C = "c_16.png";

	public static final String IMG_D = "d_16.png";

	public static final String IMG_CLASS_A = "class_a.png";

	public static final String IMG_CLASS_B = "class_b.png";

	public static final String IMG_CLASS_C = "class_c.png";

	public static final String IMG_CLASS_D = "class_d.png";

	public static final String IMG_CLASS_X = "class_x.png";

	public static final String IMG_X = "x_16.png";

	public static final String IMG_TEL = "tel1_16.png";

	public static final String IMG_FAX = "fax1_16.png";

	public static final String C_COMPANY = "company";

	public static final String C_CONTRACT = "contract";

	public static final String C_CONTACT = "contact";

	public static final String C_OPPORTUNITY = "opportunity";

	public static final String C_POITEM = "purchaseitem";

	// The shared instance
	private static Sales plugin;
	
	/**
	 * The constructor
	 */
	public Sales() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
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
	public static Sales getDefault() {
		return plugin;
	}

}
