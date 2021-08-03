import java.util.ArrayList;

import junit.framework.TestCase;


public class DictionaryTest extends TestCase {
	public void test() throws Exception{
		Dictionary dic = new Dictionary();
		//dic.loadDictionary("DiccImg/config.txt");
		//dic.toTrainingSet();
		
		Dictionary dic2=new Dictionary();
		dic.load("DiccImg/dicIndex.txt");
		for(int i=0;i<dic.nElem;i++){
			System.out.print("Valor " + dic.getValueofIndex(i) +" out = ");
			double[] out = dic.getIdealOf(dic.getValueofIndex(i));
			for(int j=0; j<out.length;j++) System.out.print(out[j] +" ");
			System.out.println();
		}
		
	}
}
