package com.example.habittracker.presentation.ui

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior


class AutoCollapseBottomSheetBehavior<V : View>(context: Context, attrs: AttributeSet) :
    BottomSheetBehavior<V>(context, attrs) {

    override fun onAttachedToLayoutParams(layoutParams: CoordinatorLayout.LayoutParams) {
        super.onAttachedToLayoutParams(layoutParams)
        state = STATE_COLLAPSED
        peekHeight = 110
    }

    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: V,
        event: MotionEvent
    ): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && state == STATE_EXPANDED) {
            val outRect = Rect()
            child.getGlobalVisibleRect(outRect)
            if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                state = STATE_COLLAPSED
            }
        }
        return super.onInterceptTouchEvent(parent, child, event)
    }
}