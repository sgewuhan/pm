package com.sg.business.commons.field.presentation;

import java.util.List;

import org.eclipse.ui.forms.IFormPart;

import com.sg.business.model.Organization;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.commons.valuepresentation.IValuePresentation;
import com.sg.widgets.part.editor.fields.AbstractFieldPart;

public class PartticipatePres implements IValuePresentation {

	@Override
	public String getPresentValue(IFormPart part) {
		AbstractFieldPart field = (AbstractFieldPart) part;
		Object value = field.getValue();
		if(value instanceof List<?>){
			List<?> list = (List<?>) value;
			String result = "<span style='color=#4a4a4a'>";
			for (int i = 0; i < list.size(); i++) {
				Object element = list.get(i);
				User user = UserToolkit.getUserById((String) element);
				if(i!=0){
					result += "<br/>";
				}
				Organization org = user.getOrganization();
				if(org!=null){
					result += user + "  ("+org+")";
				}else{
					result += user;
				}
			}
			result+="</span>";
			return result;
		}
		
		return "";
	}

}
