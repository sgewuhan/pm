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
	 * 侧边拦
	 */
	public static final String VIEW_HOME_SIDEBAR = "homenavigator";

	/**
	 * 用于一般用户的首页视图
	 */
	public static final String VIEW_HOME_COMMON = "pm2.work.detail";

	/**
	 * 工作透视图
	 */
	public static final String PERSPECTIVE_WORK = "perspective.work";

	public static final int NAVIGATE_AUTOSELECT = 0;
	public static final int NAVIGATE_BY_VIEW = 1;
	public static final int NAVIGATE_BY_EDITOR = 2;
	public static final int NAVIGATE_BY_DIALOG = 3;
	public static final int NAVIGATE_BY_WIZARD = 4;

	/**
	 * 获取首页内容视图
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
	 * 获取侧边拦
	 * 
	 * @return
	 */
	public static SideBarNavigator getSidebar() {
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		return (SideBarNavigator) page.findView(VIEW_HOME_SIDEBAR);
	}

	/**
	 * 刷新侧边拦
	 */
	public static void refreshSidebar() {
		SideBarNavigator sidebar = getSidebar();
		if (sidebar != null) {
			sidebar.doRefresh();
		}
	}

	/**
	 * 刷新首页内容区
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
	 * 导航面板导航到新的对象或回到首页
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
					MessageUtil.showToast("无法在主页中打开对象", SWT.ICON_ERROR);
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
