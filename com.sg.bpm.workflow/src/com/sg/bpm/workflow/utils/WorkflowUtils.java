package com.sg.bpm.workflow.utils;

public class WorkflowUtils {

	
	/**
	 * �������������#{}��������ȡ������ı���
	 * 
	 * @param param
	 * @return
	 */
	public static String getParameterName(String param) {

		int start = param.indexOf("#{");
		int end = param.indexOf("}");
		if (start != 0 || end == -1) {
			return null;
		} else {
			return param.substring(2, end);
		}
	}
}
