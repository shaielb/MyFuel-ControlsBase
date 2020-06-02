package controls;

import adapter.base.ControlAdapter;
import javafx.scene.control.PasswordField;

public class MfPasswordField extends ControlAdapter<String> {

	private PasswordField _control;

	public MfPasswordField() {
		setControl(_control = new PasswordField());
	}

	public MfPasswordField(PasswordField tf) {
		setControl(_control = tf);
	}

	@Override
	protected void initialize() {
		super.initialize();
		_control.focusedProperty().addListener((obs, oldVal, newVal) -> {
			if (!newVal) {
				try {
					String nValue = getValue();
					_field.set(_entity, nValue);
					runEvents(nValue);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		_control.setOnAction((event) -> {
			runEvents(event);
		});
	}

	@Override
	public void clear() throws Exception {
		_field.set(_entity, "");
	}

	@Override
	public String getValue() {
		return _control.getText();
	}

	@Override
	public void setValue(String value) throws Exception {
		super.setValue(value);
		_control.setText(value);
	}
}
