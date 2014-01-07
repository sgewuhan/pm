package com.sg.business.visualization.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;

import com.sg.business.visualization.view.AbstractDashChartView;
import com.sg.widgets.ImageResource;
import com.sg.widgets.Widgets;

public class ZoomViewAction extends Action {

	private AbstractDashChartView view;

	public ZoomViewAction(AbstractDashChartView view) {
		setText("放缩");
		setToolTipText("放大缩小显示");
		setImageDescriptor(Widgets.getImageDescriptor(ImageResource.ZOOM_16));
		this.view = view;
	}

	@Override
	public void run() {
		IWorkbenchPage page = view.getViewSite().getWorkbenchWindow()
				.getActivePage();
		IViewReference vr = page.findViewReference(view.getViewSite().getId());
		page.toggleZoom(vr);
	}

}
