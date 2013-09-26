package com.sg.business.work.filter;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.mobnut.commons.util.Utils;
import com.sg.business.resource.BusinessResource;
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
		this.filterControl = workFilterControl;
		setImageDescriptor(BusinessResource
				.getImageDescriptor(BusinessResource.IMAGE_24_BLANK));
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
			((CTreeViewer) viewer).expandAll();
			viewer.setFilters(newFilters);
		}
	}

	private void setDateText(Date[] fromto) {
		String dateText = " [";
		SimpleDateFormat sdf = new SimpleDateFormat(Utils.SDF_DATE_COMPACT_SASH);
		if (fromto.length > 0 && fromto[0] != null) {
			dateText += sdf.format(fromto[0]);
		} else {
			dateText += " ";
		}

		dateText += " ~ ";
		if (fromto.length > 1 && fromto[1] != null) {
			dateText += sdf.format(fromto[1]);
		} else {
			dateText += " ";
		}
		dateText += "]";

		setText(getNameByCode(code) + dateText);
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
			return "计划开始时间区间";

		case SHOW_PLANFINISH_FILTER:
			return "计划完成时间区间";

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
