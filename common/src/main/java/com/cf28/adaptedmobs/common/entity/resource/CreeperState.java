package com.cf28.adaptedmobs.common.entity.resource;

public enum CreeperState {
    IDLING, SITTING, STANDING, ATTACKING;

    public boolean is(CreeperState state) {
        return state == this;
    }
}