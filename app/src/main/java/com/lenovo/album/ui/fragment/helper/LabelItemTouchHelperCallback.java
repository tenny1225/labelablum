package com.lenovo.album.ui.fragment.helper;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by noahkong on 17-6-12.
 */

public class LabelItemTouchHelperCallback extends ItemTouchHelper.Callback {
    public interface SwitchItemListener {
        void onItemMove(int position1, int position2);
    }

    private SwitchItemListener switchItemListener;

    public LabelItemTouchHelperCallback(SwitchItemListener listener) {
        this.switchItemListener = listener;
    }

    private boolean enable;

    public void setEnable(boolean b) {
        this.enable = b;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView,final  RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        //设置侧滑方向为从左到右和从右到左都可以
        //final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        //将方向参数设置进去

        return makeMovementFlags(enable ? dragFlags : 0, 0);

    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if (!enable) {
            return false;
        }

        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        //回调adapter中的onItemMove方法
        if (switchItemListener != null) {
            switchItemListener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        }

        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public boolean isLongPressDragEnabled() {

        return false;
    }
}
