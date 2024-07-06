package items;


public record ItemShape(int[][] positions) {

	public int getHorizontalSize() {
		return positions[0].length;
	}

	public int getVerticalSize() {
		return positions.length;
	}

	public ItemShape rotateShape() {
		
		int rows = positions.length;
	    int cols = positions[0].length;
	    int[][] rotated = new int[cols][rows];

	    for (int i = 0; i < rows; i++) {
	        for (int j = 0; j < cols; j++) {
	            rotated[j][rows - i - 1] = positions[i][j];
	        }
	    }
	    return new ItemShape(rotated);
	}
	// In case the top left corner is not at 1
	public int getTopLeftCorner() {
		if (positions[0][0] == 1) {
			return 0;
		}
		
		int dj = 1;
		while (positions[0][dj]==0) {
			dj++;
		}
		
		return dj;
	}

	
	



}