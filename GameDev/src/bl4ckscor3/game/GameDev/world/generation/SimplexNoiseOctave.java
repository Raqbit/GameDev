package bl4ckscor3.game.GameDev.world.generation;

import java.util.Random;

public class SimplexNoiseOctave
{
	private int swaps = 400;
	private static short[] p_supply = 
		{
		42, 222, 46, 158, 150, 17, 224, 11, 252, 133, 220, 190, 93, 151, 161, 243, 54, 48, 78, 56, 20, 9,
		66, 250, 219, 25, 189, 31, 111, 14, 235, 131, 34, 170, 232, 101, 156, 159, 174, 88, 247, 41, 5, 122,
		163, 23, 107, 12, 230, 91, 50, 173, 135, 94, 248, 209, 92, 229, 40, 144, 241, 74, 134, 187, 194, 118,
		64, 114, 75, 36, 251, 160, 183, 130, 197, 200, 205, 6, 191, 166, 70, 188, 45, 83, 60, 132, 154, 147,
		215, 53, 227, 202, 245, 238, 13, 87, 223, 225, 103, 162, 72, 152, 63, 120, 171, 168, 108, 115, 117, 180,
		110, 7, 121, 82, 15, 116, 149, 139, 207, 97, 3, 213, 112, 39, 196, 172, 126, 30, 206, 179, 10, 102, 28,
		164, 138, 236, 231, 59, 193, 24, 32, 226, 73, 68, 182, 192, 100, 2, 181, 255, 167, 175, 155, 153, 157, 246,
		4, 214, 141, 137, 29, 234, 89, 18, 19, 212, 142, 44, 47, 169, 1, 71, 37, 113, 104, 204, 210, 27, 186, 216,
		95, 62, 185, 16, 105, 61, 76, 184, 218, 58, 239, 177, 22, 176, 96, 195, 199, 86, 143, 99, 49, 146, 254, 203,
		69, 51, 79, 217, 240, 21, 8, 211, 140, 57, 109, 145, 221, 35, 38, 26, 0, 136, 124, 237, 85, 81, 242, 129, 77,
		123, 80, 178, 106, 201, 84, 43, 119, 127, 125, 128, 65, 228, 253, 67, 148, 90, 55, 52, 233, 198, 98, 249, 244, 208, 33, 165
		};
	private short[] p = p_supply.clone();
	private short[] perm = new short[512];
	private short[] permMod12 = new short[512];
	
	public SimplexNoiseOctave(int seed)
	{
		Random r = new Random(seed);
		
		for (int i = 0; i < swaps; i++)
		{
			//swap position of these two numbers in array "p"
			int origin = r.nextInt(p.length);
			int destination = r.nextInt(p.length);
			short temp = p[origin];
			
			//swapping
			p[origin] = p[destination];
			p[destination] = temp;
		}
		
		for(int i = 0; i < perm.length; i++)
		{
			//see end of class
			perm[i] = p[i & 255];
			permMod12[i] = (short) (perm[i] % 4);
		}
	}
	
	//8-bit system
	//00000101 (5)
	//00001011 (11)
	//5 & 11
	//00000001 (1)
	
	//16-bit system
	//00000000 11111111 (255)
	//00000001 10101100 (428)
	//00000000 10101100 <--- making sure not going over 255
}
