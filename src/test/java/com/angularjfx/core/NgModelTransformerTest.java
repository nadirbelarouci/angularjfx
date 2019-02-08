package com.angularjfx.core;

import app.AppComponent;
import app.heros.HeroesComponent;
import com.ea.agentloader.AgentLoader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NgModelTransformerTest {

    @Before
    public void setUp() throws Exception {
        AgentLoader.loadAgentClass(EnhancerAgent.class.getName(), "");

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void transform() {
        AppComponent appComponent = new AppComponent();
        appComponent.changeValue();
    }
}