package com.sg.sales.field.option;

import com.mobnut.db.model.PrimaryObject;
import com.sg.sales.model.WorkCost;
import com.sg.widgets.Widgets;
import com.sg.widgets.commons.options.IFieldOptionProvider;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.registry.config.FieldConfigurator;
import com.sg.widgets.registry.config.Option;

public class DetailCatagory implements IFieldOptionProvider {

	public DetailCatagory() {
	}

	@Override
	public Option getOption(Object input, Object data, String key, Object value) {
		PrimaryObjectEditorInput dataInput = (PrimaryObjectEditorInput)input;
		Object parent = ((PrimaryObject)data).getValue(WorkCost.F_CATAGORY);
		FieldConfigurator field = dataInput.getEditorConfigurator().getField(WorkCost.F_CATAGORY);
		String optionId = field.getOption();
		Option root = (Option) Widgets.getOptionRegistry().getConfigurator(optionId);
		Option[] children = root.getChildren();
		for (int i = 0; i < children.length; i++) {
			if(children[i].getValue().equals(parent)){
				return children[i];
			}
		}
		return null;
	}

}
