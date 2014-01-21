package com.sg.business.organization.command;

import java.util.Set;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.TreeItem;

import com.sg.business.resource.nls.Messages;

public class SyncHROrganizationDialog extends Dialog {

	private String title;
	private TreeViewer newOrgTree;
	private TreeViewer removeOrgTree;
	private TableViewer renameOrgTable;
	private Set<OrgExchange> newOrgTreeInput;
	private Set<OrgExchange> removeOrgTreeInput;
	private Set<OrgExchange> renameOrgTableInput;

	protected SyncHROrganizationDialog(Shell parentShell) {
		super(parentShell);
		this.title = Messages.get().SyncHROrganizationDialog_0;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		if (title != null) {
			shell.setText(title);
		}
		// Workaround for RWT Text Size Determination
		// shell.addListener(SWT.Resize, new Listener() {
		// public void handleEvent(Event event) {
		// initializeBounds();
		// }
		// });
	}

	@Override
	protected Point getInitialSize() {
		return new Point(600, 600);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.get().SyncHROrganizationDialog_1, false);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.get().SyncHROrganizationDialog_2, true);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite content = (Composite) super.createDialogArea(parent);
		content.setLayout(new FormLayout());
		Label messageLabel = new Label(content, SWT.NONE);
		messageLabel.setText(Messages.get().SyncHROrganizationDialog_3);
		FormData fd = new FormData();
		messageLabel.setLayoutData(fd);
		fd.top = new FormAttachment(0, 10);
		fd.left = new FormAttachment(0, 10);
		fd.right = new FormAttachment(100, -10);

		TabFolder tab = new TabFolder(content, SWT.NONE);
		createTabItemOrgToBeInsert(tab);
		createTabItemOrgToBeRemove(tab);
		createTabItemOrgToBeRename(tab);

		fd = new FormData();
		tab.setLayoutData(fd);
		fd.top = new FormAttachment(messageLabel, 10);
		fd.left = new FormAttachment(0, 10);
		fd.right = new FormAttachment(100, -10);
		fd.bottom = new FormAttachment(100, -10);
		return content;
	}

	private void createTabItemOrgToBeRename(TabFolder tab) {
		TabItem tabItem = new TabItem(tab, SWT.NONE);
		tabItem.setText(Messages.get().SyncHROrganizationDialog_4);
		Composite panel = new Composite(tab, SWT.NONE);
		panel.setLayout(new FillLayout());
		renameOrgTable = createTable(panel);
		renameOrgTable.setInput(renameOrgTableInput);
		tabItem.setControl(panel);
	}

	private void createTabItemOrgToBeRemove(TabFolder tab) {
		TabItem tabItem = new TabItem(tab, SWT.NONE);
		tabItem.setText(Messages.get().SyncHROrganizationDialog_5);
		Composite panel = new Composite(tab, SWT.NONE);
		panel.setLayout(new FillLayout());
		removeOrgTree = createTree(panel,SWT.NONE);
		removeOrgTree.setInput(removeOrgTreeInput);

		tabItem.setControl(panel);
	}

	private void createTabItemOrgToBeInsert(TabFolder tab) {
		TabItem tabItem = new TabItem(tab, SWT.NONE);
		tabItem.setText(Messages.get().SyncHROrganizationDialog_6);
		Composite panel = new Composite(tab, SWT.NONE);
		panel.setLayout(new FillLayout());
		newOrgTree = createTree(panel,SWT.CHECK);
		newOrgTree.setInput(newOrgTreeInput);
		tabItem.setControl(panel);
	}

	private TreeViewer createTree(Composite panel, int style) {
		TreeViewer treeViewer = new TreeViewer(panel, style);
		treeViewer.getTree().setLinesVisible(true);
		treeViewer.setLabelProvider(new OrgExchangeLabelProvider());
		treeViewer.setContentProvider(new ITreeContentProvider() {

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
			}

			@Override
			public void dispose() {

			}

			@Override
			public boolean hasChildren(Object element) {
				if (element instanceof OrgExchange) {
					OrgExchange orgExchange = (OrgExchange) element;
					return !orgExchange.getChildren().isEmpty();
				}
				return false;
			}

			@Override
			public Object getParent(Object element) {
				if (element instanceof OrgExchange) {
					OrgExchange orgExchange = (OrgExchange) element;
					return orgExchange.getParent();
				}
				return null;
			}

			@Override
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof Set) {
					@SuppressWarnings("unchecked")
					Set<OrgExchange> set = (Set<OrgExchange>) inputElement;
					return set.toArray(new OrgExchange[] {});
				}
				return null;
			}

			@Override
			public Object[] getChildren(Object parentElement) {
				if (parentElement instanceof OrgExchange) {
					OrgExchange orgExchange = (OrgExchange) parentElement;
					Set<OrgExchange> children = orgExchange.getChildren();
					return children.toArray(new OrgExchange[] {});
				}
				return null;
			}
		});
		treeViewer.getTree().addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem treeItem = (TreeItem) e.item;
				((OrgExchange)treeItem.getData()).setCheckHR(treeItem.getChecked());
				if (treeItem.getChecked()) {
					TreeItem parentItem = treeItem.getParentItem();
					while(parentItem != null){
						parentItem.setChecked(true);
						((OrgExchange)parentItem.getData()).setCheckHR(true);
						parentItem= parentItem.getParentItem();
					}
				} else {
					unCheck(treeItem);
				}
			}

			private void unCheck(TreeItem treeItem) {
				for (TreeItem childrenTreeItem : treeItem.getItems()) {
					childrenTreeItem.setChecked(false);
					((OrgExchange)childrenTreeItem.getData()).setCheckHR(false);
					unCheck(childrenTreeItem);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		return treeViewer;
	}

	private TableViewer createTable(Composite panel) {
		TableViewer tableViewer = new TableViewer(panel, SWT.CHECK);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setLabelProvider(new OrgExchangeLabelProvider());

		return tableViewer;
	}

	public void setInputForNewOrganization(Set<OrgExchange> input) {

		newOrgTreeInput = input;
	}

	public void setInputForRemoveOrganization(Set<OrgExchange> input) {
		removeOrgTreeInput = input;
	}

	public void setInputForRenameOrganization(Set<OrgExchange> input) {
		renameOrgTableInput = input;
	}

}
