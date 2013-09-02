package com.sg.business.model;

import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.model.NodeAssignment;
import com.sg.business.model.check.CheckListItem;
import com.sg.business.model.check.ICheckListItem;
import com.sg.business.resource.BusinessResource;

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

	public static String getLifecycleStatusText(String lifecycleValue) {
		if(ILifecycle.STATUS_CANCELED_VALUE.equals(lifecycleValue)){
			return ILifecycle.STATUS_CANCELED_TEXT;
		}
		if(ILifecycle.STATUS_FINIHED_VALUE.equals(lifecycleValue)){
			return ILifecycle.STATUS_FINIHED_TEXT;
		}
		if(ILifecycle.STATUS_NONE_VALUE.equals(lifecycleValue)){
			return ILifecycle.STATUS_NONE_TEXT;
		}
		if(ILifecycle.STATUS_ONREADY_VALUE.equals(lifecycleValue)){
			return ILifecycle.STATUS_ONREADY_TEXT;
		}
		if(ILifecycle.STATUS_PAUSED_VALUE.equals(lifecycleValue)){
			return ILifecycle.STATUS_PAUSED_TEXT;
		}
		if(ILifecycle.STATUS_WIP_VALUE.equals(lifecycleValue)){
			return ILifecycle.STATUS_WIP_TEXT;
		}
		return null;
	}
	
	public static Image getLifecycleStatusImage(String lifecycleValue) {
		if(ILifecycle.STATUS_CANCELED_VALUE.equals(lifecycleValue)){
			return BusinessResource.getImage(BusinessResource.IMAGE_CANCELED_16);
		}
		if(ILifecycle.STATUS_FINIHED_VALUE.equals(lifecycleValue)){
			return BusinessResource.getImage(BusinessResource.IMAGE_FINISHED_16);
		}
		if(ILifecycle.STATUS_NONE_VALUE.equals(lifecycleValue)){
			return BusinessResource.getImage(BusinessResource.IMAGE_PREPARING_16);
		}
		if(ILifecycle.STATUS_ONREADY_VALUE.equals(lifecycleValue)){
			return BusinessResource.getImage(BusinessResource.IMAGE_READY_16);
		}
		if(ILifecycle.STATUS_PAUSED_VALUE.equals(lifecycleValue)){
			return BusinessResource.getImage(BusinessResource.IMAGE_PAUSEED_16);
		}
		if(ILifecycle.STATUS_WIP_VALUE.equals(lifecycleValue)){
			return BusinessResource.getImage(BusinessResource.IMAGE_WIP_16);
		}
		return null;
	}
	
	
}
