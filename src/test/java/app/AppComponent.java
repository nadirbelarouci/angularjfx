package app;

import app.heros.HeroesComponent;
import com.angularjfx.core.annotations.Component;
import com.angularjfx.core.annotations.NgModel;
import javafx.fxml.FXML;


@Component(templateUrl = "app.component.fxml")
public class AppComponent {

    @NgModel(bindFrom = "title", bindTo = {"title", "text"})
    private String title = "Tour of heroes";


    @FXML
    private HeroesComponent herosController;

    @FXML
    public void changeValue() {
        title = "Something else";

    }


}
