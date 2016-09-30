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

package com.nekokittygames.mffs.common;

import net.minecraft.util.DamageSource;

public class MFFSDamageSource extends DamageSource {

	public static DamageSource fieldShock = new MFFSDamageSource("fieldShock")
			.setDamageBypassesArmor();
	public static DamageSource areaDefense = new MFFSDamageSource("areaDefense")
			.setDamageBypassesArmor();
	public static DamageSource fieldDefense = new MFFSDamageSource("fieldDefense")
			.setDamageBypassesArmor();

	public MFFSDamageSource(String dmgId) {
		super(dmgId);
	}
}
