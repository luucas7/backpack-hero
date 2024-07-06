package utility;

import java.util.Map;
import java.util.Objects;

public record Point(int i, int j){
	
	
	public Point getCell(Map<Rectangle, Point> gridCases) {
		Objects.requireNonNull(gridCases);
		for (var rectangle : gridCases.keySet()) {
			if (rectangle.isPointInRectangle(i, j)) {
				return gridCases.get(rectangle);
			}
		}
		return null;
	}
	
	public int getIndex(Map<Rectangle, Integer> gridCases) {
		Objects.requireNonNull(gridCases);
		for (var rectangle : gridCases.keySet()) {
			if (rectangle.isPointInRectangle(i, j)) {
				return gridCases.get(rectangle);
			}
		}
		return -1;
	}
	
	public Rectangle getIndexKey(Map<Rectangle, Integer> gridCases) {
		Objects.requireNonNull(gridCases);
		for (var rectangle : gridCases.keySet()) {
			if (rectangle.isPointInRectangle(i, j)) {
				return rectangle;
			}
		}
		return null;
	}
	
	
	public boolean isPointInRectangle(Rectangle rectangle) {
		return ( i >= rectangle.upperLeft().i() && i <= rectangle.lowerRight().i() && j >=  rectangle.upperLeft().j() && j <= rectangle.lowerRight().j() );
	}
	
	public boolean isPointInRectangle(int upperLefti, int upperLeftj, int lowerRighti, int lowerRightj  ) {
		return isPointInRectangle(new Rectangle(new Point(upperLefti, upperLeftj), new Point(lowerRighti, lowerRightj)));
	}
	
	
	
	
	
}