package com.sg.business.commons.ui.part;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.jface.util.Util;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.model.Deliverable;
import com.sg.business.model.Document;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.resource.nls.Messages;

public class WorkListCreater extends Composite {

	private List<Work> input;
	private TreeViewer viewer;
	private IContext context;

	public WorkListCreater(Composite parent, int style) {
		super(parent, style);
		this.setLayout(new FillLayout());
		createContent(this);
	}

	private void createContent(Composite parent) {
		viewer = new TreeViewer(parent, SWT.FULL_SELECTION);

		viewer.getTree().setHeaderVisible(true);
		viewer.setContentProvider(new TemplateWorkContentProvider());
		TreeViewerColumn viewerColumn = new TreeViewerColumn(viewer, SWT.LEFT);
		viewerColumn.getColumn().setWidth(240);
		viewerColumn.getColumn().setText(Messages.get().WorkListCreater_0);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((PrimaryObject) element).getLabel();
			}

			@Override
			public Image getImage(Object element) {
				return ((PrimaryObject) element).getImage();
			}
		});

		viewerColumn = new TreeViewerColumn(viewer, SWT.LEFT);
		viewerColumn.getColumn().setWidth(100);
		viewerColumn.getColumn().setText(Messages.get().WorkListCreater_1);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Work) {
					Work work = (Work) element;
					Date date = work.getPlanStart();
					if (date != null) {
						return String.format(Utils.FORMATE_DATE_SIMPLE, date);
					}
				}
				return ""; //$NON-NLS-1$
			}
		});

		viewerColumn = new TreeViewerColumn(viewer, SWT.LEFT);
		viewerColumn.getColumn().setWidth(100);
		viewerColumn.getColumn().setText(Messages.get().WorkListCreater_3);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Work) {
					Work work = (Work) element;
					Date date = work.getPlanFinish();
					if (date != null) {
						return String.format(Utils.FORMATE_DATE_SIMPLE, date);
					}
				}
				return ""; //$NON-NLS-1$
			}
		});

		viewerColumn = new TreeViewerColumn(viewer, SWT.LEFT);
		viewerColumn.getColumn().setWidth(100);
		viewerColumn.getColumn().setText(Messages.get().WorkListCreater_5);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Work) {
					Work work = (Work) element;
					User charger = work.getCharger();
					if (charger != null) {
						return charger.getLabel();
					}
				}
				return ""; //$NON-NLS-1$
			}
		});

	}

	public void commit() {
		// TODO

	}

	public void setInput(List<Work> input) {
		if (Util.equals(this.input, input)) {
			return;
		}

		this.input = input;
		viewer.setInput(input);
	}

	public void refresh() {
		viewer.refresh();
	}

	public IStructuredSelection getSelection() {
		return (IStructuredSelection) viewer.getSelection();
	}

	@SuppressWarnings("rawtypes")
	public void remove(Deliverable deliverable) throws Exception {
		Work work = (Work) deliverable.getParentPrimaryObjectCache();
		if (deliverable.isPersistent()) {
			deliverable.doRemove(context);
		} else {
			Object value = work.getValue(Work.TEMPLATE_DELIVERABLE);
			DBObject data = deliverable.get_data();
			if (value instanceof List<?>) {
				((List) value).remove(data);

			} else if (value instanceof Object[]) {
				value = Utils.removeElementInArray((Object[]) value, data,
						false, Object.class);
			}
			work.setValue(Work.TEMPLATE_DELIVERABLE, value);
		}
		viewer.refresh();
	}

	public void remove(Work work) throws Exception {
		input.remove(work);
		if (work.isPersistent()) {
			work.doRemove(context);
		}
		viewer.refresh();
	}

	public void createDeliverable(Work work, List<Document> docList, String type) {
		if (docList != null) {
			for (Document doc : docList) {
				if (type == null) {
					DBCollection delCol = DBActivator.getCollection(
							IModelConstants.DB, IModelConstants.C_DELIEVERABLE);
					long count = delCol.count(new BasicDBObject().append(
							Deliverable.F_DOCUMENT_ID, doc.get_id()));
					if (count > 0) {
						type = Deliverable.TYPE_LINK;
					} else {
						type = Deliverable.TYPE_OUTPUT;
					}
				}
				createDeliverable(work, doc, type);
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void createDeliverable(Work work, Document doc, String type) {
		
		Deliverable deliverable = work.makeDeliverableDefinition(type);
		deliverable.setValue(Deliverable.F_DOCUMENT_ID, doc.get_id());
		deliverable.setParentPrimaryObject(work);

		Object value = work.getValue(Work.TEMPLATE_DELIVERABLE);
		DBObject data = deliverable.get_data();
		if (value instanceof List<?>) {
			if (!inArray(data, ((List) value).toArray())) {
				((List) value).add(data);
			}

		} else if (value instanceof Object[]) {
			if (!inArray(data, ((Object[]) value))) {
				value = Utils.addElementToArray((Object[]) value, data, false,
						Object.class);
			}
		} else if (value == null) {
			value = new ArrayList<DBObject>();
			((List<DBObject>) value).add(data);
		}
		work.setValue(Work.TEMPLATE_DELIVERABLE, value);
		work.setValue(Work.F_CHARGER, doc.get_caccount().getUserId());
		viewer.refresh();
		viewer.expandAll();
	}

	private boolean inArray(DBObject data, Object[] array) {
		for (int i = 0; i < array.length; i++) {
			DBObject obj = (DBObject) array[i];
			Object object = obj.get(Deliverable.F_DOCUMENT_ID);
			if (object.equals(data.get(Deliverable.F_DOCUMENT_ID))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 选择变更计划，创建变更工作
	 * 
	 * @param work
	 */
	public void createWork(Work work) {
		input.add(work);
		viewer.refresh();
		viewer.setSelection(new StructuredSelection(work), true);
	}

	public void setContext(IContext context) {
		this.context = context;
	}

	public List<Work> getInput() {
		return input;
	}

}
