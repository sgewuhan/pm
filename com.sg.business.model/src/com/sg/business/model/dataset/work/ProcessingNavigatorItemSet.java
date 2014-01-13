package com.sg.business.model.dataset.work;

import java.util.ArrayList;
import java.util.List;

import org.jbpm.task.Status;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.DataSetFactory;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.UserTask;
import com.sg.business.model.Work;
import com.sg.widgets.part.CurrentAccountContext;

public class ProcessingNavigatorItemSet extends DataSetFactory {

	private String userId;
	private DBCollection workCol;
	private DBCollection projectCol;
	private DBCollection userTaskCol;

	public ProcessingNavigatorItemSet() {
		userId = new CurrentAccountContext().getAccountInfo().getConsignerId();
		workCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_WORK);
		projectCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT);
		userTaskCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_USERTASK);
	}

	@Override
	public List<PrimaryObject> doQuery(DataSet ds) throws Exception {
		// 查询正在进行的项目
		List<?> projectIdList = projectCol.distinct(Project.F__ID,
				new BasicDBObject().append(Project.F_LIFECYCLE,
						ILifecycle.STATUS_WIP_VALUE));
		projectIdList.add(null);
		

		// 查询本人参与的工作
		DBObject queryCondition = new BasicDBObject();
		queryCondition.put(
				"$or", //$NON-NLS-1$
				new BasicDBObject[] {
						new BasicDBObject().append(Work.F_PARTICIPATE, userId),
						new BasicDBObject().append(Work.F_ASSIGNER, userId) });
		// 生命周期状态为准备、进行中
		queryCondition.put(Work.F_LIFECYCLE,
				new BasicDBObject().append("$in", new String[] { //$NON-NLS-1$
						Work.STATUS_ONREADY_VALUE, Work.STATUS_WIP_VALUE }));
		queryCondition.put(Work.F_PROJECT_ID,
				new BasicDBObject().append("$in", projectIdList.toArray()));

		DBCursor cur = workCol.find(queryCondition);
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		while (cur.hasNext()) {
			DBObject dbo = cur.next();
			Work work = ModelService.createModelObject(dbo, Work.class);
			if (hasParentWork(result, work)) {
				continue;
			} else {
				removeChildWork(result,work);
				result.add(work);
			}
		}
		cur.close();
		
		//查询流程工作
		queryCondition = new BasicDBObject();
		queryCondition.put(UserTask.F_USERID, userId);
		queryCondition.put(UserTask.F_LIFECYCLE_CHANGE_FLAG, Boolean.FALSE);
		queryCondition.put(
				"$or", //$NON-NLS-1$
				new BasicDBObject[] {
						new BasicDBObject().append(UserTask.F_STATUS,
								Status.Reserved.name()),
						new BasicDBObject().append(UserTask.F_STATUS,
								Status.InProgress.name()) });
		List<?> workIdList = userTaskCol.distinct(UserTask.F_WORK_ID, queryCondition);
		
		queryCondition = new BasicDBObject();
		queryCondition.put(Work.F__ID, new BasicDBObject().append("$in", workIdList));
		cur = workCol.find(queryCondition);
		while (cur.hasNext()) {
			DBObject dbo = cur.next();
			Work work = ModelService.createModelObject(dbo, Work.class);
			if (hasParentWork(result, work)) {
				continue;
			} else {
				removeChildWork(result,work);
				result.add(work);
			}
		}
		cur.close();
		
		return result;
	}

	private void removeChildWork(List<PrimaryObject> result, Work work) {
		for (int i = 0; i < result.size(); i++) {
			if(result.get(i).get_id().equals(work.get_id())){
				result.remove(i);
				break;
			}
		}
		List<PrimaryObject> childrenWorks = work.getChildrenWork();
		for (int i = 0; i < childrenWorks.size(); i++) {
			removeChildWork(result,(Work)childrenWorks.get(i));
		}
	}

	private boolean hasParentWork(List<PrimaryObject> result, Work work) {
		for (int i = 0; i < result.size(); i++) {
			Work item = (Work) result.get(i);
			if (item.get_id().equals(work.get_id())
					|| hasParentWork(item.getChildrenWork(), work)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public long getTotalCount() {
		return 100;
	}

}
