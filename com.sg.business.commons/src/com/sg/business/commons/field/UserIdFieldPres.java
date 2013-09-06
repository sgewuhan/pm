package com.sg.business.commons.field;

import org.eclipse.ui.forms.IFormPart;

import com.mobnut.commons.util.Utils;
import com.sg.business.model.User;
import com.sg.widgets.commons.valuepresentation.IValuePresentation;
import com.sg.widgets.part.editor.fields.AbstractFieldPart;

/**
 * �����û�ID������ʾ��ʽ
 * @author gdiyang
 *
 */
public class UserIdFieldPres implements IValuePresentation {

	/**
	 * �����û�ID������ʾ��ʽ
	 */
	@Override
	public String getPresentValue(IFormPart part) {
		AbstractFieldPart field = (AbstractFieldPart) part;
		String value = (String) field.getValue();
		if(!Utils.isNullOrEmpty(value)){
			User user = User.getUserById(value);
			if(user!=null){
				return user.getLabel();
			}
		}
		return value;
	}

}
