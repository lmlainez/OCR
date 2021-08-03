import java.io.FileReader;
import java.util.ArrayList;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.data.basic.BasicNeuralDataSet;

import au.com.bytecode.opencsv.CSVReader;



public class TrainingSetCreator {
	private ArrayList<DictionaryEntry> dictionary = null;
	public Dictionary dict= null;
	private ArrayList<double[]> InputPatternList=null;
	private ArrayList<double[]> OutputPatternList=null;
	
	public TrainingSetCreator(String dicName,String tsFileName) throws Exception{
		this.dict = new Dictionary();
		dict.load(dicName);
		this.loadTrainingSet(tsFileName);
	}

	public char getValueofIndex(int index){
		if(dictionary!= null){
			DictionaryEntry e = (DictionaryEntry) dictionary.get(index);
			return e.getValue();
		}
		else
			return ' ';
	}
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
	public MLDataSet toTrainingSet(){
		MLDataSet trainingSet = new BasicNeuralDataSet();
		if(InputPatternList!=null){
			for(int i=0;i<InputPatternList.size();i++){
				BasicMLData input= new BasicMLData((double[])InputPatternList.get(i));
				BasicMLData ideal= new BasicMLData((double[])OutputPatternList.get(i));
				trainingSet.add(input, ideal);
			}	
			return trainingSet;
		}
		else{
			System.out.println("No dictionary loaded, add a dictionary and retry ");
			return null;
		}
		
		
	}
	@SuppressWarnings("unchecked")
	public void loadTrainingSet(String dicName) throws Exception{
		//We create a converter image -> double[]
		ImageGrabber ig = new ImageGrabber();
		/*We need a reader for a cvs file with the configuration of the
		 * dictionary following this template
		 * No. of elements,n_rows,n_cols
		 * elem1, image1_path
		 * elem2,image2_path
		 * ...
		 * elem n,imageN_path
		 */
		CSVReader csvr = new CSVReader(new FileReader(dicName),',');
		InputPatternList=new ArrayList<double[]>();
		OutputPatternList=new ArrayList<double[]>();
		//First line -> Number of elements in the dictionary
		String[] rec=csvr.readNext();
		int numPatterns=Integer.parseInt(rec[0]);
		int nRows =Integer.parseInt(rec[1]);
		int nCols= Integer.parseInt(rec[2]);
		//Set properties for the ImageGrabber
		ig.setNumberOfRows(nRows);
		ig.setNumberOfColumns(nCols);
		double[] netOut = new double[numPatterns];
		for (int i=0;i<numPatterns;i++) netOut[i]=0.0;
		while ((rec = csvr.readNext()) != null) {
			char value = (rec[0].toCharArray())[0];
			double[] netIn=ig.read("DiccImg/"+rec[1]);
			netOut = dict.getIdealOf(value);
			InputPatternList.add(netIn);
			OutputPatternList.add(netOut.clone());
		}
		System.out.println("Num Muestras = "+InputPatternList.size());
	}
}
