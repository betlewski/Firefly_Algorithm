package sample.fa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static sample.function.FunctionUtils.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Firefly {

    private double posX;
    private double posZ;
    private double luminescence = 0;

    public Firefly(double min, double max) {
        setRandomPosition(min, max);
        updateLuminescence();
    }

    private void setRandomPosition(double min, double max) {
        this.posX = randDoubleInRange(min, max);
        this.posZ = randDoubleInRange(min, max);
    }

    private double randDoubleInRange(double min, double max) {
        return Math.random() * (max - min) + min;
    }

    public void updateLuminescence() {
        luminescence = getFunction().getFitness(this.posX, this.posZ);
    }

    public void moveToAnother(Firefly another, double attraction) {
        double anotherX = another.getPosX();
        double anotherZ = another.getPosZ();
        this.posX = this.posX + attraction * (anotherX - this.posX);
        this.posZ = this.posZ + attraction * (anotherZ - this.posZ);
        checkPositionRange();
    }

    public void moveRandomly(double randomStepCoefficient) {
        double randomX = randomStepCoefficient * (Math.random() - 0.5);
        double randomZ = randomStepCoefficient * (Math.random() - 0.5);
        this.posX = this.posX + randomX;
        this.posZ = this.posZ + randomZ;
        checkPositionRange();
    }

    public double eval() {
        return getFunction().getValueInPoint(this.posX, this.posZ);
    }

    public double getDistanceTo(Firefly another) {
        double differenceX = Math.pow(another.getPosX() - this.posX, 2);
        double differenceZ = Math.pow(another.getPosZ() - this.posZ, 2);
        double differenceY = Math.pow(another.eval() - this.eval(), 2);
        return Math.sqrt(differenceX + differenceY + differenceZ);
    }

    private void checkPositionRange() {
        if (this.posX > MAX_RANGE) {
            this.posX = MAX_RANGE;
        }
        if (this.posX < MIN_RANGE) {
            this.posX = MIN_RANGE;
        }
        if (this.posZ > MAX_RANGE) {
            this.posZ = MAX_RANGE;
        }
        if (this.posZ < MIN_RANGE) {
            this.posZ = MIN_RANGE;
        }
    }

}
