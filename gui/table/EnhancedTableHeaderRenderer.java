package com.github.lucachiaravalloti.enhancedjava.gui.table;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SortOrder;
import javax.swing.RowSorter.SortKey;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

public class EnhancedTableHeaderRenderer extends JPanel implements TableCellRenderer {

	private static final long serialVersionUID = 1L;

	final private JLabel title = new JLabel();
	final private JLabel icon = new JLabel();
	final private JLabel ordinal = new JLabel();
	final private Icon ascIcon = UIManager.getIcon("Table.ascendingSortIcon");
	final private Icon descIcon = UIManager.getIcon("Table.descendingSortIcon");

	public EnhancedTableHeaderRenderer() {

		super();

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(Box.createHorizontalGlue());
		add(title);
		add(icon);
		add(ordinal);
		add(Box.createHorizontalGlue());
		setBorder(UIManager.getBorder("TableHeader.cellBorder"));

		Font f = ordinal.getFont();
		ordinal.setFont(f.deriveFont(f.getStyle() & ~Font.BOLD));
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int rowIndex, int columnIndex) {

		icon.setIcon(null);

		// value is null if PROG enum value field columnName is set to null
		// but if title text remains unset or is set to null or is an empty string
		// header visualization has a strange behaviour
		if (value != null)
			title.setText(value.toString());
		else
			title.setText(" ");

		ordinal.setText(null);

		List<? extends SortKey> sortKeys = table.getRowSorter().getSortKeys();
		for (int i = 0; i < sortKeys.size(); i++) {

			SortKey sortKey = sortKeys.get(i);

			if (sortKey.getColumn() == table.convertColumnIndexToModel(columnIndex)) {

				icon.setIcon(sortKey.getSortOrder() == SortOrder.ASCENDING ? ascIcon : descIcon);

				if (sortKeys.size() > 1)

					ordinal.setText("(" + (i + 1) + ")");

				break;
			}
		}

		// I need to add some extra width to make the column width fit its contents
		// I also have to calculate the overall width myself, otherwise the automatic
		// fit doesn't work
		int width = 0;
		for (Component c : getComponents())
			width += c.getPreferredSize().width;

		setPreferredSize(new Dimension(10 + width, getPreferredSize().height));

		return this;
	}
}
