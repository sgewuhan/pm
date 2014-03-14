package com.sg.sales.ui.block;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

import com.mobnut.commons.html.HtmlUtil;
import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.design.ICSSConstants;
import com.sg.sales.model.Contact;
import com.sg.sales.model.dataset.MyContactDataSet;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.block.list.ListBlock;
import com.sg.widgets.commons.labelprovider.HTMLAdvanceLabelProvider;
import com.sg.widgets.part.editor.DataObjectEditor;

public class ContactBlock extends ListBlock {

	private static final String PERSPECTIVE = "sales.contact";
	private MyContactDataSet dataset;

	public ContactBlock(Composite parent) {
		super(parent);
	}

	@Override
	protected void createContent(final Composite parent) {
		init();

		parent.setLayout(new FormLayout());
		List list = new List(parent, SWT.SINGLE);
		list.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent event) {
				viewer.setSelection(new StructuredSelection(new Object[] {}));
			}
		});
		viewer = new ListViewer(list);
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		HTMLAdvanceLabelProvider labelProvider = getLabelProvider();
		labelProvider.setKey("inlist");
		labelProvider.setViewer(viewer);
		viewer.setLabelProvider(labelProvider);
		viewer.setUseHashlookup(true);
		viewer.addSelectionChangedListener(this);

		HtmlUtil.enableMarkup(list);
		list.setData(RWT.CUSTOM_ITEM_HEIGHT, new Integer(ITEM_HIGHT));
		FormData fd = new FormData();

		list.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		fd.bottom = new FormAttachment(100);
		fd.right = new FormAttachment(100, -24);

		Composite nameFilter = createNameFilter(parent);
		fd = new FormData();
		nameFilter.setLayoutData(fd);
		fd.left = new FormAttachment(list);
		fd.right = new FormAttachment(100);
		fd.top = new FormAttachment();
		fd.bottom = new FormAttachment(100);

		doRefresh();
	}

	private Composite createNameFilter(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setBackground(Widgets
				.getColor(getDisplay(), 0xcd, 0xcd, 0xcd));
		RowLayout layout = new RowLayout(SWT.VERTICAL);
		container.setLayout(layout);
		layout.fill = true;
		layout.marginBottom = 0;
		layout.marginLeft = 0;
		layout.marginRight = 0;
		layout.marginTop = 0;
		layout.spacing = 0;
		layout.wrap = false;
		layout.pack = false;

		for (int i = 0; i < 26; i++) {
			createFilterButton(container, new String(new byte[] { new Integer(
					65 + i).byteValue() }));
		}
		createFilterButton(container, "#");
		return container;
	}

	private void createFilterButton(Composite parent, final String alpha) {
		Button b = new Button(parent, SWT.PUSH);
		b.setData(RWT.CUSTOM_VARIANT, ICSSConstants.SMALL_WHITE);
		b.setText(alpha);
		RowData rd = new RowData();
		b.setLayoutData(rd);
		rd.width = 24;
		b.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				filter(alpha);
			}
		});
	}

	protected void filter(final String alpha) {
		if(alpha.equals("#")){
			viewer.resetFilters();
			return;
		}
		viewer.setFilters(new ViewerFilter[] { new ViewerFilter() {

			@Override
			public boolean select(Viewer viewer, Object parentElement,
					Object element) {
				if(element instanceof Contact){
					Contact contact = (Contact) element;
					String as = Utils.getAlphaString(contact.getFirstName());
					if(as.toUpperCase().startsWith(alpha)){
						return true;
					}
					
					as = Utils.getAlphaString(contact.getLastName());
					if(as.toUpperCase().startsWith(alpha)){
						return true;
					}
				}
				return false;
			}
		} });
	}

	@Override
	protected void init() {
		dataset = new MyContactDataSet();
		super.init();
	}

	@Override
	protected Object doGetData() {
		int limit = getCountByHeight();
		dataset.setLimit(limit);
		return dataset.getDataSet().getDataItems();
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		ISelection selection = event.getSelection();
		if (selection == null || selection.isEmpty()
				|| (!(selection instanceof IStructuredSelection))) {
		} else {
			Object element = ((IStructuredSelection) selection)
					.getFirstElement();
			select(element);
		}
	}

	@Override
	protected HTMLAdvanceLabelProvider getLabelProvider() {
		return new HTMLAdvanceLabelProvider();
	}

	@Override
	protected String getPerspective() {
		return PERSPECTIVE;
	}

	@Override
	protected void select(Object element) {
		if (element instanceof PrimaryObject) {
			PrimaryObject primaryObject = (PrimaryObject) element;
			try {
				String editorId = "sales.contact.editor";
				DataObjectEditor.open(primaryObject, editorId, true, null);
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}
		}
	}

}
