package com.sg.xdeprecated.commons.flow;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.jface.resource.ImageDescriptor;

import com.sg.business.commons.CommonsActivator;
import com.sg.xdeprecated.commons.flow.model.Activity;
import com.sg.xdeprecated.commons.flow.model.ParallelActivity;
import com.sg.xdeprecated.commons.flow.model.SequentialActivity;

/**
 * Handles the creation of the palette for the Flow Editor.
 * 
 * @author Daniel Lee
 */
public class FlowEditorPaletteFactory {

	private static List<PaletteEntry> createCategories(PaletteRoot root) {
		List<PaletteEntry> categories = new ArrayList<PaletteEntry>();
		categories.add(createControlGroup(root));
		categories.add(createComponentsDrawer());
		return categories;
	}

	private static PaletteContainer createComponentsDrawer() {

		PaletteDrawer drawer = new PaletteDrawer("Components", null); //$NON-NLS-1$

		List<PaletteEntry> entries = new ArrayList<PaletteEntry>();

		CombinedTemplateCreationEntry combined = new CombinedTemplateCreationEntry(
				"Activity", "Create a new Activity Node", Activity.class, //$NON-NLS-1$ //$NON-NLS-2$
				new SimpleFactory(Activity.class),
				ImageDescriptor.createFromFile(CommonsActivator.class,
						"image/gear16.gif"), ImageDescriptor.createFromFile( //$NON-NLS-1$
						Activity.class, "image/gear16.gif")); //$NON-NLS-1$
		entries.add(combined);

		combined = new CombinedTemplateCreationEntry("Sequential Activity", //$NON-NLS-1$
				"Create a Sequential Activity", SequentialActivity.class, //$NON-NLS-1$
				new SimpleFactory(SequentialActivity.class),
				ImageDescriptor.createFromFile(CommonsActivator.class,
						"image/sequence16.gif"), //$NON-NLS-1$
				ImageDescriptor.createFromFile(CommonsActivator.class,
						"image/sequence16.gif")); //$NON-NLS-1$
		entries.add(combined);

		combined = new CombinedTemplateCreationEntry("Parallel Activity", //$NON-NLS-1$
				"Create a  Parallel Activity", ParallelActivity.class, //$NON-NLS-1$
				new SimpleFactory(ParallelActivity.class),
				ImageDescriptor.createFromFile(CommonsActivator.class,
						"image/parallel16.gif"), //$NON-NLS-1$
				ImageDescriptor.createFromFile(CommonsActivator.class,
						"image/parallel16.gif")); //$NON-NLS-1$
		entries.add(combined);

		drawer.addAll(entries);
		return drawer;
	}

	private static PaletteContainer createControlGroup(PaletteRoot root) {
		PaletteGroup controlGroup = new PaletteGroup("Control Group"); //$NON-NLS-1$

		List<PaletteEntry> entries = new ArrayList<PaletteEntry>();

		ToolEntry tool = new SelectionToolEntry();
		entries.add(tool);
		root.setDefaultEntry(tool);

		tool = new MarqueeToolEntry();
		entries.add(tool);

		PaletteSeparator sep = new PaletteSeparator(
				"com.sg.xdeprecated.commons.flow.flowplugin.sep2"); //$NON-NLS-1$
		sep.setUserModificationPermission(PaletteEntry.PERMISSION_NO_MODIFICATION);
		entries.add(sep);

		tool = new ConnectionCreationToolEntry("Connection Creation", //$NON-NLS-1$
				"Creating connections", null, ImageDescriptor.createFromFile( //$NON-NLS-1$
						CommonsActivator.class, "image/connection16.gif"), //$NON-NLS-1$
				ImageDescriptor.createFromFile(Activity.class,
						"image/connection16.gif")); //$NON-NLS-1$
		entries.add(tool);
		controlGroup.addAll(entries);
		return controlGroup;
	}

	/**
	 * Creates the PaletteRoot and adds all Palette elements.
	 * 
	 * @return the root
	 */
	public static PaletteRoot createPalette() {
		PaletteRoot flowPalette = new PaletteRoot();
		flowPalette.addAll(createCategories(flowPalette));
		return flowPalette;
	}

}
