package axoloti.piccolo;

import org.piccolo2d.util.PBounds;

public interface PNodeLayoutable {

    public void setLayout(final PNodeLayout layout);

    public PNodeLayout getLayout();

//    public double getChildrenLayoutExtent();
    public PBounds getFullBoundsReference();

//    public PBounds getUnionOfChildrenBounds(PBounds b);
    public double getChildrenWidth();

    public double getChildrenHeight();

    public double getX();

    public double getY();

    public void setOffset(double x, double y);
}
