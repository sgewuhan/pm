package com.sg.business.model;

import org.bson.types.ObjectId;
import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.resource.BusinessResource;

/**
 * 工作的前后置关系<p/>
 * @author jinxitao
 *
 */
public class WorkDefinitionConnection extends PrimaryObject {

	/**
	 * 前置节点
	 */
	public static final String F_END1_ID = "end1_id";

	/**
	 * 后置节点
	 */
	public static final String F_END2_ID = "end2_id";

	/**
	 * 链接类型,类型可以使用
	 * {@link #TYPE_FF},{@link #TYPE_FS},{@link #TYPE_SS},{@link #TYPE_SF}
	 */
	public static final String F_CONNECTIONTYPE = "connectiontype";

	/**
	 * 间隔时间
	 */
	public static final String F_INTERVAL = "interval";

	/**
	 * 操作
	 */
	public static final String F_OPERATOR = "operator";

	/**
	 * 时间单位
	 */
	public static final String F_UNIT = "unit";

	/**
	 * 类型，前置任务完成，后置任务完成
	 */
	public static final String TYPE_FF = "FF";

	/**
	 * 类型，前置任务完成，后置任务开始
	 */
	public static final String TYPE_FS = "FS";

	/**
	 * 类型，前置任务开始，后置任务开始
	 */
	public static final String TYPE_SS = "SS";

	/**
	 * 类型，前置任务开始，后置任务结束
	 */
	public static final String TYPE_SF = "SF";

	/**
	 * 时间单位，一周
	 */
	public static final int UNIT_WEEK = 7 * 24 * 60 * 60 * 1000;

	/**
	 * 时间单位，一天
	 */
	public static final int UNIT_DAY = 24 * 60 * 60 * 1000;

	/**
	 * 时间单位，一小时
	 */
	public static final int UNIT_HOUR = 60 * 60 * 1000;

	/**
	 * 时间单位，一分钟
	 */
	public static final int UNIT_MINUTE = 60 * 1000;

	/**
	 * 单位时间，一秒
	 */
	public static final int UNIT_SECOND = 1000;

	/**
	 * 项目模板ID
	 */
	public static final String F_PROJECT_TEMPLATE_ID = "projecttemplate_id";

	/**
	 * 工作前后置任务编辑器
	 */
	public static final String EDITOR = "editor.workDefinitionConnection";

	/**
	 * 获取显示图标
	 */
	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_LINK_16);
	}

	/**
	 * 获取显示内容
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
	 * 获取前置任务
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
	 * 获取后置任务
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
	 * 获取链接类型，有四种类型可以设置
	 *  {@link #TYPE_FF},{@link #TYPE_FS},{@link #TYPE_SS},{@link #TYPE_SF}
	 * @return
	 */
	public String getConnectionType() {
		return (String) getValue(F_CONNECTIONTYPE);
	}

	/**
	 * 获取间隔时间
	 * @return
	 */
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

	/**
	 * 传递单位，获取间隔时间
	 * @param unit
	 *        时间单位
	 * @return
	 */
	public long getInterval(int unit) {
		return getInterval() / unit;
	}
}
