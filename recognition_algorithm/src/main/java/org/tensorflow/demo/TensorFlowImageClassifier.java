/* Copyright 2016 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package org.tensorflow.demo;


import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Log;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


/**
 * A classifier specialized to label images using TensorFlow.
 */
public class TensorFlowImageClassifier implements Classifier {
    static {
        System.loadLibrary("tensorflow_demo");
    }

    private static final String TAG = "TFClassifier";

    // Only return this many results with at least this confidence.
    private static final int MAX_RESULTS = 5;
    private static final float THRESHOLD = 0.1f;

    private static final float[] dThrs =
            {1.4644656372070415f, -0.65781810760497095f, 1.040008888244639f, 1.4644656372070415f, -1.3556451034545809f,
                    -3.6290153503417883f, -1.0822748565673734f, -1.0822748565673734f, 0.19109539031983402f, -0.7822748565673734f,
                    -0.23336135864256846f, -1.5067316055297759f, -1.0822748565673734f, -0.23336135864256846f, -1.5067316055297759f,
                    -1.9311883544921784f, -3.2045586013793859f, 0.83336135864256846f, -1.5067316055297759f, 1.040008888244639f,
                    -2.7801018524169834f, -3.9067316055297759f, -1.0822748565673734f, 2.3133791351318465f, -3.1001018524169834f,
                    1.4644656372070415f, -2.7801018524169834f, -5.4512990951538008f, -0.23336135864256846f, -1.5067316055297759f,
                    -0.12009539031983402f, -4.1512990951538008f, 1.4644656372070415f, 0.09109539031983402f};
    /*private static final float[] dThrs = {0.41424125671372991f, 3.90089340209947011f, -0.072410888672010287f, 0.90089340209947011f, -0.55906303405775049f,
            0.41424125671372991f, 0.41424125671372991f, -0.072410888672010287f, -1.0457151794434907f, -0.55906303405775049f,
            -0.072410888672010287f, -1.1557151794434907f, -0.072410888672010287f, -0.55906303405775049f, -1.9923673248292309f,
            -1.0457151794434907f, -3.20406303405775049f, 0.81424125671372991f, -1.5323673248292309f, -0.072410888672010287f,
            -2.072410888672010287f, -3.1323673248292309f, -0.85906303405775049f, 1.3875455474852103f, -1.55906303405775049f,
            0.90089340209947011f, -2.41424125671372991f, -1.0457151794434907f, -0.072410888672010287f, -0.072410888672010287f,
            0.41424125671372991f, -0.55906303405775049f, 0.41424125671372991f, -0.072410888672010287f};*/
    // Config values.
    private String inputName;
    private String outputName;
    private int inputSize;
    private int imageMean;
    private float imageStd;

    // Pre-allocated buffers.
    private Vector<String> labels = new Vector<String>();
    private int[] intValues;
    private float[] floatValues;
    private float[] outputs;
    private String[] outputNames;

    private TensorFlowInferenceInterface inferenceInterface;

    /**
     * Initializes a native TensorFlow session for classifying images.
     *
     * @param assetManager  The asset manager to be used to load assets.
     * @param modelFilename The filepath of the model GraphDef protocol buffer.
     * @param labelFilename The filepath of label file for classes.
     * @param numClasses    The number of classes output by the model.
     * @param inputSize     The input size. A square image of inputSize x inputSize is assumed.
     * @param imageMean     The assumed mean of the image values.
     * @param imageStd      The assumed std of the image values.
     * @param inputName     The label of the image input node.
     * @param outputName    The label of the output node.
     * @return The native return value, 0 indicating success.
     * @throws IOException
     */
    public int initializeTensorFlow(
            AssetManager assetManager,
            String modelFilename,
            String labelFilename,
            int numClasses,
            int inputSize,
            int imageMean,
            float imageStd,
            String inputName,
            String outputName) throws IOException {
        this.inputName = inputName;
        this.outputName = outputName;

        // Read the label names into memory.
        // TODO(andrewharp): make this handle non-assets.
        String actualFilename = labelFilename.split("file:///android_asset/")[1];
        Log.i(TAG, "Reading labels: " + actualFilename);
        BufferedReader br = null;
        br = new BufferedReader(new InputStreamReader(assetManager.open(actualFilename)));
        String line;
        while ((line = br.readLine()) != null) {
            labels.add(line);
        }
        br.close();
        Log.i(TAG, "Read " + labels.size() + ", " + numClasses + " specified");

        this.inputSize = inputSize;
        this.imageMean = imageMean;
        this.imageStd = imageStd;

        // Pre-allocate buffers.
        outputNames = new String[]{outputName};
        intValues = new int[inputSize * inputSize];
        floatValues = new float[inputSize * inputSize * 3];
        outputs = new float[numClasses];

        inferenceInterface = new TensorFlowInferenceInterface();

        return inferenceInterface.initializeTensorFlow(assetManager, modelFilename);
    }

    @Override
    public List<Recognition> recognizeImage(final Bitmap bitmap, int bAdpthr) {
        // Preprocess the image data from 0-255 int to normalized float based
        // on the provided parameters.
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        for (int i = 0; i < intValues.length; ++i) {
            final int val = intValues[i];
            floatValues[i * 3 + 2] = (((val >> 16) & 0xFF) - 104) / imageStd;//R
            floatValues[i * 3 + 1] = (((val >> 8) & 0xFF) - 117) / imageStd;//G
            floatValues[i * 3 + 0] = ((val & 0xFF) - 123) / imageStd;//B
        }


        // Copy the input data into TensorFlow.
        inferenceInterface.fillNodeFloat(
                inputName, new int[]{1, inputSize, inputSize, 3}, floatValues);

        // Run the inference call.
        inferenceInterface.runInference(outputNames);

        // Copy the output Tensor back into the output array.
        inferenceInterface.readNodeFloat(outputName, outputs);

        // Find the best classifications.
    /*PriorityQueue<Recognition> pq = new PriorityQueue<Recognition>(3,
        new Comparator<Recognition>() {
          @Override
          public int compare(Recognition lhs, Recognition rhs) {
            // Intentionally reversed to put high confidence at the head of the queue.
            return Float.compare(rhs.getConfidence(), lhs.getConfidence());
          }
        });*/
        Vector<Recognition> pq = new Vector<Recognition>();
    /*if(bAdpthr == 0){
      for (int i = 0; i < outputs.length; ++i) {
        Log.i(TAG, "idx is " + i + " output is " + outputs[i] + " dthr " + dThrs[i]);

        if (outputs[i] > 0.01) {
          pq.add(new Recognition(i, labels.get(i), outputs[i], dThrs[i], null));
        }
      }
    }else {*/
        for (int i = 0; i < outputs.length; ++i) {
            Log.i(TAG, "idx is " + i + " output is " + outputs[i] + " dthr " + dThrs[i]);

            if (outputs[i] > dThrs[i]) {
                pq.add(new Recognition(i, labels.get(i), outputs[i], dThrs[i], null));
            }
        }
        //}

        final ArrayList<Recognition> recognitions = new ArrayList<Recognition>();
        int recognitionsSize = Math.min(pq.size(), MAX_RESULTS);
        for (int i = 0; i < recognitionsSize; ++i) {
            //recognitions.add(pq.poll());
            recognitions.add(pq.get(i));
        }

        return recognitions;
    }

    @Override
    public void close() {
        inferenceInterface.close();
    }
}
