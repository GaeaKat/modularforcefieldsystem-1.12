package net.newgaea.mffs.transport.network.property;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.IContainerListener;
import net.newgaea.mffs.MFFS;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Collection;
import java.util.List;

public class PropertyManager {
    private final List<Property<?>> properties;
    private final short windowId;

    public PropertyManager(short windowId) {
        this.windowId = windowId;
        this.properties = Lists.newArrayList();
    }

    public <T> Property<T> addTrackedProperty(Property<T> property) {
        this.properties.add(property);
        return property;
    }

    public <T> void updateServer(Property<T> property, T value) {
        short propertyId = -1;
        for (short i = 0; i < properties.size(); i++) {
            if (properties.get(i) == property) {
                propertyId = i;
            }
        }
        property.set(value);
        MFFS.networkHandler.sendUpdateServerContainerProperties(
                new UpdateServerContainerPropertyMessage(windowId, property.getPropertyType(), propertyId, value));
    }

    public void sendChanges(Collection<IContainerListener> containerListeners, boolean firstTime) {
        List<ServerPlayerEntity> playerListeners = Lists.newArrayList();
        for (IContainerListener listener : containerListeners) {
            if (listener instanceof ServerPlayerEntity) {
                playerListeners.add((ServerPlayerEntity) listener);
            }
        }

        if (!playerListeners.isEmpty()) {
            List<Triple<PropertyType<?>, Short, Object>> dirtyProperties = Lists.newArrayList();
            for (short i = 0; i < properties.size(); i++) {
                Property<?> property = properties.get(i);
                if (property.isDirty() || firstTime) {
                    dirtyProperties.add(Triple.of(property.getPropertyType(), i, property.get()));
                }
            }

            if (!dirtyProperties.isEmpty()) {
                for (ServerPlayerEntity playerEntity : playerListeners) {
                    MFFS.networkHandler.sendUpdateClientContainerProperties(playerEntity,
                            new UpdateClientContainerPropertiesMessage(windowId, dirtyProperties));
                }
            }
        }
    }

    public void update(PropertyType<?> propertyType, short propertyId, Object value) {
        Property<?> property = properties.get(propertyId);
        if (property != null && property.getPropertyType() == propertyType) {
            propertyType.attemptSet(value, property);
        }
    }
}
