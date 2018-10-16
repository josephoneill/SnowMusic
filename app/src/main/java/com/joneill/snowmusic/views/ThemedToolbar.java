package com.joneill.snowmusic.views;

import android.animation.LayoutTransition;
import android.animation.StateListAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.widget.DecorToolbar;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Display;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.ViewOutlineProvider;
import android.view.ViewParent;
import android.view.ViewPropertyAnimator;
import android.view.ViewStructure;
import android.view.ViewTreeObserver;
import android.view.WindowId;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.Transformation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import com.joneill.snowmusic.helper.Utils;

import java.util.ArrayList;

/**
 * Created by josep on 8/27/2016.
 */
public class ThemedToolbar extends Toolbar {
    public ThemedToolbar(Context context) {
        super(context);
        setTitleTextColor(Color.WHITE);
    }

    public ThemedToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setTitleTextColor(Color.WHITE);
    }

    public ThemedToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTitleTextColor(Color.WHITE);
    }

    @Override
    public void setPopupTheme(@StyleRes int resId) {
        super.setPopupTheme(resId);
    }

    @Override
    public int getPopupTheme() {
        return super.getPopupTheme();
    }

    @Override
    public void setTitleMargin(int start, int top, int end, int bottom) {
        super.setTitleMargin(start, top, end, bottom);
    }

    @Override
    public int getTitleMarginStart() {
        return super.getTitleMarginStart();
    }

    @Override
    public void setTitleMarginStart(int margin) {
        super.setTitleMarginStart(margin);
    }

    @Override
    public int getTitleMarginTop() {
        return super.getTitleMarginTop();
    }

    @Override
    public void setTitleMarginTop(int margin) {
        super.setTitleMarginTop(margin);
    }

    @Override
    public int getTitleMarginEnd() {
        return super.getTitleMarginEnd();
    }

    @Override
    public void setTitleMarginEnd(int margin) {
        super.setTitleMarginEnd(margin);
    }

    @Override
    public int getTitleMarginBottom() {
        return super.getTitleMarginBottom();
    }

    @Override
    public void setTitleMarginBottom(int margin) {
        super.setTitleMarginBottom(margin);
    }

    @Override
    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
    }

    @Override
    public void setLogo(@DrawableRes int resId) {
        super.setLogo(resId);
    }

    @Override
    public boolean canShowOverflowMenu() {
        return super.canShowOverflowMenu();
    }

    @Override
    public boolean isOverflowMenuShowing() {
        return super.isOverflowMenuShowing();
    }

    @Override
    public boolean isOverflowMenuShowPending() {
        return super.isOverflowMenuShowPending();
    }

    @Override
    public boolean showOverflowMenu() {
        return super.showOverflowMenu();
    }

    @Override
    public boolean hideOverflowMenu() {
        return super.hideOverflowMenu();
    }


    @Override
    public void dismissPopupMenus() {
        super.dismissPopupMenus();
    }

    @Override
    public boolean isTitleTruncated() {
        return super.isTitleTruncated();
    }

    @Override
    public void setLogo(Drawable drawable) {
        super.setLogo(drawable);
    }

    @Override
    public Drawable getLogo() {
        return super.getLogo();
    }

    @Override
    public void setLogoDescription(@StringRes int resId) {
        super.setLogoDescription(resId);
    }

    @Override
    public void setLogoDescription(CharSequence description) {
        super.setLogoDescription(description);
    }

    @Override
    public CharSequence getLogoDescription() {
        return super.getLogoDescription();
    }

    @Override
    public boolean hasExpandedActionView() {
        return super.hasExpandedActionView();
    }

    @Override
    public void collapseActionView() {
        super.collapseActionView();
    }

    @Override
    public CharSequence getTitle() {
        return super.getTitle();
    }

    @Override
    public void setTitle(@StringRes int resId) {
        super.setTitle(resId);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
    }

    @Override
    public CharSequence getSubtitle() {
        return super.getSubtitle();
    }

    @Override
    public void setSubtitle(@StringRes int resId) {
        super.setSubtitle(resId);
    }

    @Override
    public void setSubtitle(CharSequence subtitle) {
        super.setSubtitle(subtitle);
    }

    @Override
    public void setTitleTextAppearance(Context context, @StyleRes int resId) {
        super.setTitleTextAppearance(context, resId);
    }

    @Override
    public void setSubtitleTextAppearance(Context context, @StyleRes int resId) {
        super.setSubtitleTextAppearance(context, resId);
    }

    @Override
    public void setTitleTextColor(@ColorInt int color) {
        super.setTitleTextColor(color);
    }

    @Override
    public void setSubtitleTextColor(@ColorInt int color) {
        super.setSubtitleTextColor(color);
    }

    @Nullable
    @Override
    public CharSequence getNavigationContentDescription() {
        return super.getNavigationContentDescription();
    }

    @Override
    public void setNavigationContentDescription(@StringRes int resId) {
        super.setNavigationContentDescription(resId);
    }

    @Override
    public void setNavigationContentDescription(@Nullable CharSequence description) {
        super.setNavigationContentDescription(description);
    }

    @Override
    public void setNavigationIcon(@DrawableRes int resId) {
        super.setNavigationIcon(resId);
    }

    @Override
    public void setNavigationIcon(@Nullable Drawable icon) {
        super.setNavigationIcon(icon);
    }

    @Nullable
    @Override
    public Drawable getNavigationIcon() {
        return super.getNavigationIcon();
    }

    @Override
    public void setNavigationOnClickListener(OnClickListener listener) {
        super.setNavigationOnClickListener(listener);
    }

    @Override
    public Menu getMenu() {
        return super.getMenu();
    }

    @Override
    public void setOverflowIcon(@Nullable Drawable icon) {
        super.setOverflowIcon(icon);
    }

    @Nullable
    @Override
    public Drawable getOverflowIcon() {
        return super.getOverflowIcon();
    }

    @Override
    public void inflateMenu(@MenuRes int resId) {
        super.inflateMenu(resId);
    }

    @Override
    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        super.setOnMenuItemClickListener(listener);
    }

    @Override
    public void setContentInsetsRelative(int contentInsetStart, int contentInsetEnd) {
        super.setContentInsetsRelative(contentInsetStart, contentInsetEnd);
    }

    @Override
    public int getContentInsetStart() {
        return super.getContentInsetStart();
    }

    @Override
    public int getContentInsetEnd() {
        return super.getContentInsetEnd();
    }

    @Override
    public void setContentInsetsAbsolute(int contentInsetLeft, int contentInsetRight) {
        super.setContentInsetsAbsolute(contentInsetLeft, contentInsetRight);
    }

    @Override
    public int getContentInsetLeft() {
        return super.getContentInsetLeft();
    }

    @Override
    public int getContentInsetRight() {
        return super.getContentInsetRight();
    }

    @Override
    public int getContentInsetStartWithNavigation() {
        return super.getContentInsetStartWithNavigation();
    }

    @Override
    public void setContentInsetStartWithNavigation(int insetStartWithNavigation) {
        super.setContentInsetStartWithNavigation(insetStartWithNavigation);
    }

    @Override
    public int getContentInsetEndWithActions() {
        return super.getContentInsetEndWithActions();
    }

    @Override
    public void setContentInsetEndWithActions(int insetEndWithActions) {
        super.setContentInsetEndWithActions(insetEndWithActions);
    }

    @Override
    public int getCurrentContentInsetStart() {
        return super.getCurrentContentInsetStart();
    }

    @Override
    public int getCurrentContentInsetEnd() {
        return super.getCurrentContentInsetEnd();
    }

    @Override
    public int getCurrentContentInsetLeft() {
        return super.getCurrentContentInsetLeft();
    }

    @Override
    public int getCurrentContentInsetRight() {
        return super.getCurrentContentInsetRight();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onHoverEvent(MotionEvent ev) {
        return super.onHoverEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return super.generateLayoutParams(attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return super.generateLayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return super.checkLayoutParams(p);
    }

    @Override
    public DecorToolbar getWrapper() {
        return super.getWrapper();
    }

    @Override
    public void setCollapsible(boolean collapsible) {
        super.setCollapsible(collapsible);
    }

    @Override
    public void setMenuCallbacks(MenuPresenter.Callback pcb, MenuBuilder.Callback mcb) {
        super.setMenuCallbacks(pcb, mcb);
    }

    @Override
    public int getDescendantFocusability() {
        return super.getDescendantFocusability();
    }

    @Override
    public void setDescendantFocusability(int focusability) {
        super.setDescendantFocusability(focusability);
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
    }

    @Override
    public void focusableViewAvailable(View v) {
        super.focusableViewAvailable(v);
    }

    @Override
    public boolean showContextMenuForChild(View originalView) {
        return super.showContextMenuForChild(originalView);
    }

    @Override
    public boolean showContextMenuForChild(View originalView, float x, float y) {
        return super.showContextMenuForChild(originalView, x, y);
    }

    @Override
    public ActionMode startActionModeForChild(View originalView, ActionMode.Callback callback) {
        return super.startActionModeForChild(originalView, callback);
    }

    @Override
    public ActionMode startActionModeForChild(View originalView, ActionMode.Callback callback, int type) {
        return super.startActionModeForChild(originalView, callback, type);
    }

    @Override
    public View focusSearch(View focused, int direction) {
        return super.focusSearch(focused, direction);
    }

    @Override
    public boolean requestChildRectangleOnScreen(View child, Rect rectangle, boolean immediate) {
        return super.requestChildRectangleOnScreen(child, rectangle, immediate);
    }

    @Override
    public boolean requestSendAccessibilityEvent(View child, AccessibilityEvent event) {
        return super.requestSendAccessibilityEvent(child, event);
    }

    @Override
    public boolean onRequestSendAccessibilityEvent(View child, AccessibilityEvent event) {
        return super.onRequestSendAccessibilityEvent(child, event);
    }

    @Override
    public void childHasTransientStateChanged(View child, boolean childHasTransientState) {
        super.childHasTransientStateChanged(child, childHasTransientState);
    }

    @Override
    public boolean hasTransientState() {
        return super.hasTransientState();
    }

    @Override
    public boolean dispatchUnhandledMove(View focused, int direction) {
        return super.dispatchUnhandledMove(focused, direction);
    }

    @Override
    public void clearChildFocus(View child) {
        super.clearChildFocus(child);
    }

    @Override
    public void clearFocus() {
        super.clearFocus();
    }

    @Override
    public View getFocusedChild() {
        return super.getFocusedChild();
    }

    @Override
    public boolean hasFocus() {
        return super.hasFocus();
    }

    @Override
    public View findFocus() {
        return super.findFocus();
    }

    @Override
    public boolean hasFocusable() {
        return super.hasFocusable();
    }

    @Override
    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        super.addFocusables(views, direction, focusableMode);
    }

    @Override
    public void setTouchscreenBlocksFocus(boolean touchscreenBlocksFocus) {
        super.setTouchscreenBlocksFocus(touchscreenBlocksFocus);
    }

    @Override
    public boolean getTouchscreenBlocksFocus() {
        return super.getTouchscreenBlocksFocus();
    }

    @Override
    public void findViewsWithText(ArrayList<View> outViews, CharSequence text, int flags) {
        super.findViewsWithText(outViews, text, flags);
    }

    @Override
    public void dispatchWindowFocusChanged(boolean hasFocus) {
        super.dispatchWindowFocusChanged(hasFocus);
    }

    @Override
    public void addTouchables(ArrayList<View> views) {
        super.addTouchables(views);
    }

    @Override
    public void dispatchDisplayHint(int hint) {
        super.dispatchDisplayHint(hint);
    }

    @Override
    protected void dispatchVisibilityChanged(View changedView, int visibility) {
        super.dispatchVisibilityChanged(changedView, visibility);
    }

    @Override
    public void dispatchWindowVisibilityChanged(int visibility) {
        super.dispatchWindowVisibilityChanged(visibility);
    }

    @Override
    public void dispatchConfigurationChanged(Configuration newConfig) {
        super.dispatchConfigurationChanged(newConfig);
    }

    @Override
    public void recomputeViewAttributes(View child) {
        super.recomputeViewAttributes(child);
    }

    @Override
    public void bringChildToFront(View child) {
        super.bringChildToFront(child);
    }

    @Override
    public boolean dispatchDragEvent(DragEvent event) {
        return super.dispatchDragEvent(event);
    }

    @Override
    public void dispatchWindowSystemUiVisiblityChanged(int visible) {
        super.dispatchWindowSystemUiVisiblityChanged(visible);
    }

    @Override
    public void dispatchSystemUiVisibilityChanged(int visible) {
        super.dispatchSystemUiVisibilityChanged(visible);
    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        return super.dispatchKeyEventPreIme(event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        return super.dispatchKeyShortcutEvent(event);
    }

    @Override
    public boolean dispatchTrackballEvent(MotionEvent event) {
        return super.dispatchTrackballEvent(event);
    }

    @Override
    public PointerIcon onResolvePointerIcon(MotionEvent event, int pointerIndex) {
        return super.onResolvePointerIcon(event, pointerIndex);
    }

    @Override
    protected boolean dispatchHoverEvent(MotionEvent event) {
        return super.dispatchHoverEvent(event);
    }

    @Override
    public void addChildrenForAccessibility(ArrayList<View> outChildren) {
        super.addChildrenForAccessibility(outChildren);
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        return super.onInterceptHoverEvent(event);
    }

    @Override
    protected boolean dispatchGenericPointerEvent(MotionEvent event) {
        return super.dispatchGenericPointerEvent(event);
    }

    @Override
    protected boolean dispatchGenericFocusedEvent(MotionEvent event) {
        return super.dispatchGenericFocusedEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void setMotionEventSplittingEnabled(boolean split) {
        super.setMotionEventSplittingEnabled(split);
    }

    @Override
    public boolean isMotionEventSplittingEnabled() {
        return super.isMotionEventSplittingEnabled();
    }

    @Override
    public boolean isTransitionGroup() {
        return super.isTransitionGroup();
    }

    @Override
    public void setTransitionGroup(boolean isTransitionGroup) {
        super.setTransitionGroup(isTransitionGroup);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        return super.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
    }

    @Override
    public void dispatchProvideStructure(ViewStructure structure) {
        super.dispatchProvideStructure(structure);
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return super.getAccessibilityClassName();
    }

    @Override
    public void notifySubtreeAccessibilityStateChanged(View child, View source, int changeType) {
        super.notifySubtreeAccessibilityStateChanged(child, source, changeType);
    }

    @Override
    public boolean onNestedPrePerformAccessibilityAction(View target, int action, Bundle args) {
        return super.onNestedPrePerformAccessibilityAction(target, action, args);
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        super.dispatchSaveInstanceState(container);
    }

    @Override
    protected void dispatchFreezeSelfOnly(SparseArray<Parcelable> container) {
        super.dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        super.dispatchRestoreInstanceState(container);
    }

    @Override
    protected void dispatchThawSelfOnly(SparseArray<Parcelable> container) {
        super.dispatchThawSelfOnly(container);
    }

    @Override
    protected void setChildrenDrawingCacheEnabled(boolean enabled) {
        super.setChildrenDrawingCacheEnabled(enabled);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    @Override
    public ViewGroupOverlay getOverlay() {
        return super.getOverlay();
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        return super.getChildDrawingOrder(childCount, i);
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        return super.drawChild(canvas, child, drawingTime);
    }

    @Override
    public boolean getClipChildren() {
        return super.getClipChildren();
    }

    @Override
    public void setClipChildren(boolean clipChildren) {
        super.setClipChildren(clipChildren);
    }

    @Override
    public void setClipToPadding(boolean clipToPadding) {
        super.setClipToPadding(clipToPadding);
    }

    @Override
    public boolean getClipToPadding() {
        return super.getClipToPadding();
    }

    @Override
    public void dispatchSetSelected(boolean selected) {
        super.dispatchSetSelected(selected);
    }

    @Override
    public void dispatchSetActivated(boolean activated) {
        super.dispatchSetActivated(activated);
    }

    @Override
    protected void dispatchSetPressed(boolean pressed) {
        super.dispatchSetPressed(pressed);
    }

    @Override
    public void dispatchDrawableHotspotChanged(float x, float y) {
        super.dispatchDrawableHotspotChanged(x, y);
    }

    @Override
    protected void setStaticTransformationsEnabled(boolean enabled) {
        super.setStaticTransformationsEnabled(enabled);
    }

    @Override
    protected boolean getChildStaticTransformation(View child, Transformation t) {
        return super.getChildStaticTransformation(child, t);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        super.addView(child, index);
    }

    @Override
    public void addView(View child, int width, int height) {
        super.addView(child, width, height);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
    }

    @Override
    public void updateViewLayout(View view, ViewGroup.LayoutParams params) {
        super.updateViewLayout(view, params);
    }

    @Override
    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        super.setOnHierarchyChangeListener(listener);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
    }

    @Override
    public void onViewRemoved(View child) {
        super.onViewRemoved(child);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params) {
        return super.addViewInLayout(child, index, params);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        return super.addViewInLayout(child, index, params, preventRequestLayout);
    }

    @Override
    protected void cleanupLayoutState(View child) {
        super.cleanupLayoutState(child);
    }

    @Override
    protected void attachLayoutAnimationParameters(View child, ViewGroup.LayoutParams params, int index, int count) {
        super.attachLayoutAnimationParameters(child, params, index, count);
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
    }

    @Override
    public void removeViewInLayout(View view) {
        super.removeViewInLayout(view);
    }

    @Override
    public void removeViewsInLayout(int start, int count) {
        super.removeViewsInLayout(start, count);
    }

    @Override
    public void removeViewAt(int index) {
        super.removeViewAt(index);
    }

    @Override
    public void removeViews(int start, int count) {
        super.removeViews(start, count);
    }

    @Override
    public void setLayoutTransition(LayoutTransition transition) {
        super.setLayoutTransition(transition);
    }

    @Override
    public LayoutTransition getLayoutTransition() {
        return super.getLayoutTransition();
    }

    @Override
    public void removeAllViews() {
        super.removeAllViews();
    }

    @Override
    public void removeAllViewsInLayout() {
        super.removeAllViewsInLayout();
    }

    @Override
    protected void removeDetachedView(View child, boolean animate) {
        super.removeDetachedView(child, animate);
    }

    @Override
    protected void attachViewToParent(View child, int index, ViewGroup.LayoutParams params) {
        super.attachViewToParent(child, index, params);
    }

    @Override
    protected void detachViewFromParent(View child) {
        super.detachViewFromParent(child);
    }

    @Override
    protected void detachViewFromParent(int index) {
        super.detachViewFromParent(index);
    }

    @Override
    protected void detachViewsFromParent(int start, int count) {
        super.detachViewsFromParent(start, count);
    }

    @Override
    protected void detachAllViewsFromParent() {
        super.detachAllViewsFromParent();
    }

    @Override
    public ViewParent invalidateChildInParent(int[] location, Rect dirty) {
        return super.invalidateChildInParent(location, dirty);
    }

    @Override
    public boolean getChildVisibleRect(View child, Rect r, Point offset) {
        return super.getChildVisibleRect(child, r, offset);
    }

    @Override
    protected boolean canAnimate() {
        return super.canAnimate();
    }

    @Override
    public void startLayoutAnimation() {
        super.startLayoutAnimation();
    }

    @Override
    public void scheduleLayoutAnimation() {
        super.scheduleLayoutAnimation();
    }

    @Override
    public void setLayoutAnimation(LayoutAnimationController controller) {
        super.setLayoutAnimation(controller);
    }

    @Override
    public LayoutAnimationController getLayoutAnimation() {
        return super.getLayoutAnimation();
    }

    @Override
    public boolean isAnimationCacheEnabled() {
        return super.isAnimationCacheEnabled();
    }

    @Override
    public void setAnimationCacheEnabled(boolean enabled) {
        super.setAnimationCacheEnabled(enabled);
    }

    @Override
    public boolean isAlwaysDrawnWithCacheEnabled() {
        return super.isAlwaysDrawnWithCacheEnabled();
    }

    @Override
    public void setAlwaysDrawnWithCacheEnabled(boolean always) {
        super.setAlwaysDrawnWithCacheEnabled(always);
    }

    @Override
    protected boolean isChildrenDrawnWithCacheEnabled() {
        return super.isChildrenDrawnWithCacheEnabled();
    }

    @Override
    protected void setChildrenDrawnWithCacheEnabled(boolean enabled) {
        super.setChildrenDrawnWithCacheEnabled(enabled);
    }

    @Override
    protected boolean isChildrenDrawingOrderEnabled() {
        return super.isChildrenDrawingOrderEnabled();
    }

    @Override
    protected void setChildrenDrawingOrderEnabled(boolean enabled) {
        super.setChildrenDrawingOrderEnabled(enabled);
    }

    @Override
    public int getPersistentDrawingCache() {
        return super.getPersistentDrawingCache();
    }

    @Override
    public void setPersistentDrawingCache(int drawingCacheToKeep) {
        super.setPersistentDrawingCache(drawingCacheToKeep);
    }

    @Override
    public int getLayoutMode() {
        return super.getLayoutMode();
    }

    @Override
    public void setLayoutMode(int layoutMode) {
        super.setLayoutMode(layoutMode);
    }

    @Override
    protected void debug(int depth) {
        super.debug(depth);
    }

    @Override
    public int indexOfChild(View child) {
        return super.indexOfChild(child);
    }

    @Override
    public int getChildCount() {
        return super.getChildCount();
    }

    @Override
    public View getChildAt(int index) {
        return super.getChildAt(index);
    }

    @Override
    protected void measureChildren(int widthMeasureSpec, int heightMeasureSpec) {
        super.measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        super.measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec);
    }

    @Override
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        super.measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
    }

    @Override
    public void clearDisappearingChildren() {
        super.clearDisappearingChildren();
    }

    @Override
    public void startViewTransition(View view) {
        super.startViewTransition(view);
    }

    @Override
    public void endViewTransition(View view) {
        super.endViewTransition(view);
    }

    @Override
    public boolean gatherTransparentRegion(Region region) {
        return super.gatherTransparentRegion(region);
    }

    @Override
    public void requestTransparentRegion(View child) {
        super.requestTransparentRegion(child);
    }

    @Override
    public WindowInsets dispatchApplyWindowInsets(WindowInsets insets) {
        return super.dispatchApplyWindowInsets(insets);
    }

    @Override
    public Animation.AnimationListener getLayoutAnimationListener() {
        return super.getLayoutAnimationListener();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
    }

    @Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        return super.onCreateDrawableState(extraSpace);
    }

    @Override
    public void setAddStatesFromChildren(boolean addsStates) {
        super.setAddStatesFromChildren(addsStates);
    }

    @Override
    public boolean addStatesFromChildren() {
        return super.addStatesFromChildren();
    }

    @Override
    public void childDrawableStateChanged(View child) {
        super.childDrawableStateChanged(child);
    }

    @Override
    public void setLayoutAnimationListener(Animation.AnimationListener animationListener) {
        super.setLayoutAnimationListener(animationListener);
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return super.shouldDelayChildPressedState();
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return super.onStartNestedScroll(child, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        super.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onStopNestedScroll(View child) {
        super.onStopNestedScroll(child);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(target, dx, dy, consumed);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(target, velocityX, velocityY, consumed);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return super.onNestedPreFling(target, velocityX, velocityY);
    }

    @Override
    public int getNestedScrollAxes() {
        return super.getNestedScrollAxes();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public int getVerticalFadingEdgeLength() {
        return super.getVerticalFadingEdgeLength();
    }

    @Override
    public void setFadingEdgeLength(int length) {
        super.setFadingEdgeLength(length);
    }

    @Override
    public int getHorizontalFadingEdgeLength() {
        return super.getHorizontalFadingEdgeLength();
    }

    @Override
    public int getVerticalScrollbarWidth() {
        return super.getVerticalScrollbarWidth();
    }

    @Override
    protected int getHorizontalScrollbarHeight() {
        return super.getHorizontalScrollbarHeight();
    }

    @Override
    public void setVerticalScrollbarPosition(int position) {
        super.setVerticalScrollbarPosition(position);
    }

    @Override
    public int getVerticalScrollbarPosition() {
        return super.getVerticalScrollbarPosition();
    }

    @Override
    public void setScrollIndicators(int indicators) {
        super.setScrollIndicators(indicators);
    }

    @Override
    public void setScrollIndicators(int indicators, int mask) {
        super.setScrollIndicators(indicators, mask);
    }

    @Override
    public int getScrollIndicators() {
        return super.getScrollIndicators();
    }

    @Override
    public void setOnScrollChangeListener(OnScrollChangeListener l) {
        super.setOnScrollChangeListener(l);
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        super.setOnFocusChangeListener(l);
    }

    @Override
    public void addOnLayoutChangeListener(OnLayoutChangeListener listener) {
        super.addOnLayoutChangeListener(listener);
    }

    @Override
    public void removeOnLayoutChangeListener(OnLayoutChangeListener listener) {
        super.removeOnLayoutChangeListener(listener);
    }

    @Override
    public void addOnAttachStateChangeListener(OnAttachStateChangeListener listener) {
        super.addOnAttachStateChangeListener(listener);
    }

    @Override
    public void removeOnAttachStateChangeListener(OnAttachStateChangeListener listener) {
        super.removeOnAttachStateChangeListener(listener);
    }

    @Override
    public OnFocusChangeListener getOnFocusChangeListener() {
        return super.getOnFocusChangeListener();
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
    }

    @Override
    public boolean hasOnClickListeners() {
        return super.hasOnClickListeners();
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener l) {
        super.setOnLongClickListener(l);
    }

    @Override
    public void setOnContextClickListener(OnContextClickListener l) {
        super.setOnContextClickListener(l);
    }

    @Override
    public void setOnCreateContextMenuListener(OnCreateContextMenuListener l) {
        super.setOnCreateContextMenuListener(l);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean callOnClick() {
        return super.callOnClick();
    }

    @Override
    public boolean performLongClick() {
        return super.performLongClick();
    }

    @Override
    public boolean performLongClick(float x, float y) {
        return super.performLongClick(x, y);
    }

    @Override
    public boolean performContextClick(float x, float y) {
        return super.performContextClick(x, y);
    }

    @Override
    public boolean performContextClick() {
        return super.performContextClick();
    }

    @Override
    public boolean showContextMenu() {
        return super.showContextMenu();
    }

    @Override
    public boolean showContextMenu(float x, float y) {
        return super.showContextMenu(x, y);
    }

    @Override
    public ActionMode startActionMode(ActionMode.Callback callback) {
        return super.startActionMode(callback);
    }

    @Override
    public ActionMode startActionMode(ActionMode.Callback callback, int type) {
        return super.startActionMode(callback, type);
    }

    @Override
    public void setOnKeyListener(OnKeyListener l) {
        super.setOnKeyListener(l);
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        super.setOnTouchListener(l);
    }

    @Override
    public void setOnGenericMotionListener(OnGenericMotionListener l) {
        super.setOnGenericMotionListener(l);
    }

    @Override
    public void setOnHoverListener(OnHoverListener l) {
        super.setOnHoverListener(l);
    }

    @Override
    public void setOnDragListener(OnDragListener l) {
        super.setOnDragListener(l);
    }

    @Override
    public boolean requestRectangleOnScreen(Rect rectangle) {
        return super.requestRectangleOnScreen(rectangle);
    }

    @Override
    public boolean requestRectangleOnScreen(Rect rectangle, boolean immediate) {
        return super.requestRectangleOnScreen(rectangle, immediate);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    @Override
    public void sendAccessibilityEvent(int eventType) {
        super.sendAccessibilityEvent(eventType);
    }

    @Override
    public void announceForAccessibility(CharSequence text) {
        super.announceForAccessibility(text);
    }

    @Override
    public void sendAccessibilityEventUnchecked(AccessibilityEvent event) {
        super.sendAccessibilityEventUnchecked(event);
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        return super.dispatchPopulateAccessibilityEvent(event);
    }

    @Override
    public void onPopulateAccessibilityEvent(AccessibilityEvent event) {
        super.onPopulateAccessibilityEvent(event);
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
    }

    @Override
    public AccessibilityNodeInfo createAccessibilityNodeInfo() {
        return super.createAccessibilityNodeInfo();
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
    }

    @Override
    public void onProvideStructure(ViewStructure structure) {
        super.onProvideStructure(structure);
    }

    @Override
    public void onProvideVirtualStructure(ViewStructure structure) {
        super.onProvideVirtualStructure(structure);
    }

    @Override
    public void setAccessibilityDelegate(AccessibilityDelegate delegate) {
        super.setAccessibilityDelegate(delegate);
    }

    @Override
    public AccessibilityNodeProvider getAccessibilityNodeProvider() {
        return super.getAccessibilityNodeProvider();
    }

    @Override
    public CharSequence getContentDescription() {
        return super.getContentDescription();
    }

    @Override
    public void setContentDescription(CharSequence contentDescription) {
        super.setContentDescription(contentDescription);
    }

    @Override
    public void setAccessibilityTraversalBefore(int beforeId) {
        super.setAccessibilityTraversalBefore(beforeId);
    }

    @Override
    public int getAccessibilityTraversalBefore() {
        return super.getAccessibilityTraversalBefore();
    }

    @Override
    public void setAccessibilityTraversalAfter(int afterId) {
        super.setAccessibilityTraversalAfter(afterId);
    }

    @Override
    public int getAccessibilityTraversalAfter() {
        return super.getAccessibilityTraversalAfter();
    }

    @Override
    public int getLabelFor() {
        return super.getLabelFor();
    }

    @Override
    public void setLabelFor(int id) {
        super.setLabelFor(id);
    }

    @Override
    public boolean isFocused() {
        return super.isFocused();
    }

    @Override
    public boolean isScrollContainer() {
        return super.isScrollContainer();
    }

    @Override
    public void setScrollContainer(boolean isScrollContainer) {
        super.setScrollContainer(isScrollContainer);
    }

    @Override
    public int getDrawingCacheQuality() {
        return super.getDrawingCacheQuality();
    }

    @Override
    public void setDrawingCacheQuality(int quality) {
        super.setDrawingCacheQuality(quality);
    }

    @Override
    public boolean getKeepScreenOn() {
        return super.getKeepScreenOn();
    }

    @Override
    public void setKeepScreenOn(boolean keepScreenOn) {
        super.setKeepScreenOn(keepScreenOn);
    }

    @Override
    public int getNextFocusLeftId() {
        return super.getNextFocusLeftId();
    }

    @Override
    public void setNextFocusLeftId(int nextFocusLeftId) {
        super.setNextFocusLeftId(nextFocusLeftId);
    }

    @Override
    public int getNextFocusRightId() {
        return super.getNextFocusRightId();
    }

    @Override
    public void setNextFocusRightId(int nextFocusRightId) {
        super.setNextFocusRightId(nextFocusRightId);
    }

    @Override
    public int getNextFocusUpId() {
        return super.getNextFocusUpId();
    }

    @Override
    public void setNextFocusUpId(int nextFocusUpId) {
        super.setNextFocusUpId(nextFocusUpId);
    }

    @Override
    public int getNextFocusDownId() {
        return super.getNextFocusDownId();
    }

    @Override
    public void setNextFocusDownId(int nextFocusDownId) {
        super.setNextFocusDownId(nextFocusDownId);
    }

    @Override
    public int getNextFocusForwardId() {
        return super.getNextFocusForwardId();
    }

    @Override
    public void setNextFocusForwardId(int nextFocusForwardId) {
        super.setNextFocusForwardId(nextFocusForwardId);
    }

    @Override
    public boolean isShown() {
        return super.isShown();
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        return super.fitSystemWindows(insets);
    }

    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        return super.onApplyWindowInsets(insets);
    }

    @Override
    public void setOnApplyWindowInsetsListener(OnApplyWindowInsetsListener listener) {
        super.setOnApplyWindowInsetsListener(listener);
    }

    @Override
    public WindowInsets getRootWindowInsets() {
        return super.getRootWindowInsets();
    }

    @Override
    public WindowInsets computeSystemWindowInsets(WindowInsets in, Rect outLocalInsets) {
        return super.computeSystemWindowInsets(in, outLocalInsets);
    }

    @Override
    public void setFitsSystemWindows(boolean fitSystemWindows) {
        super.setFitsSystemWindows(fitSystemWindows);
    }

    @Override
    public boolean getFitsSystemWindows() {
        return super.getFitsSystemWindows();
    }

    @Override
    public void requestFitSystemWindows() {
        super.requestFitSystemWindows();
    }

    @Override
    public void requestApplyInsets() {
        super.requestApplyInsets();
    }

    @Override
    public int getVisibility() {
        return super.getVisibility();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    @Override
    public void setFocusable(boolean focusable) {
        super.setFocusable(focusable);
    }

    @Override
    public void setFocusableInTouchMode(boolean focusableInTouchMode) {
        super.setFocusableInTouchMode(focusableInTouchMode);
    }

    @Override
    public void setSoundEffectsEnabled(boolean soundEffectsEnabled) {
        super.setSoundEffectsEnabled(soundEffectsEnabled);
    }

    @Override
    public boolean isSoundEffectsEnabled() {
        return super.isSoundEffectsEnabled();
    }

    @Override
    public void setHapticFeedbackEnabled(boolean hapticFeedbackEnabled) {
        super.setHapticFeedbackEnabled(hapticFeedbackEnabled);
    }

    @Override
    public boolean isHapticFeedbackEnabled() {
        return super.isHapticFeedbackEnabled();
    }

    @Override
    public void setLayoutDirection(int layoutDirection) {
        super.setLayoutDirection(layoutDirection);
    }

    @Override
    public int getLayoutDirection() {
        return super.getLayoutDirection();
    }

    @Override
    public void setHasTransientState(boolean hasTransientState) {
        super.setHasTransientState(hasTransientState);
    }

    @Override
    public boolean isAttachedToWindow() {
        return super.isAttachedToWindow();
    }

    @Override
    public boolean isLaidOut() {
        return super.isLaidOut();
    }

    @Override
    public void setWillNotDraw(boolean willNotDraw) {
        super.setWillNotDraw(willNotDraw);
    }

    @Override
    public boolean willNotDraw() {
        return super.willNotDraw();
    }

    @Override
    public void setWillNotCacheDrawing(boolean willNotCacheDrawing) {
        super.setWillNotCacheDrawing(willNotCacheDrawing);
    }

    @Override
    public boolean willNotCacheDrawing() {
        return super.willNotCacheDrawing();
    }

    @Override
    public boolean isClickable() {
        return super.isClickable();
    }

    @Override
    public void setClickable(boolean clickable) {
        super.setClickable(clickable);
    }

    @Override
    public boolean isLongClickable() {
        return super.isLongClickable();
    }

    @Override
    public void setLongClickable(boolean longClickable) {
        super.setLongClickable(longClickable);
    }

    @Override
    public boolean isContextClickable() {
        return super.isContextClickable();
    }

    @Override
    public void setContextClickable(boolean contextClickable) {
        super.setContextClickable(contextClickable);
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
    }

    @Override
    public boolean isPressed() {
        return super.isPressed();
    }

    @Override
    public boolean isSaveEnabled() {
        return super.isSaveEnabled();
    }

    @Override
    public void setSaveEnabled(boolean enabled) {
        super.setSaveEnabled(enabled);
    }

    @Override
    public boolean getFilterTouchesWhenObscured() {
        return super.getFilterTouchesWhenObscured();
    }

    @Override
    public void setFilterTouchesWhenObscured(boolean enabled) {
        super.setFilterTouchesWhenObscured(enabled);
    }

    @Override
    public boolean isSaveFromParentEnabled() {
        return super.isSaveFromParentEnabled();
    }

    @Override
    public void setSaveFromParentEnabled(boolean enabled) {
        super.setSaveFromParentEnabled(enabled);
    }

    @Override
    public View focusSearch(int direction) {
        return super.focusSearch(direction);
    }

    @Override
    public ArrayList<View> getFocusables(int direction) {
        return super.getFocusables(direction);
    }

    @Override
    public void addFocusables(ArrayList<View> views, int direction) {
        super.addFocusables(views, direction);
    }

    @Override
    public ArrayList<View> getTouchables() {
        return super.getTouchables();
    }

    @Override
    public boolean isAccessibilityFocused() {
        return super.isAccessibilityFocused();
    }

    @Override
    public int getImportantForAccessibility() {
        return super.getImportantForAccessibility();
    }

    @Override
    public void setAccessibilityLiveRegion(int mode) {
        super.setAccessibilityLiveRegion(mode);
    }

    @Override
    public int getAccessibilityLiveRegion() {
        return super.getAccessibilityLiveRegion();
    }

    @Override
    public void setImportantForAccessibility(int mode) {
        super.setImportantForAccessibility(mode);
    }

    @Override
    public boolean isImportantForAccessibility() {
        return super.isImportantForAccessibility();
    }

    @Override
    public ViewParent getParentForAccessibility() {
        return super.getParentForAccessibility();
    }

    @Override
    public boolean dispatchNestedPrePerformAccessibilityAction(int action, Bundle arguments) {
        return super.dispatchNestedPrePerformAccessibilityAction(action, arguments);
    }

    @Override
    public boolean performAccessibilityAction(int action, Bundle arguments) {
        return super.performAccessibilityAction(action, arguments);
    }

    @Override
    public void dispatchStartTemporaryDetach() {
        super.dispatchStartTemporaryDetach();
    }

    @Override
    public void onStartTemporaryDetach() {
        super.onStartTemporaryDetach();
    }

    @Override
    public void dispatchFinishTemporaryDetach() {
        super.dispatchFinishTemporaryDetach();
    }

    @Override
    public void onFinishTemporaryDetach() {
        super.onFinishTemporaryDetach();
    }

    @Override
    public KeyEvent.DispatcherState getKeyDispatcherState() {
        return super.getKeyDispatcherState();
    }

    @Override
    public boolean onFilterTouchEventForSecurity(MotionEvent event) {
        return super.onFilterTouchEventForSecurity(event);
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        return super.dispatchGenericMotionEvent(event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
    }

    @Override
    public boolean hasWindowFocus() {
        return super.hasWindowFocus();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
    }

    @Override
    protected void onDisplayHint(int hint) {
        super.onDisplayHint(hint);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
    }

    @Override
    public void onVisibilityAggregated(boolean isVisible) {
        super.onVisibilityAggregated(isVisible);
    }

    @Override
    public int getWindowVisibility() {
        return super.getWindowVisibility();
    }

    @Override
    public void getWindowVisibleDisplayFrame(Rect outRect) {
        super.getWindowVisibleDisplayFrame(outRect);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean isInTouchMode() {
        return super.isInTouchMode();
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        return super.onKeyPreIme(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        return super.onKeyMultiple(keyCode, repeatCount, event);
    }

    @Override
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        return super.onKeyShortcut(keyCode, event);
    }

    @Override
    public boolean onCheckIsTextEditor() {
        return super.onCheckIsTextEditor();
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return super.onCreateInputConnection(outAttrs);
    }

    @Override
    public boolean checkInputConnectionProxy(View view) {
        return super.checkInputConnectionProxy(view);
    }

    @Override
    public void createContextMenu(ContextMenu menu) {
        super.createContextMenu(menu);
    }

    @Override
    protected ContextMenu.ContextMenuInfo getContextMenuInfo() {
        return super.getContextMenuInfo();
    }

    @Override
    protected void onCreateContextMenu(ContextMenu menu) {
        super.onCreateContextMenu(menu);
    }

    @Override
    public boolean onTrackballEvent(MotionEvent event) {
        return super.onTrackballEvent(event);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        return super.onGenericMotionEvent(event);
    }

    @Override
    public boolean isHovered() {
        return super.isHovered();
    }

    @Override
    public void setHovered(boolean hovered) {
        super.setHovered(hovered);
    }

    @Override
    public void onHoverChanged(boolean hovered) {
        super.onHoverChanged(hovered);
    }

    @Override
    public void cancelLongPress() {
        super.cancelLongPress();
    }

    @Override
    public void setTouchDelegate(TouchDelegate delegate) {
        super.setTouchDelegate(delegate);
    }

    @Override
    public TouchDelegate getTouchDelegate() {
        return super.getTouchDelegate();
    }

    @Override
    public void bringToFront() {
        super.bringToFront();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void setScrollX(int value) {
        super.setScrollX(value);
    }

    @Override
    public void setScrollY(int value) {
        super.setScrollY(value);
    }

    @Override
    public void getDrawingRect(Rect outRect) {
        super.getDrawingRect(outRect);
    }

    @Override
    public Matrix getMatrix() {
        return super.getMatrix();
    }

    @Override
    public float getCameraDistance() {
        return super.getCameraDistance();
    }

    @Override
    public void setCameraDistance(float distance) {
        super.setCameraDistance(distance);
    }

    @Override
    public float getRotation() {
        return super.getRotation();
    }

    @Override
    public void setRotation(float rotation) {
        super.setRotation(rotation);
    }

    @Override
    public float getRotationY() {
        return super.getRotationY();
    }

    @Override
    public void setRotationY(float rotationY) {
        super.setRotationY(rotationY);
    }

    @Override
    public float getRotationX() {
        return super.getRotationX();
    }

    @Override
    public void setRotationX(float rotationX) {
        super.setRotationX(rotationX);
    }

    @Override
    public float getScaleX() {
        return super.getScaleX();
    }

    @Override
    public void setScaleX(float scaleX) {
        super.setScaleX(scaleX);
    }

    @Override
    public float getScaleY() {
        return super.getScaleY();
    }

    @Override
    public void setScaleY(float scaleY) {
        super.setScaleY(scaleY);
    }

    @Override
    public float getPivotX() {
        return super.getPivotX();
    }

    @Override
    public void setPivotX(float pivotX) {
        super.setPivotX(pivotX);
    }

    @Override
    public float getPivotY() {
        return super.getPivotY();
    }

    @Override
    public void setPivotY(float pivotY) {
        super.setPivotY(pivotY);
    }

    @Override
    public float getAlpha() {
        return super.getAlpha();
    }

    @Override
    public void forceHasOverlappingRendering(boolean hasOverlappingRendering) {
        super.forceHasOverlappingRendering(hasOverlappingRendering);
    }

    @Override
    public boolean hasOverlappingRendering() {
        return super.hasOverlappingRendering();
    }

    @Override
    public void setAlpha(float alpha) {
        super.setAlpha(alpha);
    }

    @Override
    public boolean isDirty() {
        return super.isDirty();
    }

    @Override
    public float getX() {
        return super.getX();
    }

    @Override
    public void setX(float x) {
        super.setX(x);
    }

    @Override
    public float getY() {
        return super.getY();
    }

    @Override
    public void setY(float y) {
        super.setY(y);
    }

    @Override
    public float getZ() {
        return super.getZ();
    }

    @Override
    public void setZ(float z) {
        super.setZ(z);
    }

    @Override
    public float getElevation() {
        return super.getElevation();
    }

    @Override
    public void setElevation(float elevation) {
        super.setElevation(elevation);
    }

    @Override
    public float getTranslationX() {
        return super.getTranslationX();
    }

    @Override
    public void setTranslationX(float translationX) {
        super.setTranslationX(translationX);
    }

    @Override
    public float getTranslationY() {
        return super.getTranslationY();
    }

    @Override
    public void setTranslationY(float translationY) {
        super.setTranslationY(translationY);
    }

    @Override
    public float getTranslationZ() {
        return super.getTranslationZ();
    }

    @Override
    public void setTranslationZ(float translationZ) {
        super.setTranslationZ(translationZ);
    }

    @Override
    public StateListAnimator getStateListAnimator() {
        return super.getStateListAnimator();
    }

    @Override
    public void setStateListAnimator(StateListAnimator stateListAnimator) {
        super.setStateListAnimator(stateListAnimator);
    }

    @Override
    public void setClipToOutline(boolean clipToOutline) {
        super.setClipToOutline(clipToOutline);
    }

    @Override
    public void setOutlineProvider(ViewOutlineProvider provider) {
        super.setOutlineProvider(provider);
    }

    @Override
    public ViewOutlineProvider getOutlineProvider() {
        return super.getOutlineProvider();
    }

    @Override
    public void invalidateOutline() {
        super.invalidateOutline();
    }

    @Override
    public void getHitRect(Rect outRect) {
        super.getHitRect(outRect);
    }

    @Override
    public void getFocusedRect(Rect r) {
        super.getFocusedRect(r);
    }

    @Override
    public boolean getGlobalVisibleRect(Rect r, Point globalOffset) {
        return super.getGlobalVisibleRect(r, globalOffset);
    }

    @Override
    public void offsetTopAndBottom(int offset) {
        super.offsetTopAndBottom(offset);
    }

    @Override
    public void offsetLeftAndRight(int offset) {
        super.offsetLeftAndRight(offset);
    }

    @Override
    public ViewGroup.LayoutParams getLayoutParams() {
        return super.getLayoutParams();
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public void scrollBy(int x, int y) {
        super.scrollBy(x, y);
    }

    @Override
    protected boolean awakenScrollBars() {
        return super.awakenScrollBars();
    }

    @Override
    protected boolean awakenScrollBars(int startDelay) {
        return super.awakenScrollBars(startDelay);
    }

    @Override
    protected boolean awakenScrollBars(int startDelay, boolean invalidate) {
        return super.awakenScrollBars(startDelay, invalidate);
    }

    @Override
    public void invalidate(Rect dirty) {
        super.invalidate(dirty);
    }

    @Override
    public void invalidate(int l, int t, int r, int b) {
        super.invalidate(l, t, r, b);
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }

    @Override
    public boolean isOpaque() {
        return super.isOpaque();
    }

    @Override
    public Handler getHandler() {
        return super.getHandler();
    }

    @Override
    public boolean post(Runnable action) {
        return super.post(action);
    }

    @Override
    public boolean postDelayed(Runnable action, long delayMillis) {
        return super.postDelayed(action, delayMillis);
    }

    @Override
    public void postOnAnimation(Runnable action) {
        super.postOnAnimation(action);
    }

    @Override
    public void postOnAnimationDelayed(Runnable action, long delayMillis) {
        super.postOnAnimationDelayed(action, delayMillis);
    }

    @Override
    public boolean removeCallbacks(Runnable action) {
        return super.removeCallbacks(action);
    }

    @Override
    public void postInvalidate() {
        super.postInvalidate();
    }

    @Override
    public void postInvalidate(int left, int top, int right, int bottom) {
        super.postInvalidate(left, top, right, bottom);
    }

    @Override
    public void postInvalidateDelayed(long delayMilliseconds) {
        super.postInvalidateDelayed(delayMilliseconds);
    }

    @Override
    public void postInvalidateDelayed(long delayMilliseconds, int left, int top, int right, int bottom) {
        super.postInvalidateDelayed(delayMilliseconds, left, top, right, bottom);
    }

    @Override
    public void postInvalidateOnAnimation() {
        super.postInvalidateOnAnimation();
    }

    @Override
    public void postInvalidateOnAnimation(int left, int top, int right, int bottom) {
        super.postInvalidateOnAnimation(left, top, right, bottom);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
    }

    @Override
    public boolean isHorizontalFadingEdgeEnabled() {
        return super.isHorizontalFadingEdgeEnabled();
    }

    @Override
    public void setHorizontalFadingEdgeEnabled(boolean horizontalFadingEdgeEnabled) {
        super.setHorizontalFadingEdgeEnabled(horizontalFadingEdgeEnabled);
    }

    @Override
    public boolean isVerticalFadingEdgeEnabled() {
        return super.isVerticalFadingEdgeEnabled();
    }

    @Override
    public void setVerticalFadingEdgeEnabled(boolean verticalFadingEdgeEnabled) {
        super.setVerticalFadingEdgeEnabled(verticalFadingEdgeEnabled);
    }

    @Override
    protected float getTopFadingEdgeStrength() {
        return super.getTopFadingEdgeStrength();
    }

    @Override
    protected float getBottomFadingEdgeStrength() {
        return super.getBottomFadingEdgeStrength();
    }

    @Override
    protected float getLeftFadingEdgeStrength() {
        return super.getLeftFadingEdgeStrength();
    }

    @Override
    protected float getRightFadingEdgeStrength() {
        return super.getRightFadingEdgeStrength();
    }

    @Override
    public boolean isHorizontalScrollBarEnabled() {
        return super.isHorizontalScrollBarEnabled();
    }

    @Override
    public void setHorizontalScrollBarEnabled(boolean horizontalScrollBarEnabled) {
        super.setHorizontalScrollBarEnabled(horizontalScrollBarEnabled);
    }

    @Override
    public boolean isVerticalScrollBarEnabled() {
        return super.isVerticalScrollBarEnabled();
    }

    @Override
    public void setVerticalScrollBarEnabled(boolean verticalScrollBarEnabled) {
        super.setVerticalScrollBarEnabled(verticalScrollBarEnabled);
    }

    @Override
    public void setScrollbarFadingEnabled(boolean fadeScrollbars) {
        super.setScrollbarFadingEnabled(fadeScrollbars);
    }

    @Override
    public boolean isScrollbarFadingEnabled() {
        return super.isScrollbarFadingEnabled();
    }

    @Override
    public int getScrollBarDefaultDelayBeforeFade() {
        return super.getScrollBarDefaultDelayBeforeFade();
    }

    @Override
    public void setScrollBarDefaultDelayBeforeFade(int scrollBarDefaultDelayBeforeFade) {
        super.setScrollBarDefaultDelayBeforeFade(scrollBarDefaultDelayBeforeFade);
    }

    @Override
    public int getScrollBarFadeDuration() {
        return super.getScrollBarFadeDuration();
    }

    @Override
    public void setScrollBarFadeDuration(int scrollBarFadeDuration) {
        super.setScrollBarFadeDuration(scrollBarFadeDuration);
    }

    @Override
    public int getScrollBarSize() {
        return super.getScrollBarSize();
    }

    @Override
    public void setScrollBarSize(int scrollBarSize) {
        super.setScrollBarSize(scrollBarSize);
    }

    @Override
    public void setScrollBarStyle(int style) {
        super.setScrollBarStyle(style);
    }

    @Override
    public int getScrollBarStyle() {
        return super.getScrollBarStyle();
    }

    @Override
    protected int computeHorizontalScrollRange() {
        return super.computeHorizontalScrollRange();
    }

    @Override
    protected int computeHorizontalScrollOffset() {
        return super.computeHorizontalScrollOffset();
    }

    @Override
    protected int computeHorizontalScrollExtent() {
        return super.computeHorizontalScrollExtent();
    }

    @Override
    protected int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }

    @Override
    protected int computeVerticalScrollOffset() {
        return super.computeVerticalScrollOffset();
    }

    @Override
    protected int computeVerticalScrollExtent() {
        return super.computeVerticalScrollExtent();
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return super.canScrollHorizontally(direction);
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return super.canScrollVertically(direction);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void onScreenStateChanged(int screenState) {
        super.onScreenStateChanged(screenState);
    }

    @Override
    public boolean canResolveLayoutDirection() {
        return super.canResolveLayoutDirection();
    }

    @Override
    public boolean isLayoutDirectionResolved() {
        return super.isLayoutDirectionResolved();
    }

    @Override
    protected int getWindowAttachCount() {
        return super.getWindowAttachCount();
    }

    @Override
    public IBinder getWindowToken() {
        return super.getWindowToken();
    }

    @Override
    public WindowId getWindowId() {
        return super.getWindowId();
    }

    @Override
    public IBinder getApplicationWindowToken() {
        return super.getApplicationWindowToken();
    }

    @Override
    public Display getDisplay() {
        return super.getDisplay();
    }

    @Override
    public void onCancelPendingInputEvents() {
        super.onCancelPendingInputEvents();
    }

    @Override
    public void saveHierarchyState(SparseArray<Parcelable> container) {
        super.saveHierarchyState(container);
    }

    @Override
    public void restoreHierarchyState(SparseArray<Parcelable> container) {
        super.restoreHierarchyState(container);
    }

    @Override
    public long getDrawingTime() {
        return super.getDrawingTime();
    }

    @Override
    public void setDuplicateParentStateEnabled(boolean enabled) {
        super.setDuplicateParentStateEnabled(enabled);
    }

    @Override
    public boolean isDuplicateParentStateEnabled() {
        return super.isDuplicateParentStateEnabled();
    }

    @Override
    public void setLayerType(int layerType, Paint paint) {
        super.setLayerType(layerType, paint);
    }

    @Override
    public void setLayerPaint(Paint paint) {
        super.setLayerPaint(paint);
    }

    @Override
    public int getLayerType() {
        return super.getLayerType();
    }

    @Override
    public void buildLayer() {
        super.buildLayer();
    }

    @Override
    public void setDrawingCacheEnabled(boolean enabled) {
        super.setDrawingCacheEnabled(enabled);
    }

    @Override
    public boolean isDrawingCacheEnabled() {
        return super.isDrawingCacheEnabled();
    }

    @Override
    public Bitmap getDrawingCache() {
        return super.getDrawingCache();
    }

    @Override
    public Bitmap getDrawingCache(boolean autoScale) {
        return super.getDrawingCache(autoScale);
    }

    @Override
    public void destroyDrawingCache() {
        super.destroyDrawingCache();
    }

    @Override
    public void setDrawingCacheBackgroundColor(int color) {
        super.setDrawingCacheBackgroundColor(color);
    }

    @Override
    public int getDrawingCacheBackgroundColor() {
        return super.getDrawingCacheBackgroundColor();
    }

    @Override
    public void buildDrawingCache() {
        super.buildDrawingCache();
    }

    @Override
    public void buildDrawingCache(boolean autoScale) {
        super.buildDrawingCache(autoScale);
    }

    @Override
    public boolean isInEditMode() {
        return super.isInEditMode();
    }

    @Override
    protected boolean isPaddingOffsetRequired() {
        return super.isPaddingOffsetRequired();
    }

    @Override
    protected int getLeftPaddingOffset() {
        return super.getLeftPaddingOffset();
    }

    @Override
    protected int getRightPaddingOffset() {
        return super.getRightPaddingOffset();
    }

    @Override
    protected int getTopPaddingOffset() {
        return super.getTopPaddingOffset();
    }

    @Override
    protected int getBottomPaddingOffset() {
        return super.getBottomPaddingOffset();
    }

    @Override
    public boolean isHardwareAccelerated() {
        return super.isHardwareAccelerated();
    }

    @Override
    public void setClipBounds(Rect clipBounds) {
        super.setClipBounds(clipBounds);
    }

    @Override
    public Rect getClipBounds() {
        return super.getClipBounds();
    }

    @Override
    public boolean getClipBounds(Rect outRect) {
        return super.getClipBounds(outRect);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    public int getSolidColor() {
        return super.getSolidColor();
    }

    @Override
    public boolean isLayoutRequested() {
        return super.isLayoutRequested();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    public Resources getResources() {
        return super.getResources();
    }

    @Override
    public void invalidateDrawable(Drawable drawable) {
        super.invalidateDrawable(drawable);
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        super.scheduleDrawable(who, what, when);
    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
        super.unscheduleDrawable(who, what);
    }

    @Override
    public void unscheduleDrawable(Drawable who) {
        super.unscheduleDrawable(who);
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who);
    }

    @Override
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
    }

    @Override
    public void refreshDrawableState() {
        super.refreshDrawableState();
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
    }

    public void setBackgroundColor(int color, boolean colorToolbar) {
        if(colorToolbar) {
            setBackgroundColor(color);
        } else {
            if(Utils.useWhiteFont(color)) {
                setTitleTextColor(Color.WHITE);
            } else {
                setTitleTextColor(Color.BLACK);
            }
        }
    }

    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
    }

    @Override
    public void setBackground(Drawable background) {
        super.setBackground(background);
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        super.setBackgroundDrawable(background);
    }

    @Override
    public Drawable getBackground() {
        return super.getBackground();
    }

    @Override
    public void setBackgroundTintList(ColorStateList tint) {
        super.setBackgroundTintList(tint);
    }

    @Nullable
    @Override
    public ColorStateList getBackgroundTintList() {
        return super.getBackgroundTintList();
    }

    @Override
    public void setBackgroundTintMode(PorterDuff.Mode tintMode) {
        super.setBackgroundTintMode(tintMode);
    }

    @Nullable
    @Override
    public PorterDuff.Mode getBackgroundTintMode() {
        return super.getBackgroundTintMode();
    }

    @Override
    public Drawable getForeground() {
        return super.getForeground();
    }

    @Override
    public void setForeground(Drawable foreground) {
        super.setForeground(foreground);
    }

    @Override
    public int getForegroundGravity() {
        return super.getForegroundGravity();
    }

    @Override
    public void setForegroundGravity(int gravity) {
        super.setForegroundGravity(gravity);
    }

    @Override
    public void setForegroundTintList(ColorStateList tint) {
        super.setForegroundTintList(tint);
    }

    @Nullable
    @Override
    public ColorStateList getForegroundTintList() {
        return super.getForegroundTintList();
    }

    @Override
    public void setForegroundTintMode(PorterDuff.Mode tintMode) {
        super.setForegroundTintMode(tintMode);
    }

    @Nullable
    @Override
    public PorterDuff.Mode getForegroundTintMode() {
        return super.getForegroundTintMode();
    }

    @Override
    public void onDrawForeground(Canvas canvas) {
        super.onDrawForeground(canvas);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        super.setPaddingRelative(start, top, end, bottom);
    }

    @Override
    public int getPaddingTop() {
        return super.getPaddingTop();
    }

    @Override
    public int getPaddingBottom() {
        return super.getPaddingBottom();
    }

    @Override
    public int getPaddingLeft() {
        return super.getPaddingLeft();
    }

    @Override
    public int getPaddingStart() {
        return super.getPaddingStart();
    }

    @Override
    public int getPaddingRight() {
        return super.getPaddingRight();
    }

    @Override
    public int getPaddingEnd() {
        return super.getPaddingEnd();
    }

    @Override
    public boolean isPaddingRelative() {
        return super.isPaddingRelative();
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
    }

    @Override
    public boolean isSelected() {
        return super.isSelected();
    }

    @Override
    public void setActivated(boolean activated) {
        super.setActivated(activated);
    }

    @Override
    public boolean isActivated() {
        return super.isActivated();
    }

    @Override
    public ViewTreeObserver getViewTreeObserver() {
        return super.getViewTreeObserver();
    }

    @Override
    public View getRootView() {
        return super.getRootView();
    }

    @Override
    public void getLocationOnScreen(int[] outLocation) {
        super.getLocationOnScreen(outLocation);
    }

    @Override
    public void getLocationInWindow(int[] outLocation) {
        super.getLocationInWindow(outLocation);
    }

    @Override
    public void setId(int id) {
        super.setId(id);
    }

    @Override
    public int getId() {
        return super.getId();
    }

    @Override
    public Object getTag() {
        return super.getTag();
    }

    @Override
    public void setTag(Object tag) {
        super.setTag(tag);
    }

    @Override
    public Object getTag(int key) {
        return super.getTag(key);
    }

    @Override
    public void setTag(int key, Object tag) {
        super.setTag(key, tag);
    }

    @Override
    public int getBaseline() {
        return super.getBaseline();
    }

    @Override
    public boolean isInLayout() {
        return super.isInLayout();
    }

    @Override
    public void requestLayout() {
        super.requestLayout();
    }

    @Override
    public void forceLayout() {
        super.forceLayout();
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return super.getSuggestedMinimumHeight();
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return super.getSuggestedMinimumWidth();
    }

    @Override
    public int getMinimumHeight() {
        return super.getMinimumHeight();
    }

    @Override
    public void setMinimumHeight(int minHeight) {
        super.setMinimumHeight(minHeight);
    }

    @Override
    public int getMinimumWidth() {
        return super.getMinimumWidth();
    }

    @Override
    public void setMinimumWidth(int minWidth) {
        super.setMinimumWidth(minWidth);
    }

    @Override
    public Animation getAnimation() {
        return super.getAnimation();
    }

    @Override
    public void startAnimation(Animation animation) {
        super.startAnimation(animation);
    }

    @Override
    public void clearAnimation() {
        super.clearAnimation();
    }

    @Override
    public void setAnimation(Animation animation) {
        super.setAnimation(animation);
    }

    @Override
    protected void onAnimationStart() {
        super.onAnimationStart();
    }

    @Override
    protected void onAnimationEnd() {
        super.onAnimationEnd();
    }

    @Override
    protected boolean onSetAlpha(int alpha) {
        return super.onSetAlpha(alpha);
    }

    @Override
    public void playSoundEffect(int soundConstant) {
        super.playSoundEffect(soundConstant);
    }

    @Override
    public boolean performHapticFeedback(int feedbackConstant) {
        return super.performHapticFeedback(feedbackConstant);
    }

    @Override
    public boolean performHapticFeedback(int feedbackConstant, int flags) {
        return super.performHapticFeedback(feedbackConstant, flags);
    }

    @Override
    public void setSystemUiVisibility(int visibility) {
        super.setSystemUiVisibility(visibility);
    }

    @Override
    public int getSystemUiVisibility() {
        return super.getSystemUiVisibility();
    }

    @Override
    public int getWindowSystemUiVisibility() {
        return super.getWindowSystemUiVisibility();
    }

    @Override
    public void onWindowSystemUiVisibilityChanged(int visible) {
        super.onWindowSystemUiVisibilityChanged(visible);
    }

    @Override
    public void setOnSystemUiVisibilityChangeListener(OnSystemUiVisibilityChangeListener l) {
        super.setOnSystemUiVisibilityChangeListener(l);
    }

    @Override
    public boolean onDragEvent(DragEvent event) {
        return super.onDragEvent(event);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }

    @Override
    public int getOverScrollMode() {
        return super.getOverScrollMode();
    }

    @Override
    public void setOverScrollMode(int overScrollMode) {
        super.setOverScrollMode(overScrollMode);
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        super.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return super.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return super.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        super.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return super.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return super.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return super.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return super.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return super.dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public void setTextDirection(int textDirection) {
        super.setTextDirection(textDirection);
    }

    @Override
    public int getTextDirection() {
        return super.getTextDirection();
    }

    @Override
    public boolean canResolveTextDirection() {
        return super.canResolveTextDirection();
    }

    @Override
    public boolean isTextDirectionResolved() {
        return super.isTextDirectionResolved();
    }

    @Override
    public void setTextAlignment(int textAlignment) {
        super.setTextAlignment(textAlignment);
    }

    @Override
    public int getTextAlignment() {
        return super.getTextAlignment();
    }

    @Override
    public boolean canResolveTextAlignment() {
        return super.canResolveTextAlignment();
    }

    @Override
    public boolean isTextAlignmentResolved() {
        return super.isTextAlignmentResolved();
    }

    @Override
    public void setPointerIcon(PointerIcon pointerIcon) {
        super.setPointerIcon(pointerIcon);
    }

    @Override
    public PointerIcon getPointerIcon() {
        return super.getPointerIcon();
    }

    @Override
    public ViewPropertyAnimator animate() {
        return super.animate();
    }

    @Override
    public String getTransitionName() {
        return super.getTransitionName();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
