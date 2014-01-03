package com.sg.business.project.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.types.ObjectId;
import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.model.Folder;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.ProductItem;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectBudget;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.commons.selector.NavigatorSelector;
import com.sg.widgets.viewer.ViewerControl;

public class CombinProject extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters,
			final IStructuredSelection selection) {
		if (selection == null || selection.isEmpty()) {
			return;
		}
		// 选择合并的目标
		NavigatorSelector ns = new NavigatorSelector(
				"project.navigator.allproject", "Please Select Target Project") {
			@Override
			protected void doOK(IStructuredSelection is) {
				Project project = (Project) is.getFirstElement();
				doCombin(project, selection.toArray());
				super.doOK(is);
			}
		};
		ns.show();

	}

	protected void doCombin(Project project, Object[] projects) {

		ObjectId[] tobeRemove = new ObjectId[projects.length];
		// 工作令号
		Set<String> set = new HashSet<String>();
		String[] ws = project.getWorkOrders();
		set.addAll(Arrays.asList(ws));
		for (int i = 0; i < projects.length; i++) {
			ws = ((Project) projects[i]).getWorkOrders();
			set.addAll(Arrays.asList(ws));
		}
		String[] wonsArray = set.toArray(new String[0]);

		// 物资编码
		set = new HashSet<String>();
		ws = project.getProductCode2();
		set.addAll(Arrays.asList(ws));
		for (int i = 0; i < projects.length; i++) {
			ws = ((Project) projects[i]).getProductCode2();
			set.addAll(Arrays.asList(ws));
		}
		String[] pnArray = set.toArray(new String[0]);
		
		// 更新工作令号
		DBCollection projectCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT);
		projectCol.update(new BasicDBObject().append(Project.F__ID,
				project.get_id()), new BasicDBObject().append("$set",
				new BasicDBObject().append(Project.F_WORK_ORDER, wonsArray)));

		// 更新项目的物资编码
		DBCollection productCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PRODUCT);
		productCol.remove(new BasicDBObject().append(ProductItem.F_PROJECT_ID,
				project.get_id()));
		List<DBObject> tobeInsert = new ArrayList<DBObject>();
		for (int i = 0; i < pnArray.length; i++) {
			BasicDBObject dbo = new BasicDBObject();
			dbo.put(ProductItem.F_DESC, pnArray[i]);
			dbo.put(ProductItem.F_PROJECT_ID, project.get_id());
			tobeInsert.add(dbo);
		}
		productCol.insert(tobeInsert);

		// 删除项目
		projectCol.remove(new BasicDBObject().append(Project.F__ID,
				new BasicDBObject().append("$in", tobeRemove)));
		// 删除物资编码项目对应
		productCol.remove(new BasicDBObject().append(ProductItem.F_PROJECT_ID,
				new BasicDBObject().append("$in", tobeRemove)));
		// 删除工作
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_WORK);
		col.remove(new BasicDBObject().append(Work.F_PROJECT_ID,
				new BasicDBObject().append("$in", tobeRemove)));

		// 删除预算
		col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT_BUDGET);
		col.remove(new BasicDBObject().append(ProjectBudget.F_PROJECT_ID,
				new BasicDBObject().append("$in", tobeRemove)));

		//删除文件夹
		col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_FOLDER);
		col.remove(new BasicDBObject().append(Folder.F_PROJECT_ID,
				new BasicDBObject().append("$in", tobeRemove)));

		// 删除月ETL数据
		col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT_MONTH_DATA);
		col.remove(new BasicDBObject().append("project_id",
				new BasicDBObject().append("$in", tobeRemove)));

		MessageUtil.showToast("处理已完成", SWT.ICON_INFORMATION);
	}
}
