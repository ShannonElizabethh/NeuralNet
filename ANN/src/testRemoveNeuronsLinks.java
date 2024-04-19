

public class testRemoveNeuronsLinks {

	public static void main(String[] args) {
		
		NeuralNet net1 = new NeuralNet();
		net1 = net1.initalizeNet(5, 10, 2);
		//NetFunctions.printNet(net1);
		
		NeuralNet net2 = new NeuralNet();
		
		int hidLinks = net1.getInputLayer().getNumberOfNeuronsInLayer() * net1.getHiddenLayer().getNumberOfNeuronsInLayer();
		int outLinks = net1.getHiddenLayer().getNumberOfNeuronsInLayer() * net1.getOutputLayer().getNumberOfNeuronsInLayer();
		
		int t1 = (int)(hidLinks * 0.5);
		int t2 = (int)(outLinks * 0.5);
		//System.out.println("\nt1: " + t1);
		//System.out.println("t2: " + t2);

		
		net2 = NetFunctions.removeLink(net1, t1, t2);
		
		NetFunctions.printNet(net2);
		
		net2 = NetFunctions.removeWeakNeuron(net1, 5);
		
		NetFunctions.printNet(net2);
		

	}

}
