package com.example.examplemod.API.kettle.result;

public enum ResultTypes {
    POTION("type_potion"),
    ITEM("type_item");
    public String resultType;
    ResultTypes(String type){
        this.resultType = type;
    }
}
