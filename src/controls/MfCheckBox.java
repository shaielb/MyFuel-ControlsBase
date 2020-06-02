package controls;

import adapter.base.ControlAdapter;
import javafx.scene.control.CheckBox;

public class MfCheckBox extends ControlAdapter<Boolean> {

	private String _validValue;
	
	private CheckBox _control;

	public MfCheckBox() {
		setControl(_control = new CheckBox());
	}

	public MfCheckBox(CheckBox cb) {
		setControl(_control = cb);
	}

	@Override
	protected void initialize() {
		super.initialize();
		_control.setOnAction((event) -> {
			try {
				Boolean newValue = getValue();
				_field.set(_entity, newValue);
				runEvents(newValue);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			runEvents(event);
		});
	}

	@Override
	public void clear() throws Exception {
		_field.set(_entity, false);
	}

	@Override
	public Boolean getValue() {
		return _control.isSelected();
	}

	@Override
	public void setValue(Boolean value) throws Exception {
		super.setValue(value);
		_control.setSelected(value);
	}
	
	public void setValue(String value) throws Exception {
		Boolean val = value.equals(_validValue);
		super.setValue(val);
		_control.setSelected(val);
	}
	
	public void setValidValue(String validValue) {
		_validValue = validValue;
	}
}
