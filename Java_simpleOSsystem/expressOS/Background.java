import java.util.*
public class Background implements BackgroundApps{
	
	public Apps app;
	public ArrayList<Apps> backlist;
	
	public void Backgorund(Apps app){
		this.app = app;
		backlist = new ArrayList<Apps>();
		backlist.add(this.app);
	}
	
	public backgroundStart(){
		
	}
	
	public getData(){
		
	}
	
}
