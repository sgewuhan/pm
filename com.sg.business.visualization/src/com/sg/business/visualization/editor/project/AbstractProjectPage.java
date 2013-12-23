package com.sg.business.visualization.editor.project;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.internal.widgets.IWidgetGraphicsAdapter;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import com.sg.business.model.Project;
import com.sg.business.model.etl.ProjectPresentation;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.NavigatorControl;
import com.sg.widgets.part.editor.IEditorActionListener;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.INavigatorPageBodyPartCreater;
import com.sg.widgets.part.editor.page.NavigatorPage;

@SuppressWarnings("restriction")
public abstract class AbstractProjectPage implements
		INavigatorPageBodyPartCreater {

	private static final int INFOBANNER_HEIGHT = 68;
	private static final int MARGIN = 4;
	Composite header;
	protected NavigatorControl navi;
	protected Project project;

	public AbstractProjectPage() {

	}

	@Override
	public void editorAction(IEditorActionListener reciever, int code,
			Object object) {
	}

	@Override
	public void createNavigatorBody(Composite body, NavigatorControl navi,
			PrimaryObjectEditorInput input, NavigatorPage navigatorPage) {
		this.project = (Project) input.getData();
		this.navi = navi;

		body.setLayout(new FormLayout());
		// 创建页头
		header = createHeader(body);
		FormData fd = new FormData();
		header.setLayoutData(fd);
		fd.top = new FormAttachment(0, 1);
		fd.left = new FormAttachment(0, 1);
		fd.right = new FormAttachment(100, -1);
		fd.height = INFOBANNER_HEIGHT;
		// // 创建分割线
		// Label line = new Label(body, SWT.NONE);
		// fd = new FormData();
		// line.setLayoutData(fd);
		// Color sepColor = Widgets.getColor(body.getDisplay(), 192, 192, 192);
		// line.setBackground(sepColor);
		// fd.top = new FormAttachment(header, 0);
		// fd.left = new FormAttachment(0, 1);
		// fd.right = new FormAttachment(100, -1);
		// fd.height = 1;

		// 创建内容区
		Composite navigator = createContent(body);
		fd = new FormData();
		navigator.setLayoutData(fd);
		fd.top = new FormAttachment(header, 0);
		fd.left = new FormAttachment(0, 1);
		fd.right = new FormAttachment(100, -1);
		fd.bottom = new FormAttachment(100, -1);

	}

	protected Composite createContent(Composite body) {
		SashForm content = new SashForm(body, SWT.HORIZONTAL);
		Composite tableContent = new Composite(content, SWT.NONE);
		navi.createPartContent(tableContent);

		Composite graphicContent = new Composite(content, SWT.NONE);
		graphicContent.setLayout(new FillLayout());
		createGraphic(graphicContent);

		content.setWeights(new int[] { 1, 1 });
		return content;
	}

	protected abstract void createGraphic(Composite parent);

	protected Composite createHeader(Composite body) {
		ProjectPresentation pres = project.getPresentation();

		String desc = pres.getDescriptionText();

		String coverImageURL = pres.getCoverImageURL();

		Composite header = new Composite(body, SWT.NONE);
		header.setLayout(new FormLayout());
		setBackgroundGradient(header);

		Label cover = new Label(header, SWT.NONE);
		cover.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);

		// 添加项目集合封面图片
		if (coverImageURL != null) {
			StringBuffer sb = new StringBuffer();
			sb.append("<img src='"); //$NON-NLS-1$
			sb.append(coverImageURL);
			sb.append("' style='float:left;margin-top:" + MARGIN + "' width='" //$NON-NLS-1$ //$NON-NLS-2$
					+ (INFOBANNER_HEIGHT - MARGIN) + "' height='" //$NON-NLS-1$
					+ (INFOBANNER_HEIGHT - MARGIN) + "' />"); //$NON-NLS-1$
			cover.setText(sb.toString());
		}

		FormData fd = new FormData();
		cover.setLayoutData(fd);
		fd.left = new FormAttachment();
		fd.top = new FormAttachment();
		fd.height = INFOBANNER_HEIGHT;
		fd.width = INFOBANNER_HEIGHT;

		// 显示数据过滤
		Label filterLabel = new Label(header, SWT.NONE);
		filterLabel.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		fd = new FormData();
		filterLabel.setLayoutData(fd);
		fd.left = new FormAttachment(30, -INFOBANNER_HEIGHT);
		fd.top = new FormAttachment(32);

		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:13pt'>"); //$NON-NLS-1$
		sb.append(desc + getPageTitle());
		sb.append("</span>"); //$NON-NLS-1$

		filterLabel.setText(sb.toString());

		return header;
	}

	protected abstract String getPageTitle();

	private void setBackgroundGradient(Composite header) {
		Object adapter = header.getAdapter(IWidgetGraphicsAdapter.class);
		IWidgetGraphicsAdapter gfxAdapter = (IWidgetGraphicsAdapter) adapter;
		int[] percents = new int[] { 0, 50, 100 };
		Display display = header.getDisplay();
		Color[] gradientColors = new Color[] {
				Widgets.getColor(header.getDisplay(), 220, 220, 240),
				Widgets.getColor(header.getDisplay(), 245, 245, 250),
				Widgets.getColor(display, 255, 255, 255) };

		gfxAdapter.setBackgroundGradient(gradientColors, percents, true);
	}

}
