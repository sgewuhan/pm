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
import org.eclipse.swt.internal.widgets.MarkupValidator;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.widgets.Widgets;
import com.sg.widgets.block.Block;
import com.sg.widgets.commons.labelprovider.CommonHTMLLabel;
import com.sg.widgets.part.CurrentAccountContext;

public class ProjectBlock extends Block {

	private TableViewer tv;
	private String usetId;
	private DBCollection projectCol;

	public static final int X_COUNT = 3;
	public static final int Y_COUNT = 2;
	public static final int BLOCKSIZE = 100;

	public ProjectBlock(Composite parent) {
		super(parent);
	}

	@Override
	protected void createContent(Composite parent) {
		parent.setLayout(new FillLayout());
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setBackground(Widgets.getColor(composite.getDisplay(), 0xed,
				0xed, 0xed));
		GridLayout gl = new GridLayout(X_COUNT, true);
		gl.horizontalSpacing = 1;
		gl.verticalSpacing = 1;
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		composite.setLayout(gl);
		
		

		// 显示我负责的项目
	}

	private void setInput() {
		List<Project> projectList = new ArrayList<Project>();

		DBCursor projectCursor = projectCol.find(new BasicDBObject().append(
				Project.F_CHARGER, usetId).append(Project.F_LIFECYCLE,
				ILifecycle.STATUS_WIP_VALUE));
		while (projectCursor.hasNext()) {
			DBObject projectData = projectCursor.next();
			Project project = ModelService.createModelObject(projectData,
					Project.class);
			projectList.add(project);
		}
		tv.setInput(projectList);
	}

	private TableViewer createTable(Composite parent) {
		TableViewer tv = new TableViewer(parent, SWT.NONE);

		Control table = tv.getControl();
		table.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		table.setData(RWT.CUSTOM_ITEM_HEIGHT, (TOPICSIZE + 1));
		table.setData(MarkupValidator.MARKUP_VALIDATION_DISABLED, Boolean.TRUE);
		tv.setContentProvider(ArrayContentProvider.getInstance());

		TableViewerColumn col = new TableViewerColumn(tv, SWT.LEFT);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Project) {
					Project project = (Project) element;
					CommonHTMLLabel projectHtmlLabel = project
							.getAdapter(CommonHTMLLabel.class);
					return projectHtmlLabel.getHTML();
				}
				return ""; //$NON-NLS-1$
			}

		});
		col.getColumn().setWidth(240);
		return tv;
	}

	@Override
	public void doRefresh() {
		setInput();
	}
}
