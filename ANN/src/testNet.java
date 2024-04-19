import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

public class testNet {

	public static void main(String[] args) throws IOException, ParseException, org.json.simple.parser.ParseException {
		
		//get data sets 
		//15 input; 4 output;		
		Data inputData  = new Data("data", "Obesity_input_train_CLEAN.csv");
		Data outputData = new Data("data", "Obesity_output_train_CLEAN.csv");
		
		Data testInputData  = new Data("data", "Obesity_input_test_CLEAN.csv");
		Data testOutputData = new Data("data", "Obesity_output_test_CLEAN.csv");
		
		//create net
		NeuralNet net1 = new NeuralNet();
		net1 = net1.initalizeNet(15, 20, 4);
		
		//set normalzation type
		Data.NormalizationTypesENUM  NORMALIZATION_TYPE = Data.NormalizationTypesENUM.MAX_MIN;
//		Data.NormalizationTypesENUM NORMALIZATION_TYPE = Data.NormalizationTypesENUM.MAX_MIN_EQUALIZED; 
		
		//create matrix for data
		double[][] matrixInput  = inputData.rawData2Matrix( inputData );
		double[][] matrixOutput = outputData.rawData2Matrix( outputData );
		
		double[][] matrixTestInput  = outputData.rawData2Matrix( testInputData );
		double[][] matrixTestOutput = outputData.rawData2Matrix( testOutputData );
		
		//normalize data
		double[][] matrixInputNorm = inputData.normalize(matrixInput, NORMALIZATION_TYPE);			
		double[][] matrixTestInputNorm = outputData.normalize(matrixTestInput, NORMALIZATION_TYPE);
		
		//set training set and output data set
		net1.setTrainSet( matrixInputNorm );
		net1.setRealMatrixOutputSet( matrixOutput );
		
		//set net parameters
		net1.setMaxEpochs(500);
		net1.setTargetError(0.03);
		net1.setLearningRate(0.9);
		
		net1.getHiddenLayer().setBias(0.0);	
		net1.getOutputLayer().setBias(0.0);
		
		//Train Net
		NeuralNet trainedNet = new NeuralNet();
		Backpropagation b = new Backpropagation();
		trainedNet = b.train(net1);
		
		//Test Net
		trainedNet.setTrainSet( matrixTestInputNorm );
		trainedNet.setRealMatrixOutputSet( matrixTestOutput );
		
		//print net accuracy
		double[][] netOutputValues = trainedNet.getNetOutputValues(trainedNet);
		NetFunctions.calcAccuracy(netOutputValues, matrixTestOutput);
		
		Data fileName  = new Data("JSONFiles", "testJSON.json");
		
		
		long startSaveTime   = System.nanoTime();
//		NetFunctions.saveJSONFile(trainedNet, fileName);
		
/*		
		long endSaveTime   = System.nanoTime();
		long SaveTimeLong = endSaveTime - startSaveTime;	
		double saveTime = (double)SaveTimeLong / 1_000_000_000.0;
		
		
		NeuralNet net2 = new NeuralNet();
		
		long startReadTime   = System.nanoTime();
		
		net2 = NetFunctions.readJSONFile(fileName);
		
		
		long endReadTime   = System.nanoTime();
		long readTimeLong = endReadTime - startReadTime;	
		double readTime = (double)readTimeLong / 1_000_000_000.0;	
		
		System.out.println("Save Time: " + saveTime);
		System.out.println("Read Time: " + readTime);

		long startLinkTime   = System.nanoTime();

		//300 and 80
		net2 = NetFunctions.removeLink(trainedNet, 90, 16);
		
		long endLinkTime   = System.nanoTime();
		long linkTimeLong = endLinkTime - startLinkTime;	
		double linkTime = (double)linkTimeLong / 1_000_000_000.0;
		
		long startNeuronTime   = System.nanoTime();
		
		net2 = NetFunctions.removeWeakNeuron(trainedNet, 10);
		
		long endNeuronTime   = System.nanoTime();
		long neuronTimeLong = endNeuronTime - startNeuronTime;	
		double neuronTime = (double)neuronTimeLong / 1_000_000_000.0;
		
		
		System.out.println("Remove Link Time: " + linkTime);
		System.out.println("Remove Neuron Time: " + neuronTime);
		
		//train net2
		NeuralNet net2Trained = new NeuralNet();
		Backpropagation b2 = new Backpropagation();
		net2Trained = b2.train(net2);
		
		//print net2 accuracy
		double[][] netOutputValues2 = net2Trained.getNetOutputValues(net2Trained);
		double accuracy = NetFunctions.calcAccuracy(netOutputValues2, matrixTestOutput);
		
		NetFunctions.saveJSONFile(trainedNet, fileName);
		
//		System.out.println("trainedNet");
//		NetFunctions.printNet(trainedNet);
		
//		System.out.println("net2Trained");
//		NetFunctions.printNet(net2Trained);

		//print target outputs
//		NetFunctions.print2D(matrixTestOutput);
		
		//print the actual NN output
//		NetFunctions.print2D(netOutputValues);
		
		//print all weights of network
//		NetFunctions.printNet(net1);
//		NetFunctions.printNet(trainedNet);
		
		//print all weight flags of network
//		NetFunctions.printNetFlags(net1);
		
		//print weights
//		System.out.println("hidden weights:");
//		NetFunctions.print2D(trainedNet.getHiddenLayer().getWeightsIn());
//		System.out.println("output weights:");
//		NetFunctions.print2D(trainedNet.getOutputLayer().getWeightsIn());


		//create 2nd net by removing the weak neurons of the first net
		NeuralNet net2 = new NeuralNet();
		net2 = NetFunctions.removeLink(trainedNet, 5, 5);
		net2 = NetFunctions.removeWeakNeuron(trainedNet, 5);
		
		//save net to file
		Data fileName  = new Data("JSONFiles", "testJSON.json");
		NetFunctions.saveJSONFile(net2, fileName);
		
		//train net2
		NeuralNet net2Trained = new NeuralNet();
		Backpropagation b2 = new Backpropagation();
		net2Trained = b2.train(net2);
		
		//print net2 accuracy
		double[][] netOutputValues2 = net2Trained.getNetOutputValues(net2Trained);
		NetFunctions.calcAccuracy(netOutputValues2, matrixTestOutput);
		
//		NetFunctions.printNet(net2);
*/


		NeuralNet testNet = new NeuralNet();
		testNet = NetFunctions.readJSONFile(fileName);
		
		int hiddenNeurons = testNet.getHiddenLayer().getNumberOfNeuronsInLayer();
		double preAccuracy = 0;
		double accuracy = 0;
		
		do {			
			preAccuracy = accuracy;		
			
			int numRemove = 1;
			
			//gets number of links between layers
			int hidLinks = testNet.getInputLayer().getNumberOfNeuronsInLayer() * testNet.getHiddenLayer().getNumberOfNeuronsInLayer();
			int outLinks = testNet.getHiddenLayer().getNumberOfNeuronsInLayer() * testNet.getOutputLayer().getNumberOfNeuronsInLayer();
			
			//get number of links to remove for each layer (this would be 20% of the links)
			int numRemoveInputHiddenLinks = (int)(hidLinks * 0.1);
			int numRemoveHiddenOutputLinks = (int)(outLinks * 0.1);
			
//			//remove the weak links
			testNet = NetFunctions.removeLink(testNet, numRemoveInputHiddenLinks, numRemoveHiddenOutputLinks);
			
			//remove weak neurons
			testNet = NetFunctions.removeWeakNeuron(testNet, numRemove);
			
			hiddenNeurons = testNet.getHiddenLayer().getNumberOfNeuronsInLayer();
			
			System.out.println("------ Number of Hidden Neurons = " + hiddenNeurons + " -------");
			
			//train net
			testNet = testOptimalNeurons.testAndTrainNet(testNet);

			accuracy = testNet.getAccuracy();
			
			
		} while (preAccuracy <= accuracy);
		

		
	}

}

/*		Different data sets to use with number of inputs and outputs
 
		//15 input; 4 output;		
		Data inputData  = new Data("data", "Obesity_input_train_CLEAN.csv");
		Data outputData = new Data("data", "Obesity_output_train_CLEAN.csv");
		
		Data testInputData  = new Data("data", "Obesity_input_test_CLEAN.csv");
		Data testOutputData = new Data("data", "Obesity_output_test_CLEAN.csv");
		
		//10 input; 2 output;
		Data inputData  = new Data("data", "breast_cancer_inputs_training.csv");
		Data outputData = new Data("data", "breast_cancer_output_training.csv");
		
		Data testInputData  = new Data("data", "breast_cancer_inputs_test.csv");
		Data testOutputData = new Data("data", "breast_cancer_output_test.csv");
		
		//21 input; 3 output; 
		Data inputData  = new Data("data", "thyroidDisease_train.csv");
		Data outputData = new Data("data", "thyroidDisease_output_train.csv");
		
		Data testInputData  = new Data("data", "thyroidDisease_test.csv");
		Data testOutputData = new Data("data", "thyroidDisease_output_test.csv");
 
 
 
 */
