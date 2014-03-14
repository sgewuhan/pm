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
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;

import com.mobnut.commons.html.HtmlUtil;
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

	protected static final int FONT_SIZE = 12;
	private static final int INFOBANNER_HEIGHT = 50;
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
		HtmlUtil.enableMarkup(control);
		control.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				if (event.detail == RWT.HYPERLINK) {
					try {
						String path;
						String orj;
						String eventCode;
						if (event.text.startsWith("http")) { //$NON-NLS-1$
							URL url = new URL(event.text);
							path = url.getPath();
							orj = path.substring(1, path.indexOf("/", 1)); //$NON-NLS-1$
							eventCode = path.substring(path.lastIndexOf("/") + 1); //$NON-NLS-1$
						} else {
							path = event.text;
							orj= path.substring(0, path.indexOf("/")); //$NON-NLS-1$
							eventCode = path.substring(path.lastIndexOf("/") + 1); //$NON-NLS-1$
						}
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
		if (eventCode.equals("desc")) { //$NON-NLS-1$
			ObjectId projectId = new ObjectId(orj);
			Project project = ModelService.createModelObject(Project.class,
					projectId);

			try {
				DataObjectEditor.open(project, "editor.visualization.project", //$NON-NLS-1$
						false, null);
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}
		}
	}

	protected Composite createHeader(Composite body) {
		Composite header = new Composite(body, SWT.NONE);
		header.setLayout(new FormLayout());
		

		// 显示数据过滤
		filterLabel = new Label(header, SWT.NONE);
		HtmlUtil.enableMarkup(filterLabel);
		FormData fd = new FormData();
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
		HtmlUtil.enableMarkup(projectSetLabel);
		projectSetLabel.setText(getProjectSetPageLabel());

		fd = new FormData();
		projectSetLabel.setLayoutData(fd);
		fd.left = new FormAttachment(menuButton, 4);
		fd.top = new FormAttachment(32);

		filterLabel.setText(getParameterText());

		
		Label sep = new Label(header, SWT.NONE);
		fd = new FormData();
		sep.setLayoutData(fd);
		fd.bottom = new FormAttachment(100);
		fd.left = new FormAttachment();
		fd.height = 1;
		fd.right = new FormAttachment(100);
		sep.setBackground(Widgets.getColor(header.getDisplay(), 192, 192, 192));
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

	protected void setBackgroundGradient(Composite header) {
		Object adapter = header.getAdapter(IWidgetGraphicsAdapter.class);
		IWidgetGraphicsAdapter gfxAdapter = (IWidgetGraphicsAdapter) adapter;
		int[] percents = new int[] { 0,85, 95, 100 };
		Display display = header.getDisplay();
		Color[] gradientColors = new Color[] {
				Widgets.getColor(display, 255, 255, 255),
				Widgets.getColor(display, 255, 255, 255),
				Widgets.getColor(display, 240, 240, 240),
				Widgets.getColor(display, 230, 230, 230),
				};

		gfxAdapter.setBackgroundGradient(gradientColors, percents, true);
	}

	
	protected String getParameterText() {
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:"
				+ FONT_SIZE
				+ "pt'>"); //$NON-NLS-1$
		sb.append(DurationSetting.getHeadParameterText(data));
		sb.append("</span>"); //$NON-NLS-1$
		return sb.toString();
	}
}
