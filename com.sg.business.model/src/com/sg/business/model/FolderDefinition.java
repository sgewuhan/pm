package com.sg.business.model;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;

public class FolderDefinition extends PrimaryObject {
	

	/**
	 * 项目模板id
	 */
	public static final String F_PROJECT_TEMPLATE_ID = "projecttemplate_id";
	
	/**
	 * 根文件夹id
	 */
	public static final String F_ROOT_ID = "root_id";
	
	/**
	 * 上级文件夹ID
	 */
	public static final String F_PARENT_ID = "parent_id"; //$NON-NLS-1$
	
	/**
	 * 是否是根文件夹
	 */
	public static final String F_IS_PROJECT_TEMPLATE_FOLDERROOT = "isfolderdroot";

	
	/**
	 * 新建子文件夹
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
