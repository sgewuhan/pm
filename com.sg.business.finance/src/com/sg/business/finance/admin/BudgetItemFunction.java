package com.sg.business.finance.admin;

import com.mobnut.admin.functions.FunctionEditorExcutable;

public class BudgetItemFunction extends FunctionEditorExcutable {

    String editorId = "com.sg.business.finance.budgetitemeditor";

    @Override
    protected String getName() {
        return "Ô¤Ëã¿ÆÄ¿";
    }

    @Override
    protected String getEditorId() {
        return editorId;
    }
}
