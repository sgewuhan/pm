package com.sg.business.commons.ui.flow;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.mobnut.db.model.DataSet;
import com.sg.bpm.workflow.model.NodeAssignment;
import com.sg.business.model.AbstractRoleDefinition;
import com.sg.business.model.User;
import com.sg.widgets.commons.selector.NavigatorSelector;
import com.sg.widgets.viewer.ViewerControl;

public class ActivityEditor extends Composite {

	public interface IActivityEditListener {

		void actorChanged(User newActor, User oldActor,
				NodeAssignment nodeAssignment, AbstractRoleDefinition roleDef);

		void roleChanged(AbstractRoleDefinition newRole,
				AbstractRoleDefinition oldRole, NodeAssignment nodeAssignment);

	}

	private Text activiteName;
	private Text assignmentType;
	private Text assignmentRule;
	private Text roleText;
	private Button roleSelectorButton;
	private Text actorText;
	private Button actorSelectorButton;
	private boolean hasActorSelector;
	private boolean hasRoleSelector;
	private String actorNavigatorId;
	private DataSet actorDataSet;

	private ListenerList listeners = new ListenerList();
	private NodeAssignment nodeAssignment;
	private AbstractRoleDefinition roleDef;
	private User actor;
	private DataSet roleDataSet;
	private String roleNavigatorId;

	final public void addActiviteEditListener(IActivityEditListener listener) {
		listeners.add(listener);
	}

	final public void removeActiviteEditListener(IActivityEditListener listener) {
		listeners.remove(listener);
	}

	ActivityEditor(Composite parent, boolean hasRoleSelector,
			boolean hasActorSelector) {
		super(parent, SWT.BORDER);
		this.hasActorSelector = hasActorSelector;
		this.hasRoleSelector = hasRoleSelector;
		createContent(this);
	}

	private void createContent(Composite parent) {
		setLayout(new GridLayout(3, false));
		// �����
		Label label = new Label(parent, SWT.NONE);
		label.setText("�����");
		label.setLayoutData(getGridData1());

		activiteName = new Text(parent, SWT.BORDER);
		activiteName.setLayoutData(getGridData2());
		activiteName.setEditable(false);

		// ָ�����
		label = new Label(parent, SWT.NONE);
		label.setText("ָ�����");
		label.setLayoutData(getGridData1());
		assignmentType = new Text(parent, SWT.BORDER);
		assignmentType.setLayoutData(getGridData2());
		assignmentType.setEditable(false);

		// ָ�ɹ���
		label = new Label(parent, SWT.NONE);
		label.setText("ָ�ɹ���");
		label.setLayoutData(getGridData1());
		assignmentRule = new Text(parent, SWT.BORDER);
		assignmentRule.setLayoutData(getGridData2());
		assignmentRule.setEditable(false);

		label = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3,
				1));

		// ѡ���ɫ
		if (hasRoleSelector) {
			label = new Label(parent, SWT.NONE);
			label.setText("��ɫ�޶�");
			label.setLayoutData(getGridData1());
			roleText = new Text(parent, SWT.BORDER);
			roleText.setEditable(false);
			roleText.setLayoutData(getGridData3());
			roleSelectorButton = new Button(parent, SWT.PUSH);
			roleSelectorButton.setEnabled(false);
			roleSelectorButton.setText("..");
			roleSelectorButton.setLayoutData(getGridData1());
			roleSelectorButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					showRoleSelectorNavigator();
				}
			});
		}

		// ����ѡ���û�
		if (hasActorSelector) {
			label = new Label(parent, SWT.NONE);
			label.setText("�ִ����");
			label.setLayoutData(getGridData1());
			actorText = new Text(parent, SWT.BORDER);
			actorText.setEditable(false);
			actorText.setLayoutData(getGridData3());
			actorSelectorButton = new Button(parent, SWT.PUSH);
			actorSelectorButton.setEnabled(false);
			actorSelectorButton.setText("..");
			actorSelectorButton.setLayoutData(getGridData1());
			actorSelectorButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					showActorSelectorNavigator();
				}
			});
		}
	}

	private GridData getGridData3() {
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.widthHint = 120;
		return gd;
	}

	private GridData getGridData2() {
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gd.widthHint = 120;
		return gd;
	}

	private GridData getGridData1() {
		return new GridData(SWT.LEFT, SWT.CENTER, false, false);
	}

	final public void setInput(NodeAssignment nodeAssignment,
			AbstractRoleDefinition roleDef, User actor) {
		this.nodeAssignment = nodeAssignment;
		this.roleDef = roleDef;
		this.actor = actor;
		if (actorSelectorButton != null) {
			actorSelectorButton.setEnabled(nodeAssignment != null
					&& nodeAssignment.isNeedAssignment());
		}
		if (roleSelectorButton != null) {
			roleSelectorButton.setEnabled(nodeAssignment != null
					&& nodeAssignment.isNeedAssignment());
		}

		if (nodeAssignment == null) {
			activiteName.setText("");
			assignmentType.setText("����ָ��");
			assignmentRule.setText("");
			roleText.setText("");
			if (actorText != null) {
				actorText.setText("");
			}
		} else {
			activiteName.setText(nodeAssignment.getNodeName());
			if (nodeAssignment.isDyanmic()) {
				assignmentType.setText("��ָ̬��");
			} else if (nodeAssignment.isRuleAssignment()) {
				assignmentType.setText("����ָ��");
			} else if (nodeAssignment.isStaticActor()) {
				assignmentType.setText("��ִ̬����");
			} else if (nodeAssignment.isStaticGroup()) {
				assignmentType.setText("��ִ̬����");
			} else {
				assignmentType.setText("");
			}

			String ruleName = nodeAssignment.getRuleAssignmentName();
			if (ruleName == null) {
				assignmentRule.setText("");
			} else {
				assignmentRule.setText(ruleName);
			}

			if (roleText != null) {
				if (roleDef != null) {
					roleText.setText(roleDef.getLabel());
				} else {
					roleText.setText("");
				}
			}

			if (actorText != null) {
				if (actor != null) {
					actorText.setText(actor.getLabel());
				} else {
					actorText.setText("");
				}
			}
		}
	}

	private void showActorSelectorNavigator() {
		NavigatorSelector ns = new NavigatorSelector(actorNavigatorId) {
			@Override
			protected void doOK(IStructuredSelection is) {
				User user = (User) is.getFirstElement();
				setActor(user);
				super.doOK(is);
			}

			@Override
			protected boolean isSelectEnabled(IStructuredSelection is) {

				if (!super.isSelectEnabled(is)) {
					return false;
				} else {
					Object element = is.getFirstElement();
					return element instanceof User;
				}
			}

		};
		ViewerControl vc = ns.getNavigator().getViewerControl();
		vc.setDataSet(actorDataSet);
		ns.show();

	}

	private void setActor(User user) {
		Object[] lis = listeners.getListeners();
		for (int i = 0; i < lis.length; i++) {
			((IActivityEditListener) lis[i]).actorChanged(user, actor,
					nodeAssignment, roleDef);
		}
		actor = user;
	}

	private void setRole(AbstractRoleDefinition role) {
		Object[] lis = listeners.getListeners();
		for (int i = 0; i < lis.length; i++) {
			((IActivityEditListener) lis[i]).roleChanged(role,roleDef, 
					nodeAssignment);
		}
		roleDef = role;
	}

	private void showRoleSelectorNavigator() {
		NavigatorSelector ns = new NavigatorSelector(roleNavigatorId) {
			@Override
			protected void doOK(IStructuredSelection is) {
				AbstractRoleDefinition role = (AbstractRoleDefinition) is
						.getFirstElement();
				setRole(role);
				super.doOK(is);
			}

			@Override
			protected boolean isSelectEnabled(IStructuredSelection is) {

				if (!super.isSelectEnabled(is)) {
					return false;
				} else {
					Object element = is.getFirstElement();
					return element instanceof AbstractRoleDefinition;
				}
			}

		};
		ViewerControl vc = ns.getNavigator().getViewerControl();
		vc.setDataSet(roleDataSet);
		ns.show();

	}

	final public void setActorNavigatorId(String navigatorId) {
		this.actorNavigatorId = navigatorId;
	}

	final public void setActorDataSet(DataSet dataset) {
		this.actorDataSet = dataset;
	}

	final public void setRoleNavigatorId(String navigatorId) {
		this.roleNavigatorId = navigatorId;
	}

	final public void setRoleDataSet(DataSet dataset) {
		this.roleDataSet = dataset;
	}

}
