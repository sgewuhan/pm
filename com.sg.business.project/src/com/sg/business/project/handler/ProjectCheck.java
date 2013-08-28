package com.sg.business.project.handler;

import org.eclipse.core.commands.ExecutionEvent;

import com.mobnut.db.model.PrimaryObject;
import com.sg.widgets.command.AbstractNavigatorHandler;

public class ProjectCheck extends AbstractNavigatorHandler {

	/*
	 * 1. 检查工作令号 警告，没有
	 * 2. 预算检查 警告，如果没有值
	 * 3. 检查角色的指派 警告没有指派人员的角色
	 * 4. 检查项目的流程 错误，没有指明流程负责人
	 * 5. WBS 
	 * 5.1. 检查工作的计划时间 错误，没有设定计划开始、计划完成、计划工时的叶工作
	 * 5.2. 检查工作的负责人 错误，没有设定负责人的叶工作
	 * 5.3. 工作的流程设定 警告，没有指明流程执行者的工作
	 * 5.4. 检查交付物 警告，没有交付物的叶工作
	 */

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		System.out.println(selected);
	}

}
