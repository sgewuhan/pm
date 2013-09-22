package com.sg.business.commons.ui.flow;

import java.util.List;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
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

import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.model.NodeAssignment;
import com.sg.business.commons.ui.flow.ActivityEditor.IActivityEditListener;
import com.sg.business.commons.ui.flow.ActivitySelecter.INodeSelectionListener;
import com.sg.business.model.AbstractRoleDefinition;
import com.sg.business.model.User;

/**
 * 流程定义页面，包含流程选择器，是否启用选择框，流程详细信息（流程图和活动表），活动节点详细信息（活动名称，指派类别，指派规则，角色限定，活动执行人等）
 * @author jinxitao
 *
 */
public class ProcessSettingPanel2 extends Composite {

	public interface IProcessSettingListener {

		void actorChanged(User newActor, User oldActor,
				NodeAssignment nodeAssignment, AbstractRoleDefinition roleDef);

		void processChanged(DroolsProcessDefinition newProcessDefinition,
				DroolsProcessDefinition oldProcessDef);

		void processActivatedChanged(boolean activated);

		void roleChanged(AbstractRoleDefinition newRole,
				AbstractRoleDefinition oldRole, NodeAssignment nodeAssignment);

	}

	/**
	 * 流程定义选择器
	 */
	private ComboViewer processSelecter;
	
	/**
	 * 流程是否启用选择框
	 */
	private Button activatedChecker;     
	
	/**
	 * 流程图和活动表
	 */
	private ActivitySelecter activitySelecter;
	
	/**
	 * 流程活动节点编辑器
	 */
	private ActivityEditor activiteEditor;
	
	/**
	 * 是否需要流程选择器
	 */
	private boolean hasProcessSelector;
	
	/**
	 * 是否需要活动执行人字段
	 */
	private boolean hasActorSelector;
	
	/**
	 * 流程定义
	 */
	private DroolsProcessDefinition processDefinition;
	
	/**
	 * 是否需要角色限定字段
	 */
	private boolean hasRoleSelector;
	private ListenerList listeners = new ListenerList();
	
	/**
	 * 模板所属组织下流程库中所有流程定义
	 */
	private List<DroolsProcessDefinition> processDefinitionsChoice;
	
	/**
	 * 流程定义是否启用
	 */
	private boolean processActivate;

	public void addProcessSettingListener(IProcessSettingListener listener) {
		listeners.add(listener);
	}

	public void removeProcessSettingListener(IProcessSettingListener listener) {
		listeners.remove(listener);
	}

	public ProcessSettingPanel2(Composite parent) {
		super(parent, SWT.NONE);
	}

	/**
	 * 构建流程页面内容
	 */
	public void createContent() {
		setLayout(new FormLayout());
		//判断是否显示流程选择器，如果是，则创建流程选择器
		if (hasProcessSelector) {
			createProcessSelector(this);
		}

		Composite panel = new Composite(this, SWT.NONE);
		panel.setLayout(new GridLayout(2, false));

		//创建流程详细信息（流程图和活动表）
		activitySelecter = new ActivitySelecter(panel);
		activitySelecter.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
				false));

		activitySelecter.addListener(new INodeSelectionListener() {
            //监听当前选中流程改变事件
			@Override
			public void selectionChange(NodeAssignment nodeAssignment) {
				AbstractRoleDefinition roleDef = getRoleDefinition(nodeAssignment);
				User actor = getActor(nodeAssignment);
				activiteEditor.setInput(nodeAssignment, roleDef, actor);
			}
		});

		//创建流程活动节点编辑器
		activiteEditor = new ActivityEditor(panel, hasRoleSelector,
				hasActorSelector);
		activiteEditor.addActiviteEditListener(new IActivityEditListener() {
            //监听流程活动执行人的改变事件
			@Override
			public void actorChanged(User newActor, User oldActor,
					NodeAssignment nodeAssignment,
					AbstractRoleDefinition roleDef) {
				ProcessSettingPanel2.this.actorChanged(newActor, oldActor,
						nodeAssignment, roleDef);
			}
            //监听角色限定的改变时间
			@Override
			public void roleChanged(AbstractRoleDefinition newRole,
					AbstractRoleDefinition oldRole,
					NodeAssignment nodeAssignment) {
				ProcessSettingPanel2.this.roleChanged(newRole, oldRole,
						nodeAssignment);
			}
		});
		GridData gd = new GridData(SWT.LEFT, SWT.FILL, false, false);
		gd.widthHint = 300;
		activiteEditor.setLayoutData(gd);

		FormData fd = new FormData();
		panel.setLayoutData(fd);
		if (processSelecter != null) {
			fd.top = new FormAttachment(processSelecter.getControl(), 10);
		} else {
			fd.top = new FormAttachment(0, 10);
		}
		fd.left = new FormAttachment(0, 10);
		fd.right = new FormAttachment(100, -10);
		fd.bottom = new FormAttachment(100, -10);

		initInputValue();
	}

	//初始化流程定义数据
	private void initInputValue() {
		//流程是否启用
		if (activatedChecker != null) {
			activatedChecker.setSelection(processActivate);
		}
		//流程选择器数据源
		if (processSelecter != null) {
			processSelecter.setInput(processDefinitionsChoice);
			processSelecter.setSelection(new StructuredSelection(
					new Object[] { processDefinition }));
		} else {
			activitySelecter.setInput(processDefinition);
		}

	}

	/**
	 * 创建流程选择器
	 * @param parent
	 */
	private void createProcessSelector(Composite parent) {
		// 创建流程选择器
		processSelecter = new ComboViewer(parent, SWT.READ_ONLY);
		processSelecter.setUseHashlookup(true);
		processSelecter.setContentProvider(ArrayContentProvider.getInstance());
		processSelecter.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof DroolsProcessDefinition) {
					DroolsProcessDefinition definition = (DroolsProcessDefinition) element;
					return definition.getProcessName();
				} else {
					return "";
				}
			}
		});

		FormData fd = new FormData();
		processSelecter.getControl().setLayoutData(fd);
		fd.top = new FormAttachment(0, 10);
		fd.left = new FormAttachment(0, 10);

		processSelecter
				.addSelectionChangedListener(new ISelectionChangedListener() {

					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						IStructuredSelection is = (IStructuredSelection) event
								.getSelection();
						DroolsProcessDefinition input = null;
						if (is != null && !is.isEmpty()) {
							input = (DroolsProcessDefinition) is
									.getFirstElement();
						}
						activitySelecter.setInput(input);
						DroolsProcessDefinition oldProcessDef = processDefinition;
						processDefinition = input;
						processChanged(oldProcessDef, processDefinition);

					}
				});
		activatedChecker = new Button(parent, SWT.CHECK);
		activatedChecker.setText("启用");

		fd = new FormData();
		activatedChecker.setLayoutData(fd);
		fd.left = new FormAttachment(processSelecter.getControl(), 10);
		fd.top = new FormAttachment(0, 10);

		activatedChecker.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				processActivatedChanged(activatedChecker.getSelection());
			}

		});
	}

	/**
	 * 返回角色限定
	 * @param nodeAssignment
	 * @return
	 */
	protected AbstractRoleDefinition getRoleDefinition(
			NodeAssignment nodeAssignment) {
		return null;
	}

	/**
	 * 返回活动执行人
	 * @param nodeAssignment
	 * @return
	 */
	protected User getActor(NodeAssignment nodeAssignment) {
		return null;
	}

	/**
	 * 判断流程是否启用
	 * @return boolean
	 */
	final public boolean isProcessActivated() {
		return activatedChecker.getSelection();
	}

	final public void setProcessActivated(boolean activate) {
		processActivate = activate;
		// if (processSelecter != null) {
		// activatedChecker.setSelection(activate);
		// }
	}

	/**
	 * 设置流程选择器
	 * @param processDefs
	 */
	final public void setProcessDefinitionChoice(
			List<DroolsProcessDefinition> processDefs) {
		this.processDefinitionsChoice = processDefs;
		// if (processSelecter != null) {
		// processSelecter.setInput(processDefs);
		// }
	}

	/**
	 * 设置流程
	 * @param processDef
	 */
	final public void setProcessDefinition(DroolsProcessDefinition processDef) {
		this.processDefinition = processDef;
		// if (processSelecter != null) {
		// processSelecter.setSelection(new StructuredSelection(
		// new Object[] { processDef }));
		// // set selection will set activiteSelector input.
		// } else {
		// activitySelecter.setInput(processDef);
		// }
	}

	/**
	 * 是否显示流程选择器组件
	 */
	final public void setHasProcessSelector(boolean hasProcessSelector) {
		this.hasProcessSelector = hasProcessSelector;
	}

	/**
	 * 是否显示活动执行人字段
	 * @param hasActorSelector
	 */
	final public void setHasActorSelector(boolean hasActorSelector) {
		this.hasActorSelector = hasActorSelector;
	}

	/**
	 * 是否显示角色限定字段
	 * @param hasRoleSelector
	 */
	final public void setHasRoleSelector(boolean hasRoleSelector) {
		this.hasRoleSelector = hasRoleSelector;
	}

	/**
	 * 流程定义选择改变事件
	 * @param oldProcessDef
	 * @param newProcessDefinition
	 */
	private void processChanged(DroolsProcessDefinition oldProcessDef,
			DroolsProcessDefinition newProcessDefinition) {
		Object[] lis = listeners.getListeners();
		for (int i = 0; i < lis.length; i++) {
			((IProcessSettingListener) lis[i]).processChanged(
					newProcessDefinition, oldProcessDef);
		}
	}

	/**
	 * 流程活动选择改变事件
	 * @param activated
	 */
	private void processActivatedChanged(boolean activated) {
		Object[] lis = listeners.getListeners();
		for (int i = 0; i < lis.length; i++) {
			((IProcessSettingListener) lis[i])
					.processActivatedChanged(activated);
		}
	}

	/**
	 * 活动执行人改变事件
	 * @param newActor
	 * @param oldActor
	 * @param nodeAssignment
	 * @param roleDef
	 */
	private void actorChanged(User newActor, User oldActor,
			NodeAssignment nodeAssignment, AbstractRoleDefinition roleDef) {
		Object[] lis = listeners.getListeners();
		for (int i = 0; i < lis.length; i++) {
			((IProcessSettingListener) lis[i]).actorChanged(newActor, oldActor,
					nodeAssignment, roleDef);
		}
	}

	/**
	 * 活动角色限定改变事件
	 * @param newRole
	 * @param oldRole
	 * @param nodeAssignment
	 */
	private void roleChanged(AbstractRoleDefinition newRole,
			AbstractRoleDefinition oldRole, NodeAssignment nodeAssignment) {
		Object[] lis = listeners.getListeners();
		for (int i = 0; i < lis.length; i++) {
			((IProcessSettingListener) lis[i]).roleChanged(newRole, oldRole,
					nodeAssignment);
		}

	}

}
