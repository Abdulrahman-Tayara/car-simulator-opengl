package entities;

import models.TexturedModel;
import org.lwjgl.util.vector.Matrix;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

public class Entity {

    private TexturedModel model;
    protected Vector3f position;
    protected float rotX, rotY, rotZ;
    private float scale;
    protected Vector3f forward, up, right;
    protected Matrix4f transformationMatrix;

    public Entity() {
    }

    public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ,
                  float scale) {
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
        forward = new Vector3f();
        up = new Vector3f();
        right = new Vector3f();
    }

    public Entity(Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    }

    public void increasePosition(float dx, float dy, float dz) {
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    public void increaseRotation(float dx, float dy, float dz) {
        this.rotX += dx;
        this.rotY += dy;
        this.rotZ += dz;
    }

    public TexturedModel getModel() {
        return model;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public float getRotX() {
        return rotX;
    }

    public void setRotX(float rotX) {
        this.rotX = rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    private Matrix4f getTransformationMatrix(){
        return Maths.createTransformationMatrix(position,
                getRotX(), getRotY(), getRotZ(), getScale());
    }
    public Vector3f getForward() {
        transformationMatrix = getTransformationMatrix();
        forward.x = transformationMatrix.m20 * -1;
        forward.y = transformationMatrix.m21 * -1;
        forward.z = transformationMatrix.m22 * -1;
        forward.normalise();
        return forward;
    }

    public Vector3f getUp() {
        transformationMatrix = Maths.createTransformationMatrix(position,
                getRotX(), getRotY(), getRotZ(), getScale());
        up.x = transformationMatrix.m10;
        up.y = transformationMatrix.m11;
        up.z = transformationMatrix.m12;
        up.normalise();
        return up;
    }

    public Vector3f getRight() {
        transformationMatrix = Maths.createTransformationMatrix(position,
                getRotX(), getRotY(), getRotZ(), getScale());
        right.x = transformationMatrix.m00;
        right.y = transformationMatrix.m01;
        right.z = transformationMatrix.m02;
        return right;
    }
}
