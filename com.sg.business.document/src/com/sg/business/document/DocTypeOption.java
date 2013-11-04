package com.sg.business.document;

import com.sg.widgets.commons.options.IOptionContribution;
import com.sg.widgets.registry.config.Option;

public class DocTypeOption implements IOptionContribution {

	public static final String[] DOCUMENT_TYPES = new String[] { 
													"generic",
													"drawing", 
													"design", 
													"program", 
													"report", 
													"data", 
													"book", 
													"news",
													"note", 
													"memo", 
													"mail", 
													"sticky" };
	public static final String[] DOCUMENT_TEXT = new String[] {
													"ͨ���ĵ�"
													,"ͼֽ"
													,"����ĵ�"
													,"����"
													,"����"
													,"��"
													,"�ֲ�"
													,"����"
													,"�ʼ�"
													,"����¼"
													,"�ʼ�"
													,"��ǩ"};

	public DocTypeOption() {
	}

	@Override
	public String getlabel() {
		return "�ĵ�����";
	}

	@Override
	public String getValue() {
		return "�ĵ�����";
	}

	@Override
	public Option[] getChildren() {
		Option[] result = new Option[DOCUMENT_TYPES.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = new Option(DOCUMENT_TYPES[i],DOCUMENT_TEXT[i],DOCUMENT_TYPES[i],null);
		}
		return result;
	}
	
	

}
