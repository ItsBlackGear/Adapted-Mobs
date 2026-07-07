package com.cf28.adaptedmobs.common.level.entity.mob.creeper;

public enum ClothType {
    DEFAULT("creeper"),
    FESTIVE("festive"),
    ROCKET("rocket"),
    SUPPORT("support");

    public final String id;

    ClothType(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }
}