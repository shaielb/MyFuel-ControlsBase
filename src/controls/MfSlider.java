package controls;

import adapter.base.ControlAdapter;
import javafx.scene.control.Slider;

public class MfSlider extends ControlAdapter<Double> {

	private Slider _control;

	public MfSlider() {
		setControl(_control = new Slider());
	}

	public MfSlider(Slider slider) {
		setControl(_control = slider);
	}

	@Override
	protected void initialize() {
		super.initialize();
		_control.valueChangingProperty().addListener((obs, oldVal, newVal) -> {
			if (!newVal) {
				try {
					Double nValue = getValue();
					_field.set(_entity, nValue);
					runEvents(nValue);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void clear() throws Exception {
		_field.set(_entity, 0);
	}

	@Override
	public Double getValue() {
		return _control.getValue();
	}

	@Override
	public void setValue(Double value) throws Exception {
		super.setValue(value);
		_control.setValue(value);
	}
}
