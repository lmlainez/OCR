import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.neural.data.basic.BasicNeuralDataSet;

import au.com.bytecode.opencsv.CSVReader;




public class Dictionary {
	public ArrayList dictionary = null;
	public int nElem=0;
	/* The dictionary file has a list of all the values that the OCR can recognise:
		 * 		No of elements
		 * 		elem_1
		 * 		elem_2
		 * 		...
		 * 		elem_n
		 * */
	public void load(String dicName) throws IOException{
		CSVReader csvr = new CSVReader(new FileReader(dicName),',');
		String[] rec=csvr.readNext();
		int numPatterns=Integer.parseInt(rec[0]);
		nElem=numPatterns;
		dictionary= new ArrayList();
		double[] netOut = new double[numPatterns];
		for (int i=0;i<numPatterns;i++) netOut[i]=0.0;
		for (int j=0;j<numPatterns;j++){
			try {
				DictionaryEntry newEntry = new DictionaryEntry();
				rec=csvr.readNext();
				char value = (rec[0].toCharArray())[0];
				netOut[j]=1.0;
				newEntry.setValue(value);
				newEntry.setIdeal(netOut.clone());	
				dictionary.add(newEntry);
				netOut[j]=0.0;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/*We use this function to display which neuron is the winner
	 * It is the translator Index_of_Winner_Neuron ->Letter
	 */
	@SuppressWarnings("unchecked")
	public char getValueofIndex(int index){
		if(dictionary!= null){
			DictionaryEntry e = (DictionaryEntry) dictionary.get(index);
			return e.getValue();
		}
		else
			return ' ';
	}
	/* Translator Value->OutPut
	 */
	public double[] getIdealOf(char elem){
		double[] ret=null;
		boolean found=false;
		if(dictionary!=null){
			int i=0;
			DictionaryEntry current = null;
			while((!found)&&(i<dictionary.size())){
				current=(DictionaryEntry) dictionary.get(i);
				if(current.getValue()==elem) found=true;
				else i++;
			}
			if(!found)
				return null;
			else
				return current.getIdeal();
		}
		return ret;
	}
}
