package com.sg.business.project.handler;

import org.eclipse.core.commands.ExecutionEvent;

import com.mobnut.db.model.PrimaryObject;
import com.sg.widgets.command.AbstractNavigatorHandler;

public class ProjectCheck extends AbstractNavigatorHandler {

	/*
	 * 1. ��鹤����ţ� ���棬û��
	 * 2. Ԥ���� �����棬���û��ֵ
	 * 3. ����ɫ��ָ�ɣ� ����û��ָ����Ա�Ľ�ɫ
	 * 4. �����Ŀ������ ������û��ָ�����̸�����
	 * 5. WBS 
	 * 5.1. ��鹤���ļƻ�ʱ�� ������û���趨�ƻ���ʼ���ƻ���ɡ��ƻ���ʱ��Ҷ����
	 * 5.2. ��鹤���ĸ����� ������û���趨�����ˣ�����û���趨ָ���ߵ�Ҷ����
	 * 5.3. �����������趨 �����棬û��ָ������ִ���ߵĹ���
	 * 6. ������ 
	 * 6.1. ��鹤���Ƿ���н�������棬û�н������Ҷ����
	 * 6.2. ��齻�����ĵ�û�е����ļ���Ϊģ�壺����
	 * 
	 * --
	 * ���¼�����Ŀ�ļƻ���ʼ��ʼʱ�䡢�ƻ����ʱ�䡢����
	 * ���¼�����Ŀ���ܹ�ʱ
	 */

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		System.out.println(selected);
	}

}
