
public class OutputLayer extends Layer {
	
	public OutputLayer initializeLayer(OutputLayer outputLayer) {
		
		int numHiddenNeurons = outputLayer.getNumberOfNeuronsInPreviousLayer();
		int numOutputNeurons = outputLayer.getNumberOfNeuronsInLayer();

		double weightsArray[][] = new double[numOutputNeurons][numHiddenNeurons];
		boolean flags[][] = new boolean[numOutputNeurons][numHiddenNeurons];
		
		for (int j = 0; j < numOutputNeurons; j++) {
			for (int k = 0; k < numHiddenNeurons; k++) {
				weightsArray[j][k] = initializeNeuronWeight();
				flags[j][k] = false;
			}
		}
	
		outputLayer.setWeightsIn(weightsArray);
		outputLayer.setFlagsIn(flags);
		return outputLayer;
	
	} // end initializeLayer

}
