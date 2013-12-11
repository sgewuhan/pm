package com.sg.business.visualization.labelprovider;

import java.util.Iterator;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.PrimaryObject;

public class CostSummary extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		PrimaryObject po = (PrimaryObject)element;
		double sum = 0d;
		Iterator<String> iter = po.get_data().keySet().iterator();
		while(iter.hasNext()){
			String key = iter.next();
			if(Utils.isNumbers(key)){
				Object value = po.getValue(key);
				if(value instanceof Number){
					sum += ((Number) value).doubleValue();
				}
			}
		}
		
		return String.format("%.2f",sum);
	}

}
