package com.sg.business.visualization.table;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.ProjectTemplate;

public class ProductType extends ViewPart {
	private TableViewer viewer;
	public ProductType() {
	}

	@Override
	public void createPartControl(Composite parent) {
		createTable(parent);
		List<Object> typeList = new ArrayList<Object>();
		DBCollection collection = DBActivator.getCollection(
				IModelConstants.DB, IModelConstants.C_PROJECT_TEMPLATE);
		DBCursor cur = collection.find();
		while (cur.hasNext()) {
			DBObject dbo = cur.next();
			ProjectTemplate template = ModelService.createModelObject(dbo,
					ProjectTemplate.class);
			Object value = template.getValue(ProjectTemplate.F_PRODUCTTYPE_OPTION_SET);
			if(value instanceof List){
				@SuppressWarnings("unchecked")
				List<Object> list = (List<Object>) value;
				for(Object obj:list){
					if(!typeList.contains(obj)){
						typeList.add(obj);
					}
				}
				
			}
			
		}
		viewer.setInput(typeList);
		
	}

	private void createTable(Composite parent) {
		viewer = new TableViewer(parent, SWT.FULL_SELECTION);
		viewer.getTable().setHeaderVisible(false);
		viewer.getTable().setLinesVisible(true);
		TableViewerColumn col = new TableViewerColumn(viewer, SWT.LEFT);
		col.getColumn().setText("产品类型");
		col.getColumn().setWidth(320);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof String) {
					return (String)element;
				}
				return super.getText(element);
			}

		});
		
		viewer.setContentProvider(ArrayContentProvider.getInstance());
	}

	@Override
	public void setFocus() {

	}

}
