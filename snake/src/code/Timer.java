package code;

public class Timer {
	private long fps;
	private final long fpsMax;
	
	public Timer(long current, long max) {
		fps = current;
		fpsMax = max;
	}
	
	public boolean checkTime(long timing) {
		if (timing - fps >= fpsMax) {
			fps = timing;
			return true;
		}
		return false;
	}
	
	public double advancement(long timing) {
		return (timing - fps)/(fpsMax * 1.0);
	}

}
