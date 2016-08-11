package com.geekynu.goodertest.model;

import java.util.List;

/**
 * Created by yuanhonglei on 8/9/16.
 */
public class Gateway {
    public String apiAddress;
    public String apiAddressInternet;
    public String description;
    public long id;
    public Boolean internetAvailable;
    public Boolean isControlled;
    public String name;
    public List<mySensor> sensorList;
    public String typeName;
    public int sensorNum;

    public Gateway(long id, String name, String description, String typeName, int sensorNum) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.typeName = typeName;
        this.sensorNum = sensorNum;
    }

    public String getApiAddress() {
        return apiAddress;
    }

    public void setApiAddress(String apiAddress) {
        this.apiAddress = apiAddress;
    }

    public String getApiAddressInternet() {
        return apiAddressInternet;
    }

    public void setApiAddressInternet(String apiAddressInternet) {
        this.apiAddressInternet = apiAddressInternet;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Boolean getInternetAvailable() {
        return internetAvailable;
    }

    public void setInternetAvailable(Boolean internetAvailable) {
        this.internetAvailable = internetAvailable;
    }

    public Boolean getControlled() {
        return isControlled;
    }

    public void setControlled(Boolean controlled) {
        isControlled = controlled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getSensorNum() {
        return sensorNum;
    }

    public void setSensorNum(int sensorNum) {
        this.sensorNum = sensorNum;
    }
}
