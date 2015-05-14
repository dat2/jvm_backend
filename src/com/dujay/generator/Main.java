package com.dujay.generator;

import java.io.IOException;

import com.dujay.generator.codegen.IClassFileGenerator;
import com.dujay.generator.codegen.SimpleClassFileGenerator;

public class Main {

	public static void main(String[] args) throws IOException {
		IClassFileGenerator generator = new SimpleClassFileGenerator();
		generator.setFilename("J.class");
		
		// headers
		generator.magicNumber();
		generator.java8version();
		
		// write file out
		generator.writeToFile();
	}

}
