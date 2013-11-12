package com.sg.business.model.dataset.usertask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.IProcessControl;
import com.sg.business.model.UserTask;
import com.sg.business.model.Work;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class TaskHistoryDataSet extends MasterDetailDataSetFactory {

	public TaskHistoryDataSet() {
		super(IModelConstants.DB, IModelConstants.C_USERTASK);

		DBObject sort = new BasicDBObject();
		sort.put(UserTask.F__ID, -1);
		setSort(sort);
	}

	@Override
	protected String getDetailCollectionKey() {
		return UserTask.F_PROCESSINSTANCEID;
	}

	@Override
	protected Object getMasterValue() {
		if (master instanceof Work) {
			return master.getValue(Work.F_WF_EXECUTE
					+ IProcessControl.POSTFIX_INSTANCEID);
		} else {
			return master.getValue(UserTask.F_PROCESSINSTANCEID);
		}
	}

	@Override
	public DataSet getDataSet() {
		List<PrimaryObject> items = super.getDataSet().getDataItems();
		Map<Long, PrimaryObject> taskMap = new HashMap<Long, PrimaryObject>();
		// 每个任务id只保留一个记录

		for (int i = 0; i < items.size(); i++) {
			UserTask item = (UserTask) items.get(i);
			Long taskId = item.getTaskId();
			PrimaryObject savedTask = taskMap.get(taskId);
			if (savedTask == null || laterThan(item, savedTask)) {
				taskMap.put(taskId, item);
			}
		}

		ArrayList<PrimaryObject> result = new ArrayList<PrimaryObject>();
		result.addAll(taskMap.values());
		Collections.sort(result, new Comparator<PrimaryObject>() {

			@Override
			public int compare(PrimaryObject first, PrimaryObject second) {
				Date date1 = first.get_cdate();
				Date date2 = second.get_cdate();
				return -1 * date1.compareTo(date2);
			}
		});

		return new DataSet(result);
	}

	private boolean laterThan(UserTask first, PrimaryObject second) {
		Date date1 = first.get_cdate();
		Date date2 = second.get_cdate();
		return date1.after(date2);
	}
}
