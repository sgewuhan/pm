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
 * ��õ�ǰ�û��Ŀ��Թ������֯
 * 
 * @author Administrator
 * 
 */
public class OrgOfBusinessAdminRoleUser extends DataSetFactory {

	private long count;

	@Override
	public List<PrimaryObject> doQuery(DataSet ds) throws Exception {
		
		// �ӵ�ǰ�Ľ����л�õ�¼�û�����Ϣ
		ObjectId useroid = UserSessionContext.getSession().getUserOId();
		User currentUser = ModelService.createModelObject(User.class, useroid);
		
		List<PrimaryObject> orgs = currentUser.getRoleGrantedOrganization(Role.ROLE_BUSINESS_ADMIN_ID);

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
