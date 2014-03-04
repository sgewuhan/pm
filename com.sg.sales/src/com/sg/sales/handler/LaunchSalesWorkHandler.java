package com.sg.sales.handler;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Work;
import com.sg.sales.model.Company;
import com.sg.sales.model.ICompanyRelatied;
import com.sg.sales.model.ISalesWork;
import com.sg.sales.model.Opportunity;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.commons.model.IEditorSaveHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.viewer.ViewerControl;

/**
 * 发起与公司有关的工作
 * 
 * @author zhonghua
 * 
 */
public class LaunchSalesWorkHandler extends AbstractNavigatorHandler {

	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		return command.getId().equals("sales.launchwork");
	}

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters,
			IStructuredSelection selection) {
		CurrentAccountContext context = new CurrentAccountContext();
		Work work = ModelService.createModelObject(Work.class);
		work.setValue(Work.F_CHARGER, context.getConsignerId());// 设置负责人为当前用户
		work.setValue(Work.F_LIFECYCLE, Work.STATUS_ONREADY_VALUE);// 设置该工作的状态为准备中，以便自动开始
		work.setValue(Work.F_WORK_TYPE, Work.WORK_TYPE_STANDLONE);
		work.setValue(Work.F_WORK_CATAGORY, ISalesWork.WORK_CATAGORY_SALES);

		try {
			DataObjectDialog d = DataObjectDialog.openDialog(work,
					"work.plan.sales.create", true, IEditorSaveHandler.NOSAVE);
			if (d.getReturnCode() == DataObjectDialog.OK) {
				PrimaryObjectEditorInput inputData = (PrimaryObjectEditorInput) d
						.getInput();
				work = (Work) inputData.getData();
			} else {
				return;
			}
			if (selection == null || selection.isEmpty()||command.getId().equals("sales.launchwork")) {
				work.doSave(context);
			} else {
				Iterator<?> iter = selection.iterator();
				while (iter.hasNext()) {
					Work newWork = (Work) work.clone();

					PrimaryObject parent = (PrimaryObject) iter.next();
					if (parent instanceof Company) {
						newWork.setValue(ISalesWork.F_COMPANY_ID,
								parent.get_id());
					}
					if (parent instanceof ICompanyRelatied) {
						newWork.setValue(ISalesWork.F_COMPANY_ID,
								parent.getValue(ICompanyRelatied.F_COMPANY_ID));
					}
					if (parent instanceof Opportunity) {
						newWork.setValue(ISalesWork.F_OPPORTUNITY_ID,
								parent.get_id());
					}
					newWork.doSave(context);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
