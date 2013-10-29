package com.sg.business.organization.command;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.sg.business.organization.OrganizationActivator;

/**
 * <P>
 * ͬ��HRϵͳ
 * </p>
 * <li>�ṩ��ͬ��HRϵͳ��֯�ķ���
 * 
 * @author yangjun
 * 
 */
public class SyncHR extends Job {

	private Set<OrgExchange> insertOrganizationSet;
	private Set<OrgExchange> removeOrganizationSet;
	private Set<OrgExchange> renameOrganizationSet;

	public SyncHR() {
		super("��ȡHRϵͳ����");
		setUser(true);
	}

	/**
	 * ��HR��֯����ͬ��
	 * 
	 * @param insertSet
	 *            �����PMϵͳ��HRϵͳ�ٵ���֯��������ΪPMϵͳ��Ҫ�������֯ʹ��
	 * @param removeSet
	 *            ,���PMϵͳ��HRϵͳ�����֯��������ΪPMϵͳ��Ҫɾ������֯ʹ��
	 * @param renameSet
	 *            ,���PMϵͳ��HRϵͳ���Ʋ�һ�µ���֯��������ΪPMϵͳ��Ҫ�޸�ȫ�Ƶ���֯ʹ��
	 */
	public void doSyscHROrganization() throws Exception {
		insertOrganizationSet = new HashSet<OrgExchange>();
		removeOrganizationSet = new HashSet<OrgExchange>();
		renameOrganizationSet = new HashSet<OrgExchange>();

		// ��ȡPMϵͳ��HRϵͳ�Ķ�����֯������ʱ��ͬʱ�������������֯���νṹ
		OrgExchange pmOrg = new OrgExchange(null, true);
		OrgExchange hrOrg = new OrgExchange(null, false);

		// �ж϶�����֯�Ƿ�һ��
		if (!hrOrg.equals(pmOrg)) {
			// ������֯��һ��ʱ��PMϵͳ�е�������֯��Ҫɾ����HR��������֯��Ҫ����
			insertOrganizationSet.add(hrOrg);
			removeOrganizationSet.add(pmOrg);
		} else {
			// ��ȡPMϵͳ��HRϵͳ��֯�Ĳ���
			getPMDifferentHR(pmOrg, hrOrg, insertOrganizationSet,
					removeOrganizationSet, renameOrganizationSet);
		}
	}

	public void doSyscHRUser() throws Exception {
		Set<UserExchange> insertUserSet = new HashSet<UserExchange>();
		Set<UserExchange> removeUserSet = new HashSet<UserExchange>();
		Set<UserExchange> updateUserSet = new HashSet<UserExchange>();

		// ��ȡPMϵͳ��HRϵͳ���û�����
		UserExchange userExchange = new UserExchange();
		Set<UserExchange> pmUserSet = userExchange.getByPmUsers(null);
		Set<UserExchange> hrUserSet = userExchange.getBySqlUsers(null);
		// ��ȡPMϵͳ��Ҫ������û�����
		insertUserSet.addAll(userExchange
				.getDifferentUser(hrUserSet, pmUserSet));
		// ��ȡPMϵͳ��Ҫɾ�����û�����
		removeUserSet.addAll(userExchange
				.getDifferentUser(pmUserSet, hrUserSet));
		// ��ȡPMϵͳ��Ҫ�ı������֯���û�����
		Set<UserExchange> hrSameUser = userExchange.getSameUser(hrUserSet,
				pmUserSet);
		for (UserExchange hrUserExchange : hrSameUser) {
			UserExchange pmUserExchange = userExchange
					.getByPmUser(hrUserExchange.getUserId());
			if (hrUserExchange.getDifferentUnitId(pmUserExchange)) {
				updateUserSet.add(pmUserExchange);
			}
		}
		// �����û�����
		if (insertUserSet.size() > 0) {
			userExchange.doAddHR(insertUserSet);
		}
		// ������Ҫɾ�����û�������Ϣ��ϵͳ����Ա
		if (removeUserSet.size() > 0) {
			userExchange.sendMessage(removeUserSet,
					UserExchange.MESSAGE_SENDTYPE_DELETE);
		}
		// ������Ҫ�ı������֯���û�������Ϣ��ϵͳ����Ա
		if (updateUserSet.size() > 0) {
			userExchange.sendMessage(updateUserSet,
					UserExchange.MESSAGE_SENDTYPE_UPDATE);
		}
	}

	/**
	 * ��ȡPMϵͳ��HRϵͳ��֯�Ĳ���
	 * 
	 * @param pmOrg
	 *            : {@link com.sg.business.organization.command.OrgExchange}���� ,<BR/>
	 *            PMϵͳ�е���֯��������֯��������������֯�ṹ
	 * @param hrOrg
	 *            : {@link com.sg.business.organization.command.OrgExchange}���� ,<br/>
	 *            HRϵͳ�е���֯��������֯��������������֯�ṹ
	 * @param insertSet
	 *            : {@link HashSet} <br/>
	 *            PMϵͳ��HRϵȱ�ٵ���֯,������֯
	 * @param removeSet
	 *            : {@link HashSet}, <br/>
	 *            PMϵͳ��HRϵͳ�����֯��������֯
	 * @param renameSet
	 *            : {@link HashSet}, <br/>
	 *            PMϵͳ��HRϵͳ���Ʋ�һ�µ���֯
	 */
	private void getPMDifferentHR(OrgExchange pmOrg, OrgExchange hrOrg,
			Set<OrgExchange> insertSet, Set<OrgExchange> removeSet,
			Set<OrgExchange> renameSet) {
		// ��ȡPMϵͳ��HRϵȱ�ٵ�����֯
		insertSet.addAll(hrOrg.getDifferentChildren(pmOrg));
		// ��ȡPMϵͳ��HRϵȱ�������֯
		removeSet.addAll(pmOrg.getDifferentChildren(hrOrg));
		// �жϵ�ǰ������֯��ȫ���Ƿ�һ��
		if (!hrOrg.getDifferentName(pmOrg)) {
			hrOrg.setPmId(pmOrg.getPmId());
			renameSet.add(hrOrg);
		}
		// ��ȡPMϵͳ��HRϵȱ��ͬ������֯
		Set<OrgExchange> hrSameChildren = hrOrg.getSameChildren(pmOrg);
		// ѭ����ȡHRϵͳ����ͬ������֯
		for (OrgExchange orgExchange : hrSameChildren) {
			// ͨ��HR������֯��Ź���HRϵͳ��PMϵͳ������֯
			OrgExchange pmOrgChildren = null;
			try {
				pmOrgChildren = new OrgExchange(orgExchange.getOrgId(), true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			OrgExchange hrOrgChildren = null;
			try {
				hrOrgChildren = new OrgExchange(orgExchange.getOrgId(), false);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// �������ø÷�������ȡ����֯�Ĳ���
			getPMDifferentHR(pmOrgChildren, hrOrgChildren, insertSet,
					removeSet, renameSet);
		}
	}

	public Set<OrgExchange> getInsertSet() {
		return insertOrganizationSet;
	}

	public Set<OrgExchange> getRemoveSet() {
		return removeOrganizationSet;
	}

	public Set<OrgExchange> getRenameSet() {
		return renameOrganizationSet;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask("���ڲ�ѯ����", IProgressMonitor.UNKNOWN);
		try {
			doSyscHROrganization();
			return Status.OK_STATUS;
		} catch (Exception e) {
			e.printStackTrace();
			return new MultiStatus(OrganizationActivator.PLUGIN_ID, IStatus.ERROR, "��ѯ����", e);
		}

	}
}
