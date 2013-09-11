package com.sg.business.work.view;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.mobnut.db.model.PrimaryObject;

public class ProjectDeliverableView extends ViewPart {

	private TableViewer viewer;
	public ProjectDeliverableView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.FULL_SELECTION);
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);
		TableViewerColumn col1 = new TableViewerColumn(viewer,SWT.LEFT);
		col1.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((PrimaryObject) element).getLabel();
			}

			@Override
			public Image getImage(Object element) {
				return ((PrimaryObject) element).getImage();
			}
			
			
			
		});
		
		
      viewer.setContentProvider(ArrayContentProvider.getInstance());
	}

	public void setInput(Object input) {
		viewer.setInput(input);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
