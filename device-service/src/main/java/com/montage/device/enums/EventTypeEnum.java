package com.montage.device.enums;

public enum EventTypeEnum {
    CREATE(1),
    UPDATE(2),
    DELETE(3);
    
    private final int id;
    
    EventTypeEnum(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
} 