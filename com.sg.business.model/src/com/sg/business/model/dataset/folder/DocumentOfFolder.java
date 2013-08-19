package com.sg.business.model.dataset.folder;

import com.sg.business.model.Document;
import com.sg.business.model.IModelConstants;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * 文档集合
 * <p/>
 * 文档集合用于描述文档库中的文档信息<br/>
 * 继承于 {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}
 * 
 * @author yangjun
 * 
 */
public class DocumentOfFolder extends MasterDetailDataSetFactory {

	/**
	 * 文档集合构造函数
	 */
	public DocumentOfFolder() {
		//设置文档集合的存在数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_DOCUMENT);
	}

	/**
	 * 设置文档集合的MasterID
	 */
	@Override
	protected String getDetailCollectionKey() {
		return Document.F_FOLDER_ID;
	}
}
