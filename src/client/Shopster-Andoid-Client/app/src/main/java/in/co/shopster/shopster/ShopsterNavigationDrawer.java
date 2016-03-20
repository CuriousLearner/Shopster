package in.co.shopster.shopster;

import android.os.Bundle;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;

import in.co.shopster.shopster.fragments.HomeFragment;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;

/**
 * Created by vikram on 14/3/16.
 */
public class ShopsterNavigationDrawer extends MaterialNavigationDrawer {

    public static MaterialNavigationDrawer currentDrawer;

    @Override
    public void init(Bundle savedInstanceState) {
        MaterialSection homeSection  = newSection(
                "Account",
                new IconDrawable(this, Iconify.IconValue.fa_money),
                new HomeFragment()
        );

        this.addSection(homeSection);

        this.setDefaultSectionLoaded(0);
        disableLearningPattern();

        currentDrawer = this;
    }
}
