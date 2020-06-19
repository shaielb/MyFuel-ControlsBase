package controls;

import adapter.base.ControlAdapter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class MfEmailField extends ControlAdapter<String> {

	private Text _errorLabel;

	private Label _infoLbl = new Label();
	private BorderPane _wrapper = new BorderPane();

	private boolean _useWrapper = false;

	private TextField _control;

	public MfEmailField() {
		setControl(_control = new TextField());
	}

	public MfEmailField(TextField tf) {
		setControl(_control = tf);
	}

	@Override
	protected void initialize() {
		super.initialize();
		_infoLbl.setId("error-label");
		_infoLbl.setText("Only Numbers Please");

		if (_useWrapper) {
			_wrapper.setCenter(_control);
		}

		_control.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, 
					Boolean oldValue, Boolean newValue) {
				String validEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
				if (oldValue) {
					String value = getValue();
					if (!value.matches(validEmail)) {
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
							_field.set(_entity, value);
						} catch (IllegalArgumentException | IllegalAccessException e) {
							e.printStackTrace();
						}
						runEvents(newValue);
					}
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

	public void setUseWrapper(boolean useWrapper) {
		_useWrapper = useWrapper;
	}

	public void setErrorLabel(Text text) {
		_errorLabel = text;
	}
}
