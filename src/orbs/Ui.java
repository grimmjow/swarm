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

		add(new DrawingPanel(simulator));

		setSize(500, 500);
		setVisible(true);

		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				simulator.nextStep();
				System.out.println("event");
				getComponent(0).setVisible(false);
				getComponent(0).setVisible(true);

			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
	}

	class DrawingPanel extends Panel {

		public DrawingPanel(final Simulator simulator) {
			super();
			this.simulator = simulator;
		}


		private Simulator simulator;

		@Override
		public void paint(Graphics g) {

			int offset = 250;
			g.setColor(Color.BLACK);
			for(GeometricObject go : simulator.objects) {
				g.drawString(Integer.toString(go.id), (int)go.x+offset-2, (int)go.y+offset+4);
				g.drawOval((int)go.x-go.r+offset, (int)go.y-go.r+offset, go.r*2, go.r*2);
			}
			System.out.println("painted black");

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
