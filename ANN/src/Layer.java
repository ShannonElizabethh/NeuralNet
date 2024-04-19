import java.util.Random;

public class Layer {
	
	private int numberOfNeuronsInLayer;	
	private int numberOfNeuronsInPreviousLayer;
	private double bias;
	
	private double weightsIn[][];
	private boolean flagsIn[][];
	
	private double[][] outputValues;
	
	//get input weights
	public double[][] getWeightsIn() {
		return weightsIn;
	}
	
	//set input weights
	public void setWeightsIn(double[][] weightsIn) {
		this.weightsIn = weightsIn;
	}

	
	//get flags in
	public boolean[][] getFlagsIn() {
		return flagsIn;
	}
	
	//set flags in
	public void setFlagsIn(boolean[][] flagsIn) {
		this.flagsIn = flagsIn;
	}

	//get number of neurons in layer
	public int getNumberOfNeuronsInLayer() {
		return numberOfNeuronsInLayer;
	}

	//set number of neurons in layer
	public void setNumberOfNeuronsInLayer(int numberOfNeuronsInLayer) {
		this.numberOfNeuronsInLayer = numberOfNeuronsInLayer;
	}
	
	//get number of neurons in next layer
	public int getNumberOfNeuronsInPreviousLayer() {
		return numberOfNeuronsInPreviousLayer;
	}

	//set number of neurons in next layer
	public void setNumberOfNeuronsInPreviousLayer(int numberOfNeuronsInPreviousLayer) {
		this.numberOfNeuronsInPreviousLayer = numberOfNeuronsInPreviousLayer;
	}
	
	//get layer bias
	public double getBias() {
		return bias;
	}

	//set number of neurons in next layer
	public void setBias(double bias) {
		this.bias = bias;
	}
	
	//get output values for each neuron in layer
	public double[][] getOutputValue() {
		return outputValues;
	}

	//set output values for each neuron in layer
	public void setOutputValue(double[][] outputValues) {
		this.outputValues = outputValues;
	}
	
	public double initializeNeuronWeight(){
		Random rand = new Random();
		return rand.nextDouble();
//		return 0.2;
	}

}
