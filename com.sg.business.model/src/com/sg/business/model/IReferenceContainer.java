package com.sg.business.model;

import org.bson.types.BasicBSONList;

public interface IReferenceContainer {

	/**
	 * 目标列表 {{targetid=ObjectId(""),targetclass="com.sg.business.model.Work",
	 * targeteditor="work.editor"},{}}
	 */
	
	public static final String SF_TARGET_NAME = "targetname";

	public static final String F_TARGETS = "targets";
	/**
	 * 目标对象
	 */
	public static final String SF_TARGET = "targetid";
	/**
	 * 目标类
	 */
	public static final String SF_TARGET_CLASS = "targetclass";
	/**
	 * 目标编辑器
	 */
	public static final String SF_TARGET_EDITOR = "targeteditor";
	/**
	 * 是否可以修改
	 */
	public static final String SF_TARGET_EDITABLE = "targeteditable";
	/**
	 * 使用何种方式打开，1 - DataObjectEditor, 2. DataObjectDialog, 3. DataObjectWizard
	 */
	public static final String SF_TARGET_EDITING_TYPE = "targeteditingtype";

	BasicBSONList getTargetList();

}