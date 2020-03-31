package com.sjx.app.base.model;

public interface ICacheDataListener<T> {
   void onSuccess(T bean);

   void onError(String msg);

}
