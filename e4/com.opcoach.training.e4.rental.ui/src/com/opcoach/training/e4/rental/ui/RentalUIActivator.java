package com.opcoach.training.e4.rental.ui;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;


/**
 * The activator class controls the plug-in life cycle
 */
public class RentalUIActivator extends AbstractUIPlugin implements  RentalUIConstants
{

	// The plug-in ID
	public static final String PLUGIN_ID = "com.opcoach.training.e4.rental.ui";

	// The shared instance
	private static RentalUIActivator plugin;

	/** The map of possible color providers (read in extensions) */
	private Map<String, IColorProvider> paletteManager = new HashMap<String, IColorProvider>();


	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception
	{
		super.start(context); 
		plugin = this;
		System.out.println("Start rental ui bundle");
		readViewExtensions();
		readColorProviderExtensions();
				
	}


	public void readColorProviderExtensions()
	{
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		System.out.println("Il faudra pr�voir de mettre l'extension ici...");
		IExtensionPoint extp = reg.getExtensionPoint("com.opcoach.training.e4.rental.ui.Palette");
		IExtension[] extensions = extp.getExtensions();

		for (IExtension ext : extensions)
		{
			IConfigurationElement[] config = ext.getConfigurationElements();
			for (IConfigurationElement elt : config)
			{
				// Create the color provider for label.
				try
				{
					// Create the executable extension
					Object exeExt = elt.createExecutableExtension("paletteClass");
					// Add it (with its name) in the color provider map
					String name = elt.getAttribute("name");
					paletteManager.put(name, (IColorProvider) exeExt);
				} catch (CoreException e)
				{
					IStatus st = new Status(IStatus.ERROR, PLUGIN_ID, "Unable to create palette class : "+
				                  elt.getAttribute("colorProviderClass"),e);
					getLog().log(st);
					
				}
			}
		}

	}

	
	public void readViewExtensions()
	{
		IExtensionRegistry reg = Platform.getExtensionRegistry();
	
			for (IConfigurationElement elt : reg.getConfigurationElementsFor("org.eclipse.ui.views"))
			{
				if (elt.getName().equals("view"))
					System.out.println("Plugin : " + elt.getNamespaceIdentifier() + "\t\t\tVue : " + elt.getAttribute("name"));
			}

	}

	/* @return a never null collection of overriden color providers */

	public Map<String, IColorProvider> getPaletteManager()
	{
		return paletteManager;
	}


	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception
	{
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static RentalUIActivator getDefault()
	{
		return plugin;
	}



	@Override
	protected void initializeImageRegistry(ImageRegistry reg)
	{
		reg.put(CUSTOMER_KEY, imageDescriptorFromPlugin(PLUGIN_ID,"icons/Customers.png"));
		reg.put(RENTAL_KEY, imageDescriptorFromPlugin(PLUGIN_ID,"icons/Rentals.png"));
		reg.put(RENTAL_OBJECT_KEY, imageDescriptorFromPlugin(PLUGIN_ID,"icons/RentalObjects.png"));
		reg.put(AGENCY_KEY, imageDescriptorFromPlugin(PLUGIN_ID,"icons/Agency.png"));
	}
	
	public Image getMyImage(String path)
	{
		// Utilise le Registry global de JfaceResources
		ImageRegistry reg = JFaceResources.getImageRegistry();
		// Essai de r�cuperer l'image peut �tre d�j� pr�sente
		Image img = reg.get(path);
		if (img == null)
		{
			// L'image n'est pas encore stock�e dans le registry, on l'ajoute
			ImageDescriptor desc = ImageDescriptor.createFromFile(this.getClass(), path);
			// Le path sert de cl�
			reg.put(path, desc);
			img = reg.get(path);
		}
		
		return img;
			
	}
	
	
}