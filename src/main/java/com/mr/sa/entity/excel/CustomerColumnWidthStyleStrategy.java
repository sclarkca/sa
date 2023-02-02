package com.mr.sa.entity.excel;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import org.apache.poi.ss.usermodel.Cell;

import java.util.List;
/**
 * 列宽设置
 * memo:当数据不存在标题时，defaultStyle头部样式将失效，该设置将会生效
 */
public class CustomerColumnWidthStyleStrategy extends AbstractColumnWidthStyleStrategy {
    private static final int MAX_COLUMN_WIDTH = 7000;

    @Override
    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head,
                                  Integer relativeRowIndex, Boolean isHead) {
        writeSheetHolder.getSheet().setColumnWidth(cell.getColumnIndex(), MAX_COLUMN_WIDTH);
    }
}
