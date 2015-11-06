package Test;

public class CompositionPractice {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		aTorch torch = new aTorch();
		System.out.println("charge for 2 hours:");
		torch.charge(2);
		System.out.println("turn on the torch for 3 hours:");
		torch.TurnOn(3);
		System.out.println("turn on the torch for 3 hours:");
		torch.TurnOn(3);

	}

}

class Battery{
	private double power = 0.0;
	
	public void chargeBattery(double p){
		if(this.power < 1.){
			this.power += p;
		}
	}
	
	public boolean useBattery(double p){
		if(this.power >= p){
			this.power -= p;
			return true;
		} else {
			this.power = 0.0;
			return false;
		}	
	}
}

class aTorch{
	private Battery bat = new Battery();
	
	/** 
     * 10% power per hour use
     * warning when out of power
     */
	public void TurnOn(double hour){
		boolean usable;
		usable = bat.useBattery(hour * 0.1);
		if(!usable){
			System.out.println("No more power usable, must charge!");
		}
	}
	
	 /**
     * 20% power per hour charge
     */
	public void charge(double hours){
		bat.chargeBattery(hours * 0.2);
	}
}
