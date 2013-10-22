package com.sg.business.performence.works;

import org.eclipse.jface.viewers.IStructuredSelection;

import com.sg.business.model.Work;
import com.sg.widgets.part.editor.fields.INavigatorSelectorControl;

public class WorkSelectControl implements INavigatorSelectorControl {


	@Override
	public boolean isSelectEnable(IStructuredSelection is) {
		//���ѡ��Ľ����еĹ��������ܽ��в���
		if(is==null||is.isEmpty()){
			return false;
		}
		Object element = is.getFirstElement();
		if(element instanceof Work){
			if(((Work) element).isProjectWBSRoot()){
				return false;
			}
			
			String ls = ((Work) element).getLifecycleStatus();
			if(Work.STATUS_WIP_VALUE.equals(ls)){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}


}
