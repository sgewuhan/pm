package com.sg.business.finance.editor;

import java.util.List;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.sg.business.model.BudgetItem;
import com.sg.widgets.viewer.ViewerControl;

public class DropBudgetItemTarget implements DropTargetListener {

	private TreeViewer viewer;
	private BudgetItemEditor editor;


	public DropBudgetItemTarget(BudgetItemEditor editor,
			TreeViewer viewer) {
		this.viewer = viewer;
		this.editor = editor;
	}

	@Override
	public void dragEnter(DropTargetEvent event) {
	}

	@Override
	public void dragLeave(DropTargetEvent event) {

	}

	@Override
	public void dragOperationChanged(DropTargetEvent event) {

	}

	@Override
	public void dragOver(DropTargetEvent event) {
		event.feedback = DND.FEEDBACK_EXPAND | DND.FEEDBACK_SELECT
				| DND.FEEDBACK_INSERT_BEFORE;
	}

	@Override
	public void drop(DropTargetEvent event) {
		if (Utils.isNullOrEmpty((String) event.data)||event.item==null) {
			return;
		}
		BasicDBList data = (BasicDBList) JSON.parse((String) event.data);
		
		PrimaryObject target = (PrimaryObject) event.item.getData();
		for (Object dbo : data) {
			doMove((DBObject) dbo, target);
		}
		
		editor.setDirty(true);
	}

	private void doMove(DBObject dbo, PrimaryObject target) {
		BudgetItem srcPo = getSourcePrimaryObject(dbo, null);
		BudgetItem srcParent = srcPo.getParent();
		if(srcParent==null){
			return ;
		}

		BudgetItem targetParent = ((BudgetItem) target).getParent();
		// 不能移动至顶级菜单之上
		if (targetParent == null)
			return;
        //目标节点不能为源节点的子节点
		BudgetItem[] srcChildren = srcPo.getChildren();
		for (int i = 0; i < srcChildren.length; i++) {
			if (srcChildren[i].equals(target)) {
				return;
			}
		}

		srcParent.removeChild(srcPo);

		BudgetItem[] targetChildern = targetParent.getChildren();
		int index = 0;
		for (int i = 0; i < targetChildern.length; i++) {
			if (targetChildern[i].equals(target)) {
				index = i;
			}
		}
		targetParent.createChild(srcPo, index);

		viewer.refresh(srcParent);
		viewer.refresh(targetParent);
		
		
	}

	protected void doDrop(String sourceId, List<PrimaryObject> dragsItems,
			DropTargetEvent event, ViewerControl targetViewerControl) {

	}

	private BudgetItem getSourcePrimaryObject(DBObject dbo, Object[] obj) {

		BudgetItem sourcePrimaryObject = null;
		if (obj == null) {
			obj = (Object[]) viewer.getInput();
		}

		for (int i = 0; i < obj.length; i++) {
			if (obj[i] instanceof BudgetItem) {

				if (((BudgetItem) obj[i]).get_id().equals(
						dbo.get(PrimaryObject.F__ID))) {
					sourcePrimaryObject = (BudgetItem) obj[i];
				} else {
					sourcePrimaryObject = getSourcePrimaryObject(dbo,
							((BudgetItem) obj[i]).getChildren());
				}
			}

			if (sourcePrimaryObject != null) {
				return sourcePrimaryObject;
			}
		}
		return sourcePrimaryObject;

	}

	@Override
	public void dropAccept(DropTargetEvent event) {

	}

}
