import java.io.*;
import java.text.ParseException;
import java.util.Arrays;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class NetFunctions {
	
	
	//prints the weights of each neuron for each layer, One hidden layer network
	public static void printNet(NeuralNet net){
		
		//get number of neurons in each layer
		int inputNeurons = net.getInputLayer().getNumberOfNeuronsInLayer();
		int hiddenNeurons = net.getHiddenLayer().getNumberOfNeuronsInLayer();
		int outputNeurons = net.getOutputLayer().getNumberOfNeuronsInLayer();
		
		//create 2 matrices (one for each layer)
		double[][] inputToHiddenWeights = new double [hiddenNeurons][inputNeurons];
		double[][] hiddenToOutputWeights = new double [outputNeurons][hiddenNeurons];
		
		//set the matrices with the NN weights for each layer
		inputToHiddenWeights = net.getHiddenLayer().getWeightsIn();		
		hiddenToOutputWeights = net.getOutputLayer().getWeightsIn();
		
		//print out the two matrices
		System.out.println("\nInput to Hidden Wts: ");
		print2D(inputToHiddenWeights);
		
		System.out.println("\nHidden to Output Wts: ");
		print2D(hiddenToOutputWeights);

	} // end printNet
	
	
	//prints the Flags of each neuron for each layer, One hidden layer network
	public static void printNetFlags(NeuralNet net){
		
		//get number of neurons in each layer
		int inputNeurons = net.getInputLayer().getNumberOfNeuronsInLayer();
		int hiddenNeurons = net.getHiddenLayer().getNumberOfNeuronsInLayer();
		int outputNeurons = net.getOutputLayer().getNumberOfNeuronsInLayer();
		
		//create 2 matrices (one for each layer)
		boolean[][] inputToHiddenFlags = new boolean [hiddenNeurons][inputNeurons];
		boolean[][] hiddenToOutputFlags = new boolean [outputNeurons][hiddenNeurons];
		
		//set the matrices with the NN flags for each layer
		inputToHiddenFlags = net.getHiddenLayer().getFlagsIn();		
		hiddenToOutputFlags = net.getOutputLayer().getFlagsIn();
		
		//print out the two matrices
		System.out.println("\nInput to Hidden Flags: ");
		booleanPrint2D(inputToHiddenFlags);
		
		System.out.println("\nHidden to Output Flags: ");
		booleanPrint2D(hiddenToOutputFlags);

	} // end printNetFlags
	
	//print out accuracy fot the NN
	public static double calcAccuracy (double[][] netOutputValues, double[][] target) { 
		
		double rightPrediction = 0;
		double wrongPrediction;
		double count = 0;
		
		//for each row find the number of correct predictions 
		for (int row = 0; row < netOutputValues.length; row++) {				
			count = 0;
			
			for(int col = 0; col < netOutputValues[row].length; col++) {
				//if all predictions right then count +1 else no count
				//System.out.println("target[row_i][col_i]: " + target[row_i][col_i]);
				//System.out.println("netOutputValues[row_i][col_i]: " + netOutputValues[row_i][col_i]);
				
				if (target[row][col] == 0 && netOutputValues[row][col] < 0.5) {
					count++;
				} else if(target[row][col] == 1 && netOutputValues[row][col] >= 0.5) {
					count++;
				}
				
				//if all predictions in the column are correct then that is a right prediction
				if(count == netOutputValues[0].length) {
					rightPrediction++;
				}			
			}
		}
		
		//get the number of wrong predictions
		wrongPrediction = (netOutputValues.length - rightPrediction);
			
		//print out the nmuber of right and wrong predictions 
		System.out.println("rightPrediction: " + rightPrediction);
		System.out.println("numWrong: " + wrongPrediction + "\n");
		
		//get accuracy by dividing the number of right predictions by number of rows
		double accuracy = 100 * (rightPrediction / netOutputValues.length);
		
		//print accuracy 
		System.out.println("Accuracy: " + accuracy + "%");
		System.out.println("----------------------------\n");
		
		return accuracy;
		
	}
	
	//save net to JSON file
	@SuppressWarnings("unchecked")
	public static void saveJSONFile(NeuralNet net1, Data fileName) throws IOException {
		
		//save inputNeurons
		JSONArray inputNeurons = new JSONArray();
		inputNeurons.add(net1.getInputLayer().getNumberOfNeuronsInLayer());
		
		//save hiddenNeurons
		JSONArray hiddenNeurons = new JSONArray();
		hiddenNeurons.add(net1.getHiddenLayer().getNumberOfNeuronsInLayer());
		
		//save outputNeurons
		JSONArray outputNeurons = new JSONArray();
		outputNeurons.add(net1.getOutputLayer().getNumberOfNeuronsInLayer());
	
		//save input to hidden weights
		JSONArray inputToHiddenWeights = new JSONArray();
		
		//save input to hidden flags
		JSONArray inputToHiddenFlags = new JSONArray();
		
		for (int i = 0; i < net1.getHiddenLayer().getNumberOfNeuronsInLayer(); i++) {
			inputToHiddenWeights.add(Arrays.toString(net1.getHiddenLayer().getWeightsIn()[i]));
			inputToHiddenFlags.add(Arrays.toString(net1.getHiddenLayer().getFlagsIn()[i]));
		}
		
		//save hidden to output weights
		JSONArray hiddenToOutputWeights = new JSONArray();
		
		//save hidden to output flags
		JSONArray hiddenToOutputFlags = new JSONArray();
		
		for (int j = 0; j < net1.getOutputLayer().getNumberOfNeuronsInLayer(); j++) {
			hiddenToOutputWeights.add(Arrays.toString(net1.getOutputLayer().getWeightsIn()[j]));
			hiddenToOutputFlags.add(Arrays.toString(net1.getOutputLayer().getFlagsIn()[j]));
		}
		
		//save net accuracy		
		JSONArray netAccuracy = new JSONArray();
		netAccuracy.add(net1.getAccuracy());

		//add all to saveNet
		JSONObject saveNet = new JSONObject();
		saveNet.put("inputToHiddenWeights", inputToHiddenWeights);
		saveNet.put("inputToHiddenFlags", inputToHiddenFlags);
		saveNet.put("hiddenToOutputWeights", hiddenToOutputWeights);
		saveNet.put("hiddenToOutputFlags", hiddenToOutputFlags);
		saveNet.put("netAccuracy", netAccuracy);
		
		saveNet.put("inputNeurons", inputNeurons);
		saveNet.put("hiddenNeurons", hiddenNeurons);
		saveNet.put("outputNeurons", outputNeurons);
		
		//add JSON object to a file 
		String newFileName = NetFunctions.filePath(fileName);
		try	(FileWriter file = new FileWriter(newFileName)){
			file.write(saveNet.toJSONString()); 
			file.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}
	
	//read net from JSON file
	public static NeuralNet readJSONFile( Data fileName) throws IOException, ParseException, org.json.simple.parser.ParseException {
		JSONParser parser = new JSONParser();

		String newFileName = NetFunctions.filePath(fileName);
		Object obj = parser.parse(new FileReader(newFileName));
		JSONObject jsonObject = (JSONObject)obj;
		
		JSONArray inputToHiddenWeights = (JSONArray)jsonObject.get("inputToHiddenWeights");
		JSONArray hiddenToOutputWeights = (JSONArray)jsonObject.get("hiddenToOutputWeights");
		
		JSONArray inputToHiddenFlags = (JSONArray)jsonObject.get("inputToHiddenFlags");
		JSONArray hiddenToOutputFlags = (JSONArray)jsonObject.get("hiddenToOutputFlags");
		
		//get number of inputNeurons
		JSONArray numInputNeuron = (JSONArray)jsonObject.get("inputNeurons");
		String inSize = numInputNeuron.get(0).toString();
		int inputNeuronSize = Integer.parseInt(inSize);
		
		//get number of hiddenNeurons
		JSONArray numHiddenNeuron = (JSONArray)jsonObject.get("hiddenNeurons");
		String hidSize = numHiddenNeuron.get(0).toString();
		int hiddenNeuronSize = Integer.parseInt(hidSize);
		
		//get number of outputNeurons
		JSONArray numOutputNeuron = (JSONArray)jsonObject.get("outputNeurons");
		String outSize = numOutputNeuron.get(0).toString();
		int outputNeuronSize = Integer.parseInt(outSize);
		
		//get the inputLayer to hiddenLayer weights
		double hiddenWeightArray[][] = new double[hiddenNeuronSize][inputNeuronSize];
		
		for (int i = 0; i < inputToHiddenWeights.size(); i++) {
			String in = inputToHiddenWeights.get(i).toString();
		 
			 String[] stIn = in.replace("[", "").replace("]", "").split(", ");
			 
			 for (int j = 0; j < inputNeuronSize; j++) {
				 hiddenWeightArray[i][j] = Double.parseDouble(stIn[j]);
			 }
		}
		
		//get the hiddenLayer to outputLayer weights
		double outputWeightArray[][] = new double[outputNeuronSize][hiddenNeuronSize];
		
		for (int i = 0; i < hiddenToOutputWeights.size(); i++) {
			String in = hiddenToOutputWeights.get(i).toString();
		 
			 String[] stIn = in.replace("[", "").replace("]", "").split(", ");
			 
			 for (int j = 0; j < hiddenNeuronSize; j++) {
				 outputWeightArray[i][j] = Double.parseDouble(stIn[j]);
			 }
		}
		
		//get the inputLayer to hiddenLayer flags
		boolean hiddenFlagArray[][] = new boolean[hiddenNeuronSize][inputNeuronSize];
		
		for (int i = 0; i < inputToHiddenFlags.size(); i++) {
			String in = inputToHiddenFlags.get(i).toString();
		 
			 String[] stIn = in.replace("[", "").replace("]", "").split(", ");
			 
			 for (int j = 0; j < inputNeuronSize; j++) {
				 hiddenFlagArray[i][j] =  Boolean.parseBoolean(stIn[j]);
			 }
		}
		
		//get the hiddenLayer to outputLayer flags
		boolean outputFlagArray[][] = new boolean[outputNeuronSize][hiddenNeuronSize];
		
		for (int i = 0; i < hiddenToOutputFlags.size(); i++) {
			String in = hiddenToOutputFlags.get(i).toString();
		 
			 String[] stIn = in.replace("[", "").replace("]", "").split(", ");
			 
			 for (int j = 0; j < hiddenNeuronSize; j++) {
				 outputFlagArray[i][j] = Boolean.parseBoolean(stIn[j]);
			 }
		}
		
		//get accuracy
		JSONArray accuracy = (JSONArray)jsonObject.get("netAccuracy");
        String newStr = accuracy.get(0).toString();
        double netAccuracy = Double.parseDouble(newStr);
		
        //create new inputLayer
        InputLayer newInputLayer = new InputLayer();
        newInputLayer.setNumberOfNeuronsInLayer(inputNeuronSize);
        
        //create new hiddenLayer
        HiddenLayer newHiddenLayer = new HiddenLayer();
        newHiddenLayer.setWeightsIn(hiddenWeightArray);
        newHiddenLayer.setFlagsIn(hiddenFlagArray);
        newHiddenLayer.setNumberOfNeuronsInLayer(hiddenNeuronSize); 
        
        //create new outputLayer
        OutputLayer newOutputLayer = new OutputLayer();
        newOutputLayer.setWeightsIn(outputWeightArray);
        newOutputLayer.setFlagsIn(outputFlagArray);
        newOutputLayer.setNumberOfNeuronsInLayer(outputNeuronSize); 
        
        //create the return net
        NeuralNet returnNet = new NeuralNet();
        returnNet.setInputLayer(newInputLayer);
        returnNet.setHiddenLayer(newHiddenLayer);
        returnNet.setOutputLayer(newOutputLayer);
        returnNet.setAccuracy(netAccuracy);
        
        return returnNet;
        
	} //end readFile
	
	//find file
	private static String filePath(Data r) throws IOException {

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
	
	//remove weak neurons
	public static NeuralNet removeWeakNeuron(NeuralNet nn, int numRemove) {
		
		//set number of neurons in each layer	
		int newInputNeurons = nn.getInputLayer().getNumberOfNeuronsInLayer();
		int newHiddenNeurons = nn.getHiddenLayer().getNumberOfNeuronsInLayer() - 1;	
		int newOutputNeurons = nn.getOutputLayer().getNumberOfNeuronsInLayer();

		//get the input layer to hidden layer weights and flags
		int rows = nn.getHiddenLayer().getWeightsIn().length;
		int cols = nn.getHiddenLayer().getWeightsIn()[0].length;
		
		boolean[][] inputToHiddenFlags = new boolean [rows][cols];
		double[][] inputToHiddenWeights = new double [rows][cols];
		
		inputToHiddenWeights = nn.getHiddenLayer().getWeightsIn();
		inputToHiddenFlags = nn.getHiddenLayer().getFlagsIn();
		
		//set the input layer to hidden layer weight sum for each neuron of the hidden layer
		double[][] weightSum = new double [rows][1];
		
		double sum = 0;
	    for (int i = 0; i < rows; ++i) {
	        for (int j = 0; j < cols; ++j) {
	            sum = sum + Math.abs(inputToHiddenWeights[i][j]);
	            
	        }
	        weightSum[i][0] = sum;
	        sum = 0;
	    }
	    
	    //find neuron with lowest weight sum (the weak neuron)
	    double minWeightSum = weightSum[0][0];
	    int minWeightSumNeuron = 0;
	    for (int i = 0; i < rows; i++) {
	    	if (minWeightSum > weightSum[i][0]) {
	    		minWeightSum = weightSum[i][0];
	    		minWeightSumNeuron = i;
	    	}
	    	
	    }
	    
	    //find neuron with the most deleted links
	    int linksDeleted;
	    int weakNeuron = 0;
	    int previousLinksDeleted = 0;
	    int temp = 0;
	    
	    for (int i = 0; i < rows; ++i) {
	    	linksDeleted = 0;
	        for (int j = 0; j < cols; ++j) {
	        	if (inputToHiddenFlags[i][j] == true) {
	        		linksDeleted++;
	        		temp = i;
	        	}
	            
	        }
	        
        	if (linksDeleted > previousLinksDeleted) {
        		weakNeuron = temp;
        		previousLinksDeleted = linksDeleted;
        	}

	    }
	    
	    //check if a link in the first col of any row has been deleted removed
	    boolean linkRemoved = false;
	    for (int i = 0; i < rows; ++i) {
	        if (inputToHiddenFlags[i][0] == true) {
	        		linkRemoved = true;
	        }
	    }

	    //if there are no weak links left the neuron with the lowest weight sum is then removed
	    if (weakNeuron == 0 && linkRemoved != true) {
	    	weakNeuron = minWeightSumNeuron;
	    }
		
	    //System.out.println("\nWeak Neuron is neuron number: " + weakNeuron);
	    //System.out.println("minWeightSumNeuron: " + minWeightSumNeuron);
	    
	    
	    //create a new weight array with one less neuron (weak neuron removed)
	    double[][] newInputToHiddenWeights = new double [rows - 1][cols];
	    boolean[][] newInputToHiddenFlags = new boolean [rows - 1][cols];
	    
	    int r = 0;
	    
	    for (int i = 0; i < rows; i++) {	
	    	if (i != weakNeuron) {
	    		for(int j = 0; j < cols; j++) {
	    			newInputToHiddenWeights[r][j] = inputToHiddenWeights[i][j];	
	    			newInputToHiddenFlags[r][j] = inputToHiddenFlags[i][j];	
	    		}
	    		r++;
	    	} 
	    	
	    }
	    
	    //System.out.println("\nNew Hidden to Output Wts: ");
	    //print2D(newHiddenToOutputWeights);
	    
	    //reset hidden layer to output layer output weights and flags
	    rows = nn.getOutputLayer().getWeightsIn().length;
	    cols = nn.getOutputLayer().getWeightsIn()[0].length;
	    
	    boolean[][] hiddenToOutputFlags = new boolean [rows][cols];
	    double[][] hiddenToOutputWeights = new double [rows][cols];
	    
	    hiddenToOutputWeights = nn.getOutputLayer().getWeightsIn();
	    hiddenToOutputFlags = nn.getOutputLayer().getFlagsIn();
	    
	    double[][] newHiddenToOutputWeights = new double [rows][cols - 1];
	    boolean[][] newHiddenToOutputFlags = new boolean [rows][cols - 1];
	    
	    int c;
	    
	    for (int i = 0; i < rows; i++) {	
	    	c = 0;
	    	for(int j = 0; j < cols; j++) {
	    		if(j != weakNeuron) {
		    		newHiddenToOutputWeights[i][c] = hiddenToOutputWeights[i][j];
		    		newHiddenToOutputFlags[i][c] = hiddenToOutputFlags[i][j];
		    		c++;
	    		}
	    	}
	    	
	    }
	    
	    //System.out.println("\nNew Input to Hidden Wts: ");
	    //print2D(newInputHiddenWeights);
	    
	    //set new layers of the neural net
        InputLayer newInputLayer = new InputLayer();
        newInputLayer.setNumberOfNeuronsInLayer(newInputNeurons);
        
        HiddenLayer newHiddenLayer = new HiddenLayer();
        newHiddenLayer.setNumberOfNeuronsInLayer(newHiddenNeurons);
        newHiddenLayer.setFlagsIn(newInputToHiddenFlags);
        newHiddenLayer.setWeightsIn(newInputToHiddenWeights);
        
        OutputLayer newOutputLayer = new OutputLayer();
        newOutputLayer.setNumberOfNeuronsInLayer(newOutputNeurons);
        newOutputLayer.setFlagsIn(newHiddenToOutputFlags);
        newOutputLayer.setWeightsIn(newHiddenToOutputWeights);

		nn.setInputLayer(newInputLayer);
		nn.setHiddenLayer(newHiddenLayer);
		nn.setOutputLayer(newOutputLayer);
		
		//one weak neuron removed, if the total number of neurons has not been removed call removeWeakNeuron method again
		numRemove--;
		
		if (numRemove != 0) {
			removeWeakNeuron(nn, numRemove);
		}
	    
		return nn;
	}
	
	
	//remove weak neurons
	public static NeuralNet removeWeakNeuronOLD(NeuralNet nn, int numRemove) {
		
		//set number of neurons in each layer	
		int newInputNeurons = nn.getInputLayer().getNumberOfNeuronsInLayer();
		int newHiddenNeurons = nn.getHiddenLayer().getNumberOfNeuronsInLayer() - 1;	
		int newOutputNeurons = nn.getOutputLayer().getNumberOfNeuronsInLayer();

		//get the hidden layer to output layer weights and flags
		int rows = nn.getOutputLayer().getWeightsIn().length;
		int cols = nn.getOutputLayer().getWeightsIn()[0].length;
		
		boolean[][] hiddentoOutputFlags = new boolean [rows][cols];
		double[][] hiddenToOutputWeights = new double [rows][cols];
		
		hiddenToOutputWeights = nn.getOutputLayer().getWeightsIn();
		hiddentoOutputFlags = nn.getOutputLayer().getFlagsIn();
		
		//set the hidden layer to output layer weight sum for each neuron of the hidden layer
		double[][] weightSum = new double [1][cols];
		
		double sum = 0;
	    for (int i = 0; i < cols; ++i) {
	        for (int j = 0; j < rows; ++j) {
	            sum = sum + Math.abs(hiddenToOutputWeights[j][i]);
	            
	        }
	        weightSum[0][i] = sum;
	        sum = 0;
	    }
		
	    //System.out.println("Sum of weights: ");
	    //print2D(weightSum);
	    
	    //find neuron with lowest output weight sum (the weak neuron)
	    double minWeightSum = weightSum[0][0];
	    int minWeightSumNeuron = 0;
	    for (int i = 0; i < cols; i++) {
	    	if (minWeightSum > weightSum[0][i]) {
	    		minWeightSum = weightSum[0][i];
	    		minWeightSumNeuron = i;
	    	}
	    	
	    }
	    
	    //find neuron with the most deleted links
	    int linksDeleted;
	    int weakNeuron = 0;
	    int previousLinksDeleted = 0;
	    
	    for (int i = 0; i < rows; ++i) {
	    	linksDeleted = 0;
	        for (int j = 0; j < cols; ++j) {
	        	if (hiddentoOutputFlags[i][j] == true) {
	        		linksDeleted++;
	        		
	        	}
	        	
	        	if (linksDeleted > previousLinksDeleted) {
	        		weakNeuron = j;
	        		previousLinksDeleted = linksDeleted;
	        	}
	            
	        }

	    }
	    
	    //check if a link in the first col of any row has been deleted removed
	    boolean linkRemoved = false;
	    for (int i = 0; i < rows; ++i) {
	        if (hiddentoOutputFlags[i][0] == true) {
	        		linkRemoved = true;
	        }
	    }

	    //if there are no weak links left the neuron with the lowest weight sum is then removed
	    if (weakNeuron == 0 && linkRemoved != true) {
	    	weakNeuron = minWeightSumNeuron;
	    }
		
	    //System.out.println("\nWeak Neuron is neuron number: " + weakNeuron);
	    //System.out.println("minWeightSumNeuron: " + minWeightSumNeuron);
	    
	    
	    //create a new weight array with one less neuron (weak neuron removed)
	    double[][] newHiddenToOutputWeights = new double [rows][cols - 1];
	    boolean[][] newHiddenToOutputFlags = new boolean [rows][cols - 1];
	    
	    int c;
	    
	    for (int i = 0; i < rows; i++) {	
	    	c = 0;
	    	for(int j = 0; j < cols; j++) {
	    		if(j != weakNeuron) {
	    			newHiddenToOutputWeights[i][c] = hiddenToOutputWeights[i][j];	
	    			newHiddenToOutputFlags[i][c] = hiddentoOutputFlags[i][j];	
	    			c++;
	    		}
	    	} 
	    	
	    }
	    
	    //System.out.println("\nNew Hidden to Output Wts: ");
	    //print2D(newHiddenToOutputWeights);
	    
	    //reset input layer to hidden layer output weights and flags
	    rows = nn.getHiddenLayer().getWeightsIn().length;
	    cols = nn.getHiddenLayer().getWeightsIn()[0].length;
	    
	    boolean[][] inputToHiddenFlags = new boolean [rows][cols];
	    double[][] inputToHiddenWeights = new double [rows][cols];
	    
	    inputToHiddenWeights = nn.getHiddenLayer().getWeightsIn();
	    inputToHiddenFlags = nn.getHiddenLayer().getFlagsIn();
	    
	    double[][] newInputHiddenWeights = new double [rows - 1][cols];
	    boolean[][] newInputHiddenFlags = new boolean [rows - 1][cols];
	    
	    int r = 0;
	    
	    for (int i = 0; i < rows; i++) {	
	    	if (i != weakNeuron) {
	    		for(int j = 0; j < cols; j++) {
	    			newInputHiddenWeights[r][j] = inputToHiddenWeights[i][j];
	    			newInputHiddenFlags[r][j] = inputToHiddenFlags[i][j];
	    		}
	    		r++;
	    	}
	    	
	    }
	    
	    //System.out.println("\nNew Input to Hidden Wts: ");
	    //print2D(newInputHiddenWeights);
	    
	    //set new layers of the neural net
        InputLayer newInputLayer = new InputLayer();
        newInputLayer.setNumberOfNeuronsInLayer(newInputNeurons);
        
        HiddenLayer newHiddenLayer = new HiddenLayer();
        newHiddenLayer.setNumberOfNeuronsInLayer(newHiddenNeurons);
        newHiddenLayer.setFlagsIn(newInputHiddenFlags);
        newHiddenLayer.setWeightsIn(newInputHiddenWeights);
        
        OutputLayer newOutputLayer = new OutputLayer();
        newOutputLayer.setNumberOfNeuronsInLayer(newOutputNeurons);
        newOutputLayer.setFlagsIn(newHiddenToOutputFlags);
        newOutputLayer.setWeightsIn(newHiddenToOutputWeights);

		nn.setInputLayer(newInputLayer);
		nn.setHiddenLayer(newHiddenLayer);
		nn.setOutputLayer(newOutputLayer);
		
		//one weak neuron removed, if the total number of neurons has not been removed call removeWeakNeuron method again
		numRemove--;
		
		if (numRemove != 0) {
			removeWeakNeuronOLD(nn, numRemove);
		}
	    
		return nn;
	}
	
	public static NeuralNet removeLink(NeuralNet nn, double hiddenThreshold, double outputThreshold){
		
		//get the size of the hidden layer weight array 
	    int rows = nn.getHiddenLayer().getWeightsIn().length;
	    int cols = nn.getHiddenLayer().getWeightsIn()[0].length;
	    
	    //get input layer to hidden layer weights and flags
	    boolean[][] inputToHiddenFlags = new boolean [rows][cols];
	    double[][] inputToHiddenWeights = new double [rows][cols];
	    
	    inputToHiddenWeights = nn.getHiddenLayer().getWeightsIn();
	    inputToHiddenFlags = nn.getHiddenLayer().getFlagsIn();
	    
	    //remove input layer to hidden layer links
	    while (hiddenThreshold != 0) {  
	    	//find min
		    int r = 0;
		    int c = 0;
		    double min = 0;
		    
		    boolean found = false;
		    while (min == 0) {
			    for (int i = 0; i < rows && !found; i++) {	
			    	for(int j = 0; j < cols; j++) {
			    		if (inputToHiddenWeights[i][j] != 0) {
			    			min = inputToHiddenWeights[i][j];
			    			r = i;
			    			c = j;
			    			found = true;
			    			break;
			    		}
			    	}
			    }
		    }
		    
		    
		    for (int i = 0; i < rows; i++) {	
		    	for(int j = 0; j < cols; j++) {
		    		if (min >= Math.abs(inputToHiddenWeights[i][j]) && inputToHiddenWeights[i][j] != 0) {
		    			min = Math.abs(inputToHiddenWeights[i][j]);
		    			r = i;
		    			c = j;
		    		}
		    	}
		    }
		    
		    inputToHiddenWeights[r][c] = 0;
		    inputToHiddenFlags[r][c] = true;
		    
		    hiddenThreshold--;
	    }//while
	    
	    //get the size of the output layer weight array 
	    rows = nn.getOutputLayer().getWeightsIn().length;
	    cols = nn.getOutputLayer().getWeightsIn()[0].length;
	    
	    //get hidden layer to output layer weights and flags
	    boolean[][] hiddenToOutputFlags = new boolean [rows][cols];
	    double[][] hiddenToOutputWeights = new double [rows][cols];
	    
	    hiddenToOutputWeights = nn.getOutputLayer().getWeightsIn();
	    hiddenToOutputFlags = nn.getOutputLayer().getFlagsIn();
		
	    //remove hidden layer to output layer links
	    while (outputThreshold != 0) {  
	    	//find min
		    int r = 0;
		    int c = 0;
		    double min = 0;
		    
		    boolean found = false;
		    while (min == 0) {
			    for (int i = 0; i < rows && !found; i++) {	
			    	for(int j = 0; j < cols; j++) {
			    		if (hiddenToOutputWeights[i][j] != 0) {
			    			min = hiddenToOutputWeights[i][j];
			    			r = i;
			    			c = j;
			    			found = true;
			    			break;
			    		}
			    	}
			    }
		    }
		    
		    for (int i = 0; i < rows; i++) {	
		    	for(int j = 0; j < cols; j++) {
		    		if (min > Math.abs(hiddenToOutputWeights[i][j]) && hiddenToOutputWeights[i][j] != 0) {
		    			min = Math.abs(hiddenToOutputWeights[i][j]);
		    			r = i;
		    			c = j;
		    		}
		    	}
		    }
		    
		    hiddenToOutputWeights[r][c] = 0;
		    hiddenToOutputFlags[r][c] = true;
		    
		    outputThreshold--;
	    }//while
	    
	    //set new weight and flag matrices for the neural net
        nn.getHiddenLayer().setFlagsIn(inputToHiddenFlags);
        nn.getHiddenLayer().setWeightsIn(inputToHiddenWeights);

        nn.getOutputLayer().setFlagsIn(hiddenToOutputFlags);
        nn.getOutputLayer().setWeightsIn(hiddenToOutputWeights);
	    
		return nn;
	}
	
	//print matrix of doubles
	public static void print2D(double mat[][]) {
        for (double[] row : mat)
            System.out.println(Arrays.toString(row));

	}
	
	//print matrix of boolean 
	public static void booleanPrint2D(boolean mat[][]) {
        for (boolean[] row : mat)
            System.out.println(Arrays.toString(row));

	}

}
