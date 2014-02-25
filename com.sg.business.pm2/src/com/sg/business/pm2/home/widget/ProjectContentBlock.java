package com.sg.business.pm2.home.widget;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.file.RemoteFile;
import com.mongodb.DBObject;
import com.sg.business.model.Project;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.block.ContentBlock;
import com.sg.widgets.commons.model.IEditorInputFactory;
import com.sg.widgets.part.editor.DataObjectEditor;
import com.sg.widgets.registry.config.DataEditorConfigurator;

public class ProjectContentBlock extends ContentBlock {

	private Font font;
	private Project project;

	public ProjectContentBlock(Composite parent) {
		super(parent);
		font = new Font(getDisplay(), "Î¢ÈíÑÅºÚ", 10, SWT.NORMAL);

	}

	public void setProject(Project project) {
		this.project = project;
		RWT.getUISession(getDisplay()).exec(new Runnable() {
			
			@Override
			public void run() {
				Image img = getProjectCoverImage();
				setCoverImage(img);
				
				setHeadText(getProjectHead(false));
				setHoverHeadText(getProjectHead(true));
				
				setBodyText(getProjectBody(false));
				setHoverBodyText(getProjectBody(true));
				
				setFootText(getProjectFoot(false));
				setHoverFootText(getProjectFoot(true));
			}
		});
	}

	@Override
	protected void mouseClick() {
		IEditorInputFactory inputFactory = project.getAdapter(IEditorInputFactory.class);
		DataEditorConfigurator editorConfig = inputFactory.getEditorConfig(null);
		try {
			DataObjectEditor.open(project, editorConfig, true, null);
		} catch (Exception e) {
			MessageUtil.showToast(e);
		}
		
	}

	private String getProjectHead(boolean hoverMask) {
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='float:left;color:" + getColor1(hoverMask) + ";"
				+ "font-family:Î¢ÈíÑÅºÚ;font-size:10pt;"
				+ "display:-moz-inline-box; display:inline-block; " + "width:"
				+ (blockSize - 4) + "px;" + "height:" + (blockSize - 4) + "px;"
				+ "margin:0 2px 0 2px;" + "'>");
		//					+ "overflow:hidden;white-space:nowrap;text-overflow:ellipsis'>"); //$NON-NLS-1$
		String desc = project.getDesc();
		desc = Utils.getPlainText(desc);
		// desc = Utils.wrapShortTextSize(desc, blockSize - 28, 2, font, "");

		sb.append(desc);
		sb.append("</span>");

		return sb.toString();
	}

	private String getColor1(boolean hoverMask) {
		if (hoverMask) {
			return "#f0f0f0";
		}
		return "#909090";
	}

	private String getColor0(boolean hoverMask) {
		if (hoverMask) {
			return "#ffffff";
		}
		return "#000000";
	}

	private String getProjectBody(boolean hoverMask) {
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='float:left;text-align:"
				+ "center;color:"+getColor1(hoverMask)
				+ ";font-family:Î¢ÈíÑÅºÚ;font-size:11pt;font-weight:bold;"
				+ "width:" + (blockSize - 4) + "px; display:-moz-inline-box;"
				+ "display:inline-block;'>"); //$NON-NLS-1$
		sb.append(project.getLifecycleStatusText());
		sb.append("</span>");
		return sb.toString();
	}

	private String getProjectFoot(boolean hoverMask) {
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='font-family:Î¢ÈíÑÅºÚ;font-size:8pt;"
				+ "float:left;display:-moz-inline-box; display:inline-block;"
				+ "margin:0 2px 0 2px;" + "'>"); //$NON-NLS-1$
		Date _planStart = project.getPlanStart();
		Date _actualStart = project.getActualStart();
		Date _planFinish = project.getPlanFinish();
		String start = "?"; //$NON-NLS-1$
		String color = "";//$NON-NLS-1$
		if (_actualStart != null) {
			start = String
					.format(Utils.FORMATE_DATE_COMPACT_SASH, _actualStart);
			color = "color:" + getColor0(hoverMask) + ";";
		} else if (_planStart != null) {
			start = String.format(Utils.FORMATE_DATE_COMPACT_SASH, _planStart);
			color = "color:" + getColor1(hoverMask) + ";";//$NON-NLS-1$
		}
		sb.append("<small style='" + color + "'>"); //$NON-NLS-1$
		sb.append(start);
		sb.append("</small>");//$NON-NLS-1$
		sb.append("<small style='color:" + getColor1(hoverMask) + ";'>"); //$NON-NLS-1$

		String finish = "?"; //$NON-NLS-1$
		if (_planFinish != null) {
			finish = String
					.format(Utils.FORMATE_DATE_COMPACT_SASH, _planFinish);
		}
		sb.append("~"); //$NON-NLS-1$
		sb.append(finish);

		sb.append("</span>");

		return sb.toString();
	}

	private Image getProjectCoverImage() {
		List<RemoteFile> images = project.getCoverImages();
		Image img = null;
		if (images != null && images.size() > 0) {
			RemoteFile rf = images.get(0);
			DBObject imageObject = rf.getOutputRefData();
			String dbName = (String) imageObject.get("db"); //$NON-NLS-1$
			ObjectId oid = (ObjectId) imageObject.get("_id"); //$NON-NLS-1$
			String namespace = (String) imageObject.get("namespace"); //$NON-NLS-1$
			img = FileUtil.getImage(dbName, namespace, oid.toString());
		}
		// if(img == null){
		// String lc = project.getLifecycleStatus();
		// if(ILifecycle.STATUS_ONREADY_VALUE.equals(lc)){
		// img =
		// BusinessResource.getImage(BusinessResource.IMAGE_PROJECT_ONREADY_24);
		// }else if(ILifecycle.STATUS_WIP_VALUE.equals(lc)){
		// img =
		// BusinessResource.getImage(BusinessResource.IMAGE_PROJECT_WIP_24);
		// }else{
		// img =
		// BusinessResource.getImage(BusinessResource.IMAGE_PROJECT_PREPARING_24);
		// }
		// }
		return img;
	}

	@Override
	public void dispose() {
		font.dispose();
		super.dispose();
	}

}
