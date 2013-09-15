package com.sg.business.home.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.AccountInfo;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.portal.user.UserSessionContext;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Work;
import com.sg.widgets.fullcalendar.CalendarEvent;
import com.sg.widgets.fullcalendar.FullCalendar;
import com.sg.widgets.fullcalendar.ICalendarEvent;
import com.sg.widgets.fullcalendar.IEventSelectionListener;

public class MyCalendar extends ViewPart implements IEventSelectionListener {

	private FullCalendar fc;

	public MyCalendar() {
	}

	@Override
	public void createPartControl(Composite parent) {
		fc = new FullCalendar(parent, SWT.NONE);
		List<ICalendarEvent> input = new ArrayList<ICalendarEvent>();

		List<PrimaryObject> OtherdataSet = getDataInput(new String[] { Work.STATUS_PAUSED_VALUE });
		// 暂停中的工作
		List<PrimaryObject> dataSet = getDataInput(new String[] { Work.STATUS_PAUSED_VALUE });
		List<ICalendarEvent> dataInput = setDataInput(dataSet,
				ICalendarEvent.COLOR_GRAY, "工作暂停中");
		input.addAll(dataInput);
		OtherdataSet.addAll(dataSet);

		// //工作没开始，还不到计划开始时间
		dataSet = getDataInput(new String[] { Work.STATUS_ONREADY_VALUE,
				Work.STATUS_NONE_VALUE });
		Iterator<PrimaryObject> iter = dataSet.iterator();
		while (iter.hasNext()) {
			Work work = (Work) iter.next();
			if (!(work.getPlanStart().getTime() > new Date().getTime())) {
				iter.remove();
			}
		}
		dataInput = setDataInput(dataSet, ICalendarEvent.COLOR_BLUES[0],
				"未到计划开始时间，工作准备中");
		input.addAll(dataInput);
		OtherdataSet.addAll(dataSet);

		// 工作没开始，但是已经到了计划开始时间
		dataSet = getDataInput(new String[] { Work.STATUS_ONREADY_VALUE,
				Work.STATUS_NONE_VALUE });
		iter = dataSet.iterator();
		while (iter.hasNext()) {
			Work work = (Work) iter.next();
			if (!(work.getPlanStart().getTime() < new Date().getTime())) {
				iter.remove();
			}
		}
		dataInput = setDataInput(dataSet, ICalendarEvent.COLOR_YELLOWS[0],
				"已经到了计划开始时间，工作未开始");
		input.addAll(dataInput);
		OtherdataSet.addAll(dataSet);

		// 已经到了计划完成时间但是没有完成
		dataSet = getDataInput(new String[] { Work.STATUS_ONREADY_VALUE,
				Work.STATUS_NONE_VALUE, Work.STATUS_PAUSED_VALUE });
		iter = dataSet.iterator();
		while (iter.hasNext()) {
			Work work = (Work) iter.next();
			if (!(work.getPlanFinish().getTime() <= new Date().getTime())) {
				iter.remove();
			}
		}
		dataInput = setDataInput(dataSet, ICalendarEvent.COLOR_REDS[0],
				"已经到了计划完成时间，工作未完成");
		input.addAll(dataInput);
		OtherdataSet.addAll(dataSet);

		// 按计划正常进行的工作
		dataSet = getDataInput(new String[] { Work.STATUS_ONREADY_VALUE,
				Work.STATUS_NONE_VALUE, Work.STATUS_PAUSED_VALUE,
				Work.STATUS_FINIHED_VALUE, Work.STATUS_CANCELED_VALUE,
				Work.STATUS_WIP_VALUE });
		dataSet.removeAll(OtherdataSet);
		dataInput = setDataInput(dataSet, ICalendarEvent.COLOR_GREENS[0],
				"按计划正常进行的工作");
		input.addAll(dataInput);

		fc.setInput(input);
		fc.addSelectionListener(this);
	}

	public List<PrimaryObject> getDataInput(String[] status) {

		List<PrimaryObject> ret = new ArrayList<PrimaryObject>();
		AccountInfo account = null;
		try {
			account = UserSessionContext.getAccountInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String userid = account.getConsignerId();
		// 查询本人参与的工作
		DBCollection collection = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_WORK);
		DBObject queryCondition = new BasicDBObject();
		queryCondition.put(Work.F_PARTICIPATE, userid);
		queryCondition.put(Work.F_LIFECYCLE,
				new BasicDBObject().append("$in", status));
		DBCursor cur = collection.find(queryCondition);
		while (cur.hasNext()) {
			DBObject dbo = cur.next();
			ObjectId workId = (ObjectId) dbo.get(Work.F__ID);
			Work work = ModelService.createModelObject(Work.class, workId);
			if (!ret.contains(work)) {
				ret.add(work);
			}
		}
		return ret;
	}

	public List<ICalendarEvent> setDataInput(List<PrimaryObject> dataSet,
			String color, String desc) {
		List<ICalendarEvent> input = new ArrayList<ICalendarEvent>();
		CalendarEvent event = null;
		try {

			for (PrimaryObject po : dataSet) {
				if (po instanceof Work) {
					event = new CalendarEvent();
					Work work = (Work) po;
					event.setId(work.getDesc());
					event.setDescription(desc);
					event.setNoticeMessage(1);
					event.setTitle(work.getHTMLLabel());
					event.setStart(new SimpleDateFormat("yyyy-MM-dd")
							.parse(new SimpleDateFormat("yyyy-MM-dd")
									.format(work.getPlanStart())));
					event.setEnd(new SimpleDateFormat("yyyy-MM-dd")
							.parse(new SimpleDateFormat("yyyy-MM-dd")
									.format(work.getPlanFinish())));
					event.setColor(color);
					event.setReminderTime(new SimpleDateFormat("yyyy-MM-dd")
							.parse("2013-08-20"));
					input.add(event);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return input;
	}

	@Override
	public void setFocus() {

	}

	@Override
	public void eventSelected(ICalendarEvent object) {
		System.out.println(object);
	}

}
