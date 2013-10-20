package com.sg.business.commons.ui.flow;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Section;

import com.mobnut.db.model.DataSet;
import com.sg.bpm.workflow.model.NodeAssignment;
import com.sg.business.model.AbstractRoleDefinition;
import com.sg.business.model.User;
import com.sg.widgets.commons.selector.NavigatorSelector;
import com.sg.widgets.part.SimpleSection;
import com.sg.widgets.viewer.ViewerControl;

/**
 * ���̻�༭������ʾ������ڵ����ϸ��Ϣ
 * 
 * @author jinxitao
 * 
 */
public class ActivityEditor extends Composite {

	public interface IActivityEditListener {

		void actorChanged(User newActor, User oldActor,
				NodeAssignment nodeAssignment, AbstractRoleDefinition roleDef);

		void roleChanged(AbstractRoleDefinition newRole,
				AbstractRoleDefinition oldRole, NodeAssignment nodeAssignment);

	}

	/**
	 * �����
	 */
	private Text activiteName;

	/**
	 * ָ������
	 */
	private Text assignmentType;

	/**
	 * ָ�ɹ���
	 */
	private Text assignmentRule;

	/**
	 * ��ɫ�޶�
	 */
	private Text roleText;

	/**
	 * ѡ���ɫ��ť
	 */
	private Button roleSelectorButton;

	/**
	 * �ִ����
	 */
	private Text actorText;

	/**
	 * ѡ��ִ���˰�ť
	 */
	private Button actorSelectorButton;

	/**
	 * �Ƿ�����ִ�����ֶ�
	 */
	private boolean hasActorSelector;

	/**
	 * �Ƿ񱻰�����ɫ�޶��ֶ�
	 */
	private boolean hasRoleSelector;

	/**
	 * �ִ����ѡ������navigatorId
	 */
	private String actorNavigatorId;

	/**
	 * �ִ����ѡ��������Դ
	 */
	private DataSet actorDataSet;

	private ListenerList listeners = new ListenerList();

	/**
	 * ��ڵ��ɫָ��
	 */
	private NodeAssignment nodeAssignment;

	/**
	 * ��ɫ����
	 */
	private AbstractRoleDefinition roleDef;

	/**
	 * �ִ����
	 */
	private User actor;

	/**
	 * ѡ���ɫ���ݼ�
	 */
	private DataSet roleDataSet;

	/**
	 * ѡ���ɫ��NavigatorId
	 */
	private String roleNavigatorId;

	private boolean roleSelectEnable = true;

	private boolean actorSelectEnable = true;

	/**
	 * ��Ӽ���
	 * 
	 * @param listener
	 */
	final public void addActiviteEditListener(IActivityEditListener listener) {
		listeners.add(listener);
	}

	/**
	 * �Ƴ�����
	 * 
	 * @param listener
	 */
	final public void removeActiviteEditListener(IActivityEditListener listener) {
		listeners.remove(listener);
	}

	/**
	 * ���췽������ʼ�� hasActorSelector��hasActorSelector�ֶ�
	 * 
	 * @param parent
	 * @param hasRoleSelector
	 * @param hasActorSelector
	 */
	ActivityEditor(Composite parent, boolean hasRoleSelector,
			boolean hasActorSelector) {
		super(parent, SWT.NONE);
		this.hasActorSelector = hasActorSelector;
		this.hasRoleSelector = hasRoleSelector;
		setLayout(new FormLayout());

		Section section = new SimpleSection(this, Section.EXPANDED
				| Section.SHORT_TITLE_BAR );
		section.setText("������Ϣ�Լ�ִ����ָ��");
		Composite panel = new Composite(section, SWT.NONE);
		createContent(panel);
		section.setClient(panel);
		
		FormData fd = new FormData();
		section.setLayoutData(fd);
		fd.top = new FormAttachment(0,0);
		fd.left = new FormAttachment(0,10);
		fd.right = new FormAttachment(100,-10);
		fd.bottom = new FormAttachment(100,-10);
	}

	/**
	 * ������ڵ�༭��
	 * 
	 * @param parent
	 */
	private void createContent(Composite parent) {
		parent.setLayout(new GridLayout(3, false));

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

	/**
	 * ҳ�沼��
	 * 
	 * @return GridData
	 */
	private GridData getGridData3() {
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.widthHint = 120;
		return gd;
	}

	/**
	 * ҳ�沼��
	 * 
	 * @return GridData
	 */
	private GridData getGridData2() {
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gd.widthHint = 120;
		return gd;
	}

	/**
	 * ҳ�沼��
	 * 
	 * @return GridData
	 */
	private GridData getGridData1() {
		return new GridData(SWT.LEFT, SWT.CENTER, false, false);
	}

	/**
	 * ���û���ƣ�ָ�����ָ�ɹ�����ֶε���ʾ����
	 * 
	 * @param nodeAssignment
	 * @param roleDef
	 * @param actor
	 */
	final public void setInput(NodeAssignment nodeAssignment,
			AbstractRoleDefinition roleDef, User actor) {
		this.nodeAssignment = nodeAssignment;
		this.roleDef = roleDef;
		this.actor = actor;
		if (actorSelectorButton != null) {
			actorSelectorButton.setEnabled(nodeAssignment != null
					&& actorSelectEnable && nodeAssignment.isNeedAssignment());
		}
		if (roleSelectorButton != null) {
			roleSelectorButton.setEnabled(nodeAssignment != null
					&& roleSelectEnable && nodeAssignment.isNeedAssignment());
		}

		if (nodeAssignment == null) {
			activiteName.setText("");
			assignmentType.setText("����ָ��");
			assignmentRule.setText("");
			roleText.setText("");
			if (actorText != null && actor != null && !actor.isEmpty()) {
				actorText.setText(actor.getLabel());
			} else if(actorText !=null){
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
				if (roleDef != null && !roleDef.isEmpty()) {
					roleText.setText(roleDef.getLabel());
				} else if(roleText!=null){
					roleText.setText("");
				}
			}

			if (actorText != null) {
				if (actor != null && !actor.isEmpty()) {
					actorText.setText(actor.getLabel());
				} else if(actorText!=null){
					actorText.setText("");
				}
			}
		}
	}

	/**
	 * �����ִ����ѡ����
	 */
	private void showActorSelectorNavigator() {
		NavigatorSelector ns = new NavigatorSelector(actorNavigatorId) {
			@Override
			protected void doOK(IStructuredSelection is) {
				if (is == null || is.isEmpty()) {
					setActor(null);
				} else {
					User user = (User) is.getFirstElement();
					setActor(user);
				}
				super.doOK(is);
			}

			@Override
			protected boolean isSelectEnabled(IStructuredSelection is) {
				return true;

				// if (!super.isSelectEnabled(is)) {
				// return false;
				// } else {
				// Object element = is.getFirstElement();
				// return element instanceof User;
				// }
			}

		};
		ns.show();

		ViewerControl vc = ns.getNavigator().getViewerControl();
		vc.setDataSet(getActorDataSet());

	}

	/**
	 * ���ûִ����
	 * 
	 * @param user
	 */
	private void setActor(User user) {
		Object[] lis = listeners.getListeners();
		for (int i = 0; i < lis.length; i++) {
			((IActivityEditListener) lis[i]).actorChanged(user, actor,
					nodeAssignment, roleDef);
		}
		actor = user;

		if (actor == null) {
			actorText.setText("");
		} else {
			actorText.setText(actor.getLabel());
		}
	}

	/**
	 * ���ý�ɫ�޶�
	 * 
	 * @param role
	 */
	private void setRole(AbstractRoleDefinition role) {
		Object[] lis = listeners.getListeners();
		for (int i = 0; i < lis.length; i++) {
			((IActivityEditListener) lis[i]).roleChanged(role, roleDef,
					nodeAssignment);
		}
		roleDef = role;

		if (role == null) {
			roleText.setText("");
		} else {
			roleText.setText(role.getLabel());
		}
	}

	/**
	 * ������ɫ�޶�ѡ����
	 */
	private void showRoleSelectorNavigator() {
		NavigatorSelector ns = new NavigatorSelector(roleNavigatorId) {
			@Override
			protected void doOK(IStructuredSelection is) {
				if (is == null || is.isEmpty()) {
					setRole(null);
				} else {

					AbstractRoleDefinition role = (AbstractRoleDefinition) is
							.getFirstElement();
					setRole(role);
				}
				super.doOK(is);
			}

			@Override
			protected boolean isSelectEnabled(IStructuredSelection is) {
				return true;
				// if (!super.isSelectEnabled(is)) {
				// return false;
				// } else {
				// Object element = is.getFirstElement();
				// return element instanceof AbstractRoleDefinition;
				// }
			}

		};
		ns.show();
		ViewerControl vc = ns.getNavigator().getViewerControl();
		vc.setDataSet(getRoleDataSet());

	}

	/**
	 * ���ûִ����ѡ����navigatorId
	 * 
	 * @param navigatorId
	 */
	final public void setActorNavigatorId(String navigatorId) {
		this.actorNavigatorId = navigatorId;
	}

	/**
	 * ���ûִ����ѡ����dataset
	 * 
	 * @param dataset
	 */
	final public void setActorDataSet(DataSet dataset) {
		this.actorDataSet = dataset;
	}

	public DataSet getActorDataSet() {
		return actorDataSet;
	}

	/**
	 * ���ý�ɫ�޶�ѡ����navigatorId
	 * 
	 * @param navigatorId
	 */
	final public void setRoleNavigatorId(String navigatorId) {
		this.roleNavigatorId = navigatorId;
	}

	/**
	 * ���ý�ɫ�޶�ѡ����dataset
	 * 
	 * @param dataset
	 */
	final public void setRoleDataSet(DataSet dataset) {
		this.roleDataSet = dataset;
	}

	public DataSet getRoleDataSet() {
		return roleDataSet;
	}

	public void setEditable(boolean editable) {
		if (roleSelectorButton != null && !roleSelectorButton.isDisposed()) {
			roleSelectorButton.setEnabled(nodeAssignment != null
					&& roleSelectEnable && editable);
		}
		if (actorSelectorButton != null && !actorSelectorButton.isDisposed()) {
			actorSelectorButton.setEnabled(nodeAssignment != null
					&& actorSelectEnable && editable);
		}
	}

	public void setRoleSelectEnable(boolean enable) {
		roleSelectEnable = enable;
	}

	public void setActorSelectEnable(boolean enable) {
		actorSelectEnable = enable;

	}

}
