package com.mycompany.simplestopwatch;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;

public class Stopwatch extends JFrame implements ActionListener {

    private JButton startButton;
    private JButton resetButton;
    private JButton splitButton;
    
    private JLabel timeLabel;
    private JLabel splitLabel;
    
    private JPanel splitPanel;
    private JScrollPane scrollPane;
    
    private int elapsedTime = 0;
    private int hour = 0;
    private int minute = 0;
    private int second = 0;
    private int splitCount = 0;
    private int previousTime = 0;
    private int splitSeconds;
    private int splitMinutes;
    private int splitHours;
    
    private String hourString;
    private String minuteString;
    private String secondString;
    
    private boolean timeStarted = false;
    
    private Timer timer = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            elapsedTime += 1000;
            hour = (elapsedTime / 3_600_000);
            minute = (elapsedTime / 60_000) % 60;
            second = (elapsedTime/ 1000) % 60;
            
            timeLabel.setText(getTimeLabel(hour, minute, second));
        }        
    });
    
    public Stopwatch() {
        this.setTitle("Stopwatch");
        this.setSize(400, 400);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(Color.GRAY);
            
        timeLabel = new JLabel();
        timeLabel.setText(getTimeLabel(hour, minute, second));
        timeLabel.setBounds(45, 40, 300, 90);
        timeLabel.setFont(new Font("Verdana", Font.PLAIN, 40));
        timeLabel.setBorder(BorderFactory.createBevelBorder(1));
        timeLabel.setHorizontalAlignment(JTextField.CENTER);
        timeLabel.setForeground(Color.GREEN);
        timeLabel.setBackground(Color.BLACK);
        timeLabel.setOpaque(true);
        
        startButton = new JButton("START");
        startButton.setBounds(45, 135, 100, 30);
        startButton.setFont(new Font("Verdana", Font.PLAIN, 15));
        startButton.setFocusable(false);
        startButton.addActionListener(this);
        
        splitButton = new JButton("SPLIT");
        splitButton.setBounds(145, 135, 100, 30);
        splitButton.setFont(new Font("Verdana", Font.PLAIN, 15));
        splitButton.setEnabled(false);
        splitButton.setFocusable(false);
        splitButton.addActionListener(this);
        
        resetButton = new JButton("RESET");
        resetButton.setBounds(245, 135, 100, 30);
        resetButton.setFont(new Font("Verdana", Font.PLAIN, 15));
        resetButton.setFocusable(false);
        resetButton.addActionListener(this);
        
        splitPanel = new JPanel();
        splitPanel.setLayout(new BoxLayout(splitPanel, BoxLayout.Y_AXIS));
        splitPanel.setAlignmentX(CENTER_ALIGNMENT);

        scrollPane = new JScrollPane(splitPanel);
        scrollPane.setBounds(45, 170, 300, 160);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                
        this.add(timeLabel);
        this.add(startButton);
        this.add(splitButton);
        this.add(resetButton);
        this.add(scrollPane);
        this.setVisible(true);
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == startButton) {
            if (!timeStarted) {
                timeStarted = true;
                startButton.setText("STOP");
                splitButton.setEnabled(true);
                timer.start();
            }
            else {
                timeStarted = false;
                startButton.setText("START");
                startButton.setEnabled(false);
                splitButton.setEnabled(false);
                
                addSplitTimeLabel();
                
                timer.stop();
            }
        }
        
        if (e.getSource() == splitButton) {

            int currentTime = elapsedTime;
            int splitDuration = currentTime - previousTime;
            previousTime = currentTime;
           
            splitSeconds = splitDuration / 1000;
            splitMinutes = (splitSeconds / 60) % 60;
            splitHours = (splitMinutes / 60) % 60;
            
            addSplitTimeLabel();

        }
        
        if (e.getSource() == resetButton) {
            timeStarted = false;
            startButton.setText("START");
            resetTime();
        }
    }
    
    private void resetTime() {
        timer.stop();
        
        elapsedTime = 0;
        hour = 0;
        minute = 0;
        second = 0;
        
        timeLabel.setText(getTimeLabel(hour, minute, second));
        startButton.setEnabled(true);
        clearSplitPanel();
    }
    
    private void addSplitTimeLabel() {
        
        splitLabel = new JLabel("Time #" + (splitCount + 1) + " " + getTimeLabel(splitHours, splitMinutes, splitSeconds) 
                + " " + getTimeLabel(hour, minute, second));
        splitLabel.setFont(new Font("Verdana", Font.PLAIN, 15));
        splitLabel.setAlignmentX(CENTER_ALIGNMENT);
            
        splitPanel.add(splitLabel);
        splitCount++;
            
        splitPanel.revalidate();
        splitPanel.repaint();       
    }
    
    private String getTimeLabel(int hours, int minutes, int seconds) {
        hourString = String.format("%02d", hours);
        minuteString = String.format("%02d", minutes);
        secondString = String.format("%02d", seconds);

        return hourString + ":" + minuteString + ":" + secondString;         
    }
    
    private void clearSplitPanel() {
        splitPanel.removeAll();
        splitPanel.revalidate();
        splitPanel.repaint();
        
        splitCount = 0;
    }
    
}
