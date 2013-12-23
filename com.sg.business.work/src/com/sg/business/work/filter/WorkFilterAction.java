package com.sg.business.work.filter;

import java.util.Date;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Display;

import com.mobnut.commons.util.Utils;
import com.sg.business.resource.BusinessResource;
import com.sg.business.work.nls.Messages;
import com.sg.widgets.commons.selector.DateFromToSelector;
import com.sg.widgets.viewer.CTreeViewer;

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
	private StructuredViewer viewer;
	private WorkFilter filter;

	public WorkFilterAction(WorkFilterControl workFilterControl, int filterCode) {
		super(getNameByCode(filterCode), IAction.AS_CHECK_BOX);
		setImageDescriptor(getImageDescriptorByCode(filterCode));
		this.filterControl = workFilterControl;
		this.code = filterCode;
		viewer = filterControl.getViewer();
		setChecked(false);
		initFilter();
	}

	private void initFilter() {
		filter = new WorkFilter(code);
	}

	@Override
	public void setChecked(boolean checked) {
		super.setChecked(checked);

		if (!checked) {
			setText(getNameByCode(code));
		}

	}

	@Override
	public void run() {
		boolean check = isChecked();

		if (check
				&& (code == SHOW_PLANSTART_FILTER || code == SHOW_PLANFINISH_FILTER)) {
			// 显示日期选择框
			Date[] fromto = getDateFromTo();
			if (fromto == null) {
				setChecked(false);
				return;
			} else {
				filter.setData(fromto);
				setDateText(fromto);
			}
		}

		if (code == SHOW_ALL_PROJECT_WORK) {
			filterControl.clearAllCheck();
			// 清除过滤条件
			viewer.resetFilters();
		} else {
			filterControl.clearCheck(SHOW_ALL_PROJECT_WORK);
			// 设置过滤条件
			ViewerFilter[] filters = viewer.getFilters();
			ViewerFilter[] newFilters = filters;
			if (check) {

				// 去除矛盾的过滤器符号
				ViewerFilter[] reverseFilters = filterControl
						.uncheckReverseFilters(code);
				// 去除矛盾的过滤器
				for (int i = 0; i < reverseFilters.length; i++) {
					newFilters = Utils.removeElementInArray(newFilters,
							reverseFilters[i], false, ViewerFilter.class);
				}

				newFilters = Utils.addElementToArray(newFilters, filter, false,
						ViewerFilter.class);
			} else {
				newFilters = Utils.removeElementInArray(newFilters, filter,
						false, ViewerFilter.class);
			}
			viewer.resetFilters();
			final ViewerFilter[] filtersTobeSet = newFilters;
			final Display display = viewer.getControl().getDisplay();
			
			display.asyncExec(new Runnable() {
				@Override
				public void run() {
					if(!display.isDisposed()){
						CTreeViewer cTreeViewer = (CTreeViewer) viewer;
						cTreeViewer.expandAll();
						viewer.setFilters(filtersTobeSet);
					}
				}
			});
			
		}
	}

	private void setDateText(Date[] fromto) {
		String dateText = " ["; //$NON-NLS-1$
		if (fromto.length > 0 && fromto[0] != null) {
			dateText += String.format(Utils.FORMATE_DATE_COMPACT_SASH, fromto[0]);
		} else {
			dateText += " "; //$NON-NLS-1$
		}

		dateText += " ~ "; //$NON-NLS-1$
		if (fromto.length > 1 && fromto[1] != null) {
			dateText += String.format(Utils.FORMATE_DATE_COMPACT_SASH, fromto[1]);
		} else {
			dateText += " "; //$NON-NLS-1$
		}
		dateText += "]"; //$NON-NLS-1$

		setText(getNameByCode(code) + dateText);
	}

	private ImageDescriptor getImageDescriptorByCode(int filterCode) {
		switch (filterCode) {
		case SHOW_WORK_ON_READY:
			return BusinessResource
					.getImageDescriptor(BusinessResource.IMAGE_READY_24);

		case SHOW_WORK_ON_PROGRESS:
			return BusinessResource
					.getImageDescriptor(BusinessResource.IMAGE_START_24);

		case SHOW_WORK_ASSIGNMENT:
			return BusinessResource
					.getImageDescriptor(BusinessResource.IMAGE_WORK_ASSIGNMENT_24);

		case SHOW_REMIND_WORK:
			return BusinessResource
					.getImageDescriptor(BusinessResource.IMAGE_ALERT_24);

		case SHOW_MARKED_WORK:
			return BusinessResource
					.getImageDescriptor(BusinessResource.IMAGE_MARK_24);

		case SHOW_DELAYED_WORK:
			return BusinessResource
					.getImageDescriptor(BusinessResource.IMAGE_FLAG_RED_24);

		case SHOW_ALL_PROJECT_WORK:
			return BusinessResource
					.getImageDescriptor(BusinessResource.IMAGE_WORK_FILTER_24);

		case SHOW_MY_PROJECT_WORK:
			// return "显示项目与我有关的工作";

		case SHOW_WORK_CHARGED:
			// return "我负责的工作";

		case SHOW_WORK_PATICIPATE:
			// return "我参与的工作";

		case SHOW_PLANSTART_FILTER:
			// return "计划开始时间区间";

		case SHOW_PLANFINISH_FILTER:
			// return "计划完成时间区间";

		default:
			break;
		}
		return BusinessResource
				.getImageDescriptor(BusinessResource.IMAGE_24_BLANK);
	}

	private static String getNameByCode(int filterCode) {
		switch (filterCode) {
		case SHOW_ALL_PROJECT_WORK:
			return Messages.get().WorkFilterAction_5;

		case SHOW_MY_PROJECT_WORK:
			return Messages.get().WorkFilterAction_6;

		case SHOW_WORK_ON_READY:
			return Messages.get().WorkFilterAction_7;

		case SHOW_WORK_ON_PROGRESS:
			return Messages.get().WorkFilterAction_8;

		case SHOW_WORK_CHARGED:
			return Messages.get().WorkFilterAction_9;

		case SHOW_WORK_PATICIPATE:
			return Messages.get().WorkFilterAction_10;

		case SHOW_WORK_ASSIGNMENT:
			return Messages.get().WorkFilterAction_11;

		case SHOW_REMIND_WORK:
			return Messages.get().WorkFilterAction_12;

		case SHOW_DELAYED_WORK:
			return Messages.get().WorkFilterAction_13;

		case SHOW_MARKED_WORK:
			return Messages.get().WorkFilterAction_14;

		case SHOW_PLANSTART_FILTER:
			return Messages.get().WorkFilterAction_15;

		case SHOW_PLANFINISH_FILTER:
			return Messages.get().WorkFilterAction_16;

		default:
			break;
		}
		return null;
	}

	public ViewerFilter getFilter() {
		return filter;
	}

	/**
	 * 显示两个日期的选择对话框
	 * 
	 * @return
	 */
	private Date[] getDateFromTo() {
		DateFromToSelector selector = new DateFromToSelector(viewer
				.getControl().getShell());
		int ok = selector.open();
		if (Dialog.OK == ok) {
			return selector.getDate();
		}
		return null;

	}
}
