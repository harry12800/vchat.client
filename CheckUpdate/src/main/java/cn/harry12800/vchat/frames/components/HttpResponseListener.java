package cn.harry12800.vchat.frames.components;


/**
 * Created by harry12800 on 08/06/2017.
 */
public interface HttpResponseListener<T extends Object>
{
    void onResult(T ret);


}
