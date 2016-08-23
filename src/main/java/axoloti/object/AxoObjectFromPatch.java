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
import axoloti.PatchViewSwing;
import java.awt.Rectangle;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;

/**
 *
 * @author Johannes Taelman
 */
public class AxoObjectFromPatch extends AxoObject {

    PatchModel patchModel;
    PatchView patchView;
    PatchController patchController;
    PatchFrame pf;
    File f;

    public AxoObjectFromPatch(File f) {
        this.f = f;
        Serializer serializer = new Persister();
        try {
            patchModel = serializer.read(PatchModel.class, f);
            patchController = new PatchController();
            patchView = MainFrame.prefs.getPatchView(patchController);
            patchModel.addModelChangedListener(patchView);
            patchController.setPatchModel(patchModel);
            patchController.setPatchView(patchView);
            patchView.setFileNamePath(f.getAbsolutePath());
            patchView.PostConstructor();
        } catch (Exception ex) {
            Logger.getLogger(AxoObjects.class.getName()).log(Level.SEVERE, null, ex);
        }
        shortId = f.getName().substring(0, f.getName().lastIndexOf("."));
        sPath = f.getAbsolutePath();
        UpdateObject();
        MainFrame.axoObjects.ObjectList.add(this);
        // strip file extension
    }

    final public void UpdateObject() {
        AxoObject o = patchModel.GenerateAxoObj(new AxoObject());
        attributes = o.attributes;
        depends = o.depends;
        displays = o.displays;
        id = f.getName().substring(0, f.getName().length() - 4);
        includes = o.includes;
        inlets = o.inlets;
        outlets = o.outlets;
        params = o.params;
        sAuthor = o.sAuthor;
        sDescription = o.sDescription;
        sDisposeCode = o.sDisposeCode;
        sInitCode = o.sInitCode;
        sKRateCode = o.sKRateCode;
        sLicense = o.sLicense;
        sLocalData = o.sLocalData;
        sMidiCode = o.sMidiCode;
        sSRateCode = o.sSRateCode;
        helpPatch = o.helpPatch;

        FireObjectModified(this);
    }

    @Override
    public void OpenEditor(Rectangle editorBounds, Integer editorActiveTabIndex) {
        if (patchModel == null) {
            Strategy strategy = new AnnotationStrategy();
            Serializer serializer = new Persister(strategy);
            try {
                patchModel = serializer.read(PatchModel.class, f);
                PatchController patchController = new PatchController();
                PatchView patchView = MainFrame.prefs.getPatchView(patchController);
                patchModel.addModelChangedListener(patchView);
                patchController.setPatchModel(patchModel);
                patchController.setPatchView(patchView);
                pf = new PatchFrame(patchController, MainFrame.mainframe.getQcmdprocessor());
                patchView.setFileNamePath(f.getPath());
                patchView.PostConstructor();
                patchView.setObjEditor(this);
            } catch (Exception ex) {
                Logger.getLogger(AxoObjects.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (pf == null) {
            PatchController patchController = new PatchController();
            PatchView patchView = MainFrame.prefs.getPatchView(patchController);
            patchController.setPatchModel(patchModel);
            patchController.setPatchView(patchView);
            pf = new PatchFrame(patchController, MainFrame.mainframe.getQcmdprocessor());
            patchView.setFileNamePath(id);
            patchView.PostConstructor();
        }
        pf.setState(java.awt.Frame.NORMAL);
        pf.setVisible(true);
        pf.startRendering();
    }

}
