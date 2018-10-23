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
public class InvalidShippingCostTests {
		//Input Parameters
		private float rawTotal;
		private String shippingMethod;
		private String destinationState;
		
		private float delta; 
		
		public InvalidShippingCostTests(float RawTotal, String ShippingMethod, String DestinationState) {
			this.rawTotal = RawTotal;
			this.shippingMethod = ShippingMethod;
			this.destinationState = DestinationState;
			this.delta = 0.01f;
		}
		
		@Parameters
		public static Collection<Object[]> data(){
			ShippingCost sc = new ShippingCost("./SalesTax.csv");
			
			ArrayList<Object[]> params = new ArrayList<Object[]>();
			
			//---Invalid Cases---
			
			// rawTotal
			float temp = sc.SalesTax.get("NC");
		    params.add(new Object[] {-Float.MAX_VALUE, "Next-Day Shipping", "NC"}); //Expected is infinity So doesn't really work
			
			temp = sc.SalesTax.get("NC");
		    params.add(new Object[] {-0.01f, "Next-Day Shipping", "NC"});
		    
		    temp = sc.SalesTax.get("NC");
		    params.add(new Object[] {0.00f, "Next-Day Shipping", "NC"});
		    
		    // shippingMethod
		    temp = sc.SalesTax.get("NC");
		    params.add(new Object[] {-0.01f, "", "NC"});
		    
		    temp = sc.SalesTax.get("NC");
		    params.add(new Object[] {0.00f, null, "NC"});
		    
		    //State
		    temp = sc.SalesTax.get("NC");
		    params.add(new Object[] {0.00f, "Next-Day Shipping", ""});
		    
		    temp = sc.SalesTax.get("NC");
		    params.add(new Object[] {0.00f, "Next-Day Shipping", "NK"});
		    
		    temp = sc.SalesTax.get("NC");
		    params.add(new Object[] {0.00f, "Next-Day Shipping", null});
		    
			return params;
		}

		@Test(expected=IllegalArgumentException.class)
		public void shippingCostCalcTest() {
			ShippingCost sc = new ShippingCost("./SalesTax.csv");
			System.out.println("RawTotal: " + rawTotal + " ShippingMethod: " + shippingMethod + " DestinationState = " + destinationState);
			sc.calculateTotal(rawTotal, shippingMethod, destinationState);
		}
}
