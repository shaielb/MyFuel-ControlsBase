package controls;

import adapter.base.ControlAdapter;
import javafx.scene.control.Label;

public class MfLabel extends ControlAdapter<Object> {

	private Label _control;

	public MfLabel() {
		setControl(_control = new Label());
	}

	public MfLabel(Label lbl) {
		setControl(_control = lbl);
	}

	@Override
	protected void initialize() {
		super.initialize();
	}

	@Override
	public void clear() throws Exception {
		_control.setText("");
	}

	@Override
	public Object getValue() {
		return _control.getText();
	}

	@Override
	public void setValue(Object value) throws Exception {
		super.setValue(value);
		_control.setText(value.toString());
	}
}
