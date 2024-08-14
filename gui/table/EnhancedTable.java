package com.github.lucachiaravalloti.enhancedjava.gui.table;

import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import ibdatadownloader.datastructure.Security;
import util.Constant.SecurityField;

public class EnhancedTable extends JTable {

	private static final long serialVersionUID = 1L;

	public EnhancedTable(EnhancedAbstractTableModel<SecurityField, Security> mdl) {

		super(mdl);

		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setFillsViewportHeight(true);

		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(mdl);

		for (int i = 0; i < getColumnCount(); i++)
			sorter.setSortable(i, false);

		setRowSorter(sorter);

		JTableHeader header = getTableHeader();

		header.setDefaultRenderer(new EnhancedTableHeaderRenderer());

		header.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {

				Point pointAtClick = e.getPoint();
				int columnIndexInView = header.columnAtPoint(pointAtClick);
				int columnIndexInModel = convertColumnIndexToModel(columnIndexInView);

				if (columnIndexInView == -1)

					return;

				Rectangle restrictedHeaderRect = header.getHeaderRect(columnIndexInView);
				restrictedHeaderRect.grow(-3, 0);

				if (restrictedHeaderRect.contains(pointAtClick)) {

					if (columnIndexInModel == mdl.getProgColumnIndex()) {

						if (e.getClickCount() == 2)

							fitTableToContent();

						return;
					}

					// begin SORTING stuff

					SortKey sortKeyAscending = new SortKey(columnIndexInModel, SortOrder.ASCENDING);
					SortKey sortKeyDescending = new SortKey(columnIndexInModel, SortOrder.DESCENDING);

					List<? extends SortKey> oldSortKeys = sorter.getSortKeys();
					List<SortKey> newSortKeys;

					if (e.isShiftDown()) {

						newSortKeys = new ArrayList<>(oldSortKeys);

						int index;
						if ((index = oldSortKeys.indexOf(sortKeyAscending)) != -1)

							newSortKeys.set(index, sortKeyDescending);
						else if ((index = oldSortKeys.indexOf(sortKeyDescending)) != -1)

							newSortKeys.set(index, sortKeyAscending);
						else

							newSortKeys.add(sortKeyAscending);
					} else {

						newSortKeys = new ArrayList<>();

						if (oldSortKeys.contains(sortKeyAscending))

							newSortKeys.add(sortKeyDescending);
						else

							newSortKeys.add(sortKeyAscending);
					}

					sorter.setSortKeys(newSortKeys);

					// You may need to increase the width of your columns to make room for arrow
					// icons and indexes after header titles
					for (SortKey sortKey : newSortKeys) {

						int i = sortKey.getColumn();
						if (getColumnModel().getColumn(i).getWidth() < calcHeaderColumnPreferredWidth(i))
							fitColumnToHeaderContent(i);
					}
					// end SORTING stuff
				} else if (e.getClickCount() == 2) {

					if (pointAtClick.x < restrictedHeaderRect.x + restrictedHeaderRect.width / 2)
						columnIndexInView--;

					fitColumnToContent(columnIndexInView);
				}
			}
		});

		// begin CELL CONTENT ALIGNMENT stuff
		for (int i = 0; i < getColumnCount(); i++)

			if ((mdl.getColumnClass(i) == String.class || mdl.getColumnClass(i).isEnum())
					&& i != mdl.getProgColumnIndex()) {

				Integer horizontalAlignment = mdl.getHorizontalAlignment(i);

				if (horizontalAlignment != null) {

					DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
					renderer.setHorizontalAlignment(horizontalAlignment);
					getColumnModel().getColumn(i).setCellRenderer(renderer);
				}
			}
		// end CELL CONTENT ALIGNMENT stuff

		if (mdl.isProgColumnPresent()) {

			getColumnModel().getColumn(mdl.getProgColumnIndex()).setCellRenderer(new DefaultTableCellRenderer() {

				private static final long serialVersionUID = 1L;

				private JLabel label = new JLabel();

				@Override
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
						boolean hasFocus, int rowIndex, int columnIndex) {

					label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, rowIndex,
							columnIndex);

					label.setBackground(UIManager.getColor("Label.background"));
					label.setHorizontalAlignment(SwingConstants.CENTER);

					Font f = label.getFont();
					if (isSelected)
						label.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
					else
						label.setFont(f.deriveFont(f.getStyle() & ~Font.BOLD));

					label.setText("" + rowIndex);

					return label;
				}
			});
		}

		fitTableToContent();
	}

	public void fitTableToContent() {

		for (int i = 0; i < getColumnCount(); i++)

			fitColumnToContent(i);
	}

	public void fitColumnToContent(int columnIndexInView) {

		int preferredWidth = Math.max(calcHeaderColumnPreferredWidth(columnIndexInView),
				calcCellsColumnPreferredWidth(columnIndexInView));

		TableColumn column = getColumnModel().getColumn(columnIndexInView);
		if (column.getWidth() != preferredWidth)
			column.setPreferredWidth(preferredWidth);
	}

	public void fitColumnToHeaderContent(int columnIndexInView) {

		int preferredWidth = calcHeaderColumnPreferredWidth(columnIndexInView);

		TableColumn column = getColumnModel().getColumn(columnIndexInView);
		if (column.getWidth() != preferredWidth)
			column.setPreferredWidth(preferredWidth);
	}

	public int calcHeaderColumnPreferredWidth(int columnIndexInView) {

		TableColumn column = getColumnModel().getColumn(columnIndexInView);

		return getTableHeader().getDefaultRenderer()
				.getTableCellRendererComponent(this, column.getHeaderValue(), false, false, -1, columnIndexInView)
				.getPreferredSize().width + getIntercellSpacing().width;
	}

	public int calcCellsColumnPreferredWidth(int columnIndexInView) {

		int preferredWidth = 0;
		for (int i = 0; i < getRowCount(); i++) {

			TableCellRenderer renderer = getCellRenderer(i, columnIndexInView);
			int rendererWidth = super.prepareRenderer(renderer, i, columnIndexInView).getPreferredSize().width;
			preferredWidth = Math.max(rendererWidth + getIntercellSpacing().width, preferredWidth);
		}

		return preferredWidth;
	}
}
