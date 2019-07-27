package com.croods.eventmanagement.api;

public class DataStorage
{
    private String SharedPreferenceName;
    private android.content.Context ctx;
    private android.content.SharedPreferences pref;
    private android.content.SharedPreferences.Editor writer;
    public static final int INTEGER=1;
    public static final int FLOAT=2;
    public static final int STRING=3;
    public static final int BOOLEAN=4;
    public static final int LONG=5;

    public DataStorage(String SharedPreferenceName, android.content.Context ctx)
    {
        this.ctx = ctx;
        this.SharedPreferenceName = SharedPreferenceName;
        pref = ctx.getSharedPreferences(this.SharedPreferenceName, android.content.Context.MODE_PRIVATE);
        writer = pref.edit();
    }
    public void write(String key, int value)
    {
        writer.putInt(key,value);
        writer.commit();
    }
    public void write(String key, float value)
    {
        writer.putFloat(key,value);
        writer.commit();
    }
    public void write(String key, String value)
    {
        writer.putString(key,value);
        writer.commit();
    }
    public void write(String key, boolean value)
    {
        writer.putBoolean(key,value);
        writer.commit();
    }
    public void write(String key, long value)
    {
        writer.putLong(key,value);
        writer.commit();
    }
    public Object read(String key, int datatype)
    {
        Object value=null;
        if(datatype==INTEGER)
            value = pref.getInt(key,0);
        else if(datatype==FLOAT)
            value = pref.getFloat(key,0.0f);
        else if(datatype==STRING)
            value = pref.getString(key,"");
        else if(datatype==BOOLEAN)
            value = pref.getBoolean(key,false);
        else if(datatype==LONG)
            value = pref.getLong(key,0);

        return value;
    }
}
