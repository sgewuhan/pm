package com.sg.business.model;

import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.resource.BusinessResource;

/**
 * ��ɫ���û��Ĺ�ϵ<p/>
 * ����ɫ����ĳ���û����û��ͽ�ɫΪ��Զ�Ĺ�ϵ
 * @author zhonghua
 *
 */
public abstract class AbstractRoleAssignment extends PrimaryObject{

	/**
	 * �û�ID
	 *  @see #User
	 */
	public static final String F_USER_ID = "userid"; //$NON-NLS-1$
	
	/**
	 * �û�����
	 */
	public static final String F_USER_NAME = "username"; //$NON-NLS-1$
	
	/**
	 * ��ɫ����
	 */
	public static final String F_ROLE_NAME = "rolename"; //$NON-NLS-1$

	/**
	 * ��ɫ���
	 */
	public static final String F_ROLE_NUMBER = "rolenumber"; //$NON-NLS-1$
	
	/**
	 * ��ɫID
	 */
	public static final String F_ROLE_ID = "role_id"; //$NON-NLS-1$

	/**
	 * ��ȡ�û�����
	 * @return String
	 */
	public String getUsername() {
		return (String) getValue(F_USER_NAME);
	}
	
	/**
	 * ��ȡ�û�ID
	 * @return String
	 */
	public String getUserid() {
		return (String) getValue(F_USER_ID);
	}
	
	/**
	 * �Ƿ���Ա༭
	 */
	@Override
	public boolean canEdit(IContext context) {
		return false;
	}
	
	/**
	 * ��ȡ��ʾͼ��
	 */
	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_USER_16);
	}
	
	/**
	 * ������ʾ����
	 * @return String
	 */
	@Override
	public String getLabel() {
		String uid = getUserid();
		if(uid!=null){
			User user = UserToolkit.getUserById(uid);
			return user.getLabel();
		}
		return super.getLabel();
	}
	
}
