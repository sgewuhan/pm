package com.sg.business.commons.column.labelprovider;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.mobnut.db.model.ModelService;
import com.mongodb.DBObject;
import com.sg.business.model.WorkDefinition;
import com.sg.business.model.WorkTimeProgram;

public class WorkTimeTypeLabelProvider extends ColumnLabelProvider {

	public WorkTimeTypeLabelProvider() {
	}

	@Override
	public String getText(Object element) {
		if(element instanceof WorkDefinition){
			WorkDefinition workd=(WorkDefinition) element;
			return getWorkTimeTypeLabel(workd);
		}
		
		return "";
	}
	private String getWorkTimeTypeLabel(WorkDefinition workd) {
		String result="";
		BasicBSONList workTimeTypes=(BasicBSONList) workd.getValue(WorkDefinition.F_WORKTIMETYPE);
		if(workTimeTypes!=null){
			for (int i = 0; i < workTimeTypes.size(); i++) {
				String workTimeLabel = getWorkTimeLabel((String) workTimeTypes.get(i));
				if(workTimeLabel!=null){
					if(result.isEmpty()){
						result+=workTimeLabel;
					}else if(i==1){
						result+=","+workTimeLabel;
					}else if(i==2){
						result += "..";
						break;
					}
				}
			}
		}
		return result;
	}

	private String getWorkTimeLabel(String workTimeType) {
		String[] split = workTimeType.split("@");
		ObjectId typeId = new ObjectId(split[0]);
		ObjectId programId = new ObjectId(split[1]);
		WorkTimeProgram program = ModelService.createModelObject(
				WorkTimeProgram.class, programId);
		String programDesc = program.getDesc();
		BasicBSONList workTimeTypeList = (BasicBSONList) program
				.getValue(WorkTimeProgram.F_WORKTIMETYPES);
		for (int j = 0; j < workTimeTypeList.size(); j++) {
			ObjectId workTimeTypeId = (ObjectId) ((DBObject) workTimeTypeList
					.get(j)).get(WorkTimeProgram.F__ID);
			if (workTimeTypeId.equals(typeId)) {
				String workTimeTypeDesc = (String) ((DBObject) workTimeTypeList
						.get(j)).get(WorkTimeProgram.F_DESC);
				return programDesc + "|" + workTimeTypeDesc;
			}
		}
		return null;
	}
}
