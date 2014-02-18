package com.sg.business.pm2.home.widget;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.widgets.MarkupValidator;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.mobnut.commons.util.file.ImageUtil;

@SuppressWarnings("restriction")
public class ContentBlock extends Composite {

	protected int blockSize;
	private Label imageLabel;
	private Label titleLabel;

	public ContentBlock(Composite parent) {
		super(parent, SWT.NONE);
		setLayout(new FormLayout());
		// 判断image是否为空
		createGraphicTextBlock( );
	}


	private void createGraphicTextBlock() {
		// 背景图片
		imageLabel = new Label(this, SWT.NONE);

		// 带背景色的文本
		titleLabel = new Label(this, SWT.NONE);
		titleLabel.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		titleLabel.setData(MarkupValidator.MARKUP_VALIDATION_DISABLED,
				Boolean.TRUE);

		FormData fd = new FormData();
		titleLabel.setLayoutData(fd);
		fd.bottom = new FormAttachment(100);
		fd.left = new FormAttachment(0);
		fd.right = new FormAttachment(100);
		fd.top = new FormAttachment(0);
	}

	public void setCoverImage(Image image) {
		if(image == null){
			return;
		}
		Image sImage = ImageUtil.scaleFitImage(image, blockSize, blockSize);
		Rectangle scaleBounds = sImage.getBounds();
		imageLabel.setImage(sImage);
		FormData fd = new FormData();
		fd.top = new FormAttachment(0, blockSize / 2 - scaleBounds.height / 2);
		fd.left = new FormAttachment(0, blockSize / 2 - scaleBounds.width / 2);
		imageLabel.setLayoutData(fd);
	}

	public void setContentText(String content) {
		titleLabel.setText(content);
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}
}
