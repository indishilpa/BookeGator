package com.sqlite.sqliteapp.Views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.content.Intent;

import com.sqlite.sqliteapp.Main2Activity;
import com.sqlite.sqliteapp.MainActivity;

/**
 * Created by Prakriti on 10/7/2015.
 */
public class MenuFly extends LinearLayout {
    private View menu;
    private View content;

    // Constants
    protected static final int menuMargin = 300;

    public enum MenuState {
        CLOSED, OPEN
    };

    // Position information attributes
    protected int currentContentOffset = 0;
    protected MenuState menuCurrentState = MenuState.CLOSED;

    public MenuFly(Context context, AttributeSet attributeSet, int defStyle){
        super(context,attributeSet,defStyle);
    }

    public MenuFly(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
    }

    public MenuFly(Context context){
        super(context);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        this.menu = this.getChildAt(0);
        this.content = this.getChildAt(1);

        this.menu.setVisibility(View.GONE);
    }

    private int getMenuHeight(){
        return this.menu.getLayoutParams().height;
    }

    private void getMenuDimensions(){
        this.content.getLayoutParams().height = this.getHeight();
        this.content.getLayoutParams().width = this.getWidth();

        this.menu.getLayoutParams().width = this.getWidth();
        this.menu.getLayoutParams().height = this.getHeight() - menuMargin;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,int bottom) {
        if (changed)
            this.getMenuDimensions();

        this.menu.layout(left, top, right, bottom - menuMargin);

        this.content.layout(left, top + this.currentContentOffset, right, bottom + this.currentContentOffset);

    }

    public void toggleMenu() {
        switch (this.menuCurrentState) {

            case CLOSED:
                this.menu.setVisibility(View.VISIBLE);
                this.currentContentOffset = this.getMenuHeight();
                this.content.offsetTopAndBottom(currentContentOffset);
                this.menuCurrentState = MenuState.OPEN;
                break;
            case OPEN:
                this.content.offsetTopAndBottom(-currentContentOffset);
                this.currentContentOffset = 0;
                this.menuCurrentState = MenuState.CLOSED;
                this.menu.setVisibility(View.GONE);
                break;
        }

        this.invalidate();
    }


}
