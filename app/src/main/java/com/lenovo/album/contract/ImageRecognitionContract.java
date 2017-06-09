package com.lenovo.album.contract;

import android.content.Context;

import com.lenovo.album.base.BaseContract;

/**
 * Created by noahkong on 17-6-6.
 */

public interface ImageRecognitionContract {
    interface Presenter extends BaseContract.BasePresenter{

    }
    interface Model extends BaseContract.BaseModel{
        void startRecognition(Context context);
    }
}
