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

package mods.mffs.common;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import mods.mffs.api.PointXYZ;


public class ForceFieldBlockStack {
	private PointXYZ png;
	private boolean sync;
	public Queue<ForceFieldBlock> blocks = new LinkedList<ForceFieldBlock>();

	public ForceFieldBlockStack(PointXYZ png) {
		this.png = png;
		sync = false;
	}

	public int getsize() {
		return blocks.size();
	}

	public void removeBlock() {
		blocks.poll();
	}

	public synchronized void removebyProjector(int projectorid) {
		ArrayList<ForceFieldBlock> tempblock = new ArrayList<ForceFieldBlock>();

		for (ForceFieldBlock ffblock : blocks) {
			if (ffblock.Projektor_ID == projectorid) {
				tempblock.add(ffblock);
			}
		}
		if (!tempblock.isEmpty())
			blocks.removeAll(tempblock);
	}

	public int getGenratorID() {
		ForceFieldBlock ffblock = blocks.peek();
		if (ffblock != null) {
			return ffblock.Generator_Id;
		}
		return 0;
	}

	public int getProjectorID() {
		ForceFieldBlock ffblock = blocks.peek();
		if (ffblock != null) {
			return ffblock.Projektor_ID;
		}
		return 0;
	}

	public int getTyp() {
		ForceFieldBlock ffblock = blocks.peek();
		if (ffblock != null) {
			return ffblock.typ;
		}
		return -1;
	}

	public void setSync(boolean sync) {
		this.sync = sync;
	}

	public boolean isSync() {
		return sync;
	}

	public boolean isEmpty() {
		return blocks.isEmpty();
	}

	public ForceFieldBlock get() {
		return blocks.peek();
	}

	public void add(int Generator_Id, int Projektor_ID, int typ) {
		blocks.offer(new ForceFieldBlock(Generator_Id, Projektor_ID, typ));
	}

	public PointXYZ getPoint() {
		return png;
	}

}
