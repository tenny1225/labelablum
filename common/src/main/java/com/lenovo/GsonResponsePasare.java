package com.lenovo;

import com.google.gson.Gson;
import com.lenovo.common.entity.BundleEntity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

class GsonResponsePasare<T> implements ParameterizedType {
    Class<T> cla;
    public GsonResponsePasare(Class<T> cla) {
        this.cla =cla;
    }

    public T deal(String response) {
//            Type gsonType = new ParameterizedType() {//...};//不建议该方式，推荐采用GsonResponsePasare实现ParameterizedType.因为getActualTypeArguments这里涉及获取GsonResponsePasare的泛型集合
        Type gsonType = this;

        T commonResponse = new Gson().fromJson(response, gsonType);

        return commonResponse;
    }

    @Override
    public Type[] getActualTypeArguments() {
        Class clz = this.getClass();
        //这里必须注意在外面使用new GsonResponsePasare<GsonResponsePasare.DataInfo>(){};实例化时必须带上{},否则获取到的superclass为Object
        Type superclass = clz.getGenericSuperclass(); //getGenericSuperclass()获得带有泛型的父类
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return parameterized.getActualTypeArguments();
    }

    @Override
    public Type getOwnerType() {
        return cla;
    }

    @Override
    public Type getRawType() {
        return cla;
    }
}