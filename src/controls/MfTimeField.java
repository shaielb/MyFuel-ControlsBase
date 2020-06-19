package controls;

import adapter.base.ControlAdapter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class MfTimeField extends ControlAdapter<String> {

	private Label _infoLbl = new Label();
	private BorderPane _wrapper = new BorderPane();
	
	private TextField _control;

	public MfTimeField() {
		setControl(_control = new TextField());
	}

	public MfTimeField(TextField tf) {
		setControl(_control = tf);
	}

	@Override
	protected void initialize() {
		super.initialize();
		_infoLbl.setId("error-label");
		_infoLbl.setText("Only Time Please (00:00:00)");
		
		_control.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, 
		    		String oldValue, String newValue) {
		    	String validTime = "^[12]\\d\\:[0-5]\\d\\:[0-5]\\d";
		    	if (newValue != null) {
			        if (!newValue.matches(validTime)) {
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
		_field.set(_entity, null);
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
