import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

import au.com.bytecode.opencsv.CSVReader;




import junit.framework.TestCase;

public class ImageGrabberTest extends TestCase {

	public void test1() throws IOException{
		ImageGrabber ig = new ImageGrabber();
		//No peta, tampoco  hace bien lo que tendria que hacer
		//Tendria que cojer la imagen y extenderla del todo, pero no
		double[] m = ig.read("DiccImg/O9.png");
		for(int i=0;i<10;i++){
			for (int j=0;j<8;j++) System.out.print(m[j*10+i]+" ");
			System.out.println();
		}
	}
	/*
	public void test2() throws Exception{
		ImageGrabber ig = new ImageGrabber();
		CSVReader csvr = new CSVReader(new FileReader("DiccImg/config.txt"),',');
		String[] rec=csvr.readNext();
		int numPatterns=Integer.parseInt(rec[0]);
		double[] netOut = new double[numPatterns];
		for (int i=0;i<numPatterns;i++) netOut[i]=0.0;
		System.out.println(numPatterns);
		ArrayList diccInputs = new ArrayList();
		for (int j=0;j<numPatterns;j++){
			try {
				rec=csvr.readNext();
				System.out.print(rec[0] + "  "+ rec[1] +" ");
				double[] out=ig.read("DiccImg/"+rec[1]);
				diccInputs.add(out);
				netOut[j]=1.0;
				System.out.println();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}*/

}
