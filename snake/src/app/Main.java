package app;

import java.awt.Color;

import com.github.forax.zen.Application;

import code.Coord;
import menu.Menu;


public class Main {
	public static void main(String[] args) {
		Application.run(Color.BLACK, context -> {
			var screenInfo = context.getScreenInfo();
			Menu.menuBoucle(context, new Coord(screenInfo.width(), screenInfo.height()));	
			context.dispose();
		});
	}
}
