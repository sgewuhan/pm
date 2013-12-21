package com.sg.business.document.editor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.widgets.Section;
import org.jbpm.task.Status;

import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.business.commons.ui.flow.ProcessHistoryUIToolkit;
import com.sg.business.document.nls.Messages;
import com.sg.business.model.Document;
import com.sg.business.model.IDocumentProcess;
import com.sg.business.model.UserTask;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.part.SimpleSection;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.AbstractFormPageDelegator;
import com.sg.widgets.registry.config.BasicPageConfigurator;

public class DocumentWorkflowHistory extends AbstractFormPageDelegator
		implements ISelectionChangedListener {

	private TableViewer taskViewer;
	private SimpleSection section2;
	private SimpleSection section1;
	private Document doc;

	@Override
	public Composite createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		super.createPageContent(parent, input, conf);

		doc = (Document) input.getData();
		parent.setLayout(new GridLayout());
		Composite panel = new Composite(parent, SWT.NONE);
		panel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

		panel.setLayout(new GridLayout());

		section1 = new SimpleSection(panel, Section.EXPANDED
				| Section.SHORT_TITLE_BAR | Section.TWISTIE);
		section1.setFont(font);

		section1.setText(Messages.get().DocumentWorkflowHistory_0);
		Composite table = createProcessContent(section1, doc);
		section1.setClient(table);

		section1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1,
				1));

		section2 = new SimpleSection(panel, Section.EXPANDED
				| Section.SHORT_TITLE_BAR | Section.TWISTIE);
		section2.setFont(font);
		section2.setText(Messages.get().DocumentWorkflowHistory_1);
		Composite table2 = createTaskContent(section2, doc);
		section2.setClient(table2);
		section2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));

		return panel;
	}

	private Composite createTaskContent(Composite parent, Document doc) {
		taskViewer = new TableViewer(parent, SWT.FULL_SELECTION);
		Table table = taskViewer.getTable();
		table.setData(RWT.CUSTOM_ITEM_HEIGHT, 40);
		table.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		TableViewerColumn column = new TableViewerColumn(taskViewer, SWT.LEFT);
		column.getColumn().setWidth(360);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof UserTask) {
					return ((UserTask) element).getHTMLLabel();
				}
				return ""; //$NON-NLS-1$
			}
		});
		taskViewer.setContentProvider(ArrayContentProvider.getInstance());

		ProcessHistoryUIToolkit.handleProcessHistoryTable(table);

		autoResize(parent, table);
		return table;
	}

	private void autoResize(final Composite parent, final Table table) {
		parent.addControlListener(new ControlListener() {

			@Override
			public void controlResized(ControlEvent e) {
				int maxWidth = parent.getBounds().width - 40;
				table.getColumn(0).setWidth(maxWidth);
			}

			@Override
			public void controlMoved(ControlEvent e) {
			}
		});
	}

	private Composite createProcessContent(Composite parent, Document doc) {
		TableViewer viewer = new TableViewer(parent, SWT.FULL_SELECTION);
		Table table = viewer.getTable();
		table.setData(RWT.CUSTOM_ITEM_HEIGHT, 40);
		table.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		TableViewerColumn column = new TableViewerColumn(viewer, SWT.LEFT);
		column.getColumn().setWidth(360);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof DBObject) {
					return getProcessInstanceLabel((DBObject) element);
				}
				return ""; //$NON-NLS-1$
			}
		});
		viewer.setContentProvider(ArrayContentProvider.getInstance());

		BasicBSONList history = doc.getWorkflowHistory();
		viewer.setInput(history);
		viewer.addSelectionChangedListener(this);
		autoResize(parent, table);

		table.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (event.detail == RWT.HYPERLINK) {
					try {
						String[] para = event.text.substring(
								event.text.lastIndexOf("/") + 1).split("@"); //$NON-NLS-1$ //$NON-NLS-2$
						if ("print".equals(para[2])) { //$NON-NLS-1$
							// 20102652
							ProcessHistoryUIToolkit.doPrint(Long
									.parseLong(para[0]), new ObjectId(para[1]));
						}
					} catch (Exception e) {
					}
				}
			}
		});

		return table;

	}

	protected String getProcessInstanceLabel(DBObject dbObject) {
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt'>"); //$NON-NLS-1$
		// 工作desc
		String workDesc = (String) dbObject.get(PrimaryObject.F_DESC);
		workDesc = Utils.getPlainText(workDesc);
		sb.append(workDesc);

		sb.append("  "); //$NON-NLS-1$

		// 时间
		Date date = (Date) dbObject.get(PrimaryObject.F__CDATE);
		sb.append(String.format(Utils.FORMATE_DATE_SIMPLE, date));

		sb.append("<br/>"); //$NON-NLS-1$

		// 流程名称
		sb.append("<small>"); //$NON-NLS-1$
		String processName = (String) dbObject
				.get(IDocumentProcess.F_PROCESSNAME);
		workDesc = Utils.getPlainText(processName);
		sb.append(processName);
		sb.append("</small>"); //$NON-NLS-1$
		sb.append("</span>"); //$NON-NLS-1$
		sb.append("<a href=\"" //$NON-NLS-1$
				+ dbObject.get(IDocumentProcess.F_PROCESS_INSTANCEID) + "@" //$NON-NLS-1$
				+ doc.get_id() + "@print" + "\" target=\"_rwt\">"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("<img src='"); //$NON-NLS-1$
		sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_PRINT_W_48,
				BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER));
		sb.append("' style='border-style:none;position:absolute; right:0; top:0; display:block;' width='32' height='32' />"); //$NON-NLS-1$
		sb.append("</a>"); //$NON-NLS-1$

		return sb.toString();
	}

	@Override
	public void commit(boolean onSave) {
	}

	@Override
	public void setFocus() {

	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		IStructuredSelection sel = (IStructuredSelection) event.getSelection();
		List<PrimaryObject> input = new ArrayList<PrimaryObject>();
		if (sel != null && !sel.isEmpty()) {
			DBObject processItem = (DBObject) sel
					.getFirstElement();
			List<?> history = (List<?>) processItem.get(IDocumentProcess.F_HISTORY);
			for (Object object : history) {
				if( Status.Completed.name().equals(((DBObject)object).get(UserTask.F_STATUS))){
					input.add(ModelService.createModelObject((DBObject)object, UserTask.class));
				}
			}
		}
		taskViewer.setInput(input);
		section2.layout();
		section2.reflow();
	}
}
