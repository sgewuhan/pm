package com.sg.business.model;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.sg.business.model.nls.Messages;
import com.sg.business.resource.BusinessResource;

/**
 * ����, ����ʵ�壬���ܳ־û�
 * 
 * @author Administrator
 * 
 */
public class Container extends PrimaryObject {

	/**
	 * �������ͣ�������,���ֱ�ʾ
	 */
	public static final int TYPE_OWNER = 0;

	/**
	 * �������ͣ�������,�ı���ʾ
	 */
	public static final String TYPETEXT_OWNER = Messages.get().Container_0;// ����֯�Լ�����֯���¼���֯

	/**
	 * �������ͣ���Ȩ����,���ֱ�ʾ
	 */
	public static final int TYPE_GUEST_GRANTED = 1;

	/**
	 * �������ͣ���Ȩ����,�ı���ʾ
	 */
	public static final String TYPETEXT_GUEST_GRANTED = Messages.get().Container_1;//

	/**
	 * �������ͣ���Ȩ����,���ֱ�ʾ
	 */
	public static final int TYPE_ADMIN_GRANTED = 2;

	/**
	 * �������ͣ���Ȩ����,�ı���ʾ
	 */
	public static final String TYPETEXT_ADMIN_GRANTED = Messages.get().Container_2;//

	/**
	 * ��������
	 */
	public static final String F_CONTAINER_TYPE = "containertype"; //$NON-NLS-1$

	/**
	 * ����ԭ�ʹ洢�����ݿ�
	 */
	public static final String F_SOURCE_DB = "sourceDB"; //$NON-NLS-1$

	/**
	 * ����ԭ�ʹ洢�ļ���
	 */
	public static final String F_SOURCE_COLLECTION = "sourceCollection"; //$NON-NLS-1$

	/**
	 * ɾ������
	 */
	@Override
	public void doRemove(IContext context) {
	}

	/**
	 * ��������
	 * @return Boolean
	 */
	@Override
	public boolean doSave(IContext context) throws Exception {
		return false;
	}

	/**
	 * �ж��Ƿ���Ա༭
	 * @return Boolean
	 */
	@Override
	public boolean canEdit(IContext context) {
		return false;
	}

	/**
	 * �ж��ܷ�ɾ��
	 * @return boolean
	 */
	@Override
	public boolean canDelete(IContext context) {
		return false;
	}
	
	/**
	 * �����������͵��ı���ʾ
	 * @return String
	 */
	public String getContainerTypeText(){
		Integer type = (Integer) getValue(F_CONTAINER_TYPE);
		Assert.isNotNull(type);
		switch (type.intValue()) {
		case TYPE_OWNER:
			return Messages.get().Container_6;
		case TYPE_ADMIN_GRANTED:
			return Messages.get().Container_7;
		case TYPE_GUEST_GRANTED:
			return Messages.get().Container_8;
		default:
			break;
		}
		return ""; //$NON-NLS-1$
	}
	
	public int getContainerType(){
		return ((Integer) getValue(F_CONTAINER_TYPE)).intValue();
	}
	/**
	 * ��һ�����������Ϊ����
	 * 
	 * @param source
	 *            , Դ����
	 * @param containerType
	 *            , �������, Container.TYPE_
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
	 * ������ʾͼ��
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
	 * ������������
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return Messages.get().Container_10;
	}
}
