import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.imageio.ImageIO;


public class ImageGrabber {
	private static  int patternRow = 10;
	private static  int patternCol = 8;
	private static final double positiveValue=1.0;
	private static final double negativeValue=0;
	
	
	public double[] read(String filename) throws IOException{
		BufferedImage img = ImageIO.read(new File(filename));
		int p;
		int maxX=0,minX=img.getWidth(),maxY=0,minY=img.getHeight();
		for(int j=0;j<img.getWidth();j++){
			for(int i=0;i<img.getHeight();i++){
				p=img.getRGB(j, i);
				/*Image is supposed to be black and white. If any RGB value > 0 is white
				 *Anyway we'll set out threshold in average grey RGB(127,127,127)
				 *We would only need to check one of the RGB components
				 */
				int bw = (p >> 16)&0xff;
				if(bw<127){
					if(j>maxX) maxX=j;
					if(j<minX) minX=j;
					if(i>maxY) maxY=i;
					if(i<minY) minY=i;
				}
			}
		}
		
		double m[][] = new double[patternCol][patternRow];
		/* Given that the negativeVale for the trainingSet can be different
		 * from 0, we need to initialize the whole matrix with this value.
		 * We will only change the matrix with positive values, not negative.
		 */
		for(int i=0;i<patternCol;i++){
			for (int j=0;j<patternRow;j++) m[i][j]=negativeValue;
		}
		double px;
		double py;
		double d;
		/*We transform from the origin image to a patternCol*patternRow image
		 * We apply this formula:
		 * NewX = (X -minX) * ( PatternCol / (maxX-minX))
		 * NeyY = (Y -minY) * ( PatternRow / (maxY-minY))
		 */
		px=0;
		if((d=maxX-minX +1)!=0)
			px= this.patternCol/d;
		else 
			px=1;
		if((d=maxY-minY+1)!=0)
			py= patternRow/d;
		else 
			py=1;
		int newX=0;
		int newY=0;
		/*
		 * We apply the formula and check that the obtained values
		 * are inside of the boundaries of the image
		 */
		for(int j=0;j<img.getWidth();j++){
			for(int i=0;i<img.getHeight();i++){
				p=img.getRGB(j, i);
				int bw = (p >> 16)&0xff;
				if(bw<127){
					newX = (int) ((j-minX) * px);
					//Boundary control for axis X
					newX= newX < 0 ? 0 : newX; 
					newX= newX >= patternCol ? (patternCol) : newX;  
					newY=  (int) ((i-minY) * py);
					//Boundary control for axis Y
					newY= newY < 0 ? 0 : newY; 
					newY= newY >= patternRow ? (patternRow) : newY;  
						m[newX][newY]=positiveValue;					
				}
			}
		}
		//We need to return a pattern with the content
		double outputPattern[] =new double[patternCol*patternRow];
		for(int i=0;i<patternRow;i++){
			for (int j=0;j<patternCol;j++) outputPattern[j*patternRow+i]=m[j][i];
		}
		return outputPattern;
	}
	public void setNumberOfRows(int nrows){
		patternRow=nrows;
	}
	public void setNumberOfColumns(int ncol){
		patternCol=ncol;
	}
	
}
	

