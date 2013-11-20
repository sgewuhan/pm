package com.sg.business.visualization.editor;

import java.util.Calendar;

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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.sg.business.model.ProjectProvider;
import com.sg.widgets.ImageResource;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.NavigatorControl;
import com.sg.widgets.part.editor.IEditorActionListener;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.INavigatorPageBodyPartCreater;
import com.sg.widgets.part.editor.page.NavigatorPage;

@SuppressWarnings("restriction")
public class ProjectSetContent implements INavigatorPageBodyPartCreater {

	private static final int INFOBANNER_HEIGHT = 68;
	private static final int MARGIN = 4;
	private Label projectStatusSummary;
	private Label schedualSummary;
	private Label costSummary;
	private Object[] parameters = new Object[10];
	private Label filterLabel;
	private Composite header;
	private NavigatorControl navi;
	private ProjectProvider data;

	public ProjectSetContent() {

	}

	@Override
	public void editorAction(IEditorActionListener reciever, int code,
			Object object) {
	}

	@Override
	public void createNavigatorBody(Composite body, NavigatorControl navi,
			PrimaryObjectEditorInput input, NavigatorPage navigatorPage) {
		// ����ȱʡ�Ĳ���
		parameters[0] = Calendar.getInstance();
		parameters[1] = ProjectProvider.PARAMETER_SUMMARY_BY_YEAR;
		data = (ProjectProvider) input.getData();
		data.setParameters(parameters);
		this.navi = navi;
		
		body.setLayout(new FormLayout());
		// ����ҳͷ
		header = createHeader(body);
		FormData fd = new FormData();
		header.setLayoutData(fd);
		fd.top = new FormAttachment(0, 1);
		fd.left = new FormAttachment(0, 1);
		fd.right = new FormAttachment(100, -1);
		fd.height = INFOBANNER_HEIGHT;
		// �����ָ���
		Label line = new Label(body, SWT.NONE);
		fd = new FormData();
		line.setLayoutData(fd);
		Color sepColor = Widgets.getColor(body.getDisplay(), 192, 192, 192);
		line.setBackground(sepColor);
		fd.top = new FormAttachment(header, 0);
		fd.left = new FormAttachment(0, 1);
		fd.right = new FormAttachment(100, -1);
		fd.height = 1;

		// �������
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

	private Composite createHeader(Composite body) {
		String projectSetName = data.getProjectSetName();
		String projectSetCover = data.getProjectSetCoverImage();

		Composite header = new Composite(body, SWT.NONE);
		header.setLayout(new FormLayout());
		setBackgroundGradient(header);

		Label cover = new Label(header, SWT.NONE);
		cover.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);

		StringBuffer sb = new StringBuffer();
		// �����Ŀ���Ϸ���ͼƬ
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

		// ��ʾ��Ŀ��������
		Label projectSetLabel = new Label(header, SWT.NONE);
		projectSetLabel.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:΢���ź�;font-size:13pt'>");
		sb.append(projectSetName);
		sb.append("</span>");
		projectSetLabel.setText(sb.toString());

		fd = new FormData();
		projectSetLabel.setLayoutData(fd);
		fd.left = new FormAttachment(30, -INFOBANNER_HEIGHT);
		fd.top = new FormAttachment(32);

		// ��ʾ�Ҳ�ĵ�һժҪ�ֶΣ�����
		projectStatusSummary = new Label(header, SWT.NONE);
		fd = new FormData();
		projectStatusSummary.setLayoutData(fd);
		fd.right = new FormAttachment(100, -4);
		fd.top = new FormAttachment(0, 4);

		// �Ҳ�ڶ�ժҪ�ֶΣ�����
		schedualSummary = new Label(header, SWT.NONE);
		fd = new FormData();
		schedualSummary.setLayoutData(fd);
		fd.right = new FormAttachment(100, -4);
		fd.top = new FormAttachment(projectStatusSummary, 2);

		// �Ҳ����ժҪ�ֶΣ��ɱ�
		costSummary = new Label(header, SWT.NONE);
		fd = new FormData();
		costSummary.setLayoutData(fd);
		fd.right = new FormAttachment(100, -4);
		fd.top = new FormAttachment(schedualSummary, 2);

		// ��ʾ�˵�ͼ��
		final CLabel menuButton = new CLabel(header, SWT.NONE);
		menuButton.setImage(Widgets.getImage(ImageResource.DOWN_16));
		fd = new FormData();
		menuButton.setLayoutData(fd);
		fd.right = new FormAttachment(projectStatusSummary, -16);
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

		// ��ʾ���ݹ���
		filterLabel = new Label(header, SWT.NONE);
		filterLabel.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		fd = new FormData();
		filterLabel.setLayoutData(fd);
		fd.right = new FormAttachment(menuButton, -4);
		fd.top = new FormAttachment(32);

		setSummaryText(data);

		return header;
	}

	private void setSummaryText(ProjectProvider data) {
		Object value1 = data.getSummaryValue(ProjectProvider.F_SUMMARY_TOTAL);
		value1 = value1 == null ? 0 : value1;
		Object value2 = data.getSummaryValue(
				ProjectProvider.F_SUMMARY_FINISHED);
		value2 = value2 == null ? 0 : value2;
		Object value3 = data.getSummaryValue(
				ProjectProvider.F_SUMMARY_PROCESSING);
		value3 = value3 == null ? 0 : value3;
		projectStatusSummary.setText("����/���/������" + value1 + "/" + value2 + "/"
				+ value3);
		value1 = data.getSummaryValue(ProjectProvider.F_SUMMARY_NORMAL_PROCESS);
		value1 = value1 == null ? 0 : value1;
		value2 = data.getSummaryValue(ProjectProvider.F_SUMMARY_DELAY);
		value2 = value2 == null ? 0 : value2;
		value3 = data.getSummaryValue(ProjectProvider.F_SUMMARY_ADVANCE);
		value3 = value3 == null ? 0 : value3;
		schedualSummary.setText("����/����/��ǰ��" + value1 + "/" + value2 + "/"
				+ value3);
		value1 = data.getSummaryValue(ProjectProvider.F_SUMMARY_NORMAL_COST);
		value1 = value1 == null ? 0 : value1;
		value2 = data.getSummaryValue(ProjectProvider.F_SUMMARY_OVER_COST);
		value2 = value2 == null ? 0 : value2;
		costSummary.setText("����/��֧��" + value1 + "/" + value2);

		
		filterLabel.setText(getParameterText());
		
	}

	protected void showFilterMenu(Control menuButton) {
		final Shell shell = new Shell(menuButton.getShell(), SWT.BORDER);
		shell.setLayout(new FormLayout());
		final Combo yearCombo = new Combo(shell, SWT.READ_ONLY);
		FormData fd = new FormData();
		yearCombo.setLayoutData(fd);
		fd.left = new FormAttachment(0, 4);
		fd.top = new FormAttachment(0, 4);
		fd.width = 100;

		final int startYear = Calendar.getInstance().get(Calendar.YEAR) - 5;
		for (int i = startYear; i < (startYear + 6); i++) {
			yearCombo.add("" + i);
		}
		yearCombo.select(5);

		final Combo quarterCombo = new Combo(shell, SWT.READ_ONLY);
		fd = new FormData();
		quarterCombo.setLayoutData(fd);
		fd.left = new FormAttachment(yearCombo, 4);
		fd.top = new FormAttachment(0, 4);
		fd.width = 80;
		quarterCombo.add("����");
		for (int i = 1; i < 5; i++) {
			quarterCombo.add("" + i + "����");
		}

		final Combo monthCombo = new Combo(shell, SWT.READ_ONLY);
		fd = new FormData();
		monthCombo.setLayoutData(fd);
		fd.left = new FormAttachment(quarterCombo, 4);
		fd.top = new FormAttachment(0, 4);
		fd.right = new FormAttachment(100, -4);

		fd.width = 80;
		monthCombo.add("����");
		for (int i = 1; i < 13; i++) {
			monthCombo.add("" + i + "��");
		}

		Button ok = new Button(shell, SWT.PUSH);
		ok.setText("Ok");
		fd = new FormData();
		ok.setLayoutData(fd);
		fd.left = new FormAttachment(0, 4);
		fd.top = new FormAttachment(monthCombo, 16);
		fd.bottom = new FormAttachment(100, -4);
		ok.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int yearIndex = yearCombo.getSelectionIndex();
				setFilter( startYear + yearIndex,
						quarterCombo.getSelectionIndex(),
						monthCombo.getSelectionIndex());
				shell.dispose();
			}
		});

		Button cancel = new Button(shell, SWT.PUSH);
		cancel.setText("Cancel");
		cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});

		fd = new FormData();
		cancel.setLayoutData(fd);
		fd.left = new FormAttachment(ok, 4);
		fd.top = new FormAttachment(monthCombo, 16);
		fd.bottom = new FormAttachment(100, -4);

		shell.pack();
		Point location = menuButton.toDisplay(0, 16);
		Display display = shell.getDisplay();
		if (display.getBounds().width < shell.getBounds().width + location.x) {
			location.x = display.getBounds().width - shell.getBounds().width
					- 10;
		}

		shell.setLocation(location);
		shell.open();
	}

	protected void setFilter(int yearIndex, int quarterIndex, int monthIndex) {
		if (yearIndex < 0) {
			return;
		}
		if (monthIndex > 0) {
			parameters[1] = ProjectProvider.PARAMETER_SUMMARY_BY_MONTH;
			((Calendar)parameters[0]).set(Calendar.MONTH, monthIndex-1);
			
		} else if (quarterIndex > 0) {
			parameters[1] = ProjectProvider.PARAMETER_SUMMARY_BY_QUARTER;
			((Calendar)parameters[0]).set(Calendar.MONTH, 3*(quarterIndex)-1);

			
		} else {
			parameters[1] = ProjectProvider.PARAMETER_SUMMARY_BY_YEAR;
		}
		
		reQuery();
		
	}

	private void reQuery() {
		//doquery
		data.setParameters(parameters);
		navi.getViewerControl().doReloadData(true, new Runnable(){

			@Override
			public void run() {
				setSummaryText(data);
				header.layout();
			}
			
		});
		
	}

	private String getParameterText() {
		StringBuffer sb = new StringBuffer();
		sb.append("<i style='FONT-FAMILY:΢���ź�;font-size:13pt'>");
		if (ProjectProvider.PARAMETER_SUMMARY_BY_YEAR.equals(parameters[1])) {
			sb.append(((Calendar) parameters[0]).get(Calendar.YEAR) + "��");
		} else if (ProjectProvider.PARAMETER_SUMMARY_BY_QUARTER
				.equals(parameters[1])) {
			Calendar calendar = (Calendar) parameters[0];
			int month = calendar.get(Calendar.MONTH);
			sb.append(calendar.get(Calendar.YEAR) + "��" + (1 + (1+month) / 4)
					+ "����");
		} else if (ProjectProvider.PARAMETER_SUMMARY_BY_MONTH
				.equals(parameters[1])) {
			Calendar calendar = (Calendar) parameters[0];
			int month = calendar.get(Calendar.MONTH);
			sb.append(calendar.get(Calendar.YEAR) + "��" + (1 + month) + "��");
		}

		sb.append("</i>");
		return sb.toString();
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

}
