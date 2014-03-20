package com.sg.business.commons.ui.home.perf;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.commons.ui.home.basic.ProjectContentBlock;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.widgets.ImageResource;
import com.sg.widgets.Widgets;
import com.sg.widgets.block.Block;
import com.sg.widgets.block.button.BusinessContentBlock;
import com.sg.widgets.block.button.ButtonBlock;

public class ImportantProjectBlock extends ButtonBlock {

	private static final String PERSPECTIVE = "perspective.project.charger";

	public ImportantProjectBlock(Composite parent) {
		super(parent);
	}

	@Override
	protected String getPerspective() {
		return PERSPECTIVE;
	}

	@Override
	protected DBCollection getCollection() {
		return DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT);
	}

	@Override
	protected Class<? extends PrimaryObject> getContentClass() {
		return Project.class;
	}

	@Override
	protected DBObject getSearchCondition() {
		//获取当前用户关注的项目
		return new BasicDBObject().append(
				Project.F_LIFECYCLE,
				new BasicDBObject().append("$in", new String[] {
						ILifecycle.STATUS_WIP_VALUE,
						ILifecycle.STATUS_NONE_VALUE,
						ILifecycle.STATUS_ONREADY_VALUE, null }));
	}

	@Override
	protected BusinessContentBlock createBlockContent(Composite contentArea,
			PrimaryObject po) {
		return new ProjectContentBlock(contentArea);
	}

	@Override
	protected void doAdd() {
		final Shell shell = new Shell(getShell(),SWT.ON_TOP);
		shell.setSize(getUnitCountX()*(BLOCKSIZE+1)-1,getUnitCountY()*(BLOCKSIZE+1)-1+TOPICSIZE);
		shell.setLocation(getParent().toDisplay(1,1));
		shell.setLayout(new FillLayout());
		createSettingBlock(shell);
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellDeactivated(ShellEvent e) {
				shell.dispose();
			}
		});
		shell.open();
	}

	private void createSettingBlock(final Shell shell) {
		Block block = new Block(shell){

			DBObject data = new BasicDBObject();
			
			@Override
			protected void go() {
				shell.dispose();
			}

			@Override
			protected void createContent(Composite parent) {
				parent.setLayout(new GridLayout(2,true));
				Composite left = new Composite(parent, SWT.NONE);
				left.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
				GridLayout layout = new GridLayout(4,false);
				layout.marginWidth = 1;
				left.setLayout(layout);
				new Label(left,SWT.NONE).setText("条件");
				new Label(left,SWT.NONE);
				new Label(left,SWT.NONE);
				new Label(left,SWT.NONE).setText("主页条数");
				
				createInput(left,"严重超期：","天以上","overtime");
				createInput(left,"严重超支：","万元以上","overcost");
				createInput(left,"长期亏损：","天以上","longtermloss");
				createInput(left,"严重亏损：","万元以上","heavyloss");
				
				Composite right = new Composite(parent, SWT.NONE);
				right.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
				new Label(right,SWT.NONE).setText("条件");
				new Label(right,SWT.NONE);
				new Label(right,SWT.NONE);
				new Label(right,SWT.NONE).setText("主页条数");
				layout.marginWidth = 1;
				layout = new GridLayout(4,false);
				right.setLayout(layout);
				createInput(right,"利润率高：","以上","highprofitrate");
				createInput(right,"快速盈利：","天以内","fastgain");
				createInput(right,"研发预算大：","万元以上","highbudget");
				createInput(right,"研发投入大：","万元以上","highinvestment");
				createInput(right,"研发周期长：","月以上","longtermdev");
			}

			private void createInput(Composite parent,String labelText, String unitText,final String key) {
				final Button b = new Button(parent,SWT.CHECK);
				b.setText(labelText);
				b.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						data.put(key+".check", b.getSelection());
					}
				});
				
				final Text text = new Text(parent,SWT.BORDER);
				text.addModifyListener(new ModifyListener() {
					@Override
					public void modifyText(ModifyEvent event) {
						try {
							data.put(key, text.getText());
						} catch (Exception e) {
						}
					}
				});
				GridData gd = new GridData(SWT.LEFT,SWT.CENTER,false,false);
				gd.widthHint = 40;
				text.setLayoutData(gd);
				
				new Label(parent,SWT.NONE).setText(unitText);
				
				final Text sp = new Text(parent, SWT.BORDER);
				sp.addModifyListener(new ModifyListener() {
					
					@Override
					public void modifyText(ModifyEvent event) {
						data.put(key+".cnt", sp.getText());
					}
				});
				gd = new GridData(SWT.LEFT,SWT.CENTER,false,false);
				gd.widthHint = 40;
				sp.setLayoutData(gd);
			}

			@Override
			protected void doDisplayData(Object data) {
			}

			@Override
			protected Object doGetData() {
				return null;
			}
			
		};
		block.setTopicText("希望关注哪些项目？选择以下条件，系统为您自动关注。");
		block.setButtonImage(Widgets.getImage(ImageResource.CLOSE_G_24));
	}
	

	@Override
	public int getUnitCountX() {
		return 6;
	}

	@Override
	protected int getLimit() {
		return 7;
	}

	@Override
	protected void fill(DBObject dt[]) {
		int blankCount = getUnitCountX()*getUnitCountY()-1;
		switch (dt.length) {
		case 11:
			blankCount -= insert(dt,0,11,BLOCK_TYPE[0]);
			break;
		case 10:
			blankCount -= insert(dt,0,1,BLOCK_TYPE[1]);
			blankCount -= insert(dt,1,10,BLOCK_TYPE[0]);
			break;
		case 9:
			blankCount -= insert(dt,0,2,BLOCK_TYPE[1]);
			blankCount -= insert(dt,2,9,BLOCK_TYPE[0]);
			break;
		case 8:
			blankCount -= insert(dt,0,1,BLOCK_TYPE[2]);
			blankCount -= insert(dt,1,8,BLOCK_TYPE[0]);
			break;
		case 7:
			blankCount -= insert(dt,0,1,BLOCK_TYPE[2]);
			blankCount -= insert(dt,1,2,BLOCK_TYPE[1]);
			blankCount -= insert(dt,2,7,BLOCK_TYPE[0]);
			break;
		case 6:
			blankCount -= insert(dt,0,1,BLOCK_TYPE[2]);
			blankCount -= insert(dt,1,3,BLOCK_TYPE[1]);
			blankCount -= insert(dt,3,6,BLOCK_TYPE[0]);
			break;
		case 5:
			blankCount -= insert(dt,0,2,BLOCK_TYPE[2]);
			blankCount -= insert(dt,2,5,BLOCK_TYPE[0]);
			break;
		case 4:
			blankCount -= insert(dt,0,2,BLOCK_TYPE[2]);
			blankCount -= insert(dt,2,3,BLOCK_TYPE[1]);
			blankCount -= insert(dt,3,4,BLOCK_TYPE[0]);
			break;
		case 3:
			blankCount -= insert(dt,0,2,BLOCK_TYPE[2]);
			blankCount -= insert(dt,2,3,BLOCK_TYPE[1]);
			break;
		case 2:
		case 1:
			blankCount -= insert(dt,0,dt.length,BLOCK_TYPE[2]);
			break;
		default:
		}
		fillBlank(blankCount);
	}

	private int insert(DBObject[] dt, int start, int end, Point size) {
		int result = 0;
		for (int i = start; i < end; i++) {
			addContentBlock(dt[i], size);
			result+=size.x*size.y;
		}
		return result;
	}

}
