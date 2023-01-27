package entities;

import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

public abstract class Environment extends Entity {
    protected float[][] heights;

    public Environment() {
    }

    public Environment(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    public Environment(Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(position, rotX, rotY, rotZ, scale);
    }

    public abstract void calculateHeights(float[][] heights);
}
