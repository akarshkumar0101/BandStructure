public interface Interaction {

	// atomic orbital function bra h ket
	public abstract double AOFBraHKet(Atom atom1, Atom atom2, int orbital);

	// atomic orbital function bra ket
	public abstract double AOFBraKet(Atom atom1, Atom atom2, int orbital);

}
