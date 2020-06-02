package table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import adapter.base.ControlAdapter;
import db.interfaces.IEntity;
import handler.ControlsHandler;
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

	public MfTable(Class<TEntity> entityClass) throws Exception {
		_entityClass = entityClass;
		initialize(entityClass);
	}

	public void initialize(Class<TEntity> entityClass) throws Exception {
		setAppearance();
		_table.setItems(_tvObservableList);

		Map<String, ControlAdapter> map = ControlsHandler.createEntityControls(_entityClass);

		List<TableColumn<TEntity, ?>> columns = new ArrayList<TableColumn<TEntity, ?>>();
		for (Entry<String, ControlAdapter> entry : map.entrySet()) {
			ControlAdapter control = entry.getValue();
			columns.add(ControlsHandler.createColumn(control, (entity, eventControl) -> {
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
		addColumn(ControlsHandler.createColumn(control, event));
	}

	public void addColumn(TableColumn<TEntity, ?> column) {
		_table.getColumns().add(column);
	}

	public ObservableList<TEntity> getObservableList() {
		return _tvObservableList;
	}

	public void setOnCellControlAction(ColumnEvent<TEntity> onCellControlAction) {
		_onCellControlAction = onCellControlAction;
	}

	public Class<TEntity> getEntityClass() {
		return _entityClass;
	}
}
