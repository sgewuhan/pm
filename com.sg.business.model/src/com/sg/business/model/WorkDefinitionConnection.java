package com.sg.business.model;

import org.bson.types.ObjectId;
import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.resource.BusinessResource;

public class WorkDefinitionConnection extends PrimaryObject {

	public static final String F_END1_ID = "end1_id";

	public static final String F_END2_ID = "end2_id";

	public static final String F_CONNECTIONTYPE = "connectiontype";

	public static final String F_INTERVAL = "interval";

	public static final String F_OPERATOR = "operator";

	public static final String F_UNIT = "unit";

	public static final String TYPE_FF = "FF";

	public static final String TYPE_FS = "FS";

	public static final String TYPE_SS = "SS";

	public static final String TYPE_SF = "SF";

	public static final int UNIT_WEEK = 7 * 24 * 60 * 60 * 1000;

	public static final int UNIT_DAY = 24 * 60 * 60 * 1000;

	public static final int UNIT_HOUR = 60 * 60 * 1000;

	public static final int UNIT_MINUTE = 60 * 1000;

	public static final int UNIT_SECOND = 1000;

	public static final String F_PROJECT_TEMPLATE_ID = "projecttemplate_id";

	public static final String EDITOR = "editor.workDefinitionConnection";

	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_LINK_16);
	}

	@Override
	public String getLabel() {
		WorkDefinition end1 = getEnd1();
		WorkDefinition end2 = getEnd2();
		if(end1!=null&&end2!=null){
			
			String end1Label = Utils.getLimitLengthString(end1.getLabel(), 6);
			String end2Label = Utils.getLimitLengthString(end2.getLabel(), 6);
			
			String type = getConnectionType();
			
			long interval = getInterval(UNIT_DAY);
			
			return end1Label + " [" + type + " " + interval + "d] " + end2Label;
		}
		return null;
	}

	public WorkDefinition getEnd1() {
		ObjectId end1_id = (ObjectId) getValue(F_END1_ID);
		if (end1_id != null) {
			return ModelService
					.createModelObject(WorkDefinition.class, end1_id);
		} else {
			return null;
		}
	}

	public WorkDefinition getEnd2() {
		ObjectId end2_id = (ObjectId) getValue(F_END2_ID);
		if (end2_id != null) {
			return ModelService
					.createModelObject(WorkDefinition.class, end2_id);
		} else {
			return null;
		}
	}

	public String getConnectionType() {
		return (String) getValue(F_CONNECTIONTYPE);
	}

	public int getInterval() {
		int interval = ((Integer) getValue(F_INTERVAL)).intValue();
		Object op = getValue(F_OPERATOR);
		if ("-".equals(op)) {
			interval = -1 * interval;
		}

		Object unit = getValue(F_UNIT);
		if ("d".equals(unit)) {// 取天
			interval = interval * UNIT_DAY;
		} else if ("w".equals(unit)) {// 取周
			interval = interval * UNIT_WEEK;
		} else if ("h".equals(unit)) {// 取小时
			interval = interval * UNIT_HOUR;
		} else if ("m".equals(unit)) {// 分钟
			interval = interval * UNIT_MINUTE;
		} else if ("s".equals(unit)) {// 秒
			interval = interval * UNIT_SECOND;
		}
		return interval;
	}

	public long getInterval(int unit) {
		return getInterval() / unit;
	}
}
