package com.sg.business.commons.value;

import java.util.Date;

import com.sg.widgets.part.editor.fields.value.IFieldDefaultValue;

public class TodayDefault implements IFieldDefaultValue {

	public TodayDefault() {
	}

	@Override
	public Object getDefaultValue(Object data, String key) {
		
		return new Date();
	}

}
