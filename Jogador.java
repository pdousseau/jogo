package VariosProcessos;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class Jogador extends Thread {

	ArrayList<Integer> cartas; //cartas do jogador
	Integer dir, esq; //numero dos montes
	String nome; //nome do jogador
	String ip = "127.0.0.1";

	public Jogador(Integer dir, Integer esq, String nome) {

		this.nome = nome;
		this.dir = dir;
		this.esq = esq;

	}

	@SuppressWarnings("unchecked")
	public void run() {
		try{
		Socket conexao;
		ObjectInputStream recebe;
		ObjectOutputStream envia;
		
		//criando um socket para a conexao com o servidor
		conexao = new Socket(InetAddress.getByName(ip), 5001 );
		
		System.out.println(nome + "CONECTADO");
		
		//obtendo os fluxos de entrada e de saida
		envia = new ObjectOutputStream(conexao.getOutputStream());
	    	recebe = new ObjectInputStream(conexao.getInputStream());
	    
	    	System.out.println(nome + " ganhando as cartas");
	    
	    	//pede as cartas para o servidor
	    	envia.writeObject("Cartas");
	    	envia.flush();
	    	cartas = (ArrayList<Integer>)recebe.readObject();
	    
	    	//envia o pedido de verificacao com seu nome
	    	envia.writeObject("VerificaVencedor");
	    	envia.writeObject(cartas);
	    	envia.writeObject(nome);
	    	envia.flush();
	    
	    	//recebe a resposta do servidor
	    	boolean venceu = (Boolean)recebe.readObject();

	    	System.out.println("COMECANDO A JOGAR");
	    
		// enquanto nao houve vencedores
		while (!venceu) {

			//pensando
			sleep((int) (Math.random() * 4000));

			// imprime as suas cartas
			this.imprimeMao();

			// escolhe qual carta ele quer descartar
			int carta = this.escolherCarta();

			//System.out.println("Descartou a carta " + carta);

			// coloca essa carta no monte da direita
			envia.writeObject("Devolucao");
			envia.writeObject(carta);
			envia.writeObject(dir);
		    	envia.flush();

			// retira uma carta do monte da esquerda
		    	envia.writeObject("Compra");
			envia.writeObject(esq);
		    	envia.flush();
		    	carta = (Integer)recebe.readObject();
		    
			// adiciona essa carta na sua mao
			cartas.add(carta);

			// verifica se ele tem uma mao ganhadora
			envia.writeObject("VerificaVencedor");
			envia.writeObject(cartas.clone());
			envia.writeObject(nome);
			envia.flush();

			venceu = (Boolean)recebe.readObject();
		}
		
		if (cartas.get(0) == cartas.get(1) && cartas.get(1) == cartas.get(2) && cartas.get(2) == cartas.get(3)){
			System.out.println("=================================================");
			System.out.println(nome + " VENCEU!!!!! " + cartas.get(0) + cartas.get(1)+ cartas.get(2) + cartas.get(3));
			System.out.println("=================================================");
		}
		
		}catch(Exception e){}
	}

	private void imprimeMao() {

		System.out.print(nome + ": ");

		// para cada carta
		for (int i = 0; i < cartas.size(); i++) {

			// imprime seu valor
			System.out.print(this.cartas.get(i));
		}

		System.out.println("");

	}

	

	private Integer escolherCarta() {
		int tipo[] = new int[6];
		int i, rand, menor, pos = 0;

		//inicializa
		for (i = 0; i <= 5; i++)
			tipo[i] = 0;

		// conta quantas cartas de cada tipo o jogador tem
		for (i = 0; i <= 3; i++)
			tipo[cartas.get(i) - 1]++;

		rand = 1 + ((int) (Math.random() * 2));
		menor = 999;

		// escolhe aleatoriamente qualquer um dos dois jeitos
		switch (rand) {
			case (1): {
	
				// para cada uma das cartas
				for (i = 0; i <= 5; i++)
					
					//verifica se ele tem essa carta em menor numero
					if ((tipo[i] != 0) && (menor > tipo[i])) {
						
						// menor = qnt de cartas do tipo i+1
						menor = tipo[i];
						
						//pega qual carta eh
						pos = i + 1;
					}
			}
			case (2): {
				for (i = 5; i >= 0; i--)
					
					//verifica se ele tem essa carta em menor numero
					if ((tipo[i] != 0) && (menor > tipo[i])) {
	
						// menor = qnt de cartas do tipo i+1
						menor = tipo[i];
						
						//pega qual carta eh
						pos = i + 1;
					}
			}
		}

		//para cada uma das cartas que ele tem
		for (i = 0; i <= 3; i++) {
			
			//verifica se essa eh a carta em menor numero que ele tem
			if (cartas.get(i) == pos) {
				
				//se for, anota sua posicao
				pos = i;
				break;
			}
		}

		return cartas.remove(pos);

	}
	
	
}
