package controls;

import java.util.List;

import adapter.base.MfListControl;
import db.interfaces.IEntity;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ListView;

public class MfListView extends MfListControl<Object> {

	private ListView<String> _control;

	public MfListView() {
		setControl(_control = new ListView<String>());
	}

	public MfListView(ListView<String> lv) {
		setControl(_control = lv);
	}

	@Override
	protected void initialize() {
		super.initialize();
		_control.getSelectionModel().selectedItemProperty().addListener(
				new ChangeListener<String>() {
					public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
						try {
							_field.set(_entity, _entitiesValuesMap.get(getValue()));
							runEvents(getValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						runEvents(newValue);
					}
				});
	}

	private void setSelectedItem(IEntity entity) throws Exception {
		_control.getSelectionModel().select((entity == null) ? "" : _entitiesIdsValuesMap.get(entity.getId()));
		_field.set(_entity, entity);
	}

	@Override
	public void clear() throws Exception {
		_control.getSelectionModel().select(NoSelection);
		_field.set(_entity, null);
	}

	@Override
	public Object getValue() {
		return _control.getSelectionModel().getSelectedItem();
	}

	@Override
	public void setValue(Object value) throws Exception {
		if (value instanceof String) {
			String val = (String) value;
			_control.getSelectionModel().select(val);
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
