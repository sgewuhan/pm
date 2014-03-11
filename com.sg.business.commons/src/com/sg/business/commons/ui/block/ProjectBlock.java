package com.sg.business.commons.ui.block;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.block.AbstractBusinessBlock;
import com.sg.widgets.block.ContentBlock;
import com.sg.widgets.commons.model.IEditorInputFactory;
import com.sg.widgets.part.editor.DataObjectWizard;

public class ProjectBlock extends AbstractBusinessBlock {

	private static final String PERSPECTIVE = "perspective.project.charger";

	public ProjectBlock(Composite parent) {
		super(parent);
	}

	@Override
	protected String getPerspective() {
		return PERSPECTIVE;
	}

	@Override
	protected DBCollection getCollection() {
		return DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT);
	}

	@Override
	protected Class<? extends PrimaryObject> getContentClass() {
		return Project.class;
	}

	@Override
	protected BasicDBObject getSearchCondition() {
		return new BasicDBObject().append(
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
						ILifecycle.STATUS_ONREADY_VALUE, null }));
	}

	@Override
	protected ContentBlock createBlockContent(Composite contentArea,
			PrimaryObject po) {
		ProjectContentBlock cb = new ProjectContentBlock(contentArea);
		cb.setBlockSize(BLOCKSIZE);
		if (po != null) {
			cb.setProject((Project) po);
		}
		return cb;
	}

	@Override
	protected void doAdd() {
		Project po = ModelService.createModelObject(new BasicDBObject(),
				Project.class);
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

}
