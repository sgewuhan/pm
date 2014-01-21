package com.sg.business.work.launch;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.sg.business.model.Work;
import com.sg.business.model.WorkDefinition;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.BasicWizardPage;
import com.sg.widgets.registry.config.BasicPageConfigurator;
import com.sg.widgets.registry.config.DataEditorConfigurator;

public class LaunchWorkWizard extends Wizard implements IWorkbenchWizard {

	private static final String PAGE_LAUNCH_WORK_BASICPAGE = "launch.work.basicpage"; //$NON-NLS-1$
	private SelectWorkDefinitionPage selectWorkDefinitionPage;
	private PrimaryObjectEditorInput editorInput;
	private BasicWizardPage basicPage;
	private WorkFlowSettingPage flowSettingPage;
	private boolean startWorkWhenFinish;
	private ConfirmPage comfirmPage;
	private WorkDefinition initWorkDefinition;

	public static LaunchWorkWizard OPEN(WorkDefinition workd) {
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();

		if (window != null) {
			LaunchWorkWizard wiz = new LaunchWorkWizard(workd);
			Shell shell = window.getShell();
			WizardDialog wizardDialog = new WizardDialog(shell, wiz) {
				@Override
				protected Point getInitialSize() {
					Point size = super.getInitialSize();
					return new Point(500, size.y);
				}
			};
			wizardDialog.open();
			return wiz;
		}
		return null;
	}

	public static LaunchWorkWizard OPEN() {
		return OPEN(null);
	}

	public LaunchWorkWizard(WorkDefinition workd) {
		initInput(workd);
	}

	private void initInput(WorkDefinition workd) {
		Work work = ModelService.createModelObject(Work.class);
		IContext context = new CurrentAccountContext();
		work.setValue(Work.F_CHARGER, context.getAccountInfo().getConsignerId());// 设置负责人为当前用户

		DataEditorConfigurator editor = (DataEditorConfigurator) Widgets
				.getEditorRegistry().getConfigurator("editor.work.launch"); //$NON-NLS-1$
		editorInput = new PrimaryObjectEditorInput(work, editor, null);
		editorInput.setEditable(true);
		editorInput.setNeedHostPartListenSaveEvent(false);
		editorInput.setContext(new CurrentAccountContext());

		if (workd != null) {
			setWorkDefinition(workd);
			setWindowTitle(Messages.get().LaunchWorkWizard_1 + ":"
					+ workd.getLabel());
		} else {
			setWindowTitle(Messages.get().LaunchWorkWizard_1);
		}
		
		this.initWorkDefinition = workd;

	}

	@Override
	public void addPages() {
		if (initWorkDefinition == null) {
			selectWorkDefinitionPage = new SelectWorkDefinitionPage();
			addPage(selectWorkDefinitionPage);
		}

		BasicPageConfigurator conf = (BasicPageConfigurator) Widgets
				.getPageRegistry().getConfigurator(PAGE_LAUNCH_WORK_BASICPAGE);
		basicPage = new BasicWizardPage(conf);
		basicPage.setInput(editorInput);
		addPage(basicPage);
		
		// 判断有无流程，如果无流程，则返回空
		Work work = (Work) editorInput.getData();
		if (work.isExecuteWorkflowActivateAndAvailable()) {
			addPage( getFlowSettingPage());
		}
		addPage( getConfirmPage());
	}
	

	@Override
	public boolean performFinish() {
		Work work = (Work) editorInput.getData();
		IContext context = new CurrentAccountContext();
		try {
			work.doSave(context);
			if (startWorkWhenFinish) {
				work.doStart(context);
			}
			return true;
		} catch (Exception e) {
			MessageUtil.showToast(e);
			return false;
		}

	}

	public void setWorkDefinition(WorkDefinition workd) {
		// 设置流程
		Work work = (Work) editorInput.getData();
		try {
			if (workd != null) {
				workd.makeStandloneWork(work, editorInput.getContext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page == basicPage) {
			// 判断有无流程，如果无流程，则返回空
			Work work = (Work) editorInput.getData();
			if (!work.isExecuteWorkflowActivateAndAvailable()) {
				return getConfirmPage();
			} else {
				return getFlowSettingPage();
			}
		} else if (page == flowSettingPage) {
			return getConfirmPage();
		} else if (page == comfirmPage) {
			return null;
		} else if (page == selectWorkDefinitionPage) {
			return basicPage;
		} else {
			return null;
		}
	}

	private IWizardPage getFlowSettingPage() {
		if (flowSettingPage == null) {
			flowSettingPage = new WorkFlowSettingPage();
			flowSettingPage.setInput(editorInput);
			flowSettingPage.setWizard(this);
		} else {
			flowSettingPage.refresh();
		}
		return flowSettingPage;
	}

	private IWizardPage getConfirmPage() {
		if (comfirmPage == null) {
			comfirmPage = new ConfirmPage();
			comfirmPage.setInput((Work) editorInput.getData());
			comfirmPage.setWizard(this);
		} else {
			comfirmPage.refresh();
		}
		return comfirmPage;

	}

	public void setStartWorkWhenFinsh(boolean selection) {
		this.startWorkWhenFinish = selection;
	}

	public PrimaryObjectEditorInput getInput() {
		return editorInput;
	}

	@Override
	public boolean canFinish() {
		return super.canFinish();
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}
}
