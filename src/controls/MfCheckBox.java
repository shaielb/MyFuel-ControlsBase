package controls;

import db.interfaces.IEntity;
import decorator.base.ControlDecorator;
import javafx.scene.control.CheckBox;

public class MfCheckBox extends ControlDecorator<Boolean> {

	private CheckBox _control;

	public MfCheckBox() {
		setControl(_control = new CheckBox());
	}
	
	public MfCheckBox(CheckBox cb) {
		setControl(_control = cb);
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
		_control.setSelected((Boolean) _field.get(entity));
	}
	
	@Override
	public void clear() throws Exception {
		_field.set(_entity, false);
	}

	@Override
	public Boolean getValue() {
		return _control.isSelected();
	}

	@Override
	public void setValue(Boolean value) {
		_control.setSelected(value);
	}
}
