package controls;

import java.util.ArrayList;
import java.util.List;

import adapter.base.ControlAdapter;
import javafx.scene.control.ProgressBar;

public class MfProgressBar extends ControlAdapter<Object> {

	private ProgressBar _control;
	
	private List<String> _values = new ArrayList<String>();

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
	public void setValue(Object value) throws Exception {
		if (value == null) {
			_control.setProgress(0d);
		}
		else {
			Double val = (value instanceof String) ? ((_values.indexOf(value) + 1) / _values.size()) : (Double) value;
			super.setValue(val);
			_control.setProgress(val);
		}
	}
	
	public void setValues(List<String> values) {
		_values = values;
	}
}
