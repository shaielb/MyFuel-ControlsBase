package controls;

import adapter.base.ControlAdapter;
import javafx.event.ActionEvent;
import javafx.scene.control.ToggleButton;

@SuppressWarnings({ "unchecked" })
public class MfToggleButton extends ControlAdapter<Boolean> {

	private ToggleButton _control;

	public MfToggleButton(String title) {
		setControl(_control = new ToggleButton(title));
	}

	public MfToggleButton(ToggleButton batton) {
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

	public Boolean getValue() {
		return _control.isSelected();
	}
	
	@Override
	public void setValue(Boolean value) {
		_control.setSelected(value);
	}

	public void setValue(String value) {
		_control.setText(value);
	}
}
