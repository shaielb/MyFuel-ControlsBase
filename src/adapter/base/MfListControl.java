package adapter.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.interfaces.IEntity;
import handler.UiHandler;

/**
 * @author shaielb
 *
 * @param <TType>
 */
public abstract class MfListControl<TType> extends ControlAdapter<TType> {

	/**
	 * 
	 */
	public static final String NoSelection = "-";
	
	/**
	 * 
	 */
	private List<? extends IEntity> _entities;
	
	/**
	 * 
	 */
	protected Map<String, IEntity> _entitiesValuesMap = new HashMap<String, IEntity>();
	/**
	 * 
	 */
	protected Map<Integer, IEntity> _entitiesIdsMap = new HashMap<Integer, IEntity>();
	/**
	 * 
	 */
	protected Map<Integer, String> _entitiesIdsValuesMap = new HashMap<Integer, String>();

	/**
	 * @param entities
	 * @param colTitle
	 * @throws Exception
	 */
	public void setEntities(List<? extends IEntity> entities, String colTitle) throws Exception {
		_entities = entities;
		List<String> values = new ArrayList<String>();
		values.add(NoSelection);
		for (IEntity entity : _entities) {
			UiHandler.iterateFields(entity.getClass(), (field, colName, index) -> {
				if (colName.endsWith(colTitle)) {
					try {
						String value = (String) field.get(entity);
						_entitiesValuesMap.put(value, entity);
						_entitiesIdsMap.put(entity.getId(), entity);
						_entitiesIdsValuesMap.put(entity.getId(), value);
						values.add(value);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		setValues(values);
	}

	/**
	 * @param values
	 */
	protected abstract void setValues(List<String> values);
}
