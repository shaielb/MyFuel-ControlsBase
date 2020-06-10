package controls;

import adapter.base.ControlAdapter;
import javafx.scene.control.RadioButton;

public class MfRadioButton extends ControlAdapter<Object> {

	// can be either IEnum or String
	private Object _validValue;
	
	private RadioButton _control;

	public MfRadioButton() {
		setControl(_control = new RadioButton());
	}

	public MfRadioButton(RadioButton rb) {
		setControl(_control = rb);
	}

	@Override
	protected void initialize() {
		super.initialize();
		_control.setOnAction((event) -> {
			try {
				Boolean newValue = getValue();
				_field.set(_entity, _validValue == null ? newValue : (newValue ? _validValue : null));
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
	public void setValue(Object value) throws Exception {
		if (value == null) {
			_control.setSelected(false);
		}
		else {
			Boolean val = (value instanceof Boolean) ? (Boolean) value : value.equals(_validValue);
			if (val) {
				super.setValue(val);
			}
			_control.setSelected(val);
		}
	}
	
	public void setValue(String value) throws Exception {
		Boolean val = value.equals(_validValue);
		super.setValue(val);
		_control.setSelected(val);
	}
	
	public void setValidValue(Object validValue) {
		_validValue = validValue;
	}
}
