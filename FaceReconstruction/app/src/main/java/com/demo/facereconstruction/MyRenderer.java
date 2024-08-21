package com.demo.facereconstruction;

import android.content.Context;
import android.view.MotionEvent;

import org.rajawali3d.renderer.Renderer;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.materials.textures.TextureManager;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.scene.Scene;
import org.rajawali3d.Object3D;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;

public class MyRenderer extends Renderer {

    private Object3D objModel;

    public MyRenderer(Context context) {
        super(context);
        setFrameRate(60);
    }

    @Override
    protected void initScene() {
        // Add light to the scene
        DirectionalLight light = new DirectionalLight(1f, 0.2f, -1.0f);
        light.setPower(2);
        getCurrentScene().addLight(light);

        // Load .obj file
//        LoaderOBJ objParser = new LoaderOBJ(mContext.getResources(), mTextureManager, R.raw.face1);
//        try {
//            objParser.parse();
//            objModel = objParser.getParsedObject();
//            objModel.setScale(0.1f);
//            getCurrentScene().addChild(objModel);
//        } catch (ParsingException e) {
//            e.printStackTrace();
//        }

        // Set up camera
        getCurrentCamera().setZ(7);
    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }
}

