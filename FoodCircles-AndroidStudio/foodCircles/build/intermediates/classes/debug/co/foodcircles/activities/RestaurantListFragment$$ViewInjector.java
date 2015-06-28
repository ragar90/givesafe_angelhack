// Generated code from Butter Knife. Do not modify!
package co.foodcircles.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class RestaurantListFragment$$ViewInjector<T extends co.foodcircles.activities.RestaurantListFragment> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296479, "field 'gridView'");
    target.gridView = finder.castView(view, 2131296479, "field 'gridView'");
    view = finder.findRequiredView(source, 2131296476, "field 'mPbWeeklyGoal'");
    target.mPbWeeklyGoal = finder.castView(view, 2131296476, "field 'mPbWeeklyGoal'");
    view = finder.findRequiredView(source, 2131296474, "field 'mTvKidsAidedAmount'");
    target.mTvKidsAidedAmount = finder.castView(view, 2131296474, "field 'mTvKidsAidedAmount'");
    view = finder.findRequiredView(source, 2131296478, "field 'mTvMealsWeeklyGoal'");
    target.mTvMealsWeeklyGoal = finder.castView(view, 2131296478, "field 'mTvMealsWeeklyGoal'");
  }

  @Override public void reset(T target) {
    target.gridView = null;
    target.mPbWeeklyGoal = null;
    target.mTvKidsAidedAmount = null;
    target.mTvMealsWeeklyGoal = null;
  }
}
