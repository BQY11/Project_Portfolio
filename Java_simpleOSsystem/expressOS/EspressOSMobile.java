import java.util.*;
/**
 * EspressOS Mobile Phone Class.
 *
 *
 * EspressOSMobile
 * In this assignment you will be creating an EspressOS Mobile Phone as part of a simulation.
 * The Mobile phone includes several attributes unique to the phone and has simple functionality.
 * You are to complete 2 classes. EspressOSMobile and EspressOSContact
 *
 * The phone has data
 *  Information about the phone state. 
 *    If it is On/Off
 *    Battery level 
 *    If it is connected to network. 
 *    Signal strength when connected to network
 *  Information about the current owner saved as contact information. 
 *    First name
 *    Last name
 *    Phone number
 *  A list of 10 possible contacts.
 *    Each contact stores first name, last name, phone number and chat history up to 20 messages
 *  
 * The phone has functionality
 *  Turning on the phone
 *  Charging the phone. Increase battery level
 *  Change battery (set battery level)
 *  Use phone for k units of battery (decreases battery level by k)
 *  Search/add/remove contacts
 *
 * Attribute features
 *  if the phone is off. It is not connected. 
 *  if the phone is not connected there is no signal strength
 *  the attribute for battery life has valid range [0,100]. 0 is flat, 100 is full.
 *  the attribute for signal strength has a valid range [0, 5]. 0 is no signal, 5 is best signal.
 * 
 * Please implement the methods provided, as some of the marking is
 * making sure that these methods work as specified.
 *
 *
 */
public class EspressOSMobile
{
	public static final int MAXIMUM_CONTACTS = 10;
	
	
	/* Use this to store contacts. Do not modify. */
	protected EspressOSContact[] contacts;
	public boolean phoneStatus;
	public int batteryLife;
	public boolean networkConnection;
	public int signalStrength;
	// public static int j;
	public EspressOSContact default1;
	public ArrayList<EspressOSContact> list = new ArrayList<EspressOSContact>();
	
	public boolean okornot;


	/* Every phone manufactured has the following attributes
	 * 
	 * the phone is off
	 * the phone has battery life 25
	 * the phone is not connected
	 * the phone has signal strength 0
	 * Each of the contacts stored in the array contacts has a null value
	 * 
	 * the owner first name "EspressOS"
	 * the owner last name is "Incorporated"
	 * the owner phone number is "180076237867"
	 * the owner chat message should have only one message 
	 *         "Thank you for choosing EspressOS products"
	 *
	 */
	public EspressOSMobile() {
		/* given */
		contacts = new EspressOSContact[MAXIMUM_CONTACTS];
		phoneStatus = false;
		batteryLife = 25;
		networkConnection = false;
		signalStrength = 0;
		default1 = new EspressOSContact("EspressOS", "Incorporated", "180076237867");
		default1.addChatMessage("EspressOS", "Thank you for choosing EspressOS products");
	}

	/* returns a copy of the owner contact details
	 * return null if the phone is off
	 */
	public EspressOSContact getCopyOfOwnerContact() {
		if(isPhoneOn()){
			return default1.copy();
		}else{
			return null;
		}
	}


	/* only works if phone is on
	 * will add the contact in the array only if there is space and does not exist
	 * The method will find an element that is null and set it to be the contact
	 */
	public boolean addContact(EspressOSContact contact) {
		if(isPhoneOn() && !list.contains(contact)){
			if(this.list.size() < 10){
				list.add(contact);
				this.contacts = this.list.toArray(this.contacts);
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	/* only works if phone is on
	 * find the object and set the array element to null
 	 * return true on successful remove
	 */
	public boolean removeContact(EspressOSContact contact) {
		if(isPhoneOn() && list.contains(contact)){
			list.remove(contact);
			this.contacts = this.list.toArray(this.contacts);
			return true;
		}else{
			return false;
		}
	}

	/* only works if phone is on
	 * return the number of contacts, or -1 if phone is off
	 */
	public int getNumberOfContacts() {
		if(isPhoneOn()){
			int number = list.size();
			return number;
		}else{
			return -1;
		}
	}

	/* only works if phone is on
	 * returns all contacts that match firstname OR lastname
	 * if phone is off, or no results, null is returned
	 */
	public EspressOSContact[] searchContact(String name){
		ArrayList<EspressOSContact> list2 = new ArrayList<EspressOSContact>();
		if(isPhoneOn()){
			for(int i = 0; i < list.size(); i++){
				if(list.get(i).getLastName().equals(name) || list.get(i).getFirstName().equals(name)){
					list2.add(list.get(i));
				}
			}
			if(list2.size() > 0){
				EspressOSContact[] match = new EspressOSContact[0];
				match = list2.toArray(match);
				return match;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}

	/* returns true if phone is on
	 */
	public boolean isPhoneOn() {
		return this.phoneStatus;
	}

	/* when phone turns on, it costs 5 battery for startup. network is initially disconnected
	 * when phone turns off it costs 0 battery, network is disconnected
	 * always return true if turning off
	 * return false if do not have enough battery level to turn on
	 * return true otherwise
	 */
	 public boolean setPhoneOn(boolean on) {
		int eg = batteryLife-5;
		 //turn off
		if(isPhoneOn() && !on){
			phoneStatus = false;//on-->off -0
			networkConnection = false;
			signalStrength = 0;
			return true;
		//turn on normal
		}else if(!isPhoneOn() && eg > 0 && on){
			batteryLife = batteryLife-5;
			networkConnection = false;
			signalStrength = 0;
			phoneStatus = true;//off-->on -5
			return true;
		//trun on nobattery
		}else if(!isPhoneOn() && on &&(eg <= 0) ){
			networkConnection = false;
			signalStrength = 0;
			phoneStatus = false;//stay off -0
			return false;
		}else{
			return false;
		}
	}
	
	/* Return the battery life level. if the phone is off, zero is returned.
	 */
	public int getBatteryLife() {
		if(isPhoneOn()){
			return batteryLife;
		}else{
			return 0;
		}
	}
	
	/* Change battery of phone.
	 * On success. The phone is off and new battery level adjusted and returns true
	 * If newBatteryLevel is outside manufacturer specification of [0,100], then 
	 * no changes occur and returns false.
	 */
	public boolean changeBattery(Battery battery) {
		if(battery.getLevel() >= 0 && battery.getLevel() <= 100){
			setPhoneOn(false);
			this.batteryLife = battery.getLevel();
			return true;//successful
		}else{
			return false;
		}
	}
	
	/* only works if phone is on. 
	 * returns true if the phone is connected to the network
	 */
	public boolean isConnectedNetwork() {
		if(isPhoneOn() && networkConnection){
			return true;
		}else{
			return false;
		}
	}
	
	/* only works if phone is on. 
	 * when disconnecting, the signal strength becomes zero
	 */
	public void disconnectNetwork() {
		if(isPhoneOn()){
			signalStrength = 0;
		}

	}
	
	/* only works if phone is on. 
	 * Connect to network
	 * if already connected do nothing
	 * if connecting: 
	 *  1) signal strength is set to 1 if it was 0
	 *  2) signal strength will be the previous value if it is not zero
	 *  3) it will cost 2 battery life to do so
	 * returns the network connected status
	 */
	public boolean connectNetwork() {
		int left = batteryLife - 2;
		if(isPhoneOn() && !networkConnection && left > 0){
			networkConnection = true;
			if(signalStrength == 0){
				signalStrength = 1;
			}batteryLife = batteryLife - 2;
			return networkConnection; // NORMAL SITUATION
		}else if(isPhoneOn() && (left < 0 || left == 0)){
			networkConnection = false;
			signalStrength = 0;
			return networkConnection; // NO BATTERY
		}else if(isPhoneOn() && networkConnection){
			return networkConnection;
		}else{
			return false;
		}
	}
	
	/* only works if phone is on. 
	 * returns a value in range [1,5] if connected to network
	 * otherwise returns 0
	 */
	public int getSignalStrength() {
		if(isPhoneOn() && (signalStrength > 0 && signalStrength < 6) && networkConnection){
			return signalStrength;
		}else{
			return 0;
		}
	}

	/* only works if phone is on. 
	 * sets the signal strength and may change the network connection status to on or off
	 * signal of 0 disconnects network
	 * signal [1,5] can connect to network if not already connected
	 * if the signal is set outside the range [0,5], nothing will occur and will return false
	 */
	public boolean setSignalStrength(int x) {
		if(isPhoneOn()){
			if(x == 0){
				signalStrength = x;
				networkConnection = false;
				return false;
			}else if(x > 0 && x <= 5 && !networkConnection){
				signalStrength = x;
				networkConnection = true;
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
    }
	
	/* changes the antenna object
	 * signal strength is set to default and is not connected to a network
	 * if this constraint is violated then the antenna should not be changed.
	 * return true if antenna is changed.
	 */
	public boolean changeAntenna(Antenna antenna) {
		if(antenna == null){
			signalStrength = 0;
			networkConnection = false;
			return false;
		}else if(antenna.getSignalStrength() >= 1 && antenna.getSignalStrength() <= 5 && !networkConnection){
			this.signalStrength = antenna.getSignalStrength();
			networkConnection = true;
			return true;
		}else if(networkConnection && signalStrength <= 5 && signalStrength >= 1 ){
			antenna.setSignalStrength(this.signalStrength);
			networkConnection = true;
			return true;
		}else if(antenna.getSignalStrength() < 1 || antenna.getSignalStrength() > 5){
			return false;
		}else{
			return false;
		}
	}
	
	/* each charge increases battery by 10
	 * the phone has overcharge protection and cannot exceed 100
	 * returns true if the phone was charged by 10
	 */
	public boolean chargePhone() {
		if(batteryLife < 91){
			batteryLife = batteryLife + 10;
			return true;
		}else{
			return false;
		}
	}
	
	/* Use the phone which costs k units of battery life.
	 * if the activity exceeds the battery life, the battery automatically 
	 * becomes zero and the phone turns off.
	 */
	public void usePhone(int k) {
		if(isPhoneOn() && batteryLife > k){
			batteryLife = batteryLife - k;
		}else{
			batteryLife = 0;
			phoneStatus = false;
		}
	}	
}
