package com.sg.business.work.filter;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.sg.business.resource.BusinessResource;

public class WorkFilterAction extends Action {

	public static final int SHOW_ALL_PROJECT_WORK = 0;// 显示项目所有工作
	public static final int SHOW_MY_PROJECT_WORK = 1;// 显示项目与我有关的工作

	public static final int SHOW_WORK_ON_READY = 2;// 准备中的工作
	public static final int SHOW_WORK_ON_PROGRESS = 3;// 进行中的工作

	public static final int SHOW_WORK_CHARGED = 4;// 我负责的工作
	public static final int SHOW_WORK_PATICIPATE = 5;// 我参与的工作
	public static final int SHOW_WORK_ASSIGNMENT = 6;// 需要我指派的工作

	public static final int SHOW_REMIND_WORK = 7;// 超期预警的工作
	public static final int SHOW_DELAYED_WORK = 8;// 已经超期的工作
	public static final int SHOW_MARKED_WORK = 9;// 我标记的工作

	public static final int SHOW_PLANSTART_FILTER = 10;// 在某时间段内计划开始
	public static final int SHOW_PLANFINISH_FILTER = 11;// 在某时间段内计划开始

	public static final int filterCount = 12;
	
	private WorkFilterControl filterControl;
	private int code;
	private boolean check = false;
	private StructuredViewer viewer;
	private ViewerFilter filter;

	public WorkFilterAction(WorkFilterControl workFilterControl, int filterCode) {
		super(getNameByCode(filterCode), IAction.AS_CHECK_BOX);
		this.filterControl = workFilterControl;
		setImageDescriptor(BusinessResource.getImageDescriptor(BusinessResource.IMAGE_24_BLANK));
		this.code = filterCode;
		viewer = filterControl.getViewer();
		
		initFilter();
	}

	private void initFilter() {
		filter = new WorkFilter(code) ;
	}

	@Override
	public void run() {
		check = !check;
		if(code == SHOW_ALL_PROJECT_WORK){
			filterControl.clearAllCheck();
			//清除过滤条件
			viewer.resetFilters();
		}else{
			filterControl.clearCheck(SHOW_ALL_PROJECT_WORK);
			//设置过滤条件
			if(check){
				viewer.addFilter(filter);
			}else{
				viewer.removeFilter(filter);
			}
		}
		setChecked(check);
	}


	private static String getNameByCode(int filterCode) {
		switch (filterCode) {
		case SHOW_ALL_PROJECT_WORK:
			return "显示全部工作";
			
		case SHOW_MY_PROJECT_WORK:
			return "显示项目与我有关的工作";
		
		case SHOW_WORK_ON_READY:
			return "准备中的工作";
		
		case SHOW_WORK_ON_PROGRESS:
			return "进行中的工作";

		case SHOW_WORK_CHARGED:
			return "我负责的工作";
		
		case SHOW_WORK_PATICIPATE:
			return "我参与的工作";
		
		case SHOW_WORK_ASSIGNMENT:
			return "需要我指派的工作";

		case SHOW_REMIND_WORK:
			return "超期预警的工作";

		case SHOW_DELAYED_WORK:
			return "已经超期的工作";
		
		case SHOW_MARKED_WORK:
			return "我标记的工作";

		case SHOW_PLANSTART_FILTER:
			return "在某时间段内计划开始";
		
		case SHOW_PLANFINISH_FILTER:
			return "在某时间段内计划开始";

		default:
			break;
		}
		return null;
	}
}
