package code;

public class LengthConverter {
	public double kmsToMiles(double km) {
		
		if(km < 0) {
			throw new IllegalArgumentException();
		}
		
		return km * 0.621371192;
	}
	
	public double milesToKms(double mile) {
		if(mile < 0) {
			throw new IllegalArgumentException();
		}
		
		return mile / 0.621371192;
	}
}
