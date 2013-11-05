package com.sg.business.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.graphics.Image;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.mobnut.admin.dataset.Setting;
import com.mobnut.commons.Commons;
import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.commons.util.file.GridFSFilePrevieweUtil;
import com.mobnut.db.file.RemoteFile;
import com.mobnut.db.file.RemoteFileSet;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.resource.BusinessResource;

/**
 * 文档 工作完成后产生的文档
 * 
 * @author jinxitao
 * 
 */
public class Document extends PrimaryObject implements IProjectRelative {

	public static final String[] DEFAULT_MAJOR_VID_SEQ = new String[] { "A",
			"B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
			"O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

	public static final String STATUS_RELEASED_ID = "released";
	public static final String STATUS_RELEASED_TEXT = "已发布";

	public static final String STATUS_WORKING_ID = "working";
	public static final String STATUS_WORKING_TEXT = "工作中";

	public static final String STATUS_DEPOSED_ID = "deposed";
	public static final String STATUS_DEPOSED_TEXT = "已废弃";

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

	public static final String F_WORK_ID = "work_id";

	public static final String F_MAJOR_VID = "major_vid";

	public static final String F_LIFECYCLE = "status";

	public static final String F_LOCK = "islocked";

	public static final String F_LOCKED_BY = "lockedby";

	public static final String F_LOCKED_ON = "lockdate";

	@Override
	protected String[] getVersionFields() {
		return new String[] { "$all" };
	}

	@Override
	public boolean doSave(IContext context) throws Exception {
		makeMajorVersionNumber();
		makeLifecycleStatus();
		boolean saved = super.doSave(context);

		generatePreview();

		return saved;
	}

	protected void generatePreview() {
		Job job = new Job("generate preview internal") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				OpenOfficeConnection connection = new SocketOpenOfficeConnection(
						Commons.getOfficeServerIP(), Commons.getOfficeSocket());
				try {
					connection.connect();
				} catch (ConnectException e) {
					return Status.CANCEL_STATUS;
				}

				// convert
				DocumentConverter converter = new OpenOfficeDocumentConverter(
						connection);

				File serverFile;
				File previewFile;
				ObjectId previewOid;

				List<RemoteFile> files = getGridFSFileValue(F_VAULT);
				if (files != null && files.size() > 0) {
					for (int i = 0; i < files.size(); i++) {
						RemoteFile remoteFile = files.get(i);
						GridFSFilePrevieweUtil previewUtil = new GridFSFilePrevieweUtil();
						previewUtil.setRemoteFile(remoteFile);

						if (previewUtil.isPreviewAvailable()) {
							continue;
						}
						String pathname = System.getProperty("user.dir")
								+ "/temp";
						try {
							serverFile = remoteFile.createServerFile(pathname);
						} catch (IOException e1) {
							continue;
						}

						previewOid = new ObjectId();
						String masterfileName = serverFile.getName();
						String previewFileName = masterfileName.substring(0,
								masterfileName.lastIndexOf(".")) + ".pdf";
						previewFile = new File(serverFile.getParent() + "/"
								+ previewFileName);

						// 如果目标路径不存在, 则新建该路径
						if (!previewFile.getParentFile().exists()) {
							previewFile.getParentFile().mkdirs();
						}

						converter.convert(serverFile, previewFile);
						remoteFile.setPreviewUploaded(previewFile, previewOid);
						try {
							remoteFile.addPreview();
						} catch (FileNotFoundException e) {
							continue;
						}

					}
				}
				connection.disconnect();
				return Status.OK_STATUS;
			}
		};
		job.schedule();
	}

	private void makeLifecycleStatus() {
		String lc = getLifecycle();
		if (Utils.isNullOrEmpty(lc)) {
			setValue(F_LIFECYCLE, STATUS_WORKING_ID);
		}
	}

	private void makeMajorVersionNumber() {
		Object mvid = getValue(F_MAJOR_VID);
		if (!(mvid instanceof String)) {
			String[] seq = getMajorVersionSeq();
			setValue(F_MAJOR_VID, seq[0]);
		}
	}

	private String[] getMajorVersionSeq() {
		String value = (String) Setting
				.getSystemSetting(IModelConstants.S_MAJOR_VID_SEQ);
		String[] seq = null;
		if (value != null) {
			try {
				seq = value.split(",");
			} catch (Exception e) {
			}
		}
		if (seq == null) {
			seq = DEFAULT_MAJOR_VID_SEQ;
		}
		return seq;
	}

	/**
	 * 返回所属文件夹_id
	 * 
	 * @return ObjectId
	 */
	public ObjectId getParent_id() {
		return (ObjectId) getValue(F_FOLDER_ID);
	}

	/**
	 * 返回文档类型
	 * 
	 * @return String
	 */
	public String getDocumentType() {
		return (String) getValue(F_DOCUMENT_TYPE);
	}

	/**
	 * 返回摘要信息
	 * 
	 * @return String
	 */
	public String getSummary() {
		return (String) getValue(F__SUMMARY);
	}

	/**
	 * 返回文档编号
	 * 
	 * @return String
	 */
	public String getDocumentNumber() {
		return (String) getValue(F_DOCUMENT_NUMBER);
	}

	/**
	 * 返回文档附件的存储目录
	 * 
	 * @return
	 */
	public RemoteFileSet getVault() {
		List<RemoteFile> files = getGridFSFileValue(F_VAULT);
		RemoteFileSet rs = new RemoteFileSet(getDbName(), getCollectionName());
		rs.setOriginalRemoteFileSet(files);
		rs.setUpdatedRemoteFileSet(files);
		return rs;
	}

	/**
	 * 返回显示图标
	 * 
	 * @return Image
	 */
	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_DOCUMENT_16);
	}

	/**
	 * 判断是否可以编辑
	 * 
	 * @return boolean
	 */
	@Override
	public boolean canEdit(IContext context) {
		String lc = getLifecycle();
		if (lc.equals(STATUS_WORKING_ID)) {
			if (isLocked()) {
				String userId = context.getAccountInfo().getConsignerId();
				String lockedBy = getStringValue(F_LOCKED_BY);
				return userId.equals(lockedBy);
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	/**
	 * 判断是否只读
	 * 
	 * @return boolean
	 */
	@Override
	public boolean canRead(IContext context) {
		return super.canRead(context);
	}

	/**
	 * 返回类型名称
	 * 
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return "文档";
	}

	/**
	 * 返回所属项目
	 * 
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
	 * 
	 * @return String
	 */
	@Override
	public String getDefaultEditorId() {
		String editorId = get_editor();
		if (Utils.isNullOrEmpty(editorId)) {
			return EDITOR;
		} else {
			return editorId;
		}
	}

	public void doMoveToOtherFolder(ObjectId get_id) throws Exception {
		DBCollection col = getCollection();
		WriteResult ws = col.update(
				new BasicDBObject().append(F__ID, get_id()),
				new BasicDBObject().append("$set",
						new BasicDBObject().append(F_FOLDER_ID, get_id)));

		checkWriteResult(ws);
	}

	public Folder getFolder() {
		ObjectId folderId = (ObjectId) getValue(F_FOLDER_ID);
		if (folderId != null) {
			return ModelService.createModelObject(Folder.class, folderId);
		} else {
			return null;
		}
	}

	public String getTypeIconURL() {
		String type = getDocumentType();
		if (!Utils.isNullOrEmpty(type)) {
			return FileUtil.getImageURL("doc_" + type + "_24.png",
					BusinessResource.PLUGIN_ID);
		}

		return FileUtil.getImageURL("doc_generic_24.png",
				BusinessResource.PLUGIN_ID);
	}

	public String getRevId() {
		Integer vid = getIntegerValue(F__VID);
		String release = getStringValue(F_MAJOR_VID);
		return release + "." + vid;
	}

	public String getLifecycle() {
		return getStringValue(F_LIFECYCLE);
	}

	public String getLifecycleName() {
		String lc = getStringValue(F_LIFECYCLE);
		if (STATUS_DEPOSED_ID.equals(lc)) {
			return STATUS_DEPOSED_TEXT;
		} else if (STATUS_RELEASED_ID.equals(lc)) {
			return STATUS_RELEASED_TEXT;
		} else if (STATUS_WORKING_ID.equals(lc)) {
			return STATUS_WORKING_TEXT;
		}
		return null;
	}

	public boolean isLocked() {
		return Boolean.TRUE.equals(getValue(F_LOCK));
	}

	public User getLockedBy() {
		String userId = getStringValue(F_LOCKED_BY);
		if (Utils.isNullOrEmpty(userId)) {
			return null;
		}
		return UserToolkit.getUserById(userId);
	}

	public Date getLockOn() {
		return getDateValue(F_LOCKED_ON);
	}

	public String getEditor() {
		return EDITOR;
	}

	@Override
	public boolean canDelete(IContext context) {
		return canEdit(context);
	}

}
