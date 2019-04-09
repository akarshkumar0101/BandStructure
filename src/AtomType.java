
public enum AtomType {
	TYPE_1, TYPE_2, TYPE_3,

	GA, AL, AS;

	public static AtomType getAtom(int type) {
		return AtomType.values()[type];
	}
}
