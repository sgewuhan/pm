package com.sg.business.home.view;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
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
import com.sg.business.model.ILifecycle;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.fullcalendar.CalendarEvent;
import com.sg.widgets.fullcalendar.FullCalendar;
import com.sg.widgets.fullcalendar.ICalendarEvent;
import com.sg.widgets.fullcalendar.IEventSelectionListener;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.IRefreshablePart;
import com.sg.widgets.part.view.NavigatorPart;

public class MyCalendar extends ViewPart implements IEventSelectionListener,
		IRefreshablePart {

	private FullCalendar fc;

	public MyCalendar() {
	}

	@Override
	public void createPartControl(Composite parent) {
		fc = new FullCalendar(parent, SWT.NONE);
		// List<ICalendarEvent> input = new ArrayList<ICalendarEvent>();
		//
		// List<PrimaryObject> otherdataSet = getDataInput(new String[] {
		// Work.STATUS_PAUSED_VALUE });
		// // 暂停中的工作
		// List<PrimaryObject> dataSet = getDataInput(new String[] {
		// Work.STATUS_PAUSED_VALUE });
		// List<ICalendarEvent> dataInput = setDataInput(dataSet,
		// ICalendarEvent.COLOR_GRAY);
		// input.addAll(dataInput);
		// otherdataSet.addAll(dataSet);
		//
		// // //工作没开始，还不到计划开始时间
		// dataSet = getDataInput(new String[] { Work.STATUS_ONREADY_VALUE,
		// Work.STATUS_NONE_VALUE });
		// Iterator<PrimaryObject> iter = dataSet.iterator();
		// while (iter.hasNext()) {
		// Work work = (Work) iter.next();
		// if (!(work.getPlanStart().getTime() > new Date().getTime())) {
		// iter.remove();
		// }
		// }
		// dataInput = setDataInput(dataSet, ICalendarEvent.COLOR_BLUES[0]);
		// input.addAll(dataInput);
		// otherdataSet.addAll(dataSet);
		//
		// // 工作没开始，但是已经到了计划开始时间
		// dataSet = getDataInput(new String[] { Work.STATUS_ONREADY_VALUE,
		// Work.STATUS_NONE_VALUE });
		// iter = dataSet.iterator();
		// while (iter.hasNext()) {
		// Work work = (Work) iter.next();
		// if (!(work.getPlanStart().getTime() < new Date().getTime())) {
		// iter.remove();
		// }
		// }
		// dataInput = setDataInput(dataSet, ICalendarEvent.COLOR_YELLOWS[2]);
		// input.addAll(dataInput);
		// otherdataSet.addAll(dataSet);
		//
		// // 已经到了计划完成时间但是没有完成
		// dataSet = getDataInput(new String[] { Work.STATUS_ONREADY_VALUE,
		// Work.STATUS_NONE_VALUE, Work.STATUS_PAUSED_VALUE });
		// iter = dataSet.iterator();
		// while (iter.hasNext()) {
		// Work work = (Work) iter.next();
		// if (!(work.getPlanFinish().getTime() <= new Date().getTime())) {
		// iter.remove();
		// }
		// }
		// dataInput = setDataInput(dataSet, ICalendarEvent.COLOR_REDS[2]);
		// input.addAll(dataInput);
		// otherdataSet.addAll(dataSet);
		//
		// // 按计划正常进行的工作
		// dataSet = getDataInput(new String[] { Work.STATUS_ONREADY_VALUE,
		// Work.STATUS_NONE_VALUE, Work.STATUS_PAUSED_VALUE,
		// Work.STATUS_FINIHED_VALUE, Work.STATUS_CANCELED_VALUE,
		// Work.STATUS_WIP_VALUE });
		// dataSet.removeAll(otherdataSet);
		// dataInput = setDataInput(dataSet, ICalendarEvent.COLOR_GREENS[2]);
		// input.addAll(dataInput);
		//
		// fc.setInput(input);
		doRefresh();
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
			String color) {
		List<ICalendarEvent> input = new ArrayList<ICalendarEvent>();
		CalendarEvent event = null;
		try {

			for (PrimaryObject po : dataSet) {
				if (po instanceof Work) {
					event = new CalendarEvent();
					Work work = (Work) po;
					event.setId(work.get_id().toString());
					Object value = work.getValue(Work.F_DESCRIPTION);
					String description = value == null ? "" : value.toString();
					event.setDescription(description);
					event.setNoticeMessage(0);
					event.setTitle(work.getLabel());
					event.setStart(work.getPlanStart());
					event.setEnd(work.getPlanFinish());
					event.setColor(color);
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

		String id = object.getId();
		Work work = ModelService
				.createModelObject(Work.class, new ObjectId(id));

		NavigatorPart view = (NavigatorPart) Widgets
				.getViewPart("work.processing");
		if (view != null) {
			StructuredViewer viewer = view.getNavigator().getViewer();
			viewer.setSelection(new StructuredSelection(new Object[] { work }),
					true);
		}
	}

	@Override
	public boolean canRefresh() {
		return true;
	}

	@Override
	public void doRefresh() {

		List<ICalendarEvent> input = getWorkDataInput();
		fc.setInput(input);
//		MessageUtil.showToast("Refresh", SWT.ICON_INFORMATION);
	}

	private List<ICalendarEvent> getWorkDataInput() {
		List<ICalendarEvent> result = new ArrayList<ICalendarEvent>();
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_WORK);
		DBObject ref = new BasicDBObject();
		ref.put(Work.F_PARTICIPATE, new CurrentAccountContext()
				.getAccountInfo().getConsignerId());
		ref.put(Work.F_LIFECYCLE,
				new BasicDBObject().append("$in", new String[] {
						ILifecycle.STATUS_NONE_VALUE,
						ILifecycle.STATUS_ONREADY_VALUE,
						ILifecycle.STATUS_WIP_VALUE,
						ILifecycle.STATUS_PAUSED_VALUE }));
		DBCursor cur = col.find(ref);
		while(cur.hasNext()){
			DBObject workData = cur.next();
			Work work = ModelService.createModelObject(workData, Work.class);
			result.add(getCalendarEvent(work));
		}
		return result;
	}

	private ICalendarEvent getCalendarEvent(Work work) {
		
		return null;
	}

}
