import java.io.*;
import java.util.*;

public class Data {
	
	private String path;
	private String fileName;
	
	public enum NormalizationTypesENUM {
		MAX_MIN, MAX_MIN_EQUALIZED; 
	}
	
	public Data(String path, String fileName){
		this.path = path;
		this.fileName = fileName;
	}
	
	public Data(){
		
	}
	
	public double[][] normalize(double[][] rawMatrix, NormalizationTypesENUM normType) {
		
		int rows = rawMatrix.length;
		int cols = rawMatrix[0].length;
		
		double[][] matrixNorm = new double[rows][cols];
		
		for (int cols_i = 0; cols_i < cols; cols_i++) {
			
			ArrayList<Double> listColumn = new ArrayList<Double>();
			
			for (int rows_j = 0; rows_j < rows; rows_j++) {
				listColumn.add( rawMatrix[rows_j][cols_i] );
			}
			
			double minColValue = Collections.min(listColumn);
			double maxColValue = Collections.max(listColumn);
			
			
			for (int rows_j = 0; rows_j < rows; rows_j++) {
				
				switch (normType) {

				case MAX_MIN:
					matrixNorm[rows_j][cols_i] = rawMatrix[rows_j][cols_i] / Math.abs( maxColValue );
					break;
			
				case MAX_MIN_EQUALIZED:
					//first column has BIAS: do not need to normalize
					if(cols_i >= 0 && (maxColValue - minColValue) != 0) { //change to cols_i > 0 if there is bias!!
						matrixNorm[rows_j][cols_i] = (rawMatrix[rows_j][cols_i] - minColValue) / (maxColValue - minColValue);
					}else {
						matrixNorm[rows_j][cols_i] = rawMatrix[rows_j][cols_i];
					}
					break;
				default:
					throw new IllegalArgumentException(normType
							+ " does not exist in NormalizationTypesENUM");
				}
				
			}
			
		}
		
		return matrixNorm;
		
	}
	
	public double[][] denormalize(double[][] rawMatrix, double[][] matrixNorm, NormalizationTypesENUM normType) {
		
		int rows = matrixNorm.length;
		int cols = matrixNorm[0].length;
		
		double[][] matrixDenorm = new double[rows][cols];
		
		for (int cols_i = 0; cols_i < cols; cols_i++) {
			
			ArrayList<Double> listColumn = new ArrayList<Double>();
			
			for (int rows_j = 0; rows_j < rows; rows_j++) {
				listColumn.add( rawMatrix[rows_j][cols_i] );
			}
			
			double minColValue = Collections.min(listColumn);
			double maxColValue = Collections.max(listColumn);
			
			for (int rows_j = 0; rows_j < rows; rows_j++) {
				
				switch (normType) {
				/*	linear normalization between [-1,1) ou (-1, 1]	*/
				case MAX_MIN:
					matrixDenorm[rows_j][cols_i] = matrixNorm[rows_j][cols_i] * Math.abs( maxColValue );
					break;
				/*	linear normalization between [0, 1]	*/
				case MAX_MIN_EQUALIZED:
					//first column has BIAS: do not need to denormalize
					if(cols_i > 0) {
						matrixDenorm[rows_j][cols_i] = (matrixNorm[rows_j][cols_i] * (maxColValue - minColValue)) + minColValue;
					} else {
						matrixDenorm[rows_j][cols_i] = matrixNorm[rows_j][cols_i];
					}
					break;
				default:
					throw new IllegalArgumentException(normType
							+ " does not exist in NormalizationTypesENUM");
				}
				
			}
			
		}
		
		return matrixDenorm;
		
	}

	public double[][] rawData2Matrix(Data r) throws IOException {

		String fullPath = defineAbsoluteFilePath( r );

		BufferedReader buffer = new BufferedReader(new FileReader(fullPath));
		
		try {
			StringBuilder builder = new StringBuilder();
			
			String line = buffer.readLine();
			
			int columns = line.split(",").length;
			int rows = 0; 
			while (line != null) {
				builder.append(line);
				builder.append(System.lineSeparator());
				line = buffer.readLine();
				rows++;
			}
			
			double matrix[][] = new double[rows][columns];
			String everything = builder.toString();
			
			Scanner scan = new Scanner( everything );
			rows = 0;
			while(scan.hasNextLine()){
				String[] strVector = scan.nextLine().split(",");
				for (int i = 0; i < strVector.length; i++) {
					matrix[rows][i] = Double.parseDouble(strVector[i]);
				}
				rows++;
			}
			scan.close();
			
			return matrix;

		} finally {
			buffer.close();
		}

	}
	
	private String defineAbsoluteFilePath(Data r) throws IOException {

		String absoluteFilePath = "";

		String workingDir = System.getProperty("user.dir");

		String OS = System.getProperty("os.name").toLowerCase();

		if (OS.indexOf("win") >= 0) {
			absoluteFilePath = workingDir + "\\" + r.getPath() + "\\" + r.getFileName();
		} else {
			absoluteFilePath = workingDir + "/" + r.getPath() + "/" + r.getFileName();
		}

		File file = new File(absoluteFilePath);

		if (file.exists()) {
			//System.out.println("File found!");
			//System.out.println(absoluteFilePath);
			//System.out.println();
		} else {
			System.err.println("File did not find...");
			System.out.println();
		}

		return absoluteFilePath;

	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
