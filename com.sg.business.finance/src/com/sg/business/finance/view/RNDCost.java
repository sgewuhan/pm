package com.sg.business.finance.view;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
//import org.eclipse.rap.addons.autosuggest.AutoSuggest;
//import org.eclipse.rap.addons.autosuggest.ColumnDataProvider;
//import org.eclipse.rap.addons.autosuggest.ColumnTemplate;
//import org.eclipse.rap.addons.autosuggest.DataSource;
import org.eclipse.rap.rwt.scripting.ClientListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.DBActivator;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.CostAccount;
import com.sg.business.model.IModelConstants;

public class RNDCost extends ViewPart {

	private TableViewer viewer;
	private Shell columnLocator;
//	private DataSource dataSource;

	public RNDCost() {

	}

	@Override
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.FULL_SELECTION);
		viewer.getTable().setHeaderVisible(true);
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		createColumns();

		createColumnLocator();
	}

	private void createColumnLocator() {
		columnLocator = new Shell(getSite().getShell(), SWT.BORDER);
		columnLocator.setLayout(new FormLayout());
		Text text = new Text(columnLocator, SWT.BORDER);
		String scriptCode = Utils.getClientListener(Utils.TYPE_INTEGER);
		if (scriptCode != null) {
			ClientListener clientListener = new ClientListener(scriptCode);
			text.addListener(SWT.Verify, clientListener);
			text.addListener(SWT.Modify, clientListener);
		}

//		createDataSource(text);

		FormData fd = new FormData();
		text.setLayoutData(fd);
		fd.left = new FormAttachment(0, 1);
		fd.top = new FormAttachment(0, 1);
		fd.right = new FormAttachment(100, -1);
		fd.bottom = new FormAttachment(100, -1);
		fd.width = 80;
		columnLocator.pack();
		columnLocator.addShellListener(new ShellListener() {

			@Override
			public void shellDeactivated(ShellEvent e) {
				columnLocator.setVisible(false);
			}

			@Override
			public void shellClosed(ShellEvent e) {
			}

			@Override
			public void shellActivated(ShellEvent e) {
			}
		});
	}

//	private void createDataSource(Text text) {
//		dataSource = new DataSource();
//		dataSource.setTemplate(new ColumnTemplate(60,120)); // the column
//																	// widths
//		
//		TableColumn[] columns = viewer.getTable().getColumns();
//		final String[][] suggestArray = new String[columns.length][2];
//		for (int i = 0; i < columns.length; i++) {
//			suggestArray[i][0] = (String) columns[i].getData("accountNumber");
//			suggestArray[i][1] = (String) columns[i].getData("accountName");
//		}
//		
//		ColumnDataProvider dataProvider = new ColumnDataProvider() {
//			public Iterable<?> getSuggestions() {
//				return Arrays.asList(suggestArray); 
//			}
//
//			public String getValue(Object element) {
//				String[] value = (String[]) element;
//				return value[0];
//			}
//
//			public String[] getTexts(Object element) {
//				String[] value = (String[]) element;
//				return new String[] { value[0], value[1] };
//			}
//		};
//		dataSource.setDataProvider(dataProvider);
//		AutoSuggest autoSuggest = new AutoSuggest(text);
//		autoSuggest.setDataSource(dataSource);
//	}

	private void createColumns() {
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_COSTACCOUNT);
		DBCursor cur = col.find();
		while (cur.hasNext()) {
			DBObject next = cur.next();
			String accountNumber = (String) next.get(CostAccount.accountnumber);
			String accountName = (String) next.get(CostAccount.F_DESC);
			createColumn(accountNumber, accountName);
		}
	}

	private void createColumn(String accountNumber, String accountName) {
		TableViewerColumn viewerColumn = new TableViewerColumn(viewer,
				SWT.RIGHT);
		final TableColumn column = viewerColumn.getColumn();
		column.setWidth(70);
		String columnTitle;
		if (accountName.contains("-")) {
			columnTitle = accountName.replace('-', '\n');
		} else {
			columnTitle = accountName + "\n ";
		}
		column.setText(columnTitle);
		column.setToolTipText(accountName + "(" + accountNumber + ")\n点输入定位列");
		column.setAlignment(SWT.CENTER);
		column.setData("accountNumber", accountNumber);
		column.setData("accountName", accountName);
		column.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				showInput(column);
			}
		});
	}

	protected void showInput(TableColumn column) {

		Table table = viewer.getTable();
		Point location = table.toDisplay(0, 0);
		// 定位横向显示位置
		TableColumn[] columns = table.getColumns();
		for (int i = 0; i < columns.length; i++) {
			if (columns[i] != column) {
				location.x += (columns[i].getWidth());
			} else {
				break;
			}
		}
		location.y += table.getItemHeight();
		columnLocator.setLocation(location);
		columnLocator.open();
		// shell.setLocation(location);
	}

	@Override
	public void setFocus() {

	}

	@Override
	public void dispose() {
		columnLocator.dispose();
		super.dispose();
	}
}
