package a.a.a;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.besome.sketch.beans.AdTestDeviceBean;
import com.besome.sketch.beans.ProjectLibraryBean;
import com.google.android.material.textfield.TextInputLayout;
import com.sketchware.remod.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import mod.hey.studios.util.Helper;

public class Tu extends LinearLayout implements Uu, View.OnClickListener {
    private TestDeviceAdapter adapter;
    private ArrayList<AdTestDeviceBean> testDevices;

    public Tu(Context context) {
        super(context);
        testDevices = new ArrayList<>();
        initialize(context);
    }

    private String getCurrentDeviceId() {
        return a(Settings.Secure.getString(getContext().getContentResolver(), "android_id")).toUpperCase();
    }

    @Override
    public String getDocUrl() {
        return "https://docs.sketchware.io/docs/admob-adding-test-device.html";
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.layout_set_test_device) {
            showAddTestDeviceDialog();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    @Override
    public void setData(ProjectLibraryBean projectLibraryBean) {
        testDevices = projectLibraryBean.testDevices;
        adapter.notifyDataSetChanged();
    }

    private class TestDeviceAdapter extends RecyclerView.Adapter<TestDeviceAdapter.ViewHolder> {
        private class ViewHolder extends RecyclerView.ViewHolder {
            public final TextView deviceId;
            public final ImageView delete;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                deviceId = itemView.findViewById(R.id.tv_device_id);
                delete = itemView.findViewById(R.id.img_delete);
                delete.setOnClickListener(v -> {
                    showDeleteTestDeviceDialog(getLayoutPosition());
                });
            }
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.deviceId.setText(testDevices.get(position).deviceId);
        }

        @Override
        public int getItemCount() {
            return testDevices.size();
        }

        @Override
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_library_setting_admob_test_device_item, parent, false));
        }
    }

    private void initialize(Context context) {
        wB.a(context, this, R.layout.manage_library_admob_test_device);
        gB.b(this, 600, 200, null);
        ((TextView) findViewById(R.id.tv_set_test_device)).setText(xB.b().a(getContext(), R.string.design_library_admob_button_set_test_device));
        findViewById(R.id.layout_set_test_device).setOnClickListener(this);
        RecyclerView testDevices = findViewById(R.id.list_test_device);
        testDevices.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new TestDeviceAdapter();
        testDevices.setAdapter(adapter);
    }

    @Override
    public void a(ProjectLibraryBean projectLibraryBean) {
        projectLibraryBean.testDevices = testDevices;
    }

    private String a(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes());
            byte[] digest = messageDigest.digest();
            StringBuilder stringBuffer = new StringBuilder();
            for (byte b : digest) {
                stringBuffer.append(String.format("%02X", b & 0xff));
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAddTestDeviceDialog() {
        aB dialog = new aB((Activity) getContext());
        dialog.b(xB.b().a(getContext(), R.string.design_library_admob_dialog_set_test_device_title));
        dialog.a(R.drawable.add_96_blue);
        View content = wB.a(getContext(), R.layout.manage_library_setting_admob_test_device_add);
        EditText edDeviceId = content.findViewById(R.id.ed_device_id);
        ((TextInputLayout) content.findViewById(R.id.ti_device_id)).setHint(xB.b().a(getContext(), R.string.design_library_admob_dialog_set_test_device_hint_device_id));
        SB validator = new SB(getContext(), content.findViewById(R.id.ti_device_id), 1, 100);
        edDeviceId.setText(getCurrentDeviceId());
        edDeviceId.setPrivateImeOptions("defaultInputmode=english;");
        dialog.a(content);
        dialog.b(xB.b().a(getContext(), R.string.common_word_add), v -> {
            if (validator.b()) {
                String deviceId = edDeviceId.getText().toString();
                for (AdTestDeviceBean device : testDevices) {
                    if (device.deviceId.equals(deviceId)) {
                        bB.a(getContext(), xB.b().a(getContext(), R.string.design_library_admob_dialog_set_test_device_warning_duplicated), 0).show();
                        return;
                    }
                }
                testDevices.add(new AdTestDeviceBean(deviceId));
                adapter.notifyItemInserted(testDevices.size() - 1);
                dialog.dismiss();
            } else {
                edDeviceId.requestFocus();
            }
        });
        dialog.a(xB.b().a(getContext(), R.string.common_word_cancel), Helper.getDialogDismissListener(dialog));
        dialog.show();
    }

    private void showDeleteTestDeviceDialog(int index) {
        aB dialog = new aB((Activity) getContext());
        dialog.b(xB.b().a(getContext(), R.string.design_library_admob_dialog_delete_test_device_title));
        dialog.a(R.drawable.delete_96);
        dialog.a(xB.b().a(getContext(), R.string.design_library_admob_dialog_confirm_delete_test_device));
        dialog.b(xB.b().a(getContext(), R.string.common_word_delete), v -> {
            testDevices.remove(index);
            adapter.notifyItemRemoved(index);
            bB.a(getContext(), xB.b().a(getContext(), R.string.common_message_complete_delete), 0).show();
            dialog.dismiss();
        });
        dialog.a(xB.b().a(getContext(), R.string.common_word_cancel), Helper.getDialogDismissListener(dialog));
        dialog.show();
    }
}
