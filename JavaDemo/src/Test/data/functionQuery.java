package Test.data;

public class functionQuery {
	private int id;
	private String query;
	private String verify_point;
	
	public void setFuncQueryID(int id){
		this.id = id;
	}
	
	public void setFuncQuery(String query){
		this.query = query;
	}
	
	public void setFuncVP(String verify_point){
		this.verify_point = verify_point;
	}
	
	public int getFuncQueryID(){
		return this.id;
	}
	
	public  String getFuncQuery(){
		return this.query;
	}
	
	public  String getFuncVP(){
		return this.verify_point;
	}

}
