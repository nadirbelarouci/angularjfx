package app;

import com.angularjfx.core.Component;
import com.angularjfx.core.NgModel;
import javafx.fxml.FXML;


@Component(templateUrl = "app.component.fxml")
public class AppComponent {

    @NgModel(bindFrom = "title", bindTo = {"title", "text"})
    private String title = "Tour of heroes";


    @FXML
    public void changeValue() {
        title = "Something else";
        System.out.println(title);
    }


}
