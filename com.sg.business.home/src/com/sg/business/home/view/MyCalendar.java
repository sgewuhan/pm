package com.sg.business.home.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
			CalendarEvent event=null;
			try {
				List<PrimaryObject> dataInput = getDataInput();
                for(PrimaryObject po:dataInput){
                	if(po instanceof Work){
                		event = new CalendarEvent();
						Work work = (Work) po;
						event.setId(work.getDesc());
	        			event.setDescription(work.getDesc());
	        			event.setNoticeMessage(1);
	        			event.setTitle(work.getHTMLLabel());
	        			event.setStart(new SimpleDateFormat("yyyy-MM-dd")
	        					.parse("2013-08-10"));
	        			event.setEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2013-08-28"));
	        			event.setColor(ICalendarEvent.COLOR_BLUES[1]);
	        			event.setReminderTime(new SimpleDateFormat("yyyy-MM-dd")
	        					.parse("2013-08-20"));
	        			input.add(event);
                	}
                }
			} catch (Exception e) {
				e.printStackTrace();
			}
			/*
			 * event.setId("001"); event.setDescription("测试工作1qweqwe");
			 * event.setNoticeMessage(1);
			 * event.setTitle("工作标题收到回复决定是否决定是否合适的机会福建省地方"); event.setStart(new
			 * SimpleDateFormat("yyyy-MM-dd") .parse("2013-08-10"));
			 * event.setEnd(new
			 * SimpleDateFormat("yyyy-MM-dd").parse("2013-08-28"));
			 * event.setColor(ICalendarEvent.COLOR_BLUES[1]);
			 * event.setReminderTime(new
			 * SimpleDateFormat("yyyy-MM-dd").parse("2013-08-20"));
			 * input.add(event);
			 * 
			 * event = new CalendarEvent(); event.setId("002");
			 * event.setDescription("测试工作2"); event.setNoticeMessage(1);
			 * event.setTitle("工作标题2"); event.setStart(new
			 * SimpleDateFormat("yyyy-MM-dd") .parse("2013-07-20"));
			 * event.setEnd(new
			 * SimpleDateFormat("yyyy-MM-dd").parse("2013-08-28"));
			 * event.setColor(ICalendarEvent.COLOR_REDS[2]);
			 * event.setReminderTime(new
			 * SimpleDateFormat("yyyy-MM-dd").parse("2013-07-23"));
			 * input.add(event);
			 * 
			 * event = new CalendarEvent(); event.setId("003");
			 * event.setDescription("测试工作3"); event.setNoticeMessage(0);
			 * event.setTitle("工作标题3"); event.setStart(new
			 * SimpleDateFormat("yyyy-MM-dd").parse("2013-08-20"));
			 * event.setEnd(new
			 * SimpleDateFormat("yyyy-MM-dd").parse("2013-08-25"));
			 * event.setColor(ICalendarEvent.COLOR_YELLOWS[1]);
			 * event.setReminderTime(new
			 * SimpleDateFormat("yyyy-MM-dd").parse("2013-08-23"));
			 * input.add(event);
			 */
			
			fc.setInput(input);
			fc.addSelectionListener(this);
	}

	public List<PrimaryObject> getDataInput() throws Exception {

		List<PrimaryObject> ret = new ArrayList<PrimaryObject>();
		AccountInfo account = UserSessionContext.getAccountInfo();
		String userid = account.getConsignerId();
		// 查询本人参与的工作
		DBCollection collection = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_WORK);
		DBObject queryCondition = new BasicDBObject();
		queryCondition.put(Work.F_PARTICIPATE, userid);
		queryCondition.put(Work.F_LIFECYCLE, Work.STATUS_WIP_VALUE);
		DBCursor cur = collection.find(queryCondition);
		while (cur.hasNext()) {
			DBObject dbo = cur.next();
			ObjectId workId = (ObjectId) dbo.get(Work.F__ID);;
			Work work = ModelService.createModelObject(Work.class, workId);
			if (!ret.contains(work)) {
				ret.add(work);
			}
		}
		return ret;
	}

	@Override
	public void setFocus() {

	}

	@Override
	public void eventSelected(ICalendarEvent object) {
		System.out.println(object);
	}

}
