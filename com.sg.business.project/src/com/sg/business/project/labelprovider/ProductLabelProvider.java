package com.sg.business.project.labelprovider;

import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Project;
import com.sg.widgets.commons.labelprovider.ConfiguratorColumnLabelProvider;
import com.sg.widgets.registry.config.ColumnConfigurator;

public class ProductLabelProvider extends ConfiguratorColumnLabelProvider {

	public ProductLabelProvider(ColumnConfigurator configurator) {
		super(configurator);
	}

	public ProductLabelProvider() {
	}
	
	@Override
	public String getText(Object element) {
		Project project = (Project)element;
		List<PrimaryObject> products = project.getProduct();
		StringBuffer sb = new StringBuffer();
		if(products!=null){
			for (int i = 0; i < products.size(); i++) {
				sb.append(products.get(i));
				sb.append(" ,");
			}
		}
		return sb.toString();
	}

}
