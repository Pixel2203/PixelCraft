import com.example.examplemod.API.ingredient.IngredientAPI;
import net.minecraft.gametest.framework.GameTest;

public class BasicGameTest {
    @GameTest(setupTicks = 20L, required = true)
    public static void exampleTest(){
        assert true;
    }
}
