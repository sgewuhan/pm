package com.sg.business.work.launch;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Section;

import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.model.ModelService;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.business.model.IProcessControl;
import com.sg.business.model.Organization;
import com.sg.business.model.Work;
import com.sg.business.model.WorkDefinition;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.part.SimpleSection;

public class ConfirmPage extends WizardPage {

	private Label workName;

	private Text workSummary;

	private Label planStart;

	private Label planFinish;

	private Label workDefinition;

	private Label processSummary;

	private Work work;

	private Composite content;

	private boolean created;

	private Button startImmediately;

	private boolean hasError;

	private boolean hasWarning;

	protected ConfirmPage() {
		super("WORKFLOW_CONFIRM_PAGE");
		setTitle("��ȷ����Ҫ����Ĺ���");
		setImageDescriptor(BusinessResource
				.getImageDescriptor(BusinessResource.IMAGE_WORKFLOW_72));
	}

	@Override
	public void createControl(Composite parent) {
		//
		content = new Composite(parent, SWT.NONE);
		content.setLayout(new GridLayout());

		Section section = createBasicInfoPanel(content);
		section.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));

		section = createWorkflowSettingPanel(content);
		section.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));

		startImmediately = new Button(content, SWT.CHECK);
		startImmediately.setText("�Ƿ�������ʼ����");
		startImmediately.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				((LaunchWorkWizard) getWizard())
						.setStartWorkWhenFinsh(startImmediately.getSelection());
				super.widgetSelected(e);
			}
		});
		Label label = new Label(content, SWT.WRAP);
		label.setText("���ѡ�д�����������ʱ�������������Զ���ʼ��������������̣�����Ҳ���Զ�������\n�����ѡ�д��������������׼��״̬���������޸ĺ����ֶ�ִ��\"��ʼ\"������");
		setControl(content);
		setPageComplete(true);

		created = true;
		refresh();
	}

	private Section createWorkflowSettingPanel(Composite parent) {
		//
		SimpleSection section = new SimpleSection(parent, Section.EXPANDED
				| Section.SHORT_TITLE_BAR );
		section.setText("����������");
		processSummary = new Label(section, SWT.WRAP);
		processSummary.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		section.setClient(processSummary);
		return section;
	}

	private Section createBasicInfoPanel(Composite parent) {
		SimpleSection section = new SimpleSection(parent, Section.EXPANDED
				| Section.SHORT_TITLE_BAR );
		section.setText("����������Ϣ");
		Composite panel = new Composite(section, SWT.NONE);
		panel.setLayout(new GridLayout(4, false));
		Label label = new Label(panel, SWT.NONE);
		label.setText("����: ");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1,
				1));

		workName = new Label(panel, SWT.NONE);
		workName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				3, 1));

		label = new Label(panel, SWT.NONE);
		label.setText("˵��: ");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4,
				1));

		workSummary = new Text(panel, SWT.MULTI|SWT.WRAP);
		GridData layoutData = new GridData(SWT.FILL, SWT.TOP, false, false,
				4, 1);
		layoutData.heightHint = 40;
		workSummary.setLayoutData(layoutData);
		workSummary.setEditable(false);

		label = new Label(panel, SWT.NONE);
		label.setText("�ƻ���ʼ: ");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1,
				1));

		planStart = new Label(panel, SWT.NONE);
		planStart.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		label = new Label(panel, SWT.NONE);
		label.setText("�ƻ����: ");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1,
				1));

		planFinish = new Label(panel, SWT.NONE);
		planFinish.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		label = new Label(panel, SWT.NONE);
		label.setText("��������: ");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1,
				1));

		workDefinition = new Label(panel, SWT.NONE);
		workDefinition.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 3, 1));

		section.setClient(panel);
		return section;
	}

	public void setInput(Work work) {
		this.work = work;
	}

	public void refresh() {
		if (work == null) {
			return;
		}
		if(!created){
			return;
		}
		
		Object value = work.getDesc();
		if (value != null) {
			workName.setText("" + value);
		} else {
			workName.setText("");
		}

		value = work.getValue(Work.F_DESCRIPTION);
		if (value != null) {
			workSummary.setText("" + value);
		} else {
			workSummary.setText("");
		}

		value = work.getValue(Work.F_WORK_DEFINITION_ID);
		if (value instanceof ObjectId) {
			WorkDefinition workd = ModelService.createModelObject(
					WorkDefinition.class, (ObjectId) value);
			if (workd != null && !workd.isEmpty()) {
				String label = workd.getLabel();
				Organization org = workd.getOrganization();
				workDefinition.setText(label + "[" + org + "]");
			} else {
				workDefinition.setText("");
			}
		} else {
			workDefinition.setText("");
		}

		Date date = work.getPlanStart();
		if (date != null) {
			planStart.setText(String.format(Utils.FORMATE_DATE_COMPACT_SASH, date));
		}

		date = work.getPlanFinish();
		if (date != null) {
			planFinish.setText(String.format(Utils.FORMATE_DATE_COMPACT_SASH, date));
		}

		if (work.isExecuteWorkflowActivateAndAvailable()) {
			StringBuffer sb = new StringBuffer();
			// ��ȡ������Ϣ
			IProcessControl ipc = work.getAdapter(IProcessControl.class);
			DroolsProcessDefinition pd = ipc
					.getProcessDefinition(Work.F_WF_EXECUTE);
			sb.append("����: ");
			sb.append(pd);
			sb.append("<br/>");
			List<String[]> result = ipc.checkProcessRunnable(Work.F_WF_EXECUTE);
			hasError = false;
			hasWarning = false;
			for(int i=0;i<result.size();i++){
				String[] res = result.get(i);
				if(res[0].equals("error")){
					String imageUrl = FileUtil.getImageURL(BusinessResource.IMAGE_ERROR_16,
							BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
					sb.append("<img src='" + imageUrl
							+ "' style='float:left;padding:6px' width='16' height='16' />");
					hasError = true;
				}else if(res[0].equals("info")){
					
				}else if(res[0].equals("warning")){
					String imageUrl = FileUtil.getImageURL(BusinessResource.IMAGE_WARNING_16,
							BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER);
					sb.append("<img src='" + imageUrl
							+ "' style='float:left;padding:6px' width='16' height='16' />");
					hasWarning = true;
				}
				
				sb.append(res[1]);
				sb.append("<br/>");
			}
			processSummary.setText(sb.toString());
			
			if(hasError){
				setMessage("�������������ô���һЩ���⣬�ڽ��������ǰ���������޷�������",DialogPage.ERROR);
				startImmediately.setEnabled(false);
			}else if(hasWarning){
				setMessage("�������������ô���һЩ���⣬�����������Щ���������������",DialogPage.WARNING);
				startImmediately.setEnabled(true);
			}else{
				setMessage(null);
				startImmediately.setEnabled(true);
			}
		} else {
			processSummary.setText("��������û��Ԥ������");
		}

		content.layout();
	}

}
