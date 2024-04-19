	import java.io.IOException;
	import java.math.BigDecimal;
	import java.math.RoundingMode;
	import java.text.ParseException;
	import java.util.ArrayList;
	import java.util.Arrays;

	import javax.swing.JFrame;
	import javax.swing.JScrollPane;
	import javax.swing.JTable;

	public class testAccuracythreshold {
		//create the lists to be used for creating a table
		static ArrayList <Double> averageAccuracyList = new ArrayList<Double>();
		static ArrayList <Integer> averageNumNeuronsList = new ArrayList<Integer>();
		static ArrayList <Long> averageTrainingTimesList = new ArrayList<Long>();
		static ArrayList <Integer> numberEpochs = new ArrayList<Integer>();

		public static void main(String[] args) throws IOException, ParseException, org.json.simple.parser.ParseException {
			Data fileName  = new Data("JSONFiles", "testJSON.json");
			
			//set to true to for FCNN and false for PCNN
			boolean fullyConnectedNet = false;
			
			double threshold = 88;
			
			long programTimer = System.nanoTime();
			
			//initalize net
			NeuralNet net = new NeuralNet();
			net = net.initalizeNet(21, 20, 3);
			
			//find optimal number of neurons
		//	net = getOptimalNeurons(net, fullyConnectedNet, threshold);
			
			
			double accuracy = averageAcc(net);
			//remove weak neurons until accuracy drops, first by 20%, then by 10%
			removeNeurons(net, accuracy, 0.2, fullyConnectedNet, threshold);			
			net = NetFunctions.readJSONFile(fileName);

			
			//if the number of neurons is 10 or less (meaning only one neuron was being removed at a time in previous call to removeNeurons) 
			//then there is no need to keep removing neurons
			int numNeuron = net.getHiddenLayer().getNumberOfNeuronsInLayer();
			accuracy = net.getAccuracy();
			
			if(numNeuron >= 10) {
				removeNeurons(net, accuracy, 0.1, fullyConnectedNet, threshold);
				net = NetFunctions.readJSONFile(fileName);
			}

			//save net to json file
			NetFunctions.saveJSONFile(net, fileName);
			
			long endProgramTimer   = System.nanoTime();
			long totalProgramTime = endProgramTimer - programTimer;	
			double programTimeSeconds = (double)totalProgramTime / 1_000_000_000.0;
			System.out.println("Total Time: " + programTimeSeconds + " totalProgramTime: " + totalProgramTime);
			
			NetFunctions.printNet(net);
			
			//print charts
			createJTable(averageAccuracyList, averageNumNeuronsList, averageTrainingTimesList, numberEpochs);


		} //main


		public static NeuralNet testAndTrainNet(NeuralNet net) throws IOException {
			
			//get data sets 		
			//21 input; 3 output; 
			Data inputData  = new Data("data", "thyroidDisease_train.csv");
			Data outputData = new Data("data", "thyroidDisease_output_train.csv");
			
			Data testInputData  = new Data("data", "thyroidDisease_test.csv");
			Data testOutputData = new Data("data", "thyroidDisease_output_test.csv");
			
			//create net
			NeuralNet net1 = new NeuralNet();
			net1 = net;
			
			//set normalzation type
//			Data.NormalizationTypesENUM  NORMALIZATION_TYPE = Data.NormalizationTypesENUM.MAX_MIN;
			Data.NormalizationTypesENUM NORMALIZATION_TYPE = Data.NormalizationTypesENUM.MAX_MIN_EQUALIZED; 
			
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
			net1.setTargetError(0.02);
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
			//NetFunctions.calcAccuracy(netOutputValues, matrixTestOutput);
			trainedNet.setAccuracy(NetFunctions.calcAccuracy(netOutputValues, matrixTestOutput));
			
			return trainedNet;

		}
		
		private static double averageAcc(NeuralNet net) throws IOException {
			Data fileName  = new Data("JSONFiles", "testJSON.json");
			int inputNeurons = net.getInputLayer().getNumberOfNeuronsInLayer();
			int hiddenNeurons = net.getHiddenLayer().getNumberOfNeuronsInLayer();
			int outputNeurons = net.getOutputLayer().getNumberOfNeuronsInLayer();
			
			double accuracy1, accuracy2, accuracy3, accuracy4, accuracy5, averageAcc;
			int epoch1, epoch2, epoch3, epoch4, epoch5, averageEpochs;			
			
			NeuralNet net2 = new NeuralNet();

			long startTime = System.nanoTime();
		
			System.out.print("[First trial]");
			net2 = net2.initalizeNet(inputNeurons, hiddenNeurons, outputNeurons);	
			net2 = testAndTrainNet(net2);		
			accuracy1 = net2.getAccuracy();
			epoch1 = net2.getEpochs();
			
			System.out.print("[Second trial]");
			net2 = net2.initalizeNet(inputNeurons, hiddenNeurons, outputNeurons);
			net2 = testAndTrainNet(net2);	
			accuracy2 = net2.getAccuracy();
			epoch2 = net2.getEpochs();
			
			System.out.print("[third trial]");
			net2 = net2.initalizeNet(inputNeurons, hiddenNeurons, outputNeurons);
			net2 = testAndTrainNet(net2);	
			accuracy3 = net2.getAccuracy();
			epoch3 = net2.getEpochs();
			
			System.out.print("[fourth trial]");
			net2 = net2.initalizeNet(inputNeurons, hiddenNeurons, outputNeurons);
			net2 = testAndTrainNet(net2);
			accuracy4 = net2.getAccuracy();
			epoch4 = net2.getEpochs();
			
			System.out.print("[fifth trial]");
			net2 = net2.initalizeNet(inputNeurons, hiddenNeurons, outputNeurons);
			net2 = testAndTrainNet(net2);
			accuracy5 = net2.getAccuracy();
			epoch5 = net2.getEpochs();
			
			long endTime   = System.nanoTime();
			long totalTime = endTime - startTime;	
			
			//save trained net to file
			NetFunctions.saveJSONFile(net2, fileName);
			
			double[] accuracyArray = {accuracy1, accuracy2, accuracy3, accuracy4, accuracy5};
			Arrays.sort(accuracyArray);
			
			averageAcc = (accuracyArray[1] + accuracyArray[2] + accuracyArray[3])/3;
			System.out.println("Average Accuracy: " + averageAcc + "\n");
			
			averageEpochs = (epoch1 + epoch2 + epoch3 + epoch4 + epoch5)/5;
			
			averageNumNeuronsList.add(net.getHiddenLayer().getNumberOfNeuronsInLayer());
			averageAccuracyList.add(averageAcc);
			averageTrainingTimesList.add(totalTime);
			numberEpochs.add(averageEpochs);
			
			return averageAcc;
		}
		
		public static NeuralNet getOptimalNeurons(NeuralNet net, boolean fullyConnectedNet, double threshold) throws IOException, ParseException, org.json.simple.parser.ParseException {
			
			Data fileName  = new Data("JSONFiles", "testJSON.json");
			NeuralNet net2 = new NeuralNet();
			
			int inputNeurons = net.getInputLayer().getNumberOfNeuronsInLayer();
			int outputNeurons = net.getOutputLayer().getNumberOfNeuronsInLayer();
			int hiddenNeurons = net.getHiddenLayer().getNumberOfNeuronsInLayer(); 

			double preAccuracy = 0, accuracy = 0;
			
			//increase number of hidden neurons until accuracy drops
			System.out.println("------ Number of Hidden Neurons = " + hiddenNeurons + " -------");
			accuracy = averageAcc(net);
			while (preAccuracy < accuracy) {
				preAccuracy = accuracy;
				hiddenNeurons = hiddenNeurons * 2;
				net = net.initalizeNet(inputNeurons, hiddenNeurons, outputNeurons);
				
				System.out.println("------ Number of Hidden Neurons = " + hiddenNeurons + " -------");
			    accuracy = averageAcc(net);
			    
			}	

			int numNeuron;
				//load a trained net (need trained net for removing weak neurons)
				net = NetFunctions.readJSONFile(fileName);		
			
			//if savedWeights is true, the network will used the saved weights
//			if (savedWeights == true) {
					
				//remove weak neurons until accuracy drops, first by 20%, then by 10%
				removeNeurons(net, accuracy, 0.2, fullyConnectedNet, threshold);			
				net2 = NetFunctions.readJSONFile(fileName);

				
				//if the number of neurons is 10 or less (meaning only one neuron was being removed at a time in previous call to removeNeurons) 
				//then there is no need to keep removing neurons
				numNeuron = net2.getHiddenLayer().getNumberOfNeuronsInLayer();
				
				if(numNeuron >= 10) {
					removeNeurons(net2, accuracy, 0.1, fullyConnectedNet, threshold);
					net2 = NetFunctions.readJSONFile(fileName);
				}
	/*			
			// if saved weights is false, the network will use random weights
			} else if(savedWeights == false) {
				
				//remove weak neurons until accuracy drops, first by 20%, then by 10%
				removeNeuronsNotSavedWt(net, accuracy, 0.2);
				net2 = NetFunctions.readJSONFile(fileName);
				
				numNeuron = net2.getHiddenLayer().getNumberOfNeuronsInLayer();
				
				if(numNeuron >= 10) {
					removeNeuronsNotSavedWt(net2, accuracy, 0.1);
					net2 = NetFunctions.readJSONFile(fileName);
				}
			} else {
				System.out.print("ERROR");
			}
	*/

			//return optimal number of neurons for the network
			System.out.println("------ Optimal Number of Hidden Neurons = " + net2.getHiddenLayer().getNumberOfNeuronsInLayer() + " -------");
			
			//NetFunctions.printNet(net2);
			
			return net2;
		}
		
		public static void removeNeurons(NeuralNet net, double accuracy, double percent, boolean fullyConnectedNet, double threshold) throws IOException {
			Data fileName  = new Data("JSONFiles", "testJSON.json");
			
			int inputNeurons = net.getInputLayer().getNumberOfNeuronsInLayer();
			int outputNeurons = net.getOutputLayer().getNumberOfNeuronsInLayer();
			int hiddenNeurons = net.getHiddenLayer().getNumberOfNeuronsInLayer();
			
			double preAccuracy;
			
			do {
				NetFunctions.saveJSONFile(net, fileName);			
				preAccuracy = accuracy;		
				
				int numRemove = (int) (hiddenNeurons * percent);
				
				//if number of neurons to remove is less than 1 (numRemove == 0), then remove one neuron
				if (numRemove == 0) {
					numRemove = 1;
				}
				
				
				if (fullyConnectedNet == true) {
					hiddenNeurons = hiddenNeurons - numRemove;
					net = net.initalizeNet(inputNeurons, hiddenNeurons, outputNeurons);
					
					System.out.println("------ Number of Hidden Neurons = " + hiddenNeurons + " -------");
				    accuracy = averageAcc(net);

				    
				} else {
				
				//gets number of links between layers
				int hidLinks = net.getInputLayer().getNumberOfNeuronsInLayer() * net.getHiddenLayer().getNumberOfNeuronsInLayer();
				int outLinks = net.getHiddenLayer().getNumberOfNeuronsInLayer() * net.getOutputLayer().getNumberOfNeuronsInLayer();
				
				//get number of links to remove for each layer (this would be 20% of the links)
				int numRemoveInputHiddenLinks = (int)(hidLinks * 0.1);
				int numRemoveHiddenOutputLinks = (int)(outLinks * 0.1);
				
//				//remove the weak links
				net = NetFunctions.removeLink(net, numRemoveInputHiddenLinks, numRemoveHiddenOutputLinks);
				
				//remove weak neurons
				net = NetFunctions.removeWeakNeuron(net, numRemove);
				
				hiddenNeurons = net.getHiddenLayer().getNumberOfNeuronsInLayer();
				
				System.out.println("------ Number of Hidden Neurons = " + hiddenNeurons + " -------");
				
				//start timer
				long startTime = System.nanoTime();
				
				//train net
				net = testAndTrainNet(net);
				
				//stop timer
				long endTime   = System.nanoTime();
				long totalTime = endTime - startTime;
				
				accuracy = net.getAccuracy();
				
				//add new values to the lists 
				averageNumNeuronsList.add(net.getHiddenLayer().getNumberOfNeuronsInLayer());
				averageAccuracyList.add(accuracy);
				averageTrainingTimesList.add(totalTime);
				numberEpochs.add(net.getEpochs());
				
				}
			   
			} while (accuracy > threshold);
			
		}
		
		
	/*	
		public static void removeNeuronsNotSavedWt(NeuralNet net, double accuracy, double percent) throws IOException {
			Data fileName  = new Data("JSONFiles", "testJSON.json");
			int hiddenNeurons = net.getHiddenLayer().getNumberOfNeuronsInLayer();
			double preAccuracy;
			int numEpochs;
			
			NeuralNet newNet = new NeuralNet();
			
			do {
				NetFunctions.saveJSONFile(net, fileName);
				preAccuracy = accuracy;		
				
				int numRemove = (int) (hiddenNeurons * percent);
				
				//if number of neurons to remove is less than 1 (numRemove == 0), then remove one neuron
				if (numRemove == 0) {
					numRemove = 1;
				}
							
				//gets number of links between layers
				int hidLinks = net.getInputLayer().getNumberOfNeuronsInLayer() * net.getHiddenLayer().getNumberOfNeuronsInLayer();
				int outLinks = net.getHiddenLayer().getNumberOfNeuronsInLayer() * net.getOutputLayer().getNumberOfNeuronsInLayer();
				
				//get number of links to remove for each layer (this would be 20% of the links)
				int numRemoveInputHiddenLinks = (int)(hidLinks * 0.2);
				int numRemoveHiddenOutputLinks = (int)(outLinks * 0.2);
				
				
				System.out.println("Trained Net Prior to Removing Neurons");
				System.out.println("------ Number of Hidden Neurons = " + hiddenNeurons + " -------");				
				
				int inputNeurons = net.getInputLayer().getNumberOfNeuronsInLayer();
				int outputNeurons = net.getOutputLayer().getNumberOfNeuronsInLayer();
				
				//initalize the net with random weights
				newNet = new NeuralNet();
				newNet = newNet.initalizeNet(inputNeurons, hiddenNeurons, outputNeurons);
				
				//need to train net to remove weak links and neurons			
				//start timer
				long startTime1 = System.nanoTime();
				
				//train net
				newNet = testAndTrainNet(newNet);
				
				//stop timer
				long endTime1 = System.nanoTime();
				
				//get number of epoch for training
				numEpochs = newNet.getEpochs();
						
//				//remove the weak links
				newNet = NetFunctions.removeLink(newNet, numRemoveInputHiddenLinks, numRemoveHiddenOutputLinks);
				
				//remove weak neurons
				newNet = NetFunctions.removeWeakNeuron(newNet, numRemove);
				
				hiddenNeurons = newNet.getHiddenLayer().getNumberOfNeuronsInLayer();
				
				System.out.println("Net After Removing Neurons");
				System.out.println("------ Number of Hidden Neurons = " + hiddenNeurons + " -------");

				//start timer
				long startTime = System.nanoTime();
				
				//train net
				newNet = testAndTrainNet(newNet);
				
				//stop timer
				long endTime   = System.nanoTime();
				long totalTime = (endTime - startTime) + (endTime1 - startTime1);
				
				accuracy = newNet.getAccuracy();
				net = newNet;
				
				int totalEpochs = net.getEpochs() + numEpochs;
				
				//add new values to the lists 
				averageNumNeuronsList.add(net.getHiddenLayer().getNumberOfNeuronsInLayer());
				averageAccuracyList.add(accuracy);
				averageTrainingTimesList.add(totalTime);
				numberEpochs.add(totalEpochs);
			   
			} while (preAccuracy <= accuracy);
			
		}
	*/
		
		public static void createJTable (ArrayList<Double> accuracyList, ArrayList<Integer> numNeuronsList, ArrayList<Long> trainingTimesList, ArrayList<Integer> numberEpochs) {		
		    JFrame frame;
		    JTable table;
		    frame = new JFrame();
		 
		    // Frame Title
		    frame.setTitle("Accuracy Table");     
	        
			double timesInSeconds [] = new double[trainingTimesList.size()];
			
			for (int i = 0; i < timesInSeconds.length; i++) {
				timesInSeconds[i] = trainingTimesList.get(i).doubleValue();
			}
			
			for(int i = 0; i < timesInSeconds.length; i++) {			
				timesInSeconds[i] = timesInSeconds[i] / 1000000000.0;
				BigDecimal bd2 = new BigDecimal(timesInSeconds[i]).setScale(2, RoundingMode.UP);
				timesInSeconds[i] = bd2.doubleValue();
			    
			}
		    
		    String[][] data = new String [accuracyList.size()][4];
		    
			for (int i = 0; i < accuracyList.size(); i++) {
				data[i][0] = Double.toString(accuracyList.get(i));
				data[i][1] = Double.toString(numNeuronsList.get(i));
				data[i][2] = Double.toString(timesInSeconds[i]);
				data[i][3] = Integer.toString(numberEpochs.get(i));

			}
		 
	        // Column Names
	        String[] columnNames = { "Accuracy", "Number of Hidden Neurons", "Training Time In Seconds", "Number of epochs"};
		 
	        // Initializing the JTable
	        table = new JTable(data, columnNames);
	        table.setBounds(30, 40, 200, 300);
		 
	        // adding it to JScrollPane
	        JScrollPane sp = new JScrollPane(table);
	        frame.add(sp);
	        // Frame Size
	        frame.setSize(800, 300);
	        // Frame Visible = true
	        frame.setVisible(true);
	    }
		
		
	} //class

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
				
				//9 input; 2 output;
				Data inputData  = new Data("data", "breast_cancer_inputs_training_NEW.csv");
				Data outputData = new Data("data", "breast_cancer_output_training.csv");
				
				Data testInputData  = new Data("data", "breast_cancer_inputs_test_NEW.csv");
				Data testOutputData = new Data("data", "breast_cancer_output_test.csv");


	*/
