package com.sg.business.model;

import java.util.Collection;
import java.util.Iterator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.sg.business.model.event.IEventAction;
import com.sg.business.model.event.IWorkFilter;
import com.sg.business.model.registry.WorkEventConfigurator;
import com.sg.business.model.registry.WorkEventRegistry;
import com.sg.widgets.registry.config.Configurator;

public class ModelActivator implements BundleActivator {

	public static final String PLUGIN_ID = "com.sg.business.model";
	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	private WorkEventRegistry workEventRegistry;
	private static ModelActivator PLUGINS;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext bundleContext) throws Exception {
		ModelActivator.context = bundleContext;
		PLUGINS = this;
		loadWorkEvent();
	}

	private void loadWorkEvent() {
		workEventRegistry = new WorkEventRegistry();
		workEventRegistry.init();
	}

	public static void executeEvent(Work work,String eventType) throws Exception{
		Collection<Configurator> confs = PLUGINS.workEventRegistry.getConfigurators();
		Iterator<Configurator> iter = confs.iterator();
		while(iter!=null&&iter.hasNext()){
			WorkEventConfigurator conf = (WorkEventConfigurator) iter.next();
			if(eventType.equals(conf.getEventType())){
				IWorkFilter filter = conf.getWorkFilter();
				IEventAction action = conf.getEventAction();
				if(filter!=null&&filter.filter(work)&&action!=null){
					action.run(work);
				}
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		ModelActivator.context = null;
	}

}
