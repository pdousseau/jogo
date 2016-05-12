package VariosProcessos;

public class MainServidor {

	public static void main(String[] args){
		try{
			Servidor server = new Servidor();
			server.run();
		}catch(Exception e){}
	}
}
