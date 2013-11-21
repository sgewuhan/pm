package com.sg.business.project.test;

import org.eclipse.core.expressions.PropertyTester;

import com.sg.business.model.ProductItem;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.widgets.part.CurrentAccountContext;

public class ProjectOperationTest extends PropertyTester {

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		if (receiver instanceof Project) {
			Project project = (Project) receiver;
			if ("operation".equals(property) && args != null && args.length > 0) {
				if ("edit".equals(args[0])) {
					boolean expected = Boolean.TRUE.equals(expectedValue);
					// 用于控制何种情况下可以使用编辑项目计划的editor打开
					return expected == project
							.canEdit(new CurrentAccountContext());
				} else if ("delete".equals(args[0])) {
					boolean expected = Boolean.TRUE.equals(expectedValue);
					return expected == project
							.canDelete(new CurrentAccountContext());
				} else if ("changeuser".equals(args[0])){
					boolean expected = Boolean.TRUE.equals(expectedValue);
					return expected == project
							.canChangUser(new CurrentAccountContext());
				} else if ("check".equals(args[0])) {
					boolean expected = Boolean.TRUE.equals(expectedValue);
					return expected == (project.canCheck() && project
							.canEdit(new CurrentAccountContext()));
				}
			}
		} else if (receiver instanceof Work) {
			Work work = (Work) receiver;
			Project project = work.getProject();
			if (project != null) {
				if ("operation".equals(property) && args != null
						&& args.length > 0) {
					if ("move".equals(args[0])) {
						boolean expected = Boolean.TRUE.equals(expectedValue);
						// 只有在项目无状态或者是准备中才能够移动
						String lc = project.getLifecycleStatus();

						return expected == (Project.STATUS_NONE_VALUE
								.equals(lc) || Project.STATUS_ONREADY_VALUE
								.equals(lc));
					}
				}
			}
		} else if(receiver instanceof ProductItem){
			ProductItem productItem = (ProductItem) receiver;
			if(productItem != null){
				if ("operation".equals(property) && args != null
						&& args.length > 0) {
				if("canFinal".equals(args[0])){
					return productItem.canFinal();
				}
				}
			}
			
		}
		return false;
	}

}
