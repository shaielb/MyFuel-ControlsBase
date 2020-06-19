package controls;

import adapter.base.ControlAdapter;
import javafx.scene.control.CheckBox;

public class MfCheckBox extends ControlAdapter<Object> {

	// can be either IEnum or String
	private Object _validValue;

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
				if (_entity != null && _field != null) {
					_field.set(_entity, _validValue == null ? newValue : (newValue ? _validValue : null));
				}
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
