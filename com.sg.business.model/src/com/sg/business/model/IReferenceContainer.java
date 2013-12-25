package com.sg.business.model;

import org.bson.types.BasicBSONList;

public interface IReferenceContainer {

	/**
	 * 目标列表 {{targetid=ObjectId(""),targetclass="com.sg.business.model.Work",
	 * targeteditor="work.editor"},{}}
	 */
	
	public static final String SF_TARGET_NAME = "targetname"; //$NON-NLS-1$

	public static final String F_TARGETS = "targets"; //$NON-NLS-1$
	/**
	 * 目标对象
	 */
	public static final String SF_TARGET = "targetid"; //$NON-NLS-1$
	/**
	 * 目标类
	 */
	public static final String SF_TARGET_CLASS = "targetclass"; //$NON-NLS-1$
	/**
	 * 目标编辑器
	 */
	public static final String SF_TARGET_EDITOR = "targeteditor"; //$NON-NLS-1$
	/**
	 * 是否可以修改
	 */
	public static final String SF_TARGET_EDITABLE = "targeteditable"; //$NON-NLS-1$
	/**
	 * 使用何种方式打开，1 - DataObjectEditor, 2. DataObjectDialog, 3. DataObjectWizard
	 */
	public static final String SF_TARGET_EDITING_TYPE = "targeteditingtype"; //$NON-NLS-1$

	BasicBSONList getTargetList();

}