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

	private Set<OrgExchange> insertSet;
	private Set<OrgExchange> removeSet;
	private Set<OrgExchange> renameSet;
	public SyncHR() {
		super("��ȡHRϵͳ����");
		setUser(true);
	}

	/**
	 * ��HR��֯����ͬ��
	 * @param insertSet �����PMϵͳ��HRϵͳ�ٵ���֯��������ΪPMϵͳ��Ҫ�������֯ʹ��
	 * @param removeSet ,���PMϵͳ��HRϵͳ�����֯��������ΪPMϵͳ��Ҫɾ������֯ʹ��
	 * @param renameSet ,���PMϵͳ��HRϵͳ���Ʋ�һ�µ���֯��������ΪPMϵͳ��Ҫ�޸�ȫ�Ƶ���֯ʹ��
	 */
	public void doSyscHROrganization() throws Exception {
		insertSet = new HashSet<OrgExchange>();
		removeSet = new HashSet<OrgExchange>();
		renameSet = new HashSet<OrgExchange>();
		
		// ��ȡPMϵͳ��HRϵͳ�Ķ�����֯������ʱ��ͬʱ�������������֯���νṹ
		OrgExchange pmOrg = new OrgExchange(null, true);
		OrgExchange hrOrg = new OrgExchange(null, false);

		// �ж϶�����֯�Ƿ�һ��
		if (!hrOrg.equals(pmOrg)) {
			// ������֯��һ��ʱ��PMϵͳ�е�������֯��Ҫɾ����HR��������֯��Ҫ����
			insertSet.add(hrOrg);
			removeSet.add(pmOrg);
		} else {
			// ��ȡPMϵͳ��HRϵͳ��֯�Ĳ���
			getPMDifferentHR(pmOrg, hrOrg, insertSet, removeSet, renameSet);
		}
//		// ����PMϵͳ��HRϵͳ�ٵ���֯
//		for (OrgExchange orgExchange : insertSet) {
//			orgExchange.doAddAllHR();
//		}
//		// �޸�PMϵͳ��HRϵͳ���Ʋ�һ�µ���֯
//		for (OrgExchange orgExchange : renameSet) {
//			orgExchange.doRenameHR();
//		}
//		// ���ʼ�֪ͨϵͳ����Աɾ��PMϵͳ��HRϵͳ�����֯
//		if(removeSet.size() > 0){
//			pmOrg.sendMessage(removeSet);
//		}
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
		//��ȡPMϵͳ��HRϵȱ�ٵ�����֯
		insertSet.addAll(hrOrg.getDifferentChildren(pmOrg));
		//��ȡPMϵͳ��HRϵȱ�������֯
		removeSet.addAll(pmOrg.getDifferentChildren(hrOrg));
		//�жϵ�ǰ������֯��ȫ���Ƿ�һ��
		if (!hrOrg.getDifferentName(pmOrg)) {
			hrOrg.setPmId(pmOrg.getPmId());
			renameSet.add(hrOrg);
		}
		//��ȡPMϵͳ��HRϵȱ��ͬ������֯
		Set<OrgExchange> hrSameChildren = hrOrg.getSameChildren(pmOrg);
		//ѭ����ȡHRϵͳ����ͬ������֯
		for (OrgExchange orgExchange : hrSameChildren) {
			//ͨ��HR������֯��Ź���HRϵͳ��PMϵͳ������֯
			OrgExchange pmOrgChildren = null;
			try {
				pmOrgChildren = new OrgExchange(orgExchange.getOrgId(),
						true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			OrgExchange hrOrgChildren = null;
			try {
				hrOrgChildren = new OrgExchange(orgExchange.getOrgId(),
						false);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//�������ø÷�������ȡ����֯�Ĳ���
			getPMDifferentHR(pmOrgChildren, hrOrgChildren, insertSet,
					removeSet, renameSet);
		}
	}

	
	public Set<OrgExchange> getInsertSet() {
		return insertSet;
	}

	public Set<OrgExchange> getRemoveSet() {
		return removeSet;
	}

	public Set<OrgExchange> getRenameSet() {
		return renameSet;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask("���ڲ�ѯ����", IProgressMonitor.UNKNOWN);
		try {
			doSyscHROrganization();
			return Status.OK_STATUS;
		} catch (Exception e) {
			return new MultiStatus(OrganizationActivator.PLUGIN_ID, IStatus.ERROR, "��ѯ����", e);
		}
		
	}

}
