
public class Backpropagation {
	int epoch = 0;
	
	public NeuralNet train(NeuralNet net) {
		net.setMse(1.0);

		while(net.getMse() > net.getTargetError()) {
			if ( epoch >= net.getMaxEpochs() ) break;
			
			int rows = net.getTrainSet().length;
			double sumErrors = 0.0;
			
			for (int rows_i = 0; rows_i < rows; rows_i++) {
				
				net = forward(net, rows_i);
				net = backpropagation(net, rows_i);
				
				sumErrors = sumErrors + net.getErrorMean();
				
			}

			net.setMse( sumErrors / rows );
			net.getListOfMSE().add(net.getMse());
			
			epoch++;
			
		} 
		
		System.out.println();
		System.out.println("----------------------------");
		System.out.println("MSE: " + net.getMse() );
		System.out.println("Number of epochs: " + epoch);
		System.out.println();
		
		net.setEpochs(epoch);
		
		return net;
		
	} // end train
	
	public NeuralNet forward(NeuralNet net, int row) {
		
		//get number of neurons in each layer
		int inNeuronSize = net.getInputLayer().getNumberOfNeuronsInLayer();
		int hidNeuronSize = net.getHiddenLayer().getNumberOfNeuronsInLayer();
		int outNeuronSize = net.getOutputLayer().getNumberOfNeuronsInLayer();
		
		//set matrix for inputs
		double[][] inputMatrix = new double[inNeuronSize][1];
		for (int i = 0; i < inNeuronSize; i++) {			
			inputMatrix[i][0] = net.getTrainSet()[row][i];
		}
		
		//set matrix for outputs
		double[][] outputMatrix = new double[outNeuronSize][1];
		for (int i = 0; i < outNeuronSize; i++) {	
			outputMatrix[i][0] = net.getRealMatrixOutputSet()[row][i];
		}
		
		//set matrix for input to hidden weights
		double[][] inputToHiddenWeights = net.getHiddenLayer().getWeightsIn();
		
		//multiply the two matrices 
		double[][] netValue = new double[inputToHiddenWeights.length][inputMatrix[0].length];
		netValue = multiplyWithBias(inputToHiddenWeights, inputMatrix, net.getHiddenLayer().getBias(), net.getHiddenLayer().getFlagsIn());
		
		//get siglog of all values in the netValue matrix 
		double[][] hiddenNetValueOut = new double[inputToHiddenWeights.length][inputMatrix[0].length];
		for (int i = 0; i < hidNeuronSize; i++) {
			hiddenNetValueOut[i][0] = fncSigLog(netValue[i][0]);
			
		}
		
		//set matrix for hiddenLayer to outputLayer weights
		double[][] hiddenToOutputWeights = net.getOutputLayer().getWeightsIn();
		
		//multiply hiddenLayer netOutputValues by the hiddenLayer to outputLayer weights
		double[][] outputLayerNetValue = new double[hiddenToOutputWeights.length][hiddenNetValueOut[0].length];
		outputLayerNetValue = multiplyWithBias(hiddenToOutputWeights, hiddenNetValueOut, net.getOutputLayer().getBias(), net.getOutputLayer().getFlagsIn());
		
		//get siglog of all values in the outputNetOut matrix 
		double[][] outputNetValueOut = new double[hiddenToOutputWeights.length][hiddenNetValueOut[0].length];
		for (int i = 0; i < outNeuronSize; i++) {
			outputNetValueOut[i][0] = fncSigLog(outputLayerNetValue[i][0]);
			
		}
		
		//calculate error
		// Error(k) = (T − Ok)
		double errorSum = 0;
		double[][] errorMatrix = new double[outNeuronSize][1];
		for (int i = 0; i < outNeuronSize; i++) {
			errorMatrix[i][0] = (outputMatrix[i][0] - outputNetValueOut[i][0]);
			errorSum = errorSum + Math.pow(errorMatrix[i][0], 2);
		}
		
		double errorMean = errorSum / outNeuronSize;
		net.setErrorMean(errorMean);
		net.setErrorMatrix(errorMatrix);
		net.getOutputLayer().setOutputValue(outputNetValueOut);
		net.getHiddenLayer().setOutputValue(hiddenNetValueOut);
		
		return net;
	}//end forward 
	
	private NeuralNet backpropagation(NeuralNet net, int row) {
		
		//get number of neurons in each layer
		int numInputNeurorns = net.getInputLayer().getNumberOfNeuronsInLayer();
		int numHiddenNeurons = net.getHiddenLayer().getNumberOfNeuronsInLayer();
		int numOutputNeurons = net.getOutputLayer().getNumberOfNeuronsInLayer();
		
		//set matrix for hiddenLayer to outputLayer weights and flags
		double[][] hiddenToOutputWeights = net.getOutputLayer().getWeightsIn();
		boolean[][] hiddenToOutputFlags = net.getOutputLayer().getFlagsIn();
		
		//set matrix for inputLayer to hiddenLayer weights and flags
		double[][] inputToHiddenWeights = net.getHiddenLayer().getWeightsIn();
		boolean[][] inputToHiddenFlags = net.getHiddenLayer().getFlagsIn();
				
		//set outputValues matrix
		double[][] outputLayerValues = net.getOutputLayer().getOutputValue();
		
		//set hiddenValues matrix 
		double[][] hiddenLayerValues = net.getHiddenLayer().getOutputValue();	
		
		//set error matrix
		double[][] errorMatrix = net.getErrorMatrix();
		
		//set matrix for inputs
		double[][] netInputValues = new double[numInputNeurorns][1];
		for (int i = 0; i < numInputNeurorns; i++) {	
			netInputValues[i][0] = net.getTrainSet()[row][i];
		}
		
		//sensibility output layer
		double[][] outputLayerSensibility = new double[numOutputNeurons][1];
		
		for(int i = 0; i < outputLayerSensibility.length; i++){
			for(int j = 0; j < outputLayerSensibility[0].length; j++){
				//(v * (1.0 - v) where v = netval 
				//then *error
				
				outputLayerSensibility[i][j] = (outputLayerValues[i][j] * (1 - outputLayerValues[i][j])) * errorMatrix[i][j];
			}
		}
		
		//sensibility hidden layer
		double[][] hiddenLayerSensibility = new double[numHiddenNeurons][1];
		double[][] tempVal = new double[hiddenToOutputWeights.length][hiddenToOutputWeights[0].length];
		
		for(int i = 0; i < hiddenToOutputWeights.length; i++){
			for(int j = 0; j < hiddenToOutputWeights[0].length; j++){
				tempVal[i][j] = hiddenToOutputWeights[i][j] * outputLayerSensibility[i][0];
			}
		}
		
		for(int i = 0; i < tempVal[0].length; i++){
			double sum = 0;
			for(int j = 0; j < tempVal.length; j++){
				sum = sum + tempVal[j][i];
			}
			hiddenLayerSensibility[i][0] = sum;
		} 
		
		//adjust output to hidden layer weights
		double [][] newhiddenToOutputWeights = new double[hiddenToOutputWeights.length][hiddenToOutputWeights[0].length];
		
		for(int i = 0; i < hiddenToOutputWeights.length; i++){
			for(int j = 0; j < hiddenToOutputWeights[0].length; j++){
				if (hiddenToOutputFlags[i][j] == true) {
					newhiddenToOutputWeights[i][j] = 0;
				} else {
					newhiddenToOutputWeights[i][j] = hiddenToOutputWeights[i][j] + (net.getLearningRate() * outputLayerSensibility[i][0] * hiddenLayerValues[j][0]);
				}
			}
		}
		
		//adjust hidden to input weights
		double newInputToHiddenWeights[][] = new double[inputToHiddenWeights.length][inputToHiddenWeights[0].length];
		
		for(int i = 0; i < inputToHiddenWeights.length; i++){
			for(int j = 0; j < inputToHiddenWeights[0].length; j++){
				if (inputToHiddenFlags[i][j] == true) { 
					newInputToHiddenWeights[i][j] = 0;
				} else {
					newInputToHiddenWeights[i][j] = inputToHiddenWeights[i][j] + (net.getLearningRate() * hiddenLayerSensibility[i][0] * netInputValues[j][0]);
				}
			}
		}
		
		//set new net with adjusted weights
		net.getHiddenLayer().setWeightsIn(newInputToHiddenWeights);
		net.getOutputLayer().setWeightsIn(newhiddenToOutputWeights);

		return net;
		
	}
	
    public static double[][] multiplyWithBias (double[][] a,double[][] b, double bias, boolean[][] flag) {
        double[][] result = new double[a.length][b[0].length];
        if(a[0].length != b.length)
            throw new IllegalArgumentException("Number of Columns of first Matrix must match the number of Rows of second Matrix");

        for(int i = 0; i < a.length; i++){
            for(int j = 0; j < b[0].length; j++){
            	
                double value = 0;
                for(int k = 0; k < b.length; k++){
                	
                	if(flag[i][k] == true) {
                		value += 0;
                	}
                	else { value += a[i][k] * b[k][j]; }
                }
                result[i][j] = (value + bias) * 1;
            }
        }
        return result;
    }

	private double fncSigLog(double v) {
		return (1.0 / (1.0 + Math.exp(-v)));
	}
	
} //end class
