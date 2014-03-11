package com.sg.sales.ui.home;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.IWorkbenchPart;

import com.sg.business.model.Message;
import com.sg.business.model.Work;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.part.view.PrimaryObjectDetailFormView;

public class SalesHome extends PrimaryObjectDetailFormView {

	private GenericHomePanel genericHomePanel;

	
	/**
	 * 销售业绩
1. 客户保有数量
2. 有效电话活动数量（本月）
3. 销售线索数量（识别需求状态，预算规划状态的商机）（保有和本月新增）
4. 销售机会数量（交流选型，招标谈判状态的商机）（保有和本月新增）
5. 签约合同金额（累计的，今年累计的）（本月）
6. 回款金额（累计的，今年累计的）（本月的）
进度指标
	 */
	@Override
	protected void initContent() {
		cleanUI();
		content.setLayout(new GridLayout());
		genericHomePanel = new GenericHomePanel(content);
		super.goHome();
	}

	@Override
	public void goHome() {
		cleanUI();
		content.setLayout(new GridLayout());
		genericHomePanel = new GenericHomePanel(content);
		content.layout(false,false);
		super.goHome();
	}

	@Override
	protected boolean responseSelectionChanged(IWorkbenchPart part,
			ISelection selection) {
		if (!part.getSite().getId().equals("homenavigator")) {
			return false;
		}
		if (selection == null || selection.isEmpty()
				|| (!(selection instanceof IStructuredSelection))) {
			return false;
		}
		Object element = ((IStructuredSelection) selection).getFirstElement();
		return element instanceof Work || element instanceof WorkDefinition
				|| element instanceof Message;
	}

	@Override
	public void doRefresh() {
		if (isHome) {
			genericHomePanel.doRefresh();
		} else {
			loadMaster();
		}
	}

}
