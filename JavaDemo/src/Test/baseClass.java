package Test;

public class baseClass
{
    public static void main(String[] args) throws ClassNotFoundException
    {
        Human1 aPerson = new Human1();
        Class c1      = aPerson.getClass();
        System.out.println(c1.getName());

        Human1 anotherPerson = new Woman();
        Class c2      = anotherPerson.getClass();
        System.out.println(c2.getName());  
        
//        Class c3      = Class.forName("Human1");
//        System.out.println(c3.getName());

        Class c4      = Woman.class;
        System.out.println(c4.getName());  
    }
}

class Human1
{    
    /**
     * accessor
     */
    public int getHeight()
    {
       return this.height;
    }

    /**
     * mutator
     */
    public void growHeight(int h)
    {
        this.height = this.height + h;
    }
    
    private int height; 
}


class Woman extends Human1
{
    /**
     * new method
     */
    public Human1 giveBirth()
    {
        System.out.println("Give birth");
        return (new Human1());
    }

}
