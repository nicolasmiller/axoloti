package axoloti.piccolo;

import java.awt.Color;

public class PatchLayoutTestPNode extends PatchPNode {

    public PatchLayoutTestPNode() {
        setPaint(Color.BLACK);
        setPickable(true);
        double r = Math.random();
        if (r >= 0.66) {
            setLayout(PNodeLayout.VERTICAL_LEFT);
        } else if (r >= 0.33) {
            setLayout(PNodeLayout.VERTICAL_RIGHT);
        } else {
            setLayout(PNodeLayout.VERTICAL_CENTERED);
        }

        for (int i = 0; i < (int) (Math.random() * 10 + 3); i++) {
            PatchPNode p = new PatchPNode();
            p.setPaint(Color.RED);

            if (Math.random() > 0.5) {
                p.setLayout(PNodeLayout.VERTICAL_LEFT);
            } else {
                p.setLayout(PNodeLayout.HORIZONTAL_TOP);
            }

            for (int j = 0; j < ((int) (Math.random() * 10)); j++) {
                PatchPNode q = new PatchPNode();
                if (j % 2 == 0) {
                    q.setPaint(Color.GREEN);
                } else {
                    q.setPaint(Color.BLUE);
                }
                q.setBounds(0, 0, 20, 20);
                p.addChild(q);
            }
            p.setBounds(0, 0, p.getChildrenWidth(), p.getChildrenHeight());
            addChild(p);
        }
        setBounds(0, 0, getChildrenWidth(), getChildrenHeight());
    }
}
