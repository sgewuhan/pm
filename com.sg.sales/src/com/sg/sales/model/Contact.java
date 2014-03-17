package com.sg.sales.model;

import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.file.RemoteFile;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.sg.sales.ISalesRole;
import com.sg.sales.ui.labelprovider.ContactCommonHTMLLable;
import com.sg.widgets.commons.labelprovider.CommonHTMLLabel;

public class Contact extends CompanyRelativeTeamControl implements ICompanyRelative,IContactable{
	
	
	public static final String F_LASTNAME = "lastname";
	public static final String F_MIDNAME = "midname";
	public static final String F_FIRSTNAME = "firstname";
	public static final String F_TEL1 = "tel_1";
	public static final String F_TEL2 = "tel_2";
	public static final String F_EMAIL = "email";
	public static final String F_PHOTO = "photo";
	public static final String F_DEPT = "dept";
	public static final String F_POSITION = "position";
	public static final String F_BIRTHDAY = "birthday";

	@Override
	public boolean doSave(IContext context) throws Exception {
		String firstname = getStringValue(F_FIRSTNAME);
		firstname = firstname==null?"":firstname;
		String midname = getStringValue(F_MIDNAME);
		midname = midname==null?"":midname;
		String lastname = getStringValue(F_LASTNAME);
		lastname = lastname==null?"":lastname;

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
		ObjectId companyId = getCompanyId();
		if(companyId!=null){
			return ModelService.createModelObject(Company.class, companyId);
		}else{
			return null;
		}
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

	@Override
	protected String[] getRoleDesignatedUserFieldName() {
		return DESIGNATED_FIELDS_BY_ROLE;
	}

	@Override
	protected String getRoleNumberDesignatedUserField(String field) {
		if (F_SALES_SUP.equals(field)) {
			return ISalesRole.SALES_SUPERVISOR_NUMBER;
		}
		return null;
	}

	
}
