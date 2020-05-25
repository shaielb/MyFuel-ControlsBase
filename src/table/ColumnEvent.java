package table;

import db.interfaces.IEntity;
import decorator.base.ControlDecorator;

@SuppressWarnings("rawtypes")
public interface ColumnEvent<TEntity extends IEntity> {
	public void execute(TEntity entity, ControlDecorator control) throws Exception;
}
