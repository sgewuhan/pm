package com.sg.business.commons.ui.flow;

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.model.NodeAssignment;
import com.sg.business.commons.ui.flow.ActiviteEditor.IActiviteEditListener;
import com.sg.business.commons.ui.flow.ActiviteSelecter.INodeSelectionListener;
import com.sg.business.model.AbstractRoleDefinition;
import com.sg.business.model.User;

public class ProcessSettingPanel2 extends Composite implements
		INodeSelectionListener, IActiviteEditListener {

	private ComboViewer processSelecter;
	private Button activatedChecker;
	private ActiviteSelecter activiteSelecter;
	private ActiviteEditor activiteEditor;
	private boolean hasProcessSelector;
	private boolean hasActorSelector;
	private DroolsProcessDefinition processDefinition;
	private boolean hasRoleSelector;

	public ProcessSettingPanel2(Composite parent) {
		super(parent, SWT.NONE);
		createContent();
	}

	public void createContent() {
		setLayout(new FormLayout());
		if (isHasProcessSelector()) {
			createProcessSelector(this);
		}

		Composite panel = new Composite(this, SWT.NONE);
		panel.setLayout(new GridLayout(2, false));

		activiteSelecter = createActiviteSelecter(panel);
		activiteSelecter.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
				false));

		activiteSelecter.addListener(this);

		activiteEditor = createActiviteEditor(panel);
		activiteEditor.addActiviteEditListener(this);
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
	}

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
						if (is != null && !is.isEmpty()) {
							Object element = is.getFirstElement();
							activiteSelecter
									.setInput((DroolsProcessDefinition) element);
							processDefinition = (DroolsProcessDefinition) element;
						}

					}
				});
		activatedChecker = new Button(parent, SWT.CHECK);
		activatedChecker.setText("启用");

		fd = new FormData();
		activatedChecker.setLayoutData(fd);
		fd.left = new FormAttachment(processSelecter.getControl(), 10);
		fd.top = new FormAttachment(0, 10);

	}

	private ActiviteSelecter createActiviteSelecter(Composite parent) {
		return new ActiviteSelecter(parent);
	}

	private ActiviteEditor createActiviteEditor(Composite parent) {
		return new ActiviteEditor(parent, isHasRoleSelector(),isHasActorSelector());
	}

	@Override
	public void selectionChange(NodeAssignment nodeAssignment) {
		AbstractRoleDefinition roleDef = getRoleDefinition(nodeAssignment);
		User actor = getActor(nodeAssignment);
		activiteEditor.setInput(nodeAssignment, roleDef, actor);
	}

	protected AbstractRoleDefinition getRoleDefinition(
			NodeAssignment nodeAssignment) {
		return null;
	}

	protected User getActor(NodeAssignment nodeAssignment) {
		return null;
	}

	public boolean isProcessActivated() {
		return activatedChecker.getSelection();
	}

	public void setProcessActivated(boolean activate) {
		if (processSelecter != null) {
			activatedChecker.setSelection(activate);
		}
	}

	public void setProcessDefinitionChioce(
			List<DroolsProcessDefinition> processDefs) {
		if (processSelecter != null) {
			processSelecter.setInput(processDefs);
		}
	}

	public void setProcessDefinition(DroolsProcessDefinition processDef) {
		this.processDefinition = processDef;
		if (processSelecter != null) {
			processSelecter.setSelection(new StructuredSelection(
					new Object[] { processDef }));
		} else {
			activiteSelecter.setInput(processDef);
		}
	}

	public DroolsProcessDefinition getProcessDefinition() {
		return processDefinition;
	}

	public boolean isHasProcessSelector() {
		return hasProcessSelector;
	}

	public void setHasProcessSelector(boolean hasProcessSelector) {
		this.hasProcessSelector = hasProcessSelector;
	}

	public boolean isHasActorSelector() {
		return hasActorSelector;
	}

	public void setHasActorSelector(boolean hasActorSelector) {
		this.hasActorSelector = hasActorSelector;
	}

	public boolean isHasRoleSelector() {
		return hasRoleSelector;
	}

	public void setHasRoleSelector(boolean hasRoleSelector) {
		this.hasRoleSelector = hasRoleSelector;
	}

	@Override
	public void setActor(User newActor, User oldActor,
			NodeAssignment nodeAssignment, AbstractRoleDefinition roleDef) {
		
	}
}
