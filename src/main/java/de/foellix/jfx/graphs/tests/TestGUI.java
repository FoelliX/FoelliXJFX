package de.foellix.jfx.graphs.tests;

import java.io.File;

import de.foellix.jfx.graphs.Graph;
import de.foellix.jfx.graphs.GraphDrawer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class TestGUI extends Application {
	@Override
	public void start(Stage stage) throws Exception {
		// Graph
		final Graph g = new TestGraph();
		// final Graph g = new StringGraph("'A' { 'B' { 'D1', 'E1' }, 'C' { 'D2', 'E2' } }");
		// final Graph g = new FuzzedTestGraph(42);

		// Pane to draw graph on
		final Pane p = new Pane();

		// Scene
		final Scene scene = new Scene(new ScrollPane(p), 800, 600);

		// Load Style-sheet
		scene.getStylesheets().add(new File("style.css").toURI().toString());

		// Graph drawing
		new GraphDrawer(g, p);
		// new GraphDrawer(g, p, new Options().setSameObjectEdgesEnabled(true));
		// new GraphDrawer(g, p, new FoelliXSkin());
		// new GraphDrawer(g, p, new GraySkin());

		// Show GUI
		stage.setTitle("FoelliXJavaFX - Test");
		stage.getIcons()
				.add(new Image(this.getClass().getResource("/images/fx_icon_16.png").toString(), 16, 16, false, true));
		stage.getIcons()
				.add(new Image(this.getClass().getResource("/images/fx_icon_32.png").toString(), 32, 32, false, true));
		stage.getIcons()
				.add(new Image(this.getClass().getResource("/images/fx_icon_64.png").toString(), 64, 64, false, true));
		stage.setScene(scene);

		stage.show();

		System.out.println("The graph as a string: " + g.toString());
	}
}