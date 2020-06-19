package handler;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import adapter.base.ControlAdapter;

import java.util.TreeMap;

import annotations.Column;
import annotations.Table;
import controls.MfButton;
import controls.MfCheckBox;
import controls.MfComboBox;
import controls.MfDatePicker;
import controls.MfNumberField;
import controls.MfTextField;
import controls.MfTimeField;
import db.interfaces.IEntity;
import db.services.Services;
import globals.Globals;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Control;
import javafx.scene.control.TableColumn;
import table.ColumnEvent;
import table.MfTableCol;
import utilities.Cache;
import utilities.StringUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class UiHandler {

	public interface RunOnUi {
		public void run();
	}
	
	public interface ButtonTitle {
		public String set(IEntity entity);
	}
	
	public interface ControlInstantiator {
		public ControlAdapter create() throws Exception;
	}

	public interface FieldsIterator {
		public void iterate(Field field, String columnName, Integer columnIndex) throws Exception;
	}
	
	public static <TEntity extends IEntity> TableColumn createColumn(ControlAdapter control, ColumnEvent<TEntity> ce) {
		Field field = control.getField();
		return createColumn(control, () -> { return UiHandler.createControl(field, true); }, ce);
	}

	public static <TEntity extends IEntity> TableColumn createColumn(ControlAdapter control, ControlInstantiator instantiator, ColumnEvent<TEntity> ce) {
		String title = StringUtil.getTitle(control.getColumnName());

		MfTableCol<TEntity, ?> column = new MfTableCol(title);
		column.setCi((getIndex) -> {
			{
				ControlAdapter cd = instantiator.create();
				cd.addEvent((event) -> {
					Object newValue = cd.getValue();
					TEntity entity = column.getTableView().getItems().get(getIndex.getIndex());
					try {
						if (cd.getField() != null) {
							cd.getField().set(entity, newValue);
						}
						ce.execute(entity, cd);
					} catch (Exception e) {
						e.printStackTrace();
					}

				});
				return cd;
			}
		});
		return column;
	}

	public static <TEntity extends IEntity> TableColumn createButtonColumn(String buttonTitle, String title, ColumnEvent<TEntity> ce) {
		MfTableCol<TEntity, ?> column = new MfTableCol<TEntity, String>(title);
		column.setCi((getIndex) -> {
			MfButton button = new MfButton(buttonTitle);
			{
				button.addEvent((event) -> {
					TEntity entity = column.getTableView().getItems().get(getIndex.getIndex());
					try {
						ce.execute(entity, button);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			}
			return button;
		});
		return column;
	}
	
	public static <TEntity extends IEntity> ControlAdapter createControl(Field field) throws Exception {
		return createControl(field, false);
	}

	public static <TEntity extends IEntity> ControlAdapter createControl(Field field, boolean useWrapper) throws Exception {
		Class<?> classType = field.getType();
		String colName = field.getAnnotation(Column.class).Name();
		ControlAdapter control = null;
		if (Boolean.class.isAssignableFrom(classType)) {
			control = new MfCheckBox();
		}
		else if (Number.class.isAssignableFrom(classType)) {
			control = new MfNumberField(classType, useWrapper);
		}
		else if (String.class.isAssignableFrom(classType)) {
			control = new MfTextField();
		}
		else if (Date.class.isAssignableFrom(classType)) {
			control = new MfDatePicker();
		}
		else if (Timestamp.class.isAssignableFrom(classType)) {
			control = new MfDatePicker();
		}
		else if (Time.class.isAssignableFrom(classType)) {
			control = new MfTimeField();
		}
		else if (colName.endsWith("_enum_fk")) {
			control = new MfComboBox();
			String fkTableName = field.getAnnotation(Table.class).Name();
			Map<String, List<IEntity>> enumTablesMap = (Map<String, List<IEntity>>) Cache.get(Globals.EnumTables);
			if (enumTablesMap == null) {
				Cache.put(Globals.EnumTables, enumTablesMap = new HashMap<String, List<IEntity>>());
			}
			List<IEntity> tableEntities = enumTablesMap.get(fkTableName);
			if (tableEntities == null) {
				throw new Exception(String.format("%s was not populated from server", fkTableName));
			}
			((MfComboBox) control).setEntities(tableEntities, "_key");
		}
		if (control != null) {
			control.setField(field);
		}
		return control;
	}

	/*private static <TEntity extends IEntity> int getEntityFieldsNumber(Class<TEntity> entityClass) throws Exception {
		int[] columnsNum = new int[] {0};
		iterateFields(entityClass, (field, colName, index) -> {
			columnsNum[0]++;
		});
		return columnsNum[0];
	}*/

	public static <TEntity extends IEntity> Map<String, ControlAdapter> createEntityControls(Class<TEntity> entityClass) throws Exception {
		Map<String, ControlAdapter> map = new LinkedHashMap<String, ControlAdapter>();
		Map<Integer, ControlAdapter> controlsMap = new TreeMap<Integer, ControlAdapter>();

		iterateFields(entityClass, (field, colName, index) -> {
			ControlAdapter control = UiHandler.createControl(field);
			controlsMap.put(index, control);
			control.setColumnName(colName);
		});
		for (Entry<Integer, ControlAdapter> entry: controlsMap.entrySet()) {
			ControlAdapter cd = entry.getValue();
			map.put(cd.getColumnName(), cd);
		}
		return map;
	}

	public static Map<String, Control> collectControlsByIds(Scene scene, String table) throws Exception {
		IEntity entity = Services.getBridge(table).createEntity();
		Map<String, Control> map = new HashMap<String, Control>();
		UiHandler.iterateFields(entity.getClass(), (field, colName, index) -> {
			Node node = scene.lookup("#" + colName);
			if (node != null) {
				map.put(colName, (Control) node);
			}
		});
		return map;
	}

	public static void iterateFields(Class<?> entityClass, FieldsIterator iterator) throws Exception {
		Field[] fields = entityClass.getDeclaredFields();

		for (Field field : fields) {
			Column columnAnnotation = field.getAnnotation(Column.class);
			if (columnAnnotation != null) {
				field.setAccessible(true);
				String colName = columnAnnotation.Name();
				if (isFieldValid(colName)) {
					Integer index = columnAnnotation.Index() - 1; // with no id
					iterator.iterate(field, colName, index);
				}
			}
		}
	}
	
	public static void clone(IEntity from, IEntity to) {
		try {
			iterateFields(from.getClass(), (field, colName, index) -> {
				field.set(to, field.get(from));
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean showAlert(AlertType at, String title, String header, String body) {
		Boolean[] result = new Boolean[] { true };
		UiHandler.RunUi(() -> {
			Alert alert = new Alert(at);
			alert.setTitle(title);
			alert.setHeaderText(header);
			alert.setContentText(body);

			Optional option = alert.showAndWait();
			if (option == null) {
				result[0] = false;
			}
		});
		return result[0];
	}
	
	public static void RunUi(RunOnUi runOnUi) {
		Platform.runLater(new Runnable() {
			@Override public void run() {
				runOnUi.run();
			}
		});
	}

	public static boolean isFieldValid(String colName) {
		return (!"id".equals(colName) && (!colName.endsWith("_fk") || colName.endsWith("enum_fk")));
	}
}
