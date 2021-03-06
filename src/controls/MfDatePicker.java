package controls;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.LocalDate;

import adapter.base.ControlAdapter;
import javafx.scene.control.DatePicker;

public class MfDatePicker extends ControlAdapter<Timestamp> {

	private DatePicker _control;

	public MfDatePicker() {
		setControl(_control = new DatePicker());
	}
	
	public MfDatePicker(DatePicker dp) {
		setControl(_control = dp);
	}

	@Override
	protected void initialize() {
		super.initialize();
		_control.setOnAction((event) -> {
			try {
				Timestamp date = getValue();
				_field.set(_entity, date);
				runEvents(date);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			runEvents(event);
		});
	}
	
	@Override
	public void clear() throws Exception {
		_field.set(_entity, LocalDate.now(Clock.systemDefaultZone()));
	}

	@Override
	public Timestamp getValue() {
		return Timestamp.valueOf(_control.getValue().atStartOfDay());
	}

	@Override
	public void setValue(Timestamp value) throws Exception {
		super.setValue(value);
		_control.setValue(value.toLocalDateTime().toLocalDate());
	}
}
