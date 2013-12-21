package com.sg.business.model;

import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.resource.BusinessResource;

/**
 * 角色和用户的关系<p/>
 * 将角色赋予某个用户，用户和角色为多对多的关系
 * @author zhonghua
 *
 */
public abstract class AbstractRoleAssignment extends PrimaryObject{

	/**
	 * 用户ID
	 *  @see #User
	 */
	public static final String F_USER_ID = "userid"; //$NON-NLS-1$
	
	/**
	 * 用户名称
	 */
	public static final String F_USER_NAME = "username"; //$NON-NLS-1$
	
	/**
	 * 角色名称
	 */
	public static final String F_ROLE_NAME = "rolename"; //$NON-NLS-1$

	/**
	 * 角色编号
	 */
	public static final String F_ROLE_NUMBER = "rolenumber"; //$NON-NLS-1$
	
	/**
	 * 角色ID
	 */
	public static final String F_ROLE_ID = "role_id"; //$NON-NLS-1$

	/**
	 * 获取用户名称
	 * @return String
	 */
	public String getUsername() {
		return (String) getValue(F_USER_NAME);
	}
	
	/**
	 * 获取用户ID
	 * @return String
	 */
	public String getUserid() {
		return (String) getValue(F_USER_ID);
	}
	
	/**
	 * 是否可以编辑
	 */
	@Override
	public boolean canEdit(IContext context) {
		return false;
	}
	
	/**
	 * 获取显示图标
	 */
	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_USER_16);
	}
	
	/**
	 * 返回显示内容
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
