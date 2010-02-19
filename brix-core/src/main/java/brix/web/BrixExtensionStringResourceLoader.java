package brix.web;

import java.util.Locale;
import java.util.WeakHashMap;

import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.resource.loader.ComponentStringResourceLoader;
import org.apache.wicket.resource.loader.IStringResourceLoader;

import brix.registry.ExtensionPoint;
import brix.registry.ExtensionPointRegistry.Listener;

/**
 * {@link IStringResourceLoader} that checks extension point classes for
 * contributed resource bundles
 * 
 * @author igor.vaynberg
 * 
 */
public class BrixExtensionStringResourceLoader extends ComponentStringResourceLoader
		implements
			Listener
{
	/** The application we are loading for. */
	private final WeakHashMap<Class<?>, Object> classes = new WeakHashMap<Class<?>, Object>();

	/**
	 * @see org.apache.wicket.resource.loader.ComponentStringResourceLoader#loadStringResource(java.lang.Class,
	 *      java.lang.String, java.util.Locale, java.lang.String)
	 */
	@Override
	public String loadStringResource(final Class<?> clazz, final String key, final Locale locale,
			final String style)
	{

		for (Class<?> cl : classes.keySet())
		{
			String value = super.loadStringResource(cl, key, locale, style);
			if (value != null)
			{
				return value;
			}
		}
		return null;
	}

	/**
	 * @see org.apache.wicket.resource.loader.ComponentStringResourceLoader#loadStringResource(org.apache.wicket.Component,
	 *      java.lang.String)
	 */
	@Override
	public final String loadStringResource(Component component, String key)
	{
		if (component == null)
		{
			return loadStringResource(null, key, Session.get().getLocale(), Session.get()
					.getStyle());
		}
		return super.loadStringResource(component, key);
	}

	/**
	 * @see Listener#registered(ExtensionPoint, Object)
	 */
	public void registered(ExtensionPoint<?> point, Object extension)
	{
		classes.put(extension.getClass(), null);

	}

	/**
	 * @see Listener#unregistered(ExtensionPoint, Object)
	 */
	public void unregistered(ExtensionPoint<?> point, Object extension)
	{
		classes.remove(extension.getClass());
	}
}