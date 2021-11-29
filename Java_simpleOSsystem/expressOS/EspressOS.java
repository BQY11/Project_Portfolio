import java.util.*
public class EspressOS{
	public  Apps application;
	public ArrayList<Apps> insapplist;
	public ArrayList<Apps> runapplist;
	
	
	public AppSupport(App application){
		this.application = application;
		insapplist = new ArrayList<Apps>();  
		runapplist = new ArrayList<Apps>();
	}
	
	public boolean install(Apps aplication){
		
	}
	
	public boolean uninstall(String name){
		
	}
	
	public ArrayList<Apps> getRunningApps(){
		
	}
	
	public ArrayList<Apps> getInstalledApps(){
		
	}
	
	public ArrayList<Apps> getBackgroundApps(){
		
	}
	
	public ArrayList<Apps> getNotifications(){
	
	}
	
	public boolean run(Apps application){
		application.start();
		return true;
	}
	
	public void close(Apps application){
		application.exit();
	}
	
	
	
}



