package com.example.android.tflitecamerademo;

import android.app.Activity;

import java.io.IOException;


public class ImageClassifierFloatInception extends ImageClassifier {

  private static final int IMAGE_MEAN = 128;
  private static final float IMAGE_STD = 128.0f;

  private float[][] labelProbArray = null;

  ImageClassifierFloatInception(Activity activity) throws IOException {
    super(activity);
    labelProbArray = new float[1][getNumLabels()];
  }

  @Override
  protected String getModelPath() {
    // you can download this file from
    // https://storage.googleapis.com/download.tensorflow.org/models/tflite/inception_v3_slim_2016_android_2017_11_10.zip
    return "inceptionv3_slim_2016.tflite";
    // return ""
  }

  @Override
  protected String getLabelPath() {
    return "labels_imagenet_slim.txt";
  }

  @Override
  protected int getImageSizeX() {
    return 299;
  }

  @Override
  protected int getImageSizeY() {
    return 299;
  }

  @Override
  protected int getNumBytesPerChannel() {
    // a 32bit float value requires 4 bytes
    return 4;
  }

  @Override
  protected void addPixelValue(int pixelValue) {
    imgData.putFloat((((pixelValue >> 16) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
    imgData.putFloat((((pixelValue >> 8) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
    imgData.putFloat(((pixelValue & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
  }

  @Override
  protected float getProbability(int labelIndex) {
    return labelProbArray[0][labelIndex];
  }

  @Override
  protected void setProbability(int labelIndex, Number value) {
    labelProbArray[0][labelIndex] = value.floatValue();
  }

  @Override
  protected float getNormalizedProbability(int labelIndex) {
    // TODO the following value isn't in [0,1] yet, but may be greater. Why?
    return getProbability(labelIndex);
  }

  @Override
  protected void runInference() {
    tflite.run(imgData, labelProbArray);
  }
}
