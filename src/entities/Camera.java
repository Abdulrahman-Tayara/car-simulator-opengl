package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Input;
import renderEngine.DisplayManager;

public class Camera {

    private float distanceFromCar = 50;
    private float angleAroundPlayer = 0;
    private Vector3f position = new Vector3f(0, 50, 50);
    private float pitch;
    private float yaw;
    private float roll;

    private final static float RUN_SPEED = 100;
    private final static float TURN_SPEED = 100;
    private float currentSpeed = 0;
    private float currentTurnOnYAxixSpeed = 0;
    private float currentTurnOnXAxixSpeed = 0;


    private Car car;

    public Camera(Car car) {
        this.car = car;
    }

    public void increaseRotation(float dx, float dy, float dz) {
        this.pitch += dx;
        this.yaw += dy;
        this.roll += dz;
    }

    public void increasePosition(float dx, float dy, float dz) {
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    public void checkInputs() {
        if (Keyboard.isKeyDown(Keyboard.KEY_W))
            this.currentSpeed = RUN_SPEED;
        else if (Keyboard.isKeyDown(Keyboard.KEY_S))
            this.currentSpeed = -RUN_SPEED;
        else
            this.currentSpeed = 0;

        if (Keyboard.isKeyDown(Keyboard.KEY_A))
            this.currentTurnOnYAxixSpeed = -TURN_SPEED;
        else if (Keyboard.isKeyDown(Keyboard.KEY_D))
            this.currentTurnOnYAxixSpeed = TURN_SPEED;
        else
            this.currentTurnOnYAxixSpeed = 0;

        if (Keyboard.isKeyDown(Keyboard.KEY_Q))
            this.currentTurnOnXAxixSpeed = -TURN_SPEED;
        else if (Keyboard.isKeyDown(Keyboard.KEY_E))
            this.currentTurnOnXAxixSpeed = TURN_SPEED;
        else
            this.currentTurnOnXAxixSpeed = 0;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    private void calculateZoom() {
        distanceFromCar = 400;
    }

    private void calculatePitch() {
        if (Mouse.isButtonDown(0)) {
            float pitchChange = Mouse.getY() * 0.0003f;
            pitch -= pitchChange;
        }
    }

    private void calculateAngleAroundPlayer() {
        if (Mouse.isButtonDown(0)) {
            float angleChange = Mouse.getX() * 0.0003f;
            angleAroundPlayer -= angleChange;
        }
    }

    private float calculateHorizontalDistance() {
        return (float) (distanceFromCar * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance() {
        return (float) (distanceFromCar * Math.sin(Math.toRadians(pitch)));
    }

    private void calculateCameraPosition(float horizDistance, float verticDistance) {
        float theta = car.getRotY() + angleAroundPlayer;
        float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (verticDistance * Math.cos(Math.toRadians(theta)));
        position.x = car.position.x - offsetX;
        position.z = car.position.z - offsetZ;
        position.y = car.position.y + verticDistance;
    }

    public void move() {
        position.x = car.position.x;
        position.y = car.position.y + 40;
        position.z = car.position.z + 150;
    }

}
