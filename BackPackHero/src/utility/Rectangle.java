package utility;

public record Rectangle( Point upperLeft, Point lowerRight){

	public boolean isPointInRectangle(int i, int j) {
		return ( i >= upperLeft.i() && i <= lowerRight.i() && j >= upperLeft.j() && j <= lowerRight.j() );
	}
	
}
