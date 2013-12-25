package com.sg.business.finance.rndcost;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.jface.fieldassist.ContentProposal;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.rap.addons.autosuggest.AutoSuggest;
import org.eclipse.rap.addons.autosuggest.ColumnDataProvider;
import org.eclipse.rap.addons.autosuggest.ColumnTemplate;
import org.eclipse.rap.addons.autosuggest.DataSource;
import org.eclipse.rap.rwt.scripting.ClientListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.mobnut.commons.util.Utils;

public class ColumnLocator extends Shell {
	class DataSourceProposal implements IContentProposalProvider {

		public DataSourceProposal() {
		}

		@Override
		public IContentProposal[] getProposals(String contents, int position) {
			ArrayList<ContentProposal> result = new ArrayList<ContentProposal>();
			for (int i = 0; i < suggestArray.length; i++) {
				for (int j = 0; j < suggestArray[i].length; j++) {
					if (suggestArray[i][j].contains(contents)) {
						ContentProposal cp = new ContentProposal(
								suggestArray[i][0], suggestArray[i][1]) {
							@Override
							public String getLabel() {
								return getContent() + "|" + getDescription(); //$NON-NLS-1$
							}
						};
						result.add(cp);
					} else {
						String alphaValue = Utils.getAlphaString(
								suggestArray[i][j]).toLowerCase();
						if (alphaValue.contains(contents)) {
							ContentProposal cp = new ContentProposal(
									suggestArray[i][0], suggestArray[i][1]) {
								@Override
								public String getLabel() {
									return getContent() + "|" //$NON-NLS-1$
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
	
	private String[][] suggestArray;

	private Text inputCode;
	private Table table;
	private DataSource dataSource;

	public ColumnLocator(Shell shell, Table table,String[][] suggestArray) {
		super(shell,SWT.BORDER);
		this.table = table;
		this.suggestArray = suggestArray;
		
		setLayout(new FormLayout());
		inputCode = new Text(this, SWT.BORDER);
		FormData fd = new FormData();
		inputCode.setLayoutData(fd);
		fd.left = new FormAttachment(0, 1);
		fd.top = new FormAttachment(0, 1);
		fd.right = new FormAttachment(100, -1);
		fd.bottom = new FormAttachment(100, -1);
		fd.width = 120;
		this.pack();

		if (Utils.isIE8Client()) {
			IContentProposalProvider pp = new DataSourceProposal();

			ContentProposalAdapter cpa = new ContentProposalAdapter(inputCode,
					new TextContentAdapter(), pp, null, null);
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

			this.addShellListener(new ShellListener() {

				@Override
				public void shellDeactivated(ShellEvent e) {
					ColumnLocator.this.setVisible(false);
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
					ColumnLocator.this.setVisible(false);
					scrollColumn(inputCode.getText());
				}
			}
		});
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
	
	
	public void activate() {
		inputCode.setFocus();
		inputCode.selectAll();		
	}
	

	protected void scrollColumn(String text) {
		TableColumn[] columns = table.getColumns();
		for (int i = 0; i < columns.length; i++) {
			String number = (String) columns[i].getData("accountNumber"); //$NON-NLS-1$
			if (text.equals(number)) {
				table.showColumn(columns[i]);
			}
		}
	}

}
