package com.angularjfx.core;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import java.io.File;
import java.io.FileWriter;
import java.net.URISyntaxException;
import java.util.Properties;

public class StageConfigurationTest extends ApplicationTest {
    private final String PROPERTIES_FILE_NAME = "application.properties";
    private final String PREFIX = "application";
    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        this.stage.setTitle("Title");
        this.stage.setWidth(500);
        this.stage.setHeight(200);
        stage.setScene(new Scene(new GridPane()));
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    @Test
    public void applicationPropertiesDoesNotExist() throws Exception {
        File file = createPropertiesFile(PROPERTIES_FILE_NAME);
        if (file.exists()) {
            file.delete();
        }

        StageConfiguration stageConfiguration = new StageConfiguration(stage);
        stageConfiguration.applyProperties();

        Assert.assertEquals("Title", this.stage.getTitle());
        Assert.assertEquals(500, stage.getWidth(), 0);
        Assert.assertEquals(200, stage.getHeight(), 0);
    }

    @Test
    public void setWidthAndTitleFromApplicationProperties() throws Exception {
        Properties properties = new Properties();
        properties.setProperty(PREFIX + ".title", "This is a test title");
        properties.setProperty(PREFIX + ".width", "300");
        properties.setProperty(PREFIX + ".height", "200");
        properties.setProperty(PREFIX + ".maximized", "false");

        properties.store(new FileWriter(createPropertiesFile(PROPERTIES_FILE_NAME)), "");

        StageConfiguration stageConfiguration = new StageConfiguration(stage);
        stageConfiguration.applyProperties();

        Assert.assertEquals(300, stage.getWidth(), 0);
        Assert.assertEquals(200, stage.getHeight(), 0);
        Assert.assertEquals("This is a test title", stage.getTitle());
        Assert.assertEquals(false, stage.isMaximized());
    }

    @Test(expected = RuntimeException.class)
    public void putWrongTypesInApplicationProperties() throws Exception {
        Properties properties = new Properties();
        properties.setProperty(PREFIX + ".width", "This is a test title");
        properties.store(new FileWriter(createPropertiesFile(PROPERTIES_FILE_NAME)), "");

        StageConfiguration stageConfiguration = new StageConfiguration(stage);

        stageConfiguration.applyProperties();
    }

    private File createPropertiesFile(String relativeFilePath) throws URISyntaxException {
        return new File(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()), relativeFilePath);
    }

}