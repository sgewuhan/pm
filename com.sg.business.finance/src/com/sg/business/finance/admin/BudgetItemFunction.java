package com.sg.business.finance.admin;

import com.mobnut.admin.functions.FunctionEditorExcutable;

public class BudgetItemFunction extends FunctionEditorExcutable {

    String editorId = "com.sg.business.finance.budgetitemeditor";

    @Override
    protected String getName() {
        return "Ԥ���Ŀ";
    }

    @Override
    protected String getEditorId() {
        return editorId;
    }
}
