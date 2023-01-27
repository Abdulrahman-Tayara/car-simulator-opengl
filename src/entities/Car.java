package entities;

import models.RawModel;
import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import phisics.Force;
import phisics.Vector3D;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Car extends Entity {

    private final static float TURN_ON_Y_AXIS_SPEED = 90f;

    private float maxSpeed = 0;
    private float currentTurnOnYAxixSpeed = 0;
    private float currentRollingWheelsSpeed = 0;

    private List<Force> forces;
    private float mass = 1400;
    private Vector3f acceleration;
    private Vector3D velocity;
    private boolean brakePushed;
    private boolean forwardAcceleratorPushed;
    private boolean backwardAcceleratorPushed;
    private boolean rotateFromGroundToHillChanged = false;

    private Wheel leftFrontWheel, rightFrontWheel, leftBackWheel, rightBackWheel;
    private float wheelsXOffset, wheelsZOffset, wheelYOffset, wheelRadius;

    public Car(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, float wheelRadius, Loader loader) throws IOException {
        super(model, position, rotX, rotY, rotZ, scale);
        forces = new ArrayList<>();
        this.wheelRadius = wheelRadius;
        velocity = new Vector3D();
        acceleration = new Vector3f();
        RawModel wheelModel = OBJLoader.loadObjectModel("Wheel", loader);
        TexturedModel wheelStaticModel = new TexturedModel(wheelModel, new ModelTexture(loader.loadTexture("wheels")));
        leftFrontWheel = new Wheel(wheelStaticModel, new Vector3f(), WheelsPosition.LEFT_FRONT, this.getScale());
        rightFrontWheel = new Wheel(wheelStaticModel, new Vector3f(), WheelsPosition.RIGHT_FRONT, this.getScale());
        leftBackWheel = new Wheel(wheelStaticModel, new Vector3f(), WheelsPosition.LEFT_BACK, this.getScale());
        rightBackWheel = new Wheel(wheelStaticModel, new Vector3f(), WheelsPosition.RIGHT_BACK, this.getScale());

        wheelsXOffset = 11.8f * this.getScale() / 10;
        wheelsZOffset = 7.6f * this.getScale() / 10;
        wheelYOffset = 3.3f * this.getScale() / 10;


    }

    private void applyPhysics() {
        Vector3f totalForces = new Vector3f();
        for (Force force : forces) {
            totalForces = Vector3f.add(totalForces, force.applyForce(this), totalForces);
        }
        Vector3f.add(totalForces, calculateGravitationalForce(), totalForces);
        acceleration.set(totalForces.x / mass, totalForces.y / mass, totalForces.z / mass);
        this.velocity.translate(acceleration.x * DisplayManager.getDeltaTime(),
                acceleration.y * DisplayManager.getDeltaTime(), acceleration.z * DisplayManager.getDeltaTime());
    }

    private void handleCurve() {
        if (leftFrontWheel.getRotOnYAngle(getRotY()) == 0)
            return;
        float L = 2.4f;  //distance between front and behind wheels
        float Sin = (float) Math.sin(Math.toRadians(rightFrontWheel.getRotOnYAngle(this.getRotY())));
        //curves variables
        float inf = Float.MAX_VALUE;
        float R = (Sin != 0 ? L / Sin : inf);   //Radius of the circle we're trying to rotate
        float angularVeclocity = (velocity.length() / R);
        Vector3f rotationAngle = new Vector3f(getUp());
        if (velocity.length() != 0)
            angularVeclocity *= Vector3f.dot(velocity, getForward()) / (velocity.length() * getForward().length());
        rotationAngle.scale(angularVeclocity);
        super.increaseRotation(0, angularVeclocity, 0);
        velocity.rotateOnY(angularVeclocity, getUp());
    }

    public void move(Terrain terrain) {
        checkInputs();
        handleHill(terrain);
        handleCurve();
        applyPhysics();
        super.increasePosition(velocity.x, velocity.y, velocity.z);
        applyWheelsEffects();
    }

    private void checkInputs() {
        brakePushed = Keyboard.isKeyDown(Keyboard.KEY_SPACE);

        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            forwardAcceleratorPushed = true;
            backwardAcceleratorPushed = false;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            backwardAcceleratorPushed = true;
            forwardAcceleratorPushed = false;
        } else {
            forwardAcceleratorPushed = false;
            backwardAcceleratorPushed = false;
            this.currentRollingWheelsSpeed = 0;
        }


        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            this.currentTurnOnYAxixSpeed = TURN_ON_Y_AXIS_SPEED;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            this.currentTurnOnYAxixSpeed = -TURN_ON_Y_AXIS_SPEED;
        } else {
            this.currentTurnOnYAxixSpeed = 0;
        }
    }

    public void addForce(Force force) {
        forces.add(force);
    }

    public float getMass() {
        return mass;
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public void setVelocity(float x, float y, float z) {
        this.velocity.x = x;
        this.velocity.y = y;
        this.velocity.z = z;
    }

    public boolean isBrakePushed() {
        return brakePushed;
    }

    public boolean isForwardAcceleratorPushed() {
        return forwardAcceleratorPushed;
    }

    public boolean isBackwardAcceleratorPushed() {
        return backwardAcceleratorPushed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public float getCurrentSpeed() {
        return this.velocity.length();
    }

    public float getWheelRadius() {
        return wheelRadius;
    }

    public List<Entity> getWheels() {
        List<Entity> wheels = new ArrayList<Entity>();
        wheels.add(leftBackWheel);
        wheels.add(rightBackWheel);
        wheels.add(leftFrontWheel);
        wheels.add(rightFrontWheel);
        return wheels;
    }

    private void applyWheelsEffects() {
        this.currentRollingWheelsSpeed = (this.velocity.length() * getScale() * 16.6667f) / (3.14f * 2 * wheelRadius);
        if (Vector3f.dot(getForward(), getVelocity()) < 0)
            currentRollingWheelsSpeed *= -1;
        leftFrontWheel.increaseRotation(0,
                currentTurnOnYAxixSpeed * 1.5f * DisplayManager.getDeltaTime(), -currentRollingWheelsSpeed * DisplayManager.getDeltaTime());
        rightFrontWheel.increaseRotation(0,
                currentTurnOnYAxixSpeed * 1.5f * DisplayManager.getDeltaTime(), currentRollingWheelsSpeed * DisplayManager.getDeltaTime());
        leftBackWheel.increaseRotation(0,
                0, -currentRollingWheelsSpeed * DisplayManager.getDeltaTime());
        rightBackWheel.increaseRotation(0,
                0, currentRollingWheelsSpeed * DisplayManager.getDeltaTime());
        leftBackWheel.setRotY(rotY);
        rightBackWheel.setRotY(rotY);
        if ((leftFrontWheel.getRotOnYAngle(this.getRotY())) < -45) {
            leftFrontWheel.setRotY(-45 + rotY);
            rightFrontWheel.setRotY(-45 + rotY);
        }
        if ((leftFrontWheel.getRotOnYAngle(this.getRotY())) > 45) {
            leftFrontWheel.setRotY(45 + rotY);
            rightFrontWheel.setRotY(45 + rotY);
        }

        if (currentTurnOnYAxixSpeed == 0 && ((leftFrontWheel.getRotY() - 90 != this.rotY))) {
            if ((leftFrontWheel.getRotOnYAngle(this.getRotY())) > 1.5) {
                leftFrontWheel.increaseRotation(0,
                        -TURN_ON_Y_AXIS_SPEED * 3f * DisplayManager.getDeltaTime(), 0);
                rightFrontWheel.increaseRotation(0,
                        -TURN_ON_Y_AXIS_SPEED * 3f * DisplayManager.getDeltaTime(), 0);

            } else if ((leftFrontWheel.getRotOnYAngle(this.getRotY())) < -1.5) {
                leftFrontWheel.increaseRotation(0,
                        TURN_ON_Y_AXIS_SPEED * 3f * DisplayManager.getDeltaTime(), 0);
                rightFrontWheel.increaseRotation(0,
                        TURN_ON_Y_AXIS_SPEED * 3f * DisplayManager.getDeltaTime(), 0);
            } else {
                leftFrontWheel.setRotY(rotY);
                rightFrontWheel.setRotY(rotY);
            }
        }

        float h = (float) Math.sqrt(Math.pow(wheelsXOffset, 2) + Math.pow(wheelsZOffset, 2));
        float theta = (float) Math.atan(wheelsZOffset / wheelsXOffset);
        float xDist = (float) Math.sin(Math.toRadians(this.getRotY()) + theta) * h;
        float zDist = (float) Math.cos(Math.toRadians(this.getRotY()) + theta) * h;
        float negativeXDist = (float) Math.sin(-Math.toRadians(this.getRotY()) + theta) * h;
        float negativeZDist = (float) Math.cos(-Math.toRadians(this.getRotY()) + theta) * h;
        leftFrontWheel.setPosition(this.getPosition().x - xDist, this.getPosition().y + wheelYOffset, this.getPosition().z - zDist);
        rightFrontWheel.setPosition(this.getPosition().x + negativeXDist, this.getPosition().y + wheelYOffset, this.getPosition().z - negativeZDist);
        leftBackWheel.setPosition(this.getPosition().x - negativeXDist, this.getPosition().y + wheelYOffset, this.getPosition().z + negativeZDist);
        rightBackWheel.setPosition(this.getPosition().x + xDist, this.getPosition().y + wheelYOffset, this.getPosition().z + zDist);
        List<Entity> wheels = getWheels();
        for (Entity entity : wheels) {
            float WheelsyPosition = (float) (Math.tan(Math.toRadians(getRotX())) * (entity.getPosition().z - this.getPosition().z));
            entity.increasePosition(0, -WheelsyPosition, 0);
        }
    }

    private void handleHill(Terrain terrain) {
        if (Math.abs(position.z) >= terrain.getSIZE()) {
            if (!rotateFromGroundToHillChanged) {
                this.rotX = terrain.getRotZ();
                velocity.rotateOnX(terrain.getRotZ());
                rotateFromGroundToHillChanged = true;
            }

        } else {
            if (rotateFromGroundToHillChanged) {
                this.rotX = 0;
                this.position.y = 0;
                velocity.rotateOnX(-terrain.getRotZ());
                rotateFromGroundToHillChanged = false;
            }

        }
    }

    private Vector3f calculateGravitationalForce() {
        Vector3D gravitationalForce = new Vector3D(0, 0, -1);
        gravitationalForce.rotateOnX(getRotX());
        Vector3f normalisedVelocityVector = new Vector3f(velocity);
        if (normalisedVelocityVector.length() != 0)
            normalisedVelocityVector.normalise();
        float uv = Vector3f.dot(gravitationalForce, normalisedVelocityVector);
        if (uv != 0)
            uv /= (float) Math.pow(gravitationalForce.length(), 2);
        normalisedVelocityVector.scale(uv);
        gravitationalForce.set(normalisedVelocityVector);
        float g = 9.80665f;
        gravitationalForce.scale((float) (mass * g * Math.sin(Math.toRadians(getRotX()))));
        gravitationalForce.negate();
        return gravitationalForce;
    }


}
