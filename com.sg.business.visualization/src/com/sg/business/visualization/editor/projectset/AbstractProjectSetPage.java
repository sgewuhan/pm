package com.sg.business.visualization.editor.projectset;

import java.net.URL;

import org.bson.types.ObjectId;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.widgets.IWidgetGraphicsAdapter;
import org.eclipse.swt.internal.widgets.MarkupValidator;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;

import com.mobnut.db.model.ModelService;
import com.sg.business.model.IParameterListener;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectProvider;
import com.sg.business.visualization.ui.DurationSetting;
import com.sg.widgets.ImageResource;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.NavigatorControl;
import com.sg.widgets.part.editor.DataObjectEditor;
import com.sg.widgets.part.editor.IEditorActionListener;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.INavigatorPageBodyPartCreater;
import com.sg.widgets.part.editor.page.NavigatorPage;
import com.sg.widgets.viewer.ViewerControl;

@SuppressWarnings("restriction")
public abstract class AbstractProjectSetPage implements
		INavigatorPageBodyPartCreater, IParameterListener {

	private static final int INFOBANNER_HEIGHT = 68;
	private static final int MARGIN = 4;
	Label filterLabel;
	Composite header;
	protected NavigatorControl navi;
	protected ProjectProvider data;

	public AbstractProjectSetPage() {

	}

	@Override
	public void parameterChanged(Object[] oldParameters, Object[] newParameters) {
		// doquery
		if (navi != null) {
			ViewerControl viewerControl = navi.getViewerControl();
			if (!viewerControl.getControl().isDisposed()) {
				viewerControl.doReloadData(true);
			}
		}
		if (filterLabel != null && !filterLabel.isDisposed()) {
			filterLabel.setText(getParameterText());
			header.layout();
		}
	}

	@Override
	public void editorAction(IEditorActionListener reciever, int code,
			Object object) {
	}

	@Override
	public void createNavigatorBody(Composite body, NavigatorControl navi,
			PrimaryObjectEditorInput input, NavigatorPage navigatorPage) {
		// 设置缺省的参数
		data = (ProjectProvider) input.getData();
		data.addParameterChangedListener(this);
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

		// 管理navigator表格事件
		handleNavigatorTableEvent();

	}

	private void handleNavigatorTableEvent() {
		Table control = (Table) navi.getViewer().getControl();
		control.setData(MarkupValidator.MARKUP_VALIDATION_DISABLED,
				Boolean.TRUE);
		control.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				if (event.detail == RWT.HYPERLINK) {
					try {
						URL url = new URL(event.text);
						String path = url.getPath();
						String orj = path.substring(1, path.indexOf("/", 1));
						String eventCode = path.substring(path.lastIndexOf("/") + 1);
						call(orj, eventCode);
					} catch (Exception e) {
						MessageUtil.showToast(e);
					}
				}
			}
		});
	}

	protected abstract Composite createContent(Composite body);

	protected void call(String orj, String eventCode) {
		if (eventCode.equals("desc")) {
			ObjectId projectId = new ObjectId(orj);
			Project project = ModelService.createModelObject(Project.class,
					projectId);
			
			try {
				DataObjectEditor.open(project, "editor.visualization.project", false, null);
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}
		}
	}

	protected Composite createHeader(Composite body) {
		String projectSetCover = data.getProjectSetCoverImage();

		Composite header = new Composite(body, SWT.NONE);
		header.setLayout(new FormLayout());
		setBackgroundGradient(header);

		Label cover = new Label(header, SWT.NONE);
		cover.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);

		StringBuffer sb = new StringBuffer();
		// 添加项目集合封面图片
		if (projectSetCover != null) {
			sb.append("<img src='");
			sb.append(projectSetCover);
			sb.append("' style='float:left;margin-top:" + MARGIN + "' width='"
					+ (INFOBANNER_HEIGHT - MARGIN) + "' height='"
					+ (INFOBANNER_HEIGHT - MARGIN) + "' />");
			cover.setText(sb.toString());
		}

		FormData fd = new FormData();
		cover.setLayoutData(fd);
		fd.left = new FormAttachment();
		fd.top = new FormAttachment();
		fd.height = INFOBANNER_HEIGHT;
		fd.width = INFOBANNER_HEIGHT;

		// 显示数据过滤
		filterLabel = new Label(header, SWT.NONE);
		filterLabel.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		fd = new FormData();
		filterLabel.setLayoutData(fd);
		fd.left = new FormAttachment(30, -INFOBANNER_HEIGHT);
		fd.top = new FormAttachment(32);

		// 显示菜单图标
		final CLabel menuButton = new CLabel(header, SWT.NONE);
		menuButton.setImage(Widgets.getImage(ImageResource.DOWN_16));
		fd = new FormData();
		menuButton.setLayoutData(fd);
		fd.left = new FormAttachment(filterLabel, 4);
		fd.top = new FormAttachment(40);
		menuButton.setCursor(Display.getCurrent().getSystemCursor(
				SWT.CURSOR_HAND));
		menuButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
				showFilterMenu(menuButton);
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});

		// 显示项目集合名称
		Label projectSetLabel = new Label(header, SWT.NONE);
		projectSetLabel.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		projectSetLabel.setText(getProjectSetPageLabel());

		fd = new FormData();
		projectSetLabel.setLayoutData(fd);
		fd.left = new FormAttachment(menuButton, 4);
		fd.top = new FormAttachment(32);

		filterLabel.setText(getParameterText());

		return header;
	}

	protected abstract String getProjectSetPageLabel();

	protected void showFilterMenu(Control menuButton) {
		DurationSetting shell = new DurationSetting(menuButton.getShell(), data);
		Point location = menuButton.toDisplay(0, 16);
		Display display = shell.getDisplay();
		if (display.getBounds().width < shell.getBounds().width + location.x) {
			location.x = display.getBounds().width - shell.getBounds().width
					- 10;
		}
		shell.open(location);
	}

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

	protected String getParameterText() {
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:13pt'>");
		sb.append(DurationSetting.getHeadParameterText(data));
		sb.append("</span>");
		return sb.toString();
	}
}
