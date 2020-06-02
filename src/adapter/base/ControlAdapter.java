package adapter.base;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import db.interfaces.IEntity;
import javafx.scene.Node;

@SuppressWarnings({ "rawtypes", "unchecked", "hiding" })
public abstract class ControlAdapter<TType> {

	public interface ControlEvent<T> {
		void handle(T value);
	}

	private Node _control;

	protected IEntity _entity;

	protected Field _field;

	protected String _columnName;

	protected List<ControlEvent> _events = new ArrayList<ControlEvent>();

	protected void initialize() {}

	protected void runEvents(String newValue) {
		for (ControlEvent<String> controlEvent: _events) {
			controlEvent.handle(newValue);
		}
	}

	protected <TType> void runEvents(TType newValue) {
		for (ControlEvent<TType> controlEvent: _events) {
			controlEvent.handle(newValue);
		}
	}

	public void update() throws Exception {
		if (_field != null && _entity != null) {
			setValue((TType) _field.get(_entity));
		}
	}

	public abstract void clear() throws Exception;

	public abstract TType getValue();	

	public void setValue(TType value) throws Exception {
		if (_field != null && _entity != null) {
			_field.set(_entity, value);
		}
	}

	public void setControl(Node control) {
		_control = control;
		initialize();
	}

	public void setEntity(IEntity entity) throws Exception {
		_entity = entity;
		update();
	}

	public void setField(Field field) {
		_field = field;
		_field.setAccessible(true);
	}

	public Field getField() {
		return _field;
	}

	public void setColumnName(String name) {
		_columnName = name;
	}

	public String getColumnName() {
		return _columnName;
	}

	public Node getInstance() {
		return _control;
	}

	public void setStyle(String style) {
		_control.setStyle(style);
	}

	public void addEvent(ControlEvent event) {
		_events.add(event);
	}
}
