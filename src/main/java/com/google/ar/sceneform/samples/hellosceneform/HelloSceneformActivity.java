package com.google.ar.sceneform.samples.hellosceneform;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.ar.core.Anchor;
import com.google.ar.sceneform.Node;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// BETA -> WIKI
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import java.util.stream.Collectors;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
//

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.google.ar.sceneform.utilities.SceneformBufferUtils.readStream;

public class HelloSceneformActivity extends AppCompatActivity {

    private static final String TAG = HelloSceneformActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    private ArFragment arFragment;
    private ModelRenderable andyRenderable;
    private ViewRenderable inforenderable;
    private ViewRenderable aslirenderable;
    private  String encoding = "UTF-8";
    String src = "Ed Sheeran wiki";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getApplicationContext(), "SPECTAR - Tap to get more information", Toast.LENGTH_SHORT).show();

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }

        setContentView(R.layout.activity_ux);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        ModelRenderable.builder()
                .setSource(this, R.raw.ironman).build()
                .thenAccept(renderable -> andyRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });


        //if (andyRenderable == null) {
        //  return;
        //}
        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (andyRenderable == null) {
                        return;
                    }
                    // Create the Anchor.
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);

                    // Setting the ARscene as the parent to Anchor node created.
                    anchorNode.setParent(arFragment.getArSceneView().getScene());


                    Node information = new Node();

                    information.setParent(anchorNode);
                    //information.setParent(andy);
                    information.setRenderable(inforenderable);

                    // To change the relative position, play with this parameter.
                    information.setLocalPosition(new Vector3(0.0f, 0.8f, 0.0f));

                    ViewRenderable.builder()
                            .setView(this, R.layout.infotag)
                            .build()
                            //.thenAccept(renderable -> inforenderable = renderable);
                            .thenAccept(
                                    (renderable) -> {
                                        information.setRenderable(renderable);
                                        TextView textView = (TextView) renderable.getView();
                                        textView.setText("Allo");
                                    });


                    information.setOnTapListener((v, event) -> {
                        Toast.makeText(getApplicationContext(), "Opening the link", Toast.LENGTH_LONG).show();

                        //TextView textView = (TextView) findViewById(R.id.wikiworks);
                        //textView.setTextColor(Color.RED);
                        //textView.setText(R.string.striiiiing);
                        String url = "http://www.google.com/search" + "?q=" + src + "&num=3";
                        //String url = "https://en.wikipedia.org/wiki/Ed_Sheeran";
                        String sFileName = "spectar1.txt";
                        Uri webpage = Uri.parse(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                        //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        //startActivity(browserIntent);
                        //Toast.makeText(getApplicationContext(), "Opened the link", Toast.LENGTH_LONG).show();

                        //
                    });
                });
    }
    /**
     * Returns false and displays an error message if Sceneform can not run, true if Sceneform can run
     * on this device.
     *
     * <p>Sceneform requires Android N on the device as well as OpenGL 3.0 capabilities.
     *
     * <p>Finishes the activity if Sceneform can not run
     */
    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }
}