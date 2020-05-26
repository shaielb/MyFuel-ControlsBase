package controls;

import decorator.base.ControlDecorator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class MfEmailField extends ControlDecorator<String> {

	private Label _infoLbl = new Label();
	private BorderPane _wrapper = new BorderPane();
	
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
		
		_control.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, 
		    		String oldValue, String newValue) {
		    	String validEmail = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
		        if (!newValue.matches(validEmail)) {
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
		        }
		        runEvents(newValue);
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
