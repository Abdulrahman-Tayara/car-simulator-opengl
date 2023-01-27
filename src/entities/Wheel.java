package entities;

import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

public class Wheel extends Entity {
    private WheelsPosition wheelsPosition;

    public Wheel() {
    }

    public Wheel(TexturedModel model, Vector3f position, WheelsPosition wheelsPosition, float scale) {
        super(model, position, 0, 0, 0, scale);
        this.wheelsPosition = wheelsPosition;
        if (wheelsPosition == WheelsPosition.LEFT_BACK || wheelsPosition == WheelsPosition.LEFT_FRONT) {
            this.rotX = 0;
            this.rotY = 90;
            this.rotZ = 0;

        } else if (wheelsPosition == WheelsPosition.RIGHT_BACK || wheelsPosition == WheelsPosition.RIGHT_FRONT) {
            this.rotX = 0;
            this.rotY = -90;
            this.rotZ = 0;
        }

    }


    public float getRotOnYAngle(float carRoteteOnUpAngle) {
        if (this.wheelsPosition == WheelsPosition.LEFT_BACK || this.wheelsPosition == WheelsPosition.LEFT_FRONT) {
            return (this.rotY - 90 - carRoteteOnUpAngle);
        } else {
            return (this.rotY + 90 - carRoteteOnUpAngle);
        }

    }

    @Override
    public void setRotY(float rotY) {
        if (wheelsPosition == WheelsPosition.LEFT_BACK || wheelsPosition == WheelsPosition.LEFT_FRONT) {
            super.setRotY(rotY + 90);
        } else {
            super.setRotY(rotY - 90);
        }

    }
}
