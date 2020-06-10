package controls;

import adapter.base.ControlAdapter;
import javafx.scene.control.MenuButton;

public class MfMenuButton extends ControlAdapter<String> {

	private MenuButton _control;

	public MfMenuButton(String title) {
		setControl(_control = new MenuButton(title));
	}

	public MfMenuButton(MenuButton batton) {
		setControl(_control = batton);
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
