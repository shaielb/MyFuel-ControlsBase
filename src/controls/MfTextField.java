package controls;

import adapter.base.ControlAdapter;
import javafx.scene.control.TextField;

public class MfTextField extends ControlAdapter<String> {

	private TextField _control;

	public MfTextField() {
		setControl(_control = new TextField());
	}

	public MfTextField(TextField tf) {
		setControl(_control = tf);
	}

	@Override
	protected void initialize() {
		super.initialize();
		_control.textProperty().addListener((obs, oldVal, newVal) -> {
			try {
				String nValue = getValue();
				_field.set(_entity, nValue);
				runEvents(nValue);
			} catch (Exception e) {
				e.printStackTrace();
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
