package engineTester;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entities.*;
import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import phisics.BrakeForce;
import phisics.EngineForce;
import phisics.RollingFrictionForce;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class MainGameLoop {

    public static void main(String[] args) throws IOException {

        DisplayManager.createDisplay();
        Loader loader = new Loader();
        //********************************************************//
        TerrainTexture backGroundTexture = new TerrainTexture(loader.loadTexture("grass"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("asphalt"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("mud"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("white"));
        TerrainTexture blendMapTexture1 = new TerrainTexture(loader.loadTexture("map1"));
        TerrainTexture blendMapTexture2 = new TerrainTexture(loader.loadTexture("map2"));
        TerrainTexturePack texturePack = new TerrainTexturePack(backGroundTexture,
                rTexture, gTexture, bTexture);
        //********************************************************//
        RawModel model = OBJLoader.loadObjectModel("car", loader);

        TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("redColor")));
        Car car1 = new Car(staticModel, new Vector3f(0, 0, 0), 0, 0, 0, 15, 0.34f, loader);
        car1.addForce(new EngineForce(100));
        car1.addForce(new BrakeForce(0.7f));
        car1.addForce(new RollingFrictionForce(0.02f));
        //car1.addForce(new AirResistanceForce(2,new Vector3f(50,0,-20)));

        Light light = new Light(new Vector3f(20000, 20000, 0), new Vector3f(1, 1, 1));

        Terrain terrain = new Terrain(0, 0, 4000, loader, texturePack, blendMapTexture1, null);
        Terrain terrain2 = new Terrain(0, -4000, 4000, loader, texturePack, blendMapTexture2, null);
        terrain.setRotY(90);
        terrain2.setRotY(90);
        terrain2.setRotZ(10);

        Camera camera = new Camera(car1);
        MasterRenderer renderer = new MasterRenderer(loader);


        while (!Display.isCloseRequested()) {
            camera.move();
            camera.move();
            car1.move(terrain2);
            renderer.processCar(car1);
            //renderer.processEntity(car1.rightBackWheel);
            //renderer.processEntity(car1.rightFrontWheel);
            //renderer.processEntity(car1.leftBackWheel);
            //renderer.processEntity(car1.leftFrontWheel);
            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);
            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();

    }

}
