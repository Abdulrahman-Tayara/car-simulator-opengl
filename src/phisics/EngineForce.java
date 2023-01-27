package phisics;

import entities.Car;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;

public class EngineForce extends Force {
    private float engineTourqe;
    private float[] gearRatios;
    private float rbm;
    private int index;
    private boolean setMaxSpeedLimit = false;
    private float engineForceValue;
    private Vector3f engineForceVector;

    public EngineForce(float engineTourqe) {
        this.engineTourqe = engineTourqe;
        this.gearRatios = new float[]{3.46f, 4.171f, 2.34f, 1.521f, 1.143f, 0.867f, 0.691f};
        engineForceVector = new Vector3f();
    }

    @Override
    public Vector3f applyForce(Car car) {
        if (car.isForwardAcceleratorPushed()) {
            if (!car.isBrakePushed())
                rbm += 1200 * DisplayManager.getDeltaTime();
            if (rbm > 8000)
                rbm = 8000;
            index = (int) (Math.floor(rbm / 1300f));
            if (index == gearRatios.length - 1) {
                if (!setMaxSpeedLimit) {
                    car.setMaxSpeed(car.getCurrentSpeed());
                    setMaxSpeedLimit = true;
                }
            }
            if(car.getRotX()>0)
                index = 2;
            engineForceValue = gearRatios[0] * gearRatios[index] * engineTourqe / car.getWheelRadius();
        } else if (car.isBackwardAcceleratorPushed()) {
            if (!car.isBrakePushed())
                rbm += 1200 * DisplayManager.getDeltaTime();
            if (rbm > 8000)
                rbm = 8000;
            index = (int) (Math.floor(rbm / 1300f));
            if (index == gearRatios.length - 1) {
                if (!setMaxSpeedLimit) {
                    car.setMaxSpeed(car.getCurrentSpeed());
                    setMaxSpeedLimit = true;
                }
            }
            if(car.getRotX()>0)
                index = 2;
            engineForceValue = -gearRatios[0] * gearRatios[index] * engineTourqe / car.getWheelRadius();
        } else {
            if (car.isBrakePushed())
                rbm = 1300;
            else
                rbm -= 4000 * DisplayManager.getDeltaTime();
            rbm = (rbm<1300 ? 1300 : rbm);
            index = (int) (Math.floor(rbm / 1300f));


            return new Vector3f();
           // if (car.getCurrentSpeed() != 0) {
           //     engineForceValue = -gearRatios[0] * gearRatios[3] * engineTourqe / car.getWheelRadius();
           //     if (car.getVelocity().length() != 0)
           //         engineForceValue *= Vector3f.dot(car.getVelocity(), car.getForward()) / (car.getVelocity().length() * car.getForward().length());
           //     engineForceVector.set(car.getForward());
           //     engineForceVector.scale(engineForceValue);
           //     return engineForceVector;
           // }
        }
        engineForceVector.set(car.getForward());
        engineForceVector.scale(engineForceValue);
        return engineForceVector;

    }
}
