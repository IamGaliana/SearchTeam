package Test;

public class InterfacePractice {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MusicCup cup = new	MusicCup();
		cup.addWater(10);
		System.out.println(String.format("now water volumn is: %s", cup.waterContent()));
		cup.playMusic();
	}

}

class MusicCup implements IMusicCup{
	private int _water = 0;
	public void addWater(int w){
		this._water = this._water + w;
	}
	
	public void drinkWater(int w){
		this._water = this._water - w;
	}
	
	public int waterContent(){
		return this._water;
	}
	
	public void playMusic(){
		System.out.println("la...la...la");
	}
}
