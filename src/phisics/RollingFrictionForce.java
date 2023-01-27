package phisics;

import entities.Car;
import org.lwjgl.util.vector.Vector3f;

public class RollingFrictionForce extends Force {
    private float rollingFrictionCoefficient;
    private Vector3f rollingFrictionForceVector;
    float theta;
    private float g = 9.80665f;

    public RollingFrictionForce(float rollingFrictionCoefficient) {
        this.rollingFrictionCoefficient = rollingFrictionCoefficient;
        rollingFrictionForceVector = new Vector3f();
    }

    @Override
    public Vector3f applyForce(Car car) {
        theta = car.getRotX();
        if (car.getCurrentSpeed() != 0) {
            float rollingFrictionForceValue = (float) -(rollingFrictionCoefficient * car.getMass() * Math.cos(Math.toRadians(theta)) * g);
            rollingFrictionForceVector.set(car.getVelocity());
            if (rollingFrictionForceVector.length() != 0)
                rollingFrictionForceVector.normalise();
            rollingFrictionForceVector.scale(rollingFrictionForceValue);
        }
        return rollingFrictionForceVector;
    }
}
