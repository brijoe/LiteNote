package org.bridge.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.bridge.activity.MainActivity;
import org.bridge.data.LiteNoteDB;
import org.bridge.model.NoteBean;
import org.bridge.litenote.R;
import org.bridge.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 装载Note数据的适配器类
 */
public class NoteItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ADD = 0;
    private static final int TYPE_NOTE = 1;
    private List<NoteBean> items = new ArrayList<>();
    private List<Integer> delItemIDs = new ArrayList<>();
    private List<Boolean> itemSelectedStates = new ArrayList<>();
    private List<View> cardViews = new ArrayList<>();
    private Context context;
    private Boolean isDelActivated = false;
    private int count = 0;

    public NoteItemAdapter(Context context, List<NoteBean> items) {
        this.context = context;
        this.items = items;
        for (int i = 0; i < items.size(); i++)
            itemSelectedStates.add(false);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        if (viewType == TYPE_ADD) {
            view = LayoutInflater.from(context).inflate(R.layout.item_add,
                    viewGroup, false);
            return new NoteAddViewHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_card,
                    viewGroup, false);
            return new NoteItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (position > 0) {
            final NoteBean noteBean = items.get(position - 1);
            TextView tvContent = ((NoteItemViewHolder) holder).getTvContent();
            TextView tvPubDate = ((NoteItemViewHolder) holder).getTvPudDate();
            final View cardNote = ((NoteItemViewHolder) holder).getCardNote();
            tvContent.setText(noteBean.getContent());
            tvPubDate.setText(DateUtil.formatTime(noteBean.getPubDate()));
            //点击操作
            cardNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "点击了item", Toast.LENGTH_SHORT).show();
                    if (!isDelActivated) {//执行跳转编辑操作
                        ((MainActivity) context).startEditNoteIntent(noteBean);
                    } else {//执行点选删除操作
                        if (!itemSelectedStates.get(position - 1)) {

                            setDelAction(cardNote, noteBean, true);
                            itemSelectedStates.set(position - 1, true);
                        } else {
                            setDelAction(cardNote, noteBean, false);
                            itemSelectedStates.set(position - 1, false);
                        }
                    }
                }
            });
            //长按执行激活删除面板
            cardNote.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(context, "长按了item", Toast.LENGTH_SHORT).show();
                    isDelActivated = true;
                    ((MainActivity) context).handleDelActionLayout(true);
                    if (!itemSelectedStates.get(position - 1)) {

                        setDelAction(cardNote, noteBean, true);
                        itemSelectedStates.set(position - 1, true);
                    } else {
                        setDelAction(cardNote, noteBean, false);
                        itemSelectedStates.set(position - 1, false);
                    }
                    return true;
                }
            });

        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_ADD;
        else
            return TYPE_NOTE;
    }

    @Override
    public int getItemCount() {
        return items.size() + 1;//增加了一个item布局
    }

    /**
     * 用于Note 添加的ViewHolder
     */
    class NoteAddViewHolder extends RecyclerView.ViewHolder {
        Button btnAddNote;

        public NoteAddViewHolder(View itemView) {
            super(itemView);
            btnAddNote = (Button) itemView.findViewById(R.id.btnAddNote);
            btnAddNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "添加", Toast.LENGTH_SHORT).show();
                    ((MainActivity) context).startPubNoteIntent();
                }
            });
        }
    }

    /**
     * 用于Note item内容的ViewHolder
     */
    class NoteItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent;
        TextView tvPudDate;
        LinearLayout cardNote;

        public NoteItemViewHolder(View itemView) {
            super(itemView);
            cardNote = (LinearLayout) itemView.findViewById(R.id.cardNote);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
            tvPudDate = (TextView) itemView.findViewById(R.id.tvPubDate);
        }

        public TextView getTvContent() {
            return tvContent;
        }

        public TextView getTvPudDate() {
            return tvPudDate;
        }

        public LinearLayout getCardNote() {
            return cardNote;
        }
    }

    private void setDelAction(View cardNote, NoteBean noteEntry, Boolean isSelected) {
        if (isSelected) {
            cardNote.setBackgroundColor(Color.RED);
            delItemIDs.add(noteEntry.getId());
            cardViews.add(cardNote);
            count++;

        } else {
            cardNote.setBackgroundColor(Color.WHITE);
            delItemIDs.remove(new Integer(noteEntry.getId()));
            cardViews.remove(cardNote);
            count--;
        }
        count = (count < 0) ? 0 : count;
        ((MainActivity) context).setDelActionCount(count);

    }

    public void performDelAction(LiteNoteDB liteNoteDB) {
        if (liteNoteDB != null) {
            int[] delItems = new int[delItemIDs.size()];
            for (int i = 0; i < delItemIDs.size(); i++)
                delItems[i] = delItemIDs.get(i).intValue();
            liteNoteDB.deleteNoteItem(delItems);
        }
    }

    public void cancelDel() {
        //数量为0
        count = 0;
        //激活标志置为false
        isDelActivated = false;
        //清空待删除ID集合
        delItemIDs.clear();
        //设置全部item为选中状态
        for (int i = 0; i < items.size(); i++) {
            itemSelectedStates.set(i, false);
        }
        //设置cardNote样式为未选中颜色
        for (int i = 0; i < cardViews.size(); i++) {
            cardViews.get(i).setBackgroundColor(Color.WHITE);
        }
    }

    public int getDelCount() {
        return count;
    }
}