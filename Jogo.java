package VariosProcessos;
import java.util.ArrayList;


public class Jogo {
	
	private boolean fim;
	
	public Jogo(){
		
		fim = false;
		
	}
	
	public synchronized boolean verificarJogo(ArrayList<Integer> cartas, String nome) {

		if (fim) return true;
		
		// verifica se todas as cartas sao iguais
		if ((cartas.get(0) == cartas.get(1))
				&& (cartas.get(1) == cartas.get(2))
				&& (cartas.get(2) == cartas.get(3))) {

			System.out.println("VENCEU!!!!! " + nome);
			System.out.println("CARTAS = " + cartas.get(0) + cartas.get(1) + cartas.get(2) + cartas.get(3));

			fim = true;
			
			// retorna true
			return true;
		}
		
		return false;

	}
	
}
