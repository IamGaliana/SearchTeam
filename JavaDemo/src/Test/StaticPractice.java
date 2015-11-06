package Test;

public class StaticPractice {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(Human.getPopulation());
		Human aPerson = new Human(160);
		System.out.println(aPerson.getPopulation());
	}

}

class Human{
	public Human(int h){
		this.height = h;
		// 每创建一个对象，human类的人口数就加1
		Human.population =  Human.population + 1;
	}
	
	public int getHeight(){
		return this.height;
	}
	
	public void growHeight(int h){
		this.height += h;
	}
	
	public void breath(){
		System.out.println("hu...hu...hu...");
	}
	
	private int height = 0;
	//final = unchangeable
	private final static double PI = 3.141592653; 
	
	public static int getPopulation(){
		return Human.population;
	}
	
	private static int population = 160000000;
	private static boolean is_mammal =  true;
}
