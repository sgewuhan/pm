package com.sg.business.work.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.sg.business.model.Work;

public class OverloadLabelProvider extends ColumnLabelProvider {


	@Override
	public Image getImage(Object element) {
		Work work = (Work)element;
//		int count = work.getOverloadCount();
		
		
		// TODO Auto-generated method stub
		return super.getImage(element);
	}
	
}
