package phisics;


import entities.Car;
import org.lwjgl.util.vector.Vector3f;

public class BrakeForce extends Force {
    private float brakeCoefficient;
    private Vector3f brakeVector;
    private float g = 9.80665f;

    public BrakeForce(float brakeCoefficient) {
        this.brakeCoefficient = brakeCoefficient;
        brakeVector = new Vector3f();
    }

    @Override
    public Vector3f applyForce(Car car) {
        if (car.isBrakePushed() && car.getCurrentSpeed() != 0) {
            float brakeValue = -brakeCoefficient * car.getMass() * g;
            brakeVector.set(car.getVelocity());
            if (brakeVector.length() != 0)
                brakeVector.normalise();
            brakeVector.scale(brakeValue);
            return brakeVector;
        } else return new Vector3f();
    }
}
