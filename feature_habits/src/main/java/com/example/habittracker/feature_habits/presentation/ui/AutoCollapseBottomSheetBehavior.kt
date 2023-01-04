package com.example.habittracker.feature_habits.presentation.ui

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior


class AutoCollapseBottomSheetBehavior<V : View>(private val context: Context, attrs: AttributeSet) :
    BottomSheetBehavior<V>(context, attrs) {

    override fun onAttachedToLayoutParams(layoutParams: CoordinatorLayout.LayoutParams) {
        super.onAttachedToLayoutParams(layoutParams)
        setUpCollapseParameters()
    }

    private fun setUpCollapseParameters() {
        val density = context.resources.displayMetrics.density
        state = STATE_COLLAPSED
        peekHeight = PEEK_HEIGHT * density.toInt()
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

    companion object {
        private const val PEEK_HEIGHT = 60
    }
}