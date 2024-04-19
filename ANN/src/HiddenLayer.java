

public class HiddenLayer extends Layer {
	
	
	public HiddenLayer initializeLayer(HiddenLayer hiddenLayer) {
		
		int numInputNeurons = hiddenLayer.getNumberOfNeuronsInPreviousLayer();
		int numHiddenNeurons = hiddenLayer.getNumberOfNeuronsInLayer();
		
		double weightsArray[][] = new double[numHiddenNeurons][numInputNeurons];
		boolean flags[][] = new boolean[numHiddenNeurons][numInputNeurons];
		
		for (int j = 0; j < numHiddenNeurons; j++) {
			for (int k = 0; k < numInputNeurons; k++) {
				weightsArray[j][k] = initializeNeuronWeight();
				flags[j][k] = false;
			}
		}
	
		hiddenLayer.setWeightsIn(weightsArray);
		hiddenLayer.setFlagsIn(flags);
		return hiddenLayer;
	
	} // end initializeLayer

} 
