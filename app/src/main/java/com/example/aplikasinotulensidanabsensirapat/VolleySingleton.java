package com.example.aplikasinotulensidanabsensirapat;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static VolleySingleton mInstance;
    private com.android.volley.RequestQueue mRequestQueue;
    public static Context mcontext;

    private  VolleySingleton(Context context){
        mcontext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleySingleton getInstance(Context context){
        if (mInstance == null){
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    public com.android.volley.RequestQueue getRequestQueue(){
        if (mRequestQueue==null){
            mRequestQueue = Volley.newRequestQueue(mcontext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T>  void addToRequestQueue(Request<T> request){
        getRequestQueue().add(request);
    }

}
