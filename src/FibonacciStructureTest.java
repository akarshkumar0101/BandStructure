import java.awt.Color;

import ui.FrameWrapper;
import ui.GraphPanel;
import ui.UIPointSet;
import util.vector.Vector2D;
import util.vector.Vector3D;

public class FibonacciStructureTest {

	private static final Structure structure = new FibonacciStructure();

	public static void printStr() {
		String a = "A";
		String b = "BA";
		for (int i = 0; i < 12; i++) {
			System.out.println(a);
			String c = a + b;
			a = b;
			b = c;
		}
	}

	public static void main(String[] args) {
		FibonacciStructureTest.printStr();
		for (int i = 0; i < 45; i++) {
			int fib = FibonacciStructure.fibonacci(i);
			int ip = FibonacciStructure.inverseFib(fib);
			// System.out.println(i + " -> " + fib + " -> " + ip);
		}

		FrameWrapper<GraphPanel> frame = new FrameWrapper<>("structure shown", 500, 500, true, false);
		GraphPanel gPanel = new GraphPanel();
		frame.setComponent(gPanel);

		UIPointSet redPointSet = new UIPointSet();
		redPointSet.setPointColor(Color.red);
		gPanel.pointSets.add(redPointSet);

		UIPointSet bluePointSet = new UIPointSet();
		bluePointSet.setPointColor(Color.blue);
		gPanel.pointSets.add(bluePointSet);

		UIPointSet greenPointSet = new UIPointSet();
		greenPointSet.setPointColor(Color.green);
		gPanel.pointSets.add(greenPointSet);

		Vector3D<Double> v = null;
		for (double x = -5; x < 5; x += .1) {
			for (double y = -5; y < 5; y += .1) {

				v = Vector3D.createDoubleVector3D(x, y, 0);
				Atom atom = FibonacciStructureTest.structure.closestAtom(v);
				Vector3D<Double> vp = atom.location;

				if (atom.atomType == AtomType.AS) {
					bluePointSet.add(Vector2D.createDoubleVector2D(vp.x(), vp.y()));
				} else if (atom.atomType == AtomType.GA) {
					redPointSet.add(Vector2D.createDoubleVector2D(vp.x(), vp.y()));
				} else if (atom.atomType == AtomType.AL) {
					greenPointSet.add(Vector2D.createDoubleVector2D(vp.x(), vp.y()));
				}

			}
		}

		frame.setVisible(true);
	}

}
