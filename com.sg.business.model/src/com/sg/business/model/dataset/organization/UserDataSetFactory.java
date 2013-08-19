package com.sg.business.model.dataset.organization;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.sg.business.model.IModelConstants;

/**
 * <p>
 * 用户集合
 * </p>
 * 显示当前系统中的账号信息
 * <br/>
 * 继承于 {@link com.sg.widgets.commons.dataset.OptionDataSetFactory}
 * 
 * @author gdiyang
 *
 */
public class UserDataSetFactory extends SingleDBCollectionDataSetFactory {

	/**
	 * 用户集合构造函数
	 */
	public UserDataSetFactory() {
		//设置用户集合的存在数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_USER);
	}
}
