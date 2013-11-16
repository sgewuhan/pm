package com.tmt.tb.editor.page;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IFormPart;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.portal.Portal;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.IProcessControl;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectBudget;
import com.sg.business.model.TaskForm;
import com.sg.business.model.Work;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.registry.config.BasicPageConfigurator;
import com.sg.widgets.registry.config.IPageDelegator;

public class EngineeringChangeItem implements IPageDelegator {

	private TableViewer viewer;
	private IContext context;

	public EngineeringChangeItem() {
	}

	@Override
	public Composite createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		context = new CurrentAccountContext();
		createTable(input,parent);
		TaskForm taskform = (TaskForm) input.getData();

		List<Work> changeItems = new ArrayList<Work>();
		try {
			Object value = taskform.getProcessInstanceVarible("ecn",
					new CurrentAccountContext());
			if (value == null) {
				Organization organization = null;
				Object val = taskform.getProcessInstanceVarible("project",
						context);
				if (val instanceof String) {
					ObjectId projectid = new ObjectId((String) val);
					Project project = ModelService.createModelObject(
							Project.class, projectid);
					organization = project.getFunctionOrganization();
				}
				// TODO:项目归口组织下级组织中的独立工作定义

				DBCollection collection = DBActivator.getCollection(
						IModelConstants.DB, IModelConstants.C_WORK_DEFINITION);
				DBCursor cur = collection.find(new BasicDBObject()
						.append(WorkDefinition.F_ACTIVATED, Boolean.TRUE)
						.append(WorkDefinition.F_INTERNALBY, Boolean.TRUE)
						.append(WorkDefinition.F_CHANGEITEM, Boolean.TRUE)
						.append(WorkDefinition.F_ORGANIZATION_ID,
								organization.get_id()));
				while (cur.hasNext()) {
					DBObject dbo = cur.next();
					WorkDefinition workd = ModelService.createModelObject(dbo,
							WorkDefinition.class);
					Work work = workd.makeStandloneWork(null, context);
					work.setValue(Work.F_DESC, workd.getDesc());
					changeItems.add(work);
				}
			} else {
				if (value instanceof List<?>) {
					List<?> list = (List<?>) value;
					for (Object dbo : list) {
						Work work = ModelService.createModelObject(
								(DBObject) dbo, Work.class);
						changeItems.add(work);
					}
				} else if (value instanceof Object[]) {
					Object[] array = (Object[]) value;
					for (Object dbo : array) {
						Work work = ModelService.createModelObject(
								(DBObject) dbo, Work.class);
						changeItems.add(work);
					}

				}
			}

		} catch (Exception e) {
			if (Portal.getDefault().isDevelopMode()) {
				e.printStackTrace();
			}
		}
		viewer.setInput(changeItems);
		return viewer.getTable();
	}

	private void createTable(PrimaryObjectEditorInput input,final Composite parent) {
		viewer = new TableViewer(parent, SWT.FULL_SELECTION);
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);
		TableViewerColumn col = new TableViewerColumn(viewer, SWT.LEFT);
		col.getColumn().setText("变更项点");
		col.getColumn().setWidth(100);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Work) {
					Work work = (Work) element;
					return work.getDesc();
				}
				return super.getText(element);
			}

		});

		col = new TableViewerColumn(viewer, SWT.LEFT);
		col.getColumn().setText("是否必须");
		col.getColumn().setWidth(64);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Work) {
					Work work = (Work) element;
					return (String) work.getValue(Work.TEMPLATE_MANDATORY);
				}
				return super.getText(element);
			}
			
		});
		
		
		
		if(input.isEditable()){
			EditingSupport es = new EditingSupport(viewer) {
				
				@Override
				protected void setValue(Object element, Object value) {
					Work work = (Work)element;
					work.setValue(Work.TEMPLATE_DELIVERABLE, value);
					viewer.refresh();
				}
				
				@Override
				protected Object getValue(Object element) {
					Work work = (Work)element;
				    return work.getValue(Work.TEMPLATE_MANDATORY);
				}
				
				@Override
				protected CellEditor getCellEditor(Object element) {
					Button button = new Button(parent, SWT.CHECK);
					reurn null;
				}
				
				@Override
				protected boolean canEdit(Object element) {
					return true;
				}
			};
			col.setEditingSupport(es);
		}
		
		
		
		col = new TableViewerColumn(viewer, SWT.LEFT);
		col.getColumn().setText("工作负责人");
		col.getColumn().setWidth(64);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Work) {
					Work work = (Work) element;
				}
				return super.getText(element);
			}
			
		});

		viewer.setContentProvider(ArrayContentProvider.getInstance());
	}

	@Override
	public IFormPart getFormPart() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canRefresh() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

}
