package org.onap.simulator.model;

public class Misc {
    private Integer numberOfTimes;
    private boolean replace = true;

    public Integer getNumberOfTimes() {
        return numberOfTimes;
    }

    public void setNumberOfTimes(Integer numberOfTimes) {
        this.numberOfTimes = numberOfTimes;
    }

    public boolean getReplace() {
        return replace;
    }

    public void setReplace(Boolean replace) {
        this.replace = replace;
    }

    @Override
    public String toString() {
        return "Misc{" +
                "numberOfTimes='" + numberOfTimes + '\'' +
                ", replace='" + replace + '\'' +
                '}';
    }
}
