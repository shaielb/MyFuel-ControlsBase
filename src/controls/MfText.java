package controls;

import adapter.base.ControlAdapter;
import javafx.scene.text.Text;

public class MfText extends ControlAdapter<Object> {

	private Text _control;

	public MfText() {
		setControl(_control = new Text());
	}

	public MfText(Text text) {
		setControl(_control = text);
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
		if (value != null) {
			super.setValue(value);
			_control.setText(value.toString());
		}
	}
}
