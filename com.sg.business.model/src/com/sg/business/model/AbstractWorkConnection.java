package com.sg.business.model;

import org.bson.types.ObjectId;
import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.resource.BusinessResource;

/**
 * ������ǰ���ù�ϵ<p/>
 * @author zhonghua
 *
 */
public abstract class AbstractWorkConnection extends PrimaryObject {

	/**
	 * ǰ�ýڵ�
	 */
	public static final String F_END1_ID = "end1_id"; //$NON-NLS-1$

	/**
	 * ���ýڵ�
	 */
	public static final String F_END2_ID = "end2_id"; //$NON-NLS-1$

	/**
	 * ��������
	 */
	public static final String F_CONNECTIONTYPE = "connectiontype"; //$NON-NLS-1$

	/**
	 * ���
	 */
	public static final String F_INTERVAL = "interval"; //$NON-NLS-1$

	/**
	 * ����
	 */
	public static final String F_OPERATOR = "operator"; //$NON-NLS-1$

	/**
	 * ʱ�䵥λ
	 */
	public static final String F_UNIT = "unit"; //$NON-NLS-1$

	/**
	 * ���ͣ�ǰ��������ɣ������������
	 */
	public static final String TYPE_FF = "FF"; //$NON-NLS-1$

	/**
	 * ���ͣ�ǰ��������ɣ���������ʼ
	 */
	public static final String TYPE_FS = "FS"; //$NON-NLS-1$

	/**
	 * ���ͣ�ǰ������ʼ����������ʼ
	 */
	public static final String TYPE_SS = "SS"; //$NON-NLS-1$

	/**
	 * ���ͣ�ǰ������ʼ�������������
	 */
	public static final String TYPE_SF = "SF"; //$NON-NLS-1$

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
	 * ������ʾͼ��
	 */
	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_LINK_16);
	}

	/**
	 * ������ʾ����
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
			
			return end1Label + " [" + type + " " + interval + "d] " + end2Label; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return null;
	}

	/**
	 * ����ǰ�ýڵ�
	 * @return WorkDefinition
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
	 * ���غ��ýڵ�
	 * @return WorkDefinition
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
	 * ������������
	 * @return String
	 */
	public String getConnectionType() {
		return (String) getValue(F_CONNECTIONTYPE);
	}

	/**
	 * ���ؼ��ʱ��
	 * @return int
	 */
	public int getInterval() {
		int interval = ((Integer) getValue(F_INTERVAL)).intValue();
		Object op = getValue(F_OPERATOR);
		if ("-".equals(op)) { //$NON-NLS-1$
			interval = -1 * interval;
		}

		Object unit = getValue(F_UNIT);
		if ("d".equals(unit)) {// ȡ�� //$NON-NLS-1$
			interval = interval * UNIT_DAY;
		} else if ("w".equals(unit)) {// ȡ�� //$NON-NLS-1$
			interval = interval * UNIT_WEEK;
		} else if ("h".equals(unit)) {// ȡСʱ //$NON-NLS-1$
			interval = interval * UNIT_HOUR;
		} else if ("m".equals(unit)) {// ���� //$NON-NLS-1$
			interval = interval * UNIT_MINUTE;
		} else if ("s".equals(unit)) {// �� //$NON-NLS-1$
			interval = interval * UNIT_SECOND;
		}
		return interval;
	}

	/**
	 * ����ʱ�䵥λ�����ؼ��ʱ��
	 * @param unit
	 *          ,ʱ�䵥λ
	 * @return
	 */
	public long getInterval(int unit) {
		return getInterval() / unit;
	}
}
