package adapter.base;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import db.interfaces.IEntity;
import javafx.scene.Node;

/**
 * @author shaielb
 *
 * @param <TType>
 */
@SuppressWarnings({ "rawtypes", "unchecked", "hiding" })
public abstract class ControlAdapter<TType> {

	/**
	 * @author shaielb
	 *
	 * @param <T>
	 */
	public interface ControlEvent<T> {
		void handle(T value);
	}

	/**
	 * 
	 */
	private Node _control;

	/**
	 * 
	 */
	protected IEntity _entity;

	/**
	 * 
	 */
	protected Field _field;

	/**
	 * 
	 */
	protected String _columnName;

	/**
	 * 
	 */
	protected List<ControlEvent> _events = new ArrayList<ControlEvent>();

	/**
	 * 
	 */
	protected void initialize() {}

	/**
	 * @param newValue
	 */
	protected void runEvents(String newValue) {
		for (ControlEvent<String> controlEvent: _events) {
			controlEvent.handle(newValue);
		}
	}

	/**
	 * @param <TType>
	 * @param newValue
	 */
	protected <TType> void runEvents(TType newValue) {
		for (ControlEvent<TType> controlEvent: _events) {
			controlEvent.handle(newValue);
		}
	}

	/**
	 * @throws Exception
	 */
	public void update() throws Exception {
		if (_field != null && _entity != null) {
			setValue((TType) _field.get(_entity));
		}
	}

	/**
	 * @throws Exception
	 */
	public abstract void clear() throws Exception;

	/**
	 * @return
	 */
	public abstract TType getValue();	

	/**
	 * @param value
	 * @throws Exception
	 */
	public void setValue(TType value) throws Exception {
		if (_field != null && _entity != null) {
			_field.set(_entity, value);
		}
	}

	/**
	 * @param control
	 */
	public void setControl(Node control) {
		_control = control;
		initialize();
	}

	/**
	 * @param entity
	 * @throws Exception
	 */
	public void setEntity(IEntity entity) throws Exception {
		_entity = entity;
		update();
	}

	/**
	 * @param field
	 */
	public void setField(Field field) {
		_field = field;
		_field.setAccessible(true);
	}

	/**
	 * @return
	 */
	public Field getField() {
		return _field;
	}

	/**
	 * @param name
	 */
	public void setColumnName(String name) {
		_columnName = name;
	}

	/**
	 * @return
	 */
	public String getColumnName() {
		return _columnName;
	}

	/**
	 * @return
	 */
	public Node getInstance() {
		return _control;
	}

	/**
	 * @param style
	 */
	public void setStyle(String style) {
		_control.setStyle(style);
	}

	/**
	 * @param event
	 */
	public void addEvent(ControlEvent event) {
		_events.add(event);
	}
}
