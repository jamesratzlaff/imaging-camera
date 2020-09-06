package com.jamesratzlaff.imaging.camera;

import java.util.ArrayList;
import java.util.List;

public class Camera {

	
	private List<Resolution> availableResolutions;
	private FieldOfView fieldOfView;
	private float focalDistanceMm;
	
	
	
	
	public void addResolution(int width, int height) {
		addResolution(new Resolution(width, height));
	}
	
	public void addResolution(Resolution resolution) {
		if(resolution!=null) {
			if(!getAvailableResolutions().contains(resolution)) {
				getAvailableResolutions().add(resolution);
			}
		}
	}
	
	public List<Resolution> getAvailableResolutions() {
		if(this.availableResolutions==null) {
			this.availableResolutions=new ArrayList<Resolution>();
		}
		return availableResolutions;
	}
	public void setAvailableResolutions(List<Resolution> availableResolutions) {
		this.availableResolutions = availableResolutions;
	}
	public FieldOfView getFieldOfView() {
		return fieldOfView;
	}
	
	public void setFieldOfView(float horizontal, float vertical) {
		setFieldOfView(new FieldOfView(horizontal, vertical));
	}
	
	public void setFieldOfView(FieldOfView fieldOfView) {
		this.fieldOfView = fieldOfView;
	}
	public float getFocalDistanceMm() {
		return focalDistanceMm;
	}
	public void setFocalDistanceMm(float focalDistanceMm) {
		this.focalDistanceMm = focalDistanceMm;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((availableResolutions == null) ? 0 : availableResolutions.hashCode());
		result = prime * result + ((fieldOfView == null) ? 0 : fieldOfView.hashCode());
		result = prime * result + Float.floatToIntBits(focalDistanceMm);
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
		Camera other = (Camera) obj;
		if (availableResolutions == null) {
			if (other.availableResolutions != null)
				return false;
		} else if (!availableResolutions.equals(other.availableResolutions))
			return false;
		if (fieldOfView == null) {
			if (other.fieldOfView != null)
				return false;
		} else if (!fieldOfView.equals(other.fieldOfView))
			return false;
		if (Float.floatToIntBits(focalDistanceMm) != Float.floatToIntBits(other.focalDistanceMm))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Camera [availableResolutions=");
		builder.append(availableResolutions);
		builder.append(", fieldOfView=");
		builder.append(fieldOfView);
		builder.append(", focalDistanceMm=");
		builder.append(focalDistanceMm);
		builder.append("]");
		return builder.toString();
	}
	
	
	
	
	
	
	
}
