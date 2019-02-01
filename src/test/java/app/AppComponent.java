package app;

import com.angularjfx.core.Component;
import com.angularjfx.core.NgModel;
import javafx.fxml.FXML;

import java.util.UUID;


@Component(templateUrl = "app.component.fxml")
public class AppComponent {

    @NgModel(bindFrom = "title", bindTo ={ "title","text"})
    private String title = "Tour of heroes";

    @FXML
    public void changeValue(){
        title = UUID.randomUUID().toString();
    }

}
