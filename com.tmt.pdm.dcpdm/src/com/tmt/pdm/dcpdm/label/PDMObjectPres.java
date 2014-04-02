package com.tmt.pdm.dcpdm.label;

import org.eclipse.ui.forms.IFormPart;

import com.sg.widgets.commons.valuepresentation.IValuePresentation;
import com.sg.widgets.part.editor.fields.AbstractFieldPart;
import com.tmt.pdm.client.Starter;

import dyna.framework.service.dos.DOSChangeable;

public class PDMObjectPres implements IValuePresentation {

	public PDMObjectPres() {
	}

	@Override
	public String getPresentValue(IFormPart part) {
		Object value = ((AbstractFieldPart)part).getValue();
		if(value instanceof String){
			try {
				DOSChangeable dosObj = Starter.dos.get((String) value);
				if(dosObj!=null){
					Object num = dosObj.get("md$number");
					num = num==null?"":num;
					Object desc = dosObj.get("md$description");
					desc = desc==null?"":desc;
					return ""+num+"|"+desc;
				}
			} catch (Exception e) {
			}
		}
		return "没有选择DCPDM图纸对象";
	}

}
