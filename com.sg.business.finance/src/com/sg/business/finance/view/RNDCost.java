package com.sg.business.finance.view;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.jface.fieldassist.ContentProposal;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.rap.addons.autosuggest.AutoSuggest;
import org.eclipse.rap.addons.autosuggest.ColumnDataProvider;
import org.eclipse.rap.addons.autosuggest.ColumnTemplate;
import org.eclipse.rap.addons.autosuggest.DataSource;
import org.eclipse.rap.rwt.scripting.ClientListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
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

	class DataSourceProposal implements IContentProposalProvider {

		public DataSourceProposal() {
		}

		@Override
		public IContentProposal[] getProposals(String contents, int position) {
			ArrayList<ContentProposal> result = new ArrayList<ContentProposal>();
			for (int i = 0; i < suggestArray.length; i++) {
				for (int j = 0; j < suggestArray[i].length; j++) {
					if (suggestArray[i][j].startsWith(contents)) {
						ContentProposal cp = new ContentProposal(
								suggestArray[i][0], suggestArray[i][1]) {
							@Override
							public String getLabel() {
								return getContent() + "|" + getDescription();
							}
						};
						result.add(cp);
					} else {
						String alphaValue = Utils.getAlphaString(
								suggestArray[i][j]).toLowerCase();
						if (alphaValue.startsWith(contents)) {
							ContentProposal cp = new ContentProposal(
									suggestArray[i][0], suggestArray[i][1]) {
								@Override
								public String getLabel() {
									return getContent() + "|"
											+ getDescription();
								}
							};
							result.add(cp);
						}
					}
				}
			}
			return result.toArray(new IContentProposal[0]);
		}

	}

	private TableViewer viewer;
	private Shell columnLocator;
	private DataSource dataSource;
	private String[][] suggestArray;
	private Text inputCode;

	public RNDCost() {

	}

	@Override
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.FULL_SELECTION);
		viewer.getTable().setHeaderVisible(true);
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		createColumns();

		createColumnLocator();
		
		createKeyBinding();
	}

	private void createKeyBinding() {
//		Display display = getSite().getShell().getDisplay();
//		
//		keys = (String[]) display.getData(RWT.ACTIVE_KEYS);
//		String[] newkeys = Utils.addElementToArray(keys, "CTRL+F3", true, String.class);
//		display.setData( RWT.ACTIVE_KEYS, newkeys );
//		display.addFilter( SWT.KeyDown, new Listener() {
//		  public void handleEvent( Event event ) {
//		    if( event.stateMask == SWT.CTRL && event.keyCode == SWT.F3 ) {
//		      showInput();
//		    }
//		  }
//		} );		
	}

	private void createColumnLocator() {
		columnLocator = new Shell(getSite().getShell(), SWT.BORDER);
		columnLocator.setLayout(new FormLayout());
		inputCode = new Text(columnLocator, SWT.BORDER);
		FormData fd = new FormData();
		inputCode.setLayoutData(fd);
		fd.left = new FormAttachment(0, 1);
		fd.top = new FormAttachment(0, 1);
		fd.right = new FormAttachment(100, -1);
		fd.bottom = new FormAttachment(100, -1);
		fd.width = 120;
		columnLocator.pack();

		createSuggest();

		if (Utils.isIE8Client()) {
			IContentProposalProvider pp = new DataSourceProposal();

			ContentProposalAdapter cpa = new ContentProposalAdapter(inputCode,
					new TextContentAdapter(), pp, null,null);
			cpa.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);

		} else {
			String scriptCode = Utils.getClientListener(Utils.TYPE_INTEGER);
			if (scriptCode != null) {
				ClientListener clientListener = new ClientListener(scriptCode);
				inputCode.addListener(SWT.Verify, clientListener);
				inputCode.addListener(SWT.Modify, clientListener);
			}

			createDataSource();
			AutoSuggest autoSuggest = new AutoSuggest(inputCode);
			autoSuggest.setDataSource(dataSource);

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

		inputCode.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.character == '\r') {
					columnLocator.setVisible(false);
					scrollColumn(inputCode.getText());
				}
			}
		});
	}

	protected void scrollColumn(String text) {
		TableColumn[] columns = viewer.getTable().getColumns();
		for (int i = 0; i < columns.length; i++) {
			String number = (String) columns[i].getData("accountNumber");
			if(number.equals(text)){
				viewer.getTable().showColumn(columns[i]);
			}
		}
	}

	private void createDataSource() {
		if (dataSource != null) {
			return;
		}
		dataSource = new DataSource();
		dataSource.setTemplate(new ColumnTemplate(120, 200)); // the column
																// widths
		ColumnDataProvider dataProvider = new ColumnDataProvider() {
			public Iterable<?> getSuggestions() {
				return Arrays.asList(suggestArray);
			}

			public String getValue(Object element) {
				String[] value = (String[]) element;
				return value[0];
			}

			public String[] getTexts(Object element) {
				String[] value = (String[]) element;
				return new String[] { value[0], value[1] };
			}
		};
		dataSource.setDataProvider(dataProvider);
	}

	private void createSuggest() {
		TableColumn[] columns = viewer.getTable().getColumns();
		suggestArray = new String[columns.length][2];
		for (int i = 0; i < columns.length; i++) {
			suggestArray[i][0] = (String) columns[i].getData("accountNumber");
			suggestArray[i][1] = (String) columns[i].getData("accountName");
		}
	}

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
				showInput();
			}
		});
	}

	protected void showInput() {

		Table table = viewer.getTable();
		Point location = table.toDisplay(0, 0);
		// 定位横向显示位置
//		TableColumn[] columns = table.getColumns();
//		for (int i = 0; i < columns.length; i++) {
//			if (columns[i] != column) {
//				location.x += (columns[i].getWidth());
//			} else {
//				break;
//			}
//		}
		location.y += 60;

		if (columnLocator.isDisposed() || columnLocator == null) {
			createColumnLocator();
		}
		columnLocator.setLocation(location);
		if (!columnLocator.isVisible()) {
			columnLocator.setVisible(true);
		} else {
			columnLocator.open();
		}
		inputCode.setFocus();
		inputCode.selectAll();
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
