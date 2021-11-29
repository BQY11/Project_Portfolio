import static org.junit.Assert.*;
import org.junit.Test;

public class ChangeBatteryTest{
	
	@Test
	public void testCanAdd(){
		EspressOSMobile ph = new EspressOSMobile();
		BatteryChange bt = new BatteryChange(30);
		asserEquals(true, EspressOSMobile.changeBattery(bt));
	}
	
	@Test
	public void testSucessfulLevel(){
		EspressOSMobile ph = new EspressOSMobile();
		BatteryChange bt = new BatteryChange(30);
		EspressOSMobile.changeBattery(bt);
		asserEquals(30, ph.getBatteryLife());
	}
	
	@Test
	public void testExceedRange(){
		EspressOSMobile ph = new EspressOSMobile();
		BatteryChange bt = new BatteryChange(120);
		asserEquals(false, EspressOSMobile.changeBattery(bt));
	}
	
	@Test
	public void testNegative(){
		EspressOSMobile ph = new EspressOSMobile();
		BatteryChange bt = new BatteryChange(-10);
		asserEquals(false, EspressOSMobile.changeBattery(bt));
	}
	
	@Test
	public void testUnsucessfullevel(){
		EspressOSMobile ph = new EspressOSMobile();
		BatteryChange bt = new BatteryChange(120);
		int original = ph.getBatteryLife();
		ph.changeBattery(bt);
		int after = ph.getBatteryLife();
		assertEquals(original, after);
	}
	
}





