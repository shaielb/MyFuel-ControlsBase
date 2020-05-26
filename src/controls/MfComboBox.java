package controls;

import java.util.List;

import db.interfaces.IEntity;
import decorator.base.MfListControl;
import javafx.scene.control.ComboBox;

public class MfComboBox extends MfListControl<Object> {
	
	private ComboBox<String> _control;

	public MfComboBox() {
		setControl(_control = new ComboBox<String>());
	}
	
	public MfComboBox(ComboBox<String> cmb) {
		setControl(_control = cmb);
	}

	@Override
	protected void initialize() {
		super.initialize();
		_control.setOnAction((event) -> {
					try {
						_field.set(_entity, _entitiesValuesMap.get(getValue()));
						runEvents(getValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
					runEvents(event);
				});
	}
	
	private void setSelectedItem(IEntity entity) throws Exception {
		_control.setValue((entity == null) ? "" : _entitiesIdsValuesMap.get(entity.getId()));
		_field.set(_entity, entity);
	}
	
	@Override
	public void clear() throws Exception {
		_control.setValue(NoSelection);
		_field.set(_entity, null);
	}

	@Override
	public Object getValue() {
		return _control.getValue();
	}

	@Override
	public void setValue(Object value) throws Exception {
		if (value instanceof String) {
			String val = (String) value;
			_control.setValue(val);
			_field.set(_entity, _entitiesValuesMap.get(val));
		}
		else if (value instanceof IEntity) {
			IEntity entity = (IEntity) value;
			setSelectedItem(entity);
		}
	}

	@Override
	public void setValues(List<String> values) {
		_control.getItems().addAll(values);
	}
}
