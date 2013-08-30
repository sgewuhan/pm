package com.sg.business.project.view;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.sg.business.model.check.ICheckListItem;
import com.sg.business.resource.BusinessResource;

public class ProjectCheckView extends ViewPart {

	private TableViewer viewer;

	public ProjectCheckView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent,SWT.FULL_SELECTION);
		
		TableViewerColumn column = new TableViewerColumn(viewer,SWT.LEFT);
		column.getColumn().setText("");
		column.getColumn().setWidth(80);
		column.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public Image getImage(Object element) {
				ICheckListItem ci = (ICheckListItem) element;
				int type = ci.getType();
				if(type==ICheckListItem.WARRING){
					return BusinessResource.getImage(BusinessResource.IMAGE_WARRING_16);
				}else if(type == ICheckListItem.ERROR){
					return BusinessResource.getImage(BusinessResource.IMAGE_ERROR_16);
				}else{
					return null;
				}
			}
			
			
			@Override
			public String getText(Object element) {
				ICheckListItem ci = (ICheckListItem) element;
				int type = ci.getType();

				if(type==ICheckListItem.WARRING){
					return "警告";
				}else if(type == ICheckListItem.ERROR){
					return "错误";
				}else{
					return "通过";
				}
			}
		});
		
		column = new TableViewerColumn(viewer,SWT.LEFT);
		column.getColumn().setText("检查项");
		column.getColumn().setWidth(160);
		column.setLabelProvider(new ColumnLabelProvider(){
			
			@Override
			public String getText(Object element) {
				ICheckListItem ci = (ICheckListItem) element;
				return ci.getTitle();
			}
		});
		
		column = new TableViewerColumn(viewer,SWT.LEFT);
		column.getColumn().setText("检查提示");
		column.getColumn().setWidth(240);
		column.setLabelProvider(new ColumnLabelProvider(){
			
			@Override
			public String getText(Object element) {
				ICheckListItem ci = (ICheckListItem) element;
				return ci.getMessage();
			}
		});
		viewer.setContentProvider(ArrayContentProvider.getInstance());
	}

	public void setInput(Object input){
		viewer.setInput(input);
	}
	
	@Override
	public void setFocus() {
		
	}

}
