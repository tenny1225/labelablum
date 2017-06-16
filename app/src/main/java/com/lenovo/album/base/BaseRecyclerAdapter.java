package com.lenovo.album.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by noahkong on 17-6-5.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerAdapter.VH> {
    /**
     * 自定义点击监听接口
     * @param <S>
     */
    public interface ListItemClickListener<S> {
        void onItemClick(S s);
        void onItemLongClick(S s);
    }
    /**
     * 自定义点击监听接口
     * @param <S>
     */
    public interface ListItemHolderClickListener<S> {
        void onItemHolderClick(VH holder,S s);
        void onItemHolderLongClick(VH holder,S s);
    }


    private List<T> itemList;//列表数据



    public BaseRecyclerAdapter(List<T> itemList) {
        this.itemList = itemList;
    }


    private ListItemClickListener<T> listItemClickListener;

    /**
     * 设置点击事件监听接口
     * @param listItemClickListener
     */
    public void setListItemClickListener(ListItemClickListener<T> listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }
    protected ListItemHolderClickListener<T> listItemHolderClickListener;

    public void setListItemHolderClickListener(ListItemHolderClickListener<T> listItemHolderClickListener) {
        this.listItemHolderClickListener = listItemHolderClickListener;
    }

    public int getItemIndex(T t){
        return itemList.indexOf(t);
    }
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
       final VH vh = new VH(getItemView(parent, viewType));
        vh.setItemListener(new VH.ItemListener() {
            @Override
            public void onClick(int p) {
                if(listItemClickListener!=null){
                    listItemClickListener.onItemClick(itemList.get(p));
                }
                if(listItemHolderClickListener!=null){
                    listItemHolderClickListener.onItemHolderClick(vh,itemList.get(p));
                }
            }

            @Override
            public void onLongClick(int p) {
                if(listItemClickListener!=null){
                    listItemClickListener.onItemLongClick(itemList.get(p));
                }
                if(listItemHolderClickListener!=null){
                    listItemHolderClickListener.onItemHolderLongClick(vh,itemList.get(p));
                }
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (itemList == null || itemList.size() <= position) {
            return;
        }

        onBind(holder, itemList.get(position));
    }

    /**
     * 获取item的view
     *
     * @param parent   上下文
     * @param viewType item的类型
     * @return
     */
    protected abstract View getItemView(ViewGroup parent, int viewType);

    /**
     * item与数据的绑定
     *
     * @param holder item的view集合
     * @param obj    数据
     */
    protected abstract void onBind(VH holder, T obj);

    @Override
    public int getItemCount() {
        if (itemList != null) {
            return itemList.size();
        }
        return 0;
    }

    public static class VH extends RecyclerView.ViewHolder {

        public interface ItemListener {
            void onClick(int p);

            void onLongClick(int p);
        }

        private ItemListener itemListener;

        private SparseArray<View> viewMap;

        public View rootView;


        public VH(final View itemView) {
            super(itemView);
            this.rootView = itemView;
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemListener != null) {
                        itemListener.onClick(getAdapterPosition());
                    }
                }
            });
            rootView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (itemListener != null) {
                        itemListener.onLongClick(getAdapterPosition());
                    }
                    return false;
                }
            });
            this.viewMap = new SparseArray<>();
        }

        public void setItemListener(ItemListener itemListener) {
            this.itemListener = itemListener;
        }

        /**
         * 根据id获取view
         *
         * @param id  view的id
         * @param <T> view的类型
         * @return
         */
        public <T extends View> T $(int id) {
            View v = viewMap.get(id);
            if (v == null) {
                v = rootView.findViewById(id);
                viewMap.put(id, v);
            }
            return (T) v;
        }
    }
}
