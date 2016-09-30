/*  
    Copyright (C) 2012 Thunderdark

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
    Contributors:
    Thunderdark - initial implementation
 */

package com.nekokittygames.mffs.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;


public class Versioninfo {

	public static String curentversion() {
		InputStream inputstream = Versioninfo.class.getClassLoader()
				.getResourceAsStream("versioninfo");
		Properties properties = new Properties();

		if (inputstream != null) {
			try {
				properties.load(inputstream);
				String Major = properties
						.getProperty("mffs.version.major.number");
				String Minor = properties
						.getProperty("mffs.version.minor.number");
				String Revision = properties
						.getProperty("mffs.version.revision.number");
				String betabuild = properties
						.getProperty("mffs.version.betabuild.number");

				return Major + "." + Minor + "." + Revision + "." + betabuild;

			} catch (IOException ex) {
				//todo: Logger
				ModularForceFieldSystem.log.error(
								"[Modual ForceField System] cannot read local Version file!",
								ex);
			}
		}
		return 0 + "." + 0 + "." + 0 + "." + 0;
	}

	public static String newestversion() {

		Properties properties = new Properties();
		try {

			URL versionFile = new URL(ModularForceFieldSystem.VersionRemoteURL);
			InputStreamReader inputstream = new InputStreamReader(
					versionFile.openStream());

			if (inputstream != null) {

				properties.load(inputstream);

				String Major = properties
						.getProperty("mffs.version.major.number");
				String Minor = properties
						.getProperty("mffs.version.minor.number");
				String Revision = properties
						.getProperty("mffs.version.revision.number");
				String betabuild = properties
						.getProperty("mffs.version.betabuild.number");

				return Major + "." + Minor + "." + Revision + "." + betabuild;
			}

		} catch (Exception ex) {
			ModularForceFieldSystem.log.error(
							"[Modual ForceField System] cannot read remote Version file!",
							ex);
		}

		return 0 + "." + 0 + "." + 0 + "." + 0;
	}
}
