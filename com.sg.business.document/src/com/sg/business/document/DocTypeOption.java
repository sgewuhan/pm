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
													"通用文档"
													,"图纸"
													,"设计文档"
													,"方案"
													,"报告"
													,"表单"
													,"手册"
													,"文献"
													,"笔记"
													,"备忘录"
													,"邮件"
													,"便签"};

	public DocTypeOption() {
	}

	@Override
	public String getlabel() {
		return "文档类型";
	}

	@Override
	public String getValue() {
		return "文档类型";
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
