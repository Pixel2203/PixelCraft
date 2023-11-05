package com.example.examplemod.API.result;

public enum ResultTypes {
    POTION("type_potion"),
    ITEM("type_item"),
    RITUAL("type_ritual");
    public final String resultType;
    ResultTypes(String type){
        this.resultType = type;
    }
}
