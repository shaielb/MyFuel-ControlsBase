package handler;

import java.lang.reflect.Field;
import java.sql.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import annotations.Column;
import annotations.Table;
import controls.MfButton;
import controls.MfCheckBox;
import controls.MfComboBox;
import controls.MfDatePicker;
import controls.MfNumberField;
import controls.MfTextField;
import db.interfaces.IEntity;
import decorator.base.ControlDecorator;
import globals.Globals;
import javafx.scene.control.TableColumn;
import table.ColumnEvent;
import table.MfTableCol;
import utilities.Cache;
import utilities.StringUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ControlsHandler {

	public interface ButtonTitle {
		public String set(IEntity entity);
	}

	public interface FieldsIterator {
		public void iterate(Field field, String columnName, Integer columnIndex) throws Exception;
	}

	public static <TEntity extends IEntity> TableColumn createColumn(ControlDecorator control, ColumnEvent<TEntity> ce) {
		Field field = control.getField();
		String title = StringUtil.getTitle(control.getColumnName());

		MfTableCol<TEntity, ?> column = new MfTableCol(title);
		column.setCi((getIndex) -> {
			{
				ControlDecorator cd = ControlsHandler.createControl(field);
				cd.addEvent((event) -> {
					Object newValue = cd.getValue();
					TEntity entity = column.getTableView().getItems().get(getIndex.getIndex());
					try {
						control.getField().set(entity, newValue);
						ce.execute(entity, control);
					} catch (Exception e) {
						e.printStackTrace();
					}

				});
				return cd;
			}
		});
		return column;
	}

	public static <TEntity extends IEntity> TableColumn createButtonColumn(ButtonTitle bt, String title, ColumnEvent<TEntity> ce) {
		MfTableCol<TEntity, ?> column = new MfTableCol<TEntity, String>(title);
		column.setCi((getIndex) -> {
			String btnTitle = title;
			if (bt != null) {
				TEntity entity = column.getTableView().getItems().get(getIndex.getIndex());
				btnTitle = bt.set(entity);
			}
			MfButton button = new MfButton(btnTitle);
			{
				button.addEvent((event) -> {
					Object newValue = button.getValue();
					TEntity entity = column.getTableView().getItems().get(getIndex.getIndex());
					try {
						button.getField().set(entity, newValue);
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

	public static <TEntity extends IEntity> TableColumn createButtonColumn(String title, ColumnEvent<TEntity> ce) {
		return createButtonColumn(null, title, ce);
	}

	public static <TEntity extends IEntity> ControlDecorator createControl(Field field) throws Exception {
		Class<?> classType = field.getType();
		String colName = field.getAnnotation(Column.class).Name();
		ControlDecorator control = null;
		if (Boolean.class.isAssignableFrom(classType)) {
			control = new MfCheckBox();
		}
		else if (Number.class.isAssignableFrom(classType)) {
			control = new MfNumberField(classType);
		}
		else if (String.class.isAssignableFrom(classType)) {
			control = new MfTextField();
		}
		else if (Date.class.isAssignableFrom(classType)) {
			control = new MfDatePicker();
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
			((MfComboBox) control).setEntities(tableEntities, "title");
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

	public static <TEntity extends IEntity> Map<String, ControlDecorator> createEntityControls(Class<TEntity> entityClass) throws Exception {
		Map<String, ControlDecorator> map = new LinkedHashMap<String, ControlDecorator>();
		Map<Integer, ControlDecorator> controlsMap = new TreeMap<Integer, ControlDecorator>();

		iterateFields(entityClass, (field, colName, index) -> {
			ControlDecorator control = ControlsHandler.createControl(field);
			controlsMap.put(index, control);
			control.setColumnName(colName);
		});
		for (Entry<Integer, ControlDecorator> entry: controlsMap.entrySet()) {
			ControlDecorator cd = entry.getValue();
			map.put(cd.getColumnName(), cd);
		}
		return map;
	}

	public static <TEntity extends IEntity> void iterateFields(Class<TEntity> entityClass, FieldsIterator iterator) throws Exception {
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

	public static boolean isFieldValid(String colName) {
		return (!"id".equals(colName) && (!colName.endsWith("_fk") || colName.endsWith("enum_fk")));
	}
}
