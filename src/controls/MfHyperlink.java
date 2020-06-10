package controls;

import adapter.base.ControlAdapter;
import javafx.scene.control.Hyperlink;

public class MfHyperlink extends ControlAdapter<String> {

	private Hyperlink _control;

	public MfHyperlink(String title) {
		setControl(_control = new Hyperlink(title));
	}

	public MfHyperlink(Hyperlink hl) {
		setControl(_control = hl);
	}

	@Override
	protected void initialize() {
		super.initialize();
		_control.setOnAction((event) -> {
			runEvents(event);
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
