package visuel;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

import com.github.forax.zen.ApplicationContext;

import code.Coord;
import code.Snake;

public record InterfaceVisuel(double width, double height, double initX, double initY, double size) {
	
	enum colorType{
		CASE1,
		CASE2,
		SNAKE,
		APPLE
	}
	
	public static InterfaceVisuel InitInterfaceVisuel(Coord rangeMax, Coord gridSize) {
		Objects.requireNonNull(rangeMax);	Objects.requireNonNull(gridSize);
		var size = (gridSize.x() > gridSize.y())?( (rangeMax.x()/gridSize.x() * gridSize.y() > rangeMax.y())?rangeMax.y()/gridSize.y():rangeMax.x()/gridSize.x() ):( (rangeMax.y()/gridSize.y() * gridSize.x() > rangeMax.x())?rangeMax.x()/gridSize.x():rangeMax.y()/gridSize.y() );
		var initX = (rangeMax.x() - gridSize.x() * size) / 2; 
		var initY = (rangeMax.y() - gridSize.y() * size) / 2; 
		return new InterfaceVisuel(rangeMax.x(), rangeMax.y(), initX, initY, size);
	}
	
	/** to show a square
	 * @param graphics where the square will be drawn
	 * @param x the coordinate on the horizontal axis
	 * @param y the coordinate on the vertical axis
	 * @param size the size of the square
	 * @param colorBlock the color of the square
	 */
	private static void showCase(Graphics2D graphics, double x, double y, double size, Color colorBlock) {
		Objects.requireNonNull(graphics);	Objects.requireNonNull(colorBlock);
		graphics.setColor(colorBlock);
		var rec = new Rectangle2D.Double(x, y, size, size);
		graphics.fill(rec);
	}
	
	/*private static void showCase(Graphics2D graphics, Coord cord, double taille, Color colorBlock) {
		Objects.requireNonNull(graphics);	Objects.requireNonNull(cord);		Objects.requireNonNull(colorBlock);
		showCase(graphics, cord.x(), cord.y(), taille, colorBlock);
	} */
	
	/**to show a rectangle
	 * @param graphics where the square will be drawn
	 * @param x the coordinate on the horizontal axis
	 * @param y the coordinate on the vertical axis
	 * @param taille_x the size of the rectangle horizontally
	 * @param taille_y the size of the rectangle vertically
	 * @param colorBlock the color of the rectangle
	 */
	public static void showRectangle(Graphics2D graphics, double x, double y, double taille_x, double taille_y, Color colorBlock) {
		Objects.requireNonNull(graphics);	Objects.requireNonNull(colorBlock);
		graphics.setColor(colorBlock);
		var rec = new Rectangle2D.Double(x ,y , taille_x, taille_y);
		graphics.fill(rec);
	}
	
	private static Color colorSet(colorType x) {
		Objects.requireNonNull(x);
		switch(x) {
			case CASE1:
				return new Color(129, 34, 141);
			case CASE2:
				return Color.RED;
			case SNAKE:
				return new Color(0,255,255);
			case APPLE:
				return Color.GREEN;
			default:
				return Color.BLACK;
		}
	}
	
	private static Color showBoardCase(int x) {
		return ((x) % 2 == 0)? colorSet(colorType.CASE1):colorSet(colorType.CASE2);
	}
	
	private void showApple(Graphics2D graphics, Coord position) {
		Objects.requireNonNull(graphics);	Objects.requireNonNull(position);
		showCase(graphics, initX + position.x() * size,  initY + position.y() * size, size, colorSet(colorType.APPLE));
	}

/*	private void showAppleGrid(Graphics2D graphics, HashMap<Coord, Boolean> grille) {
		Objects.requireNonNull(graphics);	Objects.requireNonNull(grille);
		for(var elem: grille.keySet()) {
			showApple(graphics, elem);
		}
	}*/
	
	private void showSnake(Graphics2D graphics, Snake snake){
		Objects.requireNonNull(graphics);	Objects.requireNonNull(snake);
		var noHead= 0;
		for(var elem: snake.body()) {
			if (noHead>1) {
				showCase(graphics, initX + elem.x() * size,  initY + elem.y() * size, size, colorSet(colorType.SNAKE));
			}
			else
				noHead++;
		}
	}
	
	private void hideSnakeTail(Graphics2D graphics, Snake snake, double q, Coord gridSize) {
		Objects.requireNonNull(graphics);	Objects.requireNonNull(snake);	Objects.requireNonNull(gridSize);
		var tailEnd = snake.body().getLast();
		var body = snake.body().get(snake.body().size() -2);
		var temp = new Coord(body.x() - tailEnd.x(), body.y() - tailEnd.y());
		showCase(graphics, initX + (tailEnd.x() - temp.x()*q) * size,
		   				   initY + (tailEnd.y() - temp.y()*q) * size,
						   size, 
						   showBoardCase(tailEnd.x() + tailEnd.y()));
		tailEnd = new Coord(tailEnd.x() - temp.x(), tailEnd.y() - temp.y());
		showCase(graphics, initX + tailEnd.x() * size, 
					   	   initY + tailEnd.y() * size, 
					   	   size, 
					   	   (tailEnd.x() > -1 && tailEnd.y() > -1 && tailEnd.x() < gridSize.x() && tailEnd.y() < gridSize.y())?showBoardCase(tailEnd.x() + tailEnd.y()):Color.BLACK);
	}
	
	private void showSemiSnake(Graphics2D graphics, Snake snake, double q, Coord gridSize){
		Objects.requireNonNull(graphics);	Objects.requireNonNull(snake);		Objects.requireNonNull(gridSize);
		if(q>1) q = 1;
		if(q<0) q = 0;
		q = 1 - q;
		var devant = snake.body().get(1);
		var suivant = snake.body().get(2);
		var temp = new Coord(devant.x() - suivant.x(), devant.y() - suivant.y());
		showRectangle(graphics, initX + (devant.x() - temp.x()*q) * size,
						   		initY + (devant.y() - temp.y()*q) * size,
						   		size , 
						   		size ,
						   		colorSet(colorType.SNAKE));
		hideSnakeTail(graphics, snake, q, gridSize);
	}
	
	private void showScore(Graphics2D graphics, int score) {
		Objects.requireNonNull(graphics);
		var max = ((width > height)?width:height) /100;
		showRectangle(graphics, (int)max/5, (int)max/5, (int)max * 10, (int)max*2, Color.BLACK);
		graphics.setColor(Color.WHITE);
		graphics.drawString("Score : " + score, (int)max, (int)max * 2);
		
		/*showRectangle(graphics, max/100, max/100, 10 * max/100, 5 * max/100, new Color(255, 223, 0));
		showRectangle(graphics, max/50, max/50, 8 * max/100, 3 * max/100, Color.BLACK);	*/
	}
	
	public void showGame(ApplicationContext context, Coord apple, Coord gridSize, Snake snake, int score, Coord screenLength) {
		Objects.requireNonNull(context);	Objects.requireNonNull(apple);	Objects.requireNonNull(gridSize);		Objects.requireNonNull(snake);
		context.renderFrame(graphics -> {
			showRectangle(graphics, 0,0, screenLength.x(), screenLength.y(), Color.BLACK);
			for (int i = 0; i < gridSize.x(); i++) {
				for (int j = 0; j < gridSize.y(); j++) {
					showCase(graphics, initX + i * size,  initY + j * size, size, showBoardCase(i + j));
				}
			}
			showApple(graphics, apple);
			showSnake(graphics, snake);
			showScore(graphics, score);
		});
	}
	
	public void showGameOptimized(ApplicationContext context, Coord apple, Coord gridSize, Snake snake, int score, double time) {
		Objects.requireNonNull(context);	Objects.requireNonNull(apple);		Objects.requireNonNull(gridSize);	Objects.requireNonNull(snake);
		context.renderFrame(graphics -> {
			showSemiSnake(graphics, snake, time, gridSize);
			showApple(graphics, apple);
			showScore(graphics, score);
		});
	}
}
