package com.sg.business.work.filter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.StructuredViewer;

public class WorkFilterControl {

	private Map<Integer,IAction> actionMap;
	private StructuredViewer viewer;

	public WorkFilterControl(StructuredViewer viewer) {
		setViewer(viewer);
	}

	public MenuManager createMenu() {
		actionMap = new HashMap<Integer,IAction>();
		
		MenuManager menuManager = new MenuManager("#filterMenu");
		menuManager.add(createAction(WorkFilterAction.SHOW_WORK_ON_READY));
		menuManager.add(createAction(WorkFilterAction.SHOW_WORK_ON_PROGRESS));
		menuManager.add(new Separator());
		
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
		this.actionMap.put(new Integer(code),action);
		return action;
	}

	public void clearAllCheck() {
		Iterator<IAction> iter = actionMap.values().iterator();
		while(iter.hasNext()){
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

}
