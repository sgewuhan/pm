package com.sg.business.document;

import com.sg.business.resource.nls.Messages;
import com.sg.widgets.commons.options.IOptionContribution;
import com.sg.widgets.registry.config.Option;

public class DocTypeOption implements IOptionContribution {

	public static final String[] DOCUMENT_TYPES = new String[] { 
													"generic", //$NON-NLS-1$
													"drawing",  //$NON-NLS-1$
													"design",  //$NON-NLS-1$
													"program",  //$NON-NLS-1$
													"report",  //$NON-NLS-1$
													"data",  //$NON-NLS-1$
													"book",  //$NON-NLS-1$
													"news", //$NON-NLS-1$
													"note",  //$NON-NLS-1$
													"memo",  //$NON-NLS-1$
													"mail",  //$NON-NLS-1$
													"sticky" }; //$NON-NLS-1$
	public static final String[] DOCUMENT_TEXT = new String[] {
													Messages.get().DocTypeOption_12
													,Messages.get().DocTypeOption_13
													,Messages.get().DocTypeOption_14
													,Messages.get().DocTypeOption_15
													,Messages.get().DocTypeOption_16
													,Messages.get().DocTypeOption_17
													,Messages.get().DocTypeOption_18
													,Messages.get().DocTypeOption_19
													,Messages.get().DocTypeOption_20
													,Messages.get().DocTypeOption_21
													,Messages.get().DocTypeOption_22
													,Messages.get().DocTypeOption_23};

	public DocTypeOption() {
	}

	@Override
	public String getlabel() {
		return Messages.get().DocTypeOption_24;
	}

	@Override
	public String getValue() {
		return Messages.get().DocTypeOption_25;
	}

	@Override
	public Option[] getChildren() {
		Option[] result = new Option[DOCUMENT_TYPES.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = new Option(DOCUMENT_TYPES[i],DOCUMENT_TEXT[i],DOCUMENT_TYPES[i],null);
		}
		return result;
	}
	
	

}
