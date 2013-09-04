package com.sg.business.model;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
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
	
	public static  void appendMessageContent(Message message, String contentLine) {
		Object value = message.getValue(Message.F_CONTENT);
		if(!(value instanceof String)){
			message.setValue(Message.F_CONTENT, "<span style='font-size:14px'>"+"您好: "+"</span><br/><br/>"+"您收到了提交的项目计划信息。<br/>您在计划中将负责和参与以下工作：<br/><br/>"+contentLine);
		}else{
			message.setValue(Message.F_CONTENT, (String)value+"<br/>"+contentLine);
		}
	}

	public static  Message createProjectCommitMessage(String userId) {
		Message message = ModelService.createModelObject(Message.class);
		message.setValue(Message.F_RECIEVER, userId);
		message.setValue(Message.F_DESC, "项目计划提交通知");
		message.setValue(Message.F_ISHTMLBODY, Boolean.TRUE);
		return message;
	}
	
	public static  void appendWorkflowActorMessage(Work work,Map<String, Message> messageList, String processKey, String processName) {
		Message message;
		String userId;
		if(work.isWorkflowActivate(processKey)){
			DBObject map = work.getProcessActorsMap(processKey);
			if(map != null){
				Iterator<String> iter = map.keySet().iterator();
				while(iter.hasNext()){
					userId = iter.next();
					message = messageList.get(userId);
					if (message == null) {
						message = ModelUtil.createProjectCommitMessage(userId);
						messageList.put(userId, message);
					}
					ModelUtil.appendMessageContent(message, "参与工作流程，"+processName + ": "
							+ work.getLabel());
					message.appendTargets(work, Work.EDITOR, Boolean.TRUE);
				}
			}
		}
	}
	
	public static  void appendWorkflowActorMessage(Project project,Map<String, Message> messageList, String processKey, String processName) {
		Message message;
		String userId;
		if(project.isWorkflowActivate(processKey)){
			DBObject map = project.getProcessActorsMap(processKey);
			if(map != null){
				Iterator<String> iter = map.keySet().iterator();
				while(iter.hasNext()){
					userId = iter.next();
					message = messageList.get(userId);
					if (message == null) {
						message = ModelUtil.createProjectCommitMessage(userId);
						messageList.put(userId, message);
					}
					ModelUtil.appendMessageContent(message, "参与项目流程，"+processName + ": "
							+ project.getLabel());
					message.appendTargets(project, Work.EDITOR, Boolean.TRUE);
				}
			}
		}
	}
}
