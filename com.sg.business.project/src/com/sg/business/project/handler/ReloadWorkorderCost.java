package com.sg.business.project.handler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.commons.Commons;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.model.CompanyWorkOrder;
import com.sg.business.model.CostAccount;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.WorkOrderPeriodCost;
import com.sg.business.model.etl.eai.RNDPeriodCostAdapter;
import com.sg.business.model.etl.eai.WorkorderPeriodCostAdapter;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.commons.selector.NavigatorSelector;
import com.sg.widgets.viewer.ViewerControl;

public class ReloadWorkorderCost extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters,
			IStructuredSelection selection) {
		if (selection == null || selection.isEmpty()) {
			return;
		}
		// 选择合并的目标
		NavigatorSelector ns = new NavigatorSelector("organization.navigator",
				"Please Select Reload Organization") {
			@Override
			protected void doOK(IStructuredSelection is) {
				Organization org = (Organization) is.getFirstElement();
				String message = ""; //$NON-NLS-1$
				// 获得所有的成本中心代码
				String[] costCodes = getCostCodeArray(org);
				// 获取成本元素代码
				String[] costElementArray = CostAccount.getCostElemenArray();
				// 获取工作令号
				String[] workOrders = CompanyWorkOrder.getWorkOrders(org);
				int i = 5;
				while (i != 0) {
					try {
						clear(workOrders);
						doReload(org, costCodes, costElementArray, workOrders);
						super.doOK(is);
					} catch (Exception e) {
						i--;
						message += e.getMessage() + "\n"; //$NON-NLS-1$
					}

				}
				Commons.logerror(message);
			}
		};
		ns.show();

	}

	protected void clear(String[] workOrders) {
		DBObject removeCondition = new BasicDBObject();
		removeCondition.put(WorkOrderPeriodCost.F_WORKORDER,
				new BasicDBObject().append("$in", workOrders)); //$NON-NLS-1$
		List<BasicDBObject> removeDateList = new ArrayList<BasicDBObject>();
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		for (int i = 2009; i <= year; i++) {
			for (int j = 0; j < 12; j++) {
				if (i == year && j >= month) {
					continue;
				}
				BasicDBObject dbo = new BasicDBObject();
				dbo.put(WorkOrderPeriodCost.F_YEAR, i);
				dbo.put(WorkOrderPeriodCost.F_MONTH, j + 1);
				removeDateList.add(dbo);
			}
		}

		BasicDBObject[] removeDate = removeDateList
				.toArray(new BasicDBObject[0]);
		if (removeDate.length > 0) {
			removeCondition.put("$or", removeDate);
		}

		DBCollection rndCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_RND_PEROIDCOST_COSTCENTER);
		rndCol.remove(removeCondition);
		
		DBCollection rndAllocationCol = DBActivator
				.getCollection(IModelConstants.DB,
						IModelConstants.C_RND_PEROIDCOST_ALLOCATION);
		rndAllocationCol.remove(removeCondition);
		
		DBCollection workOrderCol = DBActivator.getCollection(
				IModelConstants.DB, IModelConstants.C_WORKORDER_COST);
		workOrderCol.remove(removeCondition);
	}

	protected void doReload(Organization org, String[] costCodes,
			String[] costElementArray, String[] workOrders) throws Exception {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		long start, end;
		for (int i = 2009; i <= year; i++) {
			for (int j = 0; j < 12; j++) {
				if (i == year && j >= month) {
					continue;
				}

				Commons.loginfo("[成本数据]准备获取SAP成本中心数据:" + year + "-" + month); //$NON-NLS-1$ //$NON-NLS-2$
				start = System.currentTimeMillis();
				RNDPeriodCostAdapter rndAdapter = new RNDPeriodCostAdapter();
				rndAdapter.runGetData(costCodes, costElementArray, i, j);
				end = System.currentTimeMillis();
				Commons.loginfo("[成本数据]获得SAP成本中心数据完成:" + year + "-" + month + " " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						+ (end - start) / 1000 + " S");

				Commons.loginfo("[成本数据]准备获取SAP工作令号研发成本数据:" + year + "-" + month); //$NON-NLS-1$ //$NON-NLS-2$
				start = System.currentTimeMillis();
				WorkorderPeriodCostAdapter workorderadapter = new WorkorderPeriodCostAdapter();
				workorderadapter.runGetData(workOrders, costElementArray, i, j);
				end = System.currentTimeMillis();
				Commons.loginfo("[成本数据]获得SAP工作令号研发成本完成:" + year + "-" + month + " " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						+ (end - start) / 1000 + " S"); //$NON-NLS-1$
			}
		}

	}

	private String[] getCostCodeArray(Organization org) {
		List<String> result = org.getCostCenterCodeList();
		return result.toArray(new String[0]);
	}

}
