import java.io.IOException;
import java.text.ParseException;

public class readTest {

	public static void main(String[] args) throws IOException, ParseException, org.json.simple.parser.ParseException {	
		NeuralNet net1 = new NeuralNet();
		net1 = net1.initalizeNet(2, 3, 2);
		
		NetFunctions.printNet(net1);
		NetFunctions.printNetFlags(net1);
		
		Data fileName  = new Data("JSONFiles", "testJSON.json");
		NetFunctions.saveJSONFile(net1, fileName);
		
		NeuralNet readNet = new NeuralNet();
		readNet = NetFunctions.readJSONFile(fileName);

		NetFunctions.printNet(readNet);
		NetFunctions.printNetFlags(readNet);
	
	}

}
