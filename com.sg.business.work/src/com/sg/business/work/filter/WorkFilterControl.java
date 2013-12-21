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

		MenuManager menuManager = new MenuManager("#filterMenu"); //$NON-NLS-1$
		menuManager.add(createAction(WorkFilterAction.SHOW_ALL_PROJECT_WORK));
		menuManager.add(createAction(WorkFilterAction.SHOW_MY_PROJECT_WORK));
		menuManager.add(new Separator());

		menuManager.add(createAction(WorkFilterAction.SHOW_WORK_ON_READY));
		menuManager.add(createAction(WorkFilterAction.SHOW_WORK_ON_PROGRESS));
		menuManager.add(new Separator());

		menuManager.add(createAction(WorkFilterAction.SHOW_MARKED_WORK));
		menuManager.add(createAction(WorkFilterAction.SHOW_REMIND_WORK));
		menuManager.add(createAction(WorkFilterAction.SHOW_DELAYED_WORK));
		menuManager.add(new Separator());

		menuManager.add(createAction(WorkFilterAction.SHOW_WORK_CHARGED));
		menuManager.add(createAction(WorkFilterAction.SHOW_WORK_PATICIPATE));
		menuManager.add(createAction(WorkFilterAction.SHOW_WORK_ASSIGNMENT));
		menuManager.add(new Separator());

		menuManager.add(createAction(WorkFilterAction.SHOW_PLANSTART_FILTER));
		menuManager.add(createAction(WorkFilterAction.SHOW_PLANFINISH_FILTER));

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
	 * ���ĳ��������ì�ܵĹ���������ȡ�����ǵ�Action�ϵ�ѡ��ͼ�꣬����ì�ܵĹ�����
	 * 
	 * @param code
	 * @return
	 */
	public ViewerFilter[] uncheckReverseFilters(int code) {

		switch (code) {
		case WorkFilterAction.SHOW_MY_PROJECT_WORK:
			// -----------------------------------------------------------------------------------------
			// ��ʾ�ҵ�������Ŀ�еĹ�������ʾ��ָ�ɵġ��Ҹ���ġ��Ҳ���Ĺ���ì��

			return uncheckReverseFilters(new int[] {
					WorkFilterAction.SHOW_WORK_ASSIGNMENT,
					WorkFilterAction.SHOW_WORK_CHARGED,
					WorkFilterAction.SHOW_WORK_PATICIPATE });

			// -----------------------------------------------------------------------------------------
			// �ұ�ǵģ��Ѿ����ڵģ����ѳ��ڵĻ���ì��
		case WorkFilterAction.SHOW_MARKED_WORK:
			return uncheckReverseFilters(new int[] {
					WorkFilterAction.SHOW_DELAYED_WORK,
					WorkFilterAction.SHOW_REMIND_WORK });

		case WorkFilterAction.SHOW_DELAYED_WORK:
			return uncheckReverseFilters(new int[] {
					WorkFilterAction.SHOW_MARKED_WORK,
					WorkFilterAction.SHOW_REMIND_WORK });

		case WorkFilterAction.SHOW_REMIND_WORK:
			return uncheckReverseFilters(new int[] {
					WorkFilterAction.SHOW_MARKED_WORK,
					WorkFilterAction.SHOW_DELAYED_WORK });

			// ׼���У������л���
		case WorkFilterAction.SHOW_WORK_ON_PROGRESS:
			return uncheckReverseFilters(new int[] { WorkFilterAction.SHOW_WORK_ON_READY });

		case WorkFilterAction.SHOW_WORK_ON_READY:
			return uncheckReverseFilters(new int[] { WorkFilterAction.SHOW_WORK_ON_PROGRESS });

			// �Ҹ���ģ��Ҳ���ĺ���ָ�ɵĻ���
		case WorkFilterAction.SHOW_WORK_CHARGED:
			return uncheckReverseFilters(new int[] {
					WorkFilterAction.SHOW_WORK_ASSIGNMENT,
					WorkFilterAction.SHOW_WORK_PATICIPATE,
					WorkFilterAction.SHOW_MY_PROJECT_WORK });

		case WorkFilterAction.SHOW_WORK_ASSIGNMENT:
			return uncheckReverseFilters(new int[] {
					WorkFilterAction.SHOW_WORK_CHARGED,
					WorkFilterAction.SHOW_WORK_PATICIPATE,
					WorkFilterAction.SHOW_MY_PROJECT_WORK });

		case WorkFilterAction.SHOW_WORK_PATICIPATE:
			return uncheckReverseFilters(new int[] {
					WorkFilterAction.SHOW_WORK_CHARGED,
					WorkFilterAction.SHOW_WORK_ASSIGNMENT,
					WorkFilterAction.SHOW_MY_PROJECT_WORK });

		default:
			return new ViewerFilter[0];
		}

	}

	private ViewerFilter[] uncheckReverseFilters(int[] codeList) {
		ViewerFilter[] result = new ViewerFilter[codeList.length];
		WorkFilterAction action;
		for (int i = 0; i < codeList.length; i++) {
			action = actionMap.get(codeList[i]);
			action.setChecked(false);
			result[i] = action.getFilter();
		}

		return result;
	}
}
