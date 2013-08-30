package com.sg.business.model;

import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.model.NodeAssignment;
import com.sg.business.model.check.CheckListItem;
import com.sg.business.model.check.ICheckListItem;

public class ModelUtil {

	public static boolean checkProcessInternal(Project project,IProcessControlable pc,
			List<ICheckListItem> result,
			Map<ObjectId, List<PrimaryObject>> raMap, String title,
			String process, String editorId, String pageId) {
		boolean passed = true;
		List<PrimaryObject> ra;
		if (pc.isWorkflowActivate(process)) {
			// 如果流程已经激活，需要判断是否所有的actor都指派
			DroolsProcessDefinition pd = pc.getProcessDefinition(process);
			List<NodeAssignment> nalist = pd.getNodesAssignment();
			for (int i = 0; i < nalist.size(); i++) {
				NodeAssignment na = nalist.get(i);
				if(!na.isNeedAssignment()){
					continue;
				}
				String nap = na.getNodeActorParameter();
				String userId = pc.getProcessActionActor(process, nap);
				if (userId == null) {
					// 检查角色
					ProjectRole role = pc.getProcessActionAssignment(process,
							nap);
					if (role == null) {
						CheckListItem checkItem = new CheckListItem(title,
								"流程活动无法确定执行人。" + "活动名称：[" + na.getNodeName()
										+ "]", "请在提交前设定。", ICheckListItem.ERROR);
						checkItem.setData(project);
						checkItem.setEditorId(editorId);
						checkItem.setEditorPageId(pageId);
						checkItem.setSelection((PrimaryObject) pc);
						result.add(checkItem);
						passed = false;
					} else {
						ra = raMap.get(role.get_id());
						if (ra == null || ra.isEmpty()) {
							CheckListItem checkItem = new CheckListItem(title,
									"流程活动执行角色没有对应人员。" + "活动名称：["
											+ na.getNodeName() + "]",
									"\n请在提交前设定。", ICheckListItem.ERROR);
							checkItem.setData(project);
							checkItem.setEditorId(editorId);
							checkItem.setEditorPageId(pageId);
							checkItem.setSelection((PrimaryObject) pc);
							result.add(checkItem);
							passed = false;
						} else if (ra.size() > 1) {
							CheckListItem checkItem = new CheckListItem(title,
									"流程的活动指定了多名人员。" + "活动名称：["
											+ na.getNodeName() + "]",
									"流程启动后这些人员中的任一人都将可执行该活动。"

									+ "\n如果您不希望这样，请在提交前设定",
									ICheckListItem.WARRING);
							checkItem.setData(project);
							checkItem.setEditorId(editorId);
							checkItem.setEditorPageId(pageId);
							checkItem.setSelection((PrimaryObject) pc);
							result.add(checkItem);
							passed = false;
						}
					}
				}
			}
		}
		return passed;
	}
}
