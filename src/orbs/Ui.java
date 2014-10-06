package orbs;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Ui extends Frame {

	Simulator simulator;

	public Ui() {
		simulator = new Simulator();
		setTitle("snurbs");
		addWindowListener(new TestWindowListener());

		add(new DrawingPanel(simulator, this));

		setSize(500, 500);
		setVisible(true);
	}

	class DrawingPanel extends Panel {

		Ui ui;

		public DrawingPanel(final Simulator simulator, final Ui ui) {
			super();
			this.simulator = simulator;
			this.ui = ui;
			addKeyListener(new KeyListener() {

				@Override
				public void keyTyped(KeyEvent e) {
					simulator.nextStep();
					System.out.println("event");
					ui.revalidate();
				}

				@Override
				public void keyReleased(KeyEvent e) {
				}

				@Override
				public void keyPressed(KeyEvent e) {
				}
			});
		}


		private Simulator simulator;

		@Override
		public void paint(Graphics g) {

			int offset = 250;
			g.setColor(Color.BLACK);
			for(GeometricObject go : simulator.objects) {
				g.drawOval((int)go.x-go.r+offset, (int)go.y-go.r+offset, go.r*2, go.r*2);
			}
			System.out.println("painted black");

		}

		@Override
		public void update(Graphics g) {
			super.update(g);
			paint(g);
		}

	}

	class TestWindowListener extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent e) {
			e.getWindow().dispose();
			System.exit(0);
		}
	}

	public static void main(String args[]) {
		Ui ui = new Ui();
	}
}
