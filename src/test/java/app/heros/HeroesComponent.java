package app.heros;

import com.angularjfx.core.Component;
import com.angularjfx.core.NgModel;
import com.angularjfx.core.OnInit;
import app.Hero;

@Component(templateUrl = "heros/heroes.component.fxml")
public class HeroesComponent extends OnInit {
    @NgModel(bindFrom = "hero.id", bindTo = "id")
    @NgModel(bindFrom = "hero.name", bindTo = {"name", "details"})
    private Hero hero;

    private String title;

    public void initialize() {
        hero = new Hero(5, "Nadir");
    }
}
