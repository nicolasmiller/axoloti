package axoloti.parameterviews;

import axoloti.Preset;
import axoloti.datatypes.Value;
import axoloti.parameters.ParameterInstance;
import components.control.ACtrlComponent;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;

public interface IParameterInstanceView {
    public void PostConstructor();
    public void doPopup(MouseEvent e);
    public void populatePopup(JPopupMenu m);
    public ACtrlComponent getControlComponent();
    public boolean handleAdjustment();
    public ACtrlComponent CreateControl();
    public String getName();
    public void updateV();
    public void SetMidiCC(Integer cc);
    public void SetValueRaw(int v);
    public void ShowPreset(int i);

    public boolean isOnParent();
    public void IncludeInPreset();
    public void ExcludeFromPreset();
    public void CopyValueFrom(ParameterInstanceView p);
    public void setValue(Value value);
    public ParameterInstance getParameterInstance();
    public Preset AddPreset(int index, Value value);
    public void RemovePreset(int index);
}