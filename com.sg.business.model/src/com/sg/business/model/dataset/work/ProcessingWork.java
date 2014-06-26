package com.sg.business.model.dataset.work;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.CurrentAccountContext;

public class ProcessingWork extends SingleDBCollectionDataSetFactory {

	private String userId;

	public ProcessingWork() {
		super(IModelConstants.DB, IModelConstants.C_WORK);
		userId = new CurrentAccountContext().getAccountInfo().getConsignerId();

	}

	public boolean isStandloneWork(DBObject dbo) {
		Object type = dbo.get(Work.F_WORK_TYPE);
		return type instanceof Integer
				&& ((Integer) type).intValue() == Work.WORK_TYPE_STANDLONE;
	}

	public boolean isProjectWork(DBObject dbo) {
		return !isStandloneWork(dbo);
	}

	@Override
	public DataSet getDataSet() {
		DBCollection col = getCollection();
		DBCursor cur = col.find(
				getQueryCondition(),
				new BasicDBObject().append(Work.F_ROOT_ID, 1)
						.append(Work.F_PROJECT_ID, 1)
						.append(Work.F_WORK_TYPE, 1));
		List<PrimaryObject> ret = new ArrayList<PrimaryObject>();
		cur.sort(getSort());
		while (cur.hasNext()) {
			DBObject dbo = cur.next();
			if (isProjectWork(dbo)) {
				// 需要排除的项目
				Object projectId = dbo.get(Work.F_PROJECT_ID);
				if (projectId instanceof ObjectId) {
					// 是项目工作
					Project project = ModelService.createModelObject(
							Project.class, (ObjectId) projectId);
					String lc = project.getLifecycleStatus();
					if (!ILifecycle.STATUS_WIP_VALUE.equals(lc)) {
						continue;
					}
				}
			}

			ObjectId rootId = (ObjectId) dbo.get(Work.F_ROOT_ID);
			if (rootId == null) {
				rootId = (ObjectId) dbo.get(Work.F__ID);
			}
			Work work = ModelService.createModelObject(Work.class, rootId);
			if (!ret.contains(work)) {
				ret.add(work);
			}
		}

		return new DataSet(ret);

	}

	/**
	 * 获取当前账号负责的工作和参与的工作
	 * 
	 * @return 返回当前账号负责的工作和参与的工作， 为{@link com.mongodb.DBObject}类型的数据
	 */
	@Override
	public DBObject getQueryCondition() {
		// 获得当前帐号
		try {
			// 查询本人参与的工作
			DBObject queryCondition = createQueryCondition();
			queryCondition
					.put("$or", //$NON-NLS-1$
							new BasicDBObject[] {
									new BasicDBObject().append(
											Work.F_PARTICIPATE, userId),
									new BasicDBObject().append(Work.F_ASSIGNER,
											userId) });
			// 生命周期状态为准备、进行中
			queryCondition
					.put(Work.F_LIFECYCLE,
							new BasicDBObject().append("$in", new String[] { //$NON-NLS-1$
									Work.STATUS_ONREADY_VALUE,
									Work.STATUS_WIP_VALUE }));
			return queryCondition;

		} catch (Exception e) {
			MessageUtil.showToast(e);
			return new BasicDBObject().append(Work.F__ID, null);
		}
	}

	// @Override
	// public List<PrimaryObject> doQuery(DataSet ds) throws Exception {
	// DBCollection c = getCollection();
	// Assert.isNotNull(c, "无法获取集合");
	//
	// DBObject query = getQueryCondition();
	// DBObject projection = getProjection();
	// DBCursor cursor = c.find(query, projection);
	//
	//
	// List<PrimaryObject> dataItems = new ArrayList<PrimaryObject>();
	// Class<? extends PrimaryObject> clas;
	//
	// Iterator<DBObject> iter = cursor.iterator();
	//
	// while (iter.hasNext()) {
	// DBObject dbo = iter.next();
	// Object obj = dbo.get(Work.F_ROOT_ID);
	//
	//
	// clas = getModelClass(dbo);
	// Assert.isNotNull(clas, "类参数不可为空");
	// PrimaryObject po = ModelService.createModelObject(dbo, clas);
	// /*while(po.getParentPrimaryObject()!=null){
	// po=po.getParentPrimaryObject();
	// }*/
	// po.setDataSet(ds);
	// Assert.isNotNull(po, "无法创建ORM映射的对象:" + dbo.toString());
	// dataItems.add(po);
	// }
	//
	// return dataItems;
	// }

	@Override
	public DBObject getSort() {
		return new BasicDBObject().append(Work.F__ID, -1);
	}
}
