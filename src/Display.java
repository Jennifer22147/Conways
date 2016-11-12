/*
 * Created on May 24, 2004
 *
 * Latest update on April 21, 2011
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JComponent;

// Note that the JComponent is set up to listen for mouse clicks
// and mouse movement.  To achieve this, the MouseListener and
// MousMotionListener interfaces are implemented and there is additional
// code in init() to attach those interfaces to the JComponent.


public class Display extends JComponent implements MouseListener, MouseMotionListener {
	public static final int ROWS = 80;
	public static final int COLS = 100;
	public static Cell[][] cell = new Cell[ROWS][COLS];
	private final int X_GRID_OFFSET = 25; // 25 pixels from left
	private final int Y_GRID_OFFSET = 40; // 40 pixels from top
	private final int CELL_WIDTH = 5;
	private final int CELL_HEIGHT = 5;

	// Note that a final field can be initialized in constructor
	private final int DISPLAY_WIDTH;   
	private final int DISPLAY_HEIGHT;
	private StartButton startStop;
	private ClearButton clear;
	private NextButton next;
	private boolean paintloop = false;


	public Display(int width, int height) {
		DISPLAY_WIDTH = width;
		DISPLAY_HEIGHT = height;
		init();
	}


	public void init() {
		setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
		initCells();

		addMouseListener(this);
		addMouseMotionListener(this);

		// Example of setting up a button.
		// See the StartButton class nested below.
		startStop = new StartButton();
		startStop.setBounds(100, 550, 100, 36);
		clear = new ClearButton();
		clear.setBounds(300, 550, 100, 36);
		next = new NextButton();
		next.setBounds(500, 550, 100, 36);
		add(startStop);
		startStop.setVisible(true);
		add(clear);
		clear.setVisible(true);
		add(next);
		next.setVisible(true);
		repaint();
	}


	public void paintComponent(Graphics g) {
		final int TIME_BETWEEN_REPLOTS = 100; // change to your liking

		g.setColor(Color.BLACK);
		drawGrid(g);
		drawCells(g);
		drawButtons();

		if (paintloop) {
			try {
				Thread.sleep(TIME_BETWEEN_REPLOTS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			nextGeneration();
			repaint();
		}
	}


	public void initCells() {
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				cell[row][col] = new Cell(row, col);
			}
		}

		cell[36][22].setAlive(true); // sample use of cell mutator method
		cell[36][23].setAlive(true); // sample use of cell mutator method
		cell[36][24].setAlive(true); // sample use of cell mutator method
		cell[23][36].calcNeighbors(cell);
		cell[36][23].calcNeighbors(cell);
		cell[0][0].calcNeighbors(cell);
		cell[36][22].calcNeighbors(cell);
		System.out.println(cell[36][23].getNeighbors());



	}


	public void togglePaintLoop() {
		paintloop = !paintloop;
	}


	public void setPaintLoop(boolean value) {
		paintloop = value;
	}


	void drawGrid(Graphics g) {
		for (int row = 0; row <= ROWS; row++) {
			g.drawLine(X_GRID_OFFSET,
					Y_GRID_OFFSET + (row * (CELL_HEIGHT + 1)), X_GRID_OFFSET
					+ COLS * (CELL_WIDTH + 1), Y_GRID_OFFSET
					+ (row * (CELL_HEIGHT + 1)));
		}
		for (int col = 0; col <= COLS; col++) {
			g.drawLine(X_GRID_OFFSET + (col * (CELL_WIDTH + 1)), Y_GRID_OFFSET,
					X_GRID_OFFSET + (col * (CELL_WIDTH + 1)), Y_GRID_OFFSET
					+ ROWS * (CELL_HEIGHT + 1));
		}
	}


	void drawCells(Graphics g) {
		// Have each cell draw itself
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				// The cell cannot know for certain the offsets nor the height
				// and width; it has been set up to know its own position, so
				// that need not be passed as an argument to the draw method
				cell[row][col].draw(X_GRID_OFFSET, Y_GRID_OFFSET, CELL_WIDTH,
						CELL_HEIGHT, g);
			}
		}
	}


	private void drawButtons() {
		startStop.repaint();
		clear.repaint();
		next.repaint();
	}

	public void clear() {
		for (int g = 0; g < 100; g++) {
			for (int h = 0; h < 80 ; h++) {
				System.out.println(cell[h][g].getAlive());
				cell[h][g].setAlive(false);
				cell[h][g].setAliveNextTurn(false);
				repaint();

			}

		}
	}


	private void nextGeneration() {
		for (int j = 0; j < 100; j++) {
			for (int i = 0; i < 80 ; i++) {
				cell[i][j].calcNeighbors(cell);
				if(cell[i][j].getAlive()==true){
					if(cell[i][j].getNeighbors()<4 && cell[i][j].getNeighbors()>1){
						cell[i][j].setAliveNextTurn(true);
					}
					else  {
						cell[i][j].setAliveNextTurn(false);
					}
				}
				if (cell[i][j].getAlive()==false && cell[i][j].getNeighbors()==3){
					cell[i][j].setAliveNextTurn(true);
				}
			}
		}

		for (int a = 0; a < 100; a++) {
			for (int b = 0; b < 80 ; b++) {

				cell[b][a].setAlive(cell[b][a].getAliveNextTurn());
				repaint();
			}
		}
	}


	public void mouseClicked(MouseEvent arg0) {
		int CellX = (arg0.getX() -X_GRID_OFFSET)/(CELL_WIDTH+1);
		int CellY = (arg0.getY()-Y_GRID_OFFSET)/(CELL_HEIGHT+1);
		if((CellX>=0 && CellX<=99) && (CellY>=0 && CellY<=79)){

			getCell(arg0).setAlive(!getCell(arg0).getAlive());

			repaint();

		}				
	}


	//getting cell mouse is hovered on
	private Cell getCell(MouseEvent arg0){

		int CellX = (arg0.getX() -X_GRID_OFFSET)/(CELL_WIDTH+1);
		int CellY = (arg0.getY()-Y_GRID_OFFSET)/(CELL_HEIGHT+1);
		return cell[CellY][CellX];
	}


	public void mouseEntered(MouseEvent arg0) {

	}

	public void mouseExited(MouseEvent arg0) {

	}


	public void mousePressed(MouseEvent arg0) {

	}


	public void mouseReleased(MouseEvent arg0) {

	}


	public void mouseDragged(MouseEvent arg0) {

		int CellX = (arg0.getX() -X_GRID_OFFSET)/(CELL_WIDTH+1);
		int CellY = (arg0.getY()-Y_GRID_OFFSET)/(CELL_HEIGHT+1);

		if((CellX>=0 && CellX<=99) && (CellY>=0 && CellY<=79)){



			getCell(arg0).setAlive(true);

			repaint();
		}

	}


	public void mouseMoved(MouseEvent arg0) {
		/*Point point = arg0.getPoint();

			if (arg0.getX() >= 25 && arg0.getY() >=40){
				int column = (arg0.getX()-25)/5;
				int row = (arg0.getY()-40)/5;

					if(column >= 0 && row>+0 && column<100 && row <80){

					}
			}
		 */
	}


	private class StartButton extends JButton implements ActionListener {
		StartButton() {
			super("Start");
			addActionListener(this);

		}

		public void actionPerformed(ActionEvent arg0) {
			if (this.getText().equals("Start")) {
				togglePaintLoop();
				setText("Stop");
				nextGeneration();
			} else {
				togglePaintLoop();
				setText("Start");

			}	 

			repaint();
		}
	}




	private class ClearButton extends JButton implements ActionListener {
		ClearButton() {
			super("Clear");
			addActionListener(this);

		}

		public void actionPerformed(ActionEvent arg0) {
			System.out.println("clicked");
			clear();

		}




	}

	private class NextButton extends JButton implements ActionListener {
		NextButton() {
			super("Next");
			addActionListener(this);

		}

		public void actionPerformed(ActionEvent arg0) {

			if (paintloop==false) {
				nextGeneration();
			}

			repaint();
		}
	}




}