package com.sg.business.management.labelprovider;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.mobnut.db.model.ModelService;
import com.mongodb.DBObject;
import com.sg.business.model.WorkTimeProgram;

public class WorkTimeTypeLabel extends ColumnLabelProvider {

	public WorkTimeTypeLabel() {
	}

	@Override
	public String getText(Object element) {
		String[] typeIdAndProgramId = ((String) element).split("@");
		// 根据typeId获取工时类型名，根据programId获取工时方案名称
		ObjectId typeId = new ObjectId(typeIdAndProgramId[0]);
		ObjectId programId = new ObjectId(typeIdAndProgramId[1]);
		WorkTimeProgram program = ModelService.createModelObject(
				WorkTimeProgram.class, programId);
		String programDesc = program.getDesc();
		BasicBSONList workTimeTypes = (BasicBSONList) program
				.getValue(WorkTimeProgram.F_WORKTIMETYPES);
		for (int i = 0; i < workTimeTypes.size(); i++) {
			ObjectId workTimeTypeId = (ObjectId) ((DBObject) workTimeTypes
					.get(i)).get(WorkTimeProgram.F__ID);
			if (workTimeTypeId.equals(typeId)) {
				String workTimeTypeDesc = (String) ((DBObject) workTimeTypes
						.get(i)).get(WorkTimeProgram.F_DESC);
				return programDesc + "|" + workTimeTypeDesc;
			}
		}
		return "无法获取对应的工时类型";
	}
}
