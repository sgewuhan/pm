package com.sg.business.finance.admin;

import com.mobnut.admin.functions.FunctionEditorExcutable;
import com.sg.business.finance.nls.Messages;

public class BudgetItemFunction extends FunctionEditorExcutable {

    String editorId = "com.sg.business.finance.budgetitemeditor"; //$NON-NLS-1$

    @Override
    protected String getName() {
        return Messages.get().BudgetItemFunction_1;
    }

    @Override
    protected String getEditorId() {
        return editorId;
    }
}
