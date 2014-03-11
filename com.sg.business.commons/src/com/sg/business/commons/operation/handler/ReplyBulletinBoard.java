package com.sg.business.commons.operation.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.BulletinBoard;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.viewer.ViewerControl;

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
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters, IStructuredSelection selection) {
		// �жϵ�ǰѡ���Ƿ�Ϊ�����
		if (selected instanceof BulletinBoard) {
			// ��ȡ��ǰѡ��Ĺ����
			BulletinBoard bulletinboard = (BulletinBoard) selected;
			// �����¹����
			BulletinBoard reply = bulletinboard.makeReply(null);

			reply.setParentPrimaryObject(selected);
			reply.addEventListener(vc);
			// ʹ�ûظ��༭�����´����Ĺ����
//			if (bulletinboard.getProjectId() != null) {
				try {
					DataObjectDialog.openDialog(reply,
							BulletinBoard.EDITOR_CREATE, true, null);
				} catch (Exception e) {
					MessageUtil.showToast(e);
				}
//			} else {
//				try {
//					DataObjectDialog.openDialog(reply,
//							BulletinBoard.EDITOR_CREATE, true, null);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
		}

	}

}
