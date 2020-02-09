package de.sbicher.tc_clown.ui;

import de.sbicher.tc_clown.model.TcDate;
import de.sbicher.tc_clown.model.TcFileSize;
import de.sbicher.tc_clown.settings.TcSettings;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.text.SimpleDateFormat;
import javax.inject.Inject;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class TcFileTableRenderer implements TableCellRenderer {

    private final TcSettings settings;

    @Inject
    public TcFileTableRenderer(TcSettings settings) {
        this.settings = settings;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        String display = value == null?"":getDisplayValue(value);
        JLabel comp = new JLabel(display);
        comp.setFont((Font) settings.getSettingValue(TcSettings.KEY_FILE_TABLE_FONT));

        boolean isEvenRow = row%2 == 0;

        String fgColorSetting = isSelected?TcSettings.KEY_FILE_TABLE_FG_COLOR_SELECTED_ROW :TcSettings.KEY_FILE_TABLE_FG_COLOR_UNSELECTED_ROW;
        comp.setForeground((Color) settings.getSettingValue(fgColorSetting));

        String bgColorSetting = isSelected?TcSettings.KEY_FILE_TABLE_BG_COLOR_SELECTED_ROW:(isEvenRow?TcSettings.KEY_FILE_TABLE_BG_COLOR_EVEN_ROW :TcSettings.KEY_FILE_TABLE_BG_COLOR_ODD_ROW);
        comp.setBackground((Color) settings.getSettingValue(bgColorSetting));


        comp.setOpaque(true);


        return comp;
    }

    private String getDisplayValue(Object value) {
        if (value instanceof TcFileSize) {
            return "x Bytes";
        }
        if (value instanceof TcDate) {
            SimpleDateFormat format = new SimpleDateFormat((String) settings.getSettingValue(TcSettings.KEY_DATE_TIME_FORMAT));
            return format.format(((TcDate) value).getDate());
        }

        return value.toString();
    }
}
