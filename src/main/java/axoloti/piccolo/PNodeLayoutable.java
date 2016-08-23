package axoloti.piccolo;

import org.piccolo2d.util.PBounds;

public interface PNodeLayoutable {

    public void setLayout(final PNodeLayout layout);

    public PNodeLayout getLayout();

    public PBounds getFullBoundsReference();

    public double getChildrenWidth();

    public double getChildrenHeight();

    public double getX();

    public double getY();

    public void setOffset(double x, double y);
}
