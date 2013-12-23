package com.tmt.tb.editor.page;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.portal.Portal;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.TaskForm;
import com.sg.business.model.Work;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.AbstractFormPageDelegator;
import com.sg.widgets.registry.config.BasicPageConfigurator;
import com.sg.widgets.registry.config.TableConfigurator;
import com.sg.widgets.viewer.CTableViewer;

public class EngineeringChangeItem extends AbstractFormPageDelegator {

	private TableViewer viewer;
	private IContext context;

	public EngineeringChangeItem() {
	}

	@Override
	public Composite createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		setFormInput(input);
		setDirty(true);

		context = new CurrentAccountContext();
		createTable(input,parent);
		TaskForm taskform = (TaskForm) input.getData();

		List<Work> changeItems = new ArrayList<Work>();
		try {
			Object value = taskform.getProcessInstanceVarible("ecn", //$NON-NLS-1$
					context);
			if (value == null) {

				DBCollection collection = DBActivator.getCollection(
						IModelConstants.DB, IModelConstants.C_WORK_DEFINITION);
				DBCursor cur = collection.find(new BasicDBObject()
						.append(WorkDefinition.F_ACTIVATED, Boolean.TRUE)
						.append(WorkDefinition.F_INTERNAL_TYPE,WorkDefinition.INTERNAL_TYPE_CHANGEITEM));
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

	private void createTable(PrimaryObjectEditorInput input,Composite parent) {
		TableConfigurator configurator=(TableConfigurator) Widgets.getTableRegistry().getConfigurator("ec.standlonework"); //$NON-NLS-1$
		viewer=new CTableViewer(parent, configurator);
	}


	@Override
	public boolean canRefresh() {
		return false;
	}

	@Override
	public void refresh() {

	}

	@Override
	public void commit(boolean onSave) {
		TaskForm taskform = (TaskForm) getInputData();
		taskform.setValue("ecn",viewer.getInput() ); //$NON-NLS-1$
		setDirty(false);
		
	}

	@Override
	public void setFocus() {
		
	}

}
