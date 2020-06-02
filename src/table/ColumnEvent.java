package table;

import adapter.base.ControlAdapter;
import db.interfaces.IEntity;

@SuppressWarnings("rawtypes")
public interface ColumnEvent<TEntity extends IEntity> {
	public void execute(TEntity entity, ControlAdapter control) throws Exception;
}
