package table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import adapter.base.ControlAdapter;
import db.interfaces.IEntity;
import handler.UiHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class MfTable<TEntity extends IEntity> extends BorderPane {

	private final TableView<TEntity> _table = new TableView<>();
	protected final ObservableList<TEntity> _tvObservableList = FXCollections.observableArrayList();

	private Class<TEntity> _entityClass;

	private ColumnEvent<TEntity> _onCellControlAction;

	private Set<ControlAdapter> _controls = new HashSet<ControlAdapter>();

	public MfTable(Class<TEntity> entityClass) throws Exception {
		_entityClass = entityClass;
		initialize(entityClass);
	}

	public void initialize(Class<TEntity> entityClass) throws Exception {
		setAppearance();
		_table.setItems(_tvObservableList);

		Map<String, ControlAdapter> map = UiHandler.createEntityControls(_entityClass);

		List<TableColumn<TEntity, ?>> columns = new ArrayList<TableColumn<TEntity, ?>>();
		for (Entry<String, ControlAdapter> entry : map.entrySet()) {
			ControlAdapter control = entry.getValue();
			columns.add(UiHandler.createColumn(control, (entity, eventControl) -> {
				_controls.add(control);
				control.setEntity(entity);
				_onCellControlAction.execute((TEntity) entity, eventControl);
			}));
		}
		placeControls(columns.toArray(new TableColumn[columns.size()]));
	}

	private void setAppearance() {
		setPadding(new Insets(10, 10, 10, 10));

		_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		_table.setPrefWidth(600);
		_table.setPrefHeight(600);
		_table.setEditable(true);
	}

	private void placeControls(TableColumn<TEntity, ?>[] columns) throws Exception {
		_table.getColumns().addAll(Arrays.asList(columns));

		ScrollPane sp = new ScrollPane(_table);
		sp.setFitToWidth(true);
		sp.setFitToHeight(true);

		setCenter(sp);
	}

	public void addColumn(ControlAdapter control, ColumnEvent<TEntity> event) {
		addColumn(UiHandler.createColumn(control, event));
	}

	public void addColumn(TableColumn<TEntity, ?> column) {
		_table.getColumns().add(column);
	}

	public ObservableList<TEntity> getObservableList() {
		return _tvObservableList;
	}
	
	public void setRows(Collection<TEntity> rows) {
		_tvObservableList.clear();
		for (TEntity entity: rows) {
			_tvObservableList.add(entity);
		}
	}

	public void setOnCellControlAction(ColumnEvent<TEntity> onCellControlAction) {
		_onCellControlAction = onCellControlAction;
	}

	public Class<TEntity> getEntityClass() {
		return _entityClass;
	}

	public void setEditable(Boolean editable) {
		_table.setEditable(editable);
	}
}
