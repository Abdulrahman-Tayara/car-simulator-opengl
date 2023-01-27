package phisics;

import org.lwjgl.util.vector.ReadableVector3f;
import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector3f;

public class Vector3D extends Vector3f {
    public Vector3D() {
    }

    public Vector3D(ReadableVector3f src) {
        super(src);
    }

    public Vector3D(float x, float y, float z) {
        super(x, y, z);
    }

    public void rotateOnX(float theta) {
        float Y = y, Z = z;
        this.y = (float) (Y * Math.cos(Math.toRadians(theta)) - Z * Math.sin(Math.toRadians(theta)));
        this.z = (float) (Y * Math.sin(Math.toRadians(theta)) + Z * Math.cos(Math.toRadians(theta)));
    }

    public void rotateOnY(float theta,Vector3f aroundVector) {
        //float X = x, Z = z;
        //this.x = (float) (X * Math.cos(Math.toRadians(theta)) + Z * Math.sin(Math.toRadians(theta)));
        //this.z = (float) (-X * Math.sin(Math.toRadians(theta)) + Z * Math.cos(Math.toRadians(theta)));
        Vector3f newVector = new Vector3f(this);
        newVector.scale((float) Math.cos(Math.toRadians(theta)));
        Vector3f crossProduct = new Vector3f();
        this.cross(aroundVector,this,crossProduct);
        crossProduct.scale((float) Math.sin(Math.toRadians(theta)));
        newVector = Vector3f.add(crossProduct,newVector,newVector);
        Vector3f C = new Vector3f(aroundVector);
        C.scale((float) (Vector3f.dot(aroundVector,this)*(1-Math.cos(Math.toRadians(theta)))));
        newVector = Vector3f.add(C,newVector,newVector);
        this.set(newVector);

    }


    public void rotateOnZ(float theta) {
        float X = x, Y = y;
        this.x = (float) (X * Math.cos(Math.toRadians(theta)) - Y * Math.sin(Math.toRadians(theta)));
        this.y = (float) (X * Math.sin(Math.toRadians(theta)) + Y * Math.cos(Math.toRadians(theta)));
    }
}
