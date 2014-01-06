package com.sg.business.visualization.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.jface.action.Action;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.sg.business.resource.BusinessResource;
import com.sg.widgets.birtcharts.ChartCanvas;

public abstract class AbstractDashChartView extends AbstractDashWidgetView {

	private ChartCanvas chart;

	private boolean toolbarExpanded = false;

	private Composite toolbarbg;

	private List<Action> actions;

	@Override
	protected void drawContent(Composite parent) {
		parent.setLayout(new FormLayout());

		chart = new ChartCanvas(parent, SWT.NONE) {
			@Override
			public Chart getChart() throws Exception {
				return getChartData();
			}
		};
		FormData fd = new FormData();
		chart.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(100);

		actions = getActions();
		if (!actions.isEmpty()) {
			createToolbar(parent);
		}
		parent.layout();
	}

	protected List<Action> getActions() {
		return new ArrayList<Action>();
	}

	private void createToolbar(final Composite parent) {
		final Button toolbarSwitch = new Button(parent, SWT.PUSH);
		toolbarSwitch.setData(RWT.CUSTOM_VARIANT, "whitebutton");
		toolbarSwitch.setImage(BusinessResource
				.getImage(BusinessResource.IMAGE_DOWN_28X12));
		FormData fd = new FormData();
		toolbarSwitch.setLayoutData(fd);
		fd.right = new FormAttachment(100);
		fd.width = 28;
		fd.height = 12;
		fd.top = new FormAttachment(0, 1);
		toolbarSwitch.moveAbove(null);

		toolbarSwitch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				toolbarExpanded = !toolbarExpanded;
				switchToolbar(parent);
				toolbarSwitch.setVisible( false);
			}
		});

		toolbarbg = new Composite(parent, SWT.NONE);

		fd = new FormData();
		toolbarbg.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment();
		fd.width = 28;
		toolbarbg.setLayout(new FormLayout());
		Button closeButton = new Button(toolbarbg, SWT.PUSH);
		closeButton.setData(RWT.CUSTOM_VARIANT, "whitebutton");
		closeButton.setImage(BusinessResource
				.getImage(BusinessResource.IMAGE_UP_28X12));
		fd = new FormData();
		closeButton.setLayoutData(fd);
		fd.right = new FormAttachment(100);
		fd.width = 28;
		fd.height = 12;
		fd.bottom = new FormAttachment(100, -1);
		closeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				toolbarExpanded = !toolbarExpanded;
				switchToolbar(parent);
				toolbarSwitch.setVisible( true);

			}
		});

		Composite toolbar = new Composite(toolbarbg, SWT.NONE);
//		toolbar.setBackground(Display.getCurrent().getSystemColor(
//				SWT.COLOR_BLUE));
		fd = new FormData();
		toolbar.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		fd.bottom = new FormAttachment(closeButton, -1);
		fd.right = new FormAttachment(100);

		toolbar.setLayout(new RowLayout(SWT.VERTICAL));
		createActions(toolbar);
	}

	private void createActions(Composite toolbar) {
		for (int i = 0; i < actions.size(); i++) {
			final Action action = actions.get(i);
			Button b = new Button(toolbar,SWT.PUSH);
			b.setData(RWT.CUSTOM_VARIANT, "whitebutton");
			String text = action.getText();
			if(text == null){
				text = action.getToolTipText();
			}
			b.setToolTipText(text);
			b.setImage(action.getImageDescriptor().createImage());
			b.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					action.run();
				}
			});
		}
	}

	protected void switchToolbar(Composite parent) {
		if (toolbarExpanded) {
			FormData fd = new FormData();
			toolbarbg.setLayoutData(fd);
			fd.top = new FormAttachment();
			fd.right = new FormAttachment(100);
			fd.bottom = new FormAttachment(100);
			fd.width = 28;
			toolbarbg.moveAbove(null);
		} else {
			FormData fd = new FormData();
			toolbarbg.setLayoutData(fd);
			fd.top = new FormAttachment();
			fd.right = new FormAttachment(100);
			fd.bottom = new FormAttachment();
			fd.width = 28;
			toolbarbg.moveAbove(null);
		}
		parent.layout();
	}

	protected abstract Chart getChartData() throws Exception;
}
