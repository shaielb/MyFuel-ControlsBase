package controls;

import java.time.Clock;
import java.time.LocalDate;

import db.interfaces.IEntity;
import decorator.base.ControlDecorator;
import javafx.scene.control.DatePicker;

public class MfDatePicker extends ControlDecorator<LocalDate> {

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
				_field.set(_entity, _field.getType().cast(getValue()));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			runEvents(event);
		});
	}
	
	@Override
	public void setEntity(IEntity entity) throws Exception {
		super.setEntity(entity);
		_control.setValue((LocalDate) _field.get(entity));
	}
	
	@Override
	public void clear() throws Exception {
		_field.set(_entity, LocalDate.now(Clock.systemDefaultZone()));
	}

	@Override
	public LocalDate getValue() {
		return _control.getValue();
	}

	@Override
	public void setValue(LocalDate value) {
		_control.setValue(value);
	}
}
