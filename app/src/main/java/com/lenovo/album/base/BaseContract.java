package com.lenovo.album.base;

/**
 * Created by noahkong on 17-6-5.
 */

public interface BaseContract {
    interface BaseView {

    }

    interface BasePresenter {
        /**
         * presenter的初始化方法
         */
        void start();

        /**
         * view销毁方法
         */
        void destroy();
    }

    interface BaseModel {
        public static final String SUCCESS = "success";
        interface ModelResponse<T> {

            void onSuccess(T data);

            void onFailure(String msg);
        }
    }
}
