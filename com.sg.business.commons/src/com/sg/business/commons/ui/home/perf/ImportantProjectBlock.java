package com.sg.business.commons.ui.home.perf;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.commons.ui.home.basic.ProjectContentBlock;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.block.button.BusinessContentBlock;
import com.sg.widgets.block.button.ButtonBlock;
import com.sg.widgets.commons.model.IEditorInputFactory;
import com.sg.widgets.part.editor.DataObjectWizard;

public class ImportantProjectBlock extends ButtonBlock {

	private static final String PERSPECTIVE = "perspective.project.charger";

	public ImportantProjectBlock(Composite parent) {
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
	protected DBObject getSearchCondition() {
		return new BasicDBObject().append(
				Project.F_LIFECYCLE,
				new BasicDBObject().append("$in", new String[] {
						ILifecycle.STATUS_WIP_VALUE,
						ILifecycle.STATUS_NONE_VALUE,
						ILifecycle.STATUS_ONREADY_VALUE, null }));
	}

	@Override
	protected BusinessContentBlock createBlockContent(Composite contentArea,
			PrimaryObject po) {
		return new ProjectContentBlock(contentArea);
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

	@Override
	public int getUnitCountX() {
		return 6;
	}

	@Override
	protected int getLimit() {
		return 2;
	}

	@Override
	protected void fill(DBObject dt[]) {
		int blankCount = getUnitCountX()*getUnitCountY()-1;
		switch (dt.length) {
		case 11:
			blankCount -= insert(dt,0,11,BLOCK_TYPE[0]);
			break;
		case 10:
			blankCount -= insert(dt,0,1,BLOCK_TYPE[1]);
			blankCount -= insert(dt,1,10,BLOCK_TYPE[0]);
			break;
		case 9:
			blankCount -= insert(dt,0,2,BLOCK_TYPE[1]);
			blankCount -= insert(dt,2,9,BLOCK_TYPE[0]);
			break;
		case 8:
			blankCount -= insert(dt,0,1,BLOCK_TYPE[2]);
			blankCount -= insert(dt,1,8,BLOCK_TYPE[0]);
			break;
		case 7:
			blankCount -= insert(dt,0,1,BLOCK_TYPE[2]);
			blankCount -= insert(dt,1,2,BLOCK_TYPE[1]);
			blankCount -= insert(dt,2,7,BLOCK_TYPE[0]);
			break;
		case 6:
			blankCount -= insert(dt,0,1,BLOCK_TYPE[2]);
			blankCount -= insert(dt,1,3,BLOCK_TYPE[1]);
			blankCount -= insert(dt,3,6,BLOCK_TYPE[0]);
			break;
		case 5:
			blankCount -= insert(dt,0,2,BLOCK_TYPE[2]);
			blankCount -= insert(dt,2,5,BLOCK_TYPE[0]);
			break;
		case 4:
			blankCount -= insert(dt,0,2,BLOCK_TYPE[2]);
			blankCount -= insert(dt,2,3,BLOCK_TYPE[1]);
			blankCount -= insert(dt,3,4,BLOCK_TYPE[0]);
			break;
		case 3:
			blankCount -= insert(dt,0,2,BLOCK_TYPE[2]);
			blankCount -= insert(dt,2,3,BLOCK_TYPE[1]);
			break;
		case 2:
		case 1:
			blankCount -= insert(dt,0,dt.length,BLOCK_TYPE[2]);
			break;
		default:
		}
		fillBlank(blankCount);
	}

	private int insert(DBObject[] dt, int start, int end, Point size) {
		int result = 0;
		for (int i = start; i < end; i++) {
			addContentBlock(dt[i], size);
			result+=size.x*size.y;
		}
		return result;
	}

	private void fillBlank(int blankCount) {
		if(blankCount>0){
			for(int i=0;i<blankCount;i++){
				addBlankBlock();
			}
		}
	}

}
