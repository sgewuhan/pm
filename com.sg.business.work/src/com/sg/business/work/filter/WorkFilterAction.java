package com.sg.business.work.filter;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.sg.business.resource.BusinessResource;

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
			//�����������
			viewer.resetFilters();
		}else{
			filterControl.clearCheck(SHOW_ALL_PROJECT_WORK);
			//���ù�������
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
			return "��ĳʱ����ڼƻ���ʼ";
		
		case SHOW_PLANFINISH_FILTER:
			return "��ĳʱ����ڼƻ���ʼ";

		default:
			break;
		}
		return null;
	}
}
