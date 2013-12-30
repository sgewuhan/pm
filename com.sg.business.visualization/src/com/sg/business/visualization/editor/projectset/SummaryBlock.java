package com.sg.business.visualization.editor.projectset;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.sg.business.model.ProjectProvider;
import com.sg.business.visualization.labelprovider.AbstractProjectLabelProvider;
import com.sg.widgets.registry.config.ColumnConfigurator;

public class SummaryBlock extends Composite{

	private Label label;
	private ColumnConfigurator column;
	private ProjectProvider data;

	public SummaryBlock(Composite parent, ProjectProvider data, ColumnConfigurator column) {
		super(parent, SWT.NONE);
		setLayout(new FillLayout());
		label = new Label(this,SWT.NONE);
		label.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		this.data = data;
		this.column = column;
	}
	
	private void setText(String html){
		label.setText(html);
	}

	public void load() {
		String text = getSummary(data, column);
		setText(text);
	}
	
	protected String getSummary(ProjectProvider data, ColumnConfigurator column) {
		ColumnLabelProvider lp = column.getLabelProvider();
		if (lp instanceof AbstractProjectLabelProvider) {
			return ((AbstractProjectLabelProvider) lp).getSummary(data);
		}
		return "";
	}

}
