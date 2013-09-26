package com.sg.business.work.filter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class WorkFilterControl {

	private Map<Integer, WorkFilterAction> actionMap;
	private StructuredViewer viewer;

	public WorkFilterControl(StructuredViewer viewer) {
		setViewer(viewer);
	}

	public MenuManager createMenu() {
		actionMap = new HashMap<Integer, WorkFilterAction>();

		MenuManager menuManager = new MenuManager("#filterMenu");
		menuManager.add(createAction(WorkFilterAction.SHOW_WORK_ON_READY));
		menuManager.add(createAction(WorkFilterAction.SHOW_WORK_ON_PROGRESS));
		menuManager.add(new Separator());

		menuManager.add(createAction(WorkFilterAction.SHOW_MARKED_WORK));
		menuManager.add(createAction(WorkFilterAction.SHOW_REMIND_WORK));
		menuManager.add(createAction(WorkFilterAction.SHOW_DELAYED_WORK));
		menuManager.add(new Separator());

		menuManager.add(createAction(WorkFilterAction.SHOW_MY_PROJECT_WORK));
		menuManager.add(createAction(WorkFilterAction.SHOW_WORK_CHARGED));
		menuManager.add(createAction(WorkFilterAction.SHOW_WORK_PATICIPATE));
		menuManager.add(createAction(WorkFilterAction.SHOW_WORK_ASSIGNMENT));
		menuManager.add(new Separator());

		menuManager.add(createAction(WorkFilterAction.SHOW_PLANSTART_FILTER));
		menuManager.add(createAction(WorkFilterAction.SHOW_PLANFINISH_FILTER));

		menuManager.add(new Separator());
		menuManager.add(createAction(WorkFilterAction.SHOW_ALL_PROJECT_WORK));

		return menuManager;
	}

	private IAction createAction(int code) {
		WorkFilterAction action = new WorkFilterAction(this, code);
		this.actionMap.put(new Integer(code), action);
		return action;
	}

	public void clearAllCheck() {
		Iterator<WorkFilterAction> iter = actionMap.values().iterator();
		while (iter.hasNext()) {
			iter.next().setChecked(false);
		}
	}

	public void clearCheck(int code) {
		actionMap.get(code).setChecked(false);
	}

	public StructuredViewer getViewer() {
		return viewer;
	}

	public void setViewer(StructuredViewer viewer) {
		this.viewer = viewer;
	}

	/**
	 * 获得某个过滤器矛盾的过滤器，并取消他们的Action上的选择图标，返回矛盾的过滤器
	 * 
	 * @param code
	 * @return
	 */
	public ViewerFilter[] uncheckReverseFilters(int code) {
		//-----------------------------------------------------------------------------------------
		// 显示我的所有项目中的工作与显示我指派的、我负责的、我参与的工作矛盾
		if (code == WorkFilterAction.SHOW_MY_PROJECT_WORK) {

			return uncheckReverseFilters(new int[] {
					WorkFilterAction.SHOW_WORK_ASSIGNMENT,
					WorkFilterAction.SHOW_WORK_CHARGED,
					WorkFilterAction.SHOW_WORK_PATICIPATE });

		} else if (code == WorkFilterAction.SHOW_WORK_ASSIGNMENT
				|| code == WorkFilterAction.SHOW_WORK_CHARGED
				|| code == WorkFilterAction.SHOW_WORK_PATICIPATE) {

			return uncheckReverseFilters(new int[] { WorkFilterAction.SHOW_MY_PROJECT_WORK });
		}

		//-----------------------------------------------------------------------------------------
		// 我标记的，已经超期的，提醒超期的互相矛盾
		if (code == WorkFilterAction.SHOW_MARKED_WORK) {
			return uncheckReverseFilters(new int[] {
					WorkFilterAction.SHOW_DELAYED_WORK,
					WorkFilterAction.SHOW_REMIND_WORK });

		} else if (code == WorkFilterAction.SHOW_DELAYED_WORK) {
			return uncheckReverseFilters(new int[] {
					WorkFilterAction.SHOW_MARKED_WORK,
					WorkFilterAction.SHOW_REMIND_WORK });
			
		} else if (code == WorkFilterAction.SHOW_REMIND_WORK) {
			return uncheckReverseFilters(new int[] {
					WorkFilterAction.SHOW_MARKED_WORK,
					WorkFilterAction.SHOW_DELAYED_WORK });
		}

		return new ViewerFilter[0];
	}

	private ViewerFilter[] uncheckReverseFilters(int[] codeList) {
		ViewerFilter[] result = new ViewerFilter[codeList.length];
		WorkFilterAction action;
		for (int i = 0; i < codeList.length; i++) {
			action = actionMap.get(codeList[i]);
			action.setChecked(false);
			result[0] = action.getFilter();
		}

		return result;
	}
}
