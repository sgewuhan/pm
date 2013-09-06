package com.sg.business.commons.labelprovider;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.ModelService;
import com.sg.business.model.BulletinBoard;
import com.sg.business.model.Organization;
import com.sg.business.model.User;

public class PublisherLabelProvider extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		BulletinBoard bulletinboard = (BulletinBoard) element;
		StringBuffer sb = new StringBuffer();

		String punlisher =User.getUserById(bulletinboard.getPublisher()).getUsername();
		// �������
		SimpleDateFormat sdf = new SimpleDateFormat(Utils.SDF_DATE_COMPACT_SASH);
		Date date = bulletinboard.getPublishDate();
		String publishDate = sdf.format(date);
		
		String org = ((Organization)ModelService.createModelObject(Organization.class,
				bulletinboard.getOrganizationId())).getDesc();

		sb.append("<span style='padding-left:4px'>");
		sb.append("" + punlisher);
		sb.append("</span>");
		sb.append("<span style='float:right;padding-right:4px'>");
		sb.append("" + publishDate);
		sb.append("</span>");
		sb.append("<br/>");
		sb.append(""+org);

		return sb.toString();
	}

}
