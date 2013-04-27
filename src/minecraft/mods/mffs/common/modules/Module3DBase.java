package mods.mffs.common.modules;

import java.util.Set;

import mods.mffs.api.PointXYZ;
import mods.mffs.common.ForceFieldTyps;
import mods.mffs.common.IModularProjector;


public abstract class Module3DBase extends ModuleBase {

	public Module3DBase(int i) {
		super(i);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void calculateField(IModularProjector projector, Set<PointXYZ> points) {

	}

	public abstract void calculateField(IModularProjector projector,
			Set<PointXYZ> fieldPoints, Set<PointXYZ> interiorPoints);

	@Override
	public ForceFieldTyps getForceFieldTyps() {
		if (this instanceof ItemProjectorModuleContainment)
			return ForceFieldTyps.Containment;

		return ForceFieldTyps.Area;
	}

}
