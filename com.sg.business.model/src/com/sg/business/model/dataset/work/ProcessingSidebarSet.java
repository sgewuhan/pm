package com.sg.business.model.dataset.work;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
import com.sg.business.model.WorkflowSynchronizer;
import com.sg.widgets.part.CurrentAccountContext;

public class ProcessingSidebarSet extends DataSetFactory {

	private String userId;
	private DBCollection workCol;
	private DBCollection projectCol;
	private DBCollection userTaskCol;

	public ProcessingSidebarSet() {
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
		WorkflowSynchronizer synchronizer = new WorkflowSynchronizer();
		synchronizer.synchronizeUserTask(userId);
		
		// 前提：（项目必须是进行中）
		// 并且（本人负责，参与或者是指派者或者是流程任务的执行人）
		// 并且（工作必须是准备中或进行中）

		// 按照项目工作和计划完成时间排序

		// 查询正在进行的项目
		List<?> projectIdList = projectCol.distinct(Project.F__ID,
				new BasicDBObject().append(Project.F_LIFECYCLE,
						ILifecycle.STATUS_WIP_VALUE));
		projectIdList.add(null);

		// 查询流程工作
		DBObject q = new BasicDBObject();
		q.put(UserTask.F_USERID, userId);
		q.put(UserTask.F_LIFECYCLE_CHANGE_FLAG, Boolean.FALSE);
		q.put("$or", //$NON-NLS-1$
				new BasicDBObject[] {
						new BasicDBObject().append(UserTask.F_STATUS,
								Status.Reserved.name()),
						new BasicDBObject().append(UserTask.F_STATUS,
								Status.InProgress.name()) });
		List<?> pWorkId = userTaskCol.distinct(UserTask.F_WORK_ID, q);

		// 查询本人参与的工作
		//2014-04-18 按舒传玉提出的要求将原有的显示本人参与的工作改为只显示本人负责的工作
		q = new BasicDBObject();
		q.put("$or", //$NON-NLS-1$
				new BasicDBObject[] {
						new BasicDBObject().append(Work.F_CHARGER, userId),
						new BasicDBObject().append(Work.F_ASSIGNER, userId),
						new BasicDBObject().append(Work.F__ID,
								new BasicDBObject().append("$in", pWorkId)) });

		// 生命周期状态为准备、进行中
		q.put(Work.F_LIFECYCLE, new BasicDBObject().append("$in", new String[] { //$NON-NLS-1$
				Work.STATUS_ONREADY_VALUE, Work.STATUS_WIP_VALUE }));
		q.put(Work.F_PROJECT_ID,
				new BasicDBObject().append("$in", projectIdList.toArray()));

		DBCursor cur = workCol.find(q);
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		while (cur.hasNext()) {
			DBObject dbo = cur.next();
			Work work = ModelService.createModelObject(dbo, Work.class);
			append(result, work, pWorkId);
		}

		sortResult(result);
		return result;
	}

	private void sortResult(List<PrimaryObject> result) {
		Comparator<PrimaryObject> comparator = new Comparator<PrimaryObject>() {

			@Override
			public int compare(PrimaryObject p0, PrimaryObject p1) {
				Work work0 = (Work) p0;
				Work work1 = (Work) p1;
				if (work0.isProjectWBSRoot() && !work1.isProjectWBSRoot()) {
					return -1;
				} else if (!work0.isProjectWBSRoot()
						&& work1.isProjectWBSRoot()) {
					return 1;
				} else {
					Date ps0 = work0.getPlanFinish();
					Date ps1 = work1.getPlanFinish();
					if (ps0 == null) {
						Calendar cal = Calendar.getInstance();
						cal.set(Calendar.YEAR, 3000);
						ps0 = cal.getTime();
					}
					if (ps1 == null) {
						Calendar cal = Calendar.getInstance();
						cal.set(Calendar.YEAR, 3000);
						ps1 = cal.getTime();
					}
					return ps0.compareTo(ps1);
				}
			}

		};
		// 按照计划完成时间排序
		Collections.sort(result, comparator);
	}

	private void append(List<PrimaryObject> result, Work work, List<?> pWorkId) {
		// 进行中的工作,排除带有流程但流程执行人不是当前用户的
		if (ILifecycle.STATUS_WIP_VALUE.equals(work.getLifecycleStatus())) {
			if (!work.isExecuteWorkflowActivateAndAvailable()
					|| pWorkId.contains(work.get_id())) {
				addWork(result, work);
			}
			return;
		}

		// 准备中的工作，如果是需要我指派的显示，上级是准备中的不显示
		if (ILifecycle.STATUS_ONREADY_VALUE.equals(work.getLifecycleStatus())) {
			String aid = work.getAssignerId();
			if (aid != null) {
				if (!userId.equals(aid)) {
					Work parent = (Work) work.getParent();
					if (parent != null
							&& ILifecycle.STATUS_ONREADY_VALUE.equals(parent
									.getLifecycleStatus())) {
						return;
					}
				}
			}
		}
		addWork(result, work);

	}

	private void addWork(List<PrimaryObject> result, Work work) {
		Work rootWork = (Work) work.getRoot();
		if (rootWork != null) {
			if (result.size() == 0) {
				result.add(rootWork);
			} else if (result.indexOf(rootWork) < 0) {
				result.add(rootWork);
			}
		}
	}

	@Override
	public long getTotalCount() {
		return 100;
	}

}
