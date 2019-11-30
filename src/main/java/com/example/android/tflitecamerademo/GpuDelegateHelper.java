

package com.example.android.tflitecamerademo;
import org.tensorflow.lite.Delegate;
import org.tensorflow.lite.Interpreter;

public class GpuDelegateHelper {
  private GpuDelegateHelper() {}

  public static boolean isGpuDelegateAvailable() {
    try {
      Class.forName("org.tensorflow.lite.experimental.GpuDelegate");
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public static Delegate createGpuDelegate() {
    try {
      return Class.forName("org.tensorflow.lite.experimental.GpuDelegate")
          .asSubclass(Delegate.class)
          .getDeclaredConstructor()
          .newInstance();
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}
