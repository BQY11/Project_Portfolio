import java.util.*;
/**
 * EspressOS Mobile Phone Contact Class.
 *
 * EspressOSContact
 * 
 * == Contact data ==
 * Each EspressOSContact stores the first name, last name and phone number of a person. 
 * These can be queried by calling the appropriate get method. They are updated 
 * with new values. The phone number can only be a 6 - 14 digit number.
 * The chat history is also stored. 
 * 
 * 
 * == Chat history ==
 * Each EspressOSContact stores the history of chat messages related to this contact. 
 * Suppose there is a conversation between Angus and Beatrice:
 * 
 * Angus: Man, I'm so hungry! Can you buy me a burrito?
 * Beatrice: I don't have any money to buy you a burrito.
 * Angus: Please? I haven't eaten anything all day.
 * 
 * Each time a message is added the name of the person and the message is 
 * combined as above and recorded in the sequence it was received.
 * 
 * The messages are stored in the instance variable String array chatHistory. Provided for you.
 * Unfortunately there are only 20 messages maximum to store and no more. 
 * When there are more than 20 messages, oldest messages from this array are discarded and 
 * only the most recent 20 messages remain. 
 * 
 * The functions for chat history are 
 *   addChatMessage
 *   getLastMessage
 *   getOldestMessage
 *   clearChatHistory()
 *
 * Using the above conversation as an example
 *   addChatMessage("Angus", "Man, I'm so hungry! Can you buy me a burrito?");
 *   addChatMessage("Beatrice", "I don't have any money to buy you a burrito.");
 *   addChatMessage("Angus", "Please? I haven't eaten anything all day.");
 *
 *   getLastMessage() returns "Angus: Please? I haven't eaten anything all day."
 *   getOldestMessage() returns "Angus: Man, I'm so hungry! Can you buy me a burrito?"
 *
 *   clearChatHistory()
 *   getLastMessage() returns null
 *   getOldestMessage() returns null
 *
 *
 * == Copy of contact ==
 * It is necessary to make copy of this object that contains exactly the same data. 
 * There are many hackers working in other parts of EspressOS, so we cannot trust them 
 * changing the data. A copy will have all the private data and chat history included.
 *
 *
 * Please implement the methods provided, as some of the marking is
 * making sure that these methods work as specified.
 *
 *
 */
public class EspressOSContact
{
	public static final int MAXIMUM_CHAT_HISTORY = 20;	
	
	/* given */
	protected String[] chatHistory;
	public String fname;
	public String lname;
	public String pnumber;
	public ArrayList<String> chatMessage; 
	
	
	public EspressOSContact(String pfname, String plname, String ppnumber) {
		/* given */
		chatHistory = new String[20];
		fname = pfname;
		lname = plname;
		pnumber = ppnumber;
		chatMessage = new ArrayList<>();
	}
	
	public String getFirstName() {
		return fname;
	}
	public String getLastName() {
		return lname;
	}
	public String getPhoneNumber() {
		return pnumber;
	}

	/* if firstName is null the method will do nothing and return
	 */
	public void updateFirstName(String firstName) {
		this.fname = firstName;
	}
	/* if lastName is null the method will do nothing and return
	 */
	public void updateLastName(String lastName) {
		this.lname = lastName;
	}
	
	/* only allows integer numbers (long type) between 6 and 14 digits
	 * no spaces allowed, or prefixes of + symbols
	 * leading 0 digits are allowed
	 * return true if successfully updated
	 * if number is null, number is set to an empty string and the method returns false
	 */
	public boolean updatePhoneNumber(String number) {
		if (pnumber.equals(number)){
			return true;
		}else{
			return false;
		}
	}
	
	/* add a new message to the chat
	 * The message will take the form
	 * whoSaidIt + ": " + message
	 * 
	 * if the history is full, the oldest message is replaced
	 * Hint: keep track of the position of the oldest or newest message!
	 */
	public void addChatMessage(String whoSaidIt, String message) {
		// ArrayList chatMessage = new ArrayList<String>();
		chatMessage.add(whoSaidIt + ": " + message);
		if(chatMessage.size() > 20){
			chatMessage.remove(0);
			this.chatHistory  = this.chatMessage.toArray(this.chatHistory);
		}else{
			this.chatHistory  = this.chatMessage.toArray(this.chatHistory);

		}
	}
		

	/* after this, both last and oldest message should be referring to index 0
	 * all entries of chatHistory are set to null
	 */
	public void clearChatHistory() {
		for(int s = 0; s < 20; s++ ){
			chatHistory[s] = null;
		}
	}

	/* returns the last message 
	 * if no messages, returns null
	 */
	public String getLastMessage() {
		if(chatMessage.size() >= 20){
			return chatHistory[19];
		}else if(chatMessage.size() < 20 && chatMessage.size() > 0){
			return chatHistory[chatMessage.size()-1];
		}else if(chatHistory == null){
			return null;
		}else{
			return null;
		}
	}
	
	/* returns the oldest message in the chat history
	 * depending on if there was ever MAXIMUM_CHAT_HISTORY messages
	 * 1) less than MAXIMUM_CHAT_HISTORY, returns the first message
	 * 2) more than MAXIMUM_CHAT_HISTORY, returns the oldest
	 * returns null if no messages exist
	 */
	public String getOldestMessage() {
		if (chatMessage.size() <= 20 && chatMessage.size() > 0){
			return chatHistory[0];
		}else if(chatMessage == null){
			return null;
		}else if(chatMessage.size() > 20){
			return chatHistory[0];
		}else{
			return null;
		}
	}


	/* creates a copy of this contact
	 * returns a new EspressOSContact object with all data same as the current object
	 */
	public EspressOSContact copy() {
		EspressOSContact sth = new EspressOSContact(this.fname, this.lname, this.pnumber);
		sth.chatHistory = this.chatHistory.clone();
		sth.chatMessage = new ArrayList<>(this.chatMessage);
		return sth;
	}
	
	/* -- NOT TESTED --
	 * You can impelement this to help with debugging when failing ed tests 
	 * involving chat history. You can print whatever you like
	 * Implementers notes: the format is printf("%d %s\n", index, line); 
	 */
	public void printMessagesOldestToNewest() {
		for(String p: chatHistory){
			System.out.println(p);
		}
	}
	public String toString(){
		return this.fname + " " + this.lname;
	}
	
	public static void main(String[] args){
		EspressOSContact pe = new EspressOSContact("ASD","SDF","1231432");
		EspressOSContact e = new EspressOSContact("ASD","SDF","1231432");
		EspressOSMobile phone = new EspressOSMobile();
		System.out.println(phone.setPhoneOn(true));
		
		EspressOSContact copy = phone.getCopyOfOwnerContact();
		copy.updateFirstName("lee");
		
		phone.addContact(e);
		phone.addContact(pe);
		if(phone.contacts[0]==e){
			System.out.println("sdfasdf");
		}
		System.out.println(phone.contacts[0]);
		System.out.println(phone.contacts[1]);
		System.out.println(phone.contacts[2]);
		System.out.println(phone.contacts.length);
		
		// System.out.print(phone.searchContact("ASD"));
// 		System.out.println(phone.default1);
// 		System.out.println(phone.default1.chatMessage);

// 		System.out.println(Arrays.toString(phone.default1.chatHistory));

// 		System.out.println(Arrays.toString(copy.chatHistory));

		// pe.addChatMessage("1","oh oh");
		// pe.addChatMessage("2","oh oh");
		// pe.addChatMessage("3","oh oh");
		// pe.addChatMessage("4","oh oh");
		// pe.addChatMessage("5","oh oh");
		// pe.addChatMessage("6","oh oh");
		// pe.addChatMessage("7","oh oh");
		// pe.addChatMessage("8","oh oh");
		// pe.addChatMessage("9","oh oh");
		// pe.addChatMessage("10","oh oh");
		// pe.addChatMessage("11","oh oh");
		// pe.addChatMessage("12","oh oh");
		// pe.addChatMessage("13","oh oh");
		// pe.addChatMessage("14","oh oh");
		// pe.addChatMessage("15","oh oh");
		// pe.addChatMessage("16","oh oh");
		// pe.addChatMessage("17","oh oh");
		// pe.addChatMessage("18","oh oh");
		// pe.addChatMessage("19","oh oh");
		// pe.addChatMessage("20","oh oh");
		// pe.addChatMessage("21","oh oh");
		// pe.addChatMessage("22","oh oh");
		// pe.addChatMessage("23","oh oh");
		// pe.addChatMessage("24","oh oh");
		// pe.addChatMessage("25","oh oh");
		// pe.addChatMessage("26","oh oh");
		// pe.addChatMessage("27","oh oh");
		// pe.addChatMessage("28","oh oh");
		//pe.clearChatHistory();
		// System.out.println(pe.getOldestMessage());
		// System.out.println(pe.getLastMessage());
	}
}
