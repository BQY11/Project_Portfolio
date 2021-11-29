public class AntennaChange extends Antenna {
	
	private boolean conectivity;
	private int signal;
	
	public AntennaChange(boolean conectivity, int signal){
		this.signal = signal;
		this.conectivity = conectivity;
	}
    
	public boolean isConnected(){
		return conectivity;
	}

    public void setNetwork(boolean isConnected){
		conectivity = isConnected;
	}

    public int getSignalStrength(){
		return signal;
	}

    public void setSignalStrength(int n){
		this.signal = n;
	}

}
