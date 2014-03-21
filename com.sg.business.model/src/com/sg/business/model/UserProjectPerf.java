package com.sg.business.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.IPrimaryObjectEventListener;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.etl.ProjectPresentation;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;

public class UserProjectPerf extends ProjectProvider {

	public static final String F_PROJECT_ID = "project_id"; //$NON-NLS-1$

	public static final String F_USERID = "userid"; //$NON-NLS-1$

	public static final String EDITOR_SETTING = "editor.visualization.addprojectset"; //$NON-NLS-1$

	public List<PrimaryObject> getProjectSet() {
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		try {

			Date startDate = getStartDate();
			Date endDate = getEndDate();
			DBCollection projectCol = getCollection(IModelConstants.C_PROJECT);
			DBCursor cur = projectCol
					.find(getQueryCondition(startDate, endDate));
			while (cur.hasNext()) {
				DBObject dbo = cur.next();
				Project project = ModelService.createModelObject(dbo,
						Project.class);
				ProjectPresentation pres = project.getPresentation();
				if(!pres.isPresentationAvailable()){
					continue;
				}
				pres.loadSummary(sum);
				result.add(project);
			}
			sum.total = result.size();

		} catch (Exception e) {
			MessageUtil.showToast(e);
		}

		return result;
	}

	public BasicDBObject getQueryCondition(Date start, Date stop) {
		List<ObjectId> projectidlist = getAllProjectId();
		BasicDBObject dbo = new BasicDBObject();
		dbo.put(F__ID, new BasicDBObject().append("$in", projectidlist)); //$NON-NLS-1$
		if (start != null && stop != null) {
			dbo.put("$or", //$NON-NLS-1$
					new BasicDBObject[] {

							new BasicDBObject().append(Project.F_ACTUAL_START,
									new BasicDBObject().append("$gte", start) //$NON-NLS-1$
											.append("$lte", stop)), //$NON-NLS-1$

							new BasicDBObject().append(Project.F_PLAN_FINISH,
									new BasicDBObject().append("$gte", start) //$NON-NLS-1$
											.append("$lte", stop)), //$NON-NLS-1$

							new BasicDBObject().append(Project.F_ACTUAL_FINISH,
									new BasicDBObject().append("$gte", start) //$NON-NLS-1$
											.append("$lte", stop)), //$NON-NLS-1$

							new BasicDBObject().append(
									"$and", //$NON-NLS-1$
									new BasicDBObject[] {
											new BasicDBObject().append(
													Project.F_ACTUAL_START,
													new BasicDBObject().append(
															"$lte", start)), //$NON-NLS-1$
											new BasicDBObject().append(
													Project.F_ACTUAL_FINISH,
													new BasicDBObject().append(
															"$gte", stop)) }) }); //$NON-NLS-1$

		}
		return dbo;
	}

	/**
	 * ��ȡ����Ŀ��ϵ���Ŀ
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ObjectId> getAllProjectId() {
		return  (List<ObjectId>) getValue(F_PROJECT_ID);
	}

	@Override
	public String getProjectSetName() {
		return getDesc();
	}

	@Override
	public String getTypeName() {
		return Messages.get().UserProjectPerf_0;
	}

	@Override
	public String getProjectSetCoverImage() {
		return null;
	}

	@Override
	public boolean doSave(IContext context) throws Exception {
		if (!isPersistent()) {
			doInsert(context);
			fireEvent(IPrimaryObjectEventListener.INSERTED);
		} else {
			doUpdate(context);
			fireEvent(IPrimaryObjectEventListener.UPDATED);
		}
		return true;
	}

	@Override
	public void doInsert(IContext context) throws Exception {
		DBObject data = get_data();
		ObjectId oid = get_id();
		if (oid == null) {
			oid = new ObjectId();
		}
		data.put(F__ID, oid);
		DBCollection col = getCollection();
		col.insert(data);
	}

	@Override
	public List<ObjectId> getSalesAllProjectId() {
		return getAllProjectId();
	}

}
