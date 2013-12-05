package com.sg.business.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;
import org.eclipse.core.runtime.Assert;
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
import com.mobnut.db.DBActivator;
import com.mobnut.db.file.GridServerFile;
import com.mobnut.db.file.IServerFile;
import com.mobnut.db.file.RemoteFile;
import com.mobnut.db.file.RemoteFileSet;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.utils.DBUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
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
	/**
	 * 缺省主版本号
	 */
	public static final String[] DEFAULT_MAJOR_VID_SEQ = new String[] { "A",
			"B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
			"O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

	/**
	 * 已发布
	 */
	public static final String STATUS_RELEASED_ID = "released";
	public static final String STATUS_RELEASED_TEXT = "已发布";

	/**
	 * 工作中
	 */
	public static final String STATUS_WORKING_ID = "working";
	public static final String STATUS_WORKING_TEXT = "工作中";

	/**
	 * 已废弃
	 */
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

	/**
	 * 主版本号
	 */
	public static final String F_MAJOR_VID = "major_vid";

	/**
	 * 文档状态
	 */
	public static final String F_LIFECYCLE = "status";

	/**
	 * 是否锁定
	 */
	public static final String F_LOCK = "islocked";

	/**
	 * 锁定者
	 */
	public static final String F_LOCKED_BY = "lockedby";

	/**
	 * 锁定时间
	 */
	public static final String F_LOCKED_ON = "lockdate";

	/**
	 * 链接到PDM的ID
	 */
	public static final String F_PDM_OUID = "pdm_ouid";

	/**
	 * 审批历史
	 */
	public static final String F_WF_HISTORY = "wf_history";

	private static final String F_SECOND_VID = "svid";

	@Override
	protected String[] getVersionFields() {
		return new String[] { "$all" };
	}

	@Override
	public boolean doSave(IContext context) throws Exception {
		initVersionNumber();
		initVerStatus();
		checkDocumentNumber();
		boolean saved = super.doSave(context);

		generatePreview();

		return saved;
	}

	@Override
	public void doInsert(IContext context) throws Exception {
		generateCode();
		super.doInsert(context);
	}

	public  void generateCode() throws Exception {

		String documentNumber = getDocumentNumber();
		if (documentNumber != null) {
			return;
		}

		DBCollection ids = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C__IDS);

		String prefix = "";
		Project project = getProject();
		if (project != null) {
			prefix = project.getProjectNumber();
		} else {
			// 独立工作文档的编号，组织代码
			Work work = getWork();
			if (work != null && work.isStandloneWork()) {
				User charger = work.getCharger();
				Organization org = charger.getOrganization();
				prefix = org.getCode();
			}
		}
		int id = DBUtil.getIncreasedID(ids, IModelConstants.SEQ_DOCUMENT_NUMBER
				+ "." + prefix);
		String seq = String.format("%03d", id).toUpperCase();
		String codeValue = prefix + seq;
		setValue(F_DOCUMENT_NUMBER, codeValue);
	}

	private Work getWork() {
		ObjectId work_id = (ObjectId) getValue(F_WORK_ID);
		if (work_id != null) {
			return ModelService.createModelObject(Work.class, work_id);
		} else {
			return null;
		}
	}

	private void checkDocumentNumber() throws Exception {
		String documentNumber = getDocumentNumber();
		if (documentNumber != null) {
			BasicDBObject condition = new BasicDBObject();
			condition.put(Document.F_DOCUMENT_NUMBER, documentNumber);
			condition.put(Document.F__ID,
					new BasicDBObject().append("$ne", get_id()));
			long l = getRelationCountByCondition(Document.class, condition);
			if (l > 0) {
				throw new Exception("该文档编号已存在，请重新录入文档编号");
			}
		}
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

	public void initVerStatus() {
		String lc = getLifecycle();
		if (Utils.isNullOrEmpty(lc)) {
			setValue(F_LIFECYCLE, STATUS_WORKING_ID);
		}
	}

	public void initVersionNumber() {
		Object mvid = getValue(F_MAJOR_VID);
		if (!(mvid instanceof String)) {
			String[] seq = getMajorVersionSeq();
			setValue(F_MAJOR_VID, seq[0]);
		}
		Object svid = getValue(F_SECOND_VID);
		if (svid instanceof Integer) {
			setValue(F_SECOND_VID, new Integer(((Integer) svid).intValue() + 1));
		} else {
			setValue(F_SECOND_VID, new Integer(0));
		}
	}

	public void doUpdateVersion() {
		String major = (String) getValue(F_MAJOR_VID);
		String[] majorVersionSeq = getMajorVersionSeq();
		for (int i = 0; i < majorVersionSeq.length; i++) {
			if (majorVersionSeq[i].equals(major)) {
				major = majorVersionSeq[i + 1];
				break;
			}
		}

		setValue(F_MAJOR_VID, major);
		setValue(F_SECOND_VID, 0x0);
		setValue(F_LIFECYCLE, STATUS_WORKING_ID);
		DBCollection collection = getCollection();
		collection.update(
				new BasicDBObject().append(F__ID, get_id()),
				new BasicDBObject().append(
						"$set",
						new BasicDBObject().append(F_MAJOR_VID, major)
								.append(F_SECOND_VID, 0x0)
								.append(F_LIFECYCLE, STATUS_WORKING_ID)));
	}

	public String[] getMajorVersionSeq() {
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
	 * 返回文档类型
	 * 
	 * @return BasicBSONList
	 */
	public BasicBSONList getWorkflowHistory() {
		return (BasicBSONList) getValue(F_WF_HISTORY);
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
		// Integer vid = getIntegerValue(F__VID);
		Integer vid = getIntegerValue(F_SECOND_VID);
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

	/**
	 * 是否链接到了PDM
	 * 
	 * @return
	 */
	public boolean isLinkedPDM() {
		return getValue(F_PDM_OUID) != null;
	}

	public List<IServerFile> getServerFileValue() {
		if (isLinkedPDM()) {
			String name = getCollectionName();
			IFileServerDelegator fsd = ModelActivator
					.getFileServerDelegator(name);
			Assert.isNotNull(fsd, "没有定义文件服务器");
			return fsd.getFiles(this);
		} else {
			List<RemoteFile> remoteFiles = getGridFSFileValue(F_VAULT);
			List<IServerFile> result = new ArrayList<IServerFile>();
			for (int i = 0; i < remoteFiles.size(); i++) {
				result.add(new GridServerFile(remoteFiles.get(i)));
			}

			return result;
		}
	}

	public String getPDMOuid() {
		return getStringValue(F_PDM_OUID);
	}

	public void checkMandatory() throws Exception {
		// 检查文档编号
		String documentNumber = getDocumentNumber();
		if (Utils.isNullOrEmpty(documentNumber)) {
			throw new Exception("文档缺少编号：" + this);
		}

		// 检查文件
		List<IServerFile> sf = getServerFileValue();
		if (sf.isEmpty()) {
			throw new Exception("必须交付的文档缺少附件：" + this);
		}

	}

	public void doLock(IContext context) throws Exception {
		if (isLocked()) {
			return;
		}
		setValue(F_LOCK, Boolean.TRUE);
		setValue(F_LOCKED_BY, context.getAccountInfo().getConsignerId());
		Date newValue = new Date();
		setValue(F_LOCKED_ON, newValue);
		DBCollection col = getCollection();
		col.update(new BasicDBObject().append(F__ID, get_id()),
				new BasicDBObject().append(
						"$set",
						new BasicDBObject()
								.append(F_LOCK, Boolean.TRUE)
								.append(F_LOCKED_BY,
										context.getAccountInfo()
												.getConsignerId())
								.append(F_LOCKED_ON, newValue)));
	}

	public void doUnLock(IContext context) throws Exception {
		if (!isLocked()) {
			return;
		}
		setValue(F_LOCK, Boolean.FALSE);
		setValue(F_LOCKED_BY, null);
		setValue(F_LOCKED_ON, null);
		DBCollection col = getCollection();
		col.update(
				new BasicDBObject().append(F__ID, get_id()),
				new BasicDBObject().append(
						"$set",
						new BasicDBObject().append(F_LOCK, Boolean.FALSE)
								.append(F_LOCKED_BY, null)
								.append(F_LOCKED_ON, null)));
	}

	public void doSetLifeCycleStatus(IContext context, String status)
			throws Exception {
		String lc = getLifecycle();
		if (status.equals(lc)) {
			return;
		}
		setValue(F_LIFECYCLE, status);
		Date newValue = new Date();
		setValue(status + "_date", newValue);
		BasicDBObject object = new BasicDBObject().append(F_LIFECYCLE, status)
				.append(status + "_date", newValue);
		if (!STATUS_WORKING_ID.equals(getLifecycle())) {
			setValue(F_LOCK, Boolean.FALSE);
			setValue(F_LOCKED_BY, null);
			setValue(F_LOCKED_ON, null);
			object.put(F_LOCK, Boolean.FALSE);
			object.put(F_LOCKED_BY, null);
			object.put(F_LOCKED_ON, null);
		}
		DBCollection col = getCollection();
		col.update(new BasicDBObject().append(F__ID, get_id()),
				new BasicDBObject().append("$set", object));
	}

	@Override
	public String getLabel() {
		String desc = getDesc();
		desc = desc == null ? "" : desc;
		String num = getDocumentNumber();
		if (Utils.isNullOrEmpty(num)) {
			num = " * ";
		}
		String rev = getRevId();
		return desc + "|" + num + " [" + rev + "]";
	}

	public boolean canLock(IContext context) {
		boolean locked = isLocked();
		return !locked;
	}

	public boolean canUnLock(IContext context) {
		boolean locked = isLocked();
		String userId = context.getAccountInfo().getConsignerId();
		User user = getLockedBy();
		if (locked && user == null) {
			locked = false;
		}
		if (user != null && !userId.equals(user.getUserid())) {
			locked = false;
		}
		return locked;
	}

	public Date getReleaseOn() {
		return getDateValue(STATUS_RELEASED_ID + "_date");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<DBObject> getProcessHistory(long processId) {
		Object value = getValue(F_WF_HISTORY);
		if (value instanceof List) {
			for (int i = 0; i < ((List) value).size(); i++) {
				DBObject dbo = (DBObject) ((List) value).get(i);
				Object insid = dbo.get(IDocumentProcess.F_PROCESS_INSTANCEID);
				if (insid instanceof Long
						&& ((Long) insid).longValue() == processId) {
					return (List<DBObject>) dbo.get(IDocumentProcess.F_HISTORY);
				}
			}
		}
		return null;
	}

}
