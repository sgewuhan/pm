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
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class SyncHROrganizationDialog extends Dialog {

	private String title;
	private TreeViewer newOrgTree;
	private TreeViewer removeOrgTree;
	private TableViewer renameOrgTable;

	protected SyncHROrganizationDialog(Shell parentShell) {
		super(parentShell);
		this.title = "HR组织数据同步";
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		if (title != null) {
			shell.setText(title);
		}
		// Workaround for RWT Text Size Determination
		shell.addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event event) {
				initializeBounds();
			}
		});
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "确认", false);
		createButton(parent, IDialogConstants.CANCEL_ID, "取消", true);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite content = (Composite) super.createDialogArea(parent);
		content.setLayout(new FormLayout());
		Label messageLabel = new Label(content, SWT.NONE);
		messageLabel.setText("请勾选需同步的组织");
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
		fd.bottom = new FormAttachment(100,-10);
		return content;
	}

	private void createTabItemOrgToBeRename(TabFolder tab) {
		TabItem tabItem = new TabItem(tab, SWT.NONE);
		tabItem.setText("HR中更名的组织");
		Composite panel = new Composite(tab, SWT.NONE);
		renameOrgTable = createTable(panel);
		
		tabItem.setControl(panel);
	}


	private void createTabItemOrgToBeRemove(TabFolder tab) {
		TabItem tabItem = new TabItem(tab, SWT.NONE);
		tabItem.setText("HR中移除的组织");
		Composite panel = new Composite(tab, SWT.NONE);
		removeOrgTree = createTree(panel);

		
		tabItem.setControl(panel);
	}
	

	private TableViewer createTable(Composite panel) {
		TableViewer tableViewer = new TableViewer(panel,SWT.CHECK);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setLabelProvider(new OrgExchangeLabelProvider());
		
		
		return tableViewer;
	}

	private void createTabItemOrgToBeInsert(TabFolder tab) {
		TabItem tabItem = new TabItem(tab, SWT.NONE);
		tabItem.setText("HR中的新组织");
		Composite panel = new Composite(tab, SWT.NONE);
		newOrgTree = createTree(panel);
		
		tabItem.setControl(panel);
	}

	private TreeViewer createTree(Composite panel) {
		TreeViewer treeViewer = new TreeViewer(panel,SWT.CHECK);
		treeViewer.getTree().setLinesVisible(true);
		treeViewer.setLabelProvider(new OrgExchangeLabelProvider());
		treeViewer.setContentProvider(new ITreeContentProvider() {
			
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}
			
			@Override
			public void dispose() {
				
			}
			
			@Override
			public boolean hasChildren(Object element) {
				if(element instanceof OrgExchange){
					OrgExchange orgExchange = (OrgExchange) element;
					return !orgExchange.getChildren().isEmpty();
				}
				return false;
			}
			
			@Override
			public Object getParent(Object element) {
				if(element instanceof OrgExchange){
					OrgExchange orgExchange = (OrgExchange) element;
					return orgExchange.getParent();
				}
				return null;
			}
			
			@Override
			public Object[] getElements(Object inputElement) {
				if(inputElement instanceof Set){
					@SuppressWarnings("unchecked")
					Set<OrgExchange> set = (Set<OrgExchange>) inputElement;
					return set.toArray(new OrgExchange[]{});
				}
				return null;
			}
			
			@Override
			public Object[] getChildren(Object parentElement) {
				if(parentElement instanceof OrgExchange){
					OrgExchange orgExchange = (OrgExchange) parentElement;
					Set<OrgExchange> children = orgExchange .getChildren();
					return children.toArray(new OrgExchange[]{});
				}
				return null;
			}
		});
		
		
		return treeViewer;
	}
	
	
	public void setInputForNewOrganization(Set<OrgExchange> input){
		newOrgTree.setInput(input);
	}
	
	public void setInputForRemoveOrganization(Set<OrgExchange> input){
		removeOrgTree.setInput(input);
	}
	
	public void setInputForRenameOrganization(Set<OrgExchange> input){
		renameOrgTable.setInput(input);
	}

}
