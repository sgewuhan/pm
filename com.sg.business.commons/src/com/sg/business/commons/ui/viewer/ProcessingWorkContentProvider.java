package com.sg.business.commons.ui.viewer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jbpm.task.Status;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.UserTask;
import com.sg.business.model.Work;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.registry.config.TreeConfigurator;
import com.sg.widgets.viewer.RelationContentProvider;

public class ProcessingWorkContentProvider extends RelationContentProvider {

	private String userId;
	private DBCollection workCol;
	private DBCollection userTaskCol;

	public ProcessingWorkContentProvider(TreeConfigurator configurator) {
		super(configurator);
		userId = new CurrentAccountContext().getAccountInfo().getConsignerId();
		workCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_WORK);
		userTaskCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_USERTASK);
	}

	public ProcessingWorkContentProvider() {
		userId = new CurrentAccountContext().getAccountInfo().getConsignerId();
		workCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_WORK);
		userTaskCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_USERTASK);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Work) {
			Work parentWork = (Work) parentElement;
			// ��ѯ���̹���
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

			// ��ѯ���˲���Ĺ���
			//2014-04-18 ���洫�������Ҫ��ԭ�е���ʾ���˲���Ĺ�����Ϊֻ��ʾ���˸���Ĺ���
			q = new BasicDBObject();
			q.put("$or", //$NON-NLS-1$
					new BasicDBObject[] {
							new BasicDBObject().append(Work.F_CHARGER,
									userId),
							new BasicDBObject().append(Work.F_ASSIGNER, userId),
							new BasicDBObject().append(Work.F__ID,
									new BasicDBObject().append("$in", pWorkId)) });

			// ��������״̬Ϊ׼����������
			q.put(Work.F_LIFECYCLE,
					new BasicDBObject().append("$in", new String[] { //$NON-NLS-1$
							Work.STATUS_ONREADY_VALUE, Work.STATUS_WIP_VALUE }));
			q.put(Work.F_ROOT_ID, parentWork.getRoot().get_id());

			DBCursor cur = workCol.find(q);
			List<PrimaryObject> result = new ArrayList<PrimaryObject>();
			while (cur.hasNext()) {
				DBObject dbo = cur.next();
				Work work = ModelService.createModelObject(dbo, Work.class);
				append(result, work, pWorkId, parentWork);
			}

			sortResult(result);
			return result.toArray(new Object[0]);
		} else {
			return new Object[0];
		}
	}

	private void sortResult(List<PrimaryObject> result) {

		Comparator<PrimaryObject> comparator = new Comparator<PrimaryObject>() {

			@Override
			public int compare(PrimaryObject p0, PrimaryObject p1) {
				Work work0 = (Work) p0;
				Work work1 = (Work) p1;
				int ps0 = work0.getSequance();
				int ps1 = work1.getSequance();
				if (ps0 > ps1) {
					return 1;
				} else if (ps0 < ps1) {
					return -1;
				} else {
					return 0;
				}
			}

		};
		// ���ռƻ����ʱ������
		Collections.sort(result, comparator);
	}

	private void append(List<PrimaryObject> result, Work work, List<?> pWorkId,
			Work parentWork) {
		// �����еĹ���,�ų��������̵�����ִ���˲��ǵ�ǰ�û���
		if (ILifecycle.STATUS_WIP_VALUE.equals(work.getLifecycleStatus())) {
			if (!work.isExecuteWorkflowActivateAndAvailable()
					|| pWorkId.contains(work.get_id())) {
				addChildren(result, work, parentWork);
			}
			return;
		}
		
		addChildren(result, work, parentWork);

	}

	private void addChildren(List<PrimaryObject> result, Work work,
			Work parentWork) {
		Work abstractWork = (Work) work.getParent();
		if (abstractWork != null) {
			if (parentWork.equals(abstractWork)) {
				if (result.size() == 0) {
					result.add(work);
				} else if (result.indexOf(work) < 0) {
					result.add(work);
				}
			} else {
				addChildren(result, abstractWork, parentWork);
			}
		}
	}

	@Override
	public boolean hasChildren(Object parentElement) {
		return getChildren(parentElement).length > 0;
	}

	@Override
	public Object getParent(Object element) {

		if (element instanceof PrimaryObject) {
			return ((PrimaryObject) element).getParentPrimaryObjectCache();
		}
		return null;
	}
}
