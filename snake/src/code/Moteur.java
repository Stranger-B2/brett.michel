package code;

import java.util.Objects;
import java.util.random.RandomGenerator;

public class Moteur {
	Coord apple;
	
	public Moteur(Snake snake, RandomGenerator rand, Coord maxRange){
		Objects.requireNonNull(snake);	Objects.requireNonNull(rand);	Objects.requireNonNull(maxRange);	
		generateApple(snake, rand, maxRange);
	}
	
	public void generateApple(Snake snake, RandomGenerator rand, Coord maxRange) {
		Objects.requireNonNull(snake);	Objects.requireNonNull(rand);	Objects.requireNonNull(maxRange);	
		if (snake.body().size()-1 >= maxRange.x() * maxRange.y())
			return;
		var appleTemp = new Coord(rand.nextInt(maxRange.x()), rand.nextInt(maxRange.y()));
		if (snake.body().lastIndexOf(appleTemp)>0)
			generateApple(snake, rand, maxRange);
		else
			apple = appleTemp;
	}
	
	public Coord apple() {
		return apple;
	}
}
