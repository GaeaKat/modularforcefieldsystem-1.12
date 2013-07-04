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

 Thunderdark 
 Matchlighter

 */

package mods.mffs.common;

import java.util.HashMap;
import java.util.Map;

public class SecurityRight {

	public static Map<String, SecurityRight> rights = new HashMap<String, SecurityRight>();
	public final String rightKey;
	public final String name;
	public final String description;
	public String texture = "mffs:textures/gui/AdvSecStationButtons.png";
	public final int texIndex;

	public static SecurityRight FFB = new SecurityRight("FFB",
			"Forcefield Bypass", "", 0);
	public static SecurityRight EB = new SecurityRight("EB", "Edit MFFS Block",
			"", 1);
	public static SecurityRight CSR = new SecurityRight("CSR",
			"Config Security Rights", "", 2);
	public static SecurityRight SR = new SecurityRight("SR", "Stay in Area",
			"", 3);
	public static SecurityRight OSS = new SecurityRight("OSS",
			"Open Secure Storage", "", 4);
	public static SecurityRight RPB = new SecurityRight("RPB",
			"Change Protected Block", "", 5);
	public static SecurityRight AAI = new SecurityRight("AAI",
			"Allow have all Items", "", 6);
	public static SecurityRight UCS = new SecurityRight("UCS",
			"Use Control System", "", 7);

	public SecurityRight(String ID, String name, String description, int txIndex) {
		this.rightKey = ID;
		this.name = name;
		this.description = description;
		this.texIndex = txIndex;
		rights.put(ID, this);
	}

}