package com.sg.business.model.dataset.organization;

import com.sg.business.model.IModelConstants;
import com.sg.widgets.commons.dataset.OptionDataSetFactory;

/**
 * <p>
 * 上级组织集合
 * <p/>
 * 继承于 {@link com.sg.widgets.commons.dataset.OptionDataSetFactory}用于获取组织项下的文档信息<br/>
 * 实现以下几种功能：
 * <li>获取上级组织集合数据信息
 * 
 * @author yangjun
 * 
 */
public class OrgFinder extends OptionDataSetFactory {

	/**
	 * 上级组织集合构造函数,用于设置上级组织集合的存放数据库及数据存储表
	 */
	public OrgFinder() {
		//设置组织集合的存在数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_ORGANIZATION);
	}


	
}
