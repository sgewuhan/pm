package com.sg.business.model;

import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.resource.BusinessResource;

/**
 * 交付物<p/>
 * 关联工作与工作完成后产生的文档
 * @author jinxitao
 *
 */
public class Deliverable extends PrimaryObject implements IProjectRelative{

	/**
	 * 工作_id字段，用于保存工作_id的值
	 */
	public static final String F_WORK_ID = "work_id";
	
	/**
	 * 
	 */
	public static final String F_MANDATORY = "mandatory";
	
	/**
	 * 文档_id字段，用于保存文档_id的值
	 */
	public static final String F_DOCUMENT_ID = "document_id";
	
	/**
	 * 交付物的编辑器
	 */
	public static final String EDITOR = null;

	/**
	 * 返回显示图标
	 */
	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_DELIVERABLE_16);
	}
	
	@Override
	public String getTypeName() {
		return "交付物";
	}

	@Override
	public String getDefaultEditorId() {
		return EDITOR;
	}
}
