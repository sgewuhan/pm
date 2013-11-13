package com.sg.business.model.dataset.organization;

import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.DataSetFactory;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.CurrentAccountContext;

/**
 * <p>
 * ������Ŀ����ְ�ܵ���֯
 * <p/>
 * �̳��� {@link com.mobnut.db.model.DataSetFactory}��
 * ҵ������л�õ�ǰ�û��Ŀ��Խ�����Ŀģ�桢�������壨������ͨ�ã����ĵ�ģ�����֯ <br/>
 * ʵ�����¼��ֹ��ܣ�
 * <li>��ȡ��ǰ�û����Թ���ľ�����Ŀ����ְ�ܵ���֯����������ҵ�����ԱȨ�ޣ�
 * 
 * @author yangjun
 * 
 */
public class OrgOfBusinessAdminRoleUser extends DataSetFactory {
	private User currentUser;

	public OrgOfBusinessAdminRoleUser() {
		String userId = new CurrentAccountContext().getAccountInfo()
				.getConsignerId();
		currentUser = UserToolkit.getUserById(userId);
	}

	/**
	 * ��ǰ�û����Թ���ľ�����Ŀ����ְ�ܵ���֯��
	 */
	private long count;

	/**
	 * ��ȡ��ǰ�û����Թ���ľ�����Ŀ����ְ�ܵ���֯��List<{@link com.mobnut.db.model.PrimaryObject}>����
	 * 
	 * @param ds
	 *            : ��֯���ݼ�
	 * @return ʵ������{@link com.mobnut.db.model.PrimaryObject}����
	 */
	@Override
	public List<PrimaryObject> doQuery(DataSet ds) throws Exception {

		// ����û�����ҵ�����Ա(��֯��ɫ)����֯
		List<PrimaryObject> orgs = currentUser
				.getRoleGrantedInFunctionDepartmentOrganization(Role.ROLE_BUSINESS_ADMIN_ID);

		// ��ȡ��ǰ�û��ɷ��ʵ���֯������
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
