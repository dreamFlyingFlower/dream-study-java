package com.wy.qq.student;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * 学生端显式窗口
 */
public class StudentUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private JLabel lbl;

	public StudentUI() {
		init();
		this.setVisible(true);
	}

	private void init() {
		this.setTitle("学生端");
		this.setLayout(null);
		this.setBounds(0, 0, 1366, 768);

		lbl = new JLabel();
		lbl.setBounds(0, 0, 1366, 768);
		this.add(lbl);

		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				System.exit(-1);
			}
		});
	}

	/**
	 * 更新ui
	 */
	public void updateUI(byte[] frameData) {
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(frameData);
			BufferedImage image = ImageIO.read(bais);
			ImageIcon icon = new ImageIcon(image);
			lbl.setIcon(icon);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}