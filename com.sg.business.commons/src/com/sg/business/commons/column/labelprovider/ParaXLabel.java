package com.sg.business.commons.column.labelprovider;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.mobnut.db.model.ModelService;
import com.mongodb.DBObject;
import com.sg.business.model.IWorkCloneFields;
import com.sg.business.model.WorkTimeProgram;

public class ParaXLabel extends ColumnLabelProvider {

	public ParaXLabel() {
	}

	@Override
	public String getText(Object element) {
		/*String[] typeIdAndProgramId = ((String) element).split("@");
		// 根据typeId获取工时类型名，根据programId获取工时方案名称
		ObjectId typeId = new ObjectId(typeIdAndProgramId[0]);
		ObjectId programId = new ObjectId(typeIdAndProgramId[1]);*/
		DBObject typeIdAndProgramId= (DBObject) element;
		ObjectId typeId=(ObjectId) typeIdAndProgramId.get(IWorkCloneFields.F_WORKTIME_PARAX_ID);
		ObjectId programId=(ObjectId) typeIdAndProgramId.get(IWorkCloneFields.F_WORKTIME_PARAX_PROGRAM_ID);
		WorkTimeProgram program = ModelService.createModelObject(
				WorkTimeProgram.class, programId);
		String programDesc = program.getDesc();
		BasicBSONList paraXs = (BasicBSONList) program
				.getValue(WorkTimeProgram.F_WORKTIME_PARA_X);
		for (int i = 0; i < paraXs.size(); i++) {
			ObjectId paraXId = (ObjectId) ((DBObject) paraXs
					.get(i)).get(WorkTimeProgram.F__ID);
			if (paraXId.equals(typeId)) {
				String paraXDesc = (String) ((DBObject) paraXs
						.get(i)).get(WorkTimeProgram.F_DESC);
				return programDesc + "|" + paraXDesc;
			}
		}
		return "无法获取对应的工时类型";
	}
}
