package com.sg.business.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.etl.ProjectPresentation;
import com.sg.widgets.MessageUtil;

public class ProjectManagerProvider extends ProjectProvider {

	private User user;
	private DBCollection projectCol;

	public void setUser(User user) {
		this.user = user;
		setValue(F__ID, user.get_id());
		setValue(F_DESC, user.getUserid()+"/"+user.getUsername());
		projectCol = getCollection(IModelConstants.C_PROJECT);
	}
	
	@Override
	public boolean doSave(IContext context) throws Exception {
		return true;
	}

	@Override
	public void doUpdate(IContext context) throws Exception {
	}

	@Override
	public void doInsert(IContext context) throws Exception {
	}
	
	@Override
	public String getDesc() {
		return user.getUsername();
	}

	@Override
	public List<PrimaryObject> getProjectSet() {
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		try {

			Date startDate = getStartDate();
			Date endDate = getEndDate();
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
		return super.getQueryCondtion(start, stop).append(Project.F_CHARGER, user.getUserid());
	}

	@Override
	public String getProjectSetName() {
		return user.getUsername();
	}

	@Override
	public String getProjectSetCoverImage() {
		return null;
	}

}
