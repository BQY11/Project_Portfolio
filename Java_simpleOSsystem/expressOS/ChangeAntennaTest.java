import static org.junit.Assert.*;
import org.junit.Test;

public class ChangeAntennaTest{
	
	@Test
	public void testCanChange(){
		EspressOSMobile ph = new EspressOSMobile();
		AntennaChange antenna = new AntennaChange(true, 4);
		assertEquals(true, ph.changeAntenna(antenna));	
	}
	
	@Test
	public void testCannot1(){
		EspressOSMobile ph = new EspressOSMobile();
		AntennaChange antenna = new AntennaChange(true, 7);
		assertEquals(false, ph.changeAntenna(antenna));	
	}
	
	@Test
	public void testCannot2(){
		EspressOSMobile ph = new EspressOSMobile();
		AntennaChange antenna = new AntennaChange(true, -1);
		assertEquals(false, ph.changeAntenna(antenna));	
	}	
	
	@Test
	public void testbroken(){
		EspressOSMobile ph = new EspressOSMobile();
		AntennaChange antenna = new AntennaChange(false, 0);
		assertEquals(false, ph.changeAntenna(antenna));	
	}
	
	@Test
	public void testSucessSignal(){
		EspressOSMobile ph = new EspressOSMobile();
		AntennaChange antenna = new AntennaChange(true, 4);
		ph.changeAntenna(antenna);
		assertEquals(4, ph.getSignalStrength());	
	}
	
	@Test
	public void testFailSignal(){
		EspressOSMobile ph = new EspressOSMobile();
		int before = ph.getSignalStrength();
		AntennaChange antenna = new AntennaChange(true, -2);
		ph.changeAntenna(antenna);
		int after = ph.getSignalStrength();
		assertEquals(before, after);	
	}
	
	
	
	
}
