package com.sg.sales.field.option;

import com.mobnut.db.model.PrimaryObject;
import com.sg.widgets.commons.options.IFieldOptionProvider;
import com.sg.widgets.registry.config.Option;

public class DistOptions implements IFieldOptionProvider {

	public DistOptions() {
	}

	@Override
	public Option getOption(Object input, Object data, String key,
			Object value) {
		CityOptions co = new CityOptions();
		Option cityEnum = co.getOption(input, data, key, value);
		if (cityEnum != null) {

			Option[] children = cityEnum.getChildren();
			if (children != null) {
				Object city = ((PrimaryObject) data).getValue("city");

				for (int i = 0; i < children.length; i++) {
					if (children[i].getValue().equals(city)) {
						return children[i];
					}
				}
			}
		}
		return null;
	}

}
