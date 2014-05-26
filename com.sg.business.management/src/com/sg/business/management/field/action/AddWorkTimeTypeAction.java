package com.sg.business.management.field.action;

import java.util.Iterator;

import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.TreeItem;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.sg.business.model.WorkDefinition;
import com.sg.business.model.WorkTimeProgram;
import com.sg.widgets.commons.selector.DropdownNavigatorSelector;
import com.sg.widgets.part.editor.fields.AbstractFieldPart;
import com.sg.widgets.part.editor.fields.value.IAddTableItemHandler;
import com.sg.widgets.viewer.CTreeViewer;

public class AddWorkTimeTypeAction implements IAddTableItemHandler {

	public AddWorkTimeTypeAction() {
	}

	@Override
	public boolean addItem(final BasicDBList inputData,
			final AbstractFieldPart part) {
		//表单编辑器输入的数据，类型为工作定义
		WorkDefinition data = (WorkDefinition) part.getInput().getData();
		PrimaryObject master =null;
		if(data.isStandloneWork()){
			//此工作定义为独立工作定义时，master直接使用工作定义，因为独立工作定义使用了工时方案字段，保存了它可用的工时方案
			master=data;
		}else if(data.isGenericWork()){
			master=data;
		}else{
			//此工作定义为项目中的工作定义时，需使用项目模板获取项目模板中的工时方案
			//获取工作定义所在项目模板
			master=data.getProjectTemplate();
		}
		

		DropdownNavigatorSelector ns = new DropdownNavigatorSelector(
				"management.selectworktimetype") {
			@Override
			protected boolean isSelectEnabled(IStructuredSelection is) {
				return is != null && !is.isEmpty()
						&& is.getFirstElement() instanceof DBObject;
			}

			@Override
			protected void doOK(IStructuredSelection is) {
				if (is == null || is.isEmpty()) {
				} else {
					Iterator<?> iterator = is.iterator();
					while (iterator.hasNext()) {
						//此处value只能是DBObject的工时类型
						DBObject value = (DBObject) iterator.next();
						ObjectId workTimeTypeId = (ObjectId) value.get(WorkTimeProgram.F__ID);
						
						CTreeViewer viewer = (CTreeViewer) getNavigator().getViewer();
						TreeItem treeItem = (TreeItem) viewer.testFindItem(value);
						WorkTimeProgram workTimeProgram = (WorkTimeProgram) treeItem.getParentItem().getData();
						ObjectId workTimeProgramId = workTimeProgram.get_id();
						String key = workTimeTypeId.toString()+"@"+workTimeProgramId.toString();
						//判断表单编辑器中字段的值是否包含工时类型id和工时方案id组成的字符串时进行的处理
						if (!inputData.contains(key)) {
							inputData.add(key);
						}
					}
					part.setDirty(true);
					part.updateDataValueAndPresent();
					super.doOK(is);
				}
			}
		};
		ns.setMaster(master);
		ns.show(part.getShell(), part.getControl(), true);
		return true;

	}

}
