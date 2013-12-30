package com.sg.business.visualization.editor.projectset;

import org.eclipse.jface.viewers.Viewer;

import com.mobnut.commons.util.Utils;
import com.sg.business.model.Project;

public class ProjectColumnSorter extends ColumnSorter {

	private String field;

	public ProjectColumnSorter(String text) {
		super(text);
	}

	public ProjectColumnSorter(String text,String field) {
		super(text);
		this.field = field;
	}

	@Override
	protected int doCompare(Viewer viewer2, Object e1, Object e2) {
		Project project1 = (Project) e1;
		Project project2 = (Project) e2;
		return doCompare(project1, project2);
	}

	protected int doCompare(Project p1, Project p2) {
		return Utils.doCompare(p1.getValue(field), p2.getValue(field));
	}

}
