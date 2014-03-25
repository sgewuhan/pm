package com.sg.business.commons.ui.home.perf;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

public class OverBudgetAndDelayTop10Block2 extends Composite {

	private OverBudgetTop10Page overBudgetPage;
	private DelayTop10Page delayPage;
	private Composite currentPage;

	public OverBudgetAndDelayTop10Block2(Composite parent) {
		super(parent, SWT.NONE);
		createContent(this);
		setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
	}

	private void createContent(Composite parent) {
		parent.setLayout(new FormLayout());
		overBudgetPage = new OverBudgetTop10Page(parent) {
			@Override
			protected void go() {
				expand(overBudgetPage);
			}
		};
		delayPage = new DelayTop10Page(parent) {
			@Override
			protected void go() {
				expand(delayPage);
			}
		};

		expand(overBudgetPage);

	}

	private void expand(Composite item) {
		if ((item == overBudgetPage && overBudgetPage != currentPage)
				|| (item == delayPage && delayPage == currentPage)) {
			showOverBudgetPage();
		} else {
			showDelayPage();
		}
		layout();
	}

	private void showOverBudgetPage() {
		FormData fd = new FormData();
		delayPage.setLayoutData(fd);
		fd.bottom = new FormAttachment(100);
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.height = DelayTop10Page.HEAD_HEIGHT;

		fd = new FormData();
		overBudgetPage.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(delayPage);
		currentPage = overBudgetPage;
		overBudgetPage.setHoverEnable(false);
		delayPage.setHoverEnable(true);

	}

	private void showDelayPage() {
		FormData fd = new FormData();
		overBudgetPage.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.height = OverBudgetTop10Page.HEAD_HEIGHT;

		fd = new FormData();
		delayPage.setLayoutData(fd);
		fd.top = new FormAttachment(overBudgetPage);
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(100);
		currentPage = delayPage;
		overBudgetPage.setHoverEnable(true);
		delayPage.setHoverEnable(false);
	}

}
