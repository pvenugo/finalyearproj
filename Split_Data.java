package com.interfaces;

import java.sql.PreparedStatement;

import javax.crypto.SecretKey;

public class Split_Data 
{
	byte[] file1;
	byte[] file2;
	byte[] file3;
	
	public Split_Data(byte[] local_file1,byte[] local_file2,byte[] local_file3) 
	{
		this.file1=local_file1;
		file2=local_file2;
				file3=local_file3;
	}

	public byte[] getFile1() {
		return file1;
	}

	public void setFile1(byte[] file1) {
		this.file1 = file1;
	}

	public byte[] getFile2() {
		return file2;
	}

	public void setFile2(byte[] file2) {
		this.file2 = file2;
	}

	public byte[] getFile3() {
		return file3;
	}

	public void setFile3(byte[] file3) {
		this.file3 = file3;
	}

	



}
