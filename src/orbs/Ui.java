package orbs;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Ui extends Frame {

	Simulator simulator;

	public Ui() {
		simulator = new Simulator();
		setTitle("snurbs");
		addWindowListener(new TestWindowListener());

		add(new DrawingPanel(simulator, this));

		setSize(800, 800);
		setVisible(true);
	}

	class DrawingPanel extends Panel {

		private Ui ui;

		public DrawingPanel(final Simulator simulator, final Ui ui) {
			super();
			this.simulator = simulator;
			this.ui = ui;

			addMouseListener(new MouseListener() {

				@Override
				public void mouseReleased(MouseEvent arg0) {
				}

				@Override
				public void mousePressed(MouseEvent arg0) {

					simulator.nextStep();
					ui.getComponent(0).setVisible(false);
					ui.getComponent(0).setVisible(true);

				}

				@Override
				public void mouseExited(MouseEvent arg0) {
				}

				@Override
				public void mouseEntered(MouseEvent arg0) {
				}

				@Override
				public void mouseClicked(MouseEvent arg0) {
				}
			});
		}


		private Simulator simulator;

		@Override
		public void paint(Graphics g) {

			int offset = 400;
			for(GeometricObject go : simulator.objects) {
				if (simulator.executingObject == simulator.objects.indexOf(go)) {
					g.setColor(Color.red);
				} else {
					g.setColor(Color.black);
				}
				g.drawString(Integer.toString(go.id), (int)go.x+offset-2, (int)go.y+offset+4);
				g.drawOval((int)go.x-go.r+offset, (int)go.y-go.r+offset, go.r*2, go.r*2);
			}

			if (simulator.lastTargets != null) {
				g.setColor(Color.blue);
				for (Target target : simulator.lastTargets) {
					g.drawOval((int)target.x - 25 + offset,
								(int)target.y - 25 + offset,
								50,
								50);
				}
			}
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
