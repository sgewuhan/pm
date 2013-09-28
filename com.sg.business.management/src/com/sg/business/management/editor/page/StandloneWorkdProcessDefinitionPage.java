package com.sg.business.management.editor.page;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.model.NodeAssignment;
import com.sg.business.commons.ui.flow.ProcessControlSetting;
import com.sg.business.commons.ui.flow.ProcessSettingPanel2;
import com.sg.business.model.AbstractRoleDefinition;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.IProcessControl;
import com.sg.business.model.Organization;
import com.sg.business.model.User;
import com.sg.business.model.WorkDefinition;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.registry.config.BasicPageConfigurator;
import com.sg.widgets.registry.config.IPageDelegator;

public class StandloneWorkdProcessDefinitionPage implements IPageDelegator, IFormPart {

	private boolean dirty;
	private IManagedForm form;
	private IProcessControl IProcessControl;
	private WorkDefinition workd;

	public StandloneWorkdProcessDefinitionPage() {
	}

	@Override
	public Composite createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		parent.setBackgroundMode(SWT.INHERIT_DEFAULT);
		workd = (WorkDefinition) input.getData();
		IProcessControl = (IProcessControl) workd
				.getAdapter(IProcessControl.class);

		CTabFolder tabFolder = new CTabFolder(parent, SWT.TOP);
		CTabItem cti1 = new CTabItem(tabFolder, SWT.NONE);
		cti1.setText("����ִ������");
		Control control1 = createTab(tabFolder, WorkDefinition.F_WF_EXECUTE);
		cti1.setControl(control1);

		CTabItem cti2 = new CTabItem(tabFolder, SWT.NONE);
		cti2.setText("�����������");
		Control control2 = createTab(tabFolder, WorkDefinition.F_WF_CHANGE);
		cti2.setControl(control2);

		return tabFolder;
	}

	private Control createTab(Composite tab, final String key) {

		ProcessSettingPanel2 psp2 = new ProcessSettingPanel2(tab,
				ProcessSettingPanel2.PROCESS_SELECTOR
						| ProcessSettingPanel2.ROLE_SELECTOR
						| ProcessSettingPanel2.ACTOR_SELECTOR) {

			@Override
			protected AbstractRoleDefinition getRoleDefinition(
					NodeAssignment nodeAssignment) {
				if (nodeAssignment != null) {
					return IProcessControl.getProcessActionAssignment(key,
							nodeAssignment.getNodeActorParameter());
				} else {
					return null;
				}
			}

			@Override
			protected User getActor(NodeAssignment nodeAssignment) {
				String userid = IProcessControl.getProcessActionActor(key,
						nodeAssignment.getNodeActorParameter());
				return UserToolkit.getUserById(userid);
			}

		};

		// ����ģ��������֯�����̿��е�������֯����Ϊ����ѡ����������Դ
		List<DroolsProcessDefinition> processDefs = getDroolsProcessDefinitions();
		// ������̶���ѡ����
		psp2.setProcessDefinitionChoice(processDefs);

		boolean activate=isActivate(key);;
		// ��Ӹ������Ƿ����õ�ѡ���
		psp2.setProcessActivated(activate);

		// ���ص�ǰѡ������
		DroolsProcessDefinition processDef = getCurrentDroolsProcessDefinition(key);
		// ��ʾ��ǰѡ�����̵���Ϣ
		psp2.setProcessDefinition(processDef);
		
		
		psp2.setRoleNavigatorId("management.roleselector");

		List<PrimaryObject> rds =new ArrayList<PrimaryObject>();
		DBCollection coll = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ORGANIZATION);
		DBCursor cur = coll.find(new BasicDBObject().append(Organization.F_PARENT_ID, null));
		Organization org=ModelService.createModelObject(cur.next(), Organization.class);
		rds.add(org);
		psp2.setRoleDataSet(new DataSet(rds));
		
		
		

		// ���ý�ɫ��ѡ��������Ŀģ���еĽ�ɫ����
		psp2.setActorNavigatorId("organization.role");
        
		
		psp2.createContent();
		// ��Ӽ���
		psp2.addProcessSettingListener(new ProcessControlSetting(
				IProcessControl, key) {
			@Override
			protected void event(int code) {
				setDirty(true);
			}
		});
		return psp2;
	}

	/**
	 * ���ص�ǰ���̶���
	 * 
	 * @param key
	 *            ,���̶�������
	 * @return DroolsProcessDefinition
	 */
	private DroolsProcessDefinition getCurrentDroolsProcessDefinition(String key) {
			return IProcessControl.getProcessDefinition(key);
	}

	/**
	 * �жϵ�ǰ�����Ƿ�����
	 * 
	 * @param key
	 * @return boolean
	 */
	private boolean isActivate(String key) {
			return IProcessControl.isWorkflowActivate(key);
	}

	/**
	 * ����ģ��������֯�����̿����������̶���
	 * 
	 * @return List
	 */
	private List<DroolsProcessDefinition> getDroolsProcessDefinitions() {
		// ��ȡģ����������֯
		Organization org = workd.getOrganization();
		return org.getDroolsProcessDefinitions();
	}

	private void setDirty(boolean b) {
		this.dirty = b;
		form.dirtyStateChanged();
	}

	@Override
	public IFormPart getFormPart() {
		return this;
	}

	@Override
	public void initialize(IManagedForm form) {
		this.form = form;
	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public void commit(boolean onSave) {
		if (onSave) {
			dirty = false;
			form.dirtyStateChanged();
		}
	}

	@Override
	public boolean setFormInput(Object input) {
		return false;
	}

	@Override
	public void setFocus() {

	}

	@Override
	public boolean isStale() {
		return false;
	}

	@Override
	public void refresh() {

	}

}
