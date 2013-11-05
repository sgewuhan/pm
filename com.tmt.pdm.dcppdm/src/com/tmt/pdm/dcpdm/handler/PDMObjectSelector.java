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
import org.eclipse.swt.widgets.Text;

import com.tmt.pdm.client.Starter;

import dyna.framework.iip.IIPRequestException;

public class PDMObjectSelector extends Dialog {

	private String[] docContainers;
	private String[] partContainer;
	private TableViewer viewer;
	private IStructuredSelection selection;

	protected PDMObjectSelector(Shell parentShell, List<?> docContainer,
			List<?> partContainer) {
		super(parentShell);
		this.docContainers = docContainer.toArray(new String[0]);
		this.partContainer = partContainer.toArray(new String[0]);

	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new FormLayout());
		final Text inputText = new Text(composite, SWT.BORDER | SWT.ICON_SEARCH);
		inputText.setMessage("��������");

		Button button = new Button(composite, SWT.PUSH);
		button.setText("��ѯ");
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
		viewer.getTable().setData(RWT.CUSTOM_ITEM_HEIGHT,new Integer(40));
		viewer.getTable().setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		viewer.getTable().setLinesVisible(true);
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
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

	protected String getElementLable(ArrayList ar) {
		String ouid = (String) ar.get(0);
//		fieldlist.add("80001a79");// ���
		String number = (String) ar.get(1);
//		fieldlist.add("80001a7a");// ����
		String name = (String) ar.get(2);
//		fieldlist.add("800017ac");// �汾
		String version = (String) ar.get(3);

//		fieldlist.add("80001aac");// ����ʱ��
		String user = (String) ar.get(4);

//		fieldlist.add("80001a7c");// ������
		String cdate = (String) ar.get(5);

		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:΢���ź�;font-size:9pt'>");
		sb.append(name);
		sb.append(" [");
		sb.append(number);
		sb.append("] �汾:");
		sb.append(version);
		sb.append(" <br/>����:");
		sb.append(cdate);
		sb.append(" | ");
		sb.append(user);
		
		sb.append("</span>");
		
		return sb.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void searchData(final String input) {

		final ArrayList result = new ArrayList();

		Job job = new Job("����PDM����") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				for (int i = 0; i < docContainers.length; i++) {
					ArrayList fieldlist = new ArrayList();
					fieldlist.add("80001a79");// ���
					fieldlist.add("80001a7a");// ����
					fieldlist.add("800017ac");// �汾
//					fieldlist.add("86366c51");// �׶�
					fieldlist.add("80001aac");// ����ʱ��
					fieldlist.add("80001a7c");// ������

					HashMap condition = new HashMap();
					condition.put("80001a79", "*" + input + "*");
					condition.put("version.condition.type", "wip");
					try {
						ArrayList r = Starter.dos.list(docContainers[i],
								fieldlist, condition);
						if (r != null) {
							result.addAll(r);
						}
					} catch (IIPRequestException e) {
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
