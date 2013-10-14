package com.sg.business.model.dataset.organization;

import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.DataSetFactory;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.portal.user.UserSessionContext;
import com.sg.business.model.Role;
import com.sg.business.model.User;

/**
 * <p>
 * ������Ŀ����ְ�ܵ���֯
 * <p/>
 * �̳��� {@link com.mobnut.db.model.DataSetFactory}��
 * ҵ������л�õ�ǰ�û��Ŀ��Խ�����Ŀģ�桢�������壨������ͨ�ã����ĵ�ģ�����֯
 * <br/>
 * ʵ�����¼��ֹ��ܣ�
 * <li>��ȡ��ǰ�û����Թ���ľ�����Ŀ����ְ�ܵ���֯����������ҵ�����ԱȨ�ޣ�
 * 
 * @author yangjun
 * 
 */
public class OrgOfBusinessAdminRoleUser extends DataSetFactory {
	
	/**
	 * ��ǰ�û����Թ���ľ�����Ŀ����ְ�ܵ���֯��
	 */
	private long count;

	/**
	 * ��ȡ��ǰ�û����Թ���ľ�����Ŀ����ְ�ܵ���֯��List<{@link com.mobnut.db.model.PrimaryObject}>����
	 * @param ds : ��֯���ݼ�
	 * @return ʵ������{@link com.mobnut.db.model.PrimaryObject}����
	 */
	@Override
	public List<PrimaryObject> doQuery(DataSet ds) throws Exception {
		
		// �ӵ�ǰ�Ľ����л�õ�¼�û�����Ϣ
		ObjectId useroid = UserSessionContext.getSession().getUserOId();
		User currentUser = ModelService.createModelObject(User.class, useroid);
		
		//����û�����ҵ�����Ա(��֯��ɫ)����֯
		List<PrimaryObject> orgs = currentUser.getRoleGrantedInFunctionDepartmentOrganization(Role.ROLE_BUSINESS_ADMIN_ID);

		//��ȡ��ǰ�û��ɷ��ʵ���֯������
		count = orgs.size();
		return orgs;
	}


	/**
	 * ���ڷ�ҳ��ѯʱԤ��ȫ����ҳ��
	 */
	@Override
	public long getTotalCount() {
		return count;
	}

}
