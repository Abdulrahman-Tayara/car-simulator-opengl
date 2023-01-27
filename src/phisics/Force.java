package phisics;

import entities.Car;
import org.lwjgl.util.vector.Vector3f;

public abstract class Force {


    public abstract Vector3f applyForce(Car car);

}
