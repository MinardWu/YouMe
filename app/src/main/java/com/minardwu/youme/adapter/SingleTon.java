package com.minardwu.youme.adapter;

/**
 * Created by MinardWu on 2017/5/17.
 */
public class SingleTon {

    private SingleTon(){}
    private static  SingleTon singleTon;
    public static SingleTon getSingleton(){
        if(singleTon == null){
            synchronized (SingleTon.class){
                if(singleTon == null){
                    singleTon = new SingleTon();
                }
            }
        }

        return singleTon;
    }
}
