package table;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import db.interfaces.IEntity;
import decorator.base.ControlDecorator;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class MfTableCol<TEntity extends IEntity, TType> extends TableColumn<TEntity, TType> {

	private Map<Control, ControlDecorator> _controlsMap = new HashMap<Control, ControlDecorator>();

	public interface GetIndex {
		Integer getIndex();
	}

	public interface ControlInstanciator {
		ControlDecorator instanciate(GetIndex getIndex);
	}

	private ColumnEvent<TEntity> _ce;

	private ControlInstanciator _ci;

	public MfTableCol(String title, ColumnEvent<TEntity> ce) {
		this(title, ce, null);
	}

	public MfTableCol(String title, ColumnEvent<TEntity> ce, ControlInstanciator ci) {
		super(title);
		_ce = ce;
		_ci = ci;

		Callback<TableColumn<TEntity, TType>, TableCell<TEntity, TType>> cellFactory = new Callback<TableColumn<TEntity, TType>, TableCell<TEntity, TType>>() {
			@Override
			public TableCell<TEntity, TType> call(final TableColumn<TEntity, TType> param) {
				final TableCell<TEntity, TType> cell = new TableCell<TEntity, TType>() {

					private final ControlDecorator _control = _ci.instanciate(() -> getIndex());

					@Override
					public void updateItem(TType item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
						} else {
							_controlsMap.put(_control.getInstance(), _control);
							setGraphic(_control.getInstance());
							updateCellItem(_control, getIndex());
						}
					}
				};

				return cell;
			}
		};

		setCellFactory(cellFactory);
	}

	protected void updateCellItem(ControlDecorator control, int index) {
		try {
			Field field = control.getField();
			TEntity entity = getTableView().getItems().get(index);
			control.setEntity(entity);
			if (entity != null && field != null) {
				Object value = field.get(entity);
				if (value != null) {
					control.setValue(value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public <TEvent extends Event> EventHandler<TEvent> getEventHandler(GetIndex getIndex) {
		return (event) -> {
			ControlDecorator control = (ControlDecorator) _controlsMap.get(event.getSource());
			Object newValue = control.getValue();
			TEntity entity = getTableView().getItems().get(getIndex.getIndex());
			try {
				control.getField().set(entity, newValue);
				_ce.execute(entity, control);
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	}

	public void setCi(ControlInstanciator ci) {
		_ci = ci;
	}
	
	public ControlDecorator getDecorator(Control control) {
		return _controlsMap.get(control);
	}
}