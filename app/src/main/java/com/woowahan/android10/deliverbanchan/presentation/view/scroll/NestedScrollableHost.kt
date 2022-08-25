package com.woowahan.android10.deliverbanchan.presentation.view.scroll

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import kotlin.math.absoluteValue
import kotlin.math.sign

class NestedScrollableHost : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var touchSlop = 0
    private var initialX = 0f
    private var initialY = 0f
    private val parentViewPager: ViewPager2?
        get() {
            var v: View? = parent as? View
            while (v != null && v !is ViewPager2) {
                v = v.parent as? View
            }
            return v as? ViewPager2
        }

    private val child: View? get() = if (childCount > 0) getChildAt(0) else null

    init {
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    private fun canChildScroll(orientation: Int, delta: Float): Boolean {
        val direction = -delta.sign.toInt()
        return when (orientation) {
            0 -> child?.canScrollHorizontally(direction) ?: false
            1 -> child?.canScrollVertically(direction) ?: false
            else -> throw IllegalArgumentException()
        }
    }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        handleInterceptTouchEvent(e)
        return super.onInterceptTouchEvent(e)
    }

    private fun handleInterceptTouchEvent(e: MotionEvent) {
        val orientation = parentViewPager?.orientation ?: return

        // 자식뷰가 어느 방향으로도 스크롤 할 수 없는 경우에는 바로 return
        if (!canChildScroll(orientation, -1f) && !canChildScroll(orientation, 1f)) {
            return
        }

        if (e.action == MotionEvent.ACTION_DOWN) {
            initialX = e.x
            initialY = e.y
            parent.requestDisallowInterceptTouchEvent(true)
        } else if (e.action == MotionEvent.ACTION_MOVE) {
            val dx = e.x - initialX
            val dy = e.y - initialY
            val isVpHorizontal = orientation == ORIENTATION_HORIZONTAL

            // ViewPager2의 touch-slop이 자식 뷰의 touch-slop 보다 2배 크다고 가정하는 것을 하기 위함
            val scaledDx = dx.absoluteValue * if (isVpHorizontal) .5f else 1f
            val scaledDy = dy.absoluteValue * if (isVpHorizontal) 1f else .5f

            if (scaledDx > touchSlop || scaledDy > touchSlop) {
                if (isVpHorizontal == (scaledDy > scaledDx)) {
                    // 수직 스크롤인 경우에는 부모뷰가 터치 이벤트 가져가도록 설정
                    parent.requestDisallowInterceptTouchEvent(false)
                } else {
                    // 수평 스크롤인 경우
                    if (canChildScroll(orientation, if (isVpHorizontal) dx else dy)) {
                        // 자식뷰가 현재 방향으로 수평 스크롤 가능 시 이벤트 가져온다
                        Log.e("NestedScrollableHost", "can scroll")
                        parent.requestDisallowInterceptTouchEvent(true)
                    } else {
                        // 반대로 자식뷰에서 현재 방향(우측 or 좌측)으로 스크롤 더 이상 안될 시 부모뷰로 이벤트 넘긴다
                        Log.e("NestedScrollableHost", "cannot scroll")
                        parent.requestDisallowInterceptTouchEvent(false)
                    }
                }
            }
        }
    }
}
