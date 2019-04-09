import java.util.List;

import util.vector.Vector3D;

public interface Structure {

	public abstract Atom closestAtom(Vector3D<Double> point);

	public abstract List<Atom> getNeighbors(Atom atom);
}
