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
			// ��������Ѿ������Ҫ�ж��Ƿ����е�actor��ָ��
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
					// ����ɫ
					ProjectRole role = pc.getProcessActionAssignment(process,
							nap);
					if (role == null) {
						CheckListItem checkItem = new CheckListItem(title,
								"���̻�޷�ȷ��ִ���ˡ�" + "����ƣ�[" + na.getNodeName()
										+ "]", "�����ύǰ�趨��", ICheckListItem.ERROR);
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
									"���̻ִ�н�ɫû�ж�Ӧ��Ա��" + "����ƣ�["
											+ na.getNodeName() + "]",
									"\n�����ύǰ�趨��", ICheckListItem.ERROR);
							checkItem.setData(project);
							checkItem.setEditorId(editorId);
							checkItem.setEditorPageId(pageId);
							checkItem.setSelection((PrimaryObject) pc);
							result.add(checkItem);
							passed = false;
						} else if (ra.size() > 1) {
							CheckListItem checkItem = new CheckListItem(title,
									"���̵Ļָ���˶�����Ա��" + "����ƣ�["
											+ na.getNodeName() + "]",
									"������������Щ��Ա�е���һ�˶�����ִ�иû��"

									+ "\n�������ϣ�������������ύǰ�趨",
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
