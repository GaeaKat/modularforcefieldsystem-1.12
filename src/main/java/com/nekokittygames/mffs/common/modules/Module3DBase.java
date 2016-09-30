package com.nekokittygames.mffs.common.modules;

import java.util.Set;

import com.nekokittygames.mffs.api.PointXYZ;
import com.nekokittygames.mffs.common.ForceFieldTyps;
import com.nekokittygames.mffs.common.IModularProjector;


public abstract class Module3DBase extends ModuleBase {

	public Module3DBase() {
		super();
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
