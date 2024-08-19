package com.github.lucachiaravalloti.test.enhancedjava.gui.table1;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.github.lucachiaravalloti.enhancedjava.gui.table.EnhancedTable;
import com.github.lucachiaravalloti.test.enhancedjava.gui.table1.Person.Status;

public class ExampleTable {

	public static void main(String[] args) {

		TableModel tableModel = new TableModel(loadPersons());
		EnhancedTable<TableFields, Person> table = new EnhancedTable<>(tableModel) 
		{
			
			private static final long serialVersionUID = 1L;

			@Override
			public Dimension getPreferredScrollableViewportSize() {
				return new Dimension(super.getPreferredSize().width, super.getPreferredScrollableViewportSize().height);
			}
		};

		EventQueue.invokeLater(() -> {

			try {

				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				
				JFrame frame = new JFrame("ExampleTable1");
				frame.setSize(screenSize.width, screenSize.height);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				scrollPane.getViewport().addChangeListener(new ChangeListener() {
					
					@Override
					public void stateChanged(ChangeEvent e) {

						frame.revalidate();
					}
				});
				
				frame.getContentPane().setLayout(new FlowLayout());
				frame.getContentPane().add(scrollPane);
				frame.setVisible(true);
			} catch (Exception e) {

				e.printStackTrace();
			}
		});
	}

	static List<Person> loadPersons() {

		List<Person> list = new LinkedList<Person>();
		String filePath = System.getProperty("user.dir") + "/data.csv";
		try {

			list = Files.lines(Paths.get(filePath)).map(line -> parse(line)).collect(Collectors.toList());
		} catch (IOException e) {

			e.printStackTrace();
			System.exit(-1);
		}

		return list;
	}

	static Person parse(String line) {

		DateFormat d = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
		String[] values = line.split(",");
		try {

			return new Person(values[0], values[1], values[2], Status.valueOf(values[3]), d.parse(values[4]));
		} catch (ParseException e) {

			e.printStackTrace();
			System.exit(-1);
		}

		return null;
	}
}
