package com.sg.business.commons.ui;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.widgets.MarkupValidator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.mobnut.db.model.PrimaryObject;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.commons.model.IEditorInputFactory;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.part.editor.DataObjectEditor;
import com.sg.widgets.part.editor.DataObjectWizard;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.view.PrimaryObjectDetailFormView;
import com.sg.widgets.part.view.SideBarNavigator;
import com.sg.widgets.registry.config.DataEditorConfigurator;

@SuppressWarnings("restriction")
public class UIFrameworkUtils {

	/**
	 * �����
	 */
	public static final String VIEW_HOME_SIDEBAR = "homenavigator";

	/**
	 * ����һ���û�����ҳ��ͼ
	 */
	public static final String VIEW_HOME_COMMON = "pm2.work.detail";

	/**
	 * ����͸��ͼ
	 */
	public static final String PERSPECTIVE_WORK = "perspective.work";

	public static final int NAVIGATE_AUTOSELECT = 0;
	public static final int NAVIGATE_BY_VIEW = 1;
	public static final int NAVIGATE_BY_EDITOR = 2;
	public static final int NAVIGATE_BY_DIALOG = 3;
	public static final int NAVIGATE_BY_WIZARD = 4;

	/**
	 * ��ȡ��ҳ������ͼ
	 * 
	 * @return
	 */
	public static PrimaryObjectDetailFormView getHomePart() {
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		IViewReference[] vr = page.getViewReferences();
		for (int i = 0; i < vr.length; i++) {
			IViewPart _view = vr[i].getView(false);
			if (_view instanceof PrimaryObjectDetailFormView) {
				PrimaryObjectDetailFormView pv = (PrimaryObjectDetailFormView) _view;
				if (pv.isHomeView()) {
					return pv;
				}
			}
		}

		return null;
	}

	/**
	 * ��ȡ�����
	 * 
	 * @return
	 */
	public static SideBarNavigator getSidebar() {
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		return (SideBarNavigator) page.findView(VIEW_HOME_SIDEBAR);
	}

	/**
	 * ˢ�²����
	 */
	public static void refreshSidebar() {
		SideBarNavigator sidebar = getSidebar();
		if (sidebar != null) {
			sidebar.doRefresh();
		}
	}

	/**
	 * ˢ����ҳ������
	 * 
	 * @param reloadInput
	 */
	public static void refreshHomePart(boolean reloadInput) {
		PrimaryObjectDetailFormView home = getHomePart();
		home.loadMaster(reloadInput);
	}

	public static PrimaryObjectDetailFormView navigateHome() {
		PrimaryObjectDetailFormView home = getHomePart();
		home.cleanUI();
		home.cleanInput();
		home.goHome();
		return home;
	}

	/**
	 * ������嵼�����µĶ����ص���ҳ
	 * @param editable 
	 * 
	 * @return
	 */
	public static Object navigateTo(PrimaryObject po, int navigateMode, boolean editable) {
		if (po == null) {
			return null;
		} else {
			PrimaryObjectDetailFormView home = getHomePart();
			IEditorInputFactory inf = po.getAdapter(IEditorInputFactory.class);
			if (navigateMode == NAVIGATE_BY_VIEW
					|| navigateMode == NAVIGATE_AUTOSELECT) {
				if (home != null && inf != null) {
					PrimaryObjectEditorInput input = inf.getInput(null);
					if (input != null) {
						home.setInput(input);
						return home;
					}
				}

				if (navigateMode != NAVIGATE_AUTOSELECT) {
					MessageUtil.showToast("�޷�����ҳ�д򿪶���", SWT.ICON_ERROR);
					return null;
				}
			}

			DataEditorConfigurator config = inf.getEditorConfig(null);

			if (navigateMode == NAVIGATE_BY_EDITOR
					|| navigateMode == NAVIGATE_AUTOSELECT) {
				try {
					return DataObjectEditor.open(po, config, editable, null);
				} catch (Exception e) {
					if (navigateMode != NAVIGATE_AUTOSELECT) {
						MessageUtil.showToast(e);
						return null;
					}
				}
			}

			if (navigateMode == NAVIGATE_BY_DIALOG
					|| navigateMode == NAVIGATE_AUTOSELECT) {
				try {
					return DataObjectDialog.openDialog(po, config, editable, null,
							po.getLabel());
				} catch (Exception e) {
					if (navigateMode != NAVIGATE_AUTOSELECT) {
						MessageUtil.showToast(e);
						return null;
					}
				}
			}

			if (navigateMode == NAVIGATE_BY_WIZARD
					|| navigateMode == NAVIGATE_AUTOSELECT) {
				try {
					return DataObjectWizard.open(po, config, editable, null);
				} catch (Exception e) {
					if (navigateMode != NAVIGATE_AUTOSELECT) {
						MessageUtil.showToast(e);
						return null;
					}
				}
			}
		}
		return null;

	}

	public static void enableMarkup(Control control) {
		control.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		control.setData(MarkupValidator.MARKUP_VALIDATION_DISABLED,
				Boolean.TRUE);
	}

}
