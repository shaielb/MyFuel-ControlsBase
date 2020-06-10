package controls;

import adapter.base.ControlAdapter;
import javafx.scene.control.ToggleButton;

public class MfToggleButton extends ControlAdapter<Object> {

	// can be either IEnum or String
	private Object _validValue;

	private ToggleButton _control;

	public MfToggleButton(String title) {
		setControl(_control = new ToggleButton(title));
	}

	public MfToggleButton(ToggleButton batton) {
		setControl(_control = batton);
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
	public void clear() {}

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

	public void setValue(String value) {
		_control.setText(value);
	}

	public void setValidValue(Object validValue) {
		_validValue = validValue;
	}
}
