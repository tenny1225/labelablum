package org.tensorflow.demo;

import android.graphics.Bitmap;

/**
 * Created by noahkong on 17-6-6.
 */

public interface IRecognition {
    String[] importing(byte[] data);
    String[] importing(Bitmap bitmap);
}
