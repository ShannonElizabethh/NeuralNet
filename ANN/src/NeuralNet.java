import java.util.ArrayList;

public class NeuralNet {
	
	private InputLayer inputLayer;
	private HiddenLayer hiddenLayer;
	private OutputLayer outputLayer;
	
	private int epochs;
	private double mse;
	
	private double[][] trainSet;
	private double[] realOutputSet;
	private double[][] realMatrixOutputSet;
	private double[][] errorMatrix;
	private int maxEpochs;
	private double learningRate;
	private double targetError;
	private double trainingError;
	private double errorMean;
	private double accuracy;
	
	private ArrayList <Double> listOfMSE  = new ArrayList<Double>();
	
	//For one hidden layer network
	public NeuralNet initalizeNet (int numInNeurons, int numHiddenNeurons, int numOutNeurons) {

		//create input layer
		InputLayer inputLayer = new InputLayer();
		inputLayer.setNumberOfNeuronsInLayer(numInNeurons);		
		inputLayer = inputLayer.initializeLayer(inputLayer);

		//create hidden layer
		HiddenLayer hiddenLayer = new HiddenLayer();
		hiddenLayer.setNumberOfNeuronsInLayer(numHiddenNeurons);
		hiddenLayer.setNumberOfNeuronsInPreviousLayer(numInNeurons);
		hiddenLayer = hiddenLayer.initializeLayer(hiddenLayer);
		
		//create output layer
		OutputLayer outputLayer = new OutputLayer();
		outputLayer.setNumberOfNeuronsInLayer(numOutNeurons);
		outputLayer.setNumberOfNeuronsInPreviousLayer(numHiddenNeurons);
		outputLayer = outputLayer.initializeLayer(outputLayer);

		//create the NN and set each layer
		NeuralNet newNet = new NeuralNet();
		newNet.setInputLayer(inputLayer);
		newNet.setHiddenLayer(hiddenLayer);
		newNet.setOutputLayer(outputLayer);
	
		return newNet;
				
	} // end initalizeNet
	
	//get the output values of the NN
	public double[][] getNetOutputValues(NeuralNet trainedNet){
		
		int rows = trainedNet.getTrainSet().length;
		int cols = trainedNet.getOutputLayer().getNumberOfNeuronsInLayer();
		
		double[][] matrixOutputValues = new double[rows][cols];
			Backpropagation b = new Backpropagation();
				
			for (int rows_i = 0; rows_i < rows; rows_i++) {
				for (int cols_i = 0; cols_i < cols; cols_i++) {				
					matrixOutputValues[rows_i][cols_i] = b.forward(trainedNet, rows_i).getOutputLayer().getOutputValue()[cols_i][0];
				}
			}
		
		return matrixOutputValues;
				
	} //end getNetOutputValues
	
	public InputLayer getInputLayer () {
		return inputLayer;
	}

	public void setInputLayer (InputLayer inputLayer) {
		this.inputLayer = inputLayer;
	}
	
	public OutputLayer getOutputLayer () {
		return outputLayer;
	}
	
	public void setOutputLayer (OutputLayer outputLayer) {
		this.outputLayer = outputLayer;
	}
	
	public HiddenLayer getHiddenLayer () {
		return hiddenLayer;
	}
	
	public void setHiddenLayer (HiddenLayer hiddenLayer) {
		this.hiddenLayer = hiddenLayer;
	}
	
	public int getEpochs() { 
		return epochs;
	}
	
	public void setEpochs(int epochs) {
		this.epochs = epochs;
	}
	
	public double getMse() {
		return mse;
	}
	
	public void setMse(double mse) {
		this.mse = mse;
	}
	
	public double[][] getTrainSet() {
		return trainSet;
	}

	public void setTrainSet(double[][] trainSet) {
		this.trainSet = trainSet;
	}

	public double[] getRealOutputSet() {
		return realOutputSet;
	}

	public void setRealOutputSet(double[] realOutputSet) {
		this.realOutputSet = realOutputSet;
	}

	public int getMaxEpochs() {
		return maxEpochs;
	}

	public void setMaxEpochs(int maxEpochs) {
		this.maxEpochs = maxEpochs;
	}

	public double getTargetError() {
		return targetError;
	}

	public void setTargetError(double targetError) {
		this.targetError = targetError;
	}

	public double getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	public double getTrainingError() {
		return trainingError;
	}

	public void setTrainingError(double trainingError) {
		this.trainingError = trainingError;
	}

	public ArrayList <Double> getListOfMSE() {
		return listOfMSE;
	}

	public void setListOfMSE(ArrayList <Double> listOfMSE) {
		this.listOfMSE = listOfMSE;
	}

	public double[][] getRealMatrixOutputSet() {
		return realMatrixOutputSet;
	}

	public void setRealMatrixOutputSet(double[][] realMatrixOutputSet) {
		this.realMatrixOutputSet = realMatrixOutputSet;
	}

	public double getErrorMean() {
		return errorMean;
	}

	public void setErrorMean(double errorMean) {
		this.errorMean = errorMean;
	}
	
	public double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}
	
	public double[][] getErrorMatrix() {
		return errorMatrix;
	}

	public void setErrorMatrix(double[][] errorMatrix) {
		this.errorMatrix = errorMatrix;
	}

	
}
