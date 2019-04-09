import util.vector.Vector3D;

public class Atom {
	Vector3D<Double> location;
	AtomType atomType;

	public Atom(Vector3D<Double> location, AtomType atomType) {
		this.location = location;
		this.atomType = atomType;
	}

	@Override
	public boolean equals(Object another) {
		if (!(another instanceof Atom))
			return false;
		Atom atom = (Atom) another;
		return this.hasSameLocation(atom) && this.atomType == atom.atomType;
	}

	public boolean hasSameLocation(Atom another) {
		return this.location.equals(another.location);
	}
}
