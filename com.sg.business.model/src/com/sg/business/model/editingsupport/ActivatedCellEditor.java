package com.sg.business.model.editingsupport;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.sg.business.resource.BusinessResource;

public class ActivatedCellEditor extends CellEditor {

	private Button button;
	
	private Boolean value;

	public ActivatedCellEditor(Composite control) {
		super(control);
	}

	@Override
	protected Control createControl(Composite parent) {
		button = new Button(parent,SWT.PUSH);
		button.setData(RWT.CUSTOM_VARIANT, "whitebutton_s");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				value = !value;
			}
		});
		
		return button;
	}

	@Override
	protected Object doGetValue() {
		return value;
	}

	@Override
	protected void doSetFocus() {
		button.setFocus();
	}

	@Override
	protected void doSetValue(Object value) {
		this.value = (Boolean)value;
		setImage();
	}

	private void setImage() {
		if(Boolean.TRUE.equals(value)){
			button.setImage(BusinessResource.getImage(BusinessResource.IMAGE_ACTIVATED_16));
		}else if(Boolean.FALSE.equals(value)){
			button.setImage(BusinessResource.getImage(BusinessResource.IMAGE_DISACTIVATED_16));
		}else{
			button.setImage(null);
		}
	}

}
