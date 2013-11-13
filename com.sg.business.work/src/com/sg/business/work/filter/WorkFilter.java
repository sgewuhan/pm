package com.sg.business.work.filter;

import java.util.ArrayList;
import java.util.Date;

import org.bson.types.BasicBSONList;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

import com.mobnut.commons.util.Utils;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.Work;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.CTreeViewer;

public class WorkFilter extends ViewerFilter {

	abstract class WorkSelector {

		boolean select(Object holder, Work work, Object value) {
			if (select(work, value)) {
				return true;
			} else {
				Object[] items;
				if (holder instanceof CTreeViewer) {
					
					items = ((CTreeViewer) holder).testFindItems(work);
					if(items == null||items.length==0){
						ArrayList<Work> list = new ArrayList<Work>();
						list.add(work);
						Widget[] trees = ((CTreeViewer) holder).testFindItems(list);
						if(trees.length>0){
							Widget t = trees[0];
							if(t instanceof Tree){
								items = ((Tree) t).getItems();
							}
						}
					}
					
					
				} else if (holder instanceof TreeItem) {
					items = ((TreeItem) holder).getItems();
				} else {
					return false;
				}
				for (int i = 0; i < items.length; i++) {
					TreeItem child = (TreeItem) items[i];
					Object data = child.getData();
					if (data instanceof Work
							&& select(items[i], (Work) data, value)) {
						return true;
					}
				}
			}
			return false;
		}

		abstract boolean select(Work work, Object value);

	}

	private int code;
	private String userid;
	private WorkSelector selector;
	private Object data;

	public WorkFilter(int code) {
		this.code = code;
		userid = new CurrentAccountContext().getAccountInfo().getConsignerId();

		switch (code) {
		case WorkFilterAction.SHOW_WORK_ON_READY:
		case WorkFilterAction.SHOW_WORK_ON_PROGRESS:
			selector = new WorkSelector() {
				@Override
				boolean select(Work work, Object status) {
					return status != null
							&& status.equals(work.getLifecycleStatus());
				}
			};
			break;

		case WorkFilterAction.SHOW_WORK_ASSIGNMENT:
			selector = new WorkSelector() {
				@Override
				boolean select(Work work, Object userId) {
					return userId.equals(work.getAssignerId());
				}
			};
			break;

		case WorkFilterAction.SHOW_WORK_CHARGED:
			selector = new WorkSelector() {
				@Override
				boolean select(Work work, Object userId) {
					return userId.equals(work.getChargerId());
				}
			};
			break;

		case WorkFilterAction.SHOW_WORK_PATICIPATE:
			selector = new WorkSelector() {
				@Override
				boolean select(Work work, Object userId) {
					BasicBSONList ids = work.getParticipatesIdList();
					return ids != null && ids.contains(userId);
				}
			};
			break;

		case WorkFilterAction.SHOW_MY_PROJECT_WORK:
			selector = new WorkSelector() {
				@Override
				boolean select(Work work, Object userId) {
					if (userId.equals(work.getAssignerId())) {
						return true;
					}

					if (userId.equals(work.getChargerId())) {
						return true;
					}

					BasicBSONList ids = work.getParticipatesIdList();
					return ids != null && ids.contains(userId);
				}
			};
			break;

		case WorkFilterAction.SHOW_MARKED_WORK:
			selector = new WorkSelector() {
				@Override
				boolean select(Work work, Object userId) {
					return work.isMarked((String) userId);
				}
			};
			break;

		case WorkFilterAction.SHOW_REMIND_WORK:
			selector = new WorkSelector() {
				@Override
				boolean select(Work work, Object nul) {
					return work.isRemindNow();
				}
			};
			break;

		case WorkFilterAction.SHOW_DELAYED_WORK:
			selector = new WorkSelector() {
				@Override
				boolean select(Work work, Object nul) {
					return work.isDelayNow();
				}
			};
			break;

		case WorkFilterAction.SHOW_PLANSTART_FILTER:
			selector = new WorkSelector() {
				@Override
				boolean select(Work work, Object dates) {
					Date date = work.getPlanStart();
					if (date != null) {
						if (dates instanceof Date[]) {
							return Utils.dateIn((Date[])dates, date);
						}
					}
					return true;
				}
			};
			break;

		case WorkFilterAction.SHOW_PLANFINISH_FILTER:
			selector = new WorkSelector() {
				@Override
				boolean select(Work work, Object dates) {
					Date date = work.getPlanFinish();
					if (date != null) {
						if (dates instanceof Date[]) {
							return Utils.dateIn((Date[])dates, date);
						}
					}
					return true;
				}

			};
			break;

		default:
			break;
		}
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {

		Work work = (Work) element;
		switch (code) {
		case WorkFilterAction.SHOW_WORK_ON_READY:
			return selector.select(viewer, work,
					ILifecycle.STATUS_ONREADY_VALUE);

		case WorkFilterAction.SHOW_WORK_ON_PROGRESS:
			return selector.select(viewer, work, ILifecycle.STATUS_WIP_VALUE);

		case WorkFilterAction.SHOW_WORK_ASSIGNMENT:
		case WorkFilterAction.SHOW_WORK_CHARGED:
		case WorkFilterAction.SHOW_WORK_PATICIPATE:
		case WorkFilterAction.SHOW_MY_PROJECT_WORK:
		case WorkFilterAction.SHOW_MARKED_WORK:
			return selector.select(viewer, work, userid);

		case WorkFilterAction.SHOW_REMIND_WORK:
		case WorkFilterAction.SHOW_DELAYED_WORK:
			return selector.select(viewer, work, null);

		case WorkFilterAction.SHOW_PLANFINISH_FILTER:
		case WorkFilterAction.SHOW_PLANSTART_FILTER:
			return selector.select(viewer, work, data);

		default:
			break;
		}

		return false;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Object getData() {
		return data;
	}

}
