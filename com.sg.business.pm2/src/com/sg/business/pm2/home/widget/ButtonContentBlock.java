package com.sg.business.pm2.home.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class ButtonContentBlock extends Composite {

	protected int blockSize;
	private Button button;

	public ButtonContentBlock(Composite parent) {
		super(parent, SWT.NONE);
		setLayout(new FormLayout());
		// ÅÐ¶ÏimageÊÇ·ñÎª¿Õ
		createButtonBlock();
	}

	private void createButtonBlock() {
		// ±³¾°Í¼Æ¬
		FormData fd = new FormData();
		button = new Button(this, SWT.PUSH);
		button.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		fd.bottom = new FormAttachment(100);
		fd.right = new FormAttachment(100);
		button.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				mouseClick();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

	}

	protected void mouseClick() {

	}

	public void setCoverImage(Image image) {
		if (image == null) {
			return;
		}
		button.setImage(image);
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}
}
