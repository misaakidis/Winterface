package freenet.winterface.core;

import freenet.l10n.BaseL10n;
import freenet.l10n.PluginL10n;
import freenet.pluginmanager.FredPluginBaseL10n;
import freenet.pluginmanager.FredPluginL10n;

/**
 * Provides Fred plugin localization/internationalization.
 *
 * While Fred's interfaces call it l10n, the templates call it i18n.
 * This may be worth reconsidering for the sake of consistency.
 */
public class I18n implements FredPluginBaseL10n, FredPluginL10n {
	private PluginL10n l10n;

	public I18n() {
		l10n = new PluginL10n(this);
	}

	@Override
	public String getString(String s) {
		return l10n.getBase().getString(s);
	}

	/**
	 * Shorter version of getString() for template use.
	 */
	public String get(String s) {
		return getString(s);
	}

	@Override
	public void setLanguage(BaseL10n.LANGUAGE language) {
		l10n = new PluginL10n(this, language);
	}

	@Override
	public String getL10nFilesBasePath() {
		return "i18n/";
	}

	@Override
	public String getL10nFilesMask() {
		return "${lang}.properties";
	}

	@Override
	public String getL10nOverrideFilesMask() {
		return "${lang}.override.properties";
	}

	@Override
	public ClassLoader getPluginClassLoader() {
		return WinterfacePlugin.class.getClassLoader();
	}
}
