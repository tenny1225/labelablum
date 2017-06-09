package com.lenovo.recognitionalgorithm;

/**
 * Created by noahkong on 17-6-6.
 */

public class CImageRecognitionImpl implements IRecognition {
    @Override
    public String[] importing(byte[] data) {
        String[] labels = new String[2];
        labels[0] = "test";
        labels[1] = "很哈卡";
        return labels;
    }
}
