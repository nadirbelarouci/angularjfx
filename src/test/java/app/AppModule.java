package app;

import app.heros.HeroesComponent;
import com.angularjfx.core.AngularApplication;
import com.angularjfx.core.annotations.NgModule;

@NgModule(declarations = {
        HeroesComponent.class
}, bootstrap = AppComponent.class)
public class AppModule {
    public static void main(String... args) {

        AngularApplication.run(AppModule.class, args);
    }

}
