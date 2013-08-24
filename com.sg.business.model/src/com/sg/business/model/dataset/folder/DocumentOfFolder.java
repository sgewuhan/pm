package com.sg.business.model.dataset.folder;

import com.sg.business.model.Document;
import com.sg.business.model.IModelConstants;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * 文档集合
 * </p>
 * 文档集合用于描述文档库中的文档信息<br/>
 * 继承于 {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}
 * 用于获取组织项下的文档信息<br/>
 * 实现以下几种功能：
 * <li>获取文档集合数据信息
 * <li>通过FOLDERID关联“文档容器”属性为“是”的组织
 * 
 * @author yangjun
 * 
 */
public class DocumentOfFolder extends MasterDetailDataSetFactory {

	/**
	 * 文档集合构造函数,用于设置文档集合的存放数据库及数据存储表
	 */
	public DocumentOfFolder() {
		//设置文档集合的存在数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_DOCUMENT);
	}

	/**
	 * 设置文档集合与组织的关联字段：{@link com.sg.business.model.Document.F_FOLDER_ID}
	 */
	@Override
	protected String getDetailCollectionKey() {
		return Document.F_FOLDER_ID;
	}
}
