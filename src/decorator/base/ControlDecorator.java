package decorator.base;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import db.interfaces.IEntity;
import javafx.event.ActionEvent;
import javafx.scene.control.Control;

@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class ControlDecorator<TType> {

	public interface ControlEvent<T> {
		void handle(T value);
	}

	private Control _control;

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
	
	protected void runEvents(ActionEvent event) {
		for (ControlEvent<ActionEvent> controlEvent: _events) {
			controlEvent.handle(event);
		}
	}
	
	public abstract void clear() throws Exception;

	public abstract TType getValue();	

	public abstract void setValue(TType value) throws Exception;

	public void setControl(Control control) {
		_control = control;
		initialize();
	}

	public void setEntity(IEntity entity) throws Exception {
		_entity = entity;
	}

	public void setField(Field field) {
		_field = field;
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

	public Control getInstance() {
		return _control;
	}

	public void setStyle(String style) {
		_control.setStyle(style);
	}

	public void addEvent(ControlEvent event) {
		_events.add(event);
	}
}
