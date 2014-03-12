package com.sg.sales.model;

import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.file.RemoteFile;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.sales.ui.labelprovider.ContactCommonHTMLLable;
import com.sg.widgets.commons.labelprovider.CommonHTMLLabel;

public class Contact extends PrimaryObject implements ICompanyRelative{
	
	
	public static final String F_LASTNAME = "lastname";
	public static final String F_MIDNAME = "midname";
	public static final String F_FIRSTNAME = "firstname";
	public static final String F_TEL1 = "tel_1";
	public static final String F_TEL2 = "tel_2";
	public static final String F_EMAIL = "email";
	public static final String F_PHOTO = "photo";
	public static final String F_DEPT = "dept";
	public static final String F_POSITION = "position";

	@Override
	public boolean doSave(IContext context) throws Exception {
		String firstname = getStringValue(F_FIRSTNAME);
		String midname = getStringValue(F_MIDNAME);
		String lastname = getStringValue(F_LASTNAME);
		
		String desc = firstname+midname+lastname;
		
		setValue(F_DESC, desc);
		
		return super.doSave(context);
	}
	
	@Override
	public ObjectId getCompanyId() {
		return (ObjectId) getValue(F_COMPANY_ID);
	}

	@Override
	public Company getCompany() {
		return ModelService.createModelObject(Company.class, getCompanyId());
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> T getAdapter(Class<T> adapter) {
		if (adapter == CommonHTMLLabel.class) {
			return (T) (new ContactCommonHTMLLable(this));
		}
		return super.getAdapter(adapter);
	}

	public List<RemoteFile> getPhotos() {
		return getGridFSFileValue(F_PHOTO);
	}

	public String getName() {
		return getStringValue(F_DESC);
	}

	public String getEmail() {
		return getStringValue(F_EMAIL);
	}

	public String getOfficeTelNumber() {
		String tel1 = getStringValue(F_TEL1);
//		String tel2 = getStringValue(F_TEL2);
		return tel1;
	}

	public String getDepartment() {
		return getStringValue(F_DEPT);
	}

	public String getPosition() {
		return getStringValue(F_POSITION);
	}

	public String getFirstName() {
		return getStringValue(F_FIRSTNAME);
	}
	
	public String getLastName() {
		return getStringValue(F_LASTNAME);
	}
	
}
