package controls;

import db.interfaces.IEntity;
import decorator.base.ControlDecorator;
import javafx.scene.control.TextField;

public class MfTextField extends ControlDecorator<String> {

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
		_control.focusedProperty().addListener((obs, oldVal, newVal) -> {
			if (!newVal) {
				try {
					_field.set(_entity, getValue());
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
	public void setEntity(IEntity entity) throws Exception {
		super.setEntity(entity);
		_control.setText((String) _field.get(entity));
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
	public void setValue(String value) {
		_control.setText(value);
	}
}
