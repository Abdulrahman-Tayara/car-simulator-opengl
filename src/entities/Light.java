package entities;

import org.lwjgl.util.vector.Vector3f;

public class Light {
    Vector3f lightPosition;
    Vector3f lightColor;

    public Light(Vector3f lightPosition, Vector3f lightColor) {
        this.lightPosition = lightPosition;
        this.lightColor = lightColor;
    }

    public Vector3f getLightPosition() {
        return lightPosition;
    }

    public void setLightPosition(Vector3f lightPosition) {
        this.lightPosition = lightPosition;
    }

    public Vector3f getLightColor() {
        return lightColor;
    }

    public void setLightColor(Vector3f lightColor) {
        this.lightColor = lightColor;
    }
}
