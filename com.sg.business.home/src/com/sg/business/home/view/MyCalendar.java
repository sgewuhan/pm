package com.sg.business.home.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Work;
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

	@Override
	public void createPartControl(Composite parent) {
		fc = new FullCalendar(parent, SWT.NONE);
		doRefresh();
		fc.addSelectionListener(this);
	}

	@Override
	public void doRefresh() {
		List<ICalendarEvent> input = getWorkDataInput();
		fc.setInput(input);
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
		while (cur.hasNext()) {
			DBObject workData = cur.next();
			Work work = ModelService.createModelObject(workData, Work.class);
			result.add(getCalendarEvent(work));
		}
		return result;
	}

	private ICalendarEvent getCalendarEvent(Work work) {
		CalendarEvent event = new CalendarEvent();
		event.setId(work.get_id().toString());
		Object value = work.getValue(Work.F_DESCRIPTION);
		String description = value == null ? "" : value.toString();
		event.setDescription(description);
		event.setNoticeMessage(0);
		event.setTitle(work.getLabel());
		event.setStart(work.getPlanStart());
		event.setEnd(work.getPlanFinish());

		// 暂停中的工作
		if (ILifecycle.STATUS_PAUSED_VALUE.equals(work.getLifecycleStatus())) {
			event.setColor(ICalendarEvent.COLOR_GRAY);
		}
		// 沒有開始的工作
		else if (ILifecycle.STATUS_ONREADY_VALUE.equals(work
				.getLifecycleStatus())
				|| ILifecycle.STATUS_NONE_VALUE.equals(work
						.getLifecycleStatus())) {
			// 還沒到計劃開始時間
			if (work.getPlanStart().getTime() > new Date().getTime()) {
				event.setColor(ICalendarEvent.COLOR_BLUES[2]);
			}
			// 已經到了計劃開始時間
			else if (work.getPlanStart().getTime() <= new Date().getTime()) {
				event.setColor(ICalendarEvent.COLOR_YELLOWS[2]);
			}
		}
		// 已经到了计划完成时间但是没有完成
		else if (ILifecycle.STATUS_WIP_VALUE.equals(work.getLifecycleStatus())) {
			if (work.getPlanFinish().getTime() <= new Date().getTime()) {
				event.setColor(ICalendarEvent.COLOR_REDS[2]);
			}
		} else {
			event.setColor(ICalendarEvent.COLOR_GREENS[2]);

		}
		return event;
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
	public void setFocus() {

	}
	
	

}
