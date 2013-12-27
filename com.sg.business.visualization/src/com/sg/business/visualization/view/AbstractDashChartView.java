package com.sg.business.visualization.view;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.sg.business.resource.BusinessResource;
import com.sg.widgets.birtcharts.ChartCanvas;

public abstract class AbstractDashChartView extends AbstractDashWidgetView {

	private ChartCanvas chart;

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
		
		Button toolbar = new Button(parent,SWT.PUSH);
		toolbar.setData(RWT.CUSTOM_VARIANT, "whitebutton");
		toolbar.setImage(BusinessResource.getImage(BusinessResource.IMAGE_DOWN_28X12));
		fd = new FormData();
		toolbar.setLayoutData(fd);
		fd.right = new FormAttachment(100);
		fd.width = 28;
		fd.height = 12;
		fd.top = new FormAttachment(0,1);
		
		
		
		toolbar.moveAbove(null);
		parent.layout();
	}

	protected abstract Chart getChartData() throws Exception;
}
