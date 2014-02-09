package com.sg.business.model;

import java.io.File;
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
import com.sg.business.model.commonlabel.DocumentCommonHTMLLable;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.resource.BusinessResource;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.commons.labelprovider.CommonHTMLLabel;

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
	public static final String[] DEFAULT_MAJOR_VID_SEQ = new String[] { "A", //$NON-NLS-1$
			"B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$
			"O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$

	/**
	 * 已发布
	 */
	public static final String STATUS_RELEASED_ID = "released"; //$NON-NLS-1$
	public static final String STATUS_RELEASED_TEXT = Messages.get().Document_0;

	/**
	 * 工作中
	 */
	public static final String STATUS_WORKING_ID = "working"; //$NON-NLS-1$
	public static final String STATUS_WORKING_TEXT = Messages.get().Document_1;

	/**
	 * 已废弃
	 */
	public static final String STATUS_DEPOSED_ID = "deposed"; //$NON-NLS-1$
	public static final String STATUS_DEPOSED_TEXT = Messages.get().Document_2;

	/**
	 * 文档类型
	 */
	public static final String F_DOCUMENT_TYPE = "documenttype"; //$NON-NLS-1$

	/**
	 * 文档编号
	 */
	public static final String F_DOCUMENT_NUMBER = "documentnumber"; //$NON-NLS-1$

	/**
	 * 摘要信息
	 */
	public static final String F__SUMMARY = "_summary"; //$NON-NLS-1$

	/**
	 * 文件夹_id字段值
	 */
	public static final String F_FOLDER_ID = "folder_id"; //$NON-NLS-1$

	/**
	 * 文档附件的存储位置
	 */
	public static final String F_VAULT = "vault"; //$NON-NLS-1$

	/**
	 * 附件不能为空
	 */
	public static final String F_ATTACHMENT_CANNOT_EMPTY = "attachmentcannotempty"; //$NON-NLS-1$

	/**
	 * 文档描述
	 */
	public static final String F_DESCRIPTION = "description"; //$NON-NLS-1$

	/**
	 * 文件命名空间
	 */
	public static final String FILE_NAMESPACE = "vault_file"; //$NON-NLS-1$

	/**
	 * 数据库名称
	 */
	public static final String FILE_DB = "pm2"; //$NON-NLS-1$

	/**
	 * 文档编辑器ID
	 */
	public static final String EDITOR = "editor.document.generic"; //$NON-NLS-1$

	public static final String F_WORK_ID = "work_id"; //$NON-NLS-1$

	/**
	 * 主版本号
	 */
	public static final String F_MAJOR_VID = "major_vid"; //$NON-NLS-1$

	/**
	 * 文档状态
	 */
	public static final String F_LIFECYCLE = "status"; //$NON-NLS-1$

	/**
	 * 是否锁定
	 */
	public static final String F_LOCK = "islocked"; //$NON-NLS-1$

	/**
	 * 锁定者
	 */
	public static final String F_LOCKED_BY = "lockedby"; //$NON-NLS-1$

	/**
	 * 锁定时间
	 */
	public static final String F_LOCKED_ON = "lockdate"; //$NON-NLS-1$

	/**
	 * 链接到PDM的ID
	 */
	public static final String F_PDM_OUID = "pdm_ouid"; //$NON-NLS-1$

	/**
	 * 审批历史
	 */
	public static final String F_WF_HISTORY = "wf_history"; //$NON-NLS-1$

	public static final String F_SECOND_VID = "svid"; //$NON-NLS-1$

	@Override
	protected String[] getVersionFields() {
		return new String[] { "$all" }; //$NON-NLS-1$
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
		generateCode(getWork());
		super.doInsert(context);
	}

	public void generateCode(Work work) throws Exception {

		String documentNumber = getDocumentNumber();
		if (documentNumber != null) {
			return;
		}

		DBCollection ids = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C__IDS);

		String prefix = ""; //$NON-NLS-1$
		Project project = getProject();
		if (project != null) {
			prefix = project.getProjectNumber();
		} else {
			// 独立工作文档的编号，公司代码
			if (work != null && work.isStandloneWork()) {
				User charger = work.getCharger();
				if (charger != null) {
					Organization org = charger.getOrganization();
					prefix = org.getCompanyCode();
					while (Utils.isNullOrEmptyString(prefix)) {
						org = (Organization) org.getParentOrganization();
						prefix = org.getCompanyCode();
					}
				}
			}
		}
		int id = DBUtil.getIncreasedID(ids, IModelConstants.SEQ_DOCUMENT_NUMBER
				+ "." + prefix); //$NON-NLS-1$
//		String seq = String.format("%06d", id).toUpperCase(); //$NON-NLS-1$  cctec
		String seq = String.format("%03d", id).toUpperCase(); //$NON-NLS-1$
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
					new BasicDBObject().append("$ne", get_id())); //$NON-NLS-1$
			long l = getRelationCountByCondition(Document.class, condition);
			if (l > 0) {
				throw new Exception(Messages.get().Document_3);
			}
		}
	}

	protected void generatePreview() {
		Job job = new Job("generate preview internal") { //$NON-NLS-1$
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
						String pathname = System.getProperty("user.dir") //$NON-NLS-1$
								+ "/temp"; //$NON-NLS-1$
						try {
							serverFile = remoteFile.createServerFile(pathname);
						} catch (IOException e1) {
							continue;
						}

						previewOid = new ObjectId();
						String masterfileName = serverFile.getName();
						String previewFileName = masterfileName.substring(0,
								masterfileName.lastIndexOf(".")) + ".pdf"; //$NON-NLS-1$ //$NON-NLS-2$
						previewFile = new File(serverFile.getParent() + "/" //$NON-NLS-1$
								+ previewFileName);

						// 如果目标路径不存在, 则新建该路径
						if (!previewFile.getParentFile().exists()) {
							previewFile.getParentFile().mkdirs();
						}

						try {
							converter.convert(serverFile, previewFile);
							remoteFile.setPreviewUploaded(previewFile,
									previewOid);
							remoteFile.addPreview();
						} catch (Exception e) {
							e.printStackTrace();
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
						"$set", //$NON-NLS-1$
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
				seq = value.split(","); //$NON-NLS-1$
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
		return "文档"; //$NON-NLS-1$
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

	@SuppressWarnings("unchecked")
	public <T> T getAdapter(Class<T> adapter) {
		if (adapter == CommonHTMLLabel.class){
			return (T)(new DocumentCommonHTMLLable(this));
		}
		return super.getAdapter(adapter);
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
				new BasicDBObject().append("$set", //$NON-NLS-1$
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
			return FileUtil.getImageURL("doc_" + type + "_24.png", //$NON-NLS-1$ //$NON-NLS-2$
					BusinessResource.PLUGIN_ID);
		}

		return FileUtil.getImageURL("doc_generic_24.png", //$NON-NLS-1$
				BusinessResource.PLUGIN_ID);
	}

	public String getRevId() {
		// Integer vid = getIntegerValue(F__VID);
		Integer vid = getIntegerValue(F_SECOND_VID);
		String release = getStringValue(F_MAJOR_VID);
		return release + "." + vid; //$NON-NLS-1$
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
			Assert.isNotNull(fsd, Messages.get().Document_4);
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
			throw new Exception(Messages.get().Document_5 + this);
		}

		// 检查文件
		List<IServerFile> sf = getServerFileValue();
		if (sf.isEmpty()) {
			throw new Exception(Messages.get().Document_6 + this);
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
						"$set", //$NON-NLS-1$
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
						"$set", //$NON-NLS-1$
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
		setValue(status + "_date", newValue); //$NON-NLS-1$
		BasicDBObject object = new BasicDBObject().append(F_LIFECYCLE, status)
				.append(status + "_date", newValue); //$NON-NLS-1$
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
				new BasicDBObject().append("$set", object)); //$NON-NLS-1$
	}

	@Override
	public String getLabel() {
		String desc = getDesc();
		desc = desc == null ? "" : desc; //$NON-NLS-1$
		String num = getDocumentNumber();
		if (Utils.isNullOrEmpty(num)) {
			num = " * "; //$NON-NLS-1$
		}
		String rev = getRevId();
		return desc + "|" + num + " [" + rev + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
		return getDateValue(STATUS_RELEASED_ID + "_date"); //$NON-NLS-1$
	}

	@SuppressWarnings({ "rawtypes" })
	public DBObject getProcessHistory(long processId) {
		Object value = getValue(F_WF_HISTORY);
		if (value instanceof List) {
			for (int i = 0; i < ((List) value).size(); i++) {
				DBObject dbo = (DBObject) ((List) value).get(i);
				Object insid = dbo.get(IDocumentProcess.F_PROCESS_INSTANCEID);
				if (insid instanceof Long
						&& ((Long) insid).longValue() == processId) {
					return dbo;
				}
			}
		}
		return null;
	}

}
