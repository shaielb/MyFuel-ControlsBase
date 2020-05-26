package utilities;

import javafx.geometry.Point2D;
import javafx.scene.control.Control;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;

public class UiUtility {

	public static void showTooltip(Control control, String tooltipText, ImageView tooltipGraphic)
	{
		Point2D p = control.localToScene(0.0, 0.0);

		final Tooltip customTooltip = new Tooltip();
		customTooltip.setText(tooltipText);

		control.setTooltip(customTooltip);
		customTooltip.setAutoHide(true);

		customTooltip.show(control.getScene().getWindow(), p.getX()
				+ control.getScene().getX() + control.getScene().getWindow().getX(), p.getY()
				+ control.getScene().getY() + control.getScene().getWindow().getY());

	}

}
