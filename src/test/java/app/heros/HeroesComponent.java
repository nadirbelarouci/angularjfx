package app.heros;

import com.angularjfx.core.annotations.Component;
import com.angularjfx.core.annotations.NgModel;
import com.angularjfx.core.OnInit;
import app.Hero;

@Component(templateUrl = "heros/heroes.component.fxml")
public class HeroesComponent extends OnInit {
    @NgModel(bindFrom = "hero.id", bindTo = "id")
    @NgModel(bindFrom = "hero.name", bindTo = {"name", "details"})
    private Hero hero;

    public void initialize() {

        hero = new Hero(5, "Nadir");
    }
}
