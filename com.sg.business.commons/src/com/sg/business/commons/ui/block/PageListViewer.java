package com.sg.business.commons.ui.block;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.sg.business.commons.ui.UIFrameworkUtils;
import com.sg.widgets.commons.labelprovider.HTMLAdvanceLabelProvider;

@SuppressWarnings({ "rawtypes" })
public class PageListViewer extends ListViewer {

	private int pageSize;
	private int currentPageIndex = 0;
	private List input;
	private List[] pagedInput;

	public PageListViewer(Composite parent, int style) {
		super(parent, style);
		setContentProvider(ArrayContentProvider.getInstance());
		Control control = getControl();
		UIFrameworkUtils.enableMarkup(control);
		control.setData(RWT.CUSTOM_ITEM_HEIGHT, 20);
		HTMLAdvanceLabelProvider labelProvider = new HTMLAdvanceLabelProvider();
		labelProvider.setKey("singleline");
		labelProvider.setViewer(this);
		setLabelProvider(labelProvider);
		control.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent event) {
				setSelection(new StructuredSelection(new Object[]{}));
			}
			
			@Override
			public void focusGained(FocusEvent event) {
			}
		});
	}
	
	@Override
	public HTMLAdvanceLabelProvider getLabelProvider() {
		return (HTMLAdvanceLabelProvider) super.getLabelProvider();
	}

	public void setPageSize(int pageSize) {
		Assert.isLegal(pageSize > 0);
		this.pageSize = pageSize;
		currentPageIndex = 0;
		calculatePagedInput();
	}

	public void setPageInput(List srcInput) {
		Assert.isNotNull(srcInput);
		this.input = srcInput;
		currentPageIndex = 0;
		calculatePagedInput();
		if (pagedInput.length > 0) {
			setInput(pagedInput[0]);
		}
	}

	@SuppressWarnings("unchecked")
	private void calculatePagedInput() {
		if (input != null && pageSize > 0) {
			pagedInput = new ArrayList[(int) (Math.ceil(1d * input.size()
					/ pageSize))];
			int pageNo = -1;
			for (int i = 0; i < input.size(); i++) {
				if (i % pageSize == 0) {
					pageNo++;
					pagedInput[pageNo] = new ArrayList();
				}
				pagedInput[pageNo].add(input.get(i));
			}
		}
	}

	public void pageNext() {
		if (canPageNext()) {
			currentPageIndex++;
			setInput(pagedInput[currentPageIndex]);
		}
	}

	public boolean canPageNext() {
		return (currentPageIndex + 1) < pagedInput.length;
	}

	public boolean canPageBack() {
		return (currentPageIndex - 1) >= 0;
	}

	public void pageBack() {
		if (canPageBack()) {
			currentPageIndex--;
			setInput(pagedInput[currentPageIndex]);
		}
	}

	public String getPageText() {
		return ""+(currentPageIndex+1)+"/"+pagedInput.length;
	}

}
