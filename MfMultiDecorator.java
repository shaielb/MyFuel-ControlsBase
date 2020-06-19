package widgets.table;

import java.util.ArrayList;
import java.util.List;

import controls.MfCheckBox;
import db.interfaces.IEntity;
import handler.UiHandler;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;

/**
 * @author shaielb
 *
 * @param <TEntity>
 */
@SuppressWarnings({ "unchecked" })
public class MfMultiDecorator<TEntity extends IEntity> extends TableDecorator<TEntity> {

	/**
	 * 
	 */
	private List<IEntity> _selectedEntities = new ArrayList<IEntity>();

	/**
	 *
	 */
	@Override
	protected void apply() {
		MfCheckBox mfCb = new MfCheckBox();
		mfCb.setColumnName("Select");
		_table.addColumn(UiHandler.createColumn(mfCb, () -> { return new MfCheckBox(); },
				(entity, control) -> {
					CheckBox cb = (CheckBox) control.getInstance();
					if (cb.isSelected()) {
						_selectedEntities.add(entity);
					}
					else {
						_selectedEntities.remove(entity);
					}
				}));
	}

	/**
	 * @param entities
	 * @throws Exception
	 */
	public void setEntities(List<TEntity> entities) throws Exception {
		ObservableList<TEntity> list = _table.getObservableList();
		for (TEntity entity : entities) {
			list.add(entity);
		}
	}

	/**
	 * @return
	 */
	public List<IEntity> getSelectedEntities() {
		return _selectedEntities;
	}
}
