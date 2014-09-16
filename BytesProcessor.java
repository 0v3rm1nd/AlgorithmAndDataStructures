import java.io.*;
import java.math.BigInteger;

public class BytesProcessor {
	private final int N, W;
	private final byte[] a;
	// In this implementation I am using mergesort which requires an auxiliary
	// array for the merging
	private final byte[] auxiliary;
	// testing variable
	public static int count = 0;
	public static int count2 = 0;

	public BytesProcessor(int N, int W, Reader r) throws IOException {
		this.N = N;
		this.W = W;
		a = new byte[N * W];
		for (int i = 0; i < N * W; i++) {
			a[i] = (byte) readInt(r);
		}
		// will make an auxiliary array of length a
		auxiliary = new byte[a.length];
	}

	// merge implementation
	private void merge(int lo, int mid, int hi) {
		int i = lo, j = mid + 1;



		// copy to auxiliary array
		// every group of bytes
		for (int k = lo; k <= hi; k++) {
			//nlogn  
//			count++;
//			System.out.println(count);
//			 from each group, every byte will be copied
			for (int sub = 0; sub < W; sub++) {
				// will keep the blocks structure
				//(W*N)LogN complexity 
//				count++;
//				System.out.println(count);
				auxiliary[k * W + sub] = a[k * W + sub];
				// System.out.println(auxiliary[k * W + sub]);

			}
		}
		// merge back to a

		for (int k = lo; k <= hi; k++) {
			//NlogN complexity
//			count++;
//			System.out.println(count);

			if (i > mid) {
				copyToA(k, j++);
			} else if (j > hi) {
				copyToA(k, i++);
			} else if (less(j, i, auxiliary)) {
				copyToA(k, j++);
			} else {
				copyToA(k, i++);
			}

		}
	}

	private void copyToA(int aGp, int auxGp) {

		for (int i = 0; i < W; i++) {
			//(W*N)LogN complexity 
//			count++;
//			System.out.println(count);
			a[aGp * W + i] = auxiliary[auxGp * W + i];
		}
	}

	// will check the compared bytes values
	private boolean less(int gp1, int gp2, byte[] barray) {
		////////////////////////////////////////////////////////////////////////
		count2++;
//		System.out.println(count2);
		for (int i = 0; i < W; i++) {
			//nz kolko e ama mai worst case e w*nlogn
			count++;
//			System.out.println(count);
			byte gp1byte = barray[gp1 * W + i];
			byte gp2byte = barray[gp2 * W + i];
			int a = gp1byte & 255;
			int b = gp2byte & 255;
			if (a == b) {
				// System.out.println(gp1byte + " = " + gp2byte);
				continue;
			}
			if (a < b) {
				// System.out.println((gp1byte & 255) + " < " + (gp2byte &
				// 255));
			}
			return a < b;
		}
		return false;
	}

	// public sort method that will be called in the main
	public void mergesort() {
		sort(0, N - 1);
	}

	// mergesort main implementation
	private void sort(int lo, int hi) {
		if (hi <= lo)
			return;
		int mid = lo + (hi - lo) / 2;
		// recursive calls
		sort(lo, mid);
		sort(mid + 1, hi);
//		count++;
//		System.out.println(count);
		// merge call
		merge(lo, mid, hi);
	}

	/** Reads a digit sequence as an integer. */
	private static int readInt(Reader r) throws IOException {
		int c = r.read();
		while (c < '0' || c > '9') { // skip initial non-digits
			if (c < 0)
				throw new EOFException();
			c = r.read();
		}
		int v = 0;
		while (c >= '0' && c <= '9') {
			v = v * 10 + c - '0';
			c = r.read();
		}
		return v;
	}

	private BigInteger TWOFIVESIX = BigInteger.valueOf(256);

	public String numberToString(int i) {
		BigInteger v = BigInteger.valueOf(a[i * W] & 255);
		for (int j = 1; j < W; j++) {
			v = v.multiply(TWOFIVESIX).add(
					BigInteger.valueOf(a[i * W + j] & 255));
		}
		return v.toString();
	}

	public static void main(String[] args) throws IOException {
		String fileName = args[0];
		int N = Integer.parseInt(args[1]);
		int W = Integer.parseInt(args[2]);
		Reader r = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
		BytesProcessor bp = new BytesProcessor(N, W, r);
		r.close();
		// stopwatch to measure the time
		Stopwatch u = new Stopwatch();
		bp.mergesort();
		double sortTime = u.elapsedTime();
//		for (int i = 0; i < N; i++) {
//			System.out.print(bp.numberToString(i) + " ");
//		}
		System.out.println(sortTime);
		//nlogn
		System.out.println(count2);
		//no idea
		System.out.println(count);

	}
}