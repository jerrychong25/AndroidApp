package king.upihc;

/**
 * Created by user on 9/4/2017.
 */
public class RehabilitationDetails {
    String childDate;
    String childTimeStart;
    String childTimeStop;
    String childDuration;
    String childMotorSpeed;
    String childOrientation;

    public RehabilitationDetails(String childDate, String childTimeStart, String childTimeStop, String childDuration, String childMotorSpeed, String childOrientation) {
        this.childDate = childDate;
        this.childTimeStart = childTimeStart;
        this.childTimeStop = childTimeStop;
        this.childDuration = childDuration;
        this.childMotorSpeed = childMotorSpeed;
        this.childOrientation = childOrientation;
    }

    public String getChildDate() {
        return childDate;
    }

    public String getChildTimeStart() {
        return childTimeStart;
    }

    public String getChildTimeStop() {
        return childTimeStop;
    }

    public String getChildDuration() {
        return childDuration;
    }

    public String getChildMotorSpeed() {
        return childMotorSpeed;
    }

    public String getChildOrientation() {
        return childOrientation;
    }
}