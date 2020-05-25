package controls;

import db.interfaces.IEntity;
import decorator.base.ControlDecorator;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;

public class MfNumberField extends ControlDecorator<Number> {

	private interface Cast {
		Number cast();
	}

	private TextField _control;

	private Class<?> _numberType;

	private Cast _cast;

	public MfNumberField(Class<?> numberType) {
		_numberType = numberType;
		setControl(_control = new TextField());
	}

	public MfNumberField(TextField tf, Class<?> numberType) {
		_numberType = numberType;
		setControl(_control = tf);
	}

	@Override
	protected void initialize() {
		super.initialize();
		establishType(_numberType);
		_control.setOnKeyPressed(event ->  {
			String codeString = event.getCode().toString();
			if (!codeString.toLowerCase().startsWith("digit") && 
					!codeString.toLowerCase().equals("back_space")) {
				_control.setPromptText("Please Enter A Number");
				event.consume();
				ObservableList<String> styleClass = _control.getStyleClass();
				if (!styleClass.contains("error")) {
					styleClass.add("error");
				}
			}
			else {
				ObservableList<String> styleClass = _control.getStyleClass();
				if (styleClass.contains("error")) {
					styleClass.remove("error");
				}
			}
			String currText = _control.getText();
			if (currText.length() > 0 && !currText.matches("\\d+")) {
				_control.setText(currText.substring(0, currText.length() - 1));
				_control.requestFocus();
				_control.end();
			}
		});

		_control.focusedProperty().addListener((obs, oldVal, newVal) -> {
			String currText = _control.getText();
			if (currText.length() > 0) {
				if (!newVal) {
					if (_control.getText().matches("\\d+")) {
						try {
							_field.set(_entity, getValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		});

		_control.setOnAction((event) -> {
			try {
				_field.set(_entity, _field.getType().cast(getValue()));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			runEvents(event);
		});
	}

	private void establishType(Class<?> numberType) {
		if (numberType.isAssignableFrom(Integer.class)) {
			_cast = () -> castInteger();
		}
		else if (numberType.isAssignableFrom(Double.class)) {
			_cast = () -> castDouble();
		}
		else if (numberType.isAssignableFrom(Float.class)) {
			_cast = () -> castFloat();
		}
	}

	private Number castInteger() {
		return Integer.parseInt(_control.getText());
	}

	private Number castDouble() {
		return Double.parseDouble(_control.getText());
	}

	private Number castFloat() {
		return Float.parseFloat(_control.getText());
	}

	@Override
	public void setEntity(IEntity entity) throws Exception {
		super.setEntity(entity);
		Object value = _field.get(entity);
		_control.setText(value == null ? "" : value.toString());
	}

	@Override
	public void clear() throws Exception {
		_field.set(_entity, -1);
	}

	@Override
	public Number getValue() {
		return _cast.cast();
	}

	@Override
	public void setValue(Number value) {
		_control.setText(value.toString());
	}
}
