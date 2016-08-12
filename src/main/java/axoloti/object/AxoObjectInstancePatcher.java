/**
 * Copyright (C) 2013, 2014 Johannes Taelman
 *
 * This file is part of Axoloti.
 *
 * Axoloti is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Axoloti is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Axoloti. If not, see <http://www.gnu.org/licenses/>.
 */
package axoloti.object;

import axoloti.MainFrame;
import axoloti.PatchController;
import axoloti.PatchFrame;
import axoloti.PatchModel;
import axoloti.PatchView;
import axoloti.objectviews.AxoObjectInstanceViewPatcher;
import java.awt.Point;
import org.simpleframework.xml.Element;

/**
 *
 * @author Johannes Taelman
 */
public class AxoObjectInstancePatcher extends AxoObjectInstance {

    PatchFrame pf;
    @Element(name = "subpatch")
    PatchController patchController;

    public AxoObjectInstancePatcher() {
    }

    public AxoObjectInstancePatcher(AxoObject type, PatchModel patch1, String InstanceName1, Point location) {
        super(type, patch1, InstanceName1, location);
    }

    @Override
    public void updateObj1() {
        if (patchController == null) {
            patchModel = new PatchModel();
            patchController = new PatchController();
            PatchView patchView = new PatchView(patchController);
            patchModel.addModelChangedListener(patchView);
            patchController.setPatchView(patchView);
            patchController.setPatchModel(patchModel);
        }
        if (pf == null) {
            pf = new PatchFrame(patchController, MainFrame.mainframe.getQcmdprocessor());
            patchController.patchView.setFileNamePath(getInstanceName());
            patchController.patchView.PostConstructor();
        }
        if (patchController != null) {
            AxoObject ao = patchController.patchModel.GenerateAxoObj(new AxoObjectPatcher());
            setType(ao);
            ao.id = "patch/patcher";
            ao.sDescription = patchController.patchModel.getNotes();
            ao.sLicense = patchController.patchModel.getSettings().getLicense();
            ao.sAuthor = patchController.patchModel.getSettings().getAuthor();
            patchController.patchModel.container(patchModel);
        }
    }

    public void edit() {
        if (patchController == null) {
            patchModel = new PatchModel();
            patchController = new PatchController();
            PatchView patchView = new PatchView(patchController);
            patchModel.addModelChangedListener(patchView);
            patchController.setPatchModel(patchModel);
            patchController.setPatchView(patchView);
        }
        if (pf == null) {
            pf = new PatchFrame(patchController, MainFrame.mainframe.getQcmdprocessor());
            patchController.patchView.setFileNamePath(getInstanceName());
            patchController.patchView.PostConstructor();
        }
        pf.setState(java.awt.Frame.NORMAL);
        pf.setVisible(true);
    }
    
    @Override    
    public AxoObjectInstanceViewPatcher ViewFactory(PatchView patchView) {
        return new AxoObjectInstanceViewPatcher(this, patchView);
    }
    
    public PatchController getPatchController() {
        return patchController;
    }
    
    public PatchFrame getPatchFrame() {
        return pf;
    }
}