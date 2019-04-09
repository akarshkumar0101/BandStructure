import java.util.ArrayList;
import java.util.List;

import util.vector.Vector3D;

public class FibonacciStructure implements Structure {
	double hor_a = 1, ver_a = 2;

	@Override
	public Atom closestAtom(Vector3D<Double> point) {
		double x = point.x(), y = point.y(), z = point.z();

		int zbelow = (int) Math.floor(z);
		int zabove = zbelow + 1;

		int zi = (int) ((z + this.ver_a / 2) / this.ver_a);

		return null;
	}

	public Vector3D<Double> closestAtomf(Vector3D<Double> point) {
		double x = point.x(), y = point.y(), z = point.z();
		int zi = (int) ((z + this.ver_a / 2) / this.ver_a);
		double zp = zi * this.ver_a;

		point = Vector3D.createDoubleVector3D(x, y, zp);

		// int xi = (int) ((x + this.hor_a / 2) / this.hor_a);
		// int yi = (int) ((y + this.hor_a / 2) / this.hor_a);
		int xsi = (int) Math.floor(x / (2 * this.hor_a));
		double xs = xsi * (2 * this.hor_a);
		int ysi = (int) Math.floor(y / (2 * this.hor_a));
		double ys = ysi * (2 * this.hor_a);

		Vector3D<Double> sqr00 = Vector3D.createDoubleVector3D(xs, ys, zp);
		Vector3D<Double> nextx = Vector3D.createDoubleVector3D(this.hor_a, 0, 0);
		Vector3D<Double> nexty = Vector3D.createDoubleVector3D(0, this.hor_a, 0);
		Vector3D<Double> sqr10 = sqr00.add(nextx);
		Vector3D<Double> sqr20 = sqr10.add(nextx);
		Vector3D<Double> sqr21 = sqr20.add(nexty);
		Vector3D<Double> sqr22 = sqr21.add(nexty);
		Vector3D<Double> sqr12 = sqr22.sub(nextx);
		Vector3D<Double> sqr02 = sqr12.sub(nextx);
		Vector3D<Double> sqr01 = sqr02.sub(nexty);

		List<Vector3D<Double>> points = new ArrayList<>(8);
		points.add(sqr00);
		points.add(sqr10);
		points.add(sqr20);
		points.add(sqr21);
		points.add(sqr22);
		points.add(sqr12);
		points.add(sqr02);
		points.add(sqr01);

		return FibonacciStructure.closestPoint(point, points);
	}

	private static Vector3D<Double> closestPoint(Vector3D<Double> a, List<Vector3D<Double>> points) {
		double smallestDist = Double.MAX_VALUE;
		Vector3D<Double> closestPoint = null;
		for (Vector3D<Double> point : points) {
			double dist = a.dist(point);
			if (dist < smallestDist) {
				smallestDist = dist;
				closestPoint = point;
			}
		}
		return closestPoint;
	}

	@Override
	public List<Atom> getNeighbors(Atom atom) {
		return null;
	}

	private AtomType getAtomType(Vector3D<Double> atomLocation) {
		double x = atomLocation.x(), y = atomLocation.y(), z = atomLocation.z();
		int xi = (int) Math.floor(x / this.hor_a);
		int yi = (int) Math.floor(y / this.hor_a);

		if (Math.abs(xi % 2) == 1 ^ Math.abs(yi % 2) == 1)
			return AtomType.AS;
		else
			return AtomType.GA;
	}

	public static final double gr = (1 + Math.sqrt(5)) / 2;// golden ratio

	public static int fibonacci(int n) {

		double ans = Math.pow(FibonacciStructure.gr, n);

		ans = ans / Math.sqrt(5);
		ans += (double) 1 / 2;
		return (int) Math.floor(ans);
	}

	public static int inverseFib(int n) {
		double logTop = n - (double) 1 / 2;
		logTop *= Math.sqrt(5);
		double ans = Math.log(logTop) / Math.log(FibonacciStructure.gr);
		int inverse = (int) Math.floor(ans);
		if (FibonacciStructure.fibonacci(inverse) == n)
			return inverse;
		else
			return inverse + 1;
	}

	public static int bruteFibSum(int n) {
		int sum = 0;
		for (int i = 0; i < n; i++) {
			sum += FibonacciStructure.fibonacci(i);
		}
		return sum;
	}

	public static int smartFibSum(int n) {
		return FibonacciStructure.fibonacci(n + 1) - 1;
	}

	// binary:
	// xy
	// even for A, odd for B
	// x == 1 for shift x== 0 for no shift
	public int getLayerType() {

		return 0;
	}

}
