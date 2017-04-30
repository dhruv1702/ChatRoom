package assignment7;

public class emoji {
	public static void main(String[] args) {
	    for (int codePoint = 0x1F600; codePoint <= 0x1F64F;) {
	        System.out.print(Character.toChars(codePoint));
	        codePoint++;
	        if (codePoint % 16 == 0) {
	            System.out.println();
	        }
	    }
	}
}
