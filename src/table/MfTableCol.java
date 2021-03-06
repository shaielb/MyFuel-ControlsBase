package table;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import adapter.base.ControlAdapter;
import db.interfaces.IEntity;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class MfTableCol<TEntity extends IEntity, TType> extends TableColumn<TEntity, TType> {

	private Map<Node, ControlAdapter> _controlsMap = new HashMap<Node, ControlAdapter>();

	public interface GetIndex {
		Integer getIndex();
	}

	public interface ControlInstanciator {
		ControlAdapter instanciate(GetIndex getIndex) throws Exception;
	}

	private ControlInstanciator _ci;

	public MfTableCol(String title) {
		super(title);

		Callback<TableColumn<TEntity, TType>, TableCell<TEntity, TType>> cellFactory = new Callback<TableColumn<TEntity, TType>, TableCell<TEntity, TType>>() {
			@Override
			public TableCell<TEntity, TType> call(final TableColumn<TEntity, TType> param) {
				TableCell<TEntity, TType> cell = null;
				try {
					cell = new TableCell<TEntity, TType>() {

						private final ControlAdapter _control = _ci.instanciate(() -> getIndex());

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
				} catch (Exception e) {
					e.printStackTrace();
				}

				return cell;
			}
		};

		setCellFactory(cellFactory);
	}

	protected void updateCellItem(ControlAdapter control, int index) {
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

	public void setCi(ControlInstanciator ci) {
		_ci = ci;
	}

	public ControlAdapter getAdapter(Control control) {
		return _controlsMap.get(control);
	}
}
