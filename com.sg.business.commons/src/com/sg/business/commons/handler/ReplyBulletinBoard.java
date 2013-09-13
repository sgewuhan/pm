package com.sg.business.commons.handler;

import org.eclipse.core.commands.ExecutionEvent;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.BulletinBoard;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectEditor;
import com.sg.widgets.viewer.ViewerControl;
import com.sg.widgets.part.editor.DataObjectDialog;

/**
 * �ظ���ť����¼�
 * 
 * @author gdiyang
 * 
 */
public class ReplyBulletinBoard extends AbstractNavigatorHandler {

	/**
	 * ����ظ���ťִ���¼�
	 */
	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		// �жϵ�ǰѡ���Ƿ�Ϊ�����
		if (selected instanceof BulletinBoard) {
			// ��ȡ��ǰѡ��Ĺ����
			BulletinBoard bulletinboard = (BulletinBoard) selected;
			// �����¹����
			BulletinBoard reply = ModelService
					.createModelObject(BulletinBoard.class);
			// �����¹������ϼ�����ID
			reply.setValue(BulletinBoard.F_PARENT_BULLETIN,
					bulletinboard.get_id());
			reply.setValue(BulletinBoard.F_PROJECT_ID,
					bulletinboard.getProjectId());
			ViewerControl vc = getCurrentViewerControl(event);
			reply.setParentPrimaryObject(selected);
			reply.addEventListener(vc);
			// ʹ�ûظ��༭�����´����Ĺ����
			if (bulletinboard.getProjectId() != null) {
				try {
					DataObjectDialog.openDialog(bulletinboard,
							BulletinBoard.EDITOR_CREATE, true, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					DataObjectEditor.open(reply, BulletinBoard.EDITOR_CREATE,
							true, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

}