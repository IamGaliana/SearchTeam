package Test;

public class ExceptionPractice {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int avg = 0;
		try{
			avg = practise.Avg(10, 20);
			System.out.println(avg);
			practise.TryNullArray();
			System.out.println(practise.Avg3(-2, 5, 9));
		}catch(MyException e){
			System.out.println(e);
		}		
	}
}

class MyException extends Exception{
	MyException(String exceptionMessage){
		super(exceptionMessage);
	}
}

class practise{
	/**
	 * 抛出自定义异常
	 * @param a
	 * @param b
	 * @return
	 * @throws MyException
	 */
	static int Avg(int a, int b) throws MyException{
		if(a<0 || b<0)
			throw new MyException("numbers must be positive");
		if(a>100 || b>100)
			throw new MyException("numbers must be less than 100");
		return (a+b)/2;
	}
	
	/**
	 * try null pointer exception
	 */
	static void TryNullArray(){
		int[] a = null;
		try{
			a[10] = 100;
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("ArrayIndexOutOfBounds");
		}catch(NegativeArraySizeException e){
			System.out.println("NegativeArraySize");
		}catch(NullPointerException e){
			System.out.println("NullPointer");
		}
	}
	
	/**
	 * 取平均值，不在范围的抛出指定异常
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	static int Avg3(int a, int b, int c) throws ArithmeticException, IllegalArgumentException{
		if(a<0 || b<0 || c<0)
			throw new ArithmeticException();
		if(a>100 || b>100 || c>100)
			throw new IllegalArgumentException();
		return (a+b+c)/3;
	}
}
