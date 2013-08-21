package com.sg.business.model;

import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.swt.graphics.Image;

import com.mobnut.db.file.RemoteFile;
import com.mobnut.db.file.RemoteFileSet;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.resource.BusinessResource;

public class Document extends PrimaryObject implements IProjectRelative{

	public static final String F_DOCUMENT_TYPE = "documenttype";
	
	public static final String F_DOCUMENT_NUMBER = "documentnumber";

	public static final String F__SUMMARY = "_summary";

	public static final String F_FOLDER_ID = "folder_id";

	public static final String F_VAULT = "vault";

	public static final String F_ATTACHMENT_CANNOT_EMPTY = "attachmentcannotempty";

	public static final String F_DESCRIPTION = "description";

	public static final String FILE_NAMESPACE = "vault_file";

	public static final String FILE_DB = "pm2";

	public ObjectId getParent_id() {
		return (ObjectId) getValue(F_FOLDER_ID);
	}
	
	public String getDocumentType(){
		return (String) getValue(F_DOCUMENT_TYPE);
	}
	
	public String getSummary(){
		return (String) getValue(F__SUMMARY);
	}
	
	public String getDocumentNumber(){
		return (String) getValue(F_DOCUMENT_NUMBER);
	}

	public RemoteFileSet getVault(){
		List<RemoteFile> files = getFileValue(F_VAULT);
		RemoteFileSet rs = new RemoteFileSet(getDbName(), getCollectionName());
		rs.setOriginalRemoteFileSet(files);
		rs.setUpdatedRemoteFileSet(files);
		return rs;
	}

	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_DOCUMENT_16);
	}

	@Override
	public boolean canEdit(IContext context) {
		return super.canEdit(context);
	}

	@Override
	public boolean canRead(IContext context) {
		return super.canRead(context);
	}

	
	
}
