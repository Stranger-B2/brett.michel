package code;

import java.util.ArrayList;
import java.util.Objects;

public record Snake(ArrayList<Coord> body) {
	public Snake{
		Objects.requireNonNull(body);
	}
	
	public Snake(){
		this(new ArrayList<Coord>());
	}
	
	public void init(Coord range) {
		Objects.requireNonNull(range);	
		if (range.y() >= range.x()) {
			body.add(new Coord(0,-1));
			for(var i = -3; i < 0; i++)
				body.add(new Coord((range.x()-1)/2, range.y() + i));
		}else {
			body.add(new Coord(1,0));
			var temp = 0;
			if (range.x()/2-2 < 0) temp = 1;
			for(var i = temp; i > -3 + temp; i--)
				body.add(new Coord(range.x()/2+i, range.y()/2));
		}
	}
	
	public void direction(Coord dir) {
		Objects.requireNonNull(dir);	
		var tempo = new Coord(dir.x() + body.get(1).x(), dir.y() + body.get(1).y());
		if(!tempo.equals(body.get(2))) {
			body.removeFirst();
			body.addFirst(dir);
		}
	}
	
	public boolean advance(Coord apple) {
		Objects.requireNonNull(apple);	
		var tempo = new Coord(body.getFirst().x() + body.get(1).x(), body.getFirst().y() + body.get(1).y());
		body.add(1, tempo);
		if(!tempo.equals(apple)) {
			body.removeLast();
			return false;
		}
		return true;
	}
	
	public boolean deathCheck(Coord maxRange) {
		Objects.requireNonNull(maxRange);	
		var tempX = body.get(1).x();
		var tempY = body.get(1).y();
		if(tempX < 0 || tempY < 0 || maxRange.x() <= tempX || maxRange.y() <= tempY)
			return true;
		if(body.lastIndexOf(body.get(1)) > 2){
			return true;
		}
		return false;
	}
	
	public void kill() {
		body.add(1, new Coord(-1,-1));
	}
}
