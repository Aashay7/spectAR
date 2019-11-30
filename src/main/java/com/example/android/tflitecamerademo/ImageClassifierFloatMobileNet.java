package com.example.android.tflitecamerademo;

import android.app.Activity;
import java.io.IOException;

/** This classifier works with the float MobileNet model. */
public class ImageClassifierFloatMobileNet extends ImageClassifier {

  private float[][] labelProbArray = null;

  ImageClassifierFloatMobileNet(Activity activity) throws IOException {
    super(activity);
    labelProbArray = new float[1][getNumLabels()];
  }

  @Override
  protected String getModelPath() {
    // you can download this file from
    // see build.gradle for where to obtain this file. It should be auto
    // downloaded into assets.
    return "mobilenet_v1_1.0_224.tflite";
  }

  @Override
  protected String getLabelPath() {
    return "labels_mobilenet_quant_v1_224.txt";
  }

  @Override
  protected int getImageSizeX() {
    return 224;
  }

  @Override
  protected int getImageSizeY() {
    return 224;
  }

  @Override
  protected int getNumBytesPerChannel() {
    return 4; // Float.SIZE / Byte.SIZE;
  }

  @Override
  protected void addPixelValue(int pixelValue) {
    imgData.putFloat(((pixelValue >> 16) & 0xFF) / 255.f);
    imgData.putFloat(((pixelValue >> 8) & 0xFF) / 255.f);
    imgData.putFloat((pixelValue & 0xFF) / 255.f);
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
    return labelProbArray[0][labelIndex];
  }

  @Override
  protected void runInference() {
    tflite.run(imgData, labelProbArray);
  }
}
