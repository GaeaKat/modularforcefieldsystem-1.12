package com.nekokittygames.mffs.common.libs;

public enum NetworkComponents {
    CAPACITOR("CAPACITOR");

    private String name;
    NetworkComponents(String name)
    {
        this.name=name;
    }


    @Override
    public String toString() {
        return name;
    }
}
