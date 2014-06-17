package com.sg.business.performence.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Project;
import com.sg.business.model.Work;

public class ProjectWorksNode extends WorksNode {

	public ProjectWorksNode(WorksNode parent, PrimaryObject data) {
		super(parent, data);
	}

	private Set<String> stages = new HashSet<String>();


	@Override
	public void addChild(WorksNode child) {
		if (child instanceof WorkWorksNode) {
			// ȡ�������Ĺ�ʱͳ�ƽ׶�
			String stage = (String) ((Work) child.getData())
					.getValue(Work.F_STATISTICS_STEP);
			stages.add(stage);
		}
		super.addChild(child);
	}

	/**
	 * ��ʾͳ�ƽ׶Σ�����ж�����ö��ŷָ�
	 */
	@Override
	public String getAdditionInfomation() {

		String additionInfo = null;
		Iterator<String> iter = stages.iterator();
		while (iter.hasNext()) {
			if (additionInfo == null) {
				additionInfo = iter.next();
			} else {
				additionInfo += ", " + iter.next();
			}
		}
		return additionInfo;
	}

	/**
	 * ������Ŀ���ƣ��������
	 */
	@Override
	public String getLabel() {
		Project project = (Project) getData();
		String desc = project.getDesc();
		String[] workOrders = project.getWorkOrders();
		if(workOrders==null||workOrders.length==0){
			return desc;
		}else if(workOrders.length==1){
			return desc+" | "+workOrders[0];
		}else{
			return desc+" | "+workOrders[0] +" ...";
		}
	}

}
