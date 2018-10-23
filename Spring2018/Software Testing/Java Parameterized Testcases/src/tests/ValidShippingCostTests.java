package tests;

import code.ShippingCost;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@RunWith(Parameterized.class)
public class ValidShippingCostTests {
	//Expected Output
	private float expected;
	
	//Input Parameters
	private float rawTotal;
	private String shippingMethod;
	private String destinationState;
	
	private float delta; 
	
	public ValidShippingCostTests(float RawTotal, String ShippingMethod, String DestinationState, float Expected) {
		this.rawTotal = RawTotal;
		this.shippingMethod = ShippingMethod;
		this.destinationState = DestinationState;
		this.expected = Expected;
		this.delta = 0.01f;
	}
	
	@Parameters
	public static Collection<Object[]> data(){
		ShippingCost sc = new ShippingCost("./SalesTax.csv");
		
		ArrayList<Object[]> params = new ArrayList<Object[]>();
		
		//---Strong Normal---
		// Iterates through all states
		
		// Standard and over 50 No Shipping
		for (Iterator<String> i = sc.SalesTax.keySet().iterator(); i.hasNext();) {
		    String state = i.next();
		    float temp = sc.SalesTax.get(state);
		    temp = 100.00f * (temp + 1f);
		    params.add(new Object[] {100.00f, "Standard Shipping", state, temp});
		}
		
		// Standard and under 50 + Shipping
		for (Iterator<String> i = sc.SalesTax.keySet().iterator(); i.hasNext();) {
		    String state = i.next();
		    float temp = sc.SalesTax.get(state);
		    temp = (40.00f * (temp + 1f)) + 10f;
		    params.add(new Object[] {40.00f, "Standard Shipping", state, temp});
		}
		
		// Next-Day Shipping and over 50 + Shipping
		for (Iterator<String> i = sc.SalesTax.keySet().iterator(); i.hasNext();) {
		    String state = i.next();
		    float temp = sc.SalesTax.get(state);
		    temp = (100.00f * (temp + 1f)) + 25f;
		    params.add(new Object[] {100.00f, "Next-Day Shipping", state, temp});
		}
				
		// Next-Day Shipping and under 50 + Shipping
		for (Iterator<String> i = sc.SalesTax.keySet().iterator(); i.hasNext();) {
		    String state = i.next();
		    float temp = sc.SalesTax.get(state);
		    temp = (40.00f * (temp + 1f)) + 25f;
		    params.add(new Object[] {40.00f, "Next-Day Shipping", state, temp});
		}
		
		//Boundary Testing
		float temp = sc.SalesTax.get("NC");
	    temp = (Float.MAX_VALUE * (temp + 1f)) + 25f;
	    params.add(new Object[] {Float.MAX_VALUE, "Next-Day Shipping", "NC", temp}); //Expected is infinity So doesn't really work
		
		temp = sc.SalesTax.get("NC");
	    temp = (0.01f * (temp + 1f)) + 25f;
	    params.add(new Object[] {0.01f, "Next-Day Shipping", "NC", temp});
	    
		return params;
	}

	@Test
	public void shippingCostCalcTest() {
		ShippingCost sc = new ShippingCost("./SalesTax.csv");
		System.out.println("RawTotal: " + rawTotal + " ShippingMethod: " + shippingMethod + " DestinationState = " + destinationState + " Expected: " + expected);
		assertEquals(expected, sc.calculateTotal(rawTotal, shippingMethod, destinationState), delta);
	}
}
