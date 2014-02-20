package com.sg.business.pm2.home.widget;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

import com.sg.business.model.BulletinBoard;
import com.sg.business.model.dataset.bulletinboard.BulletinBoardDataSet;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.block.Block;
import com.sg.widgets.commons.labelprovider.HTMLAdvanceLabelProvider;
import com.sg.widgets.commons.model.IEditorInputFactory;
import com.sg.widgets.part.editor.DataObjectEditor;
import com.sg.widgets.registry.config.DataEditorConfigurator;

public class BulletinBoardBlock extends Block implements
		ISelectionChangedListener {

	private static final int ITEM_HIGHT = 36;
	private static final String PERSPECTIVE = "perspective.bulletinboard";
	private ListViewer viewer;

	public BulletinBoardBlock(Composite parent) {
		super(parent);
	}

	@Override
	protected void createContent(Composite parent) {
		parent.setLayout(new FormLayout());

		viewer = new ListViewer(parent, SWT.SINGLE);
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setLabelProvider(new HTMLAdvanceLabelProvider());
		viewer.setUseHashlookup(true);
		viewer.addSelectionChangedListener(this);

		List list = viewer.getList();
		list.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		list.setData(RWT.CUSTOM_ITEM_HEIGHT, new Integer(ITEM_HIGHT));
		FormData fd = new FormData();
		list.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		fd.bottom = new FormAttachment(100);
		fd.right = new FormAttachment(100);

		doRefresh();
	}

	@Override
	public void doRefresh() {
		BulletinBoardDataSet bulletinBoardDataFactory = new BulletinBoardDataSet();
		int limit = getContentHeight() / ITEM_HIGHT;
		bulletinBoardDataFactory.setLimit(limit);
		viewer.setInput(bulletinBoardDataFactory.getDataSet().getDataItems());
		super.doRefresh();
	}

	protected int getContentHeight() {
		return 265;
	}

	@Override
	protected void go() {
		try {
			IWorkbench workbench = PlatformUI.getWorkbench();
			IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
			workbench.showPerspective(PERSPECTIVE, window);
		} catch (WorkbenchException e) {
			MessageUtil.showToast(e);
		}
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		ISelection selection = event.getSelection();
		if (selection == null || selection.isEmpty()
				|| (!(selection instanceof IStructuredSelection))) {
		} else {
			Object element = ((IStructuredSelection) selection)
					.getFirstElement();
			if (element instanceof BulletinBoard) {
				BulletinBoard bulletinBoard = (BulletinBoard) element;
				IEditorInputFactory editorInputFactory = bulletinBoard
						.getAdapter(IEditorInputFactory.class);
				DataEditorConfigurator conf = editorInputFactory
						.getEditorConfig(null);
				try {
					DataObjectEditor.open(bulletinBoard, conf, true, null);
				} catch (Exception e) {
					MessageUtil.showToast(e);
				}

			}
		}
	}

}
