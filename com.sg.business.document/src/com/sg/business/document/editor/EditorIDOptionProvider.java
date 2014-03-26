package com.sg.business.document.editor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.sg.widgets.Widgets;
import com.sg.widgets.commons.options.IFieldOptionProvider;
import com.sg.widgets.registry.config.Configurator;
import com.sg.widgets.registry.config.DataEditorConfigurator;
import com.sg.widgets.registry.config.Option;

public class EditorIDOptionProvider implements IFieldOptionProvider {

	public EditorIDOptionProvider() {
	}

	@Override
	public Option getOption(Object input, Object data, String key, Object value) {
		Collection<Configurator> confs = Widgets.getEditorRegistry()
				.getConfigurators();
		List<Option> options = new ArrayList<Option>();
		Iterator<Configurator> iter = confs.iterator();
		while(iter.hasNext()){
			DataEditorConfigurator conf = (DataEditorConfigurator) iter.next();
			String collection = conf.getCollectionName();
			if(collection.equals("document")){
				options.add(new Option(conf.getId(),conf.getName()+"["+conf.getId()+"]",conf.getId(),null));
			}
		}
		Option result = new Option("documentEditor", "documentEditorLabel",
				"documentEditor", options.toArray(new Option[]{}));
		return result;
	}

}
