package com.sg.business.commons.ui.home.perf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.rap.rwt.scripting.ClientListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.mobnut.commons.util.Utils;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.widgets.ImageResource;
import com.sg.widgets.Widgets;
import com.sg.widgets.block.Block;

class ImportantSettingBlock extends Block {

	DBObject data = new BasicDBObject();
	private List<Button> blist;

	ImportantSettingBlock(Shell shell) {
		super(shell);
		setTopicText("希望关注哪些项目？");
		setButtonImage(Widgets.getImage(ImageResource.CLOSE_G_24));
	}

	void setCondition(DBObject data) {
		this.data = data;

		Iterator<Button> iter = blist.iterator();
		while (iter.hasNext()) {
			Button b = iter.next();
			Object key = b.getData("key");
			boolean selected = Boolean.TRUE.equals(data.get(key + "_check"));
			b.setSelection(selected);

			Text text = (Text) b.getData("sp");
			Object value = data.get(key+"_cnt");
			text.setText(value==null?"":value.toString());
			text.setVisible(selected);

			text = (Text) b.getData("text");
			value = data.get(key.toString());
			text.setText(value==null?"":value.toString());
			text.setVisible(selected);

			Control ctl = (Control) b.getData("ul");
			ctl.setVisible(selected);
			ImportantSettingBlock.this.layout(true);
		}
	}

	@Override
	protected void createContent(Composite parent) {
		parent.setLayout(new GridLayout(2, true));
		Composite left = new Composite(parent, SWT.NONE);
		left.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		GridLayout layout = new GridLayout(4, false);
		layout.marginWidth = 1;
		left.setLayout(layout);
		Label l = new Label(left, SWT.NONE);
		l.setText("关注的条件");
		l.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		l = new Label(left, SWT.NONE);
		l.setText("重点关注个数");
		l.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 2, 1));

		new Label(left, SWT.SEPARATOR | SWT.HORIZONTAL)
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false,
						4, 1));
		blist = new ArrayList<Button>();
		blist.add(createInput(left, "严重超期", "天以上", IImportantSetting.OVERTIME));
		blist.add(createInput(left, "严重超支", "万元以上", IImportantSetting.OVERCOST));
		blist.add(createInput(left, "严重亏损", "万元以上", IImportantSetting.HEAVYLOSS));

		Composite right = new Composite(parent, SWT.NONE);
		right.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		l = new Label(right, SWT.NONE);
		l.setText("关注的条件");
		l.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		l = new Label(right, SWT.NONE);
		l.setText("重点关注个数");
		l.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 2, 1));
		new Label(right, SWT.SEPARATOR | SWT.HORIZONTAL)
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false,
						4, 1));

		layout.marginWidth = 1;
		layout = new GridLayout(4, false);
		right.setLayout(layout);
		blist.add(createInput(right, "高利润率", "%", IImportantSetting.HIGHPROFITRATE));
		blist.add(createInput(right, "研发预算大", "万元以上", IImportantSetting.HIGHBUDGET));
		blist.add(createInput(right, "研发投入大", "万元以上", IImportantSetting.HIGHINVESTMENT));
		blist.add(createInput(right, "研发周期长", "月以上", IImportantSetting.LONGTERMDEV));
	}

	private Button createInput(Composite parent, String labelText,
			String unitText, final String key) {
		final Button enableCondition = new Button(parent, SWT.CHECK);
		enableCondition.setText(labelText);
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		enableCondition.setLayoutData(gd);

		final Text text = new Text(parent, SWT.BORDER | SWT.RIGHT);
		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent event) {
				try {
					data.put(key, text.getText());
				} catch (Exception e) {
				}
			}
		});
		text.setVisible(false);
		gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		gd.widthHint = 40;
		text.setLayoutData(gd);
		String scriptCode = Utils.getClientListener(Utils.TYPE_INTEGER);
		ClientListener clientListener = new ClientListener(scriptCode);
		text.addListener(SWT.Verify, clientListener);
		text.addListener(SWT.Modify, clientListener);

		Label unitLabel = new Label(parent, SWT.NONE);
		unitLabel.setText(unitText);
		unitLabel.setVisible(false);
		final Text sp = new Text(parent, SWT.BORDER | SWT.RIGHT);
		sp.setVisible(false);

		sp.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent event) {
				data.put(key + "_cnt", sp.getText());
			}
		});
		gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		gd.widthHint = 24;
		sp.setLayoutData(gd);
		clientListener = new ClientListener(scriptCode);
		sp.addListener(SWT.Verify, clientListener);
		sp.addListener(SWT.Modify, clientListener);

		enableCondition.setData("key", key);
		enableCondition.setData("text", text);
		enableCondition.setData("sp", sp);
		enableCondition.setData("ul", unitLabel);
		enableCondition.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean selection = enableCondition.getSelection();
				data.put(key + "_check", selection);
				Control ctl = (Control) enableCondition.getData("text");
				ctl.setVisible(selection);
				ctl = (Control) enableCondition.getData("sp");
				ctl.setVisible(selection);
				ctl = (Control) enableCondition.getData("ul");
				ctl.setVisible(selection);
				ImportantSettingBlock.this.layout(true);
			}
		});

		return enableCondition;
	}

	@Override
	protected void doDisplayData(Object data) {
	}

	@Override
	protected Object doGetData() {
		return null;
	}

	public DBObject getCondition() {
		return data;
	}

	@Override
	protected void go() {
		doSaveCondition(data);
	}

	protected void doSaveCondition(DBObject data) {

	}
}