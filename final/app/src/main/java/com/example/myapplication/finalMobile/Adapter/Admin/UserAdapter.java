package com.example.myapplication.finalMobile.Adapter.Admin;

import android.annotation.SuppressLint;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.finalMobile.Model.User;
import com.example.myapplication.finalMobile.R;
import com.example.myapplication.finalMobile.databinding.ViewholderUserBinding;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> dataSource;
    private UserAdapterListener listener;

    public UserAdapter(List<User> dataSource) {
        this.dataSource = dataSource;
    }

    public void setListener(UserAdapterListener listener) {
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDataSource(List<User> users) {
        this.dataSource = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (dataSource.isEmpty()) {
            return 0;
        }
        return dataSource.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView, emailTextView, phoneTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ViewholderUserBinding viewholderUserBinding = ViewholderUserBinding.bind(itemView);

            usernameTextView = viewholderUserBinding.usernameTextView;
            emailTextView = viewholderUserBinding.emailTextView;
            phoneTextView = viewholderUserBinding.phoneTextView;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(itemView.getContext(), view);
                    popupMenu.inflate(R.menu.user_item_menu);

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int id = item.getItemId();
                            if (id == R.id.viewDetail) {
                                showUserDetailsDialog();
                                return true;
                            }
                            if (id == R.id.userEdit) {
                                Log.i(UserAdapter.class.getSimpleName() + "1", dataSource.get(getAdapterPosition()).toString());
                                listener.onEditUserClicked(dataSource.get(getAdapterPosition()));
                            }

                            if (id == R.id.userDelete) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                                builder.setTitle("Confirm Delete")
                                        .setMessage("Are you sure you want to delete this item?")
                                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                listener.OnDeleteUser(dataSource.get(getAdapterPosition()));
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .show();
                            }


                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });
        }

        private void showUserDetailsDialog() {
            User user = dataSource.get(getAdapterPosition());

            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle("User Details");
            builder.setMessage("Name:" + user.getDisplayName() + "\n" + "Email:" + user.getEmail() + "\n" + "Phone:" + user.getPhoneNumber() + "\n" + "Last Sign InTime:" + ((user.getLastSignInTime()) == null ? "Chưa đăng nhập" : user.getLastSignInTime().toString()) + "\n" + "Creation Time:" + ((user.getCreationTime().toString())) + "\n");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        public void bind(int position) {
            User user = dataSource.get(position);
            usernameTextView.setText("Name:" + ((user.getDisplayName() != null) ? user.getDisplayName() : "not yet update"));
            emailTextView.setText("Email:" + ((user.getEmail() != null) ? user.getEmail() : "not yet update"));
            phoneTextView.setText("Phone:" + ((user.getPhoneNumber() != null) ? user.getPhoneNumber() : "not yet update"));
        }
    }

    public interface UserAdapterListener {
        void onEditUserClicked(User user);

        void OnDeleteUser(User user);
    }
}
