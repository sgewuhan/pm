package com.tmt.pdm.dcpdm.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import com.mobnut.commons.html.HtmlUtil;
import com.tmt.pdm.client.Starter;
import com.tmt.pdm.dcpdm.nls.Messages;

@Deprecated
public class PDMObjectSelector extends Dialog {

	private String[] docContainers;
//	private String[] partContainer;
	private TableViewer viewer;
	private IStructuredSelection selection;

	public PDMObjectSelector(Shell parentShell, List<?> docContainer,
			List<?> partContainer) {
		super(parentShell);
		this.docContainers = docContainer.toArray(new String[0]);
//		if(partContainer!=null){
//			this.partContainer = partContainer.toArray(new String[0]);
//		}

	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new FormLayout());
		final Text inputText = new Text(composite, SWT.BORDER | SWT.ICON_SEARCH);
		inputText.setMessage(Messages.get().PDMObjectSelector_0);

		Button button = new Button(composite, SWT.PUSH);
		button.setText(Messages.get().PDMObjectSelector_1);
		button.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				searchData(inputText.getText());
			}
		});

		FormData fd = new FormData();
		inputText.setLayoutData(fd);
		fd.top = new FormAttachment(0, 2);
		fd.left = new FormAttachment(0, 2);
		fd.right = new FormAttachment(button, -2);

		fd = new FormData();
		button.setLayoutData(fd);
		fd.top = new FormAttachment(0, 2);
		fd.right = new FormAttachment(100, -2);

		viewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION
				| SWT.VIRTUAL|SWT.MULTI);
		Table table = viewer.getTable();
		table.setData(RWT.CUSTOM_ITEM_HEIGHT,new Integer(40));
		HtmlUtil.enableMarkup(table);
		table.setLinesVisible(true);
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				@SuppressWarnings("rawtypes")
				ArrayList ar = ((ArrayList) element);
				return getElementLable(ar);
			}
		});

		Control control = viewer.getControl();
		FormData fd1 = new FormData();
		control.setLayoutData(fd1);
		fd1.left = new FormAttachment(0, 2);
		fd1.right = new FormAttachment(100, -2);
		fd1.top = new FormAttachment(inputText, 2);
		fd1.bottom = new FormAttachment(100, -2);
		fd1.width = 500;
		fd1.height = 500;
		fd.bottom = new FormAttachment(control, -2);
		composite.pack();
		return composite;
	}

	protected String getElementLable(@SuppressWarnings("rawtypes") ArrayList ar) {
//		String ouid = (String) ar.get(0);
//		fieldlist.add("80001a79");// 编号
		String number = (String) ar.get(1);
//		fieldlist.add("80001a7a");// 名称
		String name = (String) ar.get(2);
//		fieldlist.add("800017ac");// 版本
		String version = (String) ar.get(3);

//		fieldlist.add("80001aac");// 创建时间
		String user = (String) ar.get(4);

//		fieldlist.add("80001a7c");// 创建人
		String cdate = (String) ar.get(5);

		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt'>"); //$NON-NLS-1$
		sb.append(name);
		sb.append(" ["); //$NON-NLS-1$
		sb.append(number);
		sb.append(Messages.get().PDMObjectSelector_4);
		sb.append(version);
		sb.append(Messages.get().PDMObjectSelector_5);
		sb.append(cdate);
		sb.append(" | "); //$NON-NLS-1$
		sb.append(user);
		
		sb.append("</span>"); //$NON-NLS-1$
		
		return sb.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void searchData(final String input) {

		final ArrayList result = new ArrayList();

		Job job = new Job("检索PDM数据") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				for (int i = 0; i < docContainers.length; i++) {
					ArrayList fieldlist = new ArrayList();
					fieldlist.add("80001a79");// 编号 //$NON-NLS-1$
					fieldlist.add("80001a7a");// 名称 //$NON-NLS-1$
					fieldlist.add(Messages.get().PDMObjectSelector_11);// 版本
//					fieldlist.add("86366c51");// 阶段
					fieldlist.add("80001aac");// 创建时间 //$NON-NLS-1$
					fieldlist.add("80001a7c");// 创建人 //$NON-NLS-1$

					HashMap condition = new HashMap();
					condition.put("80001a79", "*" + input + "*"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					condition.put("version.condition.type", "wip"); //$NON-NLS-1$ //$NON-NLS-2$
					try {
						ArrayList r = Starter.dos.list(docContainers[i],
								fieldlist, condition);
						if (r != null) {
							result.addAll(r);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.schedule();

		job.addJobChangeListener(new JobChangeAdapter() {
			@Override
			public void done(IJobChangeEvent event) {
				getShell().getDisplay().asyncExec(new Runnable() {

					@Override
					public void run() {
						viewer.setInput(result);
					}
				});
			}
		});

	}
	
	
	@Override
	protected void okPressed() {
		this.selection = (IStructuredSelection)viewer.getSelection();
		super.okPressed();
	}

	@SuppressWarnings("rawtypes")
	public String[] getSelection() {
		if(selection==null){
			return new String[0];
		}else{
			String[] result = new String[selection.size()];
			Object[] ar = selection.toArray();
			for (int i = 0; i < result.length; i++) {
				result[i] = (String) ((ArrayList)ar[i]).get(0);
			}
			return result;
		}
	}

}
