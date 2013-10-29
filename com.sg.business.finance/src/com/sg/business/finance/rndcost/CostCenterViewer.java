package com.sg.business.finance.rndcost;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.mobnut.db.DBActivator;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.CostAccount;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.RNDPeriodCost;

public class CostCenterViewer extends TableViewer {

	private ColumnLocator columnLocator;
	String[][] suggestArray;
	
	
	public CostCenterViewer(Composite parent, int style) {
		super(parent, style);
		
		final Table costCenterTable = this.getTable();
		costCenterTable.setHeaderVisible(true);
//		costCenterTable.setLinesVisible(true);
		costCenterTable.setData(RWT.FIXED_COLUMNS,new Integer(1));
		costCenterTable.getHorizontalBar().setVisible(false);
		
		suggestArray = createColumns(this);
		createColumnLocator(suggestArray);
	}
	
	protected void createColumnLocator(String[][] suggestArray) {
		columnLocator = new ColumnLocator(getTable().getShell(),getTable(),suggestArray);
	}
	
	private String[][] createColumns(TableViewer viewer) {
		createLabelColumn(viewer);

		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_COSTACCOUNT_ITEM);
		DBCursor cur = col.find();
		int i = 0;
		String[][] suggestArray = new String[cur.size()][2];

		while (cur.hasNext()) {
			DBObject next = cur.next();
			String accountNumber = (String) next.get(CostAccount.accountnumber);
			String accountName = (String) next.get(CostAccount.F_DESC);
			createColumn(viewer, accountNumber, accountName);
			suggestArray[i][0] = accountNumber;
			suggestArray[i][1] = accountName;
			i++;
		}
		
		return suggestArray;
	}

	private void createLabelColumn(TableViewer viewer) {
		TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.LEFT);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				String costCode = ((RNDPeriodCost) element).getCostCode();
				if (costCode == null) {
					return "请选择成本中心";
				} else {
					return super.getText(element);
				}
			}
		});
		final TableColumn column = viewerColumn.getColumn();
		column.setWidth(120);
		column.setText("成本对象");
		column.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				showInput();
			}
		});

		viewerColumn.setEditingSupport(new CostCenterSelector(viewer));

	}

	private TableColumn createColumn(TableViewer viewer, String accountNumber,
			String accountName) {
		TableViewerColumn viewerColumn = new TableViewerColumn(viewer,
				SWT.RIGHT);
		viewerColumn.setLabelProvider(new AccountDurationLabelProvider(
				accountNumber));

		final TableColumn column = viewerColumn.getColumn();
		column.setWidth(120);
		String columnTitle;
		if (accountName.contains("-")) {
			columnTitle = accountName.replace('-', '\n');
		} else {
			columnTitle = accountName + "\n ";
		}
		column.setText(columnTitle);
		column.setToolTipText(accountName + "(" + accountNumber + ")\n点输入定位列");
		column.setData("accountNumber", accountNumber);
		column.setData("accountName", accountName);
		column.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				showInput();
			}
		});

		return column;
	}

	protected void showInput() {

		Table table = getTable();
		Point location = table.toDisplay(0, 0);
		location.y += 60;

		if (columnLocator.isDisposed() || columnLocator == null) {
			createColumnLocator(suggestArray);
		}
		columnLocator.setLocation(location);
		if (!columnLocator.isVisible()) {
			columnLocator.setVisible(true);
		} else {
			columnLocator.open();
		}
		columnLocator.activate();
	}
}
