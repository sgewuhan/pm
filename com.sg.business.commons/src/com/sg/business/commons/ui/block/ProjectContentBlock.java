package com.sg.business.commons.ui.block;

import java.util.Date;

import org.eclipse.swt.widgets.Composite;

import com.mobnut.commons.util.Utils;
import com.sg.business.model.Project;
import com.sg.widgets.block.button.BusinessContentBlock;

public class ProjectContentBlock extends BusinessContentBlock {


	public ProjectContentBlock(Composite parent) {
		super(parent);
	}

	@Override
	protected String getBodyText(boolean hoverMask) {
		return ((Project) getInput()).getLifecycleStatusText();
	}

	@Override
	protected String getFootText(boolean hoverMask) {
		StringBuffer sb = new StringBuffer();
		Project project = (Project) getInput();
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
		
		return sb.toString();
	}

}
