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
import org.bridge.entry.NoteEntry;
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
    private List<NoteEntry> items = new ArrayList<>();
    private List<Integer> delItemIDs = new ArrayList<>();
    private List<Boolean> itemDelStates = new ArrayList<>();
    private Context context;
    private Boolean isDelActivated = false;
    private int count = 0;

    public NoteItemAdapter(Context context, List<NoteEntry> items) {
        this.context = context;
        this.items = items;
        for (int i = 0; i < items.size(); i++)
            itemDelStates.add(false);
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position > 0) {
            final NoteEntry noteEntry = items.get(position - 1);
            TextView tvContent = ((NoteItemViewHolder) holder).getTvContent();
            TextView tvPubDate = ((NoteItemViewHolder) holder).getTvPudDate();
            final View cardNote = ((NoteItemViewHolder) holder).getCardNote();
            tvContent.setText(noteEntry.getContent());
            tvPubDate.setText(DateUtil.formatTime(noteEntry.getPubDate()));
            //点击操作
            cardNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "点击了item", Toast.LENGTH_SHORT).show();
                    if (!isDelActivated) {//执行跳转编辑操作
                        ((MainActivity) context).startEditNoteIntent(noteEntry);
                    } else {//执行点选删除操作
                        setDelAction(cardNote, noteEntry);
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
                    setDelAction(cardNote, noteEntry);
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

    private void setDelAction(View cardNote, NoteEntry noteEntry) {
        cardNote.setBackgroundColor(Color.RED);
        delItemIDs.add(noteEntry.getId());
        count++;
        ((MainActivity) context).setDelActionCallback(count);

    }

    private void performDelAction() {

    }
}