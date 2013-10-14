package com.sg.business.work.labelprovider;

import java.util.Date;

import org.eclipse.swt.graphics.Image;

import com.sg.business.model.Work;
import com.sg.business.model.WorksPerformence;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.commons.labelprovider.ConfiguratorColumnLabelProvider;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.registry.config.ColumnConfigurator;

public class TodaysRecordLabelProvider extends ConfiguratorColumnLabelProvider {

	public TodaysRecordLabelProvider(ColumnConfigurator configurator) {
		super(configurator);
	}

	public TodaysRecordLabelProvider() {
	}

	@Override
	public Image getImage(Object element) {
		
		Date today = new Date();
		String userid = new CurrentAccountContext().getAccountInfo().getConsignerId();
		//如果有记录显示记录图标
		if(element instanceof Work){
			Work work = (Work) element;
			WorksPerformence record = work.getWorksPerformence(today,userid);
			if(record!=null){
				return BusinessResource.getImage(BusinessResource.IMAGE_LOG_24);
			}
		}
		
		
		return super.getImage(element);
	}
	
	public String getText(Object element) {
		return "";
	}
}
