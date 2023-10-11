package com.example.examplemod.API.brewing.kettle.result;

public enum ResultTypes {
    POTION("type_potion"),
    ITEM("type_item");
    public String resultType;
    ResultTypes(String type){
        this.resultType = type;
    }
}
