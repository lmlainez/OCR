
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.encog.Encog;
import org.encog.engine.network.activation.ActivationRamp;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.util.csv.CSVFormat;
import org.encog.util.simple.EncogUtility;
import org.encog.util.simple.TrainingSetUtil;



/*Explanation of the class
 * We have 2 files: Dictionary and Training Set
 * The dictionary file has a list of all the values that the OCR can recognise:
 * 		No of elements
 * 		elem_1
 * 		elem_2
 * 		...
 * 		elem_n
 * The TrainingSet file contains a list of pairs value-image, also, the number of different
 * elements and the sizes of each image
 * 		No_Elements Row Columns
 * 		Value1, image_1
 * 		Value2, image_2
 * 		...
 * 		ValueN, image_N
 * 
 *The TrainingSetCreator is the real data-center of the application, it contains the dictionary
 *and the trainingValue for the neural network.
 *
 *We will first load the dictionary. The dictionary is nothing but a list of pairs: Value-OutPut
 *Once we have the dictionary we will start creating the trainingSet for the training of the neuralNet
 *	*For each line of the TrainingSet File
 *		-Read the  line and  take its value-Image
 *		-Convert Image -> Net Input
 *		-Take the outPut associated to this value (using the dictionary) and create a pair Input-Output.
 *	*end For
 *We create the Structure of the netWork,train it and display results.
 */	
public class OCR {
	static String dictionaryName = "DiccImg/dicIndex.txt";
	static String tsFileName ="DiccImg/config.txt";	
	
	public static void main(final String args[]) throws Exception {
		//Obtention of the Training set using a input CSV file
		TrainingSetCreator tsc = new TrainingSetCreator(dictionaryName, tsFileName);
		//tsc.loadDictionary(InputFileName);
		MLDataSet trainingSet = tsc.toTrainingSet();
		// create a neural network, without using a factory
		BasicNetwork network = new BasicNetwork();
		//Input Layer
		network.addLayer(new BasicLayer(null,true,80));
		//Hidden Layer
		network.addLayer(new BasicLayer(new ActivationSigmoid(),true,30));
		//OutPut Layer
		network.addLayer(new BasicLayer(new ActivationRamp(),false,26));
		network.getStructure().finalizeStructure();
		//Initialization of weights using  Nguyen-Widrow
		network.reset();
		// train the neural network
		final ResilientPropagation train = new ResilientPropagation(network, trainingSet);
		int epoch = 1;
		do {
			train.iteration();
			//System.out.println("Epoch #" + epoch + " Error:" + train.getError());
			epoch++;
		} while(train.getError() > 0.01);

		// test the neural network
		System.out.println("Neural Network Results:");
		int good=0;
		int bad=0;
		int j=0;
		for(MLDataPair pair: trainingSet) {
			final MLData output = network.compute(pair.getInput());
			if(network.winner(pair.getInput())==(j%26)) good++;
			else bad++;
			j++;
			System.out.println("OutPut = " + output.toString() + "Winner = "+tsc.dict.getValueofIndex(network.winner(pair.getInput())));
		}
		Encog.getInstance().shutdown();
		System.out.println("Good = " +good);
		System.out.println("Bad = " +bad);
	}
}