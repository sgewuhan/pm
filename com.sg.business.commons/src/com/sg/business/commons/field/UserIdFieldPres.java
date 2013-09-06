package com.sg.business.commons.field;

import org.eclipse.ui.forms.IFormPart;

import com.mobnut.commons.util.Utils;
import com.sg.business.model.User;
import com.sg.widgets.commons.valuepresentation.IValuePresentation;
import com.sg.widgets.part.editor.fields.AbstractFieldPart;

/**
 * 依据用户ID设置显示样式
 * @author gdiyang
 *
 */
public class UserIdFieldPres implements IValuePresentation {

	/**
	 * 依据用户ID设置显示样式
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
