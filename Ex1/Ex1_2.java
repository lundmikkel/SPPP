public class Ex1_2 {

	public static void main(String[] args) {
		//Task 1
		doTwice(new Runnable(){
			public void run() {
				System.out.println("Hello, World!");
			}
		});
		
		//Task 3
		doNTimes(new Runnable(){
			public void run(){
				System.out.println("Same String 14 times!");
			}
		}, 14);
		
		write14Times("write14Times method");
	}
	
	public static void doTwice(Runnable r) {
		r.run();
		r.run();
	}
	
	//Task 2
	public static void doNTimes(Runnable r, int n) {
		for(int i = 0; i<n; i++){
			r.run();
		}
	}
	
	//Task 4
	public static void write14Times(final String s){
		doNTimes(new Runnable(){
			public void run(){
				System.out.println(s);
			}
		}, 14);
	}
}
