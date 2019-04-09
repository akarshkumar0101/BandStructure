import ui.FrameWrapper;
import ui.GraphPanel;
import ui.UIPointSet;
import util.vector.Vector2D;
import util.vector.Vector3D;

public class HWProblemSolution {
	public static int N = 1000;

	public static Vector3D<Integer> crystalSize = Vector3D.createIntegerVector3D(10, 10, 10);
	public static double a0 = 1;

	public static double Ea = 16;
	public static double Va = 1;

	public static Vector3D<Double> zero = Vector3D.createDoubleVector3D(0, 0, 0);

	public static void main(String[] args) {
		Vector3D<Double> k = Vector3D.createDoubleVector3D(5 * HWProblemSolution.a0, 5 * HWProblemSolution.a0,
				5 * HWProblemSolution.a0);

		FrameWrapper<GraphPanel> frame = new FrameWrapper<>("E-k relation", 500, 500, true, false);
		GraphPanel gPanel = new GraphPanel();
		frame.setComponent(gPanel);

		UIPointSet pointSet = new UIPointSet();
		gPanel.pointSets.add(pointSet);

		int i = 0;
		for (double ki = -1; ki <= 0; ki += .1) {
			k = Vector3D.createDoubleVector3D(ki, ki, ki);
			ComplexNumber Ec = HWProblemSolution.Ek(k);
			double Er = Ec.re();
			double Ei = Ec.im();

			double Er2 = HWProblemSolution.EkActual(k);

			pointSet.add(Vector2D.createDoubleVector2D(i, Er));
			pointSet.add(Vector2D.createDoubleVector2D(i, Er2));
			pointSet.add(Vector2D.createDoubleVector2D(i++, Ei));
		}
		for (double ki = 0; ki <= 1; ki += .1) {
			k = Vector3D.createDoubleVector3D(ki, 0, 0);
			ComplexNumber Ec = HWProblemSolution.Ek(k);
			double Er = Ec.re();
			double Ei = Ec.im();
			
			double Er2 = HWProblemSolution.EkActual(k);
			pointSet.add(Vector2D.createDoubleVector2D(i, Er));
			pointSet.add(Vector2D.createDoubleVector2D(i, Er2));
			pointSet.add(Vector2D.createDoubleVector2D(i++, Ei));
		}

		frame.setVisible(true);
	}

	public static double EkActual(Vector3D<Double> k) {
		double E = 0;
		for (int i = -1; i <= 1; i += 2) {
			for (int j = -1; j <= 1; j += 2) {
				E += Math.cos(HWProblemSolution.a0 * (k.x() + i * k.y() + j * k.z()));
			}
		}
		E *= 2 * HWProblemSolution.Va;

		E += HWProblemSolution.Ea;
		return E;
	}

	public static ComplexNumber Ek(Vector3D<Double> k) {
		Vector3D<Double> r = Vector3D.createDoubleVector3D(5 * HWProblemSolution.a0, 5 * HWProblemSolution.a0,
				5 * HWProblemSolution.a0);

		ComplexNumber topSum = new ComplexNumber(0, 0);
		ComplexNumber botSum = new ComplexNumber(0, 0);

		for (int ix = 0; ix < HWProblemSolution.crystalSize.x(); ix++) {
			for (int iy = 0; iy < HWProblemSolution.crystalSize.y(); iy++) {
				for (int iz = 0; iz < HWProblemSolution.crystalSize.z(); iz++) {
					Vector3D<Double> rp = Vector3D.createDoubleVector3D(ix * HWProblemSolution.a0,
							iy * HWProblemSolution.a0, iz * HWProblemSolution.a0);
					Vector3D<Double> T = r.sub(rp);

					if (!(T.x() == 0 && T.y() == 0 && T.z() == 0)) {
						if (T.x() == 0 || T.y() == 0 || T.z() == 0) {
							continue;
						}
					} // run loop only 8 or 9 times. why??
					//running in center and corners only??

					double brahket = HWProblemSolution.AOFBraHKet(r, r.sub(T));
					double braket = HWProblemSolution.AOFBraKet(r, r.sub(T));
					double angle = -k.dot(T);

					if (brahket > 0 || braket > 0) {
						ComplexNumber topTerm = new ComplexNumber(brahket, angle, true);
						ComplexNumber botTerm = new ComplexNumber(braket, angle, true);
						topSum = topSum.plus(topTerm);
						botSum = botSum.plus(botTerm);

						System.out.println("r: " + r);
						System.out.println("k: " + k);
						System.out.println("T: " + T);
						System.out.println("braHket: " + brahket);
						System.out.println("braket: " + braket);

						System.out.println();
					}
				}
			}
		}

		return topSum.divides(botSum);
	}

	public static double AOFBraHKet(Vector3D<Double> r1, Vector3D<Double> r2) {
		if (r1.equals(r2))
			return HWProblemSolution.Ea;
		else if (r1.dist(r2) <= HWProblemSolution.a0 * Math.sqrt(3))
			return HWProblemSolution.Va;
		else
			return 0;
	}

	public static double AOFBraKet(Vector3D<Double> r1, Vector3D<Double> r2) {
		if (r1.equals(r2))
			return 1;
		else
			return 0;
	}
}
