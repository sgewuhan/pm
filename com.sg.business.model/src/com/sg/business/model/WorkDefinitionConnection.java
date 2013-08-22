package com.sg.business.model;

import org.bson.types.ObjectId;
import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.resource.BusinessResource;

/**
 * ������ǰ���ù�ϵ<p/>
 * @author jinxitao
 *
 */
public class WorkDefinitionConnection extends PrimaryObject {

	/**
	 * ǰ�ýڵ�
	 */
	public static final String F_END1_ID = "end1_id";

	/**
	 * ���ýڵ�
	 */
	public static final String F_END2_ID = "end2_id";

	/**
	 * ��������,���Ϳ���ʹ��
	 * {@link #TYPE_FF},{@link #TYPE_FS},{@link #TYPE_SS},{@link #TYPE_SF}
	 */
	public static final String F_CONNECTIONTYPE = "connectiontype";

	/**
	 * ���ʱ��
	 */
	public static final String F_INTERVAL = "interval";

	/**
	 * ����
	 */
	public static final String F_OPERATOR = "operator";

	/**
	 * ʱ�䵥λ
	 */
	public static final String F_UNIT = "unit";

	/**
	 * ���ͣ�ǰ��������ɣ������������
	 */
	public static final String TYPE_FF = "FF";

	/**
	 * ���ͣ�ǰ��������ɣ���������ʼ
	 */
	public static final String TYPE_FS = "FS";

	/**
	 * ���ͣ�ǰ������ʼ����������ʼ
	 */
	public static final String TYPE_SS = "SS";

	/**
	 * ���ͣ�ǰ������ʼ�������������
	 */
	public static final String TYPE_SF = "SF";

	/**
	 * ʱ�䵥λ��һ��
	 */
	public static final int UNIT_WEEK = 7 * 24 * 60 * 60 * 1000;

	/**
	 * ʱ�䵥λ��һ��
	 */
	public static final int UNIT_DAY = 24 * 60 * 60 * 1000;

	/**
	 * ʱ�䵥λ��һСʱ
	 */
	public static final int UNIT_HOUR = 60 * 60 * 1000;

	/**
	 * ʱ�䵥λ��һ����
	 */
	public static final int UNIT_MINUTE = 60 * 1000;

	/**
	 * ��λʱ�䣬һ��
	 */
	public static final int UNIT_SECOND = 1000;

	/**
	 * ��Ŀģ��ID
	 */
	public static final String F_PROJECT_TEMPLATE_ID = "projecttemplate_id";

	/**
	 * ����ǰ��������༭��
	 */
	public static final String EDITOR = "editor.workDefinitionConnection";

	/**
	 * ��ȡ��ʾͼ��
	 */
	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_LINK_16);
	}

	/**
	 * ��ȡ��ʾ����
	 */
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

	/**
	 * ��ȡǰ������
	 * @return
	 */
	public WorkDefinition getEnd1() {
		ObjectId end1_id = (ObjectId) getValue(F_END1_ID);
		if (end1_id != null) {
			return ModelService
					.createModelObject(WorkDefinition.class, end1_id);
		} else {
			return null;
		}
	}

	/**
	 * ��ȡ��������
	 * @return
	 */
	public WorkDefinition getEnd2() {
		ObjectId end2_id = (ObjectId) getValue(F_END2_ID);
		if (end2_id != null) {
			return ModelService
					.createModelObject(WorkDefinition.class, end2_id);
		} else {
			return null;
		}
	}

	/**
	 * ��ȡ�������ͣ����������Ϳ�������
	 *  {@link #TYPE_FF},{@link #TYPE_FS},{@link #TYPE_SS},{@link #TYPE_SF}
	 * @return
	 */
	public String getConnectionType() {
		return (String) getValue(F_CONNECTIONTYPE);
	}

	/**
	 * ��ȡ���ʱ��
	 * @return
	 */
	public int getInterval() {
		int interval = ((Integer) getValue(F_INTERVAL)).intValue();
		Object op = getValue(F_OPERATOR);
		if ("-".equals(op)) {
			interval = -1 * interval;
		}

		Object unit = getValue(F_UNIT);
		if ("d".equals(unit)) {// ȡ��
			interval = interval * UNIT_DAY;
		} else if ("w".equals(unit)) {// ȡ��
			interval = interval * UNIT_WEEK;
		} else if ("h".equals(unit)) {// ȡСʱ
			interval = interval * UNIT_HOUR;
		} else if ("m".equals(unit)) {// ����
			interval = interval * UNIT_MINUTE;
		} else if ("s".equals(unit)) {// ��
			interval = interval * UNIT_SECOND;
		}
		return interval;
	}

	/**
	 * ���ݵ�λ����ȡ���ʱ��
	 * @param unit
	 *        ʱ�䵥λ
	 * @return
	 */
	public long getInterval(int unit) {
		return getInterval() / unit;
	}
}
