package com.sg.business.project.handler;

import org.eclipse.core.commands.ExecutionEvent;

import com.mobnut.db.model.PrimaryObject;
import com.sg.widgets.command.AbstractNavigatorHandler;

public class ProjectCheck extends AbstractNavigatorHandler {

	/*
	 * 1. ��鹤����� ���棬û��
	 * 2. Ԥ���� ���棬���û��ֵ
	 * 3. ����ɫ��ָ�� ����û��ָ����Ա�Ľ�ɫ
	 * 4. �����Ŀ������ ����û��ָ�����̸�����
	 * 5. WBS 
	 * 5.1. ��鹤���ļƻ�ʱ�� ����û���趨�ƻ���ʼ���ƻ���ɡ��ƻ���ʱ��Ҷ����
	 * 5.2. ��鹤���ĸ����� ����û���趨�����˵�Ҷ����
	 * 5.3. �����������趨 ���棬û��ָ������ִ���ߵĹ���
	 * 5.4. ��齻���� ���棬û�н������Ҷ����
	 */

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		System.out.println(selected);
	}

}
