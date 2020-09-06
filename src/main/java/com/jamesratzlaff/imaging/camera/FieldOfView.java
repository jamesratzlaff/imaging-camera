package com.jamesratzlaff.imaging.camera;

public class FieldOfView {

	/**
	 * The horizontal FOV in degrees
	 */
	private float horizontal;
	/**
	 * The vertical FOV in degrees
	 */
	private float vertical;
	
	
	public FieldOfView() {
		
	}
	
	public FieldOfView(float horizotnal, float vertical) {
		this.horizontal=horizotnal;
		this.vertical=vertical;
	}

	public float getHorizontal() {
		return horizontal;
	}

	public void setHorizontal(float horizontal) {
		this.horizontal = horizontal;
	}

	public float getVertical() {
		return vertical;
	}

	public void setVertical(float vertical) {
		this.vertical = vertical;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(horizontal);
		result = prime * result + Float.floatToIntBits(vertical);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FieldOfView other = (FieldOfView) obj;
		if (Float.floatToIntBits(horizontal) != Float.floatToIntBits(other.horizontal))
			return false;
		if (Float.floatToIntBits(vertical) != Float.floatToIntBits(other.vertical))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FieldOfView [horizontal=");
		builder.append(horizontal);
		builder.append(", vertical=");
		builder.append(vertical);
		builder.append("]");
		return builder.toString();
	}
	
	
	
	
	
	
	
}
