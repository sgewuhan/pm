package com.sg.business.commons.ui.block;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

import com.mobnut.commons.html.HtmlUtil;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.sg.business.commons.operation.link.BulletinBoardLinkAdapter;
import com.sg.business.model.BulletinBoard;
import com.sg.business.model.dataset.bulletinboard.BulletinBoardDataSet;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.block.Block;
import com.sg.widgets.commons.labelprovider.HTMLAdvanceLabelProvider;
import com.sg.widgets.commons.model.IEditorInputFactory;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.part.editor.DataObjectEditor;
import com.sg.widgets.registry.config.DataEditorConfigurator;

public class BulletinBoardBlock extends Block implements
		ISelectionChangedListener {

	private static final int ITEM_HIGHT = 64;
	private static final String PERSPECTIVE = "perspective.bulletinboard";
	private ListViewer viewer;
	private BulletinBoardDataSet bulletinBoardDataFactory;

	public BulletinBoardBlock(Composite parent) {
		super(parent);
	}

	@Override
	protected void createContent(final Composite parent) {
		init();
		
		parent.setLayout(new FormLayout());
		List list = new List(parent,SWT.SINGLE);
		list.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent event) {
				viewer.setSelection(new StructuredSelection(new Object[]{}));
			}
		});
		list.addSelectionListener(new BulletinBoardLinkAdapter());
		viewer = new ListViewer(list);
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		HTMLAdvanceLabelProvider labelProvider = new HTMLAdvanceLabelProvider();
		labelProvider.setViewer(viewer);
		viewer.setLabelProvider(labelProvider);
		viewer.setUseHashlookup(true);
		viewer.addSelectionChangedListener(this);

		list.setData(RWT.CUSTOM_ITEM_HEIGHT, new Integer(ITEM_HIGHT));
		HtmlUtil.enableMarkup(list);
		FormData fd = new FormData();
		list.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		fd.bottom = new FormAttachment(100,-ITEM_HIGHT);
		fd.right = new FormAttachment(100);

		Button button = new Button(parent,SWT.PUSH);
		button.setData(RWT.CUSTOM_VARIANT, "metro_darkgray2");
		button.setImage(BusinessResource.getImage(BusinessResource.IMAGE_ADD_G_24));
		fd = new FormData();
		button.setLayoutData(fd);
		fd.bottom = new FormAttachment(100,-8);
		fd.right = new FormAttachment(100,-8);
		fd.height = 24;
		fd.width = 24;
		button.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				BulletinBoard bulletinboard = ModelService.createModelObject(
						new BasicDBObject(), BulletinBoard.class);
				try {
					DataObjectDialog.openDialog(bulletinboard,
							BulletinBoard.EDITOR_CREATE, true, null);
				} catch (Exception exception) {
					MessageUtil.showToast(exception);
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		
		doRefresh();
	}
	
	
	private void init() {
		bulletinBoardDataFactory = new BulletinBoardDataSet();
	}

	@Override
	protected Object doGetData() {
		int limit = getContentHeight() / ITEM_HIGHT - 1;
		bulletinBoardDataFactory.setLimit(limit);
		java.util.List<PrimaryObject> items = bulletinBoardDataFactory
				.getDataSet().getDataItems();
		java.util.List<Object> input = new ArrayList<Object>();
		input.addAll(items);
		return input;
	}

	
	@Override
	protected void doDisplayData(Object data) {
		HTMLAdvanceLabelProvider l = (HTMLAdvanceLabelProvider) viewer.getLabelProvider();
		l.setWidthHint(viewer.getControl().getBounds().width);
		viewer.setInput(data);
	}

	public int getContentHeight() {
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
