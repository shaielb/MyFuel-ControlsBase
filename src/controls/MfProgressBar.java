package controls;

import decorator.base.ControlDecorator;
import javafx.scene.control.ProgressBar;

public class MfProgressBar extends ControlDecorator<Double> {

	private ProgressBar _control;

	public MfProgressBar() {
		setControl(_control = new ProgressBar());
	}

	public MfProgressBar(ProgressBar pb) {
		setControl(_control = pb);
	}

	@Override
	protected void initialize() {
		super.initialize();
	}

	@Override
	public void clear() throws Exception {
		_control.setProgress(0);
	}

	@Override
	public Double getValue() {
		return _control.getProgress();
	}

	@Override
	public void setValue(Double value) throws Exception {
		super.setValue(value);
		_control.setProgress(value);
	}
}
