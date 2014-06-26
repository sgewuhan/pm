package com.sg.business.model.dataset.project;

import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.business.model.bson.SEQSorter;
import com.sg.widgets.part.CurrentAccountContext;

public class WorkOfAllProject extends SingleDBCollectionDataSetFactory {

	private String userId;
	private DBCollection projectCol;

	public WorkOfAllProject() {
		// 设置项目工作的集合的存放数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_WORK);
		// 设置项目工作集合的排序方式，使用默认的seq字段进行排序
		setSort(new SEQSorter().getBSON());
		this.userId = new CurrentAccountContext().getConsignerId();
		this.projectCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT);
	}

	@Override
	public DBObject getQueryCondition() {
		DBObject queryCondition = createQueryCondition();
		List<?> workIdList = projectCol.distinct(
				Project.F_WORK_ID,
				new BasicDBObject().append(Work.F_PARTICIPATE, userId).append(
						Project.F_LIFECYCLE, ILifecycle.STATUS_WIP_VALUE));
		queryCondition.put(
				Work.F__ID,
				new BasicDBObject().append("$in",
						workIdList.toArray(new ObjectId[0])));

		return queryCondition;
	}
}
