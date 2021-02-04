package net.newgaea.mffs.common.forcefield;

import net.newgaea.mffs.common.misc.EnumFieldType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.Contract;

public class ForceFieldBlockInfo {
    private EnumFieldType type;
    private int projector_id;
    private int capacitor_id;

    @Contract(pure = true)
    public ForceFieldBlockInfo(EnumFieldType type, int projector_id, int capacitor_id) {
        this.type = type;
        this.projector_id = projector_id;
        this.capacitor_id = capacitor_id;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public EnumFieldType getType() {
        return type;
    }

    public ForceFieldBlockInfo setType(EnumFieldType type) {
        this.type = type;
        return this;
    }

    public int getProjector_id() {
        return projector_id;
    }

    public ForceFieldBlockInfo setProjector_id(int projector_id) {
        this.projector_id = projector_id;
        return this;
    }

    public int getCapacitor_id() {
        return capacitor_id;
    }

    public ForceFieldBlockInfo setCapacitor_id(int capacitor_id) {
        this.capacitor_id = capacitor_id;
        return this;
    }
}
