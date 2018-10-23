package tc;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import code.LengthConverter;

public class LengthConverterTest {

	@Test
	public void testValidInputKmtoMiles(){	
		LengthConverter converter = new LengthConverter();
		double miles = converter.kmsToMiles(1);
		Assert.assertEquals(miles, 0.62137119224, 0.0001);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidInputKmtoMiles(){	
		LengthConverter converter = new LengthConverter();
		double miles = converter.kmsToMiles(-1);
	}
	
	@Test
	public void testValidInputMilesToKm(){	
		LengthConverter converter = new LengthConverter();
		double kms = converter.milesToKms(1);
		Assert.assertEquals(kms, 1.60934, 0.0001);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidInputMilesToKm(){	
		LengthConverter converter = new LengthConverter();
		double miles = converter.milesToKms(-1);
	}

}
