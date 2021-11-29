import java.util.*
public class Notify implements NotifyApps{
	
	public ArrayList<String> notilist;
	public Apps app;
	
	public Notify(){
		this.app = app;
		notilist = new ArrayList<String>();
	}
	
	
	public void notifyOS(String noti){
		notilist.add(noti);
	}
	
	
}
