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
import com.sg.business.model.AbstractWork;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
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
			if (work != null) {
				ICalendarEvent calendarEvent = getCalendarEvent(work);
				if (calendarEvent != null) {
					result.add(calendarEvent);
				}
			}
		}
		return result;
	}

	private ICalendarEvent getCalendarEvent(Work work) {
		Date planStart = work.getPlanStart();
		Date planFinish = work.getPlanFinish();
		if (planStart == null || planFinish == null) {
			return null;
		}
		CalendarEvent event = new CalendarEvent();
		event.setId(work.get_id().toString());
		Project project = work.getProject();
		if (project != null) {
			Object proStatus = project.getLifecycleStatus();
			if (proStatus == null
					|| ILifecycle.STATUS_NONE_VALUE.equals(proStatus)) {
				Integer workType = (Integer) work
						.getValue(AbstractWork.F_WORK_TYPE);
				if (!(workType != null && AbstractWork.WORK_TYPE_STANDLONE == workType)) {
					return null;
				}
			}
			event.setTitle(project.getLabel() + "/" + work.getLabel());
		} else {
			event.setTitle(work.getLabel());
		}
		Object workDesc = work.getValue(Work.F_DESCRIPTION);
		String description = workDesc == null ? "" : workDesc.toString();
		event.setDescription(description);
		event.setNoticeMessage(0);

		event.setStart(planStart);
		event.setEnd(planFinish);

		String lc = work.getLifecycleStatus();

		if (ILifecycle.STATUS_PAUSED_VALUE.equals(lc)) {
			// 暂停中的工作
			event.setColor(ICalendarEvent.COLOR_GRAY);
		} else if (ILifecycle.STATUS_ONREADY_VALUE.equals(lc)
				|| ILifecycle.STATUS_NONE_VALUE.equals(lc)) {
			// 沒有開始的工作
			if (work.getPlanStart().after(new Date())) {
				// 還沒到計劃開始時間
				event.setColor(ICalendarEvent.COLOR_BLUES[2]);
			} else {
				// 已經到了計劃開始時間
				event.setColor(ICalendarEvent.COLOR_YELLOWS[2]);
			}
		} else if (ILifecycle.STATUS_WIP_VALUE.equals(lc)) {
			// 已经到了计划完成时间但是没有完成
			if (work.getPlanFinish().before(new Date())) {
				event.setColor(ICalendarEvent.COLOR_REDS[2]);
			} else {
				event.setColor(ICalendarEvent.COLOR_GREENS[2]);
			}
		} else {
			return null;
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
