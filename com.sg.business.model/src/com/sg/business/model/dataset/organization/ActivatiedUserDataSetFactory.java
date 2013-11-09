package com.sg.business.model.dataset.organization;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.User;

/**
 * <p>
 * 用户集合
 * </p>
 * 继承于 {@link com.sg.widgets.commons.dataset.OptionDataSetFactory}，
 * 显示当前系统中的账号信息<br/>
 * 实现以下几种功能：
 * <li>获取当前系统中的账号信息
 * 
 * @author gdiyang
 *
 */
public class ActivatiedUserDataSetFactory extends SingleDBCollectionDataSetFactory {

	/**
	 * 用户集合构造函数,用于设置用户集合的存放数据库及数据存储表
	 */
	public ActivatiedUserDataSetFactory() {
		//设置用户集合的存在数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_USER);
		setQueryCondition(new BasicDBObject().append(User.F_ACTIVATED, Boolean.TRUE));
	}
}
