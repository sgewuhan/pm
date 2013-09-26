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

	public static final int SHOW_ALL_PROJECT_WORK = 0;// ��ʾ��Ŀ���й���
	public static final int SHOW_MY_PROJECT_WORK = 1;// ��ʾ��Ŀ�����йصĹ���

	public static final int SHOW_WORK_ON_READY = 2;// ׼���еĹ���
	public static final int SHOW_WORK_ON_PROGRESS = 3;// �����еĹ���

	public static final int SHOW_WORK_CHARGED = 4;// �Ҹ���Ĺ���
	public static final int SHOW_WORK_PATICIPATE = 5;// �Ҳ���Ĺ���
	public static final int SHOW_WORK_ASSIGNMENT = 6;// ��Ҫ��ָ�ɵĹ���

	public static final int SHOW_REMIND_WORK = 7;// ����Ԥ���Ĺ���
	public static final int SHOW_DELAYED_WORK = 8;// �Ѿ����ڵĹ���
	public static final int SHOW_MARKED_WORK = 9;// �ұ�ǵĹ���

	public static final int SHOW_PLANSTART_FILTER = 10;// ��ĳʱ����ڼƻ���ʼ
	public static final int SHOW_PLANFINISH_FILTER = 11;// ��ĳʱ����ڼƻ���ʼ

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
			// ��ʾ����ѡ���
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
			// �����������
			viewer.resetFilters();
		} else {
			filterControl.clearCheck(SHOW_ALL_PROJECT_WORK);
			// ���ù�������
			ViewerFilter[] filters = viewer.getFilters();
			ViewerFilter[] newFilters = filters;
			if (check) {

				// ȥ��ì�ܵĹ���������
				ViewerFilter[] reverseFilters = filterControl
						.uncheckReverseFilters(code);
				// ȥ��ì�ܵĹ�����
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
			return "��ʾȫ������";

		case SHOW_MY_PROJECT_WORK:
			return "��ʾ��Ŀ�����йصĹ���";

		case SHOW_WORK_ON_READY:
			return "׼���еĹ���";

		case SHOW_WORK_ON_PROGRESS:
			return "�����еĹ���";

		case SHOW_WORK_CHARGED:
			return "�Ҹ���Ĺ���";

		case SHOW_WORK_PATICIPATE:
			return "�Ҳ���Ĺ���";

		case SHOW_WORK_ASSIGNMENT:
			return "��Ҫ��ָ�ɵĹ���";

		case SHOW_REMIND_WORK:
			return "����Ԥ���Ĺ���";

		case SHOW_DELAYED_WORK:
			return "�Ѿ����ڵĹ���";

		case SHOW_MARKED_WORK:
			return "�ұ�ǵĹ���";

		case SHOW_PLANSTART_FILTER:
			return "�ƻ���ʼʱ������";

		case SHOW_PLANFINISH_FILTER:
			return "�ƻ����ʱ������";

		default:
			break;
		}
		return null;
	}

	public ViewerFilter getFilter() {
		return filter;
	}

	/**
	 * ��ʾ�������ڵ�ѡ��Ի���
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
