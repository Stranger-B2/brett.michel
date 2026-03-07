package menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.util.Objects;

import com.github.forax.zen.ApplicationContext;

import code.Coord;
import visuel.InterfaceVisuel;

public record MenuVisuel() {
	public static void ShowMenu(ApplicationContext context, Coord screenLength, Coord sizeBoard) {
		Objects.requireNonNull(context);	Objects.requireNonNull(screenLength);
		context.renderFrame(graphics -> {
			InterfaceVisuel.showRectangle(graphics, 0, 0, screenLength.x(), screenLength.y(), Color.YELLOW);
			var max = (screenLength.x() > screenLength.y())?screenLength.x():screenLength.y();
			InterfaceVisuel.showRectangle(graphics, max /50, max /50, screenLength.x()-max/25, screenLength.y()-max/25, Color.BLACK);
			graphics.setColor(Color.YELLOW);
			graphics.setFont(new Font("TitleFont", max /10, max /10));
			graphics.drawString("Snake Game", screenLength.x()/5, screenLength.y()/4);
		});
	}
	
	private static Color setColor(int x) {
		return switch(x%8) {
			case 0->{
				yield Color.BLUE;
			}
			case 1->{
				yield Color.CYAN;
			}
			case 2->{
				yield Color.GREEN;
			}
			case 3->{
				yield Color.MAGENTA;
			}
			case 4->{
				yield Color.ORANGE;
			}
			case 5->{
				yield Color.PINK;
			}
			case 6->{
				yield Color.RED;
			}
			case 7->{
				yield Color.WHITE;
			}
			default -> throw new IllegalArgumentException("Unexpected value: " + x%8);
		};
	}
	
	public static void ShowSideBar(ApplicationContext context, Coord screenLength, double time) {
		Objects.requireNonNull(context);	Objects.requireNonNull(screenLength);
		context.renderFrame(graphics -> {		
			var max = (screenLength.x() > screenLength.y())?screenLength.x():screenLength.y();
			var sizeX = max/12;
			var sizeY = max/25;
			var initX = 0;
			var initY = -max/40;
			for(int i = 0; i * sizeY + initY< screenLength.y() +  max/25; i++) {
				var locationY = initY + (i * sizeY + time * (8 * sizeY))%(screenLength.y() + 2*max/25);
				AffineTransform restore = graphics.getTransform();
				graphics.rotate(Math.toRadians(-30), initX + sizeX/2, locationY + sizeY/2);
				InterfaceVisuel.showRectangle(graphics, initX, locationY, sizeX, sizeY, setColor(i));
		
				graphics.setTransform(restore);
				
				graphics.rotate(Math.toRadians(30), screenLength.x() -(initX + sizeX/2), locationY + sizeY/2);
				InterfaceVisuel.showRectangle(graphics, screenLength.x() - (initX + sizeX), locationY, sizeX, sizeY, setColor(i));
			
				graphics.setTransform(restore);
			}
			InterfaceVisuel.showRectangle(graphics, 0 ,0, max/50, screenLength.y(), Color.YELLOW);
			InterfaceVisuel.showRectangle(graphics, 0 ,0, screenLength.x(), max/50, Color.YELLOW);
			InterfaceVisuel.showRectangle(graphics, 0,screenLength.y()-max/50, screenLength.x(), max/50, Color.YELLOW);
			InterfaceVisuel.showRectangle(graphics, max/15 ,max/50, max/25, screenLength.y()-max/25, Color.BLACK);
	
			InterfaceVisuel.showRectangle(graphics, screenLength.x()-max/50 ,0, max/50, screenLength.y(), Color.YELLOW);
			InterfaceVisuel.showRectangle(graphics, screenLength.x()-(max/15+max/25) ,max/50, max/25, screenLength.y()-max/25, Color.BLACK);
		});
	}
	
	public static void showChoice(ApplicationContext context, Coord screenLength, Coord sizeBoard, double speed) {
		Objects.requireNonNull(context);	Objects.requireNonNull(screenLength);
		context.renderFrame(graphics -> {
	
			var boxInitX =  2*screenLength.x()/15;
			var boxInitY = 3*screenLength.y()/10;
			var boxSizeX = 3*screenLength.x()/12;
			var boxSizeY = 2*screenLength.y()/7;
			InterfaceVisuel.showRectangle(graphics, boxInitX , boxInitY, boxSizeX, boxSizeY*1.5, Color.YELLOW);
			InterfaceVisuel.showRectangle(graphics, boxInitX + screenLength.x()/100, boxInitY +screenLength.y()/10, boxSizeX - screenLength.x()/50, boxSizeY*1.5 - (screenLength.y()/10 + screenLength.y()/50), Color.BLACK);
			
			
			InterfaceVisuel.showRectangle(graphics, screenLength.x() -(boxInitX +boxSizeX) , boxInitY, boxSizeX, boxSizeY, Color.YELLOW);
			InterfaceVisuel.showRectangle(graphics, screenLength.x() -(boxInitX +boxSizeX) + screenLength.x()/100, boxInitY +screenLength.y()/10, boxSizeX - screenLength.x()/50, boxSizeY - (screenLength.y()/10 + screenLength.y()/50), Color.BLACK);
			
			InterfaceVisuel.showRectangle(graphics, 2*screenLength.x()/5 , 2*screenLength.y()/3, screenLength.x()/5, screenLength.y()/6, Color.YELLOW);
			InterfaceVisuel.showRectangle(graphics, 2*screenLength.x()/5 + screenLength.x()/100, 2*screenLength.y()/3 + screenLength.y()/50, screenLength.x()/5 - screenLength.x()/50, screenLength.y()/6 - screenLength.y()/25, Color.RED);
			
			graphics.setColor(Color.BLACK);
			graphics.setFont(new Font("caseFont", screenLength.x() /30, screenLength.x() /30));
			graphics.drawString("Board size", boxInitX + screenLength.x()/20, boxInitY + screenLength.y()/15);
			graphics.drawString("Speed", screenLength.x() + screenLength.x()/14  -(boxInitX +boxSizeX), boxInitY + screenLength.y()/15);
			
			graphics.setColor(Color.YELLOW);
			graphics.setFont(new Font("stats", screenLength.x() /20, screenLength.x() /15));
			graphics.drawString("<| "+ ((sizeBoard.x() < 10)?" ":"") + sizeBoard.x() ,  boxInitX + screenLength.x()/100, boxInitY +  18*screenLength.y()/80);
			graphics.drawString(" |>",  boxInitX + screenLength.x()/6, boxInitY +  18*screenLength.y()/80);
			graphics.drawString("<| "+ ((sizeBoard.y() < 10)?" ":"") + sizeBoard.y() ,  boxInitX + screenLength.x()/100, boxInitY +  30*screenLength.y()/80);
			graphics.drawString(" |>",  boxInitX + screenLength.x()/6, boxInitY +  30*screenLength.y()/80);
			
			graphics.drawString("<|       |>", screenLength.x() - (boxInitX +boxSizeX) + screenLength.x()/200, boxInitY +  17*screenLength.y()/80);
			graphics.setFont(new Font("stats", screenLength.x() /20, screenLength.x() /20));
			graphics.drawString("x" +speed, screenLength.x() - (boxInitX +boxSizeX) + screenLength.x()/15, boxInitY +  16*screenLength.y()/80);
			
			graphics.setFont(new Font("stats", screenLength.x() /20, screenLength.x() /45));
			graphics.drawString("largeur :",  boxInitX + screenLength.x()/11, boxInitY +  13*screenLength.y()/100);
			graphics.drawString("hauteur :",  boxInitX + screenLength.x()/11, boxInitY +  27*screenLength.y()/100);
			
			graphics.setColor(Color.BLACK);
			graphics.setFont(new Font("StartFont", screenLength.x() /20, screenLength.x() /20));
			graphics.drawString("Start", 2 *screenLength.x()/5 + screenLength.x()/23, 2* screenLength.y()/3 +  screenLength.y()/10);
	//		InterfaceVisuel.showRectangle(graphics, screenLength.x()/6 , 3*screenLength.y()/5, screenLength.x()/6, screenLength.y()/4, Color.YELLOW);
		});
	}
}
