package com.sg.business.project.editor.option;

import org.bson.types.BasicBSONList;

import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.widgets.commons.options.IFieldOptionProvider;
import com.sg.widgets.registry.config.Option;

public class StatisticsStepOptionOfProject implements IFieldOptionProvider{

	public StatisticsStepOptionOfProject() {
	}

	@Override
	public Option getOption(Object input, Object data, String key, Object value) {
		//1.根据工作定义获取项目模板
//		ProjectTemplate projectTemplate = ((WorkDefinition)data).getProjectTemplate();
		Work work=(Work) data;
		Project project = work.getProject();
		BasicBSONList steps = (BasicBSONList) project.getValue(Project.F_STATISTICSS_STEP);
		if(steps==null){
			return new Option("","","",new Option[0]);
		}
		//3.构造option
		Option[] children=new Option[steps.size()];
		for (int i = 0; i < steps.size(); i++) {
			String statisticsStep = (String) steps.get(i);
			children[i]=new Option(statisticsStep,statisticsStep,statisticsStep,new Option[0]);
		}
		return new Option("", "", "", children);
	}
}
