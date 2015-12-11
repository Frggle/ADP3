package general;

public class Count {
	private int readAcc;
	private int writeAcc;
	private int leftRot;
	private int rightRot;
	
	public Count(int read, int write, int left, int right) {
		this.leftRot = left;
		this.rightRot = right;
		this.readAcc = read;
		this.writeAcc = write;
	}

	/**
	 * @return the readAcc
	 */
	public int getReadAcc() {
		return readAcc;
	}

	/**
	 * @param readAcc the readAcc to set
	 */
	public void setReadAcc(int readAcc) {
		this.readAcc = readAcc;
	}

	/**
	 * @return the writeAcc
	 */
	public int getWriteAcc() {
		return writeAcc;
	}

	/**
	 * @return the leftRot
	 */
	public int getLeftRot() {
		return leftRot;
	}

	/**
	 * @return the rightRot
	 */
	public int getRightRot() {
		return rightRot;
	}
}
