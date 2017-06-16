package org.tensorflow.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.io.IOException;
import java.util.List;

/**
 * Created by noahkong on 17-6-6.
 */

public class CImageRecognitionImpl implements IRecognition {
    private static final int NUM_CLASSES = 34;
    private static final int INPUT_SIZE = 224;
    private static final int IMAGE_MEAN = 117;
    private static final float IMAGE_STD = 1;
    private static final String INPUT_NAME = "Placeholder:0";
    private static final String OUTPUT_NAME = "scorea/scorea:0";

    private static final String MODEL_FILE = "file:///android_asset/Mtbv0.pb";
    private static final String LABEL_FILE = "file:///android_asset/cls_label.txt";

    private TensorFlowImageClassifier classifier;

    public CImageRecognitionImpl(Context context) {

        classifier = new TensorFlowImageClassifier();
        try {

            classifier.initializeTensorFlow(
                    context.getAssets(),
                    MODEL_FILE,
                    LABEL_FILE,
                    NUM_CLASSES,
                    INPUT_SIZE,
                    IMAGE_MEAN,
                    IMAGE_STD,
                    INPUT_NAME,
                    OUTPUT_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String[] importing(byte[] data) {
        String[] labels = new String[2];
        labels[0] = "test";
        labels[1] = "很哈卡";
        return labels;
    }

    @Override
    public String[] importing(Bitmap bmp) {
        if (bmp == null) {
            return new String[0];
        }


        int bAdpThr = 1;

        try {
            List<Classifier.Recognition> results = classifier.recognizeImage(bmp, bAdpThr);

            bmp.recycle();
            bmp = null;

            String[] strings = new String[results.size()];
            for (int i = 0; i < results.size(); i++) {
                strings[i] = results.get(i).getTitle();
            }
            return strings;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new String[0];

    }
}
