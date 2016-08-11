package com.geekynu.goodertest.model;

/**
 * Created by yuanhonglei on 8/10/16.
 */
public class mySensor {
    private long id;
    private String name;
    private String value;
    private String unit;
    private String lastUpdateTime;
    private boolean isOnline;
    private boolean isAlarm;

    public mySensor(long id, String name, String value, String unit, String lastUpdateTime, boolean isOnline, boolean isAlarm)
    {
        this.id = id;
        this.name = name;
        this.value = value;
        this.unit = unit;
        this.lastUpdateTime = lastUpdateTime;
        this.isOnline = isOnline;
        this.isAlarm = isAlarm;
    }

    public long getId()
    {
        return this.id;
    }

    public boolean getIsAlarm()
    {
        return this.isAlarm;
    }


    public boolean getIsOnline()
    {
        return this.isOnline;
    }

    public String getLastUpdateTime()
    {
        return this.lastUpdateTime;
    }

    public String getName()
    {
        return this.name;
    }


    public String getUnit()
    {
        return this.unit;
    }

    public String getValue()
    {
        return this.value;
    }

    public void setId(long paramLong)
    {
        this.id = paramLong;
    }

    public void setIsAlarm(boolean paramBoolean)
    {
        this.isAlarm = paramBoolean;
    }

    public void setIsOnline(boolean paramBoolean)
    {
        this.isOnline = paramBoolean;
    }

    public void setLastUpdateTime(String paramString)
    {
        this.lastUpdateTime = paramString;
    }

    public void setName(String paramString)
    {
        this.name = paramString;
    }

    public void setUnit(String paramString)
    {
        this.unit = paramString;
    }

    public void setValue(String paramString)
    {
        this.value = paramString;
    }
}
