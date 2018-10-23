package code;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ShippingCost {
	
	public Map<String, Float> SalesTax;
	
	public ShippingCost(String salesTaxPath) {
		SalesTax = new HashMap<String, Float>();
		
		try {
			Scanner scanner = new Scanner(new File(salesTaxPath));
			while(scanner.hasNext()){
				String data = scanner.next();
				String[] values = data.split(",");
				SalesTax.put(values[0], Float.parseFloat(values[1]) / (float)100);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	public float calculateTotal(float rawTotal, String shippingMethod, String destinationState) {
		float totalOut = (float)0.0;
		float shippingCost = (float)0.0;
		float salesTax = (float)1.00;
		
		if(rawTotal <= 0 || shippingMethod == null || destinationState == null) {
			throw new IllegalArgumentException();
		}
		
		if(shippingMethod == "Next-Day Shipping") {
			shippingCost = 25;
		}
		else if(shippingMethod == "Standard Shipping") {
			if(rawTotal <= 50) {
				shippingCost = 10;
			}
		}
		else {
			throw new IllegalArgumentException();
		}
		
		if(SalesTax.containsKey(destinationState)) {
			salesTax = SalesTax.get(destinationState);
		}
		else {
			throw new IllegalArgumentException();
		}
		
		totalOut = shippingCost + (rawTotal * salesTax) + rawTotal;
		
		return totalOut;
	}
}
