package net.newgaea.mffs.transport.network.property;

import net.minecraft.network.PacketBuffer;
import net.newgaea.mffs.MFFS;

import java.util.Objects;
import java.util.function.*;

public class PropertyType<T> implements Comparable<PropertyType<?>> {
    private final String name;
    private final Class<T> tClass;
    private final Function<PacketBuffer, T> reader;
    private final BiConsumer<PacketBuffer, T> writer;
    private final BiPredicate<T, T> equals;

    public PropertyType(String name, Class<T> tClass, Function<PacketBuffer, T> reader, BiConsumer<PacketBuffer, T> writer) {
        this(name, tClass, reader, writer, Objects::equals);
    }

    public PropertyType(String name, Class<T> tClass, Function<PacketBuffer, T> reader, BiConsumer<PacketBuffer, T> writer,
                        BiPredicate<T, T> equals) {
        this.name = name;
        this.tClass = tClass;
        this.reader = reader;
        this.writer = writer;
        this.equals = equals;
    }

    public Function<PacketBuffer, T> getReader() {
        return reader;
    }

    public BiConsumer<PacketBuffer, T> getWriter() {
        return writer;
    }

    public BiPredicate<T, T> getEquals() {
        return equals;
    }

    public String getName() {
        return name;
    }

    public Property<T> create() {
        return new Property<>(this);
    }

    public Property<T> create(Supplier<T> getter) {
        return new Property<>(this, getter, value -> {
        });
    }

    public Property<T> create(Supplier<T> getter, Consumer<T> setter) {
        return new Property<>(this, getter, setter);
    }

    public boolean isValid(Object object) {
        return tClass.isInstance(object);
    }

    public void attemptWrite(PacketBuffer packetBuffer, Object object) {
        if (tClass.isInstance(object)) {
            this.getWriter().accept(packetBuffer, tClass.cast(object));
        } else {
            MFFS.getLog().error("Attempted to Write with Invalid Object.");
        }
    }

    @SuppressWarnings("unchecked")
    public void attemptSet(Object object, Property<?> property) {
        if (property.getPropertyType() == this) {
            if (tClass.isInstance(object)) {
                try {
                    ((Property<T>) property).set(tClass.cast(object));
                } catch (ClassCastException classCastException) {
                    MFFS.getLog().error("Failed to Set Container Property", classCastException);
                }
            }
        }
    }

    @Override
    public int compareTo(PropertyType<?> o) {
        return String.CASE_INSENSITIVE_ORDER.compare(this.getName(), o.getName());
    }
}

