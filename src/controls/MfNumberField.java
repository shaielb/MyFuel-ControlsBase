package controls;

import adapter.base.ControlAdapter;
import db.interfaces.IEntity;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import utilities.StringUtil;

public class MfNumberField extends ControlAdapter<Number> {

	private interface Cast {
		Number cast();
	}

	private Text _errorLabel;
	
	private Text _infoLbl = new Text();
	
	private BorderPane _wrapper = new BorderPane();

	private TextField _control;

	private Class<?> _numberType;

	private Cast _cast;

	private boolean _useWrapper = false;

	public MfNumberField(Class<?> numberType) {
		_numberType = numberType;
		setControl(_control = new TextField());
	}

	public MfNumberField(Class<?> numberType, boolean useWrapper) {
		_numberType = numberType;
		_useWrapper = useWrapper;
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
		_infoLbl.setId("error-label");
		_infoLbl.setText("Only Numbers Please");
		_infoLbl.setStyle("-fx-prompt-text-fill: red");

		if (_useWrapper) {
			_wrapper.setCenter(_control);
		}
		_control.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, 
					String oldValue, String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					_control.setText(newValue.replaceAll("[^\\d\\.]", ""));
					if (_useWrapper) {
						_wrapper.setTop(_infoLbl);
					}
					if (_errorLabel != null) {
						_errorLabel.setVisible(true);
					}
				}
				else {
					if (_useWrapper) {
						_wrapper.setTop(null);
					}
					if (_errorLabel != null) {
						_errorLabel.setVisible(false);
					}
					try {
						Number value = getValue();
						_field.set(_entity, value);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
					runEvents(newValue);
				}
			}
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
		String text = _control.getText();
		if (StringUtil.isEmpty(text)) {
			text = "0";
		}
		return Integer.parseInt(text);
	}

	private Number castDouble() {
		String text = _control.getText();
		if (StringUtil.isEmpty(text)) {
			text = "0";
		}
		return Double.parseDouble(text);
	}

	private Number castFloat() {
		String text = _control.getText();
		if (StringUtil.isEmpty(text)) {
			text = "0";
		}
		return Float.parseFloat(text);
	}

	@Override
	public Region getInstance() {
		return _wrapper;
	}

	@Override
	public void setEntity(IEntity entity) throws Exception {
		_entity = entity;
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
	public void setValue(Number value) throws Exception {
		_control.setText((value == null) ? "" : value.toString());
		if (value == null) {
			value = 0;
		}
		super.setValue(value);
	}

	public void setUseWrapper(boolean useWrapper) {
		_useWrapper = useWrapper;
	}
	
	public void setErrorLabel(Text text) {
		_errorLabel = text;
	}
}
