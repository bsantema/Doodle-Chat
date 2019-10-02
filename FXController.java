package application;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

public class FXController {
	private Color currentColor = Color.BLACK;
	private Color previousColor = Color.BLACK;
	private boolean eraseMode = false;

	private boolean mouseDown = false;
	private double xPrev;
	private double yPrev;
	private boolean firstCall = true;
	
	@FXML
	private ChoiceBox<Integer> thicknessPicker;
	
	@FXML
	private Canvas canvas;
	
	@FXML
	private ColorPicker colorPicker;
	
	@FXML
	private Label toolLabel;
	
	@FXML
	private void load()
	{
		GraphicsContext gc = canvas.getGraphicsContext2D();
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
		fileChooser.getExtensionFilters().add(extFilter);
		FileChooser.ExtensionFilter extFilter2 = new FileChooser.ExtensionFilter("jpeg files (*.jpg)", "*.jpg");
		fileChooser.getExtensionFilters().add(extFilter2);
		File file = fileChooser.showOpenDialog(Main.getPrimaryStage());
		Image image = new Image(file.toURI().toString());
		
				if(file != null)
				{
					
						gc.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight());
				}
	}
	
	@FXML
	private void save()
	{
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showSaveDialog(Main.getPrimaryStage());
		if(file != null)
		{
			try
			{
				WritableImage writableImage = new WritableImage(344, 284);
				canvas.snapshot(null, writableImage);
				RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
				ImageIO.write(renderedImage, "png", file);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	

	
	@FXML
	private void erase()
	{
		GraphicsContext gc = canvas.getGraphicsContext2D();
		if(!eraseMode)
		{
			previousColor = currentColor; 
			currentColor = Color.WHITE;
			eraseMode = !eraseMode;
			toolLabel.setText("Eraser is on");
		}
		else
		{
			currentColor = previousColor; 
			eraseMode = !eraseMode;
			toolLabel.setText("");
		}
		
		
	}
	
	public void initializeEditor()
	{
		colorPicker.setValue(Color.BLACK);
		thicknessPicker.getItems().addAll(2,5,10,20,50);
		thicknessPicker.setValue(5);
	}
	
	@FXML
	private void pickColor()
	{
		GraphicsContext gc = canvas.getGraphicsContext2D();
		if(!eraseMode)
		{
			previousColor = currentColor;
			currentColor = colorPicker.getValue();
		}
		else
		{
			previousColor = colorPicker.getValue();
		}
		
		
	}
	@FXML
	private void reset() {
	    GraphicsContext gc = canvas.getGraphicsContext2D();
	    gc.setFill(Color.WHITE);
	    gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}
	 
	@FXML
	public void start() {
		
	    final GraphicsContext gc = canvas.getGraphicsContext2D();
	    
	    // Clear away portions as the user drags the mouse
	       canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, 
	       new EventHandler<MouseEvent>() {
	           @Override
	           public void handle(MouseEvent e) {
	        	   gc.setFill(currentColor);
	        	   
	        	   
	        	   gc.fillRect(e.getX(), e.getY(), thicknessPicker.getValue(), thicknessPicker.getValue());
	        	   
	        	   
	        	   if(!firstCall && mouseDown)
	        		   //smooth(e.getX(), e.getY(), xPrev, yPrev, gc);
	        		   
	        	   firstCall = false;
	        	   xPrev = e.getX();
	        	   yPrev = e.getY();
	           }
	       });
	       
	       mouseDown=true;
	}
	
	public void smooth(double x0, double y0, double x1, double y1, GraphicsContext gc)
	{
		
		
		//height 284
		if(y0<0)
			y0=0;
		
		if(y1<0)
			y1=0;
		
		y0 = 284 - y0;
		y1 = 284 - y1;
		if(x0>x1 || (x0>x1 && y0>y1))
		{
			double tempx = x0;
			double tempy = y0;
			x0 = x1;
			y0 = y1;
			x1 = tempx;
			y1 = tempy;
		}
		double y = y0;
		double x = x0;
		double slope = (y1-y0)/(x1-x0);
		if(!(Math.abs(slope)>1.0))
			for(; x0<=x1; x0+=1.0)
			{
				gc.fillRect(x0, y-284, thicknessPicker.getValue(), thicknessPicker.getValue());
				y= y + slope;
			}
		else
		{
				slope = (x1-x0)/(y1-y0);
				for(; y0<=y1; y0+=1.0)
				{
					gc.fillRect(x, y0-284, thicknessPicker.getValue(), thicknessPicker.getValue());
					x= x + slope;
				}
		}
			
		
		
	}
	
	@FXML
	public void end()
	{
		mouseDown=false;
	}
}
