package net.newgaea.mffs.common.properties;

import net.minecraft.state.Property;
import net.newgaea.mffs.api.ProjectorModules;

import java.util.Collection;
import java.util.Optional;

public class ModuleProperty extends Property<String> {

    public ModuleProperty(String name) {
        super(name, String.class);
    }

    @Override
    public Collection<String> getAllowedValues() {
        return ProjectorModules.getModules();
    }

    @Override
    public String getName(String value) {
        return value;
    }

    @Override
    public Optional<String> parseValue(String value) {
        return Optional.of(value);
    }
}
