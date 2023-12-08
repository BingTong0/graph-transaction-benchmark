package com.tb.graph.utils;

import java.awt.*;

import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.xy.XYDataset;

/**
 * Generate the charts of performance
 */
public class Charter {

    private static void displayChart(String title, JFreeChart chart) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new ChartPanel(chart), BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void createBarChart(String title, String categoryAxisLabel, String valueAxisLabel, CategoryDataset dataset) {
        JFreeChart barChart = ChartFactory.createBarChart(
                title,
                categoryAxisLabel,
                valueAxisLabel,
                dataset
        );
        displayChart(title, barChart);
    }

    public static void createXYLineChart(String title, String xAxisLabel, String yAxisLabel, XYDataset dataset) {
        JFreeChart xyLineChart = ChartFactory.createXYLineChart(
                title,
                xAxisLabel,
                yAxisLabel,
                dataset
        );
        displayChart(title, xyLineChart);
    }
}
