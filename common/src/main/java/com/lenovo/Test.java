package com.lenovo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Created by noahkong on 17-6-15.
 */

public class Test {
    public String s;

    public static void main(String[] args) {
        Test t = new Test();
        t.s ="12233";
        Test t1 = deserialize(new Gson().toJson(t),Test.class);
        System.out.print(t1.s);
    }
    public static <T> T deserialize(String jsonString, Class<T> clazz) {
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("MM/dd/yy HH:mm:ss");

        Gson gson = builder.create();
        return gson.fromJson(jsonString, clazz);
    }
}
