package org.tensorflow.demo;

import android.content.Context;

/**
 * Created by noahkong on 17-6-13.
 */

public class RecognitionFactory {
    private static IRecognition iRecognition;
    public static IRecognition getRecognition(Context context){
        if(iRecognition==null){
            iRecognition = new CImageRecognitionImpl(context);
        }
        return iRecognition;
    }
}
