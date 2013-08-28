package com.sg.business.home.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

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
		try {
			fc = new FullCalendar(parent, SWT.NONE);
			List<ICalendarEvent> input = new ArrayList<ICalendarEvent>();
			CalendarEvent event = new CalendarEvent();
			event.setId("001");
			event.setDescription("测试工作1");
			event.setNoticeMessage("提示信息1");
			event.setTitle("工作标题1");
			event.setStart(new SimpleDateFormat("yyyy-MM-dd")
					.parse("2013-08-10"));
			event.setEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2013-08-28"));
//			event.setColor(ICalendarEvent.COLOR_RED);
			event.setReminderTime(new SimpleDateFormat("yyyy-MM-dd").parse("2013-08-20"));
			input.add(event);
			
			event = new CalendarEvent();
			event.setId("002");
			event.setDescription("测试工作2");
			event.setNoticeMessage("提示信息2");
			event.setTitle("工作标题2");
			event.setStart(new SimpleDateFormat("yyyy-MM-dd")
					.parse("2013-07-20"));
			event.setEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2013-08-28"));
//			event.setColor(ICalendarEvent.COLOR_BLUE);
			event.setReminderTime(new SimpleDateFormat("yyyy-MM-dd").parse("2013-07-23"));
			input.add(event);
			
			event = new CalendarEvent();
			event.setId("003");
			event.setDescription("测试工作3");
			event.setNoticeMessage("提示信息3");
			event.setTitle("工作标题3");
			event.setStart(new SimpleDateFormat("yyyy-MM-dd")
					.parse("2013-08-20"));
			event.setEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2013-08-25"));
//			event.setColor(ICalendarEvent.COLOR_YELLOW);
			event.setReminderTime(new SimpleDateFormat("yyyy-MM-dd").parse("2013-08-23"));
			input.add(event);
			
			
			fc.setInput(input);
			
			
			fc.addSelectionListener(this);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setFocus() {

	}

	@Override
	public void eventSelected(ICalendarEvent object) {
		System.out.println(object);
	}

}
