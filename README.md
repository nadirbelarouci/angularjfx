# AngularJFX
AngularJFX aims to bring some of the Angular features like Data Binding and Components to the JavaFX world.
Binding in JavaFX is tricky and sometimes hards to implement, as well as managing the views and there where AngularJFX can help. 

AngularJFX uses java-agent and bytecode enhancement to modify primitive attributes to the relative datatype in the javafx.beans.property package.
- example: 

| Original Data Type        | Transformed Data Type    |     
| ------------- |-------------|
| String      | SimpleStringProperty | 
| int     | SimpleIntegerProperty      |  
| Object | SimpleObjectProperty      | 


AngularJFX also load components and store them for you, so you don't need to handle loading fxml files and combining them.

the following example demonstrates a snippest from the hero application in the official angular documentation (available in the test package).

### AppModule (MainApplication) 

```java
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
```

### AppComponent

```java
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
```
HeroesComponent
```java
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
```

