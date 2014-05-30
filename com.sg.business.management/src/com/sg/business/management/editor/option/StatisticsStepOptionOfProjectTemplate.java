package com.sg.business.management.editor.option;

import org.bson.types.BasicBSONList;

import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.commons.options.IFieldOptionProvider;
import com.sg.widgets.registry.config.Option;

public class StatisticsStepOptionOfProjectTemplate implements IFieldOptionProvider {

	public StatisticsStepOptionOfProjectTemplate() {
	}

	@Override
	public Option getOption(Object input, Object data, String key, Object value) {
		//1.���ݹ��������ȡ��Ŀģ��
		ProjectTemplate projectTemplate = ((WorkDefinition)data).getProjectTemplate();
		//2.������Ŀģ���ȡͳ�ƽ׶�
		BasicBSONList statisticsSteps = (BasicBSONList) projectTemplate.getValue(ProjectTemplate.F_STATISTICSSTEP);
		if(statisticsSteps==null){
			return new Option("","","",new Option[0]);
		}
		//3.����option
		Option[] children=new Option[statisticsSteps.size()];
		for (int i = 0; i < statisticsSteps.size(); i++) {
			String statisticsStep = (String) statisticsSteps.get(i);
			children[i]=new Option(statisticsStep,statisticsStep,statisticsStep,new Option[0]);
		}
		return new Option("", "", "", children);
	}

}
