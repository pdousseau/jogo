package VariosProcessos;

public class MainJogador {
	
	static boolean venceu;
	
	public static void main(String[] args){
		try{
			
			venceu = false;
			
			Jogador Joao = new Jogador(1,2,"Joao");
		        Jogador Maria = new Jogador(2,3,"Maria");
			Jogador Pedro = new Jogador(3,4,"Pedro");
			Jogador Joana = new Jogador(4,5,"Joana");
			
			Joao.start();
			Maria.start();
			Pedro.start();
			Joana.start();
			
		}catch(Exception e){}
	}

}
