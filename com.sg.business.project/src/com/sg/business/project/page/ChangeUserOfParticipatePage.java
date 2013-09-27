package com.sg.business.project.page;

import java.util.List;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Project;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.viewer.ColumnAutoResizer;

public class ChangeUserOfParticipatePage extends WizardPage {
	private PrimaryObject master;
	private TableViewer table;

	protected ChangeUserOfParticipatePage(String sName, String sTitle,
			String sDescription, String navigatorid, PrimaryObject master) {
		super(sName);
		setTitle(sTitle);
		setDescription(sDescription);
		setMessage("将 ? 的工作移交给其他人");
		this.master = master;
	}

	@Override
	public void createControl(Composite parent) {
		FormData fd = new FormData();
		fd.top = new FormAttachment(0, 0);
		fd.left = new FormAttachment(0, 0);
		fd.right = new FormAttachment(100, 0);
		fd.bottom = new FormAttachment(100, 0);
		
		Composite content = new Composite(parent, SWT.NONE);
		parent.setLayout(new FormLayout());
		content.setLayoutData(fd);
		
		content.setLayout(new FormLayout());
		Project project = (Project) master;
		table = createParticipateList(content, project);
		table.getTable().setLayoutData(fd);

		setControl(content);
		setPageComplete(true);
	}

	private TableViewer createParticipateList(Composite parent, Project project) {

		TableViewer viewer = new TableViewer(parent, SWT.NONE);
		viewer.getTable().setHeaderVisible(false);
		viewer.getTable().setLinesVisible(true);
		TableViewerColumn col = new TableViewerColumn(viewer, SWT.LEFT);
//		col.getColumn().setText("项目参与者");

		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof String) {
					User user = UserToolkit.getUserById((String) element);
					if (user != null) {
						return user.getLabel();
					}
				}
				return "";
			}

			@Override
			public Image getImage(Object element) {
				if (element instanceof String) {
					User user = UserToolkit.getUserById((String) element);
					if (user != null) {
						return user.getImage();
					}
				}
				return null;
			}
		});
		viewer.setContentProvider(new IStructuredContentProvider() {

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
			}

			@Override
			public void dispose() {
			}

			@Override
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof Project) {
					List<?> participatesIdList = ((Project) inputElement)
							.getParticipatesIdList();
					return participatesIdList.toArray();
				}
				return new Object[0];
			}
		});
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ChangeUserOfParticipatePage.this.getContainer().updateButtons();
				ChangeUserWizard wiz = (ChangeUserWizard) getWizard();
				IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();
				if (selection != null && !selection.isEmpty()) {
					Object element = selection.getFirstElement();
					wiz.setChangedUserId((String) element);
					setMessage("将 \""
							+ UserToolkit.getUserById((String) element)
							+ "\" 的工作移交给其他人");
					
					ChangeUserOfOrgUserPage changeUserOfOrgUserPage = (ChangeUserOfOrgUserPage) getNextPage();
					if (wiz.getChangeUserId() != null) {
						changeUserOfOrgUserPage.setErrorMessage(null);
						changeUserOfOrgUserPage.setMessage("将 \""
								+ UserToolkit.getUserById((String) element)
								+ "\" 的工作移交给 \""
								+ UserToolkit.getUserById(wiz.getChangeUserId())+"\" ");
					} else {
						changeUserOfOrgUserPage.setMessage("将 \""
								+ UserToolkit.getUserById((String) element)
								+ "\" 的工作移交给 ? ");
					}
				} else {
					wiz.setChangedUserId(null);

				}

			}
		});

		viewer.setInput(project);
		new ColumnAutoResizer(viewer.getTable(), col.getColumn());

		return viewer;
	}

	@Override
	public boolean canFlipToNextPage() {
		ISelection selection = table.getSelection();
		if (selection != null && !selection.isEmpty()) {
			return true;
		}
		return false;
	}

}