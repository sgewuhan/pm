package com.sg.business.pm2.home.widget;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.mobnut.commons.util.file.ImageUtil;

public class ContentBlock extends Composite {

	private int blockSize;

	public ContentBlock(Composite parent, Image image, String title,
			int blockSize) {
		super(parent, SWT.NONE);
		setLayout(new FormLayout());
		this.blockSize = blockSize;
		// 判断image是否为空
		if (image != null) {
			// 创建图文块
			createGraphicTextBlock(image, title);
		} else {
			// 创建文本块
			createTextBlock(title);
		}
	}

	private void createTextBlock(String title) {
		// TODO Auto-generated method stub

	}

	private void createGraphicTextBlock(Image image, String title) {
		// 背景图片
		Label imageLabel = new Label(this, SWT.NONE);

		Image scaleFitImage = ImageUtil.scaleFitImage(image, blockSize,
				blockSize);
		Rectangle scaleBounds = scaleFitImage.getBounds();

		imageLabel.setImage(scaleFitImage);
		FormData labelLayoutData = new FormData();
		labelLayoutData.top = new FormAttachment(0, blockSize / 2
				- scaleBounds.height / 2);
		labelLayoutData.left = new FormAttachment(0, blockSize / 2
				- scaleBounds.width / 2);

		imageLabel.setLayoutData(labelLayoutData);

		// 带背景色的文本
		Label titleLabel = new Label(this, SWT.NONE);
		titleLabel.setText(title);
		
		

	}
}
