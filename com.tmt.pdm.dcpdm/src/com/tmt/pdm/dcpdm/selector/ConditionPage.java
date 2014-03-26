package com.tmt.pdm.dcpdm.selector;

import java.util.Iterator;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.tmt.pdm.client.Starter;

import dyna.framework.service.dos.DOSChangeable;

public class ConditionPage extends WizardPage {

	
	private static final int WIDTH = 400;

	protected ConditionPage() {
		super("请选择DCPDM的图文档数据类型");
		setTitle("请选择DCPDM的图文档数据类型");
		setMessage("如果您知道将要选择的图文档的类型，请尽量缩小搜索范围提升查询速度。");
	}

	@Override
	public void createControl(Composite parent) {
		Composite pane = new Composite(parent,SWT.NONE);
		pane.setLayout(new FormLayout());
		FormData fd = new FormData();
		Control searchControl = createSearchCondition(pane);
		fd = new FormData();
		searchControl.setLayoutData(fd);
		fd.left = new FormAttachment(0, 10);
		fd.right = new FormAttachment(100, -10);
		fd.top = new FormAttachment(0, 10);
		fd.width = WIDTH;
		setPageComplete(!getWizard().docContainers.isEmpty());
		setControl(pane);
	}

	private Control createSearchCondition(Composite parent) {
		Composite pane = new Composite(parent, SWT.NONE);
		pane.setLayout(new GridLayout(2, false));
		Iterator<String> iter = getWizard().docContainers.iterator();
		while (iter.hasNext()) {
			String ouid = iter.next();
			try {
				createClassSelector(pane, ouid);
			} catch (Exception e) {
			}

		}
		return pane;
	}

	private void createClassSelector(Composite parent, final String ouid)
			throws Exception {
		DOSChangeable clasDBObject = Starter.dos.getClass(ouid);
		String title = (String) clasDBObject.get("title");
		getWizard().ouid_name.put(ouid, title);
		final Button b = new Button(parent, SWT.CHECK);
		b.setText(title);
		b.setSelection(true);
		b.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (b.getSelection()) {
					getWizard().docContainers.add(ouid);
				} else {
					getWizard().docContainers.remove(ouid);
				}
				setPageComplete(!getWizard().docContainers.isEmpty());
			}
		});
	}

	@Override
	public DCPDMObjectSelectWizard getWizard() {
		return (DCPDMObjectSelectWizard)super.getWizard();
	}
	

}
