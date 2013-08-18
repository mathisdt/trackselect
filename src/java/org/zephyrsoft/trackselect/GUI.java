package org.zephyrsoft.trackselect;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import org.zephyrsoft.trackselect.model.Chapter;
import org.zephyrsoft.trackselect.model.Disc;
import org.zephyrsoft.trackselect.model.Title;

/**
 * The user interface.
 * 
 * @author Mathis Dirksen-Thedens
 */
public class GUI extends JFrame implements LogTarget {
	
	private static final Color TITLE_COLOR = Color.RED;
	
	private static final long serialVersionUID = -2316986129445458114L;
	
	private Service service;
	
	private Disc currentDisc;
	
	private Map<String, JCheckBox> checkboxes = new HashMap<String, JCheckBox>();
	private JPanel panel;
	
	private JScrollPane logScrollPane;
	private JTextArea log;
	private JTextField globalPrefix;
	
	public GUI(Service service) {
		this.service = service;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[] {1.0};
		gridBagLayout.rowWeights = new double[] {1.0};
		getContentPane().setLayout(gridBagLayout);
		
		JPanel globalPanel = new JPanel();
		globalPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		GridBagConstraints gbc_globalPanel = new GridBagConstraints();
		gbc_globalPanel.fill = GridBagConstraints.BOTH;
		gbc_globalPanel.gridx = 0;
		gbc_globalPanel.gridy = 0;
		getContentPane().add(globalPanel, gbc_globalPanel);
		GridBagLayout gbl_globalPanel = new GridBagLayout();
		gbl_globalPanel.columnWeights = new double[] {1.0};
		gbl_globalPanel.rowWeights = new double[] {0.0, 0.0, 1.0, 0.0, 0.2};
		globalPanel.setLayout(gbl_globalPanel);
		
		JButton btnReadDisc = new JButton("Read Disc");
		GridBagConstraints gbc_btnReadDisc = new GridBagConstraints();
		gbc_btnReadDisc.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnReadDisc.insets = new Insets(0, 0, 5, 0);
		gbc_btnReadDisc.gridx = 0;
		gbc_btnReadDisc.gridy = 0;
		globalPanel.add(btnReadDisc, gbc_btnReadDisc);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.getVerticalScrollBar().setUnitIncrement(25);
		
		JPanel prefixPanel = new JPanel();
		GridBagConstraints gbc_prefixPanel = new GridBagConstraints();
		gbc_prefixPanel.insets = new Insets(0, 0, 5, 0);
		gbc_prefixPanel.fill = GridBagConstraints.BOTH;
		gbc_prefixPanel.gridx = 0;
		gbc_prefixPanel.gridy = 1;
		globalPanel.add(prefixPanel, gbc_prefixPanel);
		prefixPanel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblGlobalPrefix = new JLabel("Global Prefix:");
		lblGlobalPrefix.setBorder(new EmptyBorder(0, 0, 0, 10));
		prefixPanel.add(lblGlobalPrefix, BorderLayout.WEST);
		
		globalPrefix = new JTextField();
		prefixPanel.add(globalPrefix, BorderLayout.CENTER);
		globalPrefix.setColumns(10);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.weighty = 1.0;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 2;
		globalPanel.add(scrollPane, gbc_scrollPane);
		
		panel = new JPanel();
		scrollPane.setViewportView(panel);
		
		JButton btnStartExtraction = new JButton("Queue Extraction");
		btnStartExtraction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				extract();
			}
		});
		GridBagConstraints gbc_btnStartExtraction = new GridBagConstraints();
		gbc_btnStartExtraction.insets = new Insets(0, 0, 5, 0);
		gbc_btnStartExtraction.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnStartExtraction.gridx = 0;
		gbc_btnStartExtraction.gridy = 3;
		globalPanel.add(btnStartExtraction, gbc_btnStartExtraction);
		
		logScrollPane = new JScrollPane();
		GridBagConstraints gbc_logScrollPane = new GridBagConstraints();
		gbc_logScrollPane.fill = GridBagConstraints.BOTH;
		gbc_logScrollPane.gridx = 0;
		gbc_logScrollPane.gridy = 4;
		globalPanel.add(logScrollPane, gbc_logScrollPane);
		
		log = new JTextArea();
		logScrollPane.setViewportView(log);
		log.setLineWrap(true);
		log.setEditable(false);
		btnReadDisc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadData();
				presentData();
			}
		});
		
		setBounds(100, 100, 590, 730);
		setTitle("TrackSelect");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// stop any running processes
				GUI.this.service.cancelAllJobs();
				// exit
				dispose();
				System.exit(0);
			}
		});
	}
	
	private void loadData() {
		currentDisc = service.loadDiscData();
	}
	
	private void setSelected(Title title, Chapter chapter, boolean state) {
		JCheckBox box = checkboxes.get(title.getNumber() + (chapter == null ? "" : "-" + chapter.getNumber()));
		if (box.isSelected() != state) {
			box.doClick();
		}
	}
	
	private void extract() {
		if (currentDisc != null) {
			for (Title title : currentDisc) {
				if (title.isSelected()) {
					service.extract(title.getNumber(), null, globalPrefix.getText() + title.getName(), GUI.this);
					log("queued title " + title.getNumber() + " with name " + globalPrefix.getText() + title.getName());
					setSelected(title, null, false);
				}
				for (Chapter chapter : title) {
					if (chapter.isSelected()) {
						service.extract(title.getNumber(), chapter.getNumber(),
							globalPrefix.getText() + chapter.getName(), GUI.this);
						log("queued chapter " + title.getNumber() + " / chapter " + chapter.getNumber() + " with name "
							+ globalPrefix.getText() + chapter.getName());
						setSelected(title, chapter, false);
					}
				}
			}
		}
	}
	
	@Override
	public synchronized void log(String text) {
		log.append(text);
		log.append("\n");
		logScrollPane.getViewport()
			.setViewPosition(new Point(0, log.getSize().height - logScrollPane.getSize().height));
	}
	
	private void presentData() {
		globalPrefix.setText(currentDisc.getName() + "/");
		
		panel.removeAll();
		
		GridBagLayout gbl_panel = new GridBagLayout();
		// give last column weight 1, all previous 0 so the textfield gets all available space
		gbl_panel.columnWeights = new double[] {0, 0, 0, 0, 1};
		panel.setLayout(gbl_panel);
		
		int y = 0;
		for (Title title : currentDisc) {
			y = addSelectable(y, title, null);
		}
		panel.revalidate();
	}
	
	private int addSelectable(int y, final Title title, final Chapter chapter) {
		if (title == null) {
			throw new IllegalArgumentException("title may not be null");
		}
		final boolean isTitle = (chapter == null);
		
		@SuppressWarnings("null")
		JLabel description = new JLabel((isTitle ? "Title " + title.getNumber() : "Chapter " + chapter.getNumber()));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, (isTitle ? 5 : 45), 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = y;
		if (isTitle) {
			description.setForeground(TITLE_COLOR);
		}
		panel.add(description, gbc);
		
		JButton btn = new JButton("Play");
		btn.addActionListener(new ActionListener() {
			@SuppressWarnings("null")
			@Override
			public void actionPerformed(ActionEvent e) {
				service.play(title.getNumber(), (isTitle ? null : chapter.getNumber()));
			}
		});
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 0, 5, 5);
		gbc.gridx = 1;
		gbc.gridy = y;
		panel.add(btn, gbc);
		
		@SuppressWarnings("null")
		JLabel length = new JLabel((isTitle ? title.getLength() : chapter.getLength()));
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 10, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 2;
		gbc.gridy = y;
		if (isTitle) {
			length.setForeground(TITLE_COLOR);
		}
		panel.add(length, gbc);
		
		final JCheckBox selected = new JCheckBox();
		checkboxes.put(title.getNumber() + (chapter == null ? "" : "-" + chapter.getNumber()), selected);
		selected.addActionListener(new ActionListener() {
			@SuppressWarnings("null")
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isTitle) {
					title.setSelected(selected.isSelected());
				} else {
					chapter.setSelected(selected.isSelected());
				}
			}
		});
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 0, 5, 5);
		gbc.gridx = 3;
		gbc.gridy = y;
		panel.add(selected, gbc);
		
		final JTextField name = new JTextField();
		name.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				// nothing to do
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// nothing to do
			}
			
			@SuppressWarnings("null")
			@Override
			public void keyReleased(KeyEvent e) {
				String text = name.getText();
				if (isTitle) {
					title.setName(text);
				} else {
					chapter.setName(text);
				}
				
				boolean shouldBeSelected = (text != null && !text.isEmpty());
				setSelected(title, chapter, shouldBeSelected);
			}
		});
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 0, 5, 5);
		gbc.gridx = 4;
		gbc.gridy = y;
		panel.add(name, gbc);
		
		y++;
		
		if (isTitle) {
			for (Chapter subChapter : title) {
				y = addSelectable(y, title, subChapter);
			}
		}
		
		return y;
	}
}
