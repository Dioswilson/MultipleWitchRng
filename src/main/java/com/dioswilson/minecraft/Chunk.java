package com.dioswilson.minecraft;

public class Chunk {
	
	private int x;
	private int z;
	
	public Chunk(int x, int z) {
		this.x = x;
		this.z = z;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public boolean equals(Object object) {

		Chunk blockPos = (Chunk) object;

		if(this.x != blockPos.getX()) return false;
		if(this.z != blockPos.getZ()) return false;

		return true;

    }
	public int hashCode()
	{
		int i = 1664525 * this.x + 1013904223;
		int j = 1664525 * (this.z ^ -559038737) + 1013904223;
		return i ^ j;
	}


	public String toString() {
		return "Chunks[x:" + this.getX() + ", z:" + this.getZ() + "]";
	}

}