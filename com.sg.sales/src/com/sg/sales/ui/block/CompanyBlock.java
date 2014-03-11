package com.sg.sales.ui.block;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.resource.BusinessResource;
import com.sg.sales.Sales;
import com.sg.sales.model.Company;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.block.Block;
import com.sg.widgets.block.ButtonContentBlock;
import com.sg.widgets.commons.model.IEditorInputFactory;
import com.sg.widgets.part.editor.DataObjectWizard;

public class CompanyBlock extends Block {

	private static final String PERSPECTIVE = "sales.customer";
	private String userId;
	private DBCollection col;
	private Composite contentArea;

	public static final int X_COUNT = 3;
	public static final int Y_COUNT = 2;
	public static final int BLOCKSIZE = 100;

	public CompanyBlock(Composite parent) {
		super(parent);
	}

	@Override
	protected void go() {
		try {
			IWorkbench workbench = PlatformUI.getWorkbench();
			IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
			workbench.showPerspective(PERSPECTIVE, window);
		} catch (WorkbenchException e) {
			MessageUtil.showToast(e);
		}
	}

	@Override
	protected void createContent(Composite parent) {
		col = DBActivator.getCollection(IModelConstants.DB,
				Sales.C_COMPANY);
		userId = context.getConsignerId();
		parent.setLayout(new FillLayout());
		contentArea = new Composite(parent, SWT.NONE);
		contentArea.setBackground(Widgets.getColor(contentArea.getDisplay(),
				0xed, 0xed, 0xed));
		GridLayout gl = new GridLayout(X_COUNT, true);
		gl.horizontalSpacing = 1;
		gl.verticalSpacing = 1;
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		contentArea.setLayout(gl);

		setInput();
	}

	private void setInput() {
		Control[] children = contentArea.getChildren();
		for (int i = 0; i < children.length; i++) {
			children[i].dispose();
		}

		DBCursor cursor = col.find(new BasicDBObject().append(
				"$or", //$NON-NLS-1$
				new BasicDBObject[] {
						new BasicDBObject().append(Project.F_CHARGER, userId),
						new BasicDBObject().append(Project.F_PARTICIPATE,
								userId),
						new BasicDBObject().append(Project.F_BUSINESS_CHARGER,
								userId) }).append(
				Project.F_LIFECYCLE,
				new BasicDBObject().append("$in", new String[] {
						ILifecycle.STATUS_WIP_VALUE,
						ILifecycle.STATUS_NONE_VALUE,
						ILifecycle.STATUS_ONREADY_VALUE, null })));
		cursor.sort(new BasicDBObject().append(Project.F__ID, -1));
		cursor.limit(X_COUNT * Y_COUNT - 1);

		for (int i = 0; i < X_COUNT * Y_COUNT - 1; i++) {
			Control block;
			if (cursor.hasNext()) {
				DBObject projectData = cursor.next();
				Company project = ModelService.createModelObject(projectData,
						Company.class);
				block = createContentBlock(project);
			} else {
				block = new Composite(contentArea, SWT.NONE);
			}
			GridData gd = new GridData(SWT.FILL, SWT.FILL, false, false);
			gd.widthHint = BLOCKSIZE;
			gd.heightHint = BLOCKSIZE;
			block.setLayoutData(gd);
		}
		// 创建“更多”块
		ButtonContentBlock block = new ButtonContentBlock(contentArea) {
			@Override
			protected void mouseClick() {
				Project po = ModelService.createModelObject(
						new BasicDBObject(), Project.class);
				IEditorInputFactory inputFactory = po
						.getAdapter(IEditorInputFactory.class);
				// 默认的项目经理是当前的用户
				po.setValue(Project.F_CHARGER, userId);
				try {
					DataObjectWizard dw = DataObjectWizard.open(po,
							inputFactory.getEditorConfig("create"), true, null);
					if (dw.getResult() == Window.OK) {
						doRefresh();
					}
				} catch (Exception e) {
					MessageUtil.showToast(e);
				}
			}
		};
		block.setBlockSize(BLOCKSIZE);
		Image image = BusinessResource
				.getImage(BusinessResource.IMAGE_ADD_W_48);
		block.setCoverImage(image);

		GridData gd = new GridData(SWT.FILL, SWT.FILL, false, false);
		gd.widthHint = BLOCKSIZE;
		gd.heightHint = BLOCKSIZE;
		block.setLayoutData(gd);

		contentArea.layout();
	}

	private CompanyContentBlock createContentBlock(Company company) {
		if (company == null) {
			CompanyContentBlock cb = new CompanyContentBlock(contentArea);
			cb.setBlockSize(BLOCKSIZE);
			return cb;
		} else {
			CompanyContentBlock cb = new CompanyContentBlock(contentArea);
			cb.setBlockSize(BLOCKSIZE);
			cb.setCompany(company);
			return cb;
		}
	}

	@Override
	public void doRefresh() {
		setInput();
	}

}
