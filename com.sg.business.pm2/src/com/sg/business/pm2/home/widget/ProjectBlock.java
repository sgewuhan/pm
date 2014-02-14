package com.sg.business.pm2.home.widget;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.widgets.block.Block;
import com.sg.widgets.commons.labelprovider.CommonHTMLLabel;
import com.sg.widgets.part.CurrentAccountContext;

public class ProjectBlock extends Block {

	private String usetId;

	public ProjectBlock(Composite parent) {
		super(parent);
		this.usetId = new CurrentAccountContext().getUserId();
	}

	@Override
	protected void createContent(Composite parent) {
		parent.setLayout(new FillLayout());
		// 显示我负责的项目
		CTabFolder tabFolder = new CTabFolder(parent, SWT.NONE);
		tabFolder.setData(RWT.CUSTOM_VARIANT, "projectlist");
		tabFolder.setTabHeight(0);

		FormData fd = new FormData();
		TableViewer tv = createTable(tabFolder, fd);
		fd.left = new FormAttachment(0, 0);
		fd.right = new FormAttachment(100, 0);
		fd.top = new FormAttachment(0, 1);
		fd.bottom = new FormAttachment(100, -1);

		List<Project> projectList = new ArrayList<Project>();

		DBCollection projectCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT);
		DBCursor projectCursor = projectCol.find(new BasicDBObject().append(
				Project.F_CHARGER, usetId));
		while (projectCursor.hasNext()) {
			DBObject projectData = projectCursor.next();
			Project project = ModelService.createModelObject(projectData,
					Project.class);
			projectList.add(project);
		}
		tv.setInput(projectList);
	}

	private TableViewer createTable(CTabFolder tabFolder, FormData fd) {
		TableViewer tv = new TableViewer(tabFolder, SWT.NONE);
		Control table = tv.getControl();
		table.setLayoutData(fd);
		table.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		table.setData(RWT.CUSTOM_ITEM_HEIGHT, 40);
		tv.setContentProvider(ArrayContentProvider.getInstance());

		TableViewerColumn col = new TableViewerColumn(tv, SWT.CENTER);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Project) {
					Project project = (Project) element;
					// TODO
					CommonHTMLLabel projectHtmlLabel = project
							.getAdapter(CommonHTMLLabel.class);
					return projectHtmlLabel.getHTML();
				}
				return ""; //$NON-NLS-1$
			}

		});

		return tv;
	}
}
