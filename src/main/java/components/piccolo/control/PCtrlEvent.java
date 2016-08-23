package components.piccolo.control;

public class PCtrlEvent {

    double value;
    PCtrlComponentAbstract source;

    PCtrlEvent(PCtrlComponentAbstract source, double value) {
        this.value = value;
    }

    public PCtrlComponentAbstract getSource() {
        return source;
    }

    public double getValue() {
        return value;
    }
}
