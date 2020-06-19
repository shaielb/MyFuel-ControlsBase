package controls;

import adapter.base.ControlAdapter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MfImageView extends ControlAdapter<String> {

	private Image _plain;
	private Image _onMouseOver;
	private Image _onMouseClick;
	
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
	
	public void setMouseImages(String plain, String onMouseOver, String onMouseClick) {
		@SuppressWarnings("deprecation")
		String url = _control.getImage().impl_getUrl();
		String path = url.substring(0, url.lastIndexOf("/") + 1);
		String plainName = plain.substring(plain.lastIndexOf("/") + 1);
		String onMouseOverName = onMouseOver.substring(onMouseOver.lastIndexOf("/") + 1);
		String onMouseClickName = onMouseClick.substring(onMouseClick.lastIndexOf("/") + 1);
		_plain = new Image(path + plainName);
		_onMouseOver = new Image(path + onMouseOverName);
		_onMouseClick = new Image(path + onMouseClickName);
		_control.setOnMousePressed((event) -> {
			_control.setImage(_onMouseClick);
		});
		_control.setOnMouseReleased((event) -> {
			_control.setImage(_plain);
		});
		_control.setOnMouseEntered((event) -> {
			_control.setImage(_onMouseOver);
		});
		_control.setOnMouseExited((event) -> {
			_control.setImage(_plain);
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
