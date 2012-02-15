package com.primaryt.classdroid.bo;

public class Pupil {
	private long id;
	private String name;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Pupil) {
			Pupil another = (Pupil) o;
			if (another.getId() == id) {
				return true;
			} else {
				return false;
			}
		}
		return super.equals(o);
	}
}
