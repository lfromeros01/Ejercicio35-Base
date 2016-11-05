import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


public class VentanaPrincipal {

	//La ventana principal, en este caso, guarda todos los componentes:
	JFrame ventana;
	JPanel panelImagen;
	JPanel panelEmpezar;
	JPanel panelPuntuacion;
	JPanel panelJuego;
	
	//Todos los botones se meten en un panel independiente.
	//Hacemos esto para que podamos cambiar después los componentes por otros
	JPanel [][] panelesJuego;
	JButton [][] botonesJuego;
	
	//Correspondencia de colores para las minas:
	Color correspondenciaColores [] = {Color.BLACK, Color.CYAN, Color.GREEN, Color.ORANGE, Color.RED, Color.RED, Color.RED, Color.RED, Color.RED, Color.RED};
	
	JButton botonEmpezar;
	JTextField pantallaPuntuacion;
	
	//LA VENTANA GUARDA UN CONTROL DE JUEGO:
	ControlJuego juego;
	
	
	//Constructor, marca el tamaño y el cierre del frame
	public VentanaPrincipal() {
		ventana = new JFrame();
		ventana.setBounds(100, 100, 700, 500);
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		juego = new ControlJuego();
	}
	
	//Inicializa todos los componentes del frame
	public void inicializarComponentes(){
		
		//Definimos el layout:
		ventana.setLayout(new GridBagLayout());
		
		//Inicializamos componentes
		panelImagen = new JPanel();
		panelEmpezar = new JPanel();
		panelEmpezar.setLayout(new GridLayout(1,1));
		panelPuntuacion = new JPanel();
		panelPuntuacion.setLayout(new GridLayout(1,1));
		panelJuego = new JPanel();
		panelJuego.setLayout(new GridLayout(10,10));
		
		
		botonEmpezar = new JButton("Go!");
		pantallaPuntuacion = new JTextField("0");
		pantallaPuntuacion.setEditable(false);
		pantallaPuntuacion.setHorizontalAlignment(SwingConstants.CENTER);
		
		//Bordes y colores:
		panelImagen.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		panelEmpezar.setBorder(BorderFactory.createTitledBorder("Empezar"));
		panelPuntuacion.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		panelJuego.setBorder(BorderFactory.createTitledBorder("Juego"));
		
			
		//Colocamos los componentes:
		//AZUL
		GridBagConstraints settings = new GridBagConstraints();
		settings.gridx = 0;
		settings.gridy = 0;
		settings.weightx = 1;
		settings.weighty = 1;
		settings.fill = GridBagConstraints.BOTH;
		ventana.add(panelImagen, settings);
		//VERDE
		settings = new GridBagConstraints();
		settings.gridx = 1;
		settings.gridy = 0;
		settings.weightx = 1;
		settings.weighty = 1;
		settings.fill = GridBagConstraints.BOTH;
		ventana.add(panelEmpezar, settings);
		//AMARILLO
		settings = new GridBagConstraints();
		settings.gridx = 2;
		settings.gridy = 0;
		settings.weightx = 1;
		settings.weighty = 1;
		settings.fill = GridBagConstraints.BOTH;
		ventana.add(panelPuntuacion, settings);
		//ROJO
		settings = new GridBagConstraints();
		settings.gridx = 0;
		settings.gridy = 1;
		settings.weightx = 1;
		settings.weighty = 10;
		settings.gridwidth = 3;
		settings.fill = GridBagConstraints.BOTH;
		ventana.add(panelJuego, settings);
		
		//Paneles
		panelesJuego = new JPanel[10][10];
		for (int i = 0; i < panelesJuego.length; i++) {
			for (int j = 0; j < panelesJuego[i].length; j++) {
				panelesJuego[i][j] = new JPanel();
				panelesJuego[i][j].setLayout(new GridLayout(1,1));
				panelJuego.add(panelesJuego[i][j]);
			}
		}
		
		//Botones
		botonesJuego = new JButton[10][10];
		for (int i = 0; i < botonesJuego.length; i++) {
			for (int j = 0; j < botonesJuego[i].length; j++) {
				botonesJuego[i][j] = new JButton("-");
				panelesJuego[i][j].add(botonesJuego[i][j]);
			}
		}
		
		//BotónEmpezar:
		panelEmpezar.add(botonEmpezar);
		panelPuntuacion.add(pantallaPuntuacion);
		
	}
	
	/**
	 * Método que inicializa todos los lísteners que necesita inicialmente el programa
	 */
	public void inicializarListeners(){
		
		botonEmpezar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < botonesJuego.length; i++) 
				{
					for (int j = 0; j < botonesJuego.length; j++)
					{
						botonesJuego[i][j].setEnabled(true);
						panelesJuego[i][j].removeAll();
						panelesJuego[i][j].setBorder(BorderFactory.createEmptyBorder());
						panelesJuego[i][j].add(botonesJuego[i][j]);
						refrescarPantalla();
						actualizarPuntuacion();
						juego.inicializarPartida();
						
					}
				}		
			}
		});
		
		for (int i = 0; i < botonesJuego.length; i++) {
			for (int j = 0; j < botonesJuego[i].length; j++) {
				 botonesJuego[i][j].addActionListener(new ActionBoton(i,j,juego,this));
			}
		}
	}
	
	
	/**
	 * Método que pinta en la pantalla el número de minas que hay alrededor de la celda
	 * Saca el botón que haya en la celda determinada y añade un JLabel centrado y no editable con el número de minas alrededor.
	 * Se pinta el color del texto según la siguiente correspondecia (consultar la variable correspondeciaColor):
	 * - 0 : negro
	 * - 1 : cyan
	 * - 2 : verde
	 * - 3 : naranja
	 * - 4 ó más : rojo 
	 * @param i: posición vertical de la celda.
	 * @param j: posición horizontal de la celda.
	 */
	public void mostrarNumMinasAlrededor(int i , int j) {
		
		int numminas=juego.getMinasAlrededor(i, j);
		
		if(numminas==0)
		{
			mostrarvacias(i,j);
		}	
		else
		{
			panelesJuego[i][j].removeAll();
			JLabel etiqueta = new JLabel();
			String texto=""+numminas;
			etiqueta.setText(texto);
			etiqueta.setForeground(correspondenciaColores[numminas]);
			etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
			panelesJuego[i][j].add(etiqueta);
			refrescarPantalla();
		}	
		
	}
	/**
	 * Metodo que comprueba que cambia a una etiqueta vacia si hay 0 minas
	 * busca en las casillas de alrededor y si las minas son 0 
	 * vuelve a comprobar si hay 0 minas para mostrar una etiqueta vacia
	 */
	
	public void mostrarvacias(int i, int j)
	{
		
		panelesJuego[i][j].removeAll();
		JLabel etiqueta = new JLabel();
		String texto="";
		actualizarPuntuacion();
		etiqueta.setText(texto);
		panelesJuego[i][j].add(etiqueta);
		juego.modificaMinasAlrededor(i, j);
		refrescarPantalla();
		for(int k=(i-1);k<=(i+1);k++)
		{
			for(int l=(j-1);l<=(j+1);l++)
			{
				if(k>=0 && l>=0 && k<juego.LADO_TABLERO && l<juego.LADO_TABLERO)
				{
					if(juego.getMinasAlrededor(k, l)==0)
					{
						int puntos=juego.getPuntuacion();
						puntos++;
						juego.setPuntuacion(puntos);
						actualizarPuntuacion();
						mostrarvacias(k,l);
						
					}	
					
				}	
				
				
			}	
		}	
		
	}

	/**
	 * Método que muestra una ventana que muestra el fin del juego
	 * @param porExplosion : Un booleano que indica si es final del juego porque ha explotado una mina (true) o bien porque hemos desactivado todas (false) 
	 * @post : Todos los botones se desactivan excepto el de volver a iniciar el juego.
	 */
	public void mostrarFinJuego(boolean porExplosion) {
		if(porExplosion==true)
		{
			JOptionPane.showMessageDialog(ventana,"Ha explotado una Mina","FIN DE JUEGO", JOptionPane.ERROR_MESSAGE);
		}	
		else
		{
			JOptionPane.showMessageDialog(ventana,"Has abierto todas las casillas","FIN DE JUEGO", JOptionPane.PLAIN_MESSAGE);
		}	
		
		for (int i = 0; i < botonesJuego.length; i++) 
		{
			for (int j = 0; j < botonesJuego[i].length; j++)
			{
				panelesJuego[i][j].removeAll();
				panelesJuego[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
				JLabel etiqueta = new JLabel("");
				panelesJuego[i][j].add(etiqueta);
				if(juego.getMinasAlrededor(i, j)==juego.getMina())//Para que cuando exploto una mina me salgan todas en su posicion
				{
					etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
					etiqueta.setForeground(Color.RED);
					etiqueta.setText("*");
					Font font = new Font("Dialog", Font.BOLD, 20);
					etiqueta.setFont(font);
					
					
				}	
				refrescarPantalla();
			}
		}
	}

	/**
	 * Método que muestra la puntuación por pantalla.
	 */
	public void actualizarPuntuacion() {
		
		pantallaPuntuacion.setText(""+juego.getPuntuacion());
	}
	
	/**
	 * Método para refrescar la pantalla
	 */
	public void refrescarPantalla(){
		ventana.revalidate(); 
		ventana.repaint();
	}

	/**
	 * Método que devuelve el control del juego de una ventana
	 * @return un ControlJuego con el control del juego de la ventana
	 */
	public ControlJuego getJuego() {
		return juego;
	}

	/**
	 * Método para inicializar el programa
	 */
	public void inicializar(){
		//IMPORTANTE, PRIMERO HACEMOS LA VENTANA VISIBLE Y LUEGO INICIALIZAMOS LOS COMPONENTES.
		ventana.setVisible(true);
		inicializarComponentes();	
		inicializarListeners();		
	}



}
