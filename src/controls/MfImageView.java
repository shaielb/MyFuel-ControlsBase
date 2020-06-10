package controls;

import adapter.base.ControlAdapter;
import javafx.scene.image.ImageView;

public class MfImageView extends ControlAdapter<String> {

	private ImageView _control;

	public MfImageView(String title) {
		setControl(_control = new ImageView(title));
	}

	public MfImageView(ImageView imageView) {
		setControl(_control = imageView);
	}

	@Override
	protected void initialize() {
		super.initialize();
		_control.setOnMouseClicked((event) -> {
			runEvents(event);
		});
	}

	@Override
	public void clear() {}

	@Override
	public String getValue() {
		return _control.getId();
	}

	@Override
	public void setValue(String value) {
		_control.setId(value);
	}
}
