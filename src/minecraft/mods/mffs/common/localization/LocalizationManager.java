package mods.mffs.common.localization;

import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * Equivalent-Exchange-3
 *
 * LocalizationHelper
 * LocalizationHandler
 * Localizations
 *
 * @author pahimar
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 *
 */
public class LocalizationManager {

	private static final String LANG_RESOURCE_LOCATION = "/mods/mffs/lang/";

	private static String[] localeFiles = new String[] {
			LANG_RESOURCE_LOCATION + "en_US.xml",
			LANG_RESOURCE_LOCATION + "de_DE.xml",
			LANG_RESOURCE_LOCATION + "fi_FI.xml"
	};

	/***
	 * Simple test to determine if a specified file name represents a XML file
	 * or not
	 *
	 * @param fileName
	 *            String representing the file name of the file in question
	 * @return True if the file name represents a XML file, false otherwise
	 */
	public static boolean isXMLLanguageFile(String fileName) {

		return fileName.endsWith(".xml");
	}

	/***
	 * Returns the locale from file name
	 *
	 * @param fileName
	 *            String representing the file name of the file in question
	 * @return String representation of the locale snipped from the file name
	 */
	public static String getLocaleFromFileName(String fileName) {

		return fileName.substring(fileName.lastIndexOf('/') + 1, fileName.lastIndexOf('.'));
	}

	public static String getLocalizedString(String key) {

		return LanguageRegistry.instance().getStringLocalization(key);
	}

	public static void loadLanguages() {
		for(String localizationFile : localeFiles)
		{
			LanguageRegistry.instance().loadLocalization(localizationFile, getLocaleFromFileName(localizationFile),
					isXMLLanguageFile(localizationFile));
		}
	}
}
