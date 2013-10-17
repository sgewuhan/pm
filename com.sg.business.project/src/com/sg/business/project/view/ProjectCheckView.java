package com.sg.business.project.view;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Project;
import com.sg.business.model.check.ICheckListItem;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.part.editor.DataObjectEditor;
import com.sg.widgets.part.editor.page.NavigatorPage;
import com.sg.widgets.viewer.ViewerControl;

public class ProjectCheckView extends ViewPart {

	private TableViewer viewer;

	public ProjectCheckView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.FULL_SELECTION);
		//viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);
		viewer.getTable().setData(RWT.MARKUP_ENABLED, true);
		viewer.getTable().setData(RWT.CUSTOM_ITEM_HEIGHT, 64);

		TableViewerColumn column = new TableViewerColumn(viewer, SWT.LEFT);
		column.getColumn().setText("");
		column.getColumn().setWidth(80);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Image getImage(Object element) {
				ICheckListItem ci = (ICheckListItem) element;
				int type = ci.getType();
				if (type == ICheckListItem.WARRING) {
					return BusinessResource
							.getImage(BusinessResource.IMAGE_WARNING_16);
				} else if (type == ICheckListItem.ERROR) {
					return BusinessResource
							.getImage(BusinessResource.IMAGE_ERROR_16);
				} else {
					return null;
				}
			}

			@Override
			public String getText(Object element) {
				ICheckListItem ci = (ICheckListItem) element;
				int type = ci.getType();

				if (type == ICheckListItem.WARRING) {
					return "警告";
				} else if (type == ICheckListItem.ERROR) {
					return "错误";
				} else {
					return "通过";
				}
			}
		});

		column = new TableViewerColumn(viewer, SWT.LEFT);
		column.getColumn().setText("检查项");
		column.getColumn().setWidth(160);
		column.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				ICheckListItem ci = (ICheckListItem) element;
				return ci.getTitle();
			}
		});

		column = new TableViewerColumn(viewer, SWT.LEFT);
		column.getColumn().setText("检查结果");
		column.getColumn().setWidth(240);
		column.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				ICheckListItem ci = (ICheckListItem) element;
				return ci.getResult();
			}
		});

		column = new TableViewerColumn(viewer, SWT.LEFT);
		column.getColumn().setText("检查提示");
		column.getColumn().setWidth(240);
		column.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				ICheckListItem ci = (ICheckListItem) element;
				return ci.getMessage();
			}
		});

		column = new TableViewerColumn(viewer, SWT.LEFT);
		column.getColumn().setText("目标");
		column.getColumn().setWidth(180);
		column.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public Image getImage(Object element) {
				ICheckListItem ci = (ICheckListItem) element;
				Object data = ci.getData();
				if (data instanceof PrimaryObject) {
					return ((PrimaryObject) data).getImage();
				}
				return null;
			}

			@Override
			public String getText(Object element) {
				ICheckListItem ci = (ICheckListItem) element;
				Object data = ci.getData();
				if (data instanceof PrimaryObject) {
					return ((PrimaryObject) data).getTypeName() + ":"
							+ ((PrimaryObject) data).getLabel();
				}
				return "";
			}
		});

		column = new TableViewerColumn(viewer, SWT.LEFT);
		column.getColumn().setText("对象");
		column.getColumn().setWidth(160);
		column.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public Image getImage(Object element) {
				ICheckListItem ci = (ICheckListItem) element;
				PrimaryObject data = ci.getSelection();
				if (data != null) {
					return ((PrimaryObject) data).getImage();
				} else {
					return null;
				}
			}

			@Override
			public String getText(Object element) {
				ICheckListItem ci = (ICheckListItem) element;
				PrimaryObject data = ci.getSelection();
				if (data != null) {
					return ((PrimaryObject) data).getTypeName() + ":"
							+ ((PrimaryObject) data).getLabel();
				} else {
					return "";
				}
			}
		});
		viewer.setContentProvider(ArrayContentProvider.getInstance());

		viewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				open((ICheckListItem) ((IStructuredSelection) event
						.getSelection()).getFirstElement());
			}
		});
	}

	protected void open(ICheckListItem ci) {
		Object data = ci.getData();
		if (data instanceof Project) {
			Project po = (Project) data;
			String editorId = ci.getEditorId();
			if (editorId != null) {
				String pageId = ci.getPageId();
				if (editorId != null) {
					try {
						DataObjectEditor editor = DataObjectEditor.open(po,
								editorId, true, null);
						if (pageId != null) {
							editor.setActivePage(pageId);
						}
						PrimaryObject selection = ci.getSelection();
						if (selection != null) {
							Object page = editor.getSelectedPage();
							if (page instanceof NavigatorPage) {
								NavigatorPage navigatorPage = (NavigatorPage) page;
								ViewerControl vc = navigatorPage.getNavigator()
										.getViewerControl();
								vc.getViewer().setSelection(
										new StructuredSelection(
												new Object[] { selection }),
										true);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void setInput(Object input) {
		viewer.setInput(input);
		viewer.setFilters(new ViewerFilter[] { new ViewerFilter() {

			@Override
			public boolean select(Viewer viewer, Object parentElement,
					Object element) {
				if (element instanceof ICheckListItem) {
					ICheckListItem iCheckListItem = (ICheckListItem) element;
					return ICheckListItem.WARRING == iCheckListItem.getType()
							|| ICheckListItem.ERROR == iCheckListItem.getType();
				}
				return false;
			}
		} });
	}

	@Override
	public void setFocus() {

	}

}
