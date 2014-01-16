package com.sg.business.commons.field.presentation;

import java.util.List;

import org.eclipse.ui.forms.IFormPart;

import com.sg.business.model.Organization;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.commons.valuepresentation.IValuePresentation;
import com.sg.widgets.part.editor.fields.AbstractFieldPart;

public class ParticipatePres implements IValuePresentation {

	@Override
	public String getPresentValue(IFormPart part) {
		AbstractFieldPart field = (AbstractFieldPart) part;
		Object value = field.getValue();
		if(value instanceof List<?>){
			List<?> list = (List<?>) value;
			String result = "<span style='color=#4a4a4a'>"; //$NON-NLS-1$
			for (int i = 0; i < list.size(); i++) {
				Object element = list.get(i);
				User user = UserToolkit.getUserById((String) element);
				if(i!=0){
					result += "<br/>"; //$NON-NLS-1$
				}
				Organization org = user.getOrganization();
				if(org!=null){
					String path = org.getPath(2);
					result += user + "  ("+path+")"; //$NON-NLS-1$ //$NON-NLS-2$
				}else{
					result += user;
				}
			}
			result+="</span>"; //$NON-NLS-1$
			//²¹¿ÕÐÐ
			if(list.size()<3){
				for (int i = list.size(); i < 4; i++) {
					result += "<br/>"; //$NON-NLS-1$
				}
			}
			return result;
		}
		
		return ""; //$NON-NLS-1$
	}

}
