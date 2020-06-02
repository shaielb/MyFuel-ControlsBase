package controls;

import adapter.base.ControlAdapter;
import db.interfaces.IEntity;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;

public class MfNumberField extends ControlAdapter<Number> {

	private interface Cast {
		Number cast();
	}

	private Label _infoLbl = new Label();
	private BorderPane _wrapper = new BorderPane();

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
		_infoLbl.setId("error-label");
		_infoLbl.setText("Only Numbers Please");

		_wrapper.setCenter(_control);
		_control.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, 
		    		String oldValue, String newValue) {
		        if (!newValue.matches("\\d*")) {
		        	_control.setText(newValue.replaceAll("[^\\d]", ""));
		        	ObservableList<String> styleClass = _control.getStyleClass();
					if (!styleClass.contains("error")) {
						styleClass.add("error");
						_wrapper.setTop(_infoLbl);
					}
		        }
		        else {
		        	ObservableList<String> styleClass = _control.getStyleClass();
					if (styleClass.contains("error")) {
						styleClass.remove("error");
						_wrapper.setTop(null);
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
		return Integer.parseInt(_control.getText());
	}

	private Number castDouble() {
		return Double.parseDouble(_control.getText());
	}

	private Number castFloat() {
		return Float.parseFloat(_control.getText());
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
		super.setValue(value);
		_control.setText(value.toString());
	}
}
