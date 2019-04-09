import java.util.ArrayList;
import java.util.List;

import ui.FrameWrapper;
import ui.GraphPanel;
import ui.UIPointSet;
import util.vector.Vector2D;
import util.vector.Vector3D;

public class HW3BSolution {

	private static final Structure structure = new Structure() {

		@Override
		public Atom closestAtom(Vector3D<Double> point) {
			Vector3D<Double> location = Vector3D.createDoubleVector3D(Math.floor(point.x() + 0.5), 0, 0);
			return new Atom(location, AtomType.getAtom(this.getAtomType(location)));
		}

		@Override
		public List<Atom> getNeighbors(Atom atom) {
			Vector3D<Double> atomLocation = atom.location;
			List<Atom> atoms = new ArrayList<>();
			Vector3D<Double> l1 = Vector3D.createDoubleVector3D(atomLocation.x() + 1, 0, 0);
			Vector3D<Double> l2 = Vector3D.createDoubleVector3D(atomLocation.x() - 1, 0, 0);
			Atom atom1 = new Atom(l1, AtomType.getAtom(this.getAtomType(l1)));
			Atom atom2 = new Atom(l2, AtomType.getAtom(this.getAtomType(l2)));
			atoms.add(atom1);
			atoms.add(atom2);
			return atoms;
		}

		private int getAtomType(Vector3D<Double> atomLocation) {
			int x = (int) Math.floor(atomLocation.x() + 0.5);
			return Math.abs(x % 2);
		}

	};
	public static final double a = 1;
	public static final double[] Ei = { 2, 1 };
	public static final double[][] Vij = { { 0, .01 }, { .01, 0 } };
	public static final double[][] DELTAij = { { .5, .5 }, { .5, .5 } };

	private static final Interaction interaction = new Interaction() {

		public final double[] Ei = { 2, 1 };
		public final double[][] Vij = { { 0, .01 }, { .01, 0 } };
		public final double[][] DELTAij = { { .5, .5 }, { .5, .5 } };

		@Override
		public double AOFBraKet(Atom atom1, Atom atom2, int orbital) {
			if (atom1.equals(atom2))
				return this.DELTAij[atom1.atomType.ordinal()][atom2.atomType.ordinal()];
			else
				return 0;
		}

		@Override
		public double AOFBraHKet(Atom atom1, Atom atom2, int orbital) {
			if (atom1.equals(atom2))
				return this.Ei[atom1.atomType.ordinal()]
						* this.DELTAij[atom1.atomType.ordinal()][atom2.atomType.ordinal()];
			else if (HW3BSolution.structure.getNeighbors(atom1).contains(atom2))
				return this.Vij[atom2.atomType.ordinal()][atom2.atomType.ordinal()];
			else
				return 0;
		}

	};

	public static void main(String[] args) {
		FrameWrapper<GraphPanel> frame = new FrameWrapper<>("E-k relation", 500, 500, true, false);
		GraphPanel gPanel = new GraphPanel();
		frame.setComponent(gPanel);

		UIPointSet pointSet = new UIPointSet();
		gPanel.pointSets.add(pointSet);

		double i = 0;
		Vector3D<Double> k = null;
		for (i = -5; i < 5; i += .1) {
			k = Vector3D.createDoubleVector3D(i, 0, 0);
			ComplexNumber Ec = HW3BSolution.Ek(k, HW3BSolution.structure, HW3BSolution.interaction);
			double Er = Ec.re();
			double Ei = Ec.im();

			double ErActual = HW3BSolution.EkAnswer(k);

			pointSet.add(Vector2D.createDoubleVector2D(i, Er));
			pointSet.add(Vector2D.createDoubleVector2D(i, Ei));
			pointSet.add(Vector2D.createDoubleVector2D(i, ErActual));
		}

		frame.setVisible(true);
	}

	// hard coded answer to compare to
	public static double EkAnswer(Vector3D<Double> k) {
		double sqrt = k.x() * HW3BSolution.a;
		sqrt = Math.cos(sqrt);
		sqrt = Math.pow(sqrt, 2);
		sqrt *= Math.pow(HW3BSolution.Vij[0][1], 2);
		sqrt *= 16;

		sqrt += Math.pow(HW3BSolution.Ei[0] - HW3BSolution.Ei[1], 2);

		sqrt = Math.sqrt(sqrt);

		double a = HW3BSolution.Ei[0] + HW3BSolution.Ei[1];

		return (a - sqrt) / 2;
	}

	public static ComplexNumber Ek(Vector3D<Double> k, Structure structure, Interaction interaction) {
		Vector3D<Double> r = Vector3D.createDoubleVector3D(5, 0, 0);

		ComplexNumber topSum = new ComplexNumber(0, 0);
		ComplexNumber botSum = new ComplexNumber(0, 0);

		for (int ix = -10; ix < 10; ix++) {
			Vector3D<Double> rp = Vector3D.createDoubleVector3D(ix, 0, 0);
			Vector3D<Double> T = r.sub(rp);

			/*
			 * if (!(T.x() == 0 && T.y() == 0 && T.z() == 0)) { if (T.x() == 0 || T.y() == 0
			 * || T.z() == 0) { continue; } }
			 */
			// run loop only 8 or 9 times. why??
			// running in center and corners only??

			Atom atom1 = structure.closestAtom(r);
			Atom atom2 = structure.closestAtom(r.sub(T));

			double brahket = interaction.AOFBraHKet(atom1, atom2, 0);
			double braket = interaction.AOFBraKet(atom1, atom2, 0);
			double angle = -k.dot(T);

			if (brahket > 0 || braket > 0) {
				ComplexNumber topTerm = new ComplexNumber(brahket, angle, true);
				ComplexNumber botTerm = new ComplexNumber(braket, angle, true);
				topSum = topSum.plus(topTerm);
				botSum = botSum.plus(botTerm);

				/*
				 * System.out.println("r: " + r); System.out.println("k: " + k);
				 * System.out.println("T: " + T); System.out.println("braHket: " + brahket);
				 * System.out.println("braket: " + braket);
				 *
				 * System.out.println();
				 */
			}
		}

		return topSum.divides(botSum);
	}

}
