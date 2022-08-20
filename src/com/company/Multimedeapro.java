package com.company;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import java.io.File;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import java.awt.image.RenderedImage;
import javafx.stage.Stage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.lang.System.out;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import static javafx.scene.paint.Color.color;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.*;
import java.util.ArrayList;
import javafx.event.EventType;
import javafx.stage.StageStyle;
import static javax.swing.Spring.height;
import static javax.swing.Spring.width;
class Position{
    public int x;
    public int y;
    public Color c;
    public Position (int x , int y , Color c){
        this.x=x;
        this.y=y;
        this.c=c;
    }
}
class Check{
    public Color c ;
    public boolean check=false;
}
class GFG {
    public static int height;
    public static int width;
     public GFG (int height , int width){
         this.height=height;
         this.width=width;
     }
   public static void floodFillUtil(Color screen[][], int x, int y,Color prevC, Color newC){
    // Base cases
    if (y >= width-1 || x <= 0 || x >= height-1 || y <= 0)
        return;
    if (!screen[x][y].equals(prevC) && x<200 &&y<200)
        return;
 
    // Replace the color at (x, y)
    screen[x][y] = newC;
 
    // Recur for north, east, south and west
    floodFillUtil(screen, x+1, y, prevC, newC);
    floodFillUtil(screen, x-1, y, prevC, newC);
    floodFillUtil(screen, x, y+1, prevC, newC);
    floodFillUtil(screen, x, y-1, prevC, newC);
}
    public static void floodFill(Color screen[][], int x, int y, Color newC)
{
    Color prevC = screen[x][y];
      if(prevC==newC) return;
    floodFillUtil(screen, x, y, prevC, newC);
}
}
public class Multimedeapro extends Application {
    private static double SCENE_WIDTH = 500;
    private static double SCENE_HEIGHT = 600;
    public static Color [][] img_pixel;// image pixels
    Check[][] canvas_pixel; //canvas pixels
    Color originalColor = Color.BLUE;
    Color canvasColor = Color.BLUE;
    ArrayList<Position>position= new ArrayList<Position>();
    Canvas canvas;
    GraphicsContext graphicsContext;
    AnimationTimer loop;
    Point2D mouseLocation = new Point2D(0, 0);
    boolean mousePressed = false;
    Point2D prevMouseLocation = new Point2D(0, 0);
    Scene scene;
    double brushMaxSize = 10;
    double pressure = 0;
    double pressureDelay = 0.04;
    double pressureDirection = 1;
    double strokeTimeMax = 1;
    double strokeTime = 0;
    double strokeTimeDelay = 0.07;
    private Image[] brushVariations = new Image[256];
    private BufferedImage  bufferedImage;
    private WritableImage  image;
    private WritableImage  canvas_img;
    ColorPicker colorPicker = new ColorPicker();
    ImageView myImageView;
    @Override
    public void start(Stage primaryStage) {
        StackPane layerPane = new StackPane();
        Button btnLoad = new Button("Load Image");
        btnLoad.setOnAction(btnLoadEventListener);
        Button btnColor = new Button("color image");
        Button btnSave = new Button("save image");
        btnSave.setOnAction(btnSaveEventListener);
        EventHandler<ActionEvent> buttonHandler;
        buttonHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) { 

                img_pixel = new Color [(int)image.getHeight()][(int)image.getWidth()];
                for (int i=0 ; i<image.getHeight();i++){
                    for (int j=0 ; j<image.getWidth();j++){
                        img_pixel[i][j] = image.getPixelReader().getColor(j, i);
                    }
                }
                    for (int i=0;i<position.size();i++){
                    GFG fgf = new GFG((int)image.getHeight(),(int)image.getWidth());   
                    fgf.floodFill(img_pixel,position.get(i).x,position.get(i).y,position.get(i).c);
                    }
                    PixelWriter writer = image.getPixelWriter();
                    for (int i=0 ; i<image.getHeight();i++){
                    for (int j=0 ; j<image.getWidth();j++){
                       writer.setColor(j, i, img_pixel[i][j]);  
                    }
                }
                    bufferedImage = SwingFXUtils.fromFXImage((Image)image, null);
                    image = SwingFXUtils.toFXImage(bufferedImage, null);
                    myImageView.setImage(image);

                   
            }
        };
        btnColor.setOnAction(buttonHandler);
        VBox root = new VBox();
        myImageView = new ImageView();
        myImageView.setFitHeight(500);
        myImageView.setFitWidth(600);
        canvas = new Canvas(myImageView.getFitWidth(), myImageView.getFitHeight());
        canvas.setLayoutX(myImageView.getX());
        canvas.setLayoutY(myImageView.getY());
        canvas.setHeight(500);
        canvas.setWidth(600);
        graphicsContext = canvas.getGraphicsContext2D();
        layerPane.getChildren().add(myImageView);
        layerPane.getChildren().add(canvas);
        colorPicker.setValue(Color.BLUE);
        colorPicker.setOnAction(e -> {
            createBrushVariations();
        });
        root.getChildren().add(layerPane);
        root.getChildren().add(colorPicker);
        root.getChildren().add(btnLoad);
        root.getChildren().add(btnColor);
        root.getChildren().add(btnSave);
        root.setAlignment(Pos.TOP_CENTER);
        scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT, Color.WHITE);
        primaryStage.setScene(scene);
        primaryStage.show();
        createBrushVariations();
        addListeners();
        startAnimation();
    }
    private void createBrushVariations() {
        ///////////////////////////////////////////
            ///////////////////////////////////////////
        for (int i = 0; i < brushVariations.length; i++) {

            double size = (brushMaxSize - 1) / (double) brushVariations.length * (double) i + 1;

            brushVariations[i] = createBrush(size, colorPicker.getValue());
        }

    }

    private void startAnimation() {

        loop = new AnimationTimer() {

            @Override
            public void handle(long now) {
             
                if (mousePressed) {
                     canvas_img = new WritableImage((int)canvas.getWidth(),(int)canvas.getHeight());
                SnapshotParameters sp = new SnapshotParameters();
                sp.getFill();
                canvas.snapshot(sp, canvas_img);
                for (int i=0;i<canvas_img.getHeight();i++){
                   for (int j=0;j<canvas_img.getWidth();j++){
                       if (canvas_pixel[i][j]!=null)
                       canvas_pixel[i][j].c= canvas_img.getPixelReader().getColor(j, i);
                   }
               }
                int x=0 ,y=0;
                for (int i=0 ; i<canvas_img.getHeight();i++){
                    for(int j=0;j<canvas_img.getWidth();j++){
                       if (canvas_pixel[i][j]!=null){ 
                        if (!canvas_pixel[i][j].c.equals(canvasColor)&&canvas_pixel[i][j].check==false){
                            Position p = new Position(i,j,colorPicker.getValue());
                            position.add(p);
                            break;
                        }
                       }
                    }
                }
                 for (int i=0 ; i<canvas_img.getHeight();i++){
                     for(int j=0;j<canvas_img.getWidth();j++){
                        if (canvas_pixel[i][j]!=null){
                         if (!canvas_pixel[i][j].c.equals(canvasColor)){
                          canvas_pixel[i][j].check=true;
                        }
                        }
                    }
                }
    
                    bresenhamLine(prevMouseLocation.getX(), prevMouseLocation.getY(), mouseLocation.getX(), mouseLocation.getY());
                 
                    
                    strokeTime += strokeTimeDelay * pressureDirection;

                    if (strokeTime > strokeTimeMax) {
                        pressureDirection = -1;
                    }
                    if (strokeTime > 0) {
                        pressure += pressureDelay * pressureDirection;
                        if (pressure > 1) {
                            pressure = 1;
                        } else if (pressure < 0) {
                            pressure = 0;
                        }

                    } else {

                        pressure = 0;

                    }

                } else {

                    pressure = 0;
                    pressureDirection = 1;
                    strokeTime = 0;

                }

                prevMouseLocation = new Point2D(mouseLocation.getX(), mouseLocation.getY());

            }
        };

        loop.start();

    }

    private void bresenhamLine(double x0, double y0, double x1, double y1) {
        double dx = Math.abs(x1 - x0), sx = x0 < x1 ? 1. : -1.;
        double dy = -Math.abs(y1 - y0), sy = y0 < y1 ? 1. : -1.;
        double err = dx + dy, e2; /* error value e_xy */

        while (true) {

            int variation = (int) (pressure * (brushVariations.length - 1));
            Image brushVariation = brushVariations[variation];

            graphicsContext.setGlobalAlpha(pressure);
            graphicsContext.drawImage(brushVariation, x0 - brushVariation.getWidth() / 2.0, y0 - brushVariation.getHeight() / 2.0);
              
            if (x0 == x1 && y0 == y1)
                break;
            e2 = 2. * err;
            if (e2 > dy) {
                err += dy;
                x0 += sx;
            } /* e_xy+e_x > 0 */
            if (e2 < dx) {
                err += dx;
                y0 += sy;
            } /* e_xy+e_y < 0 */
        }
    }

    private void addListeners() {
// canvas_img = new WritableImage((int)canvas.getWidth(),(int)canvas.getHeight());
//                SnapshotParameters sp = new SnapshotParameters();
//                sp.getFill();
//                canvas.snapshot(sp, canvas_img);
//                for (int i=0;i<canvas_img.getHeight();i++){
//                   for (int j=0;j<canvas_img.getWidth();j++){
//                       if (canvas_pixel[i][j]!=null)
//                       canvas_pixel[i][j].c= canvas_img.getPixelReader().getColor(j, i);
//                   }
//               }
//                int x=0 ,y=0;
//                for (int i=0 ; i<canvas_img.getHeight();i++){
//                    for(int j=0;j<canvas_img.getWidth();j++){
//                       if (canvas_pixel[i][j]!=null){ 
//                        if (!canvas_pixel[i][j].c.equals(canvasColor)&&canvas_pixel[i][j].check==false){
//                            Position p = new Position(i,j,colorPicker.getValue());
//                            position.add(p);
//                            break;
//                        }
//                       }
//                    }
//                }
//                 for (int i=0 ; i<canvas_img.getHeight();i++){
//                     for(int j=0;j<canvas_img.getWidth();j++){
//                        if (canvas_pixel[i][j]!=null){
//                         if (!canvas_pixel[i][j].c.equals(canvasColor)){
//                          canvas_pixel[i][j].check=true;
//                        }
//                        }
//                    }
//                }
    
        canvas.addEventFilter(MouseEvent.ANY, e -> {
            mouseLocation = new Point2D(e.getX(), e.getY());
            mousePressed = e.isPrimaryButtonDown();

        });
        
    }

    public static Image createImage(Node node) {

        WritableImage wi;

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);

        int imageWidth = (int) node.getBoundsInLocal().getWidth();
        int imageHeight = (int) node.getBoundsInLocal().getHeight();

        wi = new WritableImage(imageWidth, imageHeight);
        node.snapshot(parameters, wi);

        return wi;

    }

    public static Image createBrush(double radius, Color color) {
        Circle brush = new Circle(radius);

        RadialGradient gradient1 = new RadialGradient(0, 0, 0, 0, radius, false, CycleMethod.NO_CYCLE, new Stop(0, color.deriveColor(1, 1, 1, 0.3)), new Stop(1, color.deriveColor(1, 1, 1, 0)));

        brush.setFill(gradient1);
        return createImage(brush);
    }

    public static void main(String[] args) {
        launch(args);
      
    }
    EventHandler<ActionEvent> btnLoadEventListener
            = new EventHandler<ActionEvent>(){

        @Override
        public void handle(ActionEvent t) {
             canvas_img = new WritableImage((int)canvas.getWidth(),(int)canvas.getHeight());
               SnapshotParameters sp = new SnapshotParameters();
               sp.getFill();
               canvas.snapshot(sp, canvas_img);
               canvas_pixel = new Check [(int)canvas_img.getHeight()][(int)canvas_img.getWidth()];
               canvasColor=canvas_img.getPixelReader().getColor(0,0);
               for (int i=0;i<canvas_img.getHeight();i++){
                   for (int j=0;j<canvas_img.getWidth();j++){
                       canvas_pixel[i][j]= new Check();
                       canvas_pixel[i][j].check=false;
                   }
               }
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(null);

            try {
                 BufferedImage img = new BufferedImage(600,500,BufferedImage.TYPE_INT_RGB);
                 bufferedImage = ImageIO.read(file);
                 Graphics2D g = img.createGraphics();
                 g.drawImage(bufferedImage,0,0,600,500,0,0,bufferedImage.getWidth(),bufferedImage.getHeight(),null);
                 g.dispose();
                 image = SwingFXUtils.toFXImage(img, null);
                 myImageView.setImage(image);

            } catch (IOException ex) {
                Logger.getLogger(Multimedeapro.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    };
    EventHandler<ActionEvent>btnSaveEventListener = new EventHandler<ActionEvent>(){

        @Override
        public void handle(ActionEvent t) {
            String filename;
            String s = "ABCDEFGHIJKLMNOPQRS"+"abcdefghijklmnopqrs"+"0123456789";
            StringBuilder sb = new StringBuilder(10);
            for(int i=0 ; i< 10 ;i++){
            int index=(int)(s.length()*Math.random());
            sb.append(s.charAt(index));
        }
            try {
            File file = new File("C:\\Users\\Lenovo\\Desktop\\"+sb.toString()+".PNG");
            RenderedImage renderedImage = SwingFXUtils.fromFXImage(image, null);
            
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException ex) {
                Logger.getLogger(Multimedeapro.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        };

}
