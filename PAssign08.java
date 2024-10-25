package taylorgotchi2;

/**
* File: PAssign08.java
* Class: CSCI 1302
* Author: Taylor Oneal
* Created on: Mar 21, 2024
* Description: Taylorgotchi.
*/

import java.io.File;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import taylorgotchi.KeyPadPane;

/**
 *  I had so much fun with this project, though, my code def needs to be reorganized and restructured.
 *  However, I feel like I'm missing the full picture as to what would be the most efficient way to do so.
 *  Will revisit later
 *  
 *  For the animations, I now know it'd be more efficient to offset their position instead of having so many animation images.
 *  
 *  
 *  Could use more error checking to prevent audio + animation conflicts.
 */

public class PAssign08 extends Application {
	private double framerate = 100.0; // milliseconds
	private Timeline idleAnimationTimeline;
	private Timeline danceTimeline;
	private Timeline singIntroTimeline;
	private boolean walkInDone = false;
	private Text errorText = new Text("");
	
	@Override
	public void start(@SuppressWarnings("exports") Stage primaryStage) {
		// panes
		BorderPane screenPane = new BorderPane();
		ImageView imgView = new ImageView();
		StackPane imgPane = new StackPane(imgView);
		HBox remote = new HBox(new Remote());

		// styling
		imgPane.setStyle("-fx-border-color: black; -fx-border-width: 1;");
		imgPane.setPrefSize(320, 320);
		imgPane.getChildren().add(errorText);
		
		errorText.setStyle("-fx-fill: red;");
		
		screenPane.setCenter(imgPane);
		screenPane.setBottom(remote);
		screenPane.setPadding(new Insets(10));
		
		remote.setPadding(new Insets(10, 0, 0, 0));
		remote.setAlignment(Pos.CENTER);
		
		// pane into scene and scene into stage
		Scene scene = new Scene(screenPane);
		primaryStage.setTitle("Taylorgotchi!");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		
		// ANIMATIONS -------------------------------------------------------------------------------------------
		
		// WALKIN ANIMATION | into array
		Image[] walkInImageArray = new Image[118];
		for (int i = 1; i <= walkInImageArray.length; i++) {
		    System.out.printf("Loading walkIn %d...%n", i);
		    File imageFile = new File(String.format("src/taylorgotchi2/AnimationImgs/walkIn/walkIn %d.png", i));
		    walkInImageArray[i - 1] = new Image(imageFile.toURI().toString());
		}
		
		// iterate through walkInImageArray to animate
		Timeline walkInTimeline = new Timeline();
		for (int i = 0; i < walkInImageArray.length; i++) {
		    int frameIndex = i; // lambda
		    KeyFrame keyFrame = new KeyFrame(Duration.millis((framerate / 2) * i), e -> imgView.setImage(walkInImageArray[frameIndex]));
		    walkInTimeline.getKeyFrames().add(keyFrame);
		}
		walkInTimeline.play();
		
		// IDLE ANIMATION
		File defaultStandFile = new File("src/taylorgotchi2/AnimationImgs/default.png");
		Image defaultStandImg = new Image(defaultStandFile.toURI().toString());
		File idleDownFile = new File("src/taylorgotchi2/AnimationImgs/idleDown.png");
		Image idleDownImg = new Image(idleDownFile.toURI().toString());
		idleAnimationTimeline = new Timeline(
		        new KeyFrame(Duration.millis(framerate * 3), e -> imgView.setImage(defaultStandImg)),
		        new KeyFrame(Duration.millis(framerate * 6), e -> imgView.setImage(idleDownImg))
		);
		idleAnimationTimeline.setCycleCount(Timeline.INDEFINITE);
		walkInTimeline.setOnFinished(e -> {
			idleAnimationTimeline.play();
			walkInDone = true;
			errorText.setText("");
			});
		
		// DANCE ANIMATION | into array
		Image[] danceImageArray = new Image[18];
		for (int i = 1; i <= danceImageArray.length; i++) {
			System.out.printf("Loading dance %d... %n", i);
		    File imageFile = new File(String.format("src/taylorgotchi2/AnimationImgs/dance/dance %d.png", i));
		    danceImageArray[i - 1] = new Image(imageFile.toURI().toString());
		}
		
		// iterate through danceImageArray to animate
		danceTimeline = new Timeline();
		for (int i = 0; i < danceImageArray.length; i++) {
			int frameIndex = i; // lambda
		    KeyFrame keyFrame = new KeyFrame(Duration.millis(framerate * 3 * i), e -> imgView.setImage(danceImageArray[frameIndex]));
		    danceTimeline.getKeyFrames().add(keyFrame);
		}
		danceTimeline.setOnFinished(e -> {
			idleAnimationTimeline.play();
		});
		
		// SING ANIMATION
		File caroFile = new File("src/taylorgotchi2/Audio/caroMioBen.mp3");
		Media caroMedia = new Media(caroFile.toURI().toString());
		MediaPlayer caroPlayer = new MediaPlayer(caroMedia);
		Image sing1 = new Image(new File("src/taylorgotchi2/AnimationImgs/sing/sing 1.png").toURI().toString());
		Image sing2 = new Image(new File("src/taylorgotchi2/AnimationImgs/sing/sing 2.png").toURI().toString());
		Image sing3 = new Image(new File("src/taylorgotchi2/AnimationImgs/sing/sing 3.png").toURI().toString());
		Image sing4 = new Image(new File("src/taylorgotchi2/AnimationImgs/sing/sing 4.png").toURI().toString());
		
		int randomDelay = 1;
		System.out.println(randomDelay);
		Timeline singSingTimeline = new Timeline(
				new KeyFrame(Duration.millis(500), e -> imgView.setImage(sing3)),
				new KeyFrame(Duration.millis(3000), e -> imgView.setImage(sing4))
				);
		singSingTimeline.setCycleCount(Timeline.INDEFINITE);
		
		singIntroTimeline = new Timeline(
				new KeyFrame(Duration.millis(0), e -> imgView.setImage(sing1)),
				new KeyFrame(Duration.millis(500 * (int)(Math.random() * 3) + 1), e -> imgView.setImage(sing2))
				);
		singIntroTimeline.setOnFinished(e -> {
			caroPlayer.play();
			singSingTimeline.play();
		});
		caroPlayer.setOnEndOfMedia(() -> { // what's a runnable
			singSingTimeline.stop();
			idleAnimationTimeline.play();
		});
		caroPlayer.setVolume(0.1); // are some audio files "louder" than others?
	}

	class Remote extends KeyPadPane {
		Remote() {
			// rename buttons
			btn1.setText("Dance");
			btn2.setText("Sing");
			btn3.setText("Cake");

			// delete unused buttons
			getChildren().removeAll(btn4, btn5, btn6, btn7, btn8, btn9, btnBlank1, btn0, btnBlank2, btnAsterisk, btnPound); 

			// style
			setHgap(10);
		}
		@Override // new button events
		protected void registerEventHandlers() {
			btn1.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					if (walkInDone) {
						System.out.println("Dancing...");
						idleAnimationTimeline.stop();
						danceTimeline.play();
					} else {
						errorText.setText("WAIT");
					}
				}
			});
			btn2.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					
					if (walkInDone) {
						System.out.println("Singing...");
						idleAnimationTimeline.stop();
						singIntroTimeline.play();
						
						File spotAudioFile = new File("src/taylorgotchi2/Audio/spotlight.mp3");
						Media spotMedia = new Media(spotAudioFile.toURI().toString());
						MediaPlayer spotPlayer = new MediaPlayer(spotMedia);
						
						
						spotPlayer.play();
					} else {
						errorText.setText("WAIT");
					}
				}
			});
			btn3.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					if (walkInDone) {
						System.out.println("7h3 c4k3 15 4 l13");
						getHostServices().showDocument("https://www.youtube.com/watch?v=dQw4w9WgXcQ");
					} else {
						errorText.setText("WAIT");
					}
				}
			});
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
}