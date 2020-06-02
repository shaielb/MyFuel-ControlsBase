package controls;

import adapter.base.ControlAdapter;
import javafx.scene.text.Text;

public class MfText extends ControlAdapter<String> {

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
	public String getValue() {
		return _control.getText();
	}

	@Override
	public void setValue(String value) throws Exception {
		_control.setText(value);
	}
}
