package com.sg.business.project.wizards;

import java.util.List;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.sg.business.model.Project;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.NavigatorControl;
import com.sg.widgets.part.editor.IEditorActionListener;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.INavigatorPageBodyPartCreater;
import com.sg.widgets.part.editor.page.NavigatorPage;
import com.sg.widgets.viewer.ColumnAutoResizer;

public class ProjectTeamPage implements INavigatorPageBodyPartCreater {

	private Composite participatePanel;
	private TableViewer participateTable;
	private Project project;

	@Override
	public void createNavigatorBody(Composite body, NavigatorControl navi,
			PrimaryObjectEditorInput input, NavigatorPage navigatorPage) {
		body.setLayout(new FillLayout());

		SashForm sash = new SashForm(body, SWT.HORIZONTAL);

		Composite rolepanel = new Composite(sash, SWT.NONE);
		navi.createPartContent(rolepanel);

		participatePanel = new Composite(sash, SWT.NONE);
		participatePanel.setLayout(new FormLayout());
		Label sep = new Label(participatePanel, SWT.SEPARATOR | SWT.VERTICAL);
		FormData fd = new FormData();
		sep.setLayoutData(fd);
		fd.left = new FormAttachment();
		fd.top = new FormAttachment();
		fd.width = 1;
		fd.bottom = new FormAttachment(100);

		participateTable = createParticipateList(participatePanel, input);
		fd = new FormData();
		participateTable.getTable().setLayoutData(fd);
		fd.left = new FormAttachment(sep);
		fd.top = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(100);

		sash.setWeights(new int[] { 3, 1 });

	}

	private TableViewer createParticipateList(Composite parent,
			PrimaryObjectEditorInput input) {
		project = (Project) input.getData();

		TableViewer viewer = new TableViewer(parent, SWT.FULL_SELECTION);
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);
		TableViewerColumn col = new TableViewerColumn(viewer, SWT.LEFT);
		col.getColumn().setText("项目参与者");

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

		viewer.setInput(project);
		new ColumnAutoResizer(viewer.getTable(), col.getColumn());

		return viewer;
	}

	@Override
	public void editorAction(IEditorActionListener reciever, int code,
			Object object) {
		if (participateTable != null
				&& !participateTable.getControl().isDisposed()) {
			try {
				project.reload(Project.F_PARTICIPATE);
				participateTable.refresh();
			} catch (Exception e) {
			}
		}
	}

}
