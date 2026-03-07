package code;

import java.util.HashMap;
import java.util.Objects;
import java.util.random.RandomGenerator;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.KeyboardEvent;

import visuel.InterfaceVisuel;

public record Game() {
	
	public static int temp(ApplicationContext context, Snake snake, Moteur moteur, Coord maxRange, InterfaceVisuel visu, RandomGenerator rand, int score) {
		Objects.requireNonNull(context);	Objects.requireNonNull(snake);	Objects.requireNonNull(moteur);	Objects.requireNonNull(maxRange);	Objects.requireNonNull(visu);	Objects.requireNonNull(rand);	
		visu.showGameOptimized(context, moteur.apple(), maxRange, snake, score, 1.0);
		if(snake.advance(moteur.apple())) {
			moteur.generateApple(snake, rand, maxRange);
			score++;
		}
		return score;
	}
	
	public static void gameplay(ApplicationContext context, Coord screenLength, Coord maxRange, double speed) {
		
		var grille = new HashMap<Coord, Boolean>();
		RandomGenerator rand = RandomGenerator.getDefault();
		grille.put(new Coord(1,0), true);
		var snake = new Snake();
		snake.init(maxRange);
		var moteur = new Moteur(snake, rand, maxRange);			
		var visu = InterfaceVisuel.InitInterfaceVisuel(screenLength, maxRange);
		var score = 0;
		
		visu.showGame(context, moteur.apple(), maxRange, snake, score, screenLength);
		var timer = new Timer(System.nanoTime(), ((int)((2.0-speed)/0.25*200000000L)+1000000000L) / ((maxRange.x()>maxRange.y())?maxRange.x():maxRange.y()) );
		while(!snake.deathCheck(maxRange)) {
			if (timer.checkTime(System.nanoTime())) {
				score = temp(context, snake, moteur, maxRange, visu, rand, score);
			}else {
				visu.showGameOptimized(context, moteur.apple(), maxRange, snake, score, timer.advancement(System.nanoTime()));
			}
			var event = context.pollEvent();
			if (event == null) {
				continue;
			}
			switch (event) {
				case KeyboardEvent e:
					switch(e.key()) {
						case KeyboardEvent.Key.ESCAPE->{
							snake.kill();
						}
						case KeyboardEvent.Key.LEFT->{
							snake.direction(new Coord(-1,0));							
						}
						case KeyboardEvent.Key.RIGHT->{
							snake.direction(new Coord(1,0));
						}
						case KeyboardEvent.Key.UP->{
							snake.direction(new Coord(0,-1));
						}
						case KeyboardEvent.Key.DOWN->{
							snake.direction(new Coord(0,1));
						}
						default ->{}
					}
					break;
				default:
					break;
			}
		}	
	}
}
