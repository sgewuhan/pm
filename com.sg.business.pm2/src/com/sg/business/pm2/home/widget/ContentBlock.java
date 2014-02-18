package com.sg.business.pm2.home.widget;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
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
	private Label head;
	private Label body;
	private Label foot;
	protected Composite mask;
	private Composite hoverMask;
	private Label hoverHead;
	private Label hoverBody;
	private Label hoverFoot;

	public ContentBlock(Composite parent) {
		super(parent, SWT.NONE);
		setLayout(new FormLayout());
		// 判断image是否为空
		createGraphicTextBlock();
	}

	private void createGraphicTextBlock() {
		// 背景图片
		imageLabel = new Label(this, SWT.NONE);

		// 带背景色的文本
		head = new Label(this, SWT.NONE);
		head.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		head.setData(MarkupValidator.MARKUP_VALIDATION_DISABLED, Boolean.TRUE);

		FormData fd = new FormData();
		head.setLayoutData(fd);
		fd.left = new FormAttachment(0);
		fd.right = new FormAttachment(100);
		fd.top = new FormAttachment(0);
		fd.height = 38;

		body = new Label(this, SWT.NONE);
		body.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		body.setData(MarkupValidator.MARKUP_VALIDATION_DISABLED, Boolean.TRUE);

		fd = new FormData();
		body.setLayoutData(fd);
		fd.left = new FormAttachment(0);
		fd.right = new FormAttachment(100);
		fd.top = new FormAttachment(head);
		fd.bottom = new FormAttachment(100, -16);

		foot = new Label(this, SWT.NONE);
		foot.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		foot.setData(MarkupValidator.MARKUP_VALIDATION_DISABLED, Boolean.TRUE);

		fd = new FormData();
		foot.setLayoutData(fd);
		fd.left = new FormAttachment(0);
		fd.right = new FormAttachment(100);
		fd.top = new FormAttachment(body);
		fd.bottom = new FormAttachment(100);

		hoverMask = new Composite(this, SWT.NONE);
		hoverMask.setData(RWT.CUSTOM_VARIANT, "hovermask");
		fd = new FormData();
		hoverMask.setLayoutData(fd);
		fd.left = new FormAttachment(0);
		fd.right = new FormAttachment(100);
		fd.top = new FormAttachment(0);
		fd.bottom = new FormAttachment(100);
		hoverMask.setLayout(new FormLayout());

		hoverHead = new Label(hoverMask, SWT.NONE);
		hoverHead.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		hoverHead.setData(MarkupValidator.MARKUP_VALIDATION_DISABLED,
				Boolean.TRUE);

		fd = new FormData();
		hoverHead.setLayoutData(fd);
		fd.left = new FormAttachment(0);
		fd.right = new FormAttachment(100);
		fd.top = new FormAttachment(0);
		fd.height = 38;
		
		

		hoverBody = new Label(hoverMask, SWT.NONE);
		hoverBody.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		hoverBody.setData(MarkupValidator.MARKUP_VALIDATION_DISABLED,
				Boolean.TRUE);

		fd = new FormData();
		hoverBody.setLayoutData(fd);
		fd.left = new FormAttachment(0);
		fd.right = new FormAttachment(100);
		fd.top = new FormAttachment(hoverHead);
		fd.bottom = new FormAttachment(100, -16);

		hoverFoot = new Label(hoverMask, SWT.NONE);
		hoverFoot.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		hoverFoot.setData(MarkupValidator.MARKUP_VALIDATION_DISABLED,
				Boolean.TRUE);

		fd = new FormData();
		hoverFoot.setLayoutData(fd);
		fd.left = new FormAttachment(0);
		fd.right = new FormAttachment(100);
		fd.top = new FormAttachment(hoverBody);
		fd.bottom = new FormAttachment(100);
		
		Label hoverLabel = new Label(hoverMask,SWT.NONE);
		hoverLabel.setData(RWT.CUSTOM_VARIANT, "hovermask");
		fd = new FormData();
		hoverLabel.setLayoutData(fd);
		fd.left = new FormAttachment(0);
		fd.right = new FormAttachment(100);
		fd.top = new FormAttachment(0);
		fd.bottom = new FormAttachment(100);
		hoverLabel.moveAbove(null);
		hoverLabel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				mouseClick();
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
			}
		});

		hoverMask.moveAbove(null);
	}

	protected void mouseClick() {
		
	}

	public void setCoverImage(Image image) {
		if (image == null) {
			return;
		}
		// setBackgroundMode(SWT.INHERIT_DEFAULT);

		Image sImage = ImageUtil.scaleFitImage(image, blockSize, blockSize);
		Rectangle scaleBounds = sImage.getBounds();
		imageLabel.setImage(sImage);
		FormData fd = new FormData();
		fd.top = new FormAttachment(0, blockSize / 2 - scaleBounds.height / 2);
		fd.left = new FormAttachment(0, blockSize / 2 - scaleBounds.width / 2);
		imageLabel.setLayoutData(fd);
		imageLabel.moveBelow(null);

		mask = new Composite(this, SWT.NONE);
		fd = new FormData();
		mask.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		fd.bottom = new FormAttachment(100);
		fd.right = new FormAttachment(100);
		mask.setData(RWT.CUSTOM_VARIANT, "bluemask");
		mask.moveAbove(imageLabel);
		// mask.setBackground(new Color(getDisplay(),0x00,0x99,0xcc));

	}

	public void setHeadText(String content) {
		head.setText(content);
	}

	public void setBodyText(String content) {
		body.setText(content);
	}

	public void setFootText(String content) {
		foot.setText(content);
	}

	public void setHoverHeadText(String content) {
		hoverHead.setText(content);
	}

	public void setHoverBodyText(String content) {
		hoverBody.setText(content);
	}

	public void setHoverFootText(String content) {
		hoverFoot.setText(content);
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}
}
