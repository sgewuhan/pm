package com.sg.business.resource;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.internal.util.BundleUtility;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class BusinessResource extends AbstractUIPlugin {

	public static final String IMAGE_ACTIVATED_16 = "activated_16.png";

	public static final String IMAGE_DATABASE_16 = "database_16.png";

	public static final String IMAGE_DISACTIVATED_16 = "disactivated_16.png";
	
	public static final String IMAGE_DOCUMENT_16 = "document_16.png";
	
	public static final String IMAGE_DOCUMENT_DEF_16 = "documentd_16.png";

	public static final String IMAGE_FOLDER = "image";

	public static final String IMAGE_FOLDER_16 = "folder_16.png";

	public static final String IMAGE_ORG_16 = "org_16.png";

	public static final String IMAGE_ORG_24 = "org_24.png";

	public static final String IMAGE_ROLE_16 = "role_16.png";

	public static final String IMAGE_ROLE2_16 = "role2_16.png";

	public static final String IMAGE_SEARCH32 = "search_32.png";
	
	public static final String IMAGE_TEAM_16 = "team_16.png";

	public static final String IMAGE_TEAM_24 = "team_24.png";

	public static final String IMAGE_TEMPLATE_16 = "template_16.gif";

	public static final String IMAGE_USER_16 = "user_16.png";

	public static final String IMAGE_USER2_16 = "user2_16.png";

	public static final String IMAGE_VAULT_16 = "vault_16.png";
	
	public static final String IMAGE_VAULT1_16 = "vault1_16.png";
	
	public static final String IMAGE_VAULT2_16 = "vault2_16.png";
	
	public static final String IMAGE_WORK_16 = "work_16.gif";
	
	public static final String IMAGE_DELIVERABLE_16 = "deliverable_16.gif";


	// The shared instance
	private static BusinessResource plugin;

	// The plug-in ID
	public static final String PLUGIN_ID = "com.sg.business.resource"; //$NON-NLS-1$

	public static final String IMAGE_ROLE3_16 = "role3_16.png";
	
	public static final String IMAGE_ROLE4_16 = "role4_16.png";


	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static BusinessResource getDefault() {
		return plugin;
	}

	public static Image getImage(String key) {

		return getDefault().getImageRegistry().get(key);
	}

	public static ImageDescriptor getImageDescriptor(String key) {

		return getDefault().getImageRegistry().getDescriptor(key);
	}

	/**
	 * The constructor
	 */
	public BusinessResource() {
	}

	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		// 注册image目录下的所有文件
		Bundle bundle = Platform.getBundle(PLUGIN_ID);
		if (BundleUtility.isReady(bundle)) {

			URL fullPathString = BundleUtility.find(bundle, "image");
			try {
				File folder = new File(FileLocator.toFileURL(fullPathString)
						.getFile());
				File[] files = folder.listFiles();
				ImageDescriptor imgd;
				String key;
				for (File f : files) {
					key = f.getName();
					imgd = AbstractUIPlugin.imageDescriptorFromPlugin(
							PLUGIN_ID, "image/" + key);
					reg.put(key, imgd);
				}
			} catch (Exception e) {
			}
		}

		super.initializeImageRegistry(reg);
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
}
