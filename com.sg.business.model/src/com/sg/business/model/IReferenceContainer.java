package com.sg.business.model;

import org.bson.types.BasicBSONList;

public interface IReferenceContainer {

	/**
	 * Ŀ���б� {{targetid=ObjectId(""),targetclass="com.sg.business.model.Work",
	 * targeteditor="work.editor"},{}}
	 */
	public static final String F_TARGETS = "targets";
	/**
	 * Ŀ�����
	 */
	public static final String SF_TARGET = "targetid";
	/**
	 * Ŀ����
	 */
	public static final String SF_TARGET_CLASS = "targetclass";
	/**
	 * Ŀ��༭��
	 */
	public static final String SF_TARGET_EDITOR = "targeteditor";
	/**
	 * �Ƿ�����޸�
	 */
	public static final String SF_TARGET_EDITABLE = "targeteditable";
	/**
	 * ʹ�ú��ַ�ʽ�򿪣�1 - DataObjectEditor, 2. DataObjectDialog, 3. DataObjectWizard
	 */
	public static final String SF_TARGET_EDITING_TYPE = "targeteditingtype";
	public static final int EDITING_BY_EDITOR = 1;
	public static final int EDITING_BY_DIALOG = 2;
	public static final int EDITING_BY_WIZARD = 3;
	BasicBSONList getTargetList();

}