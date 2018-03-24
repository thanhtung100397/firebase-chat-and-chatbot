package com.ttt.chat_module.common.recycler_view_adapter;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Admin on 5/12/2017.
 */

public abstract class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static String TAG = "RecyclerViewAdapter";
    public static final int VIEW_TYPE_NORMAL = 0;

    public static AtomicInteger idGenerator = new AtomicInteger();

    private List<ModelWrapper> listWrapperModels;
    private List<ModelWrapper> listWrapperModelsBackup;

    private LayoutInflater inflater;
    private List<OnItemClickListener> onItemClickListeners;
    private OnItemTouchChangedListener onItemTouchChangeListener;
    private OnItemSelectionChangedListener onItemSelectionChangeListener;
    private boolean selectedMode;
    private RecyclerView recyclerView;
    private Context context;

    public RecyclerViewAdapter(Context context, boolean enableSelectedMode) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.listWrapperModels = new ArrayList<>();
        this.onItemClickListeners = new ArrayList<>(1);

        setSelectedMode(enableSelectedMode);
    }

    protected DiffUtilCallBack initDiffUtilCallback(List<ModelWrapper> oldItems, List<ModelWrapper> newItems) {
        return new DiffUtilCallBack(oldItems, newItems);
    }

    public void backup() {
        listWrapperModelsBackup = new ArrayList<>(listWrapperModels.size());
        try {
            for (ModelWrapper modelWrapper : listWrapperModels) {
                listWrapperModelsBackup.add(modelWrapper.clone());
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public void recoverBackup() {
        this.listWrapperModels = listWrapperModelsBackup;
        notifyItemRangeChanged(0, getItemCount());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = null;
        this.context = null;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void addOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListeners.add(onItemClickListener);
    }

    private void notifyItemClickListener(RecyclerView.Adapter adapter,
                                         RecyclerView.ViewHolder viewHolder,
                                         int viewType,
                                         int position) {
        for (OnItemClickListener onItemClickListener : onItemClickListeners) {
            onItemClickListener.onItemClick(adapter, viewHolder, viewType, position);
        }
    }

    public void removeOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListeners.remove(onItemClickListener);
    }

    public void setOnItemTouchChangeListener(OnItemTouchChangedListener onItemTouchChangeListener) {
        this.onItemTouchChangeListener = onItemTouchChangeListener;
    }

    public void setOnItemSelectionChangeListener(OnItemSelectionChangedListener onItemSelectionChangeListener) {
        this.onItemSelectionChangeListener = onItemSelectionChangeListener;
    }

    public void clear() {
        int itemCount = getItemCount();
        listWrapperModels.clear();
        notifyItemRangeRemoved(0, itemCount);
    }

    public <T> void refresh(List<T> models) {
        int itemCount = getItemCount();
        listWrapperModels.clear();
        notifyItemRangeRemoved(0, itemCount);
        addModels(models, false);
    }

    public <T> void addModels(List<T> listModels, boolean isScroll) {
        addModels(listModels, VIEW_TYPE_NORMAL, isScroll);
    }

    public <T> void addModels(List<T> listModels, int viewType, boolean isScroll) {
        addModels(listModels, 0, listModels.size() - 1, viewType, isScroll);
    }

    public <T> void addModels(List<T> listModels, int fromIndex, int toIndex, int viewType, boolean isScroll) {
        int startInsertedPosition = getItemCount();
        int endInsertedPosition = startInsertedPosition + listModels.size();
        for (int i = fromIndex; i <= toIndex; i++) {
            addModel(listModels.get(i), viewType, false, false);
        }
        notifyItemRangeInserted(startInsertedPosition, endInsertedPosition);
        if (isScroll) {
            getRecyclerView().scrollToPosition(listWrapperModels.size() - 1);
        }
    }

    public void addModel(Object model, boolean isScroll) {
        addModel(listWrapperModels.size(), model, isScroll);
    }

    public void addModel(Object model, boolean isScroll, boolean isUpdate) {
        addModel(listWrapperModels.size(), model, isScroll, isUpdate);
    }

    public void addModel(Object model, int viewType, boolean isScroll) {
        addModel(listWrapperModels.size(), model, viewType, isScroll);
    }

    public void addModel(Object model, int viewType, boolean isScroll, boolean isUpdate) {
        addModel(listWrapperModels.size(), model, viewType, isScroll, isUpdate);
    }

    public void addModel(int index, Object model, boolean isScroll) {
        addModel(index, model, VIEW_TYPE_NORMAL, isScroll, true);
    }

    public void addModel(int index, Object model, boolean isScroll, boolean isUpdate) {
        addModel(index, model, VIEW_TYPE_NORMAL, isScroll, isUpdate);
    }

    public void addModel(int index, Object model, int viewType, boolean isScroll) {
        addModel(index, model, viewType, isScroll, true);
    }

    public void updateModel(int position, Object model, boolean isScroll) {
        getListWrapperModels().get(position).model = model;
        notifyItemChanged(position);
        if (isScroll) {
            getRecyclerView().scrollToPosition(position);
        }
    }

    public void addModel(int index, Object model, int viewType, boolean isScroll, boolean isUpdate) {
        ModelWrapper modelWrapper = new ModelWrapper(model, viewType);
        this.listWrapperModels.add(index, modelWrapper);
        if (isUpdate) {
            notifyItemInserted(index);
        }
        if (isScroll) {
            getRecyclerView().scrollToPosition(index);
        }
    }

    public void removeModel(int index) {
        this.removeModel(index, true);
    }

    public void removeModel(int index, boolean isUpdate) {
        this.listWrapperModels.remove(index);
        if (isUpdate) {
            notifyItemRemoved(index);
        }
    }

    public void setSelectedMode(boolean isSelected) {
        if (this.selectedMode && !isSelected) {
            deSelectAllItems(null);
            notifyItemRangeChanged(0, getItemCount());
        }
        selectedMode = isSelected;
    }

    public boolean isInSelectedMode() {
        return selectedMode;
    }

    public void setSelectedItem(int position, boolean isSelected) {
        if (selectedMode && position >= 0 && position < listWrapperModels.size()) {
            ModelWrapper modelWrapper = listWrapperModels.get(position);
            if (modelWrapper.isSelected != isSelected) {
                listWrapperModels.get(position).isSelected = isSelected;
                notifyItemChanged(position);
            }
        }
    }

    public void deSelectAllItems(OnEachUnSelectedItem onEachUnSelectedItem) {
        int size = listWrapperModels.size();
        for (int i = 0; i < size; i++) {
            ModelWrapper modelWrapper = listWrapperModels.get(i);
            if (onEachUnSelectedItem != null && !modelWrapper.isSelected) {
                onEachUnSelectedItem.onEachUnselectedItem(modelWrapper);
            }
            modelWrapper.isSelected = false;
        }
    }

    public interface OnEachUnSelectedItem {
        void onEachUnselectedItem(ModelWrapper modelWrapper);
    }

    public void removeAllSelectedItems() {
        if (selectedMode) {
            List<ModelWrapper> listItemLeft = new ArrayList<>();
            deSelectAllItems(listItemLeft::add);

            DiffUtil.DiffResult diffResult = DiffUtil
                    .calculateDiff(initDiffUtilCallback(listWrapperModels, listItemLeft));

            listWrapperModels = listItemLeft;
            diffResult.dispatchUpdatesTo(this);
        }
    }

    public boolean isItemSelected(int position) {
        return selectedMode && position >= 0 &&
                position < listWrapperModels.size() &&
                listWrapperModels.get(position).isSelected;
    }

    public <T> List<T> getSelectedItemModel(Class<T> type) {
        List<T> result = new ArrayList<>();
        for (ModelWrapper modelWrapper : listWrapperModels) {
            Object model = modelWrapper.model;
            if (modelWrapper.isSelected && model != null) {
                if (model.getClass().equals(type)) {
                    result.add(type.cast(modelWrapper.model));
                }
            }
        }
        return result;
    }

    public <T> void forEachModels(Class<T> type, OnEachModel<T> onEachModel) {
        for (ModelWrapper modelWrapper : listWrapperModels) {
            Object model = modelWrapper.model;
            if (model != null && model.getClass().equals(type)) {
                onEachModel.onEachModel(type.cast(model));
            }
        }
    }

    public void forEachItem(OnEachItem onEachItem) {
        for (ModelWrapper modelWrapper : listWrapperModels) {
            onEachItem.onEachItem(modelWrapper);
        }
    }

    public interface OnEachItem {
        void onEachItem(ModelWrapper modelWrapper);
    }

    public interface OnEachModel<T> {
        void onEachModel(T model);
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    public <T> T getItem(int position, Class<T> classType) {
        return classType.cast(listWrapperModels.get(position).model);
    }

    @Override
    public int getItemViewType(int position) {
        return listWrapperModels.get(position).viewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        final RecyclerView.ViewHolder viewHolder = solvedOnCreateViewHolder(parent, viewType);
        setClickStateBackground(viewHolder.itemView, viewType, false);
        viewHolder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        if (onItemTouchChangeListener != null) {
                            onItemTouchChangeListener.onItemPress(viewHolder, viewType);
                        }
                    }
                    break;

                    case MotionEvent.ACTION_CANCEL: {
                        if (onItemTouchChangeListener != null) {
                            onItemTouchChangeListener.onItemRelease(viewHolder, viewType);
                        }
                    }
                    break;

                    case MotionEvent.ACTION_UP: {
                        int itemPosition = getItemPosition(view);
                        setClickStateBackground(view, viewType, false);
                        notifyItemClickListener(RecyclerViewAdapter.this, viewHolder, viewType, itemPosition);
                    }
                    break;

                    default: {
                        break;
                    }
                }
                return false;
            }
        });
        return viewHolder;
    }

    private int getItemPosition(View view) {
        return recyclerView.getChildLayoutPosition(view);
    }

    protected RecyclerView.ViewHolder solvedOnCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_NORMAL) {
            return initNormalViewHolder(parent);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ModelWrapper modelWrapper = listWrapperModels.get(position);
        int viewType = modelWrapper.viewType;
        if (onItemSelectionChangeListener != null) {
            onItemSelectionChangeListener.onItemSelectionChanged(holder, viewType, modelWrapper.isSelected);
        }
        solvedOnBindViewHolder(holder, viewType, position);
    }

    protected void solvedOnBindViewHolder(RecyclerView.ViewHolder viewHolder, int viewType, int position) {
        if (viewType == VIEW_TYPE_NORMAL) {
            bindNormalViewHolder((NormalViewHolder) viewHolder, position);
        }
    }

    protected abstract RecyclerView.ViewHolder initNormalViewHolder(ViewGroup parent);

    protected abstract void bindNormalViewHolder(NormalViewHolder holder, int position);

    protected void setClickStateBackground(View view, int viewType, boolean isPress) {

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<ModelWrapper> getListWrapperModels() {
        return listWrapperModels;
    }

    public void setListWrapperModels(List<ModelWrapper> listWrapperModels) {
        this.listWrapperModels = listWrapperModels;
    }

    public void clearListSelectedItems() {
        for (ModelWrapper modelWrapper : listWrapperModels) {
            modelWrapper.isSelected = false;
        }
    }

    @Override
    public int getItemCount() {
        return listWrapperModels.size();
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerView.Adapter adapter, RecyclerView.ViewHolder viewHolder, int viewType, int position);
    }

    public interface OnItemSelectionChangedListener {
        void onItemSelectionChanged(RecyclerView.ViewHolder viewHolder, int viewType, boolean isSelected);
    }

    public interface OnItemTouchChangedListener {
        void onItemPress(RecyclerView.ViewHolder viewHolder, int viewType);

        void onItemRelease(RecyclerView.ViewHolder viewHolder, int viewType);
    }

    public static class NormalViewHolder extends RecyclerView.ViewHolder {
        public NormalViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class ModelWrapper implements Cloneable {
        int id = idGenerator.getAndIncrement();
        Object model;
        int viewType;
        boolean isSelected = false;

        public ModelWrapper(Object model, int viewType) {
            this.model = model;
            this.viewType = viewType;
        }

        public Object getModel() {
            return model;
        }

        public boolean isSelected() {
            return isSelected;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof ModelWrapper)) {
                return false;
            }
            ModelWrapper modelWrapper = (ModelWrapper) obj;
            if (modelWrapper.viewType != this.viewType) {
                return false;
            }

            if (modelWrapper.isSelected != this.isSelected) {
                return false;
            }

            if (modelWrapper.model == null) {
                return this.model == null;
            } else {
                return modelWrapper.model.equals(this.model);
            }
        }

        @Override
        protected ModelWrapper clone() throws CloneNotSupportedException {
            return (ModelWrapper) super.clone();
        }
    }
}