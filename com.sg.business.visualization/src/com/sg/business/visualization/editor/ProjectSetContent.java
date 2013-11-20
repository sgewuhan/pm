package com.sg.business.visualization.editor;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.ProjectProvider;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.NavigatorControl;
import com.sg.widgets.part.editor.IEditorActionListener;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.INavigatorPageBodyPartCreater;
import com.sg.widgets.part.editor.page.NavigatorPage;

public class ProjectSetContent implements INavigatorPageBodyPartCreater {

	private static final int INFOBANNER_HEIGHT = 68;
	private static final int MARGIN = 4;

	public ProjectSetContent() {
	}

	@Override
	public void editorAction(IEditorActionListener reciever, int code,
			Object object) {
	}

	@Override
	public void createNavigatorBody(Composite body, NavigatorControl navi,
			PrimaryObjectEditorInput input, NavigatorPage navigatorPage) {
		body.setLayout(new FormLayout());
		// 创建页头
		Composite header = createHeader(body, input);
		FormData fd = new FormData();
		header.setLayoutData(fd);
		fd.top = new FormAttachment(0, 1);
		fd.left = new FormAttachment(0, 1);
		fd.right = new FormAttachment(100, -1);
		fd.height = INFOBANNER_HEIGHT;
		// 创建分割线
		Label line = new Label(body, SWT.NONE);
		fd = new FormData();
		line.setLayoutData(fd);
		Color sepColor = Widgets.getColor(body.getDisplay(), 192, 192, 192);
		line.setBackground(sepColor);
		fd.top = new FormAttachment(header, 0);
		fd.left = new FormAttachment(0, 1);
		fd.right = new FormAttachment(100, -1);
		fd.height = 1;

		// 创建表格
		Composite navigator = createNavigator(body, navi);
		fd = new FormData();
		navigator.setLayoutData(fd);
		fd.top = new FormAttachment(line, 0);
		fd.left = new FormAttachment(0, 1);
		fd.right = new FormAttachment(100, -1);
		fd.bottom = new FormAttachment(100, -1);
	}

	private Composite createNavigator(Composite body, NavigatorControl navi) {
		Composite navigator = new Composite(body, SWT.NONE);
		navi.createPartContent(navigator);
		return navigator;
	}

	private Composite createHeader(Composite body,
			PrimaryObjectEditorInput input) {
		ProjectProvider data = (ProjectProvider) input.getData();
		String projectSetName = data.getProjectSetName();
		String projectSetCover = data.getProjectSetCoverImageURL();

		Composite header = new Composite(body, SWT.NONE);
		header.setLayout(new FillLayout());
		setBackgroundGradient(header);

		Label label = new Label(header, SWT.NONE);
		label.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);

		StringBuffer sb = new StringBuffer();
		// 添加项目集合封面图片
		if (projectSetCover != null) {
			sb.append("<img src='");
			sb.append(projectSetCover);
			sb.append("' style='border-style:none;float:left; display:block;' width='"
					+ (INFOBANNER_HEIGHT - MARGIN)
					+ "' height='"
					+ (INFOBANNER_HEIGHT - MARGIN) + "' />");
		}
		
		//添加名称
		sb.append("<span style='FONT-FAMILY:微软雅黑;float:left;font-size:14pt'>");
		sb.append(projectSetName);
		sb.append("</span>");

		label.setText(sb.toString());
		return header;
	}

	private void setBackgroundGradient(Composite header) {
		// Object adapter = header.getAdapter(IWidgetGraphicsAdapter.class);
		// IWidgetGraphicsAdapter gfxAdapter = (IWidgetGraphicsAdapter) adapter;
		// int[] percents = new int[] { 0, 50, 100 };
		// Display display = header.getDisplay();
		// Color[] gradientColors = new Color[] {
		// Widgets.getColor(header.getDisplay(), 220, 220, 240),
		// Widgets.getColor(header.getDisplay(), 245, 245, 250),
		// Widgets.getColor(display, 255, 255, 255)
		// };
		//
		// gfxAdapter.setBackgroundGradient(gradientColors, percents, true);
	}

}
