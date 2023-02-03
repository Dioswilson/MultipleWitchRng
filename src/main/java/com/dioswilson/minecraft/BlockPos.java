package com.dioswilson.minecraft;

import java.util.Objects;

public class BlockPos {
	
	private int x;
	private int y;
	private int z;
	
	public BlockPos(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}
	
	public boolean equals(Object object) {
		
		BlockPos blockPos = (BlockPos) object;
		
		if(this.getX() != blockPos.getX()) return false;
		if(this.getY() != blockPos.getY()) return false;
		if(this.getZ() != blockPos.getZ()) return false;
		
		return true;
		
    }

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}

	public String toString() {
		return "BlockPos[x:" + this.getX() + ", y:" + this.getY() + ", z:" + this.getZ() + "]";
	}
	
}