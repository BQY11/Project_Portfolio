public class BatteryChange extends Battery
{
	public int batteryLevel;
	
	public BatteryChange(int batteryLevel){
		this.batteryLevel = batteryLevel;
	}
	
	public void setLevel(int value){
		batteryLevel = value;
	}
    
    public int getLevel(){
		return batteryLevel;
	}


}
