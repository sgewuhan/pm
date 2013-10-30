package com.sg.business.finance.rndcost;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.util.Util;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.internal.widgets.ITableAdapter;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.commons.eai.WorkOrderPeriodCostAdapter;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.RNDPeriodCost;
import com.sg.business.model.WorkOrderPeriodCost;
import com.sg.widgets.MessageUtil;

@SuppressWarnings("restriction")
public class RNDCostAdjustmentView extends ViewPart {

	class CostCenterDuration implements IStructuredContentProvider {

		private Organization organization;

		private Integer year;

		private Integer month;

		private DBCollection costCol;

		private DBCollection costAllocateCol;

		private RNDPeriodCost rndPeriodCost;

		private List<WorkOrderPeriodCost> workOrdersCostAllocation = new ArrayList<WorkOrderPeriodCost>();

		public CostCenterDuration(Organization organization) {
			this.organization = organization;
		}

		public CostCenterDuration() {
			costCol = DBActivator.getCollection(IModelConstants.DB,
					IModelConstants.C_RND_PEROIDCOST_COSTCENTER);
			costAllocateCol = DBActivator.getCollection(IModelConstants.DB,
					IModelConstants.C_RND_PEROIDCOST_ALLOCATION);

		}

		public void load() {
			if (organization != null) {
				String costCenterCode = organization.getCostCenterCode();

				// ��ȡ����
				// ��ô�SAP�õ��ĸ������ݾ���
				DBObject dbo = null;
				dbo = costCol.findOne(new BasicDBObject()
						.append(RNDPeriodCost.F_YEAR, year)
						.append(RNDPeriodCost.F_MONTH, month)
						.append(RNDPeriodCost.F_COSTCENTERCODE,
								organization.getCostCenterCode()));
				if (dbo != null) {
					rndPeriodCost = ModelService.createModelObject(dbo,
							RNDPeriodCost.class);
				} else {
					MessageUtil.showToast(
							"�޷���ȡ�ڼ��з��ɱ����ݣ�" + organization.getCostCenterCode()
									+ "\n" + year + "-" + month,
							SWT.ICON_WARNING);

					// try {
					// rndPeriodCost = readRNDPeriodCost(costCenterCode);
					// } catch (Exception e) {
					// MessageUtil.showToast(e);
					// return;
					// }
				}

				// ��ȡ������ŷ�̯����
				DBCursor cur = costAllocateCol.find(new BasicDBObject()
						.append(WorkOrderPeriodCost.F_YEAR, year)
						.append(WorkOrderPeriodCost.F_MONTH, month)
						.append(WorkOrderPeriodCost.F_COSTCENTERCODE,
								costCenterCode));
				if (cur.size() > 0) {
					workOrdersCostAllocation.clear();
					// �Ѿ��������������
					while (cur.hasNext()) {
						DBObject dbObject = cur.next();
						workOrdersCostAllocation.add(ModelService
								.createModelObject(dbObject,
										WorkOrderPeriodCost.class));
					}
				} else {
					workOrdersCostAllocation.clear();
					workOrdersCostAllocation
							.addAll(readWorkOrderPeriodCost(costCenterCode));
				}
			} else {
				// �����ն�������ѡ��
				rndPeriodCost = ModelService.createModelObject(
						new BasicDBObject(), RNDPeriodCost.class);
				// ��չ�����ű��input
				workOrdersCostAllocation.clear();
			}
		}

		private Collection<? extends WorkOrderPeriodCost> readWorkOrderPeriodCost(
				String costCenterCode) {
			WorkOrderPeriodCostAdapter adapter = new WorkOrderPeriodCostAdapter();

			Map<String, Object> parameter = new HashMap<String, Object>();

			parameter.put(WorkOrderPeriodCostAdapter.YEAR, year);
			parameter.put(WorkOrderPeriodCostAdapter.MONTH, month);
			parameter.put(WorkOrderPeriodCostAdapter.COSECENTERCODE,
					organization.getCostCenterCode());
			parameter.put(WorkOrderPeriodCostAdapter.RNDCOST, rndPeriodCost);

			return adapter.getData(parameter);
		}

		// private RNDPeriodCost readRNDPeriodCost(String costCenterCode) throws
		// Exception {
		// RNDPeriodCostAdapter adapter = new RNDPeriodCostAdapter();
		//
		// Map<String, Object> parameter = new HashMap<String, Object>();
		//
		// parameter.put(RNDPeriodCostAdapter.YEAR, year);
		// parameter.put(RNDPeriodCostAdapter.MONTH, month);
		// parameter.put(RNDPeriodCostAdapter.ORGCODE,
		// organization.getCompanyCode());
		// parameter.put(RNDPeriodCostAdapter.COSECENTERCODE,
		// organization.getCostCenterCode());
		// return adapter.getData(parameter);
		// }

		@Override
		public void dispose() {

		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			if (Util.equals(oldInput, newInput)) {
				return;
			}
			if (newInput instanceof CostCenterDurationQueryParameter) {
				CostCenterDurationQueryParameter p = (CostCenterDurationQueryParameter) newInput;
				month = p.month;
				year = p.year;
				organization = p.organization;
				load();

				workOrderViewer.refresh();
			}
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return new Object[] { rndPeriodCost };
		}

		/**
		 * ��õ�ǰ��֯�µĹ�����Ŷ�Ӧ�ķ�̯���з��ɱ����
		 * 
		 * @return
		 */
		public List<WorkOrderPeriodCost> getWorkOrdersCostAllocation() {
			return workOrdersCostAllocation;
		}

	}

	private CostCenterViewer costCenterViewer;
	private WorkOrderCostViewer workOrderViewer;

	public RNDCostAdjustmentView() {

	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FormLayout());

		costCenterViewer = new CostCenterViewer(parent, SWT.FULL_SELECTION);
		FormData fd = new FormData();
		Table costCenterTable = costCenterViewer.getTable();
		costCenterTable.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);

		workOrderViewer = new WorkOrderCostViewer(parent, SWT.FULL_SELECTION,
				costCenterViewer.suggestArray);
		Table workOrderTable = workOrderViewer.getTable();
		fd = new FormData();
		workOrderTable.setLayoutData(fd);
		fd.top = new FormAttachment(costCenterTable);
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(100);

		syncHorizontalScroll(workOrderTable, costCenterTable);
		syncColumnSize(costCenterTable, workOrderTable);

		loadata();
	}

	private void loadata() {
		// ��ȡ�ɱ����İ��ڼ�ĸ���Ŀ���
		CostCenterDuration ccd = new CostCenterDuration();
		costCenterViewer.setContentProvider(ccd);
		Calendar cal = Calendar.getInstance();
		costCenterViewer.setInput(new CostCenterDurationQueryParameter(cal
				.get(Calendar.YEAR), cal.get(Calendar.MONTH), null));// ȡ�ϸ��µ�����

		workOrderViewer.setInput(ccd.getWorkOrdersCostAllocation());

	}

	private void syncColumnSize(Table table1, Table table2) {
		final TableColumn[] cols1 = table1.getColumns();
		final TableColumn[] cols2 = table2.getColumns();
		for (int i = 0; i < cols1.length; i++) {
			final int k = i;
			cols1[i].addControlListener(new ControlListener() {

				@Override
				public void controlResized(ControlEvent e) {
					cols2[k].setWidth(cols1[k].getWidth());
				}

				@Override
				public void controlMoved(ControlEvent e) {
				}
			});
		}
	}

	private int scrolling;

	private void syncHorizontalScroll(final Table table1, final Table table2) {
		table1.getHorizontalBar().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				int scrolling = ((ScrollBar) event.widget).getSelection();
				if (scrolling == RNDCostAdjustmentView.this.scrolling) {
					return;
				}
				RNDCostAdjustmentView.this.scrolling = scrolling;
				scrollingTable(table2, scrolling);
			}
		});

		table2.getHorizontalBar().addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent event) {
				int scrolling = ((ScrollBar) event.widget).getSelection();
				if (scrolling == RNDCostAdjustmentView.this.scrolling) {
					return;
				}
				RNDCostAdjustmentView.this.scrolling = scrolling;
				scrollingTable(table1, scrolling);
			}
		});

	}

	protected void scrollingTable(Table table, int table2Scrolling) {
		table.getAdapter(ITableAdapter.class).setLeftOffset(table2Scrolling);
	}

	@Override
	public void setFocus() {
		costCenterViewer.getControl().setFocus();
	}

}
