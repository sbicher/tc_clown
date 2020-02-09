package de.sbicher.tc_clown.ui;

import de.sbicher.tc_clown.model.TcDate;
import de.sbicher.tc_clown.model.TcFileInfo;
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
        TcFileInfo fileInfo = ( (TcFileTableModel) table.getModel()).getFileInfo(row);
        String display = value == null ? "" : getDisplayValue(value, fileInfo);

        JLabel comp = new JLabel(display);
        comp.setFont((Font) settings.getSettingValue(TcSettings.KEY_FILE_TABLE_FONT));

        boolean isEvenRow = row % 2 == 0;

        String fgColorSetting = isSelected ? TcSettings.KEY_FILE_TABLE_FG_COLOR_SELECTED_ROW : TcSettings.KEY_FILE_TABLE_FG_COLOR_UNSELECTED_ROW;
        comp.setForeground((Color) settings.getSettingValue(fgColorSetting));

        String bgColorSetting = isSelected ? TcSettings.KEY_FILE_TABLE_BG_COLOR_SELECTED_ROW : getBgColorSettingsForRow(table, row);
        comp.setBackground((Color) settings.getSettingValue(bgColorSetting));

        comp.setOpaque(true);

        return comp;
    }

    private String getBgColorSettingsForRow(JTable table, int row) {
        TcFileInfo fileInfo = ((TcFileTableModel) table.getModel()).getFileInfo(row);
        if (fileInfo.getFile() == null || fileInfo.getFile().isDirectory()) {
            return TcSettings.KEY_FILE_TABLE_BG_COLOR_DIRECTORY_ROW;
        }
        return TcSettings.KEY_FILE_TABLE_BG_COLOR_FILE_ROW;
    }

    private String getDisplayValue(Object value, TcFileInfo fileInfo) {
        if (value instanceof TcFileSize) {
            return getDisplayValueFileSize(fileInfo);
        }

        if (value instanceof TcDate) {
            if (fileInfo == TcFileInfo.NAVIGATE_UP) {
                return "";
            }
            SimpleDateFormat format = new SimpleDateFormat((String) settings.getSettingValue(TcSettings.KEY_DATE_TIME_FORMAT));
            return format.format(((TcDate) value).getDate());
        }

        return value.toString();
    }

    private String getDisplayValueFileSize(TcFileInfo value) {
        if (value == TcFileInfo.NAVIGATE_UP) {
            return "";
        }
        if (value.getFile().isDirectory()) {
            return "<DIR>";
        }

        return humanReadableByteCountBin(value.getFile().length());
    }

    /**
     * Converts the Bytes to human readable format, source: https://stackoverflow.com/questions/3758606/how-to-convert-byte-size-into-human-readable-format-in-java
     * @param bytes byte count to format
     * @return Readable String
     */
    private static String humanReadableByteCountBin(long bytes) {
        if (bytes == 0) {
            return "0";
        }

        long b = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        String result = b < 1024L ? bytes + " B"
                : b <= 0xfffccccccccccccL >> 40 ? String.format("%.1f KB", bytes / 0x1p10)
                : b <= 0xfffccccccccccccL >> 30 ? String.format("%.1f MB", bytes / 0x1p20)
                : b <= 0xfffccccccccccccL >> 20 ? String.format("%.1f GB", bytes / 0x1p30)
                : b <= 0xfffccccccccccccL >> 10 ? String.format("%.1f TB", bytes / 0x1p40)
                : b <= 0xfffccccccccccccL ? String.format("%.1f PB", (bytes >> 10) / 0x1p40)
                : String.format("%.1f EB", (bytes >> 20) / 0x1p40);

            int firstSpaceIndex = result.indexOf(' ');

        while (result.length() < 8) {
            result = result.substring(0,firstSpaceIndex) + " " + result.substring(firstSpaceIndex);
        }

        return result;
    }
}
