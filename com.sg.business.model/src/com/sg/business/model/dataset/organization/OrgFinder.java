package com.sg.business.model.dataset.organization;

import com.sg.business.model.IModelConstants;
import com.sg.widgets.commons.dataset.OptionDataSetFactory;

/**
 * <p>
 * 组织集合
 * <p/>
 * 组织集合用于获取当前组织集合，用于编辑器中使用
 * <br/>
 * 继承于 {@link com.sg.widgets.commons.dataset.OptionDataSetFactory}
 * 
 * @author yangjun
 * 
 */
public class OrgFinder extends OptionDataSetFactory {

	/**
	 * 组织集合构造函数
	 */
	public OrgFinder() {
		//设置组织集合的存在数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_ORGANIZATION);
	}


	
}
