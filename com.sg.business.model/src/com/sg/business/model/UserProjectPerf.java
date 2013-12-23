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
import com.sg.widgets.MessageUtil;

public class UserProjectPerf extends ProjectProvider {

	public static final String F_PROJECT_ID = "project_id";

	public static final String F_USERID = "userid";

	public static final String EDITOR_SETTING = "editor.visualization.addprojectset";

	public List<PrimaryObject> getProjectSet() {
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		try {

			Date startDate = getStartDate();
			Date endDate = getEndDate();
			DBCollection projectCol = getCollection(IModelConstants.C_PROJECT);
			DBCursor cur = projectCol
					.find(getQueryCondtion(startDate, endDate));
			while (cur.hasNext()) {
				DBObject dbo = cur.next();
				Project project = ModelService.createModelObject(dbo,
						Project.class);
				ProjectPresentation pres = project.getPresentation();
				pres.loadSummary(sum);
				result.add(project);
			}
			sum.total = result.size();

		} catch (Exception e) {
			MessageUtil.showToast(e);
		}

		return result;
	}

	protected BasicDBObject getQueryCondtion(Date start, Date stop) {
		List<ObjectId> projectidlist = getAllProjectId();
		BasicDBObject dbo = new BasicDBObject();
		dbo.put(F__ID, new BasicDBObject().append("$in", projectidlist));
		if (start != null && stop != null) {
			dbo.put("$or",
					new BasicDBObject[] {

							new BasicDBObject().append(Project.F_ACTUAL_START,
									new BasicDBObject().append("$gte", start)
											.append("$lte", stop)),

							new BasicDBObject().append(Project.F_PLAN_FINISH,
									new BasicDBObject().append("$gte", start)
											.append("$lte", stop)),

							new BasicDBObject().append(Project.F_ACTUAL_FINISH,
									new BasicDBObject().append("$gte", start)
											.append("$lte", stop)),

							new BasicDBObject().append(
									"$and",
									new BasicDBObject[] {
											new BasicDBObject().append(
													Project.F_ACTUAL_START,
													new BasicDBObject().append(
															"$lte", start)),
											new BasicDBObject().append(
													Project.F_ACTUAL_FINISH,
													new BasicDBObject().append(
															"$gte", stop)) }) });

		}
		return dbo;
	}

	/**
	 * 获取本项目组合的项目
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
		return "自定义项目组合";
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

}
