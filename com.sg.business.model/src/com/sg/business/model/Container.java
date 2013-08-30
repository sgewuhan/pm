package com.sg.business.model;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.sg.business.resource.BusinessResource;

/**
 * 容器, 虚拟实体，不能持久化
 * 
 * @author Administrator
 * 
 */
public class Container extends PrimaryObject {

	/**
	 * 容器类型：所有者,数字表示
	 */
	public static final int TYPE_OWNER = 0;

	/**
	 * 容器类型：所有者,文本表示
	 */
	public static final String TYPETEXT_OWNER = "所有者";// 本组织以及本组织的下级组织

	/**
	 * 容器类型：授权访问,数字表示
	 */
	public static final int TYPE_GUEST_GRANTED = 1;

	/**
	 * 容器类型：授权访问,文本表示
	 */
	public static final String TYPETEXT_GUEST_GRANTED = "授权访问";//

	/**
	 * 容器类型：授权管理,数字表示
	 */
	public static final int TYPE_ADMIN_GRANTED = 2;

	/**
	 * 容器类型：授权管理,文本表示
	 */
	public static final String TYPETEXT_ADMIN_GRANTED = "授权管理";//

	/**
	 * 容器类型
	 */
	public static final String F_CONTAINER_TYPE = "containertype";

	/**
	 * 容器原型存储的数据库
	 */
	public static final String F_SOURCE_DB = "sourceDB";

	/**
	 * 容器原型存储的集合
	 */
	public static final String F_SOURCE_COLLECTION = "sourceCollection";

	/**
	 * 删除容器
	 */
	@Override
	public void doRemove(IContext context) {
	}

	/**
	 * 保存容器
	 * @return Boolean
	 */
	@Override
	public boolean doSave(IContext context) throws Exception {
		return false;
	}

	/**
	 * 判断是否可以编辑
	 * @return Boolean
	 */
	@Override
	public boolean canEdit(IContext context) {
		return false;
	}

	/**
	 * 判断能否删除
	 * @return boolean
	 */
	@Override
	public boolean canDelete(IContext context) {
		return false;
	}
	
	/**
	 * 返回容器类型的文本显示
	 * @return String
	 */
	public String getContainerTypeText(){
		Integer type = (Integer) getValue(F_CONTAINER_TYPE);
		Assert.isNotNull(type);
		switch (type.intValue()) {
		case TYPE_OWNER:
			return "部门内部文件柜";
		case TYPE_ADMIN_GRANTED:
			return "可管理的文件柜";
		case TYPE_GUEST_GRANTED:
			return "可访问的文件柜";
		default:
			break;
		}
		return "";
	}
	
	public int getContainerType(){
		return ((Integer) getValue(F_CONTAINER_TYPE)).intValue();
	}
	/**
	 * 将一个对象适配成为容器
	 * 
	 * @param source
	 *            , 源对象
	 * @param containerType
	 *            , 容器类别, Container.TYPE_
	 * @return Container
	 */
	public static Container adapter(PrimaryObject source, Integer containerType) {
		Container c = new Container();
		BasicDBObject data = new BasicDBObject();
		data.put(F_DESC, source.getLabel());
		data.put(F_CONTAINER_TYPE, containerType);
		data.put(F_SOURCE_DB, source.getDbName());
		data.put(F_SOURCE_COLLECTION, source.getCollectionName());
		data.putAll(source.get_data());
		c.set_data(data);
		return c;
	}

	/**
	 * 返回显示图标
	 * @return Image
	 */
	@Override
	public Image getImage() {
		Integer type = (Integer) getValue(F_CONTAINER_TYPE);
		Assert.isNotNull(type);
		switch (type.intValue()) {
		case TYPE_OWNER:
			return BusinessResource.getImage(BusinessResource.IMAGE_VAULT1_16);
		case TYPE_ADMIN_GRANTED:
			return BusinessResource.getImage(BusinessResource.IMAGE_VAULT_16);
		case TYPE_GUEST_GRANTED:
			return BusinessResource.getImage(BusinessResource.IMAGE_VAULT2_16);
		default:
			break;
		}
		return null;
	}
	
	public String getContainerHostPath() {
		Class<? extends PrimaryObject> hostClass = ModelService.getModelClass((String)getValue(F_SOURCE_DB), (String)getValue(F_SOURCE_COLLECTION));
		PrimaryObject host = ModelService.createModelObject(hostClass, get_id());
		if(host instanceof Organization){
			Organization org = (Organization)host;
			return org.getPath();
		}
		return null;
	}
	
	/**
	 * 返回类型名称
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return "容器";
	}
}
