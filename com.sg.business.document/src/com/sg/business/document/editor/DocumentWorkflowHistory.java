package com.sg.business.document.editor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.BasicBSONList;
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.widgets.Section;
import org.jbpm.task.Status;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.business.commons.ui.flow.ProcessHistoryUIToolkit;
import com.sg.business.model.Document;
import com.sg.business.model.IDocumentProcess;
import com.sg.business.model.UserTask;
import com.sg.widgets.part.SimpleSection;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.AbstractFormPageDelegator;
import com.sg.widgets.registry.config.BasicPageConfigurator;

public class DocumentWorkflowHistory extends AbstractFormPageDelegator
		implements ISelectionChangedListener {

	private TableViewer taskViewer;
	private SimpleSection section2;
	private SimpleSection section1;

	@Override
	public Composite createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		super.createPageContent(parent, input, conf);
		Document doc = (Document) input.getData();
		parent.setLayout(new GridLayout());
		Composite panel = new Composite(parent, SWT.NONE);
		panel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

		panel.setLayout(new GridLayout());

		section1 = new SimpleSection(panel, Section.EXPANDED
				| Section.SHORT_TITLE_BAR | Section.TWISTIE);
		section1.setFont(font);

		section1.setText("流程历史");
		Composite table = createProcessContent(section1, doc);
		section1.setClient(table);

		section1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1,
				1));

		section2 = new SimpleSection(panel, Section.EXPANDED
				| Section.SHORT_TITLE_BAR | Section.TWISTIE);
		section2.setFont(font);
		section2.setText("流程过程详情");
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
				return "";
			}
		});
		taskViewer.setContentProvider(ArrayContentProvider.getInstance());

		ProcessHistoryUIToolkit
				.handleProcessHistoryTable(taskViewer.getTable());

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
				return "";
			}
		});
		viewer.setContentProvider(ArrayContentProvider.getInstance());

		BasicBSONList history = doc.getWorkflowHistory();
		viewer.setInput(history);
		viewer.addSelectionChangedListener(this);
		autoResize(parent, table);
		return table;

	}

	protected String getProcessInstanceLabel(DBObject dbObject) {
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt'>");
		// 工作desc
		String workDesc = (String) dbObject.get(PrimaryObject.F_DESC);
		workDesc = Utils.getPlainText(workDesc);
		sb.append(workDesc);

		sb.append("  ");

		// 时间
		Date date = (Date) dbObject.get(PrimaryObject.F__CDATE);
		sb.append(String.format(Utils.FORMATE_DATE_SIMPLE, date));

		sb.append("<br/>");

		// 流程名称
		sb.append("<small>");
		String processName = (String) dbObject
				.get(IDocumentProcess.F_PROCESSNAME);
		workDesc = Utils.getPlainText(processName);
		sb.append(processName);
		sb.append("</small>");
		sb.append("</span>");
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
		PrimaryObject[] input;
		if (sel == null || sel.isEmpty()) {
			input = new PrimaryObject[0];
		} else {
			DBObject processItem = (DBObject) sel
					.getFirstElement();
			List<?> history = (List<?>) processItem.get(IDocumentProcess.F_HISTORY);
			List<Object> removeHistory = new ArrayList<Object>();
			for (Object object : history) {
				if(! Status.Completed.name().equals(((DBObject)object).get(UserTask.F_STATUS))){
					removeHistory.add(object);
				}
			}
			history.removeAll(removeHistory);
			input = new PrimaryObject[history.size()];
			for (int i = 0; i < input.length; i++) {
				input[i] = ModelService.createModelObject((DBObject)history.get(i), UserTask.class);
			}
			
			
		}
		taskViewer.setInput(input);
		
		section2.layout();
		section2.reflow();
	}
}
