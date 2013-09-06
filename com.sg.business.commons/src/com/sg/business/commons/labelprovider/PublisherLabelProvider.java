package com.sg.business.commons.labelprovider;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.ModelService;
import com.sg.business.model.BulletinBoard;
import com.sg.business.model.Organization;

public class PublisherLabelProvider extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		BulletinBoard bulletinboard = (BulletinBoard) element;
		StringBuffer sb = new StringBuffer();

		String punlisher = bulletinboard.getPublisher();
		// Ìí¼ÓÈÕÆÚ
		SimpleDateFormat sdf = new SimpleDateFormat(Utils.SDF_DATE_COMPACT_SASH);
		Date date = bulletinboard.getPublishDate();
		String publishDate = sdf.format(date);
		
		String org = ((Organization)ModelService.createModelObject(Organization.class,
				bulletinboard.getOrganizationId())).getDesc();

		sb.append("<span style='padding-right:4px'>");
		sb.append("" + punlisher + ";" + publishDate);
		sb.append("</span>");
		sb.append("<br/>");
		sb.append("<span style='padding-right:4px'>");
		sb.append(""+org);
		sb.append("</span>");

		return sb.toString();
	}

}
