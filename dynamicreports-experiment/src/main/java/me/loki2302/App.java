package me.loki2302;

import lombok.Getter;
import static me.loki2302.db.Tables.*;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.chart.BarChartBuilder;
import net.sf.dynamicreports.report.builder.chart.PieChartBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

@SpringBootApplication
public class App implements CommandLineRunner {
    public static void main(String[] args) throws DRException {
        SpringApplication.run(App.class, args);
    }

    @Autowired
    private DSLContext dslContext;

    @Override
    public void run(String... args) throws Exception {
        int categoryOneId = dslContext.insertInto(CATEGORIES, CATEGORIES.NAME)
                .values("Category One")
                .returning(CATEGORIES.ID)
                .fetchOne()
                .getId();

        int categoryTwoId = dslContext.insertInto(CATEGORIES, CATEGORIES.NAME)
                .values("Category Two")
                .returning(CATEGORIES.ID)
                .fetchOne()
                .getId();

        for(int i = 0; i < 10; ++i) {
            String name = String.format("Item #%d", i + 1);
            int categoryId = i % 2 == 0 ? categoryOneId : categoryTwoId;
            int quantity = 1 + 2 * i;
            dslContext
                    .insertInto(ITEMS, ITEMS.NAME, ITEMS.CATEGORYID, ITEMS.QUANTITY)
                    .values(name, categoryId, quantity)
                    .execute();
        }

        ResultSet resultSet = dslContext
                .select(
                        ITEMS.NAME.as("itemName"),
                        ITEMS.QUANTITY.as("quantity"),
                        CATEGORIES.NAME.as("categoryName")
                )
                .from(ITEMS).join(CATEGORIES).on(CATEGORIES.ID.eq(ITEMS.CATEGORYID))
                .orderBy(CATEGORIES.NAME.asc())
                .fetch()
                .intoResultSet();

        System.out.println(resultSet);

        TextColumnBuilder<String> itemColumn =
                col.column("Item", "itemName", type.stringType());
        TextColumnBuilder<String> categoryColumn =
                col.column("Category", "categoryName", type.stringType());
        TextColumnBuilder<Integer> quantityColumn =
                col.column("Quantity", "quantity", type.integerType());

        report()
                .columns(itemColumn, categoryColumn, quantityColumn)
                .groupBy(categoryColumn)
                .setDataSource(resultSet)
                .toPdf(export.pdfExporter("fromdb.pdf"));
    }

    private static void makeDummyReport() throws Exception {
        List<Item> items = new ArrayList<>();
        for(int i = 0; i < 10; ++i) {
            // the weird thing is, it won't actual do any grouping if I do i % 2
            items.add(new Item(String.format("Item #%d", i + 1), String.format("Category #%d", i < 5 ? 1 : 2), 1 + 2 * i));
        }

        StyleBuilder boldStyle = stl.style().bold();
        StyleBuilder columnTitleStyle = stl.style(boldStyle).setBorder(stl.pen1Point());

        TextColumnBuilder<String> itemColumn =
                col.column("Item", "name", type.stringType());
        TextColumnBuilder<Integer> quantityColumn =
                col.column("Quantity", "quantity", type.integerType());
        TextColumnBuilder<String> categoryColumn =
                col.column("Category", "category", type.stringType());

        BarChartBuilder myBarChart = cht.barChart()
                .setTitle("My Fancy Bar Chart")
                .setCategory(itemColumn)
                .addSerie(cht.serie(quantityColumn));

        PieChartBuilder myPieChart = cht.pieChart()
                .setTitle("My Fancy Pie Chart")
                .setKey(itemColumn)
                .addSerie(cht.serie(quantityColumn));

        JasperReportBuilder r = report()
                .setColumnTitleStyle(columnTitleStyle)
                .setDetailStyle(stl.style(stl.pen1Point()))
                .setColumnStyle(stl.style(stl.pen1Point()))
                .highlightDetailEvenRows()
                .title(cmp.text("My Fancy Report"))
                .columns(
                        itemColumn,
                        quantityColumn)
                .subtotalsAtSummary(sbt.sum(quantityColumn))
                .groupBy(categoryColumn)
                .summary(myBarChart, myPieChart)
                .setDataSource(items)
                .toPdf(export.pdfExporter("1.pdf"))
                .show(); // .show() won't work with Spring

        concatenatedReport()
                .setContinuousPageNumbering(true)
                .concatenate(r, r, r)
                .toPdf(export.pdfExporter("2.pdf"));
    }

    @Getter
    public static class Item {
        public String name;
        public String category;
        public int quantity;

        public Item(String name, String category, int quantity) {
            this.name = name;
            this.category = category;
            this.quantity = quantity;
        }
    }
}
