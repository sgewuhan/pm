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
	

	// The shared instance
	private static BusinessResource plugin;

	// The plug-in ID
	public static final String PLUGIN_ID = "com.sg.business.resource"; //$NON-NLS-1$

	public static final String IMAGE_FOLDER = "image";//$NON-NLS-1$

	public static final String IMAGE_ACTIVATED_16 = "activated_16.png";//$NON-NLS-1$
	public static final String IMAGE_DENIED_32 = "denied_32.png";//$NON-NLS-1$
	public static final String IMAGE_LOCK_16 = "lock_16.png";//$NON-NLS-1$
	public static final String IMAGE_OUTREP_16 = "out_rep_16.png";//$NON-NLS-1$

	public static final String IMAGE_ATTACHMENT_24 = "attachment_24.png";//$NON-NLS-1$

	public static final String IMAGE_DATABASE_16 = "database_16.png";//$NON-NLS-1$

	public static final String IMAGE_DISACTIVATED_16 = "disactivated_16.png";//$NON-NLS-1$

	public static final String IMAGE_DOCUMENT_16 = "document_16.png";//$NON-NLS-1$

	public static final String IMAGE_LINK_16 = "link_16.gif";//$NON-NLS-1$

	public static final String IMAGE_DOCUMENT_DEF_16 = "documentd_16.png";//$NON-NLS-1$

	public static final String IMAGE_PROJECT_16 = "project_16.png";//$NON-NLS-1$

	public static final String IMAGE_PREVIEW_24 = "preview_24.png";//$NON-NLS-1$

	public static final String IMAGE_FOLDER_16 = "folder_16.png";//$NON-NLS-1$

	public static final String IMAGE_ORG_16 = "org_16.png";//$NON-NLS-1$

	public static final String IMAGE_ORG_24 = "org_24.png";//$NON-NLS-1$

	public static final String IMAGE_ROLE_16 = "role_16.png";//$NON-NLS-1$

	public static final String IMAGE_ROLE2_16 = "role2_16.png";//$NON-NLS-1$

	public static final String IMAGE_SEARCH24 = "search_24.png";//$NON-NLS-1$

	public static final String IMAGE_TEAM_16 = "team_16.png";//$NON-NLS-1$

	public static final String IMAGE_TEAM_24 = "team_24.png";//$NON-NLS-1$

	public static final String IMAGE_TEMPLATE_16 = "template_16.gif";//$NON-NLS-1$

	public static final String IMAGE_USER_16 = "user_16.png";

	public static final String IMAGE_USER2_16 = "user2_16.png";

	public static final String IMAGE_VAULT_16 = "vault_16.png";

	public static final String IMAGE_VAULT1_16 = "vault1_16.png";

	public static final String IMAGE_VAULT2_16 = "vault2_16.png";

	public static final String IMAGE_WORK_16 = "work_16.gif";
	
	public static final String IMAGE_WORK_FINISH_16 = "work_finish_16.png";
	
	public static final String IMAGE_WORK_CANCEL_16 = "work_cancel_16.gif";
	
	public static final String IMAGE_WORK_PAUSE_16 = "work_pause_16.gif";
	
	public static final String IMAGE_WORK_WIP_16 = "work_wip_16.png";

	public static final String IMAGE_DELIVERABLE_16 = "deliverable_16.gif";

	public static final String IMAGE_PROJECT_32 = "project_32.png";

//	public static final String IMAGE_WORK_32x24 = "work_32x24.png";
//
//	public static final String IMAGE_WORK_CANCEL_32x24 = "work_cancel_32x24.png";
//
//	public static final String IMAGE_WORK_WIP_32x24 = "work_wip_32x24.png";
//	
//	public static final String IMAGE_WORK2_WIP_32x24 = "work2_wip_32x24.png";
//
//	public static final String IMAGE_WORK_PAUSE_32x24 = "work_pause_32x24.png";
//
//	public static final String IMAGE_WORK_FINISH_32x24 = "work_finish_32x24.png";
//
//	public static final String IMAGE_WORK_READY_32x24 = "work_ready_32x24.png";
//	
//	public static final String IMAGE_WORK2_READY_32x24 = "work2_ready_32x24.png";
	
	//
	public static final String IMAGE_WORK2_CANCEL_16 = "work2_cancel_16.png";

	public static final String IMAGE_WORK2_WIP_16 = "work2_wip_16.png";
	
	public static final String IMAGE_WORK2_PAUSE_16 = "work2_pause_16.png";

	public static final String IMAGE_WORK2_FINISH_16 = "work2_finish_16.png";

	public static final String IMAGE_WORK2_READY_16 = "work2_ready_16.png";
	
	//
	public static final String IMAGE_WF_WORK_CANCEL_10 = "workcancel_10.png";
	
	public static final String IMAGE_WF_WORK_READY_10 = "workready_10.png";
	
	public static final String IMAGE_WF_WORK_STOP_10 = "workstop_10.png";
	
	public static final String IMAGE_WF_WORK_CLOSE_10 = "workclose_10.png";
	
	public static final String IMAGE_WF_WORK_PROCESS_10 = "workprocess_10.png";

	public static final String IMAGE_ROLE3_16 = "role3_16.png";

	public static final String IMAGE_ROLE4_16 = "role4_16.png";

	public static final String IMAGE_WBS_SORT_24 = "wbssort_24.png";

	public static final String IMAGE_ASSIGNMENT_24 = "assignment_24.png";

	public static final String IMAGE_CHECK_24 = "check_24.png";

	public static final String IMAGE_WARNING_16 = "warning_16.gif";

	public static final String IMAGE_ERROR_16 = "error_16.gif";

	public static final String IMAGE_READY_16 = "ready_16.png";

	public static final String IMAGE_WIP_16 = "wip_16.png";

	public static final String IMAGE_FINISHED_16 = "finished_16.png";

	public static final String IMAGE_CANCELED_16 = "canceled_16.png";

	public static final String IMAGE_PAUSEED_16 = "paused_16.png";

	public static final String IMAGE_PREPARING_16 = "preparing_16.png";

	/*
	 * public static final String IMAGE_MESSAGE_32 = "message_32.png";
	 * 
	 * public static final String IMAGE_MESSAGE_ADD_32 = "message_add_32.png";
	 * 
	 * public static final String IMAGE_MESSAGE_REPLY_32 =
	 * "message_reply_32.png";
	 * 
	 * public static final String IMAGE_MESSAGE_OPEN_32 = "message_open_32.png";
	 */

	public static final String IMAGE_MESSAGE_24 = "message_24.png";

	public static final String IMAGE_STAR_14 = "star_14.png";

	public static final String IMAGE_MESSAGE_ADD_24 = "message_add_24.png";

	public static final String IMAGE_MESSAGE_REPLY_24 = "message_reply_24.png";

	public static final String IMAGE_MESSAGE_OPEN_24 = "message_open_24.png";
	
	public static final String IMAGE_MESSAGE_UNREAD_24 = "message_unread_24.png";

	public static final String IMAGE_FLOW_START_24 = "flow_start_24.png";

	public static final String IMAGE_FLOW_FINISH_24 = "flow_finish_24.png";

	public static final String IMAGE_FLOW_JOIN_24 = "flow_join_24.png";

	public static final String IMAGE_FLOW_SPLIT_24 = "flow_split_24.png";
	
	public static final String IMAGE_FLOW_ACTIVITIE_24 = "flow_activitie_24.png";
	
	public static final String IMAGE_FLOW_HUMAN_24 = "flow_human_24.png";
	
	public static final String IMAGE_MESSAGE_16 = "message_16.png";

	public static final String IMAGE_USER_24 = "user_24.png";
	
	public static final String IMAGE_VARIABLE_24 = "variable_24.png";

	public static final String IMAGE_24_BLANK = "24_blank.png";

	public static final String IMAGE_READY_24 = "ready_24.png";
	
	public static final String IMAGE_START_24 = "start_24.png";

	public static final String IMAGE_ALERT_24 = "alert_24.png";

	public static final String IMAGE_MARK_24 = "mark_24.png";
	
	public static final String IMAGE_WORK_ASSIGNMENT_24 = "work_assignment_24.png";
	
	public static final String IMAGE_FLAG_RED_24 = "flag_red_24.png";

	public static final String IMAGE_FLAG_YELLOW_24 = "flag_yellow_24.png";
	
	public static final String IMAGE_WORK_FILTER_24 = "work_filter_24.png";

	public static final String IMAGE_BULLETING_16 = "bulletin_16.png";

	public static final String IMAGE_TEMPLATE_72 = "template_72.png";
	
	public static final String IMAGE_SCHEDULE_72 = "schedule_72.png";
	
	public static final String IMAGE_WORKFLOW_72 = "workflow_72.png";

	public static final String IMAGE_REASSIGNMENT_16 = "reassignment_16.png";

	public static final String IMAGE_REASSIGNMENT_24 = "reassignment_24.png";

	public static final String IMAGE_FLAG_RED_16 = "flag_red_16.png";
	
	public static final String IMAGE_FLAG_YELLOW_16 = "flag_yellow_16.png";

	public static final String IMAGE_BALL_GRAY_1_16 = "gray_ball_1_16.png";

	public static final String IMAGE_BALL_BLUE_1_16 = "blue_ball_1_16.png";

	public static final String IMAGE_BALL_GREEN_1_16 = "green_ball_1_16.png";

	public static final String IMAGE_BALL_YELLOW_1_16 = "yellow_ball_1_16.png";

	public static final String IMAGE_BALL_RED_1_16 = "red_ball_1_16.png";

	public static final String IMAGE_LOG_24 = "log_24.png";
	
	public static final String IMAGE_SUMMARY_16 = "summary_16.png";

	public static final String IMAGE_RIGHT_16 = "right_16.png";

	public static final String IMAGE_NAVI_24 = "navigate_24.png";

	public static final String IMAGE_PROJECT_FINISH_16 = "project_finish_16.png";

	public static final String IMAGE_PROJECT_ONREADY_16 = "project_commit_16.png";

	public static final String IMAGE_PROJECT_CANCEL_16 = "project_stop_16.png";

	public static final String IMAGE_PROJECT_PAUSED_16 = "project_pause_16.png";

	public static final String IMAGE_PROJECT_WIP_16 = "project_start_16.png";

	public static final String IMAGE_FLOW_ACTIVITY_FINISH_24 = "human_complete_24.png";

	public static final String IMAGE_FLOW_ACTIVITY_UNFINISH_24 = "human_uncomplete_24.png";
	
	public static final String IMAGE_LOG_16 = "log_16.png";

	public static final String IMAGE_COMPANY_16 = "company_16.png";
	
	public static final String IMAGE_BU_16 = "business_unit_16.png";
	
	public static final String IMAGE_DEPTARTMENT_16 = "dept_16.png";
	
	public static final String IMAGE_WARNING_24 = "warning_24.png";

	public static final String IMAGE_PASS_32 = "pass_32.png";

//	public static final String IMAGE_DOCUMENT_24 = "document_24.png";

	public static final String IMAGE_FILE_24 = "file_24.png";

	public static final String IMAGE_DOWNLOAD_15X10 = "download_15x10.png";
	
	
	public static final String IMAGE_CREATEWORK_24 = "creatework_24.png";
	
	public static final String IMAGE_DELIVERABLECREATE_24 = "deliverablecreate_24.png";
	
	public static final String IMAGE_EDITWORK_24 = "editwork_24.png";

	public static final String IMAGE_REMOVE_24 = "remove_24.png";

	public static final String IMAGE_REFRESH_24 = "refresh_24.png";
	


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
