package com.example.attendencemonitor.service.model;

public enum UserType
{
    ADMIN(0),

    TEACHER(1),

    STUDENT(2),

    TEACHING_STUDENT(3);

    private final int key;

    UserType(int value)
    {
        key = value;
    }

    public int getKey(){
        return this.key;
    }

    public static UserType fromKey(int key) {
        for(UserType type : UserType.values()) {
            if(type.getKey() == key) {
                return type;
            }
        }
        return null;
    }

}
