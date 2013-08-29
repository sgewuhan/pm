package com.sg.business.model;

import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.file.RemoteFile;
import com.mobnut.db.file.RemoteFileSet;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.resource.BusinessResource;

/**
 * 文档
 * 工作完成后产生的文档
 * @author jinxitao
 *
 */
public class Document extends PrimaryObject implements IProjectRelative{

	/**
	 * 文档类型
	 */
	public static final String F_DOCUMENT_TYPE = "documenttype";
	
	/**
	 * 文档编号
	 */
	public static final String F_DOCUMENT_NUMBER = "documentnumber";

	/**
	 * 摘要信息
	 */
	public static final String F__SUMMARY = "_summary";

	/**
	 * 文件夹_id字段值
	 */
	public static final String F_FOLDER_ID = "folder_id";

	/**
	 * 文档附件的存储位置
	 */
	public static final String F_VAULT = "vault";

	/**
	 * 附件不能为空
	 */
	public static final String F_ATTACHMENT_CANNOT_EMPTY = "attachmentcannotempty";

	/**
	 * 文档描述
	 */
	public static final String F_DESCRIPTION = "description";

	/**
	 * 文件命名空间
	 */
	public static final String FILE_NAMESPACE = "vault_file";

	/**
	 * 数据库名称
	 */
	public static final String FILE_DB = "pm2";
	
	/**
	 * 文档编辑器ID
	 */
	public static final String EDITOR = "editor.document.generic";


	/**
	 * 返回所属文件夹_id
	 * @return  ObjectId
	 */
	public ObjectId getParent_id() {
		return (ObjectId) getValue(F_FOLDER_ID);
	}
	
	/**
	 * 返回文档类型
	 * @return String
	 */
	public String getDocumentType(){
		return (String) getValue(F_DOCUMENT_TYPE);
	}
	
	/**
	 * 返回摘要信息
	 * @return String
	 */
	public String getSummary(){
		return (String) getValue(F__SUMMARY);
	}
	
	/**
	 * 返回文档编号 
	 * @return String
	 */
	public String getDocumentNumber(){
		return (String) getValue(F_DOCUMENT_NUMBER);
	}

	/**
	 * 返回文档附件的存储目录
	 * @return
	 */
	public RemoteFileSet getVault(){
		List<RemoteFile> files = getFileValue(F_VAULT);
		RemoteFileSet rs = new RemoteFileSet(getDbName(), getCollectionName());
		rs.setOriginalRemoteFileSet(files);
		rs.setUpdatedRemoteFileSet(files);
		return rs;
	}

	/**
	 * 返回显示图标
	 * @return Image
	 */
	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_DOCUMENT_16);
	}

	/**
	 * 判断是否可以编辑
	 * @return boolean
	 */
	@Override
	public boolean canEdit(IContext context) {
		return super.canEdit(context);
	}

	/**
	 * 判断是否只读
	 * @return boolean
	 */
	@Override
	public boolean canRead(IContext context) {
		return super.canRead(context);
	}

	/**
	 * 返回类型名称
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return "文档";
	}

	/**
	 * 返回所属项目
	 * @return Project
	 */
	@Override
	public Project getProject() {
		ObjectId ptId = (ObjectId) getValue(F_PROJECT_ID);
		if (ptId != null) {
			return ModelService.createModelObject(Project.class, ptId);
		} else {
			return null;
		}
	}
	
	/**
	 * 返回默认编辑器ID
	 * @return String
	 */
	@Override
	public String getDefaultEditorId() {
		String editorId = get_editor();
		if(Utils.isNullOrEmpty(editorId)){
			return EDITOR;
		}else{
			return editorId;
		}
	}
}
