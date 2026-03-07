package menu;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;

import code.Coord;
import code.Game;
import code.Timer;

public record Menu() {
	
	private static boolean compare(double min, double max, double num) {
		return min <= num && num <= max;
	}
	
	private static boolean checkBox(double minX, double minY, double sizeX, double sizeY, Coord pos) {
		return compare(minX, minX + sizeX, pos.x()) && compare(minY, minY + sizeY, pos.y());
	}
	
	public static void menuBoucle(ApplicationContext context, Coord screenLength) {
		var boucle = true;
		var sizeBoard = new Coord(20,20);
		var speed = 1.0;
		MenuVisuel.ShowMenu(context, screenLength, sizeBoard);
		MenuVisuel.showChoice(context, screenLength, sizeBoard, speed);
		var timer = new Timer(System.nanoTime(), 2000000000L);
		while (boucle){
			timer.checkTime(System.nanoTime());
			MenuVisuel.ShowSideBar(context, screenLength, timer.advancement(System.nanoTime()));
			
			var event = context.pollEvent();
			if (event == null) {
				continue;
			}
			switch (event) {
				case PointerEvent e:
					switch(e.action()) {
					case POINTER_DOWN:
						var x = e.location().x();
						var y = e.location().y();
						var pos = new Coord(x, y);
						
						if(checkBox( 2*screenLength.x()/5 , 2*screenLength.y()/3, screenLength.x()/5, screenLength.y()/6, pos)) {
							boucle = false;
						}
						if(checkBox(2*screenLength.x()/15 + screenLength.x()/100, 
								17*screenLength.y()/40,
								7*screenLength.x()/140,
								2*screenLength.y()/7 - 8*screenLength.y()/50, pos)) {
							if (sizeBoard.x()>5 || (sizeBoard.y()>=5 && sizeBoard.x()>1))
								sizeBoard = new Coord(sizeBoard.x() - 1, sizeBoard.y());
							MenuVisuel.showChoice(context, screenLength, sizeBoard, speed);
						}
						if(checkBox(  2*screenLength.x()/15 + 19*screenLength.x()/100, 
								17*screenLength.y()/40,
								7*screenLength.x()/140,
								2*screenLength.y()/7 - 8*screenLength.y()/50, pos)) {
							if (sizeBoard.x()<100)
								sizeBoard = new Coord(sizeBoard.x() + 1, sizeBoard.y());
							MenuVisuel.showChoice(context, screenLength, sizeBoard, speed);
						}
						if(checkBox( 2*screenLength.x()/15 + screenLength.x()/100, 
								47*screenLength.y()/80,
								7*screenLength.x()/140,
								2*screenLength.y()/7 - 17*screenLength.y()/100, pos)) {
							if (sizeBoard.y()>5 || (sizeBoard.x()>=5 && sizeBoard.y()>1))
								sizeBoard = new Coord(sizeBoard.x(), sizeBoard.y() -1);
							MenuVisuel.showChoice(context, screenLength, sizeBoard, speed);
						}
						if(checkBox( 2*screenLength.x()/15 + 19* screenLength.x()/100, 
								47*screenLength.y()/80,
								7*screenLength.x()/140,
								2*screenLength.y()/7 - 17*screenLength.y()/100, pos)) {
							if (sizeBoard.y()<100)
								sizeBoard = new Coord(sizeBoard.x(), sizeBoard.y() + 1);
							MenuVisuel.showChoice(context, screenLength, sizeBoard, speed);
						}
						if(checkBox( 9*screenLength.x()/12-2*screenLength.x()/15+screenLength.x()/100,
								4*screenLength.y()/10, 
								3*screenLength.x()/70,
								2*screenLength.y()/7 - (screenLength.y()/10 + screenLength.y()/50), pos)) {
							if(speed > 0.25)
								speed -= 0.25;
							MenuVisuel.showChoice(context, screenLength, sizeBoard, speed);
						}
						if(checkBox(  9*screenLength.x()/12-2*screenLength.x()/15+screenLength.x()/5,
								3*screenLength.y()/10 +screenLength.y()/10, 
								screenLength.x()/24,
								2*screenLength.y()/7 - (screenLength.y()/10 + screenLength.y()/50), pos)) {
							if(speed < 2)
								speed += 0.25;
							MenuVisuel.showChoice(context, screenLength, sizeBoard, speed);
						}
						break;
					default:
						break;
					}
					break;
				case KeyboardEvent e:
					switch(e.key()) {
						case KeyboardEvent.Key.ESCAPE->{
							return;
						}
					default->{}
					}
			default:
				break;
			}
		}
		Game.gameplay(context, screenLength, sizeBoard, speed);
	}
}
