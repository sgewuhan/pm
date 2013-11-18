package com.tmt.tb.editor;

import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import com.mobnut.commons.util.Utils;
import com.sg.business.model.TaskForm;
import com.sg.business.model.Work;
import com.sg.widgets.commons.model.IEditorSaveHandler;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;

public class ProjectChangeConfirmProgramSaveHandler implements IEditorSaveHandler {

	public ProjectChangeConfirmProgramSaveHandler() {
	}

	@Override
	public boolean doSaveBefore(PrimaryObjectEditorInput input,
			IProgressMonitor monitor, String operation) throws Exception {
		
		TaskForm taskform = (TaskForm) input.getData();
		String choice = (String) taskform.getValue("choice");
		taskform.setProcessInputValue("choice", choice);
		if("��ͨ��".equals(choice)){
			return true;
		}
		Object ecn = taskform.getValue("ecn");
		if(ecn instanceof List<?>){
			List<?> ecnlist = (List<?>) ecn;
			String var;
			for(Object obj:ecnlist){
				Work work=(Work)obj;
				
				Boolean noskip = (Boolean) work.getValue(Work.F_INTERNAL_DEFAULTSELECTED);
				if(Boolean.TRUE.equals(noskip)){
					Object value = work.getValue(Work.F_PLAN_START);
					if(value==null){
						throw new Exception("�����üƻ���ʼʱ�䡣\n"+work);
					}
					value = work.getValue(Work.F_PLAN_FINISH);
					if(value==null){
						throw new Exception("�����üƻ����ʱ�䡣\n"+work);
					}
					value = work.getValue(Work.F_CHARGER);
					if(value==null){
						throw new Exception("�����ø����ˡ�\n"+work);
					}
					System.out.println();
					checkDuration(work);
					
					var = (String) work.getValue("chargerpara");
					taskform.setProcessInputValue(var, value);
					var=(String) work.getValue("noskippara");
					taskform.setProcessInputValue(var, "��");
					
					

				}else{
					var=(String) work.getValue("noskippara");
					taskform.setProcessInputValue(var, "��");
				}
				
			}
			
		}
		return true;
	}

	@Override
	public boolean doSaveAfter(PrimaryObjectEditorInput input,
			IProgressMonitor monitor, String operation) throws Exception {
		return true;
	}
	
	private void checkDuration(Work work) throws Exception {
		Date start = work.getPlanStart();
		if (start != null) {
			start = Utils.getDayBegin(start).getTime();
		}

		Date finish =  work.getPlanFinish();
		if (finish != null) {
			finish = Utils.getDayEnd(finish).getTime();
		}

		if (start != null && finish != null) {
			// ����Ƿ�Ϸ�
			if (start.after(finish)) {
				throw new Exception("��ʼ���ڱ��������������");
			}

		}
	}
}
