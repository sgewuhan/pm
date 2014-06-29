package com.sg.business.model;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;

public class FolderDefinition extends PrimaryObject {
	

	/**
	 * ��Ŀģ��id
	 */
	public static final String F_PROJECT_TEMPLATE_ID = "projecttemplate_id";
	
	/**
	 * ���ļ���id
	 */
	public static final String F_ROOT_ID = "root_id";
	
	/**
	 * �ϼ��ļ���ID
	 */
	public static final String F_PARENT_ID = "parent_id"; //$NON-NLS-1$
	
	/**
	 * �Ƿ��Ǹ��ļ���
	 */
	public static final String F_IS_PROJECT_TEMPLATE_FOLDERROOT = "isfolderdroot";

	
	/**
	 * �½����ļ���
	 * @param po
	 * @return
	 */
	public FolderDefinition makeFolderDefinition(FolderDefinition po) {
		if (po == null) {
			po = ModelService.createModelObject(new BasicDBObject(),
					FolderDefinition.class);
		}
		po.setValue(FolderDefinition.F_PARENT_ID, get_id());
		return po;
	}

}
