package com.lucidlabs.tender.api;

public class Data_Alias {

    String[] alias;
    String[] result;

    public Data_Alias() {

    }



    public Data_Alias(String[] alias, String[] result) {
        this.alias = alias;
        this.result = result;
    }

    public String[] getAlias() {
        return alias;
    }

    public void setAlias(String[] alias) {
        this.alias = alias;
    }

    public String[] getResult() {
        return result;
    }

    public void setResult(String[] result) {
        this.result = result;
    }
}
