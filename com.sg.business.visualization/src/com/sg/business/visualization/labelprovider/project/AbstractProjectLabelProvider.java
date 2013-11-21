package com.sg.business.visualization.labelprovider.project;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.sg.business.model.Project;

abstract class AbstractProjectLabelProvider extends ColumnLabelProvider {

	
	@Override
	public Image getImage(Object element) {
		return null;
	}
	
	@Override
	public String getText(Object element) {
		if(element instanceof Project){
			String text = getProjectText((Project)element);
			return text == null?"":text;
		}else{
			return "unsupport type, required: com.sg.business.model.Project";
		}
	}

	protected abstract String getProjectText(Project project) ;
}
