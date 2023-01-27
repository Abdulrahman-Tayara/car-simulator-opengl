package phisics;

import entities.Car;
import org.lwjgl.util.vector.Vector3f;

public class AirResistanceForce extends Force {
    private float p = 1.2f;
    private float Cd = 0.82f;
    private float carSurfaceArea;
    private Vector3f windVelocity;

    public AirResistanceForce(float carSurfaceArea, Vector3f windVelocity) {
        this.carSurfaceArea = carSurfaceArea;
        this.windVelocity = windVelocity;
    }

    @Override
    public Vector3f applyForce(Car car) {
        float windVelocityValue = (float) (0.5 * p * Cd * carSurfaceArea *
                Math.pow((car.getVelocity().length() - windVelocity.length()), 2));
        windVelocity.normalise();
        Vector3f windVelocityVector = new Vector3f();
        windVelocityVector.x = windVelocityValue * windVelocity.x;
        windVelocityVector.y = windVelocityValue * windVelocity.y;
        windVelocityVector.z = windVelocityValue * windVelocity.z;
        float uv = Vector3f.dot(windVelocityVector, car.getVelocity());
        uv /= Math.pow(car.getForward().length(), 2);
        Vector3f normalisedVelocityVector = new Vector3f(car.getVelocity());
        if (normalisedVelocityVector.length() != 0)
            normalisedVelocityVector.normalise();
        windVelocityVector.x = normalisedVelocityVector.x * uv;
        windVelocityVector.y = normalisedVelocityVector.y * uv;
        windVelocityVector.z = normalisedVelocityVector.z * uv;
        return windVelocityVector;
    }
}
