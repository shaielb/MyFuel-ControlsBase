package controls;

import adapter.base.ControlAdapter;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

@SuppressWarnings({ "unchecked" })
public class MfButton extends ControlAdapter<String> {

	private Button _control;

	public MfButton(String title) {
		setControl(_control = new Button(title));
	}
	
	public MfButton(Button batton) {
		setControl(_control = batton);
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		_control.setOnAction((event) -> {
			for (ControlEvent<ActionEvent> controlEvent: _events) {
				controlEvent.handle(event);
			}
		});
	}
	
	@Override
	public void clear() {}

	@Override
	public String getValue() {
		return _control.getText();
	}

	@Override
	public void setValue(String value) {
		_control.setText(value);
	}
}
